package tw.musemodel.dingzhiqingren.controller;

import java.util.Locale;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/poc")
public class ProofOfConcept {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private Servant servant;

	/**
	 * 车马费(男对女)
	 *
	 * @param female 女生
	 * @param points 点数
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/fare.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String fare(@RequestParam("whom") Lover female, @RequestParam(name = "howMany") short points, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover male = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.fare(
				male,
				female,
				points
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 给我赖(男对女)
	 *
	 * @param female 女生
	 * @param greetingMessage 招呼语
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/stalking.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String gimmeYourLineInvitation(@RequestParam("whom") Lover female, @RequestParam(name = "what", required = false) String greetingMessage, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover male = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.gimmeYourLineInvitation(
				male,
				female,
				greetingMessage
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 打招呼(女对男)
	 *
	 * @param male 男生
	 * @param greetingMessage 招呼语
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/greet.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String greet(@RequestParam("whom") Lover male, @RequestParam(name = "what", required = false) String greetingMessage, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover female = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.greet(
				female,
				male,
				greetingMessage
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 看过我
	 *
	 * @param masochism 谁被看
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/peek.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String peek(@RequestParam Lover masochism, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover sadism = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.peek(
				sadism,
				masochism
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}
}
