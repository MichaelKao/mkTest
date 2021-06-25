package tw.musemodel.dingzhiqingren.event;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.LoverService;

/**
 * 表明身份验证成功的应用程序事件。
 *
 * @author p@musemodel.tw
 */
@Component
public class AuthenticationSuccessListener {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

	@Autowired
	LoverService loverService;

	@EventListener
	public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
		Authentication authentication = authenticationSuccessEvent.getAuthentication();
		Lover lover = loverService.loadByUsername(
			authentication.getName()
		);
		lover.setActive(new Date(System.currentTimeMillis()));
		lover = loverService.saveLover(lover);
		LOGGER.debug(
			String.format(
				"表明身份验证成功的应用程序事件。\n%s.onAuthenticationSuccessEvent(\n\t%s = {}\n);\n%s = {}",
				getClass().getName(),
				AuthenticationSuccessEvent.class,
				Lover.class
			),
			authenticationSuccessEvent,
			lover
		);
	}
}
