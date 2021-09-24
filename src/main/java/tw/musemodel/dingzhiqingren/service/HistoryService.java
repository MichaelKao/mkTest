package tw.musemodel.dingzhiqingren.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import lombok.Data;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.WebSocketServer;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.LineGivenKey;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.embedded.Follow;
import tw.musemodel.dingzhiqingren.entity.embedded.FollowKey;
import tw.musemodel.dingzhiqingren.model.Activity;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.FollowRepository;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import static tw.musemodel.dingzhiqingren.service.Servant.UTF_8;
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
	private FollowRepository followRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LineGivenRepository lineGivenRepository;

	@Autowired
	private LoverRepository loverRepository;

	@Value("classpath:sql/与其它用户近期的对话.sql")
	private Resource latestConversationsResource;

	@Data
	private class Dialogist implements Serializable {

		private static final long serialVersionUID = -7377929167558711132L;

		private final int initiative;

		private final int passive;

		@Override
		public String toString() {
			try {
				return new JsonMapper().writeValueAsString(this);
			} catch (JsonProcessingException ignore) {
				return "null";
			}
		}
	}

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
	 * 历程：要求車馬費
	 */
	public static final Behavior BEHAVIOR_ASK_FOR_FARE = Behavior.YAO_CHE_MA_FEI;

	/**
	 * 历程：退回車馬費
	 */
	public static final Behavior BEHAVIOR_RETURN_FARE = Behavior.TUI_HUI_CHE_MA_FEI;

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
	 * 历程：短期贵宾体验
	 */
	public static final Behavior BEHAVIOR_TRIAL_CODE = Behavior.DUAN_QI_GUI_BIN_TI_YAN;

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
	 * 历程：放行生活照
	 */
	public static final Behavior BEHAVIOR_PICTURES_VIEWABLE = Behavior.FANG_XING_SHENG_HUO_ZHAO;

	/**
	 * 历程：可放行生活照
	 */
	public static final Behavior BEHAVIOR_ACCEPT_PICTURES_VIEWABLE = Behavior.KE_FANG_XING;

	/**
	 * 历程：不放行生活照
	 */
	public static final Behavior BEHAVIOR_REFUSE_PICTURES_VIEWABLE = Behavior.BU_FANG_XING;

	/**
	 * 聊天相关行为
	 */
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	public final static Collection<History.Behavior> BEHAVIORS_OF_CONVERSATIONS = Arrays.asList(new History.Behavior[]{
		BEHAVIOR_CHAT_MORE,
		BEHAVIOR_GREETING,
		BEHAVIOR_GROUP_GREETING,
		BEHAVIOR_FARE,
		BEHAVIOR_ASK_FOR_FARE,
		BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
		BEHAVIOR_INVITE_ME_AS_LINE_FRIEND,
		BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND,
		BEHAVIOR_LAI_KOU_DIAN
	});

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
	 * 与其它用户近期的对话。
	 *
	 * @param me 用户号
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<History> latestConversations(final Lover me) {
		int id = me.getId();
		List<Dialogist> candidates = jdbcTemplate.query(
			(Connection connection) -> {
				StringBuilder stringBuilder = new StringBuilder();
				BEHAVIORS_OF_CONVERSATIONS.forEach(behavior -> {
					stringBuilder.append(String.format(
						"'%s',",
						behavior.toString()
					));
				});
				final String CONVERSATIONAL_BEHAVIORS = stringBuilder.
					toString().
					replaceAll(
						",$",
						""
					);

				PreparedStatement preparedStatement;
				try {
					preparedStatement = connection.prepareStatement(
						String.format(
							FileCopyUtils.copyToString(
								new InputStreamReader(
									latestConversationsResource.getInputStream(),
									UTF_8
								)
							),
							CONVERSATIONAL_BEHAVIORS,
							CONVERSATIONAL_BEHAVIORS
						)
					);
					preparedStatement.setInt(1, id);
					preparedStatement.setInt(2, id);
					return preparedStatement;
				} catch (IOException ex) {
					return null;
				}
			},
			(ResultSet resultSet, int rowNum) -> {
				return new Dialogist(
					resultSet.getInt("zhu_dong_de"),
					resultSet.getInt("bei_dong_de")
				);
			}
		);

		List<Dialogist> dialogists = new ArrayList<>();
		Iterator<Dialogist> iterator = candidates.iterator();
		while (iterator.hasNext()) {
			Dialogist candidate = iterator.next(),
				dialogist = new Dialogist(
					candidate.getPassive(),
					candidate.getInitiative()
				);
			if (!dialogists.contains(candidate) && !dialogists.contains(dialogist)) {
				dialogists.add(candidate);
			}
		}

		List<History> histories = new ArrayList<>();
		dialogists.forEach(dialogist -> {
			histories.add(historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorInOrderByOccurredDesc(
				loverRepository.
					findById(dialogist.getInitiative()).
					orElseThrow(),
				loverRepository.
					findById(dialogist.getPassive()).
					orElseThrow(),
				BEHAVIORS_OF_CONVERSATIONS
			));
		});
		return histories;
	}

	/**
	 * 两用户之间的最后一次对话。
	 *
	 * @param initiative 某用户
	 * @param passive 另一用户
	 * @return 最后一次对话
	 */
	@Transactional(readOnly = true)
	public History lastConversation(Lover initiative, Lover passive) {
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
	 * @param locale 语言环境
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
					Servant.LOCALHOST
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
					Servant.LOCALHOST
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
	 * @param locale 语言环境
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

		Collection<Lover> following = loverService.getThoseIFollow(
			initiative
		);
		for (Lover followed : following) {
			if (Objects.equals(passive, followed)) {
				FollowKey followKey = new FollowKey();
				followKey.setFollowingId(initiative.getId());
				followKey.setFollowedId(passive.getId());

				Follow follow = followRepository.
					findById(followKey).
					orElseThrow();
				followRepository.delete(follow);
				followRepository.flush();

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

		FollowKey followKey = new FollowKey();
		followKey.setFollowingId(initiative.getId());
		followKey.setFollowedId(passive.getId());
		Follow follow = new Follow();
		follow.setId(followKey);
		follow.setFollowing(initiative);
		follow.setFollowed(passive);
		followRepository.saveAndFlush(follow);

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
	 * @param locale 语言环境
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
					new LineGivenKey(passive.getId(), initiative.getId()),
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
					Servant.LOCALHOST
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
	 * @param locale 语言环境
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
					Servant.LOCALHOST
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
	 * @param locale 语言环境
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
					Servant.LOCALHOST
				));
		}

		History historyReply = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
			passive, initiative, BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		historyReply.setReply(new Date(System.currentTimeMillis()));
		historyRepository.saveAndFlush(historyReply);

		LineGiven lineGiven = new LineGiven(
			new LineGivenKey(initiative.getId(), passive.getId()),
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
	 * @param locale 语言环境
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
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"openLine.upgardeVipToOpen",
					null,
					locale
				)).
				withResponse(false).
				withRedirect("/upgrade.asp").
				toJSONObject();
		}

		Boolean isLine = Servant.isLine(URI.create(female.getInviteMeAsLineFriend()));
		Boolean isWeChat = Servant.isWeChat(URI.create(female.getInviteMeAsLineFriend()));
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
	 * @param rate
	 * @param comment
	 * @param locale
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
		Lover female = initiative.getGender() ? passive : initiative;
		Lover male = initiative.getGender() ? initiative : passive;
		LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(female, male);
		if (Objects.isNull(lineGiven) || Objects.isNull(lineGiven.getResponse()) || !lineGiven.getResponse()) {
			throw new RuntimeException("rate.notAbleToCommentBeforeBeingFriend");
		}

		// 已評價過，要編輯內容
		History history = historyRepository.findByInitiativeAndPassiveAndBehavior(initiative, passive, BEHAVIOR_RATE);
		if (Objects.isNull(history)) {
			history = new History(
				initiative,
				passive,
				BEHAVIOR_RATE
			);
		}

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
	 * @param locale 语言环境
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
					Servant.LOCALHOST
				));
		}

		LineGiven lineGiven = new LineGiven(
			new LineGivenKey(initiative.getId(), passive.getId()),
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
		Calendar twelveHours = Calendar.getInstance();
		twelveHours.add(Calendar.HOUR, -12);

		Date twelveHoursAgo = twelveHours.getTime();

		Long dailyCount = historyRepository.countByInitiativeAndBehaviorAndOccurredBetween(
			male,
			BEHAVIOR_LAI_KOU_DIAN,
			twelveHoursAgo,
			new Date(System.currentTimeMillis())
		);
		return (loverService.isVVIP(male) || loverService.isVIP(male)) && Objects.nonNull(dailyCount) && dailyCount < VIP_DAILY_TOLERANCE;
	}

	/**
	 * 找出需顯示在歷程頁面的所有歷程
	 *
	 * @param lover
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Activity> findActiveLogsOrderByOccurredDesc(Lover lover) {
		List<Activity> list = new ArrayList<>();

		Collection<Behavior> BEHAVIORS_ANNO_IGNORE = Arrays.asList(new History.Behavior[]{
			BEHAVIOR_GREETING,
			BEHAVIOR_GROUP_GREETING,
			BEHAVIOR_CHAT_MORE
		//			BEHAVIOR_PEEK,
		//			BEHAVIOR_FOLLOW
		});

		for (History history : historyRepository.findByInitiativeAndBehaviorNotIn(lover, BEHAVIORS_ANNO_IGNORE)) {
			Activity activeLogs = new Activity(
				history.getId(),
				lover,
				history.getPassive(),
				history.getBehavior(),
				history.getOccurred(),
				history.getPoints(),
				history.getGreeting(),
				history.getSeen(),
				history.getReply()
			);
			if (Objects.equals(history.getBehavior(), BEHAVIOR_PICTURES_VIEWABLE)) {
				history.setShowAllPictures(history.getShowAllPictures());
			}
			list.add(activeLogs);
		}
		for (History history : historyRepository.findByPassiveAndBehaviorNotIn(lover, BEHAVIORS_ANNO_IGNORE)) {
			Activity activeLogs = new Activity(
				history.getId(),
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
	@Transactional
	public Document historiesToDocument(Lover lover) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		// 確認性別
		Boolean isMale = lover.getGender();

		// 將通知已讀
		Date now = new Date(System.currentTimeMillis());
		List<History> historyList = new ArrayList<>();
		for (History history : loverService.annoucementHistories(lover)) {
			history.setSeen(now);
			historyList.add(history);
		}
		historyRepository.saveAllAndFlush(historyList);

		List<Activity> activeLogsList = findActiveLogsOrderByOccurredDesc(lover);

		for (Activity activeLogs : activeLogsList) {
			Lover initiative = activeLogs.getInitiative();
			Lover passive = activeLogs.getPassive();
			// 這幾個行為主動者不須通知
			if (activeLogs.getBehavior() == BEHAVIOR_RATE || activeLogs.getBehavior() == BEHAVIOR_FOLLOW || activeLogs.getBehavior() == BEHAVIOR_PEEK
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
			boolean isBlacklisted = !loverService.getBlockers(initiative).contains(passive) && !loverService.getBlockeds(initiative).contains(passive);

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
			if (activeLogs.getBehavior() == BEHAVIOR_TRIAL_CODE) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					message = String.format(
						"您已開啟單日 VIP 體驗"
					);
					identifier = initiativeIdentifier;
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_GIMME_YOUR_LINE_INVITATION) {
				if (isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您已向%s要求通訊軟體",
						passiveNickname
					);
				}
				if (!isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s向您要求通訊軟體",
						initiativeNickname
					);
					if (Objects.isNull(activeLogs.getReply()) && isBlacklisted) {
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
					if (Objects.nonNull(lineGiven) && lineGiven.getResponse() && isBlacklisted) {
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
					if (Objects.isNull(historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(passive, initiative, BEHAVIOR_RATE)) && isBlacklisted) {
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
					if (Objects.isNull(historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(initiative, passive, BEHAVIOR_RATE)) && isBlacklisted) {
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
			if (activeLogs.getBehavior() == BEHAVIOR_PICTURES_VIEWABLE) {
				if (Objects.equals(initiative, lover)) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您向%s要求生活照授權",
						passiveNickname
					);
				}
				if (Objects.equals(passive, lover)) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s向您要求生活照授權",
						initiativeNickname
					);
					History history = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(
						initiative,
						passive,
						BEHAVIOR_PICTURES_VIEWABLE
					);
					if (Objects.nonNull(history) && !history.getShowAllPictures()) {
						historyElement.setAttribute(
							"pixAuthBtn",
							null
						);
					}
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_ACCEPT_PICTURES_VIEWABLE) {
				if (Objects.equals(initiative, lover)) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您已同意給%s看您的生活照",
						passiveNickname
					);
				}
				if (Objects.equals(passive, lover)) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s同意給您看生活照",
						initiativeNickname
					);
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_LAI_KOU_DIAN) {
				if (isMale) {
					profileImage = passiveProfileImage;
					message = String.format(
						"您加入%s的通訊軟體",
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
					if (ableToReturnFare(historyRepository.findById(activeLogs.getId()).orElseThrow())) {
						historyElement.setAttribute(
							"returnFareId",
							activeLogs.getId().toString()
						);
						historyElement.setAttribute(
							"ableToReturn",
							null
						);
					}
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_ASK_FOR_FARE) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s向您要求 %d 車馬費",
						initiativeNickname,
						Math.abs(activeLogs.getPoints())
					);
					if (Objects.isNull(activeLogs.getReply())) {
						historyElement.setAttribute(
							"replyFareReqBtn",
							null
						);
						historyElement.setAttribute(
							"historyId",
							activeLogs.getId().toString()
						);
					}
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您向%s要求 %d 車馬費",
						passiveNickname,
						Math.abs(activeLogs.getPoints())
					);
				}
			}
			if (activeLogs.getBehavior() == BEHAVIOR_RETURN_FARE) {
				if (isMale) {
					profileImage = initiativeProfileImage;
					identifier = initiativeIdentifier;
					message = String.format(
						"%s退回您給的 %d 車馬費",
						initiativeNickname,
						Math.abs(activeLogs.getPoints())
					);
				}
				if (!isMale) {
					profileImage = passiveProfileImage;
					identifier = passiveIdentifier;
					message = String.format(
						"您已退回%s給您的 %d 車馬費",
						passiveNickname,
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

			// 人數較多後拿掉***********
			if (activeLogs.getBehavior() == BEHAVIOR_FOLLOW) {
				profileImage = initiativeProfileImage;
				identifier = initiativeIdentifier;
				message = String.format(
					"%s已收藏您",
					initiativeNickname
				);
			}
			// 人數較多後拿掉***********
			if (activeLogs.getBehavior() == BEHAVIOR_PEEK) {
				profileImage = initiativeProfileImage;
				identifier = initiativeIdentifier;
				message = String.format(
					"%s已看過您",
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
					Servant.STATIC_HOST,
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

	/**
	 * 要求生活照授權
	 *
	 * @param initiative
	 * @param passive
	 * @return
	 */
	@Transactional
	public JSONObject picturesAuth(Lover initiative, Lover passive) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("picturesAuth.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("picturesAuth.passiveMustntBeNull");
		}
		if (Objects.equals(initiative, passive)) {
			throw new RuntimeException("picturesAuth.mustBeDifferent");
		}
		if (Objects.equals(initiative.getGender(), passive.getGender())) {
			throw new RuntimeException("picturesAuth.mustBeStraight");
		}
		if (initiative.getGender() && !loverService.isVIP(initiative) && !loverService.isVVIP(initiative)) {
			return new JavaScriptObjectNotation().
				withReason("non-vip").
				withResponse(false).
				toJSONObject();
		}

		History history = historyRepository.saveAndFlush(new History(
			initiative,
			passive,
			BEHAVIOR_PICTURES_VIEWABLE
		));
		// 推送通知給對方
		webSocketServer.sendNotification(
			passive.getIdentifier().toString(),
			String.format(
				"%s和您要求生活照授權!",
				initiative.getNickname()
			));
		if (loverService.hasLineNotify(passive)) {
			// LINE Notify
			lineMessagingService.notify(
				passive,
				String.format(
					"有養蜜和您要求生活照授權..馬上查看 https://%s/activeLogs.asp",
					Servant.LOCALHOST
				));
		}
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history.getOccurred()).
			toJSONObject();
	}

	@Transactional
	public JSONObject acceptPixAuth(Lover acceptant, Lover requester) {
		if (Objects.isNull(acceptant)) {
			throw new IllegalArgumentException("picturesAuth.initiativeMustntBeNull");
		}
		if (Objects.isNull(requester)) {
			throw new IllegalArgumentException("picturesAuth.passiveMustntBeNull");
		}
		if (Objects.equals(acceptant, requester)) {
			throw new RuntimeException("picturesAuth.mustBeDifferent");
		}
		if (Objects.equals(acceptant.getGender(), requester.getGender())) {
			throw new RuntimeException("picturesAuth.mustBeStraight");
		}
		toggleShowAllPictures(requester, acceptant);
		History history = new History(acceptant, requester, BEHAVIOR_ACCEPT_PICTURES_VIEWABLE);
		historyRepository.saveAndFlush(history);
		// 推送通知給對方
		webSocketServer.sendNotification(
			requester.getIdentifier().toString(),
			String.format(
				"%s同意給您看生活照!",
				acceptant.getNickname()
			));
		if (loverService.hasLineNotify(requester)) {
			// LINE Notify
			lineMessagingService.notify(
				requester,
				String.format(
					"有養蜜同意給您看生活照..馬上查看 https://%s/activeLogs.asp",
					Servant.LOCALHOST
				));
		}
		return new JavaScriptObjectNotation().
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 48小時內可以退回車馬費
	 *
	 * @param history
	 * @return
	 */
	public boolean ableToReturnFare(History history) {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -2);

		// 車馬費歷程的行為是'車馬費'
		// 兩天前的時戳在車馬費歷程之前
		// 還沒有被退回過('退回車馬費'的歷程是空的)
		return Objects.equals(history.getBehavior(), BEHAVIOR_FARE)
			&& cal.getTime().before(history.getOccurred())
			&& Objects.isNull(historyRepository.findByBehaviorAndHistory(BEHAVIOR_RETURN_FARE, history));
	}

	/**
	 * 退回車馬費
	 *
	 * @param fareHistory
	 * @param locale 语言环境
	 * @return
	 */
	@Transactional
	public JSONObject returnFare(History fareHistory, Locale locale) {
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
				"%s退回您給的看車馬費!",
				female.getNickname()
			));
		if (loverService.hasLineNotify(male)) {
			// LINE Notify
			lineMessagingService.notify(
				male,
				String.format(
					"有養蜜退回您給的車馬費..馬上查看 https://%s/activeLogs.asp",
					Servant.LOCALHOST
				));
		}
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"returnFare.done",
				null,
				locale
			)).
			withResponse(true).
			toJSONObject();
	}
}
