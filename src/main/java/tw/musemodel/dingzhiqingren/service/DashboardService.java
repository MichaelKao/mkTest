package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.WebSocketServer;
import tw.musemodel.dingzhiqingren.entity.ForumThreadTag;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Lover.MaleSpecies;
import tw.musemodel.dingzhiqingren.entity.Privilege;
import tw.musemodel.dingzhiqingren.entity.PrivilegeKey;
import tw.musemodel.dingzhiqingren.entity.Role;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.entity.TrialCode;
import tw.musemodel.dingzhiqingren.entity.WithdrawalInfo;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord.WayOfWithdrawal;
import tw.musemodel.dingzhiqingren.model.EachWithdrawal;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.ForumThreadTagRepository;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.PrivilegeRepository;
import tw.musemodel.dingzhiqingren.repository.RoleRepository;
import tw.musemodel.dingzhiqingren.repository.StopRecurringPaymentApplicationRepository;
import tw.musemodel.dingzhiqingren.repository.TrialCodeRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalInfoRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalRecordRepository;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;

/**
 * 服务层：情人
 *
 * @author p@musemodel.tw
 */
@Service
public class DashboardService {

	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Servant servant;

	@Autowired
	private LoverService loverService;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private StopRecurringPaymentApplicationRepository stopRecurringPaymentApplicationRepository;

	@Autowired
	private TrialCodeRepository trialCodeRepository;

	@Autowired
	private WithdrawalInfoRepository withdrawalInfoRepository;

	@Autowired
	private WithdrawalRecordRepository withdrawalRecordRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private WebSocketServer webSocketServer;

	@Autowired
	private LineMessagingService lineMessagingService;

	@Autowired
	private ForumThreadTagRepository forumThreadTagRepository;

