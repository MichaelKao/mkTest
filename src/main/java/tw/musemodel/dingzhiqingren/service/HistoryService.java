package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.net.URI;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.specification.HistorySpecification;

/**
 * 服务层：历程
 *
 * @author p@musemodel.tw
 */
@Service
public class HistoryService {

	private final static Logger LOGGER = LoggerFactory.getLogger(HistoryService.class);

	private final static Short COST_GIMME_YOUR_LINE_INVITATION = Short.valueOf(System.getenv("COST_GIMME_YOUR_LINE_INVITATION"));

	private final static long VIP_DAILY_TOLERANCE = 30L;

	@Autowired
	private Servant servant;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private WebSocketServer webSocketServer;

	@Autowired
	private LineMessagingService lineMessagingService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LineGivenRepository lineGivenRepository;

	@Autowired
	private LoverRepository loverRepository;

	/**
	 * 历程：要求車馬費
	 */
	public static final Behavior BEHAVIOR_ASK_FOR_FARE = Behavior.YAO_CHE_MA_FEI;

	/**
	 * 历程：充值行为
	 */
	public static final Behavior BEHAVIOR_CHARGED = Behavior.CHU_ZHI;

	/**
	 * 历程：再聊聊
	 */
	public static final Behavior BEHAVIOR_CHAT_MORE = Behavior.LIAO_LIAO;

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
	 * 历程：賴要求的扣點(VIP超多要求上限)
	 */
	public static final Behavior BEHAVIOR_LAI_KOU_DIAN = Behavior.LAI_KOU_DIAN;

	/**
	 * 历程：月费行为
	 */
	public static final Behavior BEHAVIOR_MONTHLY_CHARGED = Behavior.YUE_FEI;

	/**
	 * 历程：看过我行为
	 */
	public static final Behavior BEHAVIOR_PEEK = Behavior.KAN_GUO_WO;

	/**
	 * 历程：評價
	 */
	public static final Behavior BEHAVIOR_RATE = Behavior.PING_JIA;

	/**
	 * 历程：收藏
	 */
	public static final Behavior BEHAVIOR_FOLLOW = Behavior.SHOU_CANG;

	/**
	 * 历程：不收藏
	 */
	public static final Behavior BEHAVIOR_UNFOLLOW = Behavior.BU_SHOU_CANG;

	/**
	 * 历程：提領成功
	 */
	public static final Behavior BEHAVIOR_WITHDRAWAL_SUCCESS = Behavior.TI_LING_CHENG_GONG;

	/**
	 * 历程：提領失敗
	 */
	public static final Behavior BEHAVIOR_WITHDRAWAL_FAIL = Behavior.TI_LING_SHI_BAI;

	/**
	 * 历程：安心認證通過
	 */
	public static final Behavior BEHAVIOR_CERTIFICATION_SUCCESS = Behavior.AN_XIN_CHENG_GONG;

	/**
	 * 历程：安心認證不通過
	 */
	public static final Behavior BEHAVIOR_CERTIFICATION_FAIL = Behavior.AN_XIN_SHI_BAI;

	/**
	 * 历程：甜心群發打招呼
	 */
	public static final Behavior BEHAVIOR_GROUP_GREETING = Behavior.QUN_FA;

	/**
	 * 历程：甜心要求車馬費
	 */
	public static final Behavior BEHAVIOR_REQ_FARE = Behavior.YAO_CHE_MA_FEI;

	/**
	 * 历程：放行生活照
	 */
	public static final Behavior BEHAVIOR_PICTURES_VIEWABLE = Behavior.FANG_XING_SHENG_HUO_ZHAO;

