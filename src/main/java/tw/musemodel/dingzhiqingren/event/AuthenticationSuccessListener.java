package tw.musemodel.dingzhiqingren.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author p@musemodel.tw
 */
@Component
public class AuthenticationSuccessListener {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

	@EventListener
	public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
		Authentication authentication = authenticationSuccessEvent.getAuthentication();
		LOGGER.info("抓到登入事件：{}", authentication);
	}
}
