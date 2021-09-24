package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Lover.MaleSpecies;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.entity.TrialCode;
import tw.musemodel.dingzhiqingren.entity.WithdrawalInfo;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord.WayOfWithdrawal;
import tw.musemodel.dingzhiqingren.model.EachWithdrawal;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.StopRecurringPaymentApplicationRepository;
import tw.musemodel.dingzhiqingren.repository.TrialCodeRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalInfoRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalRecordRepository;

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
		return stopRecurringPaymentApplicationRepository.
			saveAndFlush(application);
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
		Document document = servant.parseDocument();

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
				"name",
				lover.getNickname()
			);

			documentElement.appendChild(loverElement);
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
		Document document = servant.parseDocument();

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
		Document document = servant.parseDocument();

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
					servant.toTaipeiZonedDateTime(
						applicant.getVip()
					).withZoneSameInstant(
						Servant.ASIA_TAIPEI
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
					servant.toTaipeiZonedDateTime(
						stopRecurringPaymentApplication.
							getHandledAt()
					).withZoneSameInstant(
						Servant.ASIA_TAIPEI
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
		Document document = servant.parseDocument();

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
					servant.toTaipeiZonedDateTime(
						eachWithdrawal.getTimestamp()
					).withZoneSameInstant(Servant.ASIA_TAIPEI)
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
						servant.toTaipeiZonedDateTime(
							withdrawalRecord.
								getHistory().
								getOccurred()
						).withZoneSameInstant(
							Servant.ASIA_TAIPEI
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
}
