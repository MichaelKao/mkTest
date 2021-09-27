package tw.musemodel.dingzhiqingren.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
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
import tw.musemodel.dingzhiqingren.WebSocketServer;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.entity.TrialCode;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.StopRecurringPaymentApplicationRepository;
import tw.musemodel.dingzhiqingren.repository.TrialCodeRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalRecordRepository;
import tw.musemodel.dingzhiqingren.service.DashboardService;
import tw.musemodel.dingzhiqingren.service.HistoryService;
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
	private WebSocketServer webSocketServer;

	@Autowired
	private LoverService loverService;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private Servant servant;

	@Autowired
	private WithdrawalRecordRepository withdrawalRecordRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private StopRecurringPaymentApplicationRepository stopRecurringPaymentApplicationRepository;

	@Autowired
	private TrialCodeRepository trialCodeRepository;

	/**
	 * 新增體驗碼
	 *
	 * @param code
	 * @param keyOpinionLeader
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/addTrialCode.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String addTrialCode(@RequestParam String code, @RequestParam String keyOpinionLeader, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		if (keyOpinionLeader.isBlank()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"trial.keyOpinionLeaderMustntBeNull",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().toString();
		}

		if (code.isBlank()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"trial.codeMustntBeNull",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().toString();
		}

		// 體驗碼不能重複
		List<TrialCode> trialCodes = trialCodeRepository.findAll();
		for (TrialCode trialCode : trialCodes) {
			if (Objects.equals(trialCode.getCode(), code)) {
				return new JavaScriptObjectNotation().
					withReason(messageSource.getMessage(
						"trial.codeMustBeUnique",
						null,
						locale
					)).
					withResponse(false).
					toJSONObject().toString();
			}
		}

		try {
			TrialCode trialCode = new TrialCode(code, keyOpinionLeader);
			trialCodeRepository.saveAndFlush(trialCode);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}

		return new JavaScriptObjectNotation().
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 安心认证审核。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/certification.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView certification(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.certification(
			authentication,
			locale
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.certification",
				null,
				locale
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/certification");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 查看安心認證的照片
	 *
	 * @param lover
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/seeCetificationPic.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String seeCetificationPic(@RequestParam long id, Authentication authentication, Locale locale) {
		try {
			return new JavaScriptObjectNotation().
				withResult(
					String.format(
						"https://%s/identity/%d",
						Servant.STATIC_HOST,
						id
					)).
				withResponse(true).
				toJSONObject().toString();
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}
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
	@PostMapping(path = "/withdrawalFail.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String fail(@RequestParam UUID identifier, @RequestParam Boolean status, @RequestParam Long timestamp, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		// 財務
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 甜心
		Lover honey = loverService.loadByIdentifier(identifier);
		Date current = new Date(timestamp);
		withdrawalRecordRepository.deleteByHoneyAndStatusAndTimestamp(honey, status, current);

		withdrawalRecordRepository.flush();

		History history = new History(
			me,
			honey,
			History.Behavior.TI_LING_SHI_BAI
		);
		historyRepository.saveAndFlush(history);

		// 推送通知給甜心
		webSocketServer.sendNotification(
			honey.getIdentifier().toString(),
			String.format(
				"您欲提領的車馬費失敗!"
			));

		return new JavaScriptObjectNotation().
			withReason("已通知甜心").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 核发体验码。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/genTrialCode.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView genTrialCode(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.generateTrialCode(
			authentication,
			locale
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.genTrialCode",
				null,
				locale
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/genTrialCode");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
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
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	@SuppressWarnings("UnusedAssignment")
	String identityFailed(@RequestParam("id") Lover lover, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		// 後台人員
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		History history = new History(
			me,
			lover,
			History.Behavior.AN_XIN_SHI_BAI
		);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給情人
		webSocketServer.sendNotification(
			lover.getIdentifier().toString(),
			String.format(
				"您申請的安心認證失敗!"
			));

		lover.setRelief(null);
		loverRepository.saveAndFlush(lover);

		return new JavaScriptObjectNotation().
			withReason("審核不通過").
			withResponse(true).
			toJSONObject().toString();
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
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	@SuppressWarnings("UnusedAssignment")
	String identityPassed(@RequestParam("id") Lover lover, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		// 後台人員
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		lover.setRelief(Boolean.TRUE);
		loverRepository.saveAndFlush(lover);

		History history = new History(
			me,
			lover,
			History.Behavior.AN_XIN_CHENG_GONG
		);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給情人
		webSocketServer.sendNotification(
			lover.getIdentifier().toString(),
			String.format(
				"您已通過安心認證!"
			));

		return new JavaScriptObjectNotation().
			withReason("審核通過").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 长期贵宾解除定期定额。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/stopRecurring.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView stopRecurring(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.stopRecurringDocument(
			authentication,
			locale
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.stopRecurring",
				null,
				locale
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/stopRecurring");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 解除定期定額長期貴賓
	 *
	 * @param lover
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/stopRecurring.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String stopRecurring(@RequestParam("applyID") Long applyID, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		StopRecurringPaymentApplication stopRecurringPaymentApplication = stopRecurringPaymentApplicationRepository.
			findById(applyID).
			orElse(null);

		// 後台人員
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		stopRecurringPaymentApplication = dashboardService.handleStopRecurringPaymentApplication(stopRecurringPaymentApplication, me);

		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(stopRecurringPaymentApplication).
			toJSONObject().toString();
	}

	/**
	 * 更新體驗碼
	 *
	 * @param trialCode
	 * @param editedCode
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/updateTrialCode.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String updateTrialCode(@RequestParam TrialCode trialCode, @RequestParam String editedCode, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		try {
			trialCode.setCode(editedCode);
			trialCodeRepository.saveAndFlush(trialCode);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}

		return new JavaScriptObjectNotation().
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 手动升级贵宾。
	 *
	 * @author r@musemodel.tw
	 * @param p 第几页
	 * @param s 每页几笔
	 * @param response
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	@GetMapping(path = "/upgradeManually.xml")
	@ResponseBody
	//@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	void upgradeManually(@RequestParam(defaultValue = "0") int p, @RequestParam(defaultValue = "10") int s, HttpServletResponse response) throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		Element usersElement = document.createElement("users");
		documentElement.appendChild(usersElement);

		for (Lover lover : loverRepository.findAll(PageRequest.of(p, s))) {
			Element userElement = document.createElement("user");

			Element nicknameElement = document.createElement("nickname");
			nicknameElement.setTextContent(lover.getNickname());
			userElement.appendChild(nicknameElement);

			Element loginElement = document.createElement("login");
			loginElement.setTextContent(lover.getLogin());
			userElement.appendChild(loginElement);

			Date vipExpiration = lover.getVip();
			if (loverService.isVIP(lover)) {
				Element vipElement = document.createElement("vip");
				vipElement.setTextContent(
					LoverService.DATE_TIME_FORMATTER.format(
						servant.
							toTaipeiZonedDateTime(
								vipExpiration
							).
							withZoneSameInstant(
								Servant.ASIA_TAIPEI
							)
					)
				);
				userElement.appendChild(vipElement);
			}
			if (loverService.isVVIP(lover)) {
				Element vvipElement = document.createElement("vvip");
				vvipElement.setTextContent(
					LoverService.DATE_TIME_FORMATTER.format(
						servant.
							toTaipeiZonedDateTime(
								vipExpiration
							).
							withZoneSameInstant(
								Servant.ASIA_TAIPEI
							)
					)
				);
			}

			Element idElement = document.createElement("id");
			idElement.setTextContent(lover.getId().toString());
			userElement.appendChild(idElement);

			usersElement.appendChild(userElement);
		}
		TransformerFactory.
			newDefaultInstance().
			newTransformer().
			transform(
				new DOMSource(document),
				new StreamResult(response.getOutputStream())
			);
	}

	/**
	 * 甜心提取车马费。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/withdrawal.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView withdrawal(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.withdrawal(
			authentication,
			locale
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.withdrawal",
				null,
				locale
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/withdrawal");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 匯款車馬費成功
	 *
	 * @author m@musemodel.tw
	 * @param identifier 识别码
	 * @param status 状态
	 * @param timestamp EPOCH 秒数
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森
	 */
	@PostMapping(path = "/withdrawalSuccess.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String withdrawalSuccess(@RequestParam UUID identifier, @RequestParam Boolean status, @RequestParam Long timestamp, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		Lover honey = loverService.loadByIdentifier(identifier);

		Date current = new Date(timestamp);
		List<WithdrawalRecord> withdrawalRecords = withdrawalRecordRepository.findByHoneyAndStatusAndTimestamp(honey, status, current);

		int totalPoints = 0;
		for (WithdrawalRecord withdrawalRecord : withdrawalRecords) {
			totalPoints += withdrawalRecord.getPoints();
			withdrawalRecord.setStatus(Boolean.TRUE);
			withdrawalRecordRepository.save(withdrawalRecord);
		}

		withdrawalRecordRepository.flush();

		History history = new History(
			me,
			honey,
			HistoryService.BEHAVIOR_WITHDRAWAL_SUCCESS
		);
		history.setGreeting(Integer.toString(totalPoints));
		historyRepository.saveAndFlush(history);

		// 推送通知給甜心
		webSocketServer.sendNotification(
			honey.getIdentifier().toString(),
			String.format(
				"您提領的車馬費 %d 已匯款成功!",
				totalPoints
			)
		);

		return new JavaScriptObjectNotation().
			withReason("已撥款成功").
			withResponse(true).
			toJSONObject().toString();
	}
}
