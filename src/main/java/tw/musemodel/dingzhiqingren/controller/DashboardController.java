package tw.musemodel.dingzhiqingren.controller;

import java.io.IOException;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalRecordRepository;
import tw.musemodel.dingzhiqingren.service.DashboardService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * 控制器：後台
 *
 * @author m@musemodel.tw
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Servant servant;

	@Autowired
	private LoverService loverService;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private WithdrawalRecordRepository withdrawalRecordRepository;

	@Autowired
	private LoverRepository loverRepository;

	/**
	 * 甜心提取車馬費(財務後台)
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/withdrawal.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView withdrawal(Authentication authentication, Locale locale)
		throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = dashboardService.withdrawalDocument(locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.withdrawal",
			null,
			locale
		));

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}
		if (!isFinance && !isAlmighty) {
			return new ModelAndView("redirect:/");
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/withdrawal");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 匯款車馬費成功
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/success.json")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	@ResponseBody
	String success(@RequestParam("id") WithdrawalRecord withdrawalRecord, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		withdrawalRecord.setStatus(Boolean.TRUE);
		withdrawalRecordRepository.saveAndFlush(withdrawalRecord);

		return new JavaScriptObjectNotation().
			withReason("已撥款成功").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 匯款車馬費失敗
	 *
	 * @param withdrawalRecord
	 * @param failReason
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/fail.json")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	@ResponseBody
	String fail(@RequestParam("id") WithdrawalRecord withdrawalRecord, @RequestParam String failReason,
		Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		withdrawalRecord.setStatus(Boolean.FALSE);
		withdrawalRecord.setFailReason(failReason);
		withdrawalRecordRepository.saveAndFlush(withdrawalRecord);

		return new JavaScriptObjectNotation().
			withReason("已通知甜心").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 安心認證審核
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/certification.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView certification(Authentication authentication, Locale locale)
		throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = dashboardService.certificationDocument(locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.certification",
			null,
			locale
		));

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}
		if (!isFinance && !isAlmighty) {
			return new ModelAndView("redirect:/");
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/certification");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 安心認證審核通過
	 *
	 * @param lover
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/identityPassed.json")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	@ResponseBody
	String identityPassed(@RequestParam("id") Lover lover, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		lover.setCertification(Boolean.TRUE);
		loverRepository.saveAndFlush(lover);

		return new JavaScriptObjectNotation().
			withReason("審核通過").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 安心認證審核不通過
	 *
	 * @param lover
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/identityFailed.json")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	@ResponseBody
	String identityFailed(@RequestParam("id") Lover lover, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		lover.setCertification(null);
		loverRepository.saveAndFlush(lover);

		return new JavaScriptObjectNotation().
			withReason("審核不通過").
			withResponse(true).
			toJSONObject().toString();
	}
}
