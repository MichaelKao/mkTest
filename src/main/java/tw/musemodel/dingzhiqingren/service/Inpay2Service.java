package tw.musemodel.dingzhiqingren.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
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
import org.springframework.stereotype.Service;
import tw.com.ecpay.ecpg.PaymentRequest;
import tw.com.ecpay.ecpg.PaymentResponse;
import tw.com.ecpay.ecpg.TokenRequest;
import tw.com.ecpay.ecpg.TokenRequest.Data.CardInfo;
import tw.com.ecpay.ecpg.TokenRequest.Data.ConsumerInfo;
import tw.com.ecpay.ecpg.TokenRequest.Data.OrderInfo;
import tw.com.ecpay.ecpg.TokenRequest.RqHeader;
import tw.com.ecpay.ecpg.TokenResponse;
import tw.musemodel.dingzhiqingren.entity.LuJie;
import tw.musemodel.dingzhiqingren.repository.LuJieRepository;

/**
 * 服务层：站内付 2.0
 *
 * @author p@musemodel.tw
 */
@Service
public class Inpay2Service {

	private final static Logger LOGGER = LoggerFactory.getLogger(Inpay2Service.class);

	private final static JsonMapper JSON_MAPPER = new JsonMapper();

	private final static String INPAY2ENDPOINT_CREATE_PAYMENT = System.getenv("INPAY2ENDPOINT_CREATE_PAYMENT");

	private final static String INPAY2ENDPOINT_GET_TOKEN_BY_TRADE = System.getenv("INPAY2ENDPOINT_GET_TOKEN_BY_TRADE");

	private final static String INPAY2_ALGORITHM = "AES";

	private final static String INPAY2_API_VERSION = System.getenv("INPAY2_API_VERSION");

	private final static String INPAY2_HASH_IV = System.getenv("INPAY2_HASH_IV");

	private final static String INPAY2_HASH_KEY = System.getenv("INPAY2_HASH_KEY");

	private final static String INPAY2_MERCHANT_ID = System.getenv("INPAY2_MERCHANT_ID");

	private final static String INPAY2_TRANSFORMATION = "AES/CBC/PKCS5Padding";

	private static final SimpleDateFormat SIMPLEDATEFORMAT_MERCHANTTRADEDATE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private static final SimpleDateFormat SIMPLEDATEFORMAT_MERCHANTTRADENO = new SimpleDateFormat("yyww'YOUNG'");

	@Autowired
	private LuJieRepository luJieRepository;

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

	/**
	 * 产生厂商交易时间。
	 *
	 * @param timeMillis 自 1970 年 1 月 1 日格林威治标准时间 00:00:00 以来的毫秒数
	 * @return 字符串
	 */
	private String generateMerchantTradeDate(final long timeMillis) {
		return SIMPLEDATEFORMAT_MERCHANTTRADEDATE.format(new Date(
			timeMillis
		));
	}

	/**
	 * 产生特店交易编号。
	 *
	 * @param timeMillis 自 1970 年 1 月 1 日格林威治标准时间 00:00:00 以来的毫秒数
	 * @return 字符串
	 */
	private String generateMerchantTradeNo(final long timeMillis) {
		String merchantTradeNo = null;
		while (Objects.isNull(merchantTradeNo)) {
			merchantTradeNo = SIMPLEDATEFORMAT_MERCHANTTRADENO.format(
				new Date(timeMillis)
			).concat(Long.
				toHexString(timeMillis).
				toUpperCase()
			);
			if (luJieRepository.countByMerchantTradeNo(merchantTradeNo) > 0) {
				merchantTradeNo = null;
			}
		}
		return merchantTradeNo;
	}

