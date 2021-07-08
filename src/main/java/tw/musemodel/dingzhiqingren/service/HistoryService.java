package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.WebSocketServer;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.LineGivenPK;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Activity;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;

/**
 * 服务层：历程
 *
 * @author p@musemodel.tw
 */
@Service
public class HistoryService {

	private final static Logger LOGGER = LoggerFactory.getLogger(HistoryService.class);

	private final static Short COST_GIMME_YOUR_LINE_INVITATION = Short.valueOf(System.getenv("COST_GIMME_YOUR_LINE_INVITATION"));

	private final static long VIP_DAILY_TOLERANCE = 10L;

	@Autowired
	private Servant servant;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LineGivenRepository lineGivenRepository;

	@Autowired
	private LoverService loverService;

	@Autowired
	private WebSocketServer webSocketServer;

	/**
	 * 历程：充值行为
	 */
	public static final Behavior BEHAVIOR_CHARGED = Behavior.CHU_ZHI;

	/**
	 * 历程：车马费(男对女)行为
	 */
	public static final Behavior BEHAVIOR_FARE = Behavior.CHE_MA_FEI;

	/**
	 * 历程：给我赖(男对女)行为
	 */
	public static final Behavior BEHAVIOR_GIMME_YOUR_LINE_INVITATION = Behavior.JI_WO_LAI;

	/**
	 * 历程：打招呼(女对男)行为
	 */
	public static final Behavior BEHAVIOR_GREETING = Behavior.DA_ZHAO_HU;

	/**
	 * 历程：给你赖(女对男)行为
	 */
	public static final Behavior BEHAVIOR_INVITE_ME_AS_LINE_FRIEND = Behavior.JI_NI_LAI;

	/**
	 * 历程：不给你赖(女对男)行为
	 */
	public static final Behavior BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND = Behavior.BU_JI_LAI;

	/**
	 * 历程：賴要求的退點(拒絕或過期)
	 */
	public static final Behavior BEHAVIOR_LAI_TUI_DIAN = Behavior.LAI_TUI_DIAN;

	/**
	 * 历程：月费行为
	 */
	public static final Behavior BEHAVIOR_MONTHLY_CHARGED = Behavior.YUE_FEI;

	/**
	 * 历程：看过我行为
	 */
	public static final Behavior BEHAVIOR_PEEK = Behavior.KAN_GUO_WO;

	public static final Behavior BEHAVIOR_RATE = Behavior.PING_JIA;

