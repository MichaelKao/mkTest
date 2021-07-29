package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import me.line.notifybot.OAuthAccessToken;
import me.line.notifybot.OAuthAuthorizationCode;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.musemodel.dingzhiqingren.entity.LineNotifyAuthentication;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.LineNotifyAuthenticationRepository;

/**
 * Line Messaging API 服务层
 *
 * @author p@musemodel.tw
 */
@Service
public class LineMessagingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LineMessagingService.class);

	/**
	 * Access Token
	 */
	private static final String LINE_NOTIFY_ACCESS_TOKEN = System.getenv("LINE_NOTIFY_ACCESS_TOKEN");

	/**
	 * Client ID
	 */
	private static final String LINE_NOTIFY_CLIENT_ID = System.getenv("LINE_NOTIFY_CLIENT_ID");

	/**
	 * Client Secret
	 */
	private static final String LINE_NOTIFY_CLIENT_SECRET = System.getenv("LINE_NOTIFY_CLIENT_SECRET");

	/**
	 * Host name for authentication API endpoint
	 */
	private static final String LINE_NOTIFY_ENDPOINT_HOST = System.getenv("LINE_NOTIFY_ENDPOINT_HOST");

	/**
	 * LINE Notify 服务。
	 *
	 * @param accessToken 访问令牌
	 * @param message 1000 characters max
	 */
	private static void notify(String accessToken, String message) {
		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(
				new URIBuilder("https://notify-api.line.me/api/notify").
					setParameters(
						new BasicNameValuePair(
							"message",
							message
						)
					).
					build()
			);
			httpPost.setHeader(
				"Content-Type",
				"application/x-www-form-urlencoded"
			);
			httpPost.setHeader(
				"Authorization",
				String.format(
					"Bearer %s",
					accessToken
				)
			);
			closeableHttpClient.execute(httpPost);
			closeableHttpClient.close();
		} catch (URISyntaxException | IOException exception) {
			LOGGER.info(
				String.format(
					"%s.notify(\n\tString message = {}\n);",
					LineMessagingService.class
				),
				message,
				exception
			);
		}
	}

	/**
	 * 用户号服务
	 */
	@Autowired
	private LoverService loverService;

	/**
	 * LINE 通知数据访问对象
	 */
	@Autowired
	private LineNotifyAuthenticationRepository lineNotifyAuthenticationRepository;

	/**
	 * 重定向 URI
	 */
	public static URI redirectUri;

	/**
	 * 初始化重定向 URI
	 */
	static {
		try {
			redirectUri = new URIBuilder().
				setScheme("https").
				setHost(Servant.LOCALHOST).
				setPath("/notify-bot.line.me/authorize.asp").
				build();
		} catch (URISyntaxException uriSyntaxException) {
			LOGGER.info(
				String.format(
					"无法解析生成的重定向 URI\n%s#redirectUri",
					URI.class
				),
				uriSyntaxException
			);
		}
	}

	/**
	 * LINE Notify 服务。
	 *
	 * @param message 最多 1000 个字符
	 */
	public static void notify(String message) {
		notify(LINE_NOTIFY_ACCESS_TOKEN, message);
	}

	/**
	 * LINE Notify 服务。
	 *
	 * @param sucker 接收人
	 * @param message 最多 1000 个字符
	 */
	@Transactional(readOnly = true)
	public static void notify(Lover sucker, String message) {
		if (Objects.isNull(sucker)) {
			throw new IllegalArgumentException("接收人不得为空值❗️");
		}

		final String lineNotifyAccessToken = sucker.getLineNotifyAccessToken();
		if (Objects.isNull(lineNotifyAccessToken)) {
			throw new NullPointerException("访问令牌不得为空值❗️");
		}

		notify(lineNotifyAccessToken, message);
	}

	/**
	 * 请求授权码。
	 *
	 * @param sessionId HTTP session 识别键
	 * @param sucker 情人
	 * @return 重定向网址
	 */
	public URI requestNotifyIntegration(String sessionId, Lover sucker) {
		OAuthAuthorizationCode oAuthAuthorizationCode = new OAuthAuthorizationCode(
			LINE_NOTIFY_CLIENT_ID,
			redirectUri,
			String.format(
				"%s%d",
				sessionId,
				System.currentTimeMillis()
			)
		);

		lineNotifyAuthenticationRepository.saveAndFlush(
			new LineNotifyAuthentication(
				oAuthAuthorizationCode.getState(),
				sucker
			)
		);

		URI uri;
		try {
			uri = new URIBuilder().
				setScheme(Servant.SCHEME_HTTPS).
				setHost(LINE_NOTIFY_ENDPOINT_HOST).
				setPath("/oauth/authorize").
				setParameters(
					new BasicNameValuePair(
						"response_type",
						oAuthAuthorizationCode.
							getResponseType()
					),
					new BasicNameValuePair(
						"client_id",
						oAuthAuthorizationCode.
							getClientId()
					),
					new BasicNameValuePair(
						"redirect_uri",
						oAuthAuthorizationCode.
							getRedirectUri().
							toString()
					),
					new BasicNameValuePair(
						"scope",
						oAuthAuthorizationCode.
							getScope()
					),
					new BasicNameValuePair(
						"state",
						oAuthAuthorizationCode.
							getState()
					),
					new BasicNameValuePair(
						"response_mode",
						oAuthAuthorizationCode.
							getResponseMode()
					)
				).
				build();
		} catch (URISyntaxException uriSyntaxException) {
			LOGGER.info(
				String.format(
					"生成的重定向 URI\n%s#redirectUri 无法解析",
					URI.class
				),
				uriSyntaxException
			);
			uri = null;
		}
		return uri;
	}

	/**
	 * 请求访问令牌。
	 *
	 * @param oAuthAuthorizationCode
	 * @return 杰森格式对象
	 */
	@Transactional
	public JavaScriptObjectNotation requestNotifyAccessToken(OAuthAuthorizationCode oAuthAuthorizationCode) {
		if (Objects.nonNull(oAuthAuthorizationCode.getError())) {
			return new JavaScriptObjectNotation().
				withReason(
					oAuthAuthorizationCode.getErrorDescription()
				).
				withResponse(false);
		}

		String code = oAuthAuthorizationCode.getCode();
		OAuthAccessToken oAuthAccessToken = new OAuthAccessToken(
			code,
			redirectUri,
			LINE_NOTIFY_CLIENT_ID,
			LINE_NOTIFY_CLIENT_SECRET
		);

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(new URIBuilder().
				setScheme(Servant.SCHEME_HTTPS).
				setHost(LINE_NOTIFY_ENDPOINT_HOST).
				setPath("/oauth/token").
				setParameters(
					new BasicNameValuePair(
						"grant_type",
						oAuthAccessToken.
							getGrantType()
					),
					new BasicNameValuePair(
						"code",
						oAuthAccessToken.
							getCode()
					),
					new BasicNameValuePair(
						"redirect_uri",
						oAuthAccessToken.
							getRedirectUri().
							toString()
					),
					new BasicNameValuePair(
						"client_id",
						oAuthAccessToken.
							getClientId()
					),
					new BasicNameValuePair(
						"client_secret",
						oAuthAccessToken.
							getClientSecret()
					)
				).
				build()
			);
			httpPost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
			switch (closeableHttpResponse.getStatusLine().getStatusCode()) {
				case HttpStatus.SC_OK:
					break;
				default:
					return new JavaScriptObjectNotation().
						withReason(
							Servant.JSON_MAPPER.readValue(
								closeableHttpResponse.
									getEntity().
									getContent(),
								OAuthAccessToken.class
							).getMessage()
						).
						withResponse(false);
			}

			Lover sucker = lineNotifyAuthenticationRepository.
				findOneByState(
					oAuthAuthorizationCode.getState()
				).
				orElseThrow().
				getSucker();
			oAuthAccessToken = Servant.JSON_MAPPER.readValue(
				closeableHttpResponse.
					getEntity().
					getContent(),
				OAuthAccessToken.class
			);
			sucker.setLineNotifyAccessToken(
				oAuthAccessToken.getAccessToken()
			);
			lineNotifyAuthenticationRepository.
				deleteBySucker(sucker);//清除此人的请求数据

			loverService.saveLover(sucker);
			return new JavaScriptObjectNotation().
				withResponse(true).
				withResult(oAuthAccessToken);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getLocalizedMessage()).
				withResponse(false);
		}
	}
}
