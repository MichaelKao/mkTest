package tw.musemodel.dingzhiqingren.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;
import tw.musemodel.dingzhiqingren.service.LoverService;

/**
 * 当 ApplicationContext 被初始化或刷新时引发的事件。
 *
 * @author p@musemodel.tw
 */
@Component
public class RequestHandledEventListener {

	private final static Logger LOGGER = LoggerFactory.getLogger(RequestHandledEventListener.class);

	@Autowired
	private LoverService loverService;

	@EventListener
	public void onRequestHandledEvent(RequestHandledEvent requestHandledEvent) throws JsonProcessingException {
		loverService.lastActive(
			requestHandledEvent.getTimestamp(),
			requestHandledEvent.getUserName()
		);
	}
}