	/**
	 * 构建根元素。
	 *
	 * @param document 文件
	 * @param authentication 认证
	 * @return 根元素
	 */
	@Transactional(readOnly = true)
	private Element documentElement(Document document, Authentication authentication) {
		String login = authentication.getName();

		Lover mofo = loverService.loadByUsername(login);

		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute(
			"signIn",
			login
		);//帐号(国码➕手机号)
		documentElement.setAttribute(
			"identifier",
			mofo.getIdentifier().toString()
		);//识别码
		documentElement.setAttribute(
			mofo.getGender() ? "male" : "female",
			null
		);//性别

		// 給當日新進會員報表的URL
		Calendar today = Calendar.getInstance();
		int m = today.get(Calendar.MONTH) + 1;
		int date = today.get(Calendar.DATE);
		documentElement.setAttribute("year", Integer.toString(today.get(Calendar.YEAR)));
		documentElement.setAttribute("month", m < 10 ? "0" + m : Integer.toString(m));
		documentElement.setAttribute("date", date < 10 ? "0" + date : Integer.toString(date));

		/*
		 身份
		 */
		boolean isAdministrative = loverService.hasRole(
			mofo,
			Servant.ROLE_ADMINISTRATOR
		);
		boolean isFinancial = loverService.hasRole(
			mofo,
			Servant.ROLE_ACCOUNTANT
		);
		if (isAdministrative) {
			//万能天神
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinancial) {
			//财务会计
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		return servant.documentElement(document, authentication);
	}

	/**
	 * 一天内注册的新用户号。
	 *
	 * @param year 年
	 * @param month 月
	 * @param dayOfMonth 日
	 * @param authentication
	 * @return org.w3.dom.Document
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public Document accountsCreatedOfTheDay(int year, int month, int dayOfMonth, Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		int fakeCount = 0, genuineCount = 0;
		Element registeredElement = document.createElement("registered");
		registeredElement.setTextContent(
			String.format(
				"%s/%s/%s",
				year,
				month,
				dayOfMonth
			)
		);
		documentElement.appendChild(registeredElement);//註冊时间

		Element genuineMaleElement = document.createElement("genuineMale");
		genuineMaleElement.setTextContent(
			Long.toString(loverService.countAccountsCreatedOfTheDay(true, year, month, dayOfMonth))
		);
		documentElement.appendChild(genuineMaleElement);//優質男仕總數

		Element genuineFemaleElement = document.createElement("genuineFemale");
		genuineFemaleElement.setTextContent(
			Long.toString(loverService.countAccountsCreatedOfTheDay(false, year, month, dayOfMonth))
		);
		documentElement.appendChild(genuineFemaleElement);//優質甜心總數

		Element accountsElement = document.createElement("accounts");
		for (Lover mofo : loverService.accountsCreatedOfTheDay(year, month, dayOfMonth)) {
			Element accountElement = document.createElement("account");

			Element idElement = document.createElement("id");
			idElement.setTextContent(mofo.getId().toString());
			accountElement.appendChild(idElement);//主键

			Element identifierElement = document.createElement("identifier");
			identifierElement.setTextContent(mofo.getIdentifier().toString());
			accountElement.appendChild(identifierElement);//識別碼

			Element nicknameElement = document.createElement("nickname");
			nicknameElement.setTextContent(mofo.getNickname());
			accountElement.appendChild(nicknameElement);//昵称

			Element loginElement = document.createElement("login");
			loginElement.setTextContent(mofo.getLogin());
			accountElement.appendChild(loginElement);//帳號

			Element genderElement = document.createElement("gender");
			genderElement.setTextContent(
				messageSource.getMessage(
					mofo.getGender() ? "gender.male" : "gender.female",
					null,
					Locale.TAIWAN//台湾化
				)
			);
			accountElement.appendChild(genderElement);//性別

			boolean fake = mofo.isFake();
			if (fake) {
				++fakeCount;
			} else {
				++genuineCount;
			}
			Element fakeElement = document.createElement("fake");
			fakeElement.setTextContent(Boolean.toString(fake));
			accountElement.appendChild(fakeElement);//伪用户号

			accountsElement.appendChild(accountElement);
		}
		documentElement.appendChild(accountsElement);
		accountsElement.setAttribute("fake", Integer.toString(fakeCount));
		accountsElement.setAttribute("genuine", Integer.toString(genuineCount));

		return document;
	}

	/**
	 * 在金流平台的后台处理完解除定期定额申请后纪录谁处理的、什么时候处理的。
	 *
	 * @param application 申请书
	 * @param handler 谁处理
	 * @return 解除定期定额申请
	 */
	@Transactional
	public StopRecurringPaymentApplication handleStopRecurringPaymentApplication(StopRecurringPaymentApplication application, Lover handler) {
		application.setHandler(handler);
		application.setHandledAt(new Date(System.currentTimeMillis()));
		return stopRecurringPaymentApplicationRepository.saveAndFlush(
			application
		);
	}

	/**
	 * @param applicant 申请人
	 * @return 是否有资格申请解除定期定额
	 */
	@Transactional(readOnly = true)
	public boolean isRecurringPaymentStoppable(Lover applicant) {
		Date vipDuration = applicant.getVip();
		if (Objects.isNull(vipDuration)) {
			LOGGER.debug(
				String.format(
					"是否有资格申请解除定期定额\n%s.isEligible(\n\t%s = {}\n);\n{}\n%s",
					getClass(),
					Lover.class,
					"未曾是贵宾"
				),
				applicant,
				vipDuration
			);
			return false;
		}
		if (vipDuration.before(new Date(System.currentTimeMillis()))) {
			LOGGER.debug(
				String.format(
					"是否有资格申请解除定期定额\n%s.isEligible(\n\t%s = {}\n);\n{}\n%s",
					getClass(),
					Lover.class,
					"已不是贵宾"
				),
				applicant,
				vipDuration
			);
			return false;
		}
		if (Objects.equals(applicant, MaleSpecies.VIP)) {
			LOGGER.debug(
				String.format(
					"是否有资格申请解除定期定额\n%s.isEligible(\n\t%s = {}\n);\n{}\n%s",
					getClass(),
					Lover.class,
					"短期貴賓不能解除"
				),
				applicant,
				vipDuration
			);
			return false;
		}

		List<StopRecurringPaymentApplication> applications = (List<StopRecurringPaymentApplication>) stopRecurringPaymentApplicationRepository.
			findByApplicantOrderByCreatedAtDesc(applicant);
		if (Objects.isNull(applications) || applications.isEmpty()) {
			LOGGER.debug(
				String.format(
					"是否有资格申请解除定期定额\n%s.isEligible(\n\t%s = {}\n);\n{}\n%s",
					getClass(),
					Lover.class,
					"未曾申请过"
				),
				applicant,
				applications
			);
			return true;
		}

		StopRecurringPaymentApplication application = applications.get(0);
		LOGGER.debug(
			String.format(
				"是否有资格申请解除定期定额\n%s.isEligible(\n\t%s = {}\n);\n{}\n{}\n%s",
				getClass(),
				Lover.class,
				"是否处理过"
			),
			applicant,
			application.getHandledAt(),
			application.getHandler()
		);
		return Objects.nonNull(application.getHandledAt()) && Objects.nonNull(application.getHandler());
	}

	/**
	 * 安心认证审核。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return DOM 文件
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@Transactional(readOnly = true)
	public Document certification(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		for (Lover lover : loverRepository.findByRelief(false)) {
			Element loverElement = document.createElement("lover");

			loverElement.setAttribute(
				"id",
				lover.getId().toString()
			);

			loverElement.setAttribute(
				"identifier",
				lover.getIdentifier().toString()
			);

			loverElement.setAttribute(
				"name",
				lover.getNickname()
			);

			documentElement.appendChild(loverElement);
		}

		List<Behavior> behaviors = new ArrayList<Behavior>();
		behaviors.add(BEHAVIOR_CERTIFICATION_SUCCESS);
		behaviors.add(BEHAVIOR_CERTIFICATION_FAIL);
		behaviors.add(BEHAVIOR_CERTIFICATION_FAIL_1);
		behaviors.add(BEHAVIOR_CERTIFICATION_FAIL_2);
		behaviors.add(BEHAVIOR_CERTIFICATION_FAIL_3);
		for (History history : historyRepository.findByBehaviorInOrderByOccurredDesc(behaviors)) {
			Element recordElement = document.createElement("record");

			recordElement.setAttribute(
				"identifier",
				history.getPassive().getIdentifier().toString()
			);

			recordElement.setAttribute(
				"name",
				history.getPassive().getNickname()
			);

			recordElement.setAttribute(
				"date",
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				)
			);

			recordElement.setAttribute(
				"result",
				messageSource.getMessage(
					history.getBehavior().toString(),
					null,
					locale
				)
			);

			documentElement.appendChild(recordElement);
		}

		return document;
	}

	/**
	 * 核发体验码。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return DOM 文件
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@Transactional(readOnly = true)
	public Document generateTrialCode(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		for (TrialCode trialCode : trialCodeRepository.findAll()) {
			Element trialElement = document.createElement("trial");

			trialElement.setAttribute(
				"trialCodeID",
				trialCode.getId().toString()
			);
			trialElement.setAttribute(
				"keyOpinionLeader",
				trialCode.getKeyOpinionLeader()
			);
			trialElement.setAttribute(
				"code",
				trialCode.getCode()
			);

			documentElement.appendChild(trialElement);
		}

		return document;
	}

	/**
	 * 新增文章標籤
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@Transactional(readOnly = true)
	public Document generateHashtags(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		for (ForumThreadTag forumThreadTag : forumThreadTagRepository.findAllByOrderById()) {
			Element hashtagElement = document.createElement("hashtag");

			hashtagElement.setAttribute(
				"id",
				forumThreadTag.getId().toString()
			);
			hashtagElement.setAttribute(
				"phrase",
				forumThreadTag.getPhrase()
			);

			documentElement.appendChild(hashtagElement);
		}

		return document;
	}

	/**
	 * 长期贵宾解除定期定额。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return DOM 文件
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@Transactional(readOnly = true)
	public Document stopRecurringDocument(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		/*
		 未處理
		 */
		for (StopRecurringPaymentApplication stopRecurringPaymentApplication : stopRecurringPaymentApplicationRepository.findAllByHandlerNullOrderByCreatedAtDesc()) {
			Element pendingElement = document.createElement("pending");
			documentElement.appendChild(pendingElement);

			Lover applicant = stopRecurringPaymentApplication.getApplicant();

			pendingElement.setAttribute(
				"applyID",
				stopRecurringPaymentApplication.getId().toString()
			);

			pendingElement.setAttribute(
				"id",
				applicant.getId().toString()
			);

			pendingElement.setAttribute(
				"identifier",
				applicant.getIdentifier().toString()
			);

			pendingElement.setAttribute(
				"name",
				applicant.getNickname()
			);

			pendingElement.setAttribute(
				"expiry",
				Servant.DATE_TIME_FORMATTER_yyyyMMdd.format(
					Servant.toTaipeiZonedDateTime(
						applicant.getVip()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID
					)
				)
			);
		}

		/*
		 已處理
		 */
		for (StopRecurringPaymentApplication stopRecurringPaymentApplication : stopRecurringPaymentApplicationRepository.findAllByHandlerNotNullOrderByCreatedAtDesc()) {
			Element finishedElement = document.createElement("finished");

			Lover applicant = stopRecurringPaymentApplication.getApplicant();

			finishedElement.setAttribute(
				"id",
				applicant.getId().toString()
			);

			finishedElement.setAttribute(
				"identifier",
				applicant.getIdentifier().toString()
			);

			finishedElement.setAttribute(
				"name",
				applicant.getNickname()
			);

			finishedElement.setAttribute(
				"handleDate",
				Servant.DATE_TIME_FORMATTER_yyyyMMdd.format(
					Servant.toTaipeiZonedDateTime(
						stopRecurringPaymentApplication.
							getHandledAt()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID
					)
				)
			);

			documentElement.appendChild(finishedElement);
		}

		return document;
	}

