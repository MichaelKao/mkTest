package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.entity.WithdrawalInfo;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord.WayOfWithdrawal;
import tw.musemodel.dingzhiqingren.model.EachWithdrawal;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.StopRecurringPaymentApplicationRepository;
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

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
	private WithdrawalInfoRepository withdrawalInfoRepository;

	@Autowired
	private WithdrawalRecordRepository withdrawalRecordRepository;

	public Document withdrawalDocument(Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		Element recordsElement = document.createElement("records");
		documentElement.appendChild(recordsElement);

		for (EachWithdrawal eachWithdrawal : withdrawalRecordRepository.findAllGroupByHoneyAndStatusAndWayAndTimeStamp()) {
			Element recordElement = document.createElement("record");
			recordsElement.appendChild(recordElement);

			Lover honey = eachWithdrawal.getHoney();
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
				DATE_TIME_FORMATTER.format(
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

			recordElement.setAttribute(
				"points",
				eachWithdrawal.getPoints().toString()
			);

			Boolean status = eachWithdrawal.getStatus();
			recordElement.setAttribute(
				"status",
				status.toString()
			);

			for (WithdrawalRecord withdrawalRecord
				: withdrawalRecordRepository.findByHoneyAndStatusAndTimestamp(honey, status, timestamp)) {
				Element historyElement = document.createElement("history");
				recordElement.appendChild(historyElement);
				historyElement.setAttribute(
					"date",
					DATE_TIME_FORMATTER.format(
						servant.toTaipeiZonedDateTime(
							withdrawalRecord.getHistory().getOccurred()
						).withZoneSameInstant(Servant.ASIA_TAIPEI)
					));

				historyElement.setAttribute(
					"male",
					withdrawalRecord.getHistory().getInitiative().getNickname()
				);

				historyElement.setAttribute(
					"maleId",
					withdrawalRecord.getHistory().getInitiative().getIdentifier().toString()
				);

				historyElement.setAttribute(
					"type",
					messageSource.getMessage(
						withdrawalRecord.getHistory().getBehavior().name(),
						null,
						locale
					));

				historyElement.setAttribute(
					"points",
					Short.toString(withdrawalRecord.getPoints())
				);

			}
		}
		return document;
	}

	/**
	 * 在金流平台的后台处理完解除定期定额申请后纪录谁处理的、什么时候处理的。
	 *
	 * @param application 申请书
	 * @param handler 谁处理
	 * @return 解除定期定额申请
	 */
	public StopRecurringPaymentApplication handleStopRecurringPaymentApplication(StopRecurringPaymentApplication application, Lover handler) {
		application.setHandledAt(new Date(System.currentTimeMillis()));
		application.setHandler(handler);
		return stopRecurringPaymentApplicationRepository.saveAndFlush(application);
	}

	/**
	 * @param applicant 申请人
	 * @return 是否有资格申请解除定期定额
	 */
	@Transactional(readOnly = true)
	public boolean isEligible(Lover applicant) {
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
			return true;
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

	public Document certificationDocument(Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		for (Lover lover : loverRepository.findByRelief(Boolean.FALSE)) {
			Element loverElement = document.createElement("lover");
			documentElement.appendChild(loverElement);

			loverElement.setAttribute(
				"id",
				lover.getId().toString()
			);

			loverElement.setAttribute(
				"name",
				lover.getNickname()
			);
		}
		return document;
	}
}
