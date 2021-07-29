package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import me.line.notifybot.api.Notify;
import me.line.notifybot.oauth.AccessToken;
import me.line.notifybot.oauth.AuthorizationCode;
import me.line.notifybot.api.Revoke;
import me.line.notifybot.api.Status;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
	 * Host name for authentication API endpoint
	 */
	private static final String LINE_NOTIFY_API_ENDPOINT_HOST = System.getenv("LINE_NOTIFY_API_ENDPOINT_HOST");

	/**
	 * Host name for notification API endpoint
	 */
	private static final String LINE_NOTIFY_BOT_ENDPOINT_HOST = System.getenv("LINE_NOTIFY_BOT_ENDPOINT_HOST");

	/**
	 * Client ID
	 */
	private static final String LINE_NOTIFY_CLIENT_ID = System.getenv("LINE_NOTIFY_CLIENT_ID");

	/**
	 * Client Secret
	 */
	private static final String LINE_NOTIFY_CLIENT_SECRET = System.getenv("LINE_NOTIFY_CLIENT_SECRET");

	/**
	 * LINE Notify 服务。
	 *
	 * @param accessToken 访问令牌
	 * @param message 1000 characters max
	 */
	@SuppressWarnings("ConvertToTryWithResources")
	private static JavaScriptObjectNotation notify(String accessToken, String message) {
		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(
				new URIBuilder().
					setScheme(Servant.SCHEME_HTTPS).
					setHost(LINE_NOTIFY_API_ENDPOINT_HOST).
					setPath("/api/notify").
					setParameters(
						new BasicNameValuePair(
							"message",
							message
						)
					).
					build()
			);
			httpPost.setHeader(new BasicHeader(
				HttpHeaders.CONTENT_TYPE,
				"application/x-www-form-urlencoded"
			));
			httpPost.setHeader(new BasicHeader(
				HttpHeaders.AUTHORIZATION,
				String.format(
					"Bearer %s",
					accessToken
				)
			));

			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
			Integer statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				closeableHttpResponse.close();

				switch (statusCode) {
					case HttpStatus.SC_BAD_REQUEST:
						return new JavaScriptObjectNotation().
							withReason("Bad request").
							withResponse(false).
							withResult(statusCode);
					case HttpStatus.SC_UNAUTHORIZED:
						return new JavaScriptObjectNotation().
							withReason("Invalid access token").
							withResponse(false).
							withResult(statusCode);
					case HttpStatus.SC_INTERNAL_SERVER_ERROR:
						return new JavaScriptObjectNotation().
							withReason("Failure due to server error").
							withResponse(false).
							withResult(statusCode);
					default:
						return new JavaScriptObjectNotation().
							withReason("Processed over time or stopped").
							withResponse(false).
							withResult(statusCode);
				}
			}
			Notify notify = Servant.JSON_MAPPER.readValue(
				closeableHttpResponse.
					getEntity().
					getContent(),
				Notify.class
			);

			closeableHttpResponse.close();
			closeableHttpClient.close();
			return new JavaScriptObjectNotation().
				withReason(notify.getMessage()).
				withResponse(200 == notify.getStatus()).
				withResult(notify);
		} catch (URISyntaxException | IOException exception) {
			LOGGER.info(
				String.format(
					"%s.notify(\n\t%s accessToken = {},\n\t%s message = {}\n);",
					LineMessagingService.class,
					String.class,
					String.class
				),
				accessToken,
				message,
				exception
			);
			return new JavaScriptObjectNotation().
				withReason(exception.getLocalizedMessage()).
				withResponse(false);
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
	 * LINE Notify 服务；开发人员用。
	 *
	 * @param message 最多 1000 个字符
	 */
	@Transactional(readOnly = true)
	public static void notify(String message) {
		notify(LINE_NOTIFY_ACCESS_TOKEN, message);
	}

	/**
	 * LINE Notify 服务；一般用户号用。
	 *
	 * @param sucker 用户号
	 * @param message 最多 1000 个字符
	 */
	@Transactional(readOnly = true)
	public void notify(Lover sucker, String message) {
		if (Objects.isNull(sucker)) {
			throw new IllegalArgumentException("接收人不得为空值❗️");
		}

		final String lineNotifyAccessToken = sucker.getLineNotifyAccessToken();
		if (Objects.isNull(lineNotifyAccessToken)) {
			throw new NullPointerException("访问令牌不得为空值❗️");
		}

		if (notifyStatus(sucker).isResponse()) {
			notify(lineNotifyAccessToken, message);
		}
	}

	/**
	 * 通过禁用访问令牌访问 API 来撤销通知配置。
	 *
	 * @param sucker 情人
	 * @return 杰森格式对象
	 */
	@SuppressWarnings("ConvertToTryWithResources")
	@Transactional
	public JavaScriptObjectNotation notifyRevoke(Lover sucker) {
		if (Objects.isNull(sucker)) {
			return new JavaScriptObjectNotation().
				withResponse(false).
				withResult(sucker);
		}

		final String lineNotifyAccessToken = sucker.getLineNotifyAccessToken();
		if (Objects.isNull(lineNotifyAccessToken)) {
			return new JavaScriptObjectNotation().
				withResponse(false).
				withResult(sucker);
		}

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(
				new URIBuilder().
					setScheme(Servant.SCHEME_HTTPS).
					setHost(LINE_NOTIFY_API_ENDPOINT_HOST).
					setPath("/api/revoke").
					build()
			);
			httpPost.setHeader(new BasicHeader(
				HttpHeaders.CONTENT_TYPE,
				"application/x-www-form-urlencoded"
			));
			httpPost.setHeader(new BasicHeader(
				HttpHeaders.AUTHORIZATION,
				String.format(
					"Bearer %s",
					lineNotifyAccessToken
				)
			));

			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
			Integer statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				closeableHttpResponse.close();

				switch (statusCode) {
					case HttpStatus.SC_UNAUTHORIZED:
						return new JavaScriptObjectNotation().
							withReason("Invalid access token").
							withResponse(false).
							withResult(statusCode);
					default:
						return new JavaScriptObjectNotation().
							withReason("Processed over time or stopped").
							withResponse(false).
							withResult(statusCode);
				}
			}
			Revoke revoke = Servant.JSON_MAPPER.readValue(
				closeableHttpResponse.
					getEntity().
					getContent(),
				Revoke.class
			);

			sucker.setLineNotifyAccessToken("");
			loverService.saveLover(sucker);

			closeableHttpResponse.close();
			closeableHttpClient.close();
			return new JavaScriptObjectNotation().
				withReason(revoke.getMessage()).
				withResponse(200 == revoke.getStatus()).
				withResult(revoke);
		} catch (URISyntaxException | IOException exception) {
			LOGGER.info(
				String.format(
					"%s.notifyRevoke(\n\t%s sucker = {}\n);",
					LineMessagingService.class,
					Lover.class
				),
				sucker,
				exception
			);
			return new JavaScriptObjectNotation().
				withReason(exception.getLocalizedMessage()).
				withResponse(false);
		}
	}

	/**
	 * 检查连接状态和访问令牌的有效性。
	 *
	 * @param sucker 情人
	 * @return 杰森格式对象
	 */
	@SuppressWarnings("ConvertToTryWithResources")
	public JavaScriptObjectNotation notifyStatus(Lover sucker) {
		if (Objects.isNull(sucker)) {
			return new JavaScriptObjectNotation().
				withResponse(false).
				withResult(sucker);
		}

		final String lineNotifyAccessToken = sucker.getLineNotifyAccessToken();
		if (Objects.isNull(lineNotifyAccessToken)) {
			return new JavaScriptObjectNotation().
				withResponse(false).
				withResult(sucker);
		}

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpGet httpGet = new HttpGet(
				new URIBuilder().
					setScheme(Servant.SCHEME_HTTPS).
					setHost(LINE_NOTIFY_API_ENDPOINT_HOST).
					setPath("/api/status").
					build()
			);
			httpGet.setHeader(new BasicHeader(
				HttpHeaders.AUTHORIZATION,
				String.format(
					"Bearer %s",
					lineNotifyAccessToken
				)
			));

			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
			Integer statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				closeableHttpResponse.close();

				sucker.setLineNotifyAccessToken("");
				loverService.saveLover(sucker);

				switch (statusCode) {
					case HttpStatus.SC_UNAUTHORIZED:
						return new JavaScriptObjectNotation().
							withReason("Invalid access token").
							withResponse(false).
							withResult(statusCode);
					default:
						return new JavaScriptObjectNotation().
							withReason("Processed over time or stopped").
							withResponse(false).
							withResult(statusCode);
				}
			}
			Status status = Servant.JSON_MAPPER.readValue(
				closeableHttpResponse.
					getEntity().
					getContent(),
				Status.class
			);

			closeableHttpResponse.close();
			closeableHttpClient.close();
			return new JavaScriptObjectNotation().
				withReason(status.getMessage()).
				withResponse(200 == status.getStatus()).
				withResult(status);
		} catch (URISyntaxException | IOException exception) {
			LOGGER.info(
				String.format(
					"%s.notifyStatus(\n\t%s sucker = {}\n);",
					LineMessagingService.class,
					Lover.class
				),
				sucker,
				exception
			);
			return new JavaScriptObjectNotation().
				withReason(exception.getLocalizedMessage()).
				withResponse(false);
		}
	}

	/**
	 * 请求授权码。
	 *
	 * @param sessionId HTTP session 识别键
	 * @param sucker 情人
	 * @return 重定向网址
	 */
	@Transactional
	public URI requestNotifyIntegration(String sessionId, Lover sucker) {
		AuthorizationCode authorizationCode = new AuthorizationCode(
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
				authorizationCode.getState(),
				sucker
			)
		);

		URI uri;
		try {
			uri = new URIBuilder().
				setScheme(Servant.SCHEME_HTTPS).
				setHost(LINE_NOTIFY_BOT_ENDPOINT_HOST).
				setPath("/oauth/authorize").
				setParameters(
					new BasicNameValuePair(
						"response_type",
						authorizationCode.
							getResponseType()
					),
					new BasicNameValuePair(
						"client_id",
						authorizationCode.
							getClientId()
					),
					new BasicNameValuePair(
						"redirect_uri",
						authorizationCode.
							getRedirectUri().
							toString()
					),
					new BasicNameValuePair(
						"scope",
						authorizationCode.
							getScope()
					),
					new BasicNameValuePair(
						"state",
						authorizationCode.
							getState()
					),
					new BasicNameValuePair(
						"response_mode",
						authorizationCode.
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
	@SuppressWarnings("ConvertToTryWithResources")
	public JavaScriptObjectNotation requestNotifyAccessToken(AuthorizationCode oAuthAuthorizationCode) {
		if (Objects.nonNull(oAuthAuthorizationCode.getError())) {
			return new JavaScriptObjectNotation().
				withReason(
					oAuthAuthorizationCode.
						getErrorDescription()
				).
				withResponse(false);
		}

		String code = oAuthAuthorizationCode.getCode();
		AccessToken accessToken = new AccessToken(
			code,
			redirectUri,
			LINE_NOTIFY_CLIENT_ID,
			LINE_NOTIFY_CLIENT_SECRET
		);

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(new URIBuilder().
				setScheme(Servant.SCHEME_HTTPS).
				setHost(LINE_NOTIFY_BOT_ENDPOINT_HOST).
				setPath("/oauth/token").
				setParameters(
					new BasicNameValuePair(
						"grant_type",
						accessToken.
							getGrantType()
					),
					new BasicNameValuePair(
						"code",
						accessToken.
							getCode()
					),
					new BasicNameValuePair(
						"redirect_uri",
						accessToken.
							getRedirectUri().
							toString()
					),
					new BasicNameValuePair(
						"client_id",
						accessToken.
							getClientId()
					),
					new BasicNameValuePair(
						"client_secret",
						accessToken.
							getClientSecret()
					)
				).
				build()
			);
			httpPost.setHeader(new BasicHeader(
				HttpHeaders.CONTENT_TYPE,
				"application/x-www-form-urlencoded"
			));

			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
			Integer statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				closeableHttpResponse.close();

				switch (statusCode) {
					case HttpStatus.SC_BAD_REQUEST:
						return new JavaScriptObjectNotation().
							withReason("Bad request").
							withResponse(false).
							withResult(statusCode);
					default:
						return new JavaScriptObjectNotation().
							withReason("Processed over time or stopped").
							withResponse(false).
							withResult(statusCode);
				}
			}
			accessToken = Servant.JSON_MAPPER.readValue(closeableHttpResponse.
				getEntity().
				getContent(),
				AccessToken.class
			);

			Lover sucker = lineNotifyAuthenticationRepository.
				findOneByState(
					oAuthAuthorizationCode.getState()
				).
				orElseThrow().
				getSucker();
			sucker.setLineNotifyAccessToken(
				accessToken.getAccessToken()
			);
			loverService.saveLover(sucker);
			lineNotifyAuthenticationRepository.
				deleteBySucker(sucker);//清除此人的请求数据

			closeableHttpResponse.close();
			closeableHttpClient.close();
			return new JavaScriptObjectNotation().
				withReason(accessToken.getMessage()).
				withResponse(true).
				withResult(accessToken);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getLocalizedMessage()).
				withResponse(false);
		}
	}
}
