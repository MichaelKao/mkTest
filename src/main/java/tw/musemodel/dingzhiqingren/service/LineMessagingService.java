package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import me.line.notifybot.OAuthAuthorizeRequest;
import me.line.notifybot.OAuthAuthorizeResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.musemodel.dingzhiqingren.entity.LineNotifyAuthentication;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.LineNotifyAuthenticationRepository;

/**
 * Line Messaging API 服务层
 *
 * @author p@musemodel.tw
 */
@Service
public class LineMessagingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LineMessagingService.class);

	private static final String LINE_NOTIFY_ACCESS_TOKEN = System.getenv("LINE_NOTIFY_ACCESS_TOKEN");

	private static final String LINE_NOTIFY_CLIENT_ID = System.getenv("LINE_NOTIFY_CLIENT_ID");

	private static final String LINE_NOTIFY_CLIENT_SECRET = System.getenv("LINE_NOTIFY_CLIENT_SECRET");

	private static final String LINE_NOTIFY_ENDPOINT_HOST = System.getenv("LINE_NOTIFY_ENDPOINT_HOST");

	private static URI redirectUri;

	static {
		try {
			redirectUri = new URIBuilder().
				setScheme("https").
				setHost(Servant.LOCALHOST).
				setPath("/notify-bot.line.me/oauth/authorize").
				build();
		} catch (URISyntaxException uriSyntaxException) {
			LOGGER.info(
				String.format(
					"生成的重定向 URI\n%s#redirectUri 无法解析",
					URI.class
				),
				uriSyntaxException
			);
		}
	}

	@Autowired
	private LineNotifyAuthenticationRepository lineNotifyAuthenticationRepository;

	/**
	 * LINE Notify 服务。
	 *
	 * @param message 1000 characters max
	 */
	public void notify(String message) {
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
					LINE_NOTIFY_ACCESS_TOKEN
				)
			);
			closeableHttpClient.execute(httpPost);
			closeableHttpClient.close();
		} catch (URISyntaxException | IOException exception) {
			LOGGER.info(
				String.format(
					"%s.notify(\n\tString message = {}\n);",
					getClass().getName()
				),
				message,
				exception
			);
		}
	}

	public URI notifyAuthorize(String sessionId, Lover sucker) {
		OAuthAuthorizeRequest oAuthAuthorizeRequest = new OAuthAuthorizeRequest();
		oAuthAuthorizeRequest.setClientId(LINE_NOTIFY_CLIENT_ID);
		oAuthAuthorizeRequest.setRedirectUri(redirectUri);
		oAuthAuthorizeRequest.setState(String.format(
			"%s%d",
			sessionId,
			System.currentTimeMillis()
		));

		lineNotifyAuthenticationRepository.saveAndFlush(
			new LineNotifyAuthentication(
				oAuthAuthorizeRequest.getState(),
				sucker
			)
		);

		URI uri;
		try {
			uri = new URIBuilder().
				setScheme("https").
				setHost(LINE_NOTIFY_ENDPOINT_HOST).
				setPath("/oauth/authorize").
				setParameters(
					new BasicNameValuePair(
						"response_type",
						oAuthAuthorizeRequest.getResponseType()
					),
					new BasicNameValuePair(
						"client_id",
						oAuthAuthorizeRequest.getClientId()
					),
					new BasicNameValuePair(
						"redirect_uri",
						oAuthAuthorizeRequest.
							getRedirectUri().
							toString()
					),
					new BasicNameValuePair(
						"scope",
						oAuthAuthorizeRequest.getScope()
					),
					new BasicNameValuePair(
						"state",
						oAuthAuthorizeRequest.getState()
					),
					new BasicNameValuePair(
						"response_mode",
						oAuthAuthorizeRequest.getResponseMode()
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

	public void notifyToken(OAuthAuthorizeResponse oAuthAuthorizeResponse) {
		if (Objects.nonNull(oAuthAuthorizeResponse.getError())) {
			oAuthAuthorizeResponse.getErrorDescription();
			return;
		}

		Lover sucker = lineNotifyAuthenticationRepository.
			findOneByState(
				oAuthAuthorizeResponse.getState()
			).
			orElseThrow().
			getSucker();

		String code = oAuthAuthorizeResponse.getCode();
		//TODO:	取得 access token
		//TODO:	儲存 access token
		//TODO:	刪除紀錄
	}
}
