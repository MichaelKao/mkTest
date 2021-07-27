package tw.musemodel.dingzhiqingren.controller;

import java.net.URI;
import javax.servlet.http.HttpSession;
import me.line.notifybot.OAuthAuthorizeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tw.musemodel.dingzhiqingren.service.LineMessagingService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/notify-bot.line.me")
public class LineNotifyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LineNotifyController.class);

	@Autowired
	private Servant servant;

	@Autowired
	private LineMessagingService lineMessagingService;

	@Autowired
	private LoverService loverService;

	@GetMapping(path = "/oauth/authorize")
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	ModelAndView lineNotify(HttpSession session, Authentication authentication) {
		if (servant.isNull(authentication)) {
			Servant.redirectToRoot();
		}

		URI uri = lineMessagingService.notifyAuthorize(
			session.getId(),
			loverService.loadByUsername(
				authentication.getName()
			)
		);

		return new ModelAndView(String.format(
			"redirect:%s",
			uri.toString()
		));
	}

	@PostMapping(path = "/oauth/authorize")
	ResponseEntity<String> oauthAuthorize(OAuthAuthorizeResponse oAuthAuthorizeResponse, @RequestBody String payload) {
		LOGGER.info(
			"第六十三行\n\n{}\n\n{}\n",
			oAuthAuthorizeResponse,
			payload
		);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
