package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.WithdrawalInfo;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord.WayOfWithdrawal;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
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
	private WithdrawalRecordRepository withdrawalRecordRepository;

	@Autowired
	private WithdrawalInfoRepository withdrawalInfoRepository;

	@Autowired
	private LoverRepository loverRepository;

	public Document withdrawalDocument(Locale locale)
		throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		Element recordsElement = document.createElement("records");
		documentElement.appendChild(recordsElement);

		for (WithdrawalRecord withdrawalRecord : withdrawalRecordRepository.findAllByOrderByTimestampDesc()) {
			Element recordElement = document.createElement("record");
			recordsElement.appendChild(recordElement);

			recordElement.setAttribute(
				"id",
				withdrawalRecord.getId().toString()
			);

			recordElement.setAttribute(
				"identifier",
				withdrawalRecord.getHoney().getIdentifier().toString()
			);

			recordElement.setAttribute(
				"name",
				withdrawalRecord.getHoney().getNickname()
			);

			recordElement.setAttribute(
				"date",
				DATE_TIME_FORMATTER.format(
					servant.toTaipeiZonedDateTime(
						withdrawalRecord.getTimestamp()
					).withZoneSameInstant(Servant.ASIA_TAIPEI)
				));

			if (Objects.isNull(withdrawalRecord.getStatus()) && Objects.equals(withdrawalRecord.getWay(), WayOfWithdrawal.WIRE_TRANSFER)) {
				Element wireTransferElement = document.createElement("wireTransfer");
				recordElement.appendChild(wireTransferElement);
				WithdrawalInfo withdrawalInfo = withdrawalInfoRepository.findByHoney(withdrawalRecord.getHoney());
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

			if (Objects.equals(withdrawalRecord.getWay(), WayOfWithdrawal.PAYPAL)) {
				Element paypalElement = document.createElement("paypal");
				recordElement.appendChild(paypalElement);
			}

			if (Objects.nonNull(withdrawalRecord.getStatus()) && !withdrawalRecord.getStatus()) {
				recordElement.setAttribute(
					"failReason",
					null
				);
			}

			recordElement.setAttribute(
				"points",
				withdrawalRecord.getPoints().toString()
			);

			if (Objects.nonNull(withdrawalRecord.getStatus())) {
				recordElement.setAttribute(
					"status",
					withdrawalRecord.getStatus().toString()
				);
			}
		}
		return document;
	}

	public Document certificationDocument(Locale locale)
		throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		for (Lover lover : loverRepository.findAllByRelief(Boolean.FALSE)) {
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
