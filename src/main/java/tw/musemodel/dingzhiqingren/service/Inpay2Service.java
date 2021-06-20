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
import org.springframework.stereotype.Service;
import tw.com.ecpay.ecpg.PaymentRequest;
import tw.com.ecpay.ecpg.PaymentResponse;
import tw.com.ecpay.ecpg.TokenRequest;
import tw.com.ecpay.ecpg.TokenRequest.Data.CardInfo;
import tw.com.ecpay.ecpg.TokenRequest.Data.ConsumerInfo;
import tw.com.ecpay.ecpg.TokenRequest.Data.OrderInfo;
import tw.com.ecpay.ecpg.TokenRequest.RqHeader;
import tw.com.ecpay.ecpg.TokenResponse;

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
	 * 发出 http post 请求并取得响应。
	 *
	 * @param requestBody 请求内容
	 * @return 响应内容
	 */
	@SuppressWarnings("ConvertToTryWithResources")
	private String httpPost(final String requestBody) {
		String responseBody;
		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(new URIBuilder(
				INPAY2_GET_TOKEN_BY_TRADE
			).build());
			httpPost.setHeader(new BasicHeader(
				"Content-Type",
				"application/json"
			));
			httpPost.setEntity(new StringEntity(
				JSON_MAPPER.writeValueAsString(requestBody)
			));

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

	/**
	 * 建立交易
	 *
	 * @return
	 */
	public String createPayment(final String payToken) {
		final Long currentTimeMillis = System.currentTimeMillis();
		String merchantTradeNo = "";//TODO
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
					"生成建立交易请求对象时发生序列化异常！\n%s.createPayment(\n\tString payToken = {}\n);",
					getClass().getName()
				),
				payToken,
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
					"生成建立交易请求对象时发生加密异常！\n%s.createPayment(\n\tString payToken = {}\n);",
					getClass().getName()
				),
				payToken,
				exception
			);
			return null;
		}

		String responseBody;
		try {
			responseBody = httpPost(
				JSON_MAPPER.writeValueAsString(paymentRequest)
			);
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"建立建立交易请求时发生序列化异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				jsonProcessingException
			);
			return null;
		}

		try {
			return decrypt(JSON_MAPPER.readValue(
				responseBody,
				PaymentResponse.class
			).getData());
		} catch (JsonProcessingException jsonProcessingException) {
			LOGGER.info(
				String.format(
					"生成建立交易响应对象时发生反序列化异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				jsonProcessingException
			);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException exception) {
			LOGGER.info(
				String.format(
					"生成建立交易响应对象时发生解密异常！\n%s.getTokenByTrade();",
					getClass().getName()
				),
				exception
			);
		}

		return null;
	}

	/**
	 * 取得厂商验证码
	 *
	 * @return 绿界回传厂商验证码对象
	 */
	public String getTokenByTrade() {
		final Long currentTimeMillis = System.currentTimeMillis();
		TokenRequest.Data tokenRequestData = new TokenRequest.Data(
			INPAY2_MERCHANT_ID,
			(short) 0,
			(short) 0
		);

		tokenRequestData.setOrderInfo(tokenRequestData.new OrderInfo(
			"2021/06/18 02:46:00",
			String.format("%s", currentTimeMillis.toString()),
			300,
			"https://musemodel.ngrok.io/poc/returnUrl.asp",
			"交易描述",
			"商品名称"
		));

		CardInfo cardInfo = tokenRequestData.new CardInfo(
			"https://musemodel.ngrok.io/poc/orderResultUrl.asp"//TODO
		);
		cardInfo.setPeriodAmount((short) 300);
		cardInfo.setPeriodType("M");
		cardInfo.setFrequency((short) 1);
		cardInfo.setExecTimes((short) 99);
		cardInfo.setPeriodReturnUrl(
			"https://musemodel.ngrok.io/poc/periodReturnUrl.asp"//TODO
		);
		tokenRequestData.setCardInfo(cardInfo);

		ConsumerInfo consumerInfo = tokenRequestData.new ConsumerInfo();
		consumerInfo.setMerchantMemberId("test123456");//TODO
		tokenRequestData.setConsumerInfo(consumerInfo);

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