	/**
	 * 主动方是否愿意给被动方看生活照。
	 *
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @return 主动方是否放行生活照给被动方
	 */
	@Transactional(readOnly = true)
	public boolean arePicturesViewable(Lover initiative, Lover passive) {
		History history = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
			initiative,
			passive,
			BEHAVIOR_PICTURES_VIEWABLE
		);
		return Objects.nonNull(history) && history.getShowAllPictures();
	}

	/**
	 * 两用户之间的最后一次对话。
	 *
	 * @param initiative 某用户
	 * @param passive 另一用户
	 * @return 最后一次对话
	 */
	public History lastConversationOfTwo(Lover initiative, Lover passive) {
		final Pageable limitOne = PageRequest.of(0, 1);
		Page<History> initiativePage = historyRepository.findAll(
			HistorySpecification.conversationsOfTwo(initiative, passive),
			limitOne
		), passivePage = historyRepository.findAll(
			HistorySpecification.conversationsOfTwo(passive, initiative),
			limitOne
		);

		List<History> conversations = new ArrayList<>();
		conversations.addAll(initiativePage.toList());
		conversations.addAll(passivePage.toList());

		if (conversations.isEmpty()) {
			return null;
		}
		Collections.sort(conversations, (History history, History otherHistory) -> Long.compare(
			history.getOccurred().getTime(),
			otherHistory.getOccurred().getTime()
		));
		Collections.reverse(conversations);
		return conversations.get(0);
	}

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
		if (points < 1) {
			throw new RuntimeException("fare.pointsMustBeValid");//點數不能為負數
		}
		if (points(initiative) < Math.abs(points)) {
			throw new RuntimeException("fare.insufficientPoints");//点数不足
		}
		History history = new History(
			initiative,
			passive,
			BEHAVIOR_FARE,
			(short) -points
		);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給女生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s給了妳車馬費%d!",
				initiative.getNickname(),
				points
			));
		if (loverService.hasLineNotify(passive)) {
			// LINE Notify
			lineMessagingService.notify(
				passive,
				String.format(
					"有一位男仕給妳車馬費！馬上查看 https://%s/activeLogs.asp",
					servant.LOCALHOST
				));
		}

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

	@Transactional
	public JSONObject reqFare(Lover initiative, Lover passive, short points, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("fare.initiativeMustntBeNull");//无主动方
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("fare.passiveMustntBeNull");//无被动方
		}
		if (Objects.equals(initiative.getGender(), true)) {
			throw new RuntimeException("reqFare.initiativeMustBeFemale");//主动方为女
		}
		if (Objects.equals(passive.getGender(), false)) {
			throw new RuntimeException("reqFare.passiveMustBeMale");//被动方为男
		}
		if (points < 1) {
			throw new RuntimeException("fare.pointsMustBeValid");//點數不能為負數
		}
		History history = new History(
			initiative,
			passive,
			BEHAVIOR_ASK_FOR_FARE,
			(short) points
		);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給男生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s和你要求車馬費%d!",
				initiative.getNickname(),
				points
			));
		if (loverService.hasLineNotify(passive)) {
			// LINE Notify
			lineMessagingService.notify(
				passive,
				String.format(
					"有一位甜心和你要求車馬費！馬上查看 https://%s/activeLogs.asp",
					servant.LOCALHOST
				));
		}

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"reqFare.done",
				null,
				locale
			)).
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	/**
	 * 收藏
	 *
	 * @param initiative
	 * @param passive
	 * @param locale
	 * @return
	 */
	@Transactional
	public JSONObject follow(Lover initiative, Lover passive, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("follow.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("follow.passiveMustntBeNull");
		}
		if (Objects.equals(initiative, passive)) {
			throw new RuntimeException("follow.mustBeDifferent");
		}
		if (Objects.equals(initiative.getGender(), passive.getGender())) {
			throw new RuntimeException("follow.mustBeStraight");
		}

		Set<Lover> following = initiative.getFollowing();
		for (Lover followed : following) {
			if (Objects.equals(passive, followed)) {
				following.remove(followed);
				initiative.setFollowing(following);
				loverRepository.saveAndFlush(initiative);
				return new JavaScriptObjectNotation().
					withReason(messageSource.getMessage(
						"unfollow.done",
						null,
						locale
					)).
					withResponse(true).
					toJSONObject();
			}
		}

		following.add(passive);
		initiative.setFollowing(following);
		loverRepository.saveAndFlush(initiative);

		History history = new History(
			initiative,
			passive,
			BEHAVIOR_FOLLOW
		);
		historyRepository.saveAndFlush(history);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"follow.done",
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
	public JSONObject gimme(Lover initiative, Lover passive, Locale locale) {
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
		LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(passive, initiative);
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
				//離上一次拒絕不到12小時
				History history = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(passive, initiative, BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND);
				if (Objects.nonNull(history) && within12hrsFromLastRefused(history)) {
					throw new RuntimeException("gimmeYourLineInvitation.within12hrsFromLastRefused");
				}
				//被拒絕過
				lineGiven.setResponse(null);
				lineGivenRepository.saveAndFlush(lineGiven);
			}
		}
		History history = new History(
			initiative,
			passive,
			BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		history = historyRepository.saveAndFlush(history);

		// 推送通知給女生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s和妳要求通訊軟體",
				initiative.getNickname()
			));

		if (loverService.hasLineNotify(passive)) {
			// LINE Notify
			lineMessagingService.notify(
				passive,
				String.format(
					"您收到一位男仕的通訊軟體要求！馬上查看 https://%s/activeLogs.asp",
					servant.LOCALHOST
				));
		}

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
	public JSONObject greet(Lover initiative, Lover passive, String greetingMessage, Locale locale) {
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
		if (loverService.hasLineNotify(passive)) {
			// LINE Notify
			lineMessagingService.notify(
				passive,
				String.format(
					"有位甜心向你打招呼！馬上查看 https://%s/activeLogs.asp",
					servant.LOCALHOST
				));
		}

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
				"%s已答應給你通訊軟體!",
				initiative.getNickname()
			));
		if (loverService.hasLineNotify(passive)) {
			// LINE Notify
			lineMessagingService.notify(
				passive,
				String.format(
					"有位甜心答應給你通訊軟體！馬上查看 https://%s/activeLogs.asp",
					servant.LOCALHOST
				));
		}

		History historyReply = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
			passive, initiative, BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		historyReply.setReply(new Date(System.currentTimeMillis()));
		historyRepository.saveAndFlush(historyReply);

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
	 * 打開女生的 LINE
	 *
	 * @param male
	 * @param female
	 * @return
	 */
	@Transactional
	public JSONObject openLine(Lover male, Lover female, Locale locale) {
		History history = historyRepository.findByInitiativeAndPassiveAndBehavior(male, female, BEHAVIOR_LAI_KOU_DIAN);
		if ((loverService.isVIP(male) || loverService.isVVIP(male)) && Objects.isNull(history)) {
			History historyReply
				= historyRepository.findByInitiativeAndPassiveAndBehavior(female, male, BEHAVIOR_INVITE_ME_AS_LINE_FRIEND);
			historyReply.setReply(new Date(System.currentTimeMillis()));
			historyRepository.saveAndFlush(historyReply);
			if (!withinRequiredLimit(male)) {
				Short cost = COST_GIMME_YOUR_LINE_INVITATION;
				if (points(male) < Math.abs(cost)) {
					throw new RuntimeException("openLine.insufficientPoints");//点数不足
				}
				historyRepository.saveAndFlush(new History(
					male,
					female,
					BEHAVIOR_LAI_KOU_DIAN,
					cost
				));
			} else if (withinRequiredLimit(male)) {
				historyRepository.saveAndFlush(new History(
					male,
					female,
					BEHAVIOR_LAI_KOU_DIAN,
					(short) 0
				));
			}
		} else if (!loverService.isVIP(male) && !loverService.isVVIP(male)) {
			throw new RuntimeException("openLine.upgardeVipToOpen");
		}

		Boolean isLine = servant.isLine(URI.create(female.getInviteMeAsLineFriend()));
		Boolean isWeChat = servant.isWeChat(URI.create(female.getInviteMeAsLineFriend()));
		String redirect = null;
		if (isLine) {
			redirect = female.getInviteMeAsLineFriend();
		}
		if (isWeChat) {
			redirect = String.format("/%s.png", female.getIdentifier());
		}

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"openLine.done",
				null,
				locale
			)).
			withResponse(true).
			withRedirect(redirect).
			withResult(isLine ? "isLine" : "isWeChat").
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
	 * 剩余点数。
	 *
	 * @param lover 用户号
	 * @return 点数
	 */
	@Transactional(readOnly = true)
	public Long points(Lover lover) {
		if (Objects.isNull(lover)) {
			throw new IllegalArgumentException("points.loverMustntBeNull");
		}
		return historyRepository.sumByInitiativeHearts(lover);
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
		if (historyRepository.countByInitiativeAndPassiveAndBehavior(initiative, passive, BEHAVIOR_RATE) > 0) {
			throw new RuntimeException("rate.onlyCanRateOnce");
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
			withReason(messageSource.getMessage(
				"rate.done",
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

		// 把男生的要求轉為已回應
		History historyReply = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
			passive, initiative, BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		historyReply.setReply(new Date(System.currentTimeMillis()));
		historyRepository.saveAndFlush(historyReply);

		// 推送通知給男生
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s已拒絕給你通訊軟體!",
				initiative.getNickname()
			));
		if (loverService.hasLineNotify(passive)) {
			// LINE Notify
			lineMessagingService.notify(
				passive,
				String.format(
					"有位甜心拒絕給你通訊軟體..馬上查看 https://%s/activeLogs.asp",
					servant.LOCALHOST
				));
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
	 * 从头重新排序主键值。
	 *
	 * @return 重新排序的历程们
	 */
	@Transactional
	public int[] reorderPrimaryKey() {
		List<SqlParameterSource> sqlParameterSources = new ArrayList<>();
		List<Long> jiuList = jdbcTemplate.query(
			"SELECT\"id\"FROM\"li_cheng\"ORDER BY\"id\"",
			(resultSet, rowNum) -> resultSet.getLong("id")
		);
		int xin = 0;
		for (long jiu : jiuList) {
			++xin;
			if (xin < jiu) {
				sqlParameterSources.add(new MapSqlParameterSource().
					addValue(
						"xin",
						xin,
						Types.BIGINT
					).
					addValue(
						"jiu",
						jiu,
						Types.BIGINT
					)
				);
			}
		}
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
			jdbcTemplate.getDataSource()
		);
		return namedParameterJdbcTemplate.batchUpdate(
			"UPDATE\"li_cheng\"SET\"id\"=:xin WHERE\"id\"=:jiu",
			sqlParameterSources.toArray(new SqlParameterSource[0])
		);
	}

	/**
	 * 若主动方未同意或同意则拒绝，反之则同意。
	 *
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @return 主动方是否放行生活照给被动方
	 */
	public boolean toggleShowAllPictures(Lover initiative, Lover passive) {
		History history = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
			initiative,
			passive,
			BEHAVIOR_PICTURES_VIEWABLE
		);
		if (Objects.isNull(history)) {
			history = new History(
				initiative,
				passive,
				BEHAVIOR_PICTURES_VIEWABLE
			);
		}
		Boolean showAllPictures = history.getShowAllPictures();
		history.setShowAllPictures(
			!(Objects.isNull(showAllPictures) || showAllPictures)
		);
		return historyRepository.
			saveAndFlush(history).
			getShowAllPictures();
	}

	/**
	 * 距離上次被拒絕 12 小時內
	 *
	 * @param history
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean within12hrsFromLastRefused(History history) {
		Date nowDate = new Date();
		Calendar refused = Calendar.getInstance();
		refused.setTime(history.getOccurred());
		refused.add(Calendar.HOUR, 12);
		Date refusedDate = refused.getTime();

		return nowDate.before(refusedDate);
	}

	/**
	 * 男生開啟 加入通訊軟體 12小時內未超過上限值
	 *
	 * @param male
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean withinRequiredLimit(Lover male) {
		Date twelveHrsAgo = null;
		Date nowDate = null;
		Calendar twelveHrs = Calendar.getInstance();
		twelveHrs.add(Calendar.HOUR, -12);
		twelveHrsAgo = twelveHrs.getTime();
		nowDate = new Date();
		Long dailyCount = historyRepository.countByInitiativeAndBehaviorAndOccurredBetween(
			male,
			BEHAVIOR_LAI_KOU_DIAN,
			twelveHrsAgo,
			nowDate
		);
		return (loverService.isVVIP(male) || loverService.isVIP(male)) && Objects.nonNull(dailyCount) && dailyCount < VIP_DAILY_TOLERANCE;
	}

	@Transactional(readOnly = true)
	public List<Activity> findActiveLogsOrderByOccurredDesc(Lover lover) {
		List<Activity> list = new ArrayList<Activity>();

		for (History history : historyRepository.findByInitiativeAndBehaviorNot(lover, BEHAVIOR_PEEK)) {
			Activity activeLogs = new Activity(
				lover,
				history.getPassive(),
				history.getBehavior(),
				history.getOccurred(),
				history.getPoints(),
				history.getGreeting(),
				history.getSeen(),
				history.getReply()
			);
			list.add(activeLogs);
		}
		for (History history : historyRepository.findByPassiveAndBehaviorNot(lover, BEHAVIOR_PEEK)) {
			Activity activeLogs = new Activity(
				history.getInitiative(),
				lover,
				history.getBehavior(),
				history.getOccurred(),
				history.getPoints(),
				history.getGreeting(),
				history.getSeen(),
				history.getReply()
			);
			list.add(activeLogs);
		}

		Collections.sort(list, Comparator.reverseOrder());
		return list;
	}

	/**
	 * 歷程 document
	 *
	 * @param lover
	 * @return
	 * @throws org.xml.sax.SAXException
	 * @throws javax.xml.parsers.ParserConfigurationException
	 * @throws java.io.IOException
	 */
	@Transactional(readOnly = true)
	public Document historiesToDocument(Lover lover) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		// 確認性別
		Boolean isMale = lover.getGender();

		// 將通知已讀
		List<History> histories = loverService.annoucementHistories(lover);
		for (History history : histories) {
			history.setSeen(new Date(System.currentTimeMillis()));
			historyRepository.saveAndFlush(history);
		}

		List<Activity> activeLogsList = findActiveLogsOrderByOccurredDesc(lover);

		for (Activity activeLogs : activeLogsList) {
			Lover initiative = activeLogs.getInitiative();
			Lover passive = activeLogs.getPassive();
			// 這幾個行為主動者不須通知
			if (activeLogs.getBehavior() == BEHAVIOR_RATE || activeLogs.getBehavior() == BEHAVIOR_FOLLOW
				|| activeLogs.getBehavior() == BEHAVIOR_WITHDRAWAL_FAIL || activeLogs.getBehavior() == BEHAVIOR_WITHDRAWAL_SUCCESS
				|| activeLogs.getBehavior() == BEHAVIOR_CERTIFICATION_FAIL || activeLogs.getBehavior() == BEHAVIOR_CERTIFICATION_SUCCESS
				|| activeLogs.getBehavior() == BEHAVIOR_GROUP_GREETING) {
				if (Objects.equals(lover, initiative)) {
					continue;
				}
			}
			String initiativeIdentifier = initiative.getIdentifier().toString();
			String initiativeProfileImage = initiative.getProfileImage();
			String initiativeNickname = initiative.getNickname();
			String passiveIdentifier = null;
			String passiveProfileImage = null;
			String passiveNickname = null;
			if (Objects.nonNull(activeLogs.getPassive())) {
				passiveIdentifier = passive.getIdentifier().toString();
				passiveProfileImage = passive.getProfileImage();
				passiveNickname = passive.getNickname();
			}
			String identifier = null;
			String profileImage = null;
			String message = null;

			Element historyElement = document.createElement("history");
			documentElement.appendChild(historyElement);
			if (activeLogs.getBehavior() == BEHAVIOR_CHARGED) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					message = String.format(
						"您儲值了 %d 愛心",
						Math.abs(activeLogs.getPoints())
					);
					identifier = initiativeIdentifier;
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_MONTHLY_CHARGED) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					message = String.format(
						"升級 VIP 費用扣款 $1688"
					);
					identifier = initiativeIdentifier;
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_GIMME_YOUR_LINE_INVITATION) {
				if (isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您已向 %s 要求通訊軟體",
						passiveNickname
					);
				}
				if (!isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s向您要求通訊軟體：%s",
						initiativeNickname,
						activeLogs.getGreeting()
					);
					if (Objects.isNull(activeLogs.getReply())
						&& (!initiative.getBlocking().contains(passive) && !initiative.getBlockedBy().contains(passive))) {
						historyElement.setAttribute(
							"decideButton",
							null
						);
					}
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_INVITE_ME_AS_LINE_FRIEND) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s同意給您通訊軟體",
						initiativeNickname
					);
					LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(initiative, passive);
					if (Objects.nonNull(lineGiven) && lineGiven.getResponse() && (!initiative.getBlocking().contains(passive) && !initiative.getBlockedBy().contains(passive))) {
						historyElement.setAttribute(
							"addLineButton",
							null
						);
						Long count = historyRepository.countByInitiativeAndPassiveAndBehavior(passive, initiative, BEHAVIOR_LAI_KOU_DIAN);
						if ((loverService.isVIP(passive) || loverService.isVVIP(passive)) && (count < 1 && !withinRequiredLimit(passive))) {
							historyElement.setAttribute(
								"remindDeduct",
								null
							);
						}
					}
					if (Objects.isNull(
						historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(passive, initiative, BEHAVIOR_RATE))
						&& (!initiative.getBlocking().contains(passive) && !initiative.getBlockedBy().contains(passive))) {
						historyElement.setAttribute(
							"rateButton",
							null
						);
					}
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您已同意給 %s 通訊軟體",
						passiveNickname
					);
					if (Objects.isNull(
						historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(initiative, passive, BEHAVIOR_RATE))
						&& (!initiative.getBlocking().contains(passive) && !initiative.getBlockedBy().contains(passive))) {
						historyElement.setAttribute(
							"rateButton",
							null
						);
					}
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s拒絕給您通訊軟體",
						initiativeNickname
					);
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您已拒絕給 %s 通訊軟體",
						passiveNickname
					);
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_GREETING || activeLogs.getBehavior() == BEHAVIOR_GROUP_GREETING) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s向您打招呼：%s",
						initiativeNickname,
						activeLogs.getGreeting()
					);
					LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(initiative, passive);
					if (Objects.isNull(lineGiven) || (Objects.isNull(lineGiven.getResponse()) || !lineGiven.getResponse())
						&& (!initiative.getBlocking().contains(passive) && !initiative.getBlockedBy().contains(passive))) {
						historyElement.setAttribute(
							"requestLineButton",
							null
						);
					}
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您已向 %s 打招呼",
						passiveNickname
					);
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_LAI_KOU_DIAN) {
				if (isMale) {
					profileImage = passiveProfileImage;
					message = String.format(
						"您取得 %s 的通訊軟體，扣 %d 點",
						passiveNickname,
						Math.abs(activeLogs.getPoints())
					);
					identifier = passiveIdentifier;
				}
				if (!isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s加了您的通訊軟體",
						initiativeNickname
					);
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_FARE) {
				if (isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您給了%s %d 車馬費",
						passiveNickname,
						Math.abs(activeLogs.getPoints())
					);
				}
				if (!isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"您收到了來自%s的 %d 車馬費",
						initiativeNickname,
						Math.abs(activeLogs.getPoints())
					);
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_RATE) {
				profileImage = initiativeProfileImage;
				identifier = initiativeIdentifier;
				message = String.format(
					"%s給予您評價",
					initiativeNickname
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_WITHDRAWAL_SUCCESS) {
				profileImage = passiveProfileImage;
				identifier = passiveIdentifier;
				message = String.format(
					"您提領的 %s 車馬費已成功匯款!",
					activeLogs.getGreeting()
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_WITHDRAWAL_FAIL) {
				profileImage = passiveProfileImage;
				identifier = passiveIdentifier;
				message = String.format(
					"您欲提領的車馬費失敗，請重新提領。"
				);
			}
			if (activeLogs.getBehavior() == BEHAVIOR_CERTIFICATION_SUCCESS) {
				profileImage = passiveProfileImage;
				message = String.format(
					"您已通過安心認證審核!"
				);
				identifier = passiveIdentifier;
			}
			if (activeLogs.getBehavior() == BEHAVIOR_CERTIFICATION_FAIL) {
				profileImage = passiveProfileImage;
				message = String.format(
					"您申請的安心認證不通過，請重新上傳正確手持證件照!"
				);
				identifier = passiveIdentifier;
			}
			// 歷程時間
			historyElement.setAttribute(
				"time",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(activeLogs.getOccurred())
			);
			// 顯示的大頭貼
			historyElement.setAttribute(
				"profileImage",
				String.format(
					"https://%s/profileImage/%s",
					servant.STATIC_HOST,
					profileImage
				)
			);
			// 歷程內容
			historyElement.setAttribute(
				"message",
				message
			);
			// 識別碼(個人檔案連結用)
			historyElement.setAttribute(
				"identifier",
				identifier
			);
		}
		return document;
	}
}