	/**
	 * 发出 http post 请求并取得响应。
	 *
	 * @param requestBody 请求内容
	 * @return 响应内容
	 */
	@SuppressWarnings("ConvertToTryWithResources")
	private String httpPost(final String uri, final String requestBody) {
		String responseBody;
		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(
				new URIBuilder(uri).build()
			);
			httpPost.setHeader(new BasicHeader(
				"Content-Type",
				"application/json"
			));
			httpPost.setEntity(new StringEntity(requestBody));

			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (Objects.isNull(httpEntity)) {
				LOGGER.info(
					String.format(
						"请求厂商验证码时发生不明的异常！\n%s.httpPost(\n\tString requestBody = {}\n);",
						getClass().getName()
					),
					requestBody
				);
				return null;
			}
			responseBody = IOUtils.toString(
				httpEntity.getContent(),
				Servant.UTF_8
			);

			closeableHttpResponse.close();
			closeableHttpClient.close();
		} catch (URISyntaxException | IllegalArgumentException | UnsupportedEncodingException exception) {
			LOGGER.info(
				String.format(
					"建立 http post 请求时发生异常！\n%s.httpPost(\n\tString requestBody = {}\n);",
					getClass().getName()
				),
				requestBody,
				exception
			);
			return null;
		} catch (IOException ioException) {
			LOGGER.info(
				String.format(
					"输入输出异常！\n%s.httpPost(\n\tString requestBody = {}\n);",
					getClass().getName()
				),
				requestBody,
				ioException
			);
			return null;
		}
		return responseBody;
	}

	/**
	 * 解密。
	 *
	 * @param data 解密前密文
	 * @return 解密后数据
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
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

	/**
	 * 加密。
	 *
	 * @param data 加密前数据
	 * @return 加密后密文
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
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

	/**
	 * 建立交易
	 *
	 * @param payToken 支付令牌
	 * @param session 分配给会话的标识符
	 * @return 绿界回传支付令牌对象字符串
	 */
	public String createPayment(final String payToken, final HttpSession session) {
		final Long currentTimeMillis = System.currentTimeMillis();
		String merchantTradeNo = luJieRepository.findTop1BySessionIdOrderByIdDesc(
			session.getId()
		).getMerchantTradeNo();
		LOGGER.debug(
			String.format(
				"建立交易 輸入參數\n%s.createPayment(\n\tString payToken = {}\n\tHttpSession session = {}\n);\n交易編號：{}",
				getClass().getName()
			),
			payToken,
			session,
			merchantTradeNo
		);
		PaymentRequest.Data paymentRequestData = new PaymentRequest.Data(
			INPAY2_MERCHANT_ID,
			payToken,
			merchantTradeNo
		);

		String paymentRequestDataString;
		try {
			paymentRequestDataString = JSON_MAPPER.writeValueAsString(
				paymentRequestData
			);
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"生成建立交易请求对象时发生序列化异常！\n%s.createPayment(\n\tString payToken = {}\n\tHttpSession session = {}\n);",
					getClass().getName()
				),
				payToken,
				session,
				jsonProcessingException
			);
			return null;
		}

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setMerchantId(INPAY2_MERCHANT_ID);
		paymentRequest.setRqHeader(paymentRequest.new RqHeader(
			currentTimeMillis / 1000,
			INPAY2_API_VERSION
		));
		try {
			paymentRequest.setData(encrypt(paymentRequestDataString));
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException exception) {
			LOGGER.info(
				String.format(
					"生成建立交易请求对象时发生加密异常！\n%s.createPayment(\n\tString payToken = {}\n\tHttpSession session = {}\n);",
					getClass().getName()
				),
				payToken,
				session,
				exception
			);
			return null;
		}

		String responseBody;
		try {
			responseBody = httpPost(
				INPAY2ENDPOINT_CREATE_PAYMENT,
				JSON_MAPPER.writeValueAsString(paymentRequest)
			);
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"建立交易请求时发生序列化异常！\n%s.createPayment(\n\tString payToken = {}\n\tHttpSession session = {}\n);",
					getClass().getName()
				),
				payToken,
				session,
				jsonProcessingException
			);
			return null;
		}

		try {
			LOGGER.info(
				String.format(
					"建立交易！\n%s.createPayment(\n\tString payToken = {}\n\tHttpSession session = {}\n);",
					getClass().getName()
				),
				payToken,
				session,
				decrypt(JSON_MAPPER.readValue(
					responseBody,
					PaymentResponse.class
				).getData())
			);
			return decrypt(JSON_MAPPER.readValue(
				responseBody,
				PaymentResponse.class
			).getData());
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"生成建立交易响应对象时发生反序列化异常！\n%s.createPayment(\n\tString payToken = {}\n\tHttpSession session = {}\n);",
					getClass().getName()
				),
				payToken,
				session,
				jsonProcessingException
			);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException exception) {
			LOGGER.info(
				String.format(
					"生成建立交易响应对象时发生解密异常！\n%s.createPayment(\n\tString payToken = {}\n\tHttpSession session = {}\n);",
					getClass().getName()
				),
				payToken,
				session,
				exception
			);
		}

		return null;
	}

	/**
	 * 取得厂商验证码
	 *
	 * @param session 分配给会话的标识符
	 * @return 绿界回传厂商验证码对象字符串
	 * @throws com.fasterxml.jackson.core.JsonProcessingException
	 */
	public String getTokenByTrade(final HttpSession session) throws JsonProcessingException {
		final Long currentTimeMillis = System.currentTimeMillis();
		final String merchantTradeNo = generateMerchantTradeNo(currentTimeMillis);
		LuJie luJie = new LuJie();
		luJie.setSessionId(session.getId());
		luJie.setMerchantTradeNo(merchantTradeNo);
		luJie = luJieRepository.saveAndFlush(luJie);

		TokenRequest.Data tokenRequestData = new TokenRequest.Data(
			INPAY2_MERCHANT_ID,
			(short) 0,
			(short) 0
		);

		tokenRequestData.setOrderInfo(tokenRequestData.new OrderInfo(
			generateMerchantTradeDate(currentTimeMillis),
			luJie.getMerchantTradeNo(),//TODO：SimpleDateFormat
			1688,//TODO：JPA
			String.format(
				"https://%s/inpay2/return.asp",
				Servant.LOCALHOST
			),
			"交易描述",//TODO：JPA
			"商品名称"//TODO：JPA
		));

		CardInfo cardInfo = tokenRequestData.new CardInfo(
			String.format(
				"https://%s/inpay2/orderResult.asp",
				Servant.LOCALHOST
			)
		);
		cardInfo.setPeriodAmount((short) 1688);
		cardInfo.setPeriodType("M");
		cardInfo.setFrequency((short) 1);
		cardInfo.setExecTimes((short) 99);
		cardInfo.setPeriodReturnUrl(
			String.format(
				"https://%s/inpay2/periodReturn.asp",
				Servant.LOCALHOST
			)
		);
		tokenRequestData.setCardInfo(cardInfo);

		ConsumerInfo consumerInfo = tokenRequestData.new ConsumerInfo();
		consumerInfo.setMerchantMemberId("test123456");//TODO：JPA
		//consumerInfo.setPhone("0930940238");
		tokenRequestData.setConsumerInfo(consumerInfo);
		LOGGER.debug(
			String.format(
				"生成厂商验证码请求对象！\n%s.getTokenByTrade();\n{}\n交易編號：{}",
				getClass().getName()
			),
			JSON_MAPPER.writeValueAsString(
				tokenRequestData
			),
			merchantTradeNo
		);

		String tokenRequestDataString;
		try {
			tokenRequestDataString = JSON_MAPPER.writeValueAsString(
				tokenRequestData
			);
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"生成厂商验证码请求对象时发生序列化异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				jsonProcessingException
			);
			return null;
		}

		TokenRequest tokenRequest = new TokenRequest();

		tw.com.ecpay.ecpg.TokenRequest.RqHeader rqHeader = tokenRequest.new RqHeader();
		rqHeader.setTimestamp(currentTimeMillis / 1000);
		rqHeader.setRevision(INPAY2_API_VERSION);

		tokenRequest.setMerchantId(INPAY2_MERCHANT_ID);
		tokenRequest.setRqHeader(rqHeader);
		try {
			tokenRequest.setData(encrypt(tokenRequestDataString));
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException exception) {
			LOGGER.info(
				String.format(
					"生成厂商验证码请求对象时发生加密异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				exception
			);
			return null;
		}

		String responseBody;
		try {
			responseBody = httpPost(
				INPAY2ENDPOINT_GET_TOKEN_BY_TRADE,
				JSON_MAPPER.writeValueAsString(tokenRequest)
			);
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"建立厂商验证码请求时发生序列化异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				jsonProcessingException
			);
			return null;
		}

		try {
			LOGGER.debug(
				String.format(
					"生成厂商验证码请求！\n%s.getTokenByTrade();\n{}",
					getClass().getName()
				),
				decrypt(JSON_MAPPER.readValue(
					responseBody,
					TokenResponse.class
				).getData())
			);
			return decrypt(JSON_MAPPER.readValue(
				responseBody,
				TokenResponse.class
			).getData());
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"生成厂商验证码响应对象时发生反序列化异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				jsonProcessingException
			);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException exception) {
			LOGGER.info(
				String.format(
					"生成厂商验证码响应对象时发生解密异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				exception
			);
		}
		return null;
	}
}
