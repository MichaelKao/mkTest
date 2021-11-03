package tw.musemodel.dingzhiqingren.controller;

import com.beust.jcommander.internal.Lists;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.Specifications;
import tw.musemodel.dingzhiqingren.WebSocketServer;
import tw.musemodel.dingzhiqingren.entity.ForumThreadTag;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Role;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.entity.TrialCode;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.Member;
import tw.musemodel.dingzhiqingren.repository.ForumThreadTagRepository;
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

	<<<<<<< HEAD
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

	@Autowired
	private ForumThreadTagRepository forumThreadTagRepository;

	/**
	 * 一天內註冊的用戶
	 *
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	@GetMapping(path = "/{year:20\\d{2}}/{month:[01]\\d}/{dayOfMonth:[0-3]\\d}/newAccounts.asp", produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY"})
	ModelAndView accountsCreatedOfTheDay(@PathVariable int year, @PathVariable int month, @PathVariable int dayOfMonth, Authentication authentication) throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
		Document document = dashboardService.accountsCreatedOfTheDay(year, month, dayOfMonth, authentication);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.accountsOfTheDay",
				null,
				Locale.TAIWAN//台湾化
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/accountsOfTheDay");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	@PostMapping(path = "/updateGenuineMemebr.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String updateGenuineMemebr(@RequestParam Lover lover, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		JSONObject jsonObject;
		try {
			jsonObject = dashboardService.updateGenuineMember(lover);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

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
	String addTrialCode(@RequestParam String code, @RequestParam String keyOpinionLeader, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		if (keyOpinionLeader.isBlank()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"trial.keyOpinionLeaderMustntBeNull",
					null,
					Locale.TAIWAN//台湾化
				)).
				withResponse(false).
				toJSONObject().toString();
		}

		if (code.isBlank()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"trial.codeMustntBeNull",
					null,
					Locale.TAIWAN//台湾化
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
						Locale.TAIWAN//台湾化
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
	 * 新增文章標籤
	 *
	 * @param phrase
	 * @param authentication
	 * @return
	 */
	@PostMapping(path = "/addHashtag.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String addHashtag(@RequestParam String phrase, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		if (phrase.isBlank()) {
			return new JavaScriptObjectNotation().
				withReason("請填入標籤名稱").
				withResponse(false).
				toJSONObject().toString();
		}

		// 體驗碼不能重複
		List<ForumThreadTag> forumThreadTags = forumThreadTagRepository.findAll();
		for (ForumThreadTag forumThreadTag : forumThreadTags) {
			if (Objects.equals(forumThreadTag.getPhrase(), phrase)) {
				return new JavaScriptObjectNotation().
					withReason("標籤不能重複").
					withResponse(false).
					toJSONObject().toString();
			}
		}

		try {
			ForumThreadTag forumThreadTag = new ForumThreadTag(phrase);
			forumThreadTagRepository.saveAndFlush(forumThreadTag);
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
	ModelAndView certification(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.certification(
			authentication,
			Locale.TAIWAN//台湾化
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.certification",
				null,
				Locale.TAIWAN//台湾化
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
	String seeCetificationPic(@RequestParam long id, Authentication authentication) {
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
	String fail(@RequestParam UUID identifier, @RequestParam Boolean status, @RequestParam Long timestamp, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
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
	ModelAndView genTrialCode(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.generateTrialCode(
			authentication,
			Locale.TAIWAN//台湾化
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.genTrialCode",
				null,
				Locale.TAIWAN//台湾化
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/genTrialCode");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	@GetMapping(path = "/genHashtags.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView genHashtags(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.generateHashtags(
			authentication,
			Locale.TAIWAN//台湾化
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.genHashtags",
				null,
				Locale.TAIWAN//台湾化
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/genHashtags");
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
	String identityFailed(@RequestParam("id") Lover lover, @RequestParam Behavior reason, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		// 後台人員
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		History history = new History(
			me,
			lover,
			reason
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
	String identityPassed(@RequestParam("id") Lover lover, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
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
	 * 用户号互动报表。
	 *
	 * @author p@musemodel.tw
	 * @param p 第几页
	 * @param s 每页几笔
	 * @param response
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	@GetMapping(path = "/log/chat.xls")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY"})
	void logsOfChat(@RequestParam(defaultValue = "1") int p, @RequestParam(defaultValue = "100") int s, HttpServletResponse response) throws IOException {
		final Collection<History.Behavior> behaviors = Lists.newArrayList(
			HistoryService.BEHAVIOR_CHAT_MORE,
			HistoryService.BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
			HistoryService.BEHAVIOR_GREETING,
			HistoryService.BEHAVIOR_FOLLOW,
			HistoryService.BEHAVIOR_PEEK
		);

		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
			Sheet sheet = workbook.createSheet();
			Row firstRow = sheet.createRow(0);
			firstRow.createCell(0, CellType.STRING).setCellValue("主動方");
			firstRow.createCell(1, CellType.STRING).setCellValue("被動方");
			firstRow.createCell(2, CellType.STRING).setCellValue("行為");
			firstRow.createCell(3, CellType.NUMERIC).setCellValue("發生時戳");
			sheet.createFreezePane(0, 1);

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(workbook.
				getCreationHelper().
				createDataFormat().
				getFormat("yyyy/m/d hh:mm:ss")
			);//格式化时戳

			int rowNumber = 1;
			for (History history : historyRepository.findByBehaviorInOrderByOccurredDesc(behaviors, PageRequest.of(p < 1 ? 0 : p - 1, s))) {
				String behavior = "";
				switch (history.getBehavior()) {
					case DA_ZHAO_HU:
						behavior = "打招呼";
						break;
					case LIAO_LIAO:
						behavior = "聊聊";
						break;
					case JI_WO_LAI:
						behavior = "給我 LINE";
						break;
					case SHOU_CANG:
						behavior = "收藏";
						break;
					case KAN_GUO_WO:
						behavior = "看過我";
						break;
				}

				Row row = sheet.createRow(rowNumber);
				row.createCell(
					0,
					CellType.STRING
				).setCellValue(
					history.getInitiative().getNickname()
				);
				row.createCell(
					1,
					CellType.STRING
				).setCellValue(
					history.getPassive().getNickname()
				);
				row.createCell(
					2,
					CellType.STRING
				).setCellValue(
					behavior
				);

				Cell cell = row.createCell(
					3,
					CellType.STRING
				);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(
					history.getOccurred()
				);

				String greeting = history.getGreeting();
				if (Objects.nonNull(greeting)) {
					row.createCell(
						4,
						CellType.STRING
					).setCellValue(
						history.getGreeting()
					);
				}

				++rowNumber;
			}//for

			try (OutputStream outputStream = response.getOutputStream()) {
				response.setHeader("Content-Type", "application/vnd.ms-excel");
				response.setHeader(
					"Content-Disposition",
					String.format(
						"attachment; filename=\"chat@%s.xls\"",
						Servant.toTaipeiZonedDateTime(
							new Date(
								System.currentTimeMillis()
							).toInstant()
						).format(Servant.TAIWAN_DATE_TIME_FORMATTER)
					)
				);
				workbook.write(outputStream);
				outputStream.close();
			}
			workbook.close();
		}//try
	}

	/**
	 * 用户号报表。
	 *
	 * @param since 开始日期
	 * @param until 结束日期
	 * @param response
	 * @throws IOException
	 */
	@GetMapping(path = "/meMBERS.xls")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY"})
	void meMBERS(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date since, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date until, HttpServletResponse response) throws IOException {
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
			Sheet sheet = workbook.createSheet();
			Row firstRow = sheet.createRow(0);
			firstRow.createCell(0, CellType.STRING).setCellValue("主鍵");
			firstRow.createCell(1, CellType.STRING).setCellValue("國碼");
			firstRow.createCell(2, CellType.STRING).setCellValue("手機號");
			firstRow.createCell(3, CellType.STRING).setCellValue("貴賓到期日");
			firstRow.createCell(4, CellType.STRING).setCellValue("暱稱");
			firstRow.createCell(5, CellType.STRING).setCellValue("生日");
			firstRow.createCell(6, CellType.STRING).setCellValue("性別");
			firstRow.createCell(7, CellType.STRING).setCellValue("刪除");
			firstRow.createCell(8, CellType.STRING).setCellValue("安心");
			firstRow.createCell(9, CellType.STRING).setCellValue("註冊時間");
			firstRow.createCell(10, CellType.STRING).setCellValue("活躍時間");
			firstRow.createCell(11, CellType.STRING).setCellValue("推薦人");
			firstRow.createCell(12, CellType.STRING).setCellValue("內測");
			sheet.createFreezePane(0, 1);

			CellStyle cellStyleDate = workbook.createCellStyle(),
				cellStyleDateTime = workbook.createCellStyle();
			cellStyleDate.setDataFormat(workbook.
				getCreationHelper().
				createDataFormat().
				getFormat("yyyy/m/d")
			);//格式化时戳(年月日)
			cellStyleDateTime.setDataFormat(workbook.
				getCreationHelper().
				createDataFormat().
				getFormat("yyyy/m/d hh:mm:ss")
			);//格式化时戳(年月日时分秒)

			int rowNumber = 1;
			for (Lover mofo : loverService.accountsCreatedOfCertainPeriod(since, until)) {
				Row row = sheet.createRow(rowNumber);

				//主键
				row.createCell(
					0,
					CellType.STRING
				).setCellValue(
					mofo.getId().toString()
				);

				//国码
				row.createCell(
					1,
					CellType.STRING
				).setCellValue(
					mofo.getCountry().getCallingCode()
				);

				//帐号(手机号)
				String login = mofo.getLogin();
				if (Objects.nonNull(login)) {
					row.createCell(
						2,
						CellType.STRING
					).setCellValue(
						login
					);
				}

				//贵宾有效期
				Date vip = mofo.getVip();
				if (Objects.nonNull(vip)) {
					Cell cell = row.createCell(
						3,
						CellType.STRING
					);
					cell.setCellStyle(cellStyleDateTime);
					cell.setCellValue(
						Servant.TAIWAN_DATE_TIME_FORMATTER.format(
							Servant.toTaipeiZonedDateTime(vip)
						)
					);
				}

				//昵称
				String nickname = mofo.getNickname();
				if (Objects.nonNull(nickname)) {
					Cell cell = row.createCell(
						4,
						CellType.STRING
					);
					cell.setCellValue(
						nickname
					);
				}

				//生日
				Date birthday = mofo.getBirthday();
				if (Objects.nonNull(birthday)) {
					Cell cell = row.createCell(
						5,
						CellType.STRING
					);
					cell.setCellStyle(cellStyleDate);
					cell.setCellValue(
						Servant.DATE_TIME_FORMATTER_yyyyMMdd.format(
							Servant.toTaipeiZonedDateTime(birthday)
						)
					);
				}

				//性别
				row.createCell(
					6,
					CellType.STRING
				).setCellValue(
					mofo.getGender() ? "男" : "女"
				);

				//砍号
				String delete = mofo.getDelete();
				if (Objects.nonNull(delete)) {
					row.createCell(
						7,
						CellType.STRING
					).setCellValue(
						"砍號"
					);
				}

				//通过安心认证
				Boolean relief = mofo.getRelief();
				if (Objects.nonNull(relief)) {
					row.createCell(
						8,
						CellType.STRING
					).setCellValue(
						relief ? "通過" : "未認證"
					);
				}

				//註冊时间
				Date registered = mofo.getRegistered();
				if (Objects.nonNull(registered)) {
					Cell cell = row.createCell(
						9,
						CellType.STRING
					);
					cell.setCellStyle(cellStyleDateTime);
					cell.setCellValue(
						Servant.TAIWAN_DATE_TIME_FORMATTER.format(
							Servant.toTaipeiZonedDateTime(registered)
						)
					);
				}

				//最后活跃时间
				Date active = mofo.getActive();
				if (Objects.nonNull(active)) {
					Cell cell = row.createCell(
						10,
						CellType.STRING
					);
					cell.setCellStyle(cellStyleDateTime);
					cell.setCellValue(
						Servant.TAIWAN_DATE_TIME_FORMATTER.format(
							Servant.toTaipeiZonedDateTime(active)
						)
					);
				}

				//推荐人
				Lover referrer = mofo.getReferrer();
				if (Objects.nonNull(referrer)) {
					row.createCell(
						11,
						CellType.STRING
					).setCellValue(
						referrer.getId().toString()
					);
				}

				//가짜 계좌
				row.createCell(
					12,
					CellType.STRING
				).setCellValue(
					mofo.isFake() ? "異常" : "一般"
				);

				++rowNumber;
			}//for

			try (OutputStream outputStream = response.getOutputStream()) {
				response.setHeader("Content-Type", "application/vnd.ms-excel");
				response.setHeader(
					"Content-Disposition",
					String.format(
						"attachment; filename=\"meMBERS@%s-%s.xls\"",
						Servant.SIMPLE_DATE_FORMAT_yyyyMMdd.format(since),
						Servant.SIMPLE_DATE_FORMAT_yyyyMMdd.format(until)
					)
				);
				workbook.write(outputStream);
				outputStream.close();
			}
			workbook.close();
		}//try
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
	ModelAndView stopRecurring(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.stopRecurringDocument(
			authentication,
			Locale.TAIWAN//台湾化
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.stopRecurring",
				null,
				Locale.TAIWAN//台湾化
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
	@SuppressWarnings("UnusedAssignment")
	String stopRecurring(@RequestParam("applyID") Long applyID, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		StopRecurringPaymentApplication stopRecurringPaymentApplication = stopRecurringPaymentApplicationRepository.
			findById(applyID).
			orElse(null);

		// 後台人員
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		stopRecurringPaymentApplication = dashboardService.
			handleStopRecurringPaymentApplication(
				stopRecurringPaymentApplication,
				me
			);

		return new JavaScriptObjectNotation().
			withResponse(true).
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
	String updateTrialCode(@RequestParam TrialCode trialCode, @RequestParam String editedCode, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
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
			withReason("已更改成功").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 更改文章標籤
	 *
	 * @param forumThreadTag
	 * @param editedHashtag
	 * @param authentication
	 * @return
	 */
	@PostMapping(path = "/updateHashtag.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String updateHashtag(@RequestParam ForumThreadTag forumThreadTag, @RequestParam String phrase, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		try {
			forumThreadTag.setPhrase(phrase);
			forumThreadTagRepository.saveAndFlush(forumThreadTag);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}

		return new JavaScriptObjectNotation().
			withReason("已更改成功").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 會員們
	 *
	 * @author r@musemodel.tw
	 * @param locale
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	@GetMapping(path = "/members.asp")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView members(Authentication authentication) throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
		Document document = dashboardService.members(authentication, Locale.TAIWAN);

		ModelAndView modelAndView = new ModelAndView("dashboard/members");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 查看會員的邀請碼、下線
	 *
	 * @param p
	 * @param s
	 * @param lover
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/referralCode.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String referralCode(@RequestParam(defaultValue = "0") final int p, @RequestParam(defaultValue = "5") final int s, @RequestParam Lover lover, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		return loverService.
			getReferralCodeAndDescendants(lover, p, s).
			toString();
	}

	/**
	 * 搜尋會員
	 *
	 * @param searchGender
	 * @param searchValue
	 * @param pageSearch
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/searchMember.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String searchMember(@RequestParam Boolean searchGender, @RequestParam String searchValue, @RequestParam int pageSearch, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		Page<Lover> loverPage = loverRepository.findAll(
			Specifications.findByGenderAndNicknameLikeOrLoginAccount(
				searchGender,
				searchValue
			),
			PageRequest.of(pageSearch - 1, 10)
		);
		List<Member> memberList = new ArrayList<>();

		for (Lover lover : loverPage.getContent()) {
			Date vip = lover.getVip();
			Member member = new Member(
				lover.getId(),
				lover.getIdentifier(),
				lover.getNickname(),
				lover.getLogin(),
				Objects.nonNull(vip) ? LoverService.DATE_FORMATTER.format(
				Servant.
					toTaipeiZonedDateTime(
						vip
					).
					withZoneSameInstant(
						Servant.ASIA_TAIPEI_ZONE_ID
					)) : null,
				LoverService.DATE_FORMATTER.format(
					Servant.
						toTaipeiZonedDateTime(
							lover.getRegistered()
						).
						withZoneSameInstant(
							Servant.ASIA_TAIPEI_ZONE_ID
						)
				)
			);
			if (loverService.isVIP(lover)) {
				member.setIsVIP(true);
			}
			if (loverService.isVVIP(lover)) {
				member.setIsVVIP(true);
			}
			if (loverService.isTrial(lover)) {
				member.setIsTrial(true);
			}
			memberList.add(member);
		}
		JSONObject jsonObject = new JSONObject();
		return jsonObject.put("result", memberList).
			put("totalPages", loverPage.getTotalPages()).
			put("currentPage", pageSearch).toString();
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
	ModelAndView withdrawal(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.withdrawal(
			authentication,
			Locale.TAIWAN//台湾化
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.withdrawal",
				null,
				Locale.TAIWAN//台湾化
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
	String withdrawalSuccess(@RequestParam UUID identifier, @RequestParam Boolean status, @RequestParam Long timestamp, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
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

	/**
	 * 更新權限
	 *
	 * @param role
	 * @param lover
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/updatePrivilege.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String updatePrivilege(@RequestParam Role role, @RequestParam Lover lover, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		JSONObject jsonObject;
		try {
			jsonObject = dashboardService.updatePrivilege(role, lover);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					Locale.TAIWAN
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 顯示權限
	 *
	 * @param lover
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/privilege.json")
	@ResponseBody
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	String privilege(@RequestParam Lover lover, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		JSONObject jsonObject;
		try {
			jsonObject = dashboardService.privilege(lover);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					Locale.TAIWAN//台湾化
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 男仕 ME 點紀錄
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/mePointsRecordsForMale.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView mePointsRecordsForMale(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.mePointsRecords(
			authentication,
			Locale.TAIWAN//台湾化
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.mePointsRecords",
				null,
				Locale.TAIWAN//台湾化
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/mePointsRecords");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 退回 ME 點
	 *
	 * @param history
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/returnFare.json")
	@ResponseBody
	String returnFare(@RequestParam History history, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		JSONObject jsonObject;
		try {
			jsonObject = dashboardService.returnFare(
				history
			);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}
		return jsonObject.toString();
	}

	@PostMapping(path = "/upgradeVip.json")
	@ResponseBody
	String upgradeVip(@RequestParam Lover lover, @RequestParam Date date, Authentication authentication) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(Locale.TAIWAN);
		}

		JSONObject jsonObject;
		try {
			jsonObject = dashboardService.upgradeVip(
				lover,
				date
			);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}
		return jsonObject.toString();
	}

	@GetMapping(path = "/groupMsg.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_FINANCE"})
	ModelAndView groupMsg(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = dashboardService.mePointsRecords(
			authentication,
			Locale.TAIWAN//台湾化
		);

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.groupMsg",
				null,
				Locale.TAIWAN//台湾化
			)
		);

		ModelAndView modelAndView = new ModelAndView("dashboard/groupMsg");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 小编发送文字讯息与站内会员做聊天室互动
	 *
	 * @return 网页
	 */
	@GetMapping(path = "/broadcast.asp")
	@Secured({"ROLE_ALMIGHTY", "ROLE_XIAOBIAN"})
	ModelAndView broadcast() {
		ModelAndView modelAndView = new ModelAndView("dashboard/broadcast");//尚未建档
		modelAndView.getModelMap().addAttribute(null);
		return modelAndView;
	}

	/**
	 * 小编发送文字讯息与站内会员做聊天室互动
	 *
	 * @param gender 性别
	 * @param date 在此日期之后
	 * @param content 讯息内容
	 * @param authentication 认证
	 * @return 杰森对象
	 */
	@PostMapping(path = "/broadcast.asp", produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured({"ROLE_ALMIGHTY", "ROLE_XIAOBIAN"})
	Collection<History> broadcast(
		@RequestParam Boolean gender,
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam(required = false) Date date,
		@RequestParam String content,
		Authentication authentication
	) {
		return dashboardService.broadcast(
			gender,
			date,
			content,
			loverService.loadByUsername(
				authentication.getName()
			)
		);
	}
}
