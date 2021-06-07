package tw.musemodel.dingzhiqingren.event;

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

/**
 * @author p@musemodel.tw
 */
@Component
public class SignedUpListener implements ApplicationListener<SignedUpEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(SignedUpListener.class);

	private static final AmazonSNS AMAZON_SNS = AmazonSNSClientBuilder.defaultClient();

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

		PublishResult publishResult = AMAZON_SNS.publish(
			new PublishRequest().
				withMessage(String.format(
					"請造訪 https://%s/activation.asp 並輸入激活碼：%s。",
					event.getDomainName(),
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
	}
}
