package tw.musemodel.dingzhiqingren.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
		List<Activity> wholeHistoryMsgs = new ArrayList<>();
		for (History history : maleTalkHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : gimmeHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : alreadyBeingFriendHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : fareHistoryMsgs) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			activity.setPoints((short) Math.abs(history.getPoints()));
			wholeHistoryMsgs.add(activity);
		}
		for (History history : femaleTalkHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : femaleGroupGreetingHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : inviteAsFriendHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : refuseAsFriendHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : requestFareHistoryMsgs) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getBehavior(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			activity.setPoints(history.getPoints());
			activity.setId(history.getId());
			activity.setReply(history.getReply());
			wholeHistoryMsgs.add(activity);
		}
		Collections.sort(wholeHistoryMsgs);
		return wholeHistoryMsgs;
	}

	public Document inbox(Document document, Lover me) {
		Element documentElement = document.getDocumentElement();

		boolean isMale = me.getGender();

		List<History> conversation = historyService.latestConversations(me);
		// 好友的未讀總數
		int matchedNotSeenCount = 0;
		// 非好友的未讀總數
		int notMatchedNotSeenCount = 0;
		for (History history : conversation) {
			Element conversationElement = document.createElement("conversation");
			documentElement.appendChild(conversationElement);

			String identifier = null;
			String profileImage = null;
			String nickname = null;
			String content = history.getGreeting();
			Boolean matched = null;
			// 某個人的未讀訊息數
			int notSeenCount = 0;
			Collection<Behavior> BEHAVIORS_OF_MALE = Arrays.asList(new History.Behavior[]{
				HistoryService.BEHAVIOR_CHAT_MORE
			});
			Collection<Behavior> BEHAVIORS_OF_FEMALE = Arrays.asList(new History.Behavior[]{
				HistoryService.BEHAVIOR_GREETING,
				HistoryService.BEHAVIOR_GROUP_GREETING
			});
			if (Objects.equals(me, history.getInitiative())) {
				if (isMale) {
					// 雙方是否有加過通訊軟體
					matched = loverService.areMatched(history.getPassive(), me);
					// 未讀訊息數量
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getPassive(), me, BEHAVIORS_OF_FEMALE);
				}
				if (!isMale) {
					matched = loverService.areMatched(me, history.getPassive());
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getPassive(), me, BEHAVIORS_OF_MALE);
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
					matched = loverService.areMatched(history.getInitiative(), me);
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getInitiative(), me, BEHAVIORS_OF_FEMALE);
				}
				if (!isMale) {
					matched = loverService.areMatched(me, history.getInitiative());
					notSeenCount = historyRepository.countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(history.getInitiative(), me, BEHAVIORS_OF_MALE);
				}
				identifier = history.getInitiative().getIdentifier().toString();
				profileImage = String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					history.getInitiative().getProfileImage()
				);
				nickname = history.getInitiative().getNickname();
			}
			conversationElement.setAttribute(
				"identifier",
				identifier
			);
			conversationElement.setAttribute(
				"profileImage",
				profileImage
			);
			conversationElement.setAttribute(
				"nickname",
				nickname
			);
			conversationElement.setAttribute(
				"content",
				content
			);
			conversationElement.setAttribute(
				"occurredTime",
				calculateOccurredTime(history.getOccurred())
			);
			conversationElement.setAttribute(
				"matched",
				matched.toString()
			);
			if (matched) {
				matchedNotSeenCount += notSeenCount;
			} else {
				notMatchedNotSeenCount += notSeenCount;
			}
			if (notSeenCount > 0) {
				conversationElement.setAttribute(
					"notSeenCount",
					Integer.toString(notSeenCount)
				);
			}
		}

		if (matchedNotSeenCount > 0) {
			documentElement.setAttribute(
				"matchedNotSeenCount",
				Integer.toString(matchedNotSeenCount)
			);
		}

		if (notMatchedNotSeenCount > 0) {
			documentElement.setAttribute(
				"notMatchedNotSeenCount",
				Integer.toString(notMatchedNotSeenCount)
			);
		}

		return document;
	}

	public Document chatroom(Document document, Lover me, Lover chatPartner) {
		Element documentElement = document.getDocumentElement();

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

		if (gender && !lessThen3MsgsWithin12Hrs(male, female)) {
			// 若甜心已接受給男士通訊軟體，若是VIP身分則可以無限暢聊
			if ((loverService.isVIP(male) || loverService.isVVIP(male))
				&& (Objects.nonNull(lineGiven) && Objects.nonNull(lineGiven.getResponse()) && lineGiven.getResponse())) {
				return document;
			}
			documentElement.setAttribute(
				"notAbleToSendMsgs",
				null
			);
		}

		return document;
	}

	public boolean lessThen3MsgsWithin12Hrs(Lover male, Lover female) {
		int msgsCount = msgsCountWithin12Hrs(male, female);
		return msgsCount < 3;
	}

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
}