	/**
	 * 车马费(男对女)
	 *
	 * @param initiative 男生
	 * @param passive 女生
	 * @param points 点数
	 * @param locale
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject fare(Lover initiative, Lover passive, short points, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("fare.initiativeMustntBeNull");//无主动方
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("fare.passiveMustntBeNull");//无被动方
		}
		if (Objects.equals(initiative.getGender(), false)) {
			throw new RuntimeException("fare.initiativeMustBeMale");//主动方为女
		}
		if (Objects.equals(passive.getGender(), true)) {
			throw new RuntimeException("fare.passiveMustBeFemale");//被动方为男
		}
		if (points(initiative) < Math.abs(points)) {
			throw new RuntimeException("fare.insufficientPoints");//点数不足
		}
		History history = new History(
			initiative,
			passive,
			BEHAVIOR_FARE,
			points
		);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給女生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s打賞車馬費%d給妳!",
				initiative.getNickname(),
				points
			));

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"fare.done",
				null,
				locale
			)).
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	/**
	 * 给我赖(男对女)
	 *
	 * @param initiative 男生
	 * @param passive 女生
	 * @param greetingMessage 招呼语
	 * @param locale
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject gimme(Lover initiative, Lover passive, String greetingMessage, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("gimmeYourLineInvitation.initiativeMustntBeNull");//无主动方
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("gimmeYourLineInvitation.passiveMustntBeNull");//无被动方
		}
		if (Objects.equals(initiative.getGender(), false)) {
			throw new RuntimeException("gimmeYourLineInvitation.initiativeMustBeMale");//主动方为女
		}
		if (Objects.equals(passive.getGender(), true)) {
			throw new RuntimeException("gimmeYourLineInvitation.passiveMustBeFemale");//被动方为男
		}
		if (Objects.isNull(greetingMessage) || greetingMessage.isBlank()) { //招呼語不能為空
			throw new RuntimeException("gimmeYourLineInvitation.greetingMessageMustntBeNull");
		}
		LineGiven lineGiven = lineGivenRepository.findByFemaleAndMale(passive, initiative);
		if (Objects.isNull(lineGiven)) {
			//第一次
			lineGivenRepository.saveAndFlush(
				new LineGiven(
					new LineGivenPK(passive.getId(), initiative.getId()),
					null
				));
		} else if (Objects.nonNull(lineGiven)) {
			Boolean okay = lineGiven.getResponse();
			if (Objects.isNull(okay)) {
				//女生未回應
				throw new RuntimeException("gimmeYourLineInvitation.femaleHasntResponsed");
			}

			if (okay) {
				//女生已經同意給過 LINE
				throw new RuntimeException("gimmeYourLineInvitation.femaleHasGivenLine");
			} else {
				//被拒絕過
				lineGiven.setResponse(null);
				lineGivenRepository.saveAndFlush(lineGiven);
			}
		}
		short cost = COST_GIMME_YOUR_LINE_INVITATION;
		long currentTimeMillis = System.currentTimeMillis();
		Date vip = initiative.getVip();
		if (Objects.nonNull(vip) && vip.after(new Date(currentTimeMillis))) {
			Long dailyCount = historyRepository.countByInitiativeAndBehaviorAndOccurredBetween(
				initiative,
				BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
				Servant.minimumToday(currentTimeMillis),
				Servant.maximumToday(currentTimeMillis)
			);
			if (Objects.isNull(dailyCount) || dailyCount <= VIP_DAILY_TOLERANCE) {
				cost = 0;
			}
		} else {
			if (points(initiative) < Math.abs(cost)) {
				throw new RuntimeException("gimmeYourLineInvitation.insufficientPoints");//点数不足
			}
		}
		History history = new History(
			initiative,
			passive,
			BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
			cost
		);
		history.setGreeting(greetingMessage);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給女生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s和妳要求LINE：「%s」",
				initiative.getNickname(),
				greetingMessage
			));
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"gimmeYourLineInvitation.done",
				null,
				locale
			)).
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	/**
	 * 打招呼(女对男)
	 *
	 * @param initiative 女生
	 * @param passive 男生
	 * @param greetingMessage 招呼语
	 * @param locale
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject greet(Lover initiative, Lover passive,
		String greetingMessage, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("greet.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("greet.passiveMustntBeNull");
		}
		if (Objects.equals(initiative.getGender(), true)) {
			throw new RuntimeException("greet.initiativeMustBeFemale");
		}
		if (Objects.equals(passive.getGender(), false)) {
			throw new RuntimeException("greet.passiveMustBeMale");
		}
		if (Objects.isNull(greetingMessage) || greetingMessage.isBlank()) { //招呼語不能為空
			throw new RuntimeException("greet.greetingMessageMustntBeNull");
		}

		History history = new History(
			initiative,
			passive,
			BEHAVIOR_GREETING
		);
		history.setGreeting(greetingMessage);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給男生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s向你打招呼：「%s」",
				initiative.getNickname(),
				greetingMessage
			));
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"greet.done",
				null,
				locale
			)).
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	/**
	 * 给你赖(女对男)
	 *
	 * @param initiative 女生
	 * @param passive 男生
	 * @param locale
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject inviteMeAsLineFriend(Lover initiative, Lover passive, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("inviteMeAsLineFriend.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("inviteMeAsLineFriend.passiveMustntBeNull");
		}
		if (Objects.equals(initiative.getGender(), true)) {
			throw new RuntimeException("inviteMeAsLineFriend.initiativeMustBeFemale");
		}
		if (Objects.equals(passive.getGender(), false)) {
			throw new RuntimeException("inviteMeAsLineFriend.passiveMustBeMale");
		}
		//TODO:	男生有要求过吗？女生已给过吗？
		//Long inviteMeAsLineFriendCount = historyRepository.countByInitiativeAndPassiveAndBehavior(
		//	initiative,
		//	passive,
		//	BEHAVIOR_INVITE_ME_AS_LINE_FRIEND
		//);
		String inviteMeAsLineFriend = initiative.getInviteMeAsLineFriend();
		if (Objects.isNull(inviteMeAsLineFriend) || inviteMeAsLineFriend.isBlank()) {
			throw new RuntimeException("inviteMeAsLineFriend.mustntBeNull");
		}

		History history = new History(
			initiative,
			passive,
			BEHAVIOR_INVITE_ME_AS_LINE_FRIEND
		);
		history.setGreeting(inviteMeAsLineFriend);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給男生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s已答應給你LINE!",
				initiative.getNickname()
			));

		History historySeen = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
			passive, initiative, BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		historySeen.setSeen(new Date(System.currentTimeMillis()));
		historyRepository.saveAndFlush(historySeen);

		LineGiven lineGiven = new LineGiven(
			new LineGivenPK(initiative.getId(), passive.getId()),
			true
		);
		lineGivenRepository.saveAndFlush(lineGiven);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"inviteMeAsLineFriend.done",
				null,
				locale
			)).
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	/**
	 * 不給你賴
	 *
	 * @param initiative
	 * @param passive
	 * @param locale
	 * @return
	 */
	@Transactional
	public JSONObject refuseToBeLineFriend(Lover initiative, Lover passive, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("refuseToBeLineFriend.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("refuseToBeLineFriend.passiveMustntBeNull");
		}
		if (Objects.equals(initiative.getGender(), true)) {
			throw new RuntimeException("refuseToBeLineFriend.initiativeMustBeFemale");
		}
		if (Objects.equals(passive.getGender(), false)) {
			throw new RuntimeException("refuseToBeLineFriend.passiveMustBeMale");
		}

		History history = new History(
			initiative,
			passive,
			BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND
		);
		history = historyRepository.saveAndFlush(history);

		// 把男生的要求轉為已讀
		History historySeen = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
			passive, initiative, BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		historySeen.setSeen(new Date(System.currentTimeMillis()));
		historyRepository.saveAndFlush(historySeen);

		// 推送通知給男生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s已拒絕給你LINE!",
				initiative.getNickname()
			));

		if (historySeen.getPoints() == -30) {
			History historyTuiDian = new History(
				passive,
				initiative,
				BEHAVIOR_LAI_TUI_DIAN
			);
			historyTuiDian.setPoints((short) 30);
			historyRepository.saveAndFlush(historyTuiDian);
		}

		LineGiven lineGiven = new LineGiven(
			new LineGivenPK(initiative.getId(), passive.getId()),
			false
		);
		lineGivenRepository.saveAndFlush(lineGiven);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"refuseToBeLineFriend.done",
				null,
				locale
			)).
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	/**
	 * 看过我
	 *
	 * @param initiative 谁看了
	 * @param passive 看了谁
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject peek(Lover initiative, Lover passive) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("peek.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("peek.passiveMustntBeNull");
		}
		if (Objects.equals(initiative, passive)) {
			throw new RuntimeException("peek.mustBeDifferent");
		}
		if (Objects.equals(initiative.getGender(), passive.getGender())) {
			throw new RuntimeException("peek.mustBeStraight");
		}

		History history = historyRepository.saveAndFlush(new History(
			initiative,
			passive,
			BEHAVIOR_PEEK
		));
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	/**
	 * 星級評價
	 *
	 * @param initiative
	 * @param passive
	 * @return
	 */
	@Transactional
	public JSONObject rate(Lover initiative, Lover passive, Short rate, String comment, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("rate.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("rate.passiveMustntBeNull");
		}
		if (Objects.equals(initiative, passive)) {
			throw new RuntimeException("rate.mustBeDifferent");
		}
		if (Objects.equals(initiative.getGender(), passive.getGender())) {
			throw new RuntimeException("rate.mustBeStraight");
		}
		if (Objects.isNull(rate)) {
			throw new RuntimeException("rate.rateMustntBeNull");
		}
		if (comment.isBlank() || comment.isEmpty()) {
			throw new RuntimeException("rate.commentMustntBeNull");
		}

		History history = new History(
			initiative,
			passive,
			BEHAVIOR_RATE
		);
		history.setRate(rate);
		history.setComment(comment);
		historyRepository.saveAndFlush(history);

		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	@Transactional(readOnly = true)
	public Long points(Lover lover) {
		if (Objects.isNull(lover)) {
			throw new IllegalArgumentException("points.loverMustntBeNull");
		}
		return historyRepository.sumByInitiativeHearts(lover);
	}

	public List<Activity> findActiveLogsOrderByOccurredDesc(Lover lover) {
		List<Activity> list = new ArrayList<Activity>();

		for (History history : historyRepository.findByInitiativeAndBehaviorNot(lover, Behavior.KAN_GUO_WO)) {
			Activity activeLogs = new Activity(
				lover,
				history.getPassive(),
				history.getBehavior(),
				history.getOccurred(),
				history.getPoints(),
				history.getGreeting(),
				history.getSeen()
			);
			list.add(activeLogs);
		}
		for (History history : historyRepository.findByPassiveAndBehaviorNot(lover, Behavior.KAN_GUO_WO)) {
			Activity activeLogs = new Activity(
				history.getInitiative(),
				lover,
				history.getBehavior(),
				history.getOccurred(),
				history.getPoints(),
				history.getGreeting(),
				history.getSeen()
			);
			list.add(activeLogs);
		}
		Collections.sort(list, Comparator.reverseOrder());
		return list;
	}

	public Document historiesToDocument(Lover lover) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		// 確認性別
		Boolean isMale = loverService.isMale(lover);

		List<Activity> activeLogsList = findActiveLogsOrderByOccurredDesc(lover);

		for (Activity activeLogs : activeLogsList) {
			if (!isMale && activeLogs.getBehavior() == BEHAVIOR_LAI_TUI_DIAN) {
				continue;
			}
			String initiativeIdentifier = activeLogs.getInitiative().getIdentifier().toString();
			String initiativeProfileImage = activeLogs.getInitiative().getProfileImage();
			String initiativeNickname = activeLogs.getInitiative().getNickname();
			String passiveIdentifier = null;
			String passiveProfileImage = null;
			String passiveNickname = null;
			if (Objects.nonNull(activeLogs.getPassive())) {
				passiveIdentifier = activeLogs.getPassive().getIdentifier().toString();
				passiveProfileImage = activeLogs.getPassive().getProfileImage();
				passiveNickname = activeLogs.getPassive().getNickname();
			}
			String identifier = null;
			String profileImage = null;
			String message = null;

			Element historyElement = document.createElement("history");
			documentElement.appendChild(historyElement);
			historyElement.setAttribute(
				"time",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(activeLogs.getOccurred()
					));
			if (activeLogs.getBehavior() == BEHAVIOR_CHARGED) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					message = String.format(
						"%s%d%s",
						"您儲值了",
						Math.abs(activeLogs.getPoints()),
						"愛心點數"
					);
					identifier = initiativeIdentifier;
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_MONTHLY_CHARGED) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					message = String.format(
						"%s",
						"升級 VIP 費用扣款 $1688"
					);
					identifier = initiativeIdentifier;
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_LAI_TUI_DIAN) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					message = String.format(
						"%s%s%s%d",
						"您向",
						passiveNickname,
						"要 Line 不成功, 而退點",
						activeLogs.getPoints()
					);
					identifier = initiativeIdentifier;
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_GIMME_YOUR_LINE_INVITATION) {
				if (isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"%s%s%s",
						"您已向",
						passiveNickname,
						"要求 LINE"
					);
				}
				if (!isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s%s%s",
						initiativeNickname,
						"向您要求 Line：",
						activeLogs.getGreeting()
					);
					if (Objects.isNull(activeLogs.getSeen())) {
						historyElement.setAttribute(
							"decideButton",
							null
						);
					}
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_INVITE_ME_AS_LINE_FRIEND) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s%s",
						initiativeNickname,
						"接受給您 Line"
					);
					historyElement.setAttribute(
						"addLineButton",
						activeLogs.getInitiative().getInviteMeAsLineFriend()
					);
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"%s%s%s",
						"您已答應",
						passiveNickname,
						"給出 Line"
					);
				}
				historyElement.setAttribute(
					"rateButton",
					null
				);
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s%s",
						initiativeNickname,
						"拒絕給您 Line"
					);
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"%s%s%s",
						"您已拒絕",
						passiveNickname,
						"給出 Line"
					);
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_GREETING) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s%s%s",
						initiativeNickname,
						"向您打招呼：",
						activeLogs.getGreeting()
					);
					historyElement.setAttribute(
						"requestLineButton",
						null
					);
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"%s%s%s",
						"您已向",
						passiveNickname,
						"打招呼"
					);
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_FARE) {
				if (isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"%s%s%d%s",
						"您給了",
						passiveNickname,
						Math.abs(activeLogs.getPoints()),
						"車馬費"
					);
				}
				if (!isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s%s%d%s",
						"您收到了來自",
						initiativeNickname,
						Math.abs(activeLogs.getPoints()),
						"車馬費"
					);
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						servant.STATIC_HOST,
						profileImage
					)
				);
				historyElement.setAttribute(
					"message",
					message
				);
				historyElement.setAttribute(
					"identifier",
					identifier
				);
			}
		}
		return document;
	}
}
