package tw.musemodel.dingzhiqingren.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Activity;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;

/**
 * 服务层：聊天室
 *
 * @author m@musemodel.tw
 */
@Service
public class WebSocketService {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);

	private final static int MSG_TWELVE_HRS_TOLERANCE = 3;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private LineGivenRepository lineGivenRepository;

	@Autowired
	private Servant servant;

	public List<Activity> wholeHistoryMsgs(Lover male, Lover female) {
		List<History> maleTalkHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			male,
			female,
			HistoryService.BEHAVIOR_CHAT_MORE
		);
		List<History> gimmeHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			male,
			female,
			HistoryService.BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		List<History> alreadyBeingFriendHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			male,
			female,
			HistoryService.BEHAVIOR_LAI_KOU_DIAN
		);
		List<History> fareHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			male,
			female,
			HistoryService.BEHAVIOR_FARE
		);
		List<History> femaleTalkHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			female,
			male,
			HistoryService.BEHAVIOR_GREETING
		);
		List<History> femaleGroupGreetingHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			female,
			male,
			HistoryService.BEHAVIOR_GROUP_GREETING
		);
		List<History> inviteAsFriendHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			female,
			male,
			HistoryService.BEHAVIOR_INVITE_ME_AS_LINE_FRIEND
		);
		List<History> refuseAsFriendHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			female,
			male,
			HistoryService.BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND
		);
		List<History> requestFareHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			female,
			male,
			HistoryService.BEHAVIOR_ASK_FOR_FARE
		);
		List<History> returnFareHistoryMsgs = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(
			female,
			male,
			HistoryService.BEHAVIOR_RETURN_FARE
		);
		List<Activity> wholeHistoryMsgs = new ArrayList<>();
		for (History history : maleTalkHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				servant.markdownToHtml(history.getGreeting()),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : gimmeHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : alreadyBeingFriendHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : fareHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				history.getSeen()
			);
			if (historyService.ableToReturnFare(history)) {
				activity.setAbleToReturnFare(Boolean.TRUE);
			} else {
				activity.setAbleToReturnFare(Boolean.FALSE);
			}
			activity.setPoints((short) Math.abs(history.getPoints()));
			activity.setId(history.getId());
			wholeHistoryMsgs.add(activity);
		}
		for (History history : femaleTalkHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				servant.markdownToHtml(history.getGreeting()),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : femaleGroupGreetingHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				servant.markdownToHtml(history.getGreeting()),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : inviteAsFriendHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : refuseAsFriendHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : requestFareHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				history.getSeen()
			);
			activity.setPoints(history.getPoints());
			activity.setId(history.getId());
			activity.setReply(history.getReply());
			wholeHistoryMsgs.add(activity);
		}
		for (History history : returnFareHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				),
				history.getSeen()
			);
			activity.setPoints(history.getPoints());
			wholeHistoryMsgs.add(activity);
		}
		Collections.sort(wholeHistoryMsgs);
		return wholeHistoryMsgs;
	}

	@SuppressWarnings({"UnusedAssignment", "null"})
	public Document inbox(Document document, Lover me) {
		Element documentElement = document.getDocumentElement();

		boolean isMale = me.getGender();

		List<History> conversations = historyService.latestConversations(me);
		Integer friendlyOrVipUnreads = 0,//好友或贵宾的未读总数
			unfriendlyOrNonVipUnreads = 0;//非好友或非贵宾的未读总数
		for (History history : conversations) {
			Element conversationElement = document.createElement("conversation");
			documentElement.appendChild(conversationElement);

			Short points = history.getPoints();
			Behavior behavior = history.getBehavior();
			Lover initiative = history.getInitiative(),
				passive = history.getPassive();

			UUID identifier = null;
			String profileImage = null;
			String nickname = null;
			String content = null;
			Boolean eitherMatchedOrVip = null;
			int unreads = 0;//某个人的未读讯息数
			final Collection<Behavior> BEHAVIORS_OF_MALE = Arrays.
				asList(
					new History.Behavior[]{
						BEHAVIOR_CHAT_MORE,
						BEHAVIOR_FARE,
						BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
						BEHAVIOR_LAI_KOU_DIAN
					}
				);
			final Collection<Behavior> BEHAVIORS_OF_FEMALE = Arrays.
				asList(
					new History.Behavior[]{
						BEHAVIOR_GREETING,
						BEHAVIOR_GROUP_GREETING,
						BEHAVIOR_ASK_FOR_FARE,
						BEHAVIOR_INVITE_ME_AS_LINE_FRIEND,
						BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND
					}
				);
			if (Objects.equals(me, initiative)) {
				if (isMale) {
					//双方是否有加过通信软件
					eitherMatchedOrVip = loverService.areMatched(
						passive,
						me
					);

					//未读信息数量
					unreads = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(
						passive,
						me,
						BEHAVIORS_OF_FEMALE
					);

					if (Objects.equals(BEHAVIOR_FARE, behavior)) {
						content = String.format(
							"您已給 💗 %d ME 點",
							Math.abs(points)
						);
					}
					if (Objects.equals(BEHAVIOR_GIMME_YOUR_LINE_INVITATION, behavior)) {
						content = "您已發出要求通訊軟體";
					}
					if (Objects.equals(BEHAVIOR_LAI_KOU_DIAN, behavior)) {
						content = "您開啟了對方的通訊軟體QRcode";
					}
				} else {
					eitherMatchedOrVip = loverService.isVIP(passive) || loverService.isVVIP(passive);
					unreads = historyRepository.
						countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(
							passive,
							me,
							BEHAVIORS_OF_MALE
						);
					if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
						content = String.format(
							"您已和對方要求 💗 %d ME 點",
							Math.abs(points)
						);
					}
					if (Objects.equals(BEHAVIOR_INVITE_ME_AS_LINE_FRIEND, behavior)) {
						content = "您已接受給對方通訊軟體";
					}
					if (Objects.equals(BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND, behavior)) {
						content = "您已拒絕給對方通訊軟體";
					}
				}
				identifier = passive.getIdentifier();
				profileImage = String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					passive.getProfileImage()
				);
				nickname = passive.getNickname();
			} else {
				if (isMale) {
					eitherMatchedOrVip = loverService.areMatched(initiative, me);
					unreads = historyRepository.
						countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(
							initiative,
							me,
							BEHAVIORS_OF_FEMALE
						);
					if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
						content = String.format(
							"對方和您要求 💗 %d ME 點",
							Math.abs(points)
						);
					}
					if (Objects.equals(BEHAVIOR_INVITE_ME_AS_LINE_FRIEND, behavior)) {
						content = "對方同意給您通訊軟體";
					}
					if (Objects.equals(BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND, behavior)) {
						content = "對方拒絕給您通訊軟體";
					}
				} else {
					eitherMatchedOrVip = loverService.isVIP(initiative) || loverService.isVVIP(initiative);
					unreads = historyRepository.
						countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(
							initiative,
							me,
							BEHAVIORS_OF_MALE
						);
					if (Objects.equals(BEHAVIOR_FARE, behavior)) {
						content = String.format(
							"對方給了您 💗 %d ME 點",
							Math.abs(points)
						);
					}
					if (Objects.equals(BEHAVIOR_GIMME_YOUR_LINE_INVITATION, behavior)) {
						content = "收到加入好友邀請";
					}
					if (Objects.equals(BEHAVIOR_LAI_KOU_DIAN, behavior)) {
						content = "對方已開啟了您的通訊軟體QRcode";
					}
				}
				identifier = initiative.getIdentifier();
				profileImage = String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					initiative.getProfileImage()
				);
				nickname = initiative.getNickname();
			}//if;else
			conversationElement.setAttribute(
				"identifier",
				identifier.toString()
			);
			conversationElement.setAttribute(
				"profileImage",
				profileImage
			);
			conversationElement.setAttribute(
				"nickname",
				nickname
			);

			if (Objects.equals(BEHAVIOR_CHAT_MORE, behavior) || Objects.equals(BEHAVIOR_GREETING, behavior) || Objects.equals(BEHAVIOR_GROUP_GREETING, behavior)) {
				//"聊聊"、"打招呼"、"群发"等行为则捞招呼语
				content = history.getGreeting();
			}
			conversationElement.setAttribute(
				"content",
				content
			);
			conversationElement.setAttribute(
				"occurredTime",
				calculateOccurredTime(history.getOccurred())
			);//多久之前
			conversationElement.setAttribute(
				"isMatchedOrIsVip",
				eitherMatchedOrVip.toString()
			);

			if (eitherMatchedOrVip) {
				friendlyOrVipUnreads += unreads;
			} else {
				unfriendlyOrNonVipUnreads += unreads;
			}

			if (unreads > 0) {
				//未读数
				conversationElement.setAttribute(
					"notSeenCount",
					Integer.toString(unreads)
				);
			}
		}//for

		if (friendlyOrVipUnreads > 0) {
			//好友或贵宾的未读总数
			documentElement.setAttribute(
				"matchedOrVipNotSeenCount",
				friendlyOrVipUnreads.toString()
			);
		}

		if (unfriendlyOrNonVipUnreads > 0) {
			//非好友或非贵宾的未读总数
			documentElement.setAttribute(
				"notMatchedOrNotVipNotSeenCount",
				unfriendlyOrNonVipUnreads.toString()
			);
		}

		return document;
	}

	@SuppressWarnings("UnusedAssignment")
	public Document chatroom(Document document, Lover me, Lover chatPartner) {
		Element documentElement = document.getDocumentElement();

		documentElement.setAttribute(
			"gender",
			me.getGender().toString()
		);

		// 將訊息改成已讀
		List<History> unreadMessages = historyRepository.
			findByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(
				chatPartner,
				me,
				loverService.behaviorOfConversation(me)
			);
		for (History history : unreadMessages) {
			history.setSeen(new Date(System.currentTimeMillis()));
			historyRepository.saveAndFlush(history);
		}

		documentElement.setAttribute(
			"friendIdentifier",
			chatPartner.getIdentifier().toString()
		);

		documentElement.setAttribute(
			"friendProfileImage",
			String.format(
				"https://%s/profileImage/%s",
				Servant.STATIC_HOST,
				chatPartner.getProfileImage()
			)
		);

		documentElement.setAttribute(
			"friendNickname",
			chatPartner.getNickname()
		);

		documentElement.setAttribute(
			"friendGender",
			chatPartner.getGender().toString()
		);

		Lover male = null;
		Lover female = null;
		boolean gender = me.getGender();
		if (gender) {
			male = me;
			female = chatPartner;
		} else {
			female = me;
			male = chatPartner;
		}

		LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(female, male);
		// '給我賴'的行為甜心還沒回應
		if (Objects.nonNull(lineGiven) && Objects.isNull(lineGiven.getResponse())) {
			// 甜心的接受拒絕按鈕
			if (!gender) {
				documentElement.setAttribute(
					"decideBtn",
					null
				);
			}
			// 男士等待甜心回應
			if (gender) {
				documentElement.setAttribute(
					"waitingForRes",
					null
				);
			}
		}
		// 男士要求通訊軟體的按鈕
		// 離上一次拒絕不到12小時
		History refuseHistory = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(female, male, BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND);
		if (Objects.isNull(lineGiven) || (Objects.nonNull(lineGiven) && Objects.nonNull(lineGiven.getResponse())
			&& Objects.nonNull(refuseHistory) && !historyService.within12hrsFromLastRefused(refuseHistory) && !lineGiven.getResponse())) {
			documentElement.setAttribute(
				"reqSocialMediaBtn",
				null
			);
		}

		if (Objects.nonNull(lineGiven) && Objects.nonNull(lineGiven.getResponse()) && lineGiven.getResponse()) {
			documentElement.setAttribute(
				"addLineBtn",
				null
			);
			Long count = historyRepository.countByInitiativeAndPassiveAndBehavior(male, female, BEHAVIOR_LAI_KOU_DIAN);
			if ((loverService.isVIP(male) || loverService.isVVIP(male)) && (count < 1 && !historyService.withinRequiredLimit(male))) {
				documentElement.setAttribute(
					"remindDeduct",
					null
				);
			}
			if (gender && Objects.isNull(
				historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(male, female, BEHAVIOR_RATE))) {
				documentElement.setAttribute(
					"rateBtn",
					null
				);
			}
			if (!gender && Objects.isNull(
				historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(female, male, BEHAVIOR_RATE))) {
				documentElement.setAttribute(
					"rateBtn",
					null
				);
			}
		}

		if (!gender) {
			documentElement.setAttribute(
				"able",
				null
			);
		}
		// 男生當天傳的女生清單
		List<Lover> list = loverService.maleHasSentFemaleListWithinOneDay(male);
		if (gender && ((loverService.isVIP(male) || loverService.isVVIP(male) || loverService.isTrial(male)))) {
			if (Objects.nonNull(lineGiven) && Objects.nonNull(lineGiven.getResponse()) && lineGiven.getResponse()
				|| lessThan3MsgsWithin12Hrs(male, female)) {
				documentElement.setAttribute(
					"able",
					null
				);// 加了通訊軟體或12小時內少於三句話
			} else {
				documentElement.setAttribute(
					"exceedSentencesLimit",
					null
				);
			}
		} else if (gender && lessThan3MsgsWithin12Hrs(male, female) && loverService.maleAbleToSendMsgsWithinOneDay(me)) {
			documentElement.setAttribute(
				"able",
				null
			);
		} else if (gender && lessThan3MsgsWithin12Hrs(male, female) && !loverService.maleAbleToSendMsgsWithinOneDay(me)) {
			if (list.contains(female) && lessThan3MsgsWithin12Hrs(male, female)) {
				documentElement.setAttribute(
					"able",
					null
				);//超過當天傳送訊息上限
			}
			if (!list.contains(female)) {
				documentElement.setAttribute(
					"exceedFemaleLimit",
					null
				);//超過當天傳送訊息上限
			}
		} else if (gender && !lessThan3MsgsWithin12Hrs(male, female) && !loverService.maleAbleToSendMsgsWithinOneDay(me)) {
			if (list.contains(female)) {
				documentElement.setAttribute(
					"exceedSentencesLimit",
					null
				);//超過3句話限制
			} else if (!list.contains(female)) {
				documentElement.setAttribute(
					"exceedFemaleLimit",
					null
				);//超過當天傳送訊息上限
			}
		}

		return document;
	}

	public boolean lessThan3MsgsWithin12Hrs(Lover male, Lover female) {
		int msgsCount = msgsCountWithin12Hrs(male, female);
		return msgsCount < 3;
	}

	@SuppressWarnings("UnusedAssignment")
	public int msgsCountWithin12Hrs(Lover male, Lover female) {
		Date twelveHrsAgo = null;
		Date nowDate = null;
		Calendar twelveHrs = Calendar.getInstance();
		twelveHrs.add(Calendar.HOUR, -12);
		twelveHrsAgo = twelveHrs.getTime();
		nowDate = new Date();
		int msgsCount = historyRepository.countByInitiativeAndPassiveAndBehaviorAndOccurredBetween(male,
			female,
			BEHAVIOR_CHAT_MORE,
			twelveHrsAgo,
			nowDate
		);
		return msgsCount;
	}

	@SuppressWarnings("UnusedAssignment")
	public String calculateOccurredTime(Date date) {
		Date now = new Date();
		long l = now.getTime() - date.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

		String occurredTime = null;
		if (day > 0) {
			occurredTime = String.format("%s天前", day);
		} else if (hour > 0) {
			occurredTime = String.format("%s小時前", hour);
		} else if (min > 0) {
			occurredTime = String.format("%s分鐘前", min);
		} else {
			occurredTime = String.format("幾秒前");
		}

		return occurredTime;
	}

	@SuppressWarnings({"UnusedAssignment", "null"})
	public JSONObject updateInbox(Lover lover) {
		JSONArray jsonArrayForList = new JSONArray();
		boolean isMale = lover.getGender();
		List<History> conversation = historyService.latestConversations(lover);

		// 好友或VIP的未讀總數
		int matchedOrVipNotSeenCount = 0;
		// 非好友或非VIP的未讀總數
		int notMatchedOrNotVipNotSeenCount = 0;
		for (History history : conversation) {
			JSONObject json = new JSONObject();

			String identifier = null;
			String profileImage = null;
			String nickname = null;
			String content = null;
			Boolean isMatchedOrIsVip = null;
			// 某個人的未讀訊息數
			int notSeenCount = 0;
			Collection<Behavior> BEHAVIORS_OF_MALE = Arrays.asList(new History.Behavior[]{
				BEHAVIOR_CHAT_MORE,
				BEHAVIOR_FARE,
				BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
				BEHAVIOR_LAI_KOU_DIAN
			});
			Collection<Behavior> BEHAVIORS_OF_FEMALE = Arrays.asList(new History.Behavior[]{
				BEHAVIOR_GREETING,
				BEHAVIOR_GROUP_GREETING,
				BEHAVIOR_ASK_FOR_FARE,
				BEHAVIOR_INVITE_ME_AS_LINE_FRIEND,
				BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND
			});
			if (Objects.equals(lover, history.getInitiative())) {
				if (isMale) {
					// 雙方是否有加過通訊軟體
					isMatchedOrIsVip = loverService.areMatched(history.getPassive(), lover);
					// 未讀訊息數量
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getPassive(), lover, BEHAVIORS_OF_FEMALE);
					if (Objects.equals(history.getBehavior(), BEHAVIOR_FARE)) {
						content = String.format(
							"您已給 💗 %d ME 點",
							Math.abs(history.getPoints())
						);
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_GIMME_YOUR_LINE_INVITATION)) {
						content = "您已發出要求通訊軟體";
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_LAI_KOU_DIAN)) {
						content = "您開啟了對方的通訊軟體QRcode";
					}
				}
				if (!isMale) {
					isMatchedOrIsVip = loverService.isVIP(history.getPassive()) || loverService.isVVIP(history.getPassive());
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getPassive(), lover, BEHAVIORS_OF_MALE);
					if (Objects.equals(history.getBehavior(), BEHAVIOR_ASK_FOR_FARE)) {
						content = String.format(
							"您已和對方要求 💗 %d ME 點",
							Math.abs(history.getPoints())
						);
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_INVITE_ME_AS_LINE_FRIEND)) {
						content = "您已接受給對方通訊軟體";
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND)) {
						content = "您已拒絕給對方通訊軟體";
					}
				}
				identifier = history.getPassive().getIdentifier().toString();
				profileImage = String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					history.getPassive().getProfileImage()
				);
				nickname = history.getPassive().getNickname();
			} else {
				if (isMale) {
					isMatchedOrIsVip = loverService.areMatched(history.getInitiative(), lover);
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getInitiative(), lover, BEHAVIORS_OF_FEMALE);
					if (Objects.equals(history.getBehavior(), BEHAVIOR_ASK_FOR_FARE)) {
						content = String.format(
							"對方和您要求 💗 %d ME 點",
							Math.abs(history.getPoints())
						);
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_INVITE_ME_AS_LINE_FRIEND)) {
						content = "對方同意給您通訊軟體";
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND)) {
						content = "對方拒絕給您通訊軟體";
					}
				}
				if (!isMale) {
					isMatchedOrIsVip = loverService.isVIP(history.getInitiative()) || loverService.isVVIP(history.getInitiative());
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getInitiative(), lover, BEHAVIORS_OF_MALE);
					if (Objects.equals(history.getBehavior(), BEHAVIOR_FARE)) {
						content = String.format(
							"對方給了您 💗 %d ME 點",
							Math.abs(history.getPoints())
						);
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_GIMME_YOUR_LINE_INVITATION)) {
						content = "收到加入好友邀請";
					}
					if (Objects.equals(history.getBehavior(), BEHAVIOR_LAI_KOU_DIAN)) {
						content = "對方已開啟了您的通訊軟體QRcode";
					}
				}
				identifier = history.getInitiative().getIdentifier().toString();
				profileImage = String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					history.getInitiative().getProfileImage()
				);
				nickname = history.getInitiative().getNickname();
			}
			json.put("identifier", identifier);
			json.put("profileImage", profileImage);
			json.put("nickname", nickname);
			// 若是"聊聊"、"打招呼"、"群發"行為就直接 getGreeting()
			if (Objects.equals(history.getBehavior(), BEHAVIOR_CHAT_MORE) || Objects.equals(history.getBehavior(), BEHAVIOR_GREETING)
				|| Objects.equals(history.getBehavior(), BEHAVIOR_GROUP_GREETING)) {
				content = history.getGreeting();
			}
			json.put("content", content);
			json.put("occurredTime", calculateOccurredTime(history.getOccurred()));
			json.put("isMatchedOrIsVip", isMatchedOrIsVip.toString());
			if (isMatchedOrIsVip) {
				matchedOrVipNotSeenCount += notSeenCount;
			} else {
				notMatchedOrNotVipNotSeenCount += notSeenCount;
			}
			if (notSeenCount > 0) {
				json.put("notSeenCount", Integer.toString(notSeenCount));
			}
			jsonArrayForList.put(json);

		}
		JSONObject jSONObject = new JSONObject();
		jSONObject.put(
			"chatList",
			jsonArrayForList
		);
		if (matchedOrVipNotSeenCount > 0) {
			jSONObject.put(
				"matchedOrVipNotSeenCount",
				Integer.toString(matchedOrVipNotSeenCount)
			);
		}

		if (notMatchedOrNotVipNotSeenCount > 0) {
			jSONObject.put(
				"notMatchedOrNotVipNotSeenCount",
				Integer.toString(notMatchedOrNotVipNotSeenCount)
			);
		}
		return jSONObject;
	}
}
