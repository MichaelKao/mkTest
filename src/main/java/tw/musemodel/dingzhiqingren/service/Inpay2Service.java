package tw.musemodel.dingzhiqingren.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tw.com.ecpay.ecpg.ApplyToken;
import tw.com.ecpay.ecpg.ApplyToken.Data.CardInfo;
import tw.com.ecpay.ecpg.ApplyToken.Data.ConsumerInfo;
import tw.com.ecpay.ecpg.ApplyToken.Data.OrderInfo;
import tw.com.ecpay.ecpg.ApplyToken.RqHeader;
import tw.com.ecpay.ecpg.ReplyToken;
import tw.musemodel.dingzhiqingren.repository.ActivationRepository;
import tw.musemodel.dingzhiqingren.repository.CountryRepository;
import tw.musemodel.dingzhiqingren.repository.LineUserProfileRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.UserRepository;

/**
 * 服务层：情人
 *
 * @author p@musemodel.tw
 */
@Service
public class Inpay2Service {

	private final static Logger LOGGER = LoggerFactory.getLogger(Inpay2Service.class);

	private final static JsonMapper JSON_MAPPER = new JsonMapper();

	private final static String INPAY2_ALGORITHM = "AES";

	private final static String INPAY2_API_VERSION = System.getenv("INPAY2_API_VERSION");

	private final static String INPAY2_GET_TOKEN_BY_TRADE = System.getenv("INPAY2_GET_TOKEN_BY_TRADE");

	private final static String INPAY2_HASH_IV = System.getenv("INPAY2_HASH_IV");

	private final static String INPAY2_HASH_KEY = System.getenv("INPAY2_HASH_KEY");

	private final static String INPAY2_MERCHANT_ID = System.getenv("INPAY2_MERCHANT_ID");

	private final static String INPAY2_TRANSFORMATION = "AES/CBC/PKCS5Padding";

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Servant servant;

	@Autowired
	private ActivationRepository activationRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private LineUserProfileRepository lineUserProfileRepository;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private UserRepository userRepository;

	private IvParameterSpec getIvParameterSpec() throws UnsupportedEncodingException {
		StringBuilder stringBuilder = new StringBuilder(16);
		stringBuilder.append(INPAY2_HASH_IV);
		while (stringBuilder.length() < 16) {
			stringBuilder.append("0");
		}
		if (stringBuilder.length() > 16) {
			stringBuilder.setLength(16);
		}
		return new IvParameterSpec(stringBuilder.toString().getBytes(
			Servant.UTF_8.toString()
		));
	}

	private SecretKeySpec getSecretKeySpec() throws UnsupportedEncodingException {
		StringBuilder stringBuilder = new StringBuilder(16);
		stringBuilder.append(INPAY2_HASH_KEY);
		while (stringBuilder.length() < 16) {
			stringBuilder.append("0");
		}
		if (stringBuilder.length() > 16) {
			stringBuilder.setLength(16);
		}
		return new SecretKeySpec(
			stringBuilder.toString().getBytes(
				Servant.UTF_8.toString()
			),
			INPAY2_ALGORITHM
		);
	}

	public String decrypt(final String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(INPAY2_TRANSFORMATION);
		cipher.init(
			Cipher.DECRYPT_MODE,
			getSecretKeySpec(),
			getIvParameterSpec()
		);

		return URLDecoder.decode(
			new String(cipher.doFinal(
				Base64.getDecoder().decode(data)
			)),
			Servant.UTF_8.toString()
		);
	}

	public String encrypt(final String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String string = URLEncoder.encode(
			data,
			Servant.UTF_8.toString()
		);

		Cipher cipher = Cipher.getInstance(INPAY2_TRANSFORMATION);
		cipher.init(
			Cipher.ENCRYPT_MODE,
			getSecretKeySpec(),
			getIvParameterSpec()
		);

		return Base64.getEncoder().encodeToString(cipher.doFinal(
			string.getBytes(
				Servant.UTF_8.toString()
			)
		));
	}

