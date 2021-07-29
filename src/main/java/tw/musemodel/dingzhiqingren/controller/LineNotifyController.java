package tw.musemodel.dingzhiqingren.controller;

import java.io.IOException;
import java.net.URI;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import me.line.notifybot.OAuthAuthorizationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
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

	@Autowired
	private MessageSource messageSource;

	/**
	 * 授权 LINE 接收网站服务通知。
	 *
	 * @param session HTTP session
	 * @param authentication 认证
	 * @return 重定向
	 */
	@GetMapping(path = "/authorize.asp")
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	ModelAndView authorize(HttpSession session, Authentication authentication) {
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
//	@PostMapping(path = "/authorize.asp")
//	ResponseEntity<String> authorized(OAuthAuthorizationCode oAuthAuthorizationCode) {
//		JavaScriptObjectNotation json = lineMessagingService.requestNotifyAccessToken(
//			oAuthAuthorizationCode
//		);
//		if (json.isResponse()) {
//			return new ResponseEntity<>(HttpStatus.OK);
//		}
//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}
	@PostMapping(path = "/authorize.asp")
	@ResponseBody
	ModelAndView authorized(OAuthAuthorizationCode oAuthAuthorizationCode) throws SAXException, IOException, ParserConfigurationException {
		JavaScriptObjectNotation json = lineMessagingService.requestNotifyAccessToken(
			oAuthAuthorizationCode
		);

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		documentElement.setAttribute(
			"result",
			Boolean.toString(json.isResponse())
		);

		ModelAndView modelAndView = new ModelAndView("lineNotifyCallback");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}
}
