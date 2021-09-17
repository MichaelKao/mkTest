package tw.musemodel.dingzhiqingren.event;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tw.musemodel.dingzhiqingren.entity.Activation;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * @author p@musemodel.tw
 */
@Component
public class SignedUpListener implements ApplicationListener<SignedUpEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(SignedUpListener.class);

	private static final String AWS_ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");

	private static final String AWS_SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");

	private static final AmazonSNS AMAZON_SNS = AmazonSNSClientBuilder.
		standard().
		withCredentials(new AWSStaticCredentialsProvider(
			new BasicAWSCredentials(
				AWS_ACCESS_KEY_ID,
				AWS_SECRET_ACCESS_KEY
			)
		)).
		withRegion(Regions.AP_SOUTHEAST_1).build();

	@Autowired
	private Servant servant;

	@Autowired
	private LoverService loverService;

	/**
	 * 处理应用程序事件
	 *
	 * @param event 要响应的事件
	 */
	@Override
	public void onApplicationEvent(SignedUpEvent event) {
		Lover lover = event.getLover();
		Activation activation = loverService.signedUp(lover);

		if (!servant.isDevelopment()) {
			PublishResult publishResult = AMAZON_SNS.publish(
				new PublishRequest().
					withMessage(String.format(
						"歡迎加入「養蜜youngme」您的激活碼為：%s，使用期限為1小時，如超過時間需重新申請唷！",
						activation.getString()
					)).
					withPhoneNumber(String.format(
						"+%s%s",
						lover.getCountry().getCallingCode(),
						lover.getLogin()
					))
			);

			LOGGER.debug(
				"%s.onApplicationEvent(\n\tSignedUpEvent = {}\n);\n使用 AWS 开发工具包提供的 Amazon SNS 客户端发送消息：{}",
				event,
				publishResult
			);
		} else {
			LOGGER.debug("开发模式下不发送短信息。");
		}
	}
}