	public String token() throws Exception {
		final Long currentTimeMillis = System.currentTimeMillis();
		ApplyToken.Data applyTokenData = new ApplyToken.Data(
			INPAY2_MERCHANT_ID,
			(short) 0,
			(short) 0
		);

		applyTokenData.setOrderInfo(applyTokenData.new OrderInfo(
			"2021/06/18 02:46:00",
			String.format("%s", currentTimeMillis.toString()),
			300,
			"https://musemodel.ngrok.io/poc/returnUrl.asp",
			"交易描述",
			"商品名称"
		));

		CardInfo cardInfo = applyTokenData.new CardInfo(
			"https://musemodel.ngrok.io/poc/orderResultUrl.asp"
		);
		cardInfo.setPeriodAmount((short) 300);
		cardInfo.setPeriodType("M");
		cardInfo.setFrequency((short) 1);
		cardInfo.setExecTimes((short) 99);
		cardInfo.setPeriodReturnUrl(
			"https://musemodel.ngrok.io/poc/periodReturnUrl.asp"
		);
		applyTokenData.setCardInfo(cardInfo);

		ConsumerInfo consumerInfo = applyTokenData.new ConsumerInfo();
		consumerInfo.setMerchantMemberId("test123456");
		applyTokenData.setConsumerInfo(consumerInfo);

		String applyTokenDataString = JSON_MAPPER.writeValueAsString(
			applyTokenData
		);
		LOGGER.debug(
			"取得厂商验证码(服务器)\n{}",
			applyTokenDataString
		);

		ApplyToken applyToken = new ApplyToken();

		RqHeader rqHeader = applyToken.new RqHeader();
		rqHeader.setTimestamp(currentTimeMillis / 1000);
		rqHeader.setRevision(INPAY2_API_VERSION);

		applyToken.setMerchantId(INPAY2_MERCHANT_ID);
		applyToken.setRqHeader(rqHeader);
		applyToken.setData(encrypt(applyTokenDataString));

		LOGGER.debug(
			"向绿界取得厂商验证码\n{}",
			JSON_MAPPER.writeValueAsString(applyToken)
		);

		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(new URIBuilder(
			INPAY2_GET_TOKEN_BY_TRADE
		).build());
		httpPost.setHeader(new BasicHeader(
			"Content-Type",
			"application/json"
		));
		httpPost.setEntity(new StringEntity(
			JSON_MAPPER.writeValueAsString(applyToken)
		));

		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
		HttpEntity httpEntity = closeableHttpResponse.getEntity();
		if (Objects.isNull(httpEntity)) {
			return null;
		}
		String responseBody = IOUtils.toString(
			httpEntity.getContent(),
			Servant.UTF_8
		);
		closeableHttpResponse.close();
		closeableHttpClient.close();

		return decrypt(JSON_MAPPER.readValue(
			responseBody,
			ReplyToken.class
		).getData());
	}

	public String applyToken() throws JsonProcessingException {
		ApplyToken applyToken = new ApplyToken();
		RqHeader rqHeader = applyToken.new RqHeader();
		rqHeader.setTimestamp(System.currentTimeMillis() / 1000);
		rqHeader.setRevision(INPAY2_API_VERSION);

		applyToken.setMerchantId(INPAY2_MERCHANT_ID);
		applyToken.setRqHeader(rqHeader);
		applyToken.setData("{\"MerchantID\":\"3002607\",\"RqHeader\":{\"Timestamp\":1525168923,\"Revision\":\"1.0.0\"},\"Data\":\"7woM9RorZKAtXJRVccAb0qhHYm+5lnlhBzyfh5EZdNck7PacNsRHgv/Jvp//ajJidqcQcs0UmAgPQVjXQHeziw==\"}");

		return JSON_MAPPER.writeValueAsString(applyToken);
	}

	public String replyToken() throws JsonProcessingException {
		ReplyToken replyToken = JSON_MAPPER.readValue(
			"{\"MerchantID\":\"3002607\",\"RpHeader\":{\"Timestamp\":1525169058},\"TransCode\":1,\"TransMsg\":\"Success\",\"Data\":\"7woM9RorZKAtXJRVccAb0qhHYm+5lnlhBzyfh5EZdNck7PacNsRHgv/Jvp//ajJidqcQcs0UmAgPQVjXQHeziw==\"}",
			ReplyToken.class
		);
		return JSON_MAPPER.writeValueAsString(replyToken);
	}
}