	/**
	 * 甜心提取车马费。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return DOM 文件
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@Transactional(readOnly = true)
	public Document withdrawal(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		Element recordsElement = document.createElement("records");
		for (EachWithdrawal eachWithdrawal : withdrawalRecordRepository.findAllGroupByHoneyAndStatusAndWayAndTimeStamp()) {
			Lover honey = eachWithdrawal.getHoney();

			Element recordElement = document.createElement("record");
			recordsElement.appendChild(recordElement);

			recordElement.setAttribute(
				"identifier",
				honey.getIdentifier().toString()
			);

			recordElement.setAttribute(
				"name",
				honey.getNickname()
			);

			recordElement.setAttribute(
				"date",
				Servant.DATE_TIME_FORMATTER_yyyyMMdd.format(
					Servant.toTaipeiZonedDateTime(
						eachWithdrawal.getTimestamp()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				));

			Date timestamp = eachWithdrawal.getTimestamp();
			long epoch = timestamp.getTime();
			recordElement.setAttribute(
				"timestamp",
				Long.toString(epoch)
			);

			if (!eachWithdrawal.getStatus() && Objects.equals(eachWithdrawal.getWay(), WayOfWithdrawal.WIRE_TRANSFER)) {
				Element wireTransferElement = document.createElement("wireTransfer");
				recordElement.appendChild(wireTransferElement);
				WithdrawalInfo withdrawalInfo = withdrawalInfoRepository.findByHoney(eachWithdrawal.getHoney());
				wireTransferElement.setAttribute(
					"bankCode",
					withdrawalInfo.getWireTransferBankCode()
				);
				wireTransferElement.setAttribute(
					"branchCode",
					withdrawalInfo.getWireTransferBranchCode()
				);
				wireTransferElement.setAttribute(
					"accountName",
					withdrawalInfo.getWireTransferAccountName()
				);
				wireTransferElement.setAttribute(
					"accountNumber",
					withdrawalInfo.getWireTransferAccountNumber()
				);
			}

			if (Objects.equals(eachWithdrawal.getWay(), WayOfWithdrawal.PAYPAL)) {
				Element paypalElement = document.createElement("paypal");
				recordElement.appendChild(paypalElement);
			}

			// 總共提領金額
			recordElement.setAttribute(
				"points",
				Long.toString(Math.round(eachWithdrawal.getPoints() * 0.9))
			);

			Boolean status = eachWithdrawal.getStatus();
			recordElement.setAttribute(
				"status",
				status.toString()
			);

			for (WithdrawalRecord withdrawalRecord : withdrawalRecordRepository.findByHoneyAndStatusAndTimestamp(honey, status, timestamp)) {
				Element historyElement = document.createElement("history");
				recordElement.appendChild(historyElement);
				historyElement.setAttribute(
					"date",
					Servant.DATE_TIME_FORMATTER_yyyyMMdd.format(
						Servant.toTaipeiZonedDateTime(
							withdrawalRecord.
								getHistory().
								getOccurred()
						).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID
						)
					)
				);

				historyElement.setAttribute(
					"male",
					withdrawalRecord.
						getHistory().
						getInitiative().
						getNickname()
				);

				historyElement.setAttribute(
					"maleId",
					withdrawalRecord.
						getHistory().
						getInitiative().
						getIdentifier().
						toString()
				);

				historyElement.setAttribute(
					"type",
					messageSource.getMessage(
						withdrawalRecord.
							getHistory().
							getBehavior().
							name(),
						null,
						locale
					));

				historyElement.setAttribute(
					"mePoints",
					Short.toString(withdrawalRecord.getPoints())
				);

				historyElement.setAttribute(
					"points",
					Long.toString(Math.round(withdrawalRecord.getPoints() * 0.9))
				);
			}
		}
		documentElement.appendChild(recordsElement);

		return document;
	}

	@Transactional(readOnly = true)
	public Document members(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();

		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		document.getDocumentElement().setAttribute(
			"title",
			messageSource.getMessage(
				"title.members",
				null,
				locale
			)
		);

		// 權限管理
		for (Role role : roleRepository.findAll()) {
			Element roleElement = document.createElement("role");
			roleElement.setAttribute("roleID", role.getId().toString());
			roleElement.setTextContent(
				messageSource.getMessage(
					role.getTextualRepresentation(),
					null,
					locale
				)
			);
			documentElement.appendChild(roleElement);
		}

		Pageable pageable = PageRequest.of(0, 10);

		//所有男士
		Element maleElement = document.createElement("male");
		documentElement.appendChild(maleElement);

		Page<Lover> malePage = loverRepository.findAllByGenderOrderByIdDesc(true, pageable);
		maleElement.setAttribute("maleTotalPage", Integer.toString(malePage.getTotalPages()));
		for (Lover lover : malePage.getContent()) {
			Element userElement = document.createElement("user");
			maleElement.appendChild(userElement);

			Element nicknameElement = document.createElement("nickname");
			nicknameElement.setTextContent(lover.getNickname());
			userElement.appendChild(nicknameElement);

			Element idElement = document.createElement("id");
			idElement.setTextContent(lover.getId().toString());
			userElement.appendChild(idElement);

			Element identifierElement = document.createElement("identifier");
			identifierElement.setTextContent(lover.getIdentifier().toString());
			userElement.appendChild(identifierElement);

			Element loginElement = document.createElement("login");
			loginElement.setTextContent(lover.getLogin());
			userElement.appendChild(loginElement);

			Element registeredElement = document.createElement("registered");
			registeredElement.setTextContent(
				LoverService.DATE_FORMATTER.format(
					servant.
						toTaipeiZonedDateTime(
							lover.getRegistered()
						).
						withZoneSameInstant(
							Servant.ASIA_TAIPEI_ZONE_ID
						)
				));
			userElement.appendChild(registeredElement);

			Date vipExpiration = lover.getVip();
			if (loverService.isVIP(lover)) {
				Element vipElement = document.createElement("vip");
				vipElement.setTextContent(
					LoverService.DATE_FORMATTER.format(
						servant.
							toTaipeiZonedDateTime(
								vipExpiration
							).
							withZoneSameInstant(
								Servant.ASIA_TAIPEI_ZONE_ID
							)
					)
				);
				userElement.appendChild(vipElement);
			}
			if (loverService.isVVIP(lover)) {
				Element vvipElement = document.createElement("vvip");
				vvipElement.setTextContent(
					LoverService.DATE_FORMATTER.format(
						servant.
							toTaipeiZonedDateTime(
								vipExpiration
							).
							withZoneSameInstant(
								Servant.ASIA_TAIPEI_ZONE_ID
							)
					)
				);
				userElement.appendChild(vvipElement);
			}
			if (loverService.isTrial(lover)) {
				Element trialElement = document.createElement("trial");
				trialElement.setTextContent(
					LoverService.DATE_FORMATTER.format(
						servant.
							toTaipeiZonedDateTime(
								vipExpiration
							).
							withZoneSameInstant(
								Servant.ASIA_TAIPEI_ZONE_ID
							)
					)
				);
				userElement.appendChild(trialElement);
			}
		}

		//所有甜心
		Element femaleElement = document.createElement("female");
		documentElement.appendChild(femaleElement);

		Page<Lover> femalePage = loverRepository.findAllByGenderOrderByIdDesc(false, pageable);
		femaleElement.setAttribute("femaleTotalPage", Integer.toString(femalePage.getTotalPages()));
		for (Lover lover : femalePage.getContent()) {
			Element userElement = document.createElement("user");
			femaleElement.appendChild(userElement);

			Element nicknameElement = document.createElement("nickname");
			nicknameElement.setTextContent(lover.getNickname());
			userElement.appendChild(nicknameElement);

			Element idElement = document.createElement("id");
			idElement.setTextContent(lover.getId().toString());
			userElement.appendChild(idElement);

			Element identifierElement = document.createElement("identifier");
			identifierElement.setTextContent(lover.getIdentifier().toString());
			userElement.appendChild(identifierElement);

			Element loginElement = document.createElement("login");
			loginElement.setTextContent(lover.getLogin());
			userElement.appendChild(loginElement);

			Element registeredElement = document.createElement("registered");
			registeredElement.setTextContent(
				LoverService.DATE_FORMATTER.format(
					servant.
						toTaipeiZonedDateTime(
							lover.getRegistered()
						).
						withZoneSameInstant(
							Servant.ASIA_TAIPEI_ZONE_ID
						)
				));
			userElement.appendChild(registeredElement);
		}

		return document;
	}

	/**
	 * 更新會員的權限
	 *
	 * @param role
	 * @param someone
	 * @return
	 */
	@Transactional
	public JSONObject updatePrivilege(Role role, Lover someone) {
		Set<Short> rolesID = new HashSet<>();
		privilegeRepository.findByLover(someone).forEach(privilege -> {
			rolesID.add(privilege.getRole().getId());
		});

		Privilege privilege;
		if (rolesID.contains(role.getId())) {
			/*
			 已存在则删除
			 */
			privilege = privilegeRepository.
				findOneByLoverAndRole(someone, role).
				orElseThrow();
			privilegeRepository.delete(privilege);
		} else {
			/*
			 不存在则创建
			 */
			PrivilegeKey id = new PrivilegeKey();
			id.setLoverId(someone.getId());
			id.setRoleId(role.getId());

			privilege = new Privilege();
			privilege.setId(id);
			privilege.setLover(someone);
			privilege.setRole(role);
			privilegeRepository.save(privilege);
		}
		privilegeRepository.flush();

		return new JavaScriptObjectNotation().
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 顯示會員的權限
	 *
	 * @param someone
	 * @return
	 */
	public JSONObject privilege(Lover someone) {
		Set<Short> rolesID = new HashSet<>();
		privilegeRepository.findByLover(someone).forEach(privilege -> {
			rolesID.add(privilege.getRole().getId());
		});
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(rolesID).
			toJSONObject();
	}

	/**
	 * 更新優質會員
	 *
	 * @param someone
	 * @return
	 */
	@Transactional
	public JSONObject updateGenuineMember(Lover someone) {
		if (someone.isFake()) {
			someone.setFake(false);
		} else {
			someone.setFake(true);
		}
		loverRepository.saveAndFlush(someone);

		return new JavaScriptObjectNotation().
			withResponse(true).
			withReason("更新成功").
			toJSONObject();
	}

	/**
	 * 男仕 ME 點紀錄頁面
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@Transactional(readOnly = true)
	public Document mePointsRecords(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();
		Element documentElement = documentElement(
			document,
			authentication
		);//根元素

		Element recordsElement = document.createElement("records");
		documentElement.appendChild(recordsElement);
		for (History history : historyRepository.findByBehaviorOrderByOccurredDesc(BEHAVIOR_FARE)) {
			Lover male = history.getInitiative();
			Lover female = history.getPassive();

			Element recordElement = document.createElement("record");
			recordsElement.appendChild(recordElement);

			recordElement.setAttribute(
				"historyId",
				history.getId().toString()
			);

			recordElement.setAttribute(
				"maleIdentifier",
				male.getIdentifier().toString()
			);

			recordElement.setAttribute(
				"maleNickname",
				male.getNickname()
			);

			recordElement.setAttribute(
				"maleLogin",
				male.getLogin()
			);

			recordElement.setAttribute(
				"femaleIdentifier",
				female.getIdentifier().toString()
			);

			recordElement.setAttribute(
				"femaleNickname",
				female.getNickname()
			);

			recordElement.setAttribute(
				"femaleLogin",
				female.getLogin()
			);

			recordElement.setAttribute(
				"date",
				Servant.DATE_TIME_FORMATTER_yyyyMMdd.format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				));

			recordElement.setAttribute(
				"mePoints",
				Integer.toString(Math.abs(history.getPoints()))
			);

			if (Objects.nonNull(historyRepository.findByBehaviorAndHistory(BEHAVIOR_RETURN_FARE, history))) {
				recordElement.setAttribute(
					"notAbleToReturn",
					null
				);
			}
		}

		return document;
	}

	/**
	 * 退回 ME 點
	 *
	 * @param fareHistory
	 * @return
	 */
	@Transactional
	public JSONObject returnFare(History fareHistory) {
		Lover male = fareHistory.getInitiative();
		Lover female = fareHistory.getPassive();

		// 新增歷程
		History returnFareHistory = new History(
			female,
			male,
			BEHAVIOR_RETURN_FARE,
			(short) Math.abs(fareHistory.getPoints()),
			fareHistory
		);
		historyRepository.saveAndFlush(returnFareHistory);

		// 推送通知給對方
		webSocketServer.sendNotification(
			male.getIdentifier().toString(),
			String.format(
				"%s退回您給的 ME 點!",
				female.getNickname()
			));
		if (loverService.hasLineNotify(male)) {
			// LINE Notify
			lineMessagingService.notify(
				male,
				String.format(
					"有養蜜退回您給的 ME 點..馬上查看 https://%s/activeLogs.asp",
					Servant.LOCALHOST
				)
			);
		}
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"returnFare.done",
				null,
				Locale.TAIWAN//台湾化
			)).
			withResponse(true).
			toJSONObject();
	}

	@Transactional
	public JSONObject upgradeVip(Lover lover, Date date) {
		lover.setVip(date);
		lover.setMaleSpecies(MaleSpecies.VVIP);
		loverRepository.saveAndFlush(lover);

		return new JavaScriptObjectNotation().
			withReason("成功").
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 小编发送文字讯息与站内会员做聊天室互动
	 *
	 * @param gender 性别
	 * @param afterThisDate 在此日期之后
	 * @param content 讯息内容
	 * @param moderator 小编
	 * @return 历程们
	 */
	public List<History> broadcast(boolean gender, Date afterThisDate, String content, Lover moderator) {
		final Behavior behavior = gender ? HistoryService.BEHAVIOR_GREETING : HistoryService.BEHAVIOR_CHAT_MORE;
		final List<Lover> suckers = Objects.isNull(afterThisDate) ? loverRepository.findByGenderAndFakeFalse(gender) : loverRepository.findByGenderAndFakeFalseAndRegisteredAfter(gender, afterThisDate);

		List<History> histories = new ArrayList<>();
		for (Lover mofo : suckers) {
			History history = new History(
				moderator,
				mofo,
				behavior
			);
			history.setComment(content);

			histories.add(history);
		}

		return historyRepository.saveAllAndFlush(histories);
	}
}
