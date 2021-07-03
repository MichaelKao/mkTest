package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Line Messaging API 服务层
 *
 * @author p@musemodel.tw
 */
@Service
public class LineMessagingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LineMessagingService.class);

	private static final String LINE_NOTIFY_ACCESS_TOKEN = System.getenv("LINE_NOTIFY_ACCESS_TOKEN");

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
}
