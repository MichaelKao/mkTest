package tw.musemodel.dingzhiqingren.controller;

import java.net.URI;
import javax.servlet.http.HttpSession;
import me.line.notifybot.OAuthAuthorizationCode;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.service.LineMessagingService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * 控制器：透过 LINE 接收网站服务通知
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

	/**
	 * 授权 LINE 接收网站服务通知。
	 *
	 * @param session HTTP session
	 * @param authentication 认证
	 * @return 重定向
	 */
	@GetMapping(path = "/oauth/authorize")
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	ModelAndView oauthAuthorize(HttpSession session, Authentication authentication) {
		if (servant.isNull(authentication)) {
			Servant.redirectToRoot();
		}

		URI uri = lineMessagingService.requestNotifyIntegration(
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

	/**
	 * 请求访问令牌。
	 *
	 * @param oAuthAuthorizationCode OAuth2 Authorization Code
	 * @return org.​springframework.​http.​ResponseEntity
	 */
	@PostMapping(path = "/oauth/authorize")
	ResponseEntity<String> oauthAuthorize(OAuthAuthorizationCode oAuthAuthorizationCode) {
		JavaScriptObjectNotation json = lineMessagingService.requestNotifyAccessToken(
			oAuthAuthorizationCode
		);
		if (json.isResponse()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
