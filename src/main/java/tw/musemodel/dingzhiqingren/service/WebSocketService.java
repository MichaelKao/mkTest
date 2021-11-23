package tw.musemodel.dingzhiqingren.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
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

	public List<Activity> wholeHistoryMsgs(Lover self, Lover friend, int page) {
		List<History> latestPageableConversations
			= historyService.latestPageableConversations(self, friend, PageRequest.of(page, 20));
		List<Activity> wholeHistoryMsgs = new ArrayList<>();
		if (Objects.nonNull(latestPageableConversations)) {
			for (History history : latestPageableConversations) {
				Activity activity = new Activity(
					history.getInitiative().getIdentifier().toString(),
					history.getBehavior(),
					history.getOccurred(),
					servant.DATE_TIME_FORMATTER_yyyyMMddHHmm.format(
						Servant.toTaipeiZonedDateTime(
							history.getOccurred()
						).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
					)
				);
				if (Objects.nonNull(history.getGreeting())) {
					activity.setGreeting(servant.markdownToHtml(history.getGreeting()));
				}
				if (Objects.nonNull(history.getSeen())) {
					activity.setSeen(history.getSeen());
				}
				if (Objects.nonNull(history.getReply())) {
					activity.setReply(history.getReply());
				}
				if (Objects.nonNull(history.getPoints())) {
					activity.setPoints((short) Math.abs(history.getPoints()));
				}
				if (historyService.ableToReturnFare(history)) {
					activity.setAbleToReturnFare(Boolean.TRUE);
				} else {
					activity.setAbleToReturnFare(Boolean.FALSE);
				}
				if (Objects.equals(history.getBehavior(), BEHAVIOR_PICTURES_VIEWABLE)) {
					activity.setShowAllPictures(!history.getShowAllPictures());
				} else {
					activity.setShowAllPictures(Boolean.FALSE);
				}
				activity.setId(history.getId());
				wholeHistoryMsgs.add(activity);
			}
			Collections.sort(wholeHistoryMsgs);
		}
		return wholeHistoryMsgs;
	}

	@SuppressWarnings({"UnusedAssignment", "null"})
	public Document inbox(Document document, Lover me) {
		Element documentElement = document.getDocumentElement();

		boolean isMale = me.getGender();

		List<History> conversations = historyService.latestPageableConversations(me, PageRequest.of(0, 12));
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
						loverService.behaviorOfConversation()
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
							loverService.behaviorOfConversation()
						);
					if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
						content = String.format(
							"您已和對方要求 💗 %d ME 點",
							Math.abs(points)
						);
					}
					if (Objects.equals(BEHAVIOR_RETURN_FARE, behavior)) {
						content = String.format(
							"您已退回對方給您的 💗 %d ME 點",
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
				if (Objects.equals(BEHAVIOR_PICTURES_VIEWABLE, behavior)) {
					//"生活照授權"
					content = "向對方要求生活照授權";
				}
				if (Objects.equals(BEHAVIOR_ACCEPT_PICTURES_VIEWABLE, behavior)) {
					//"生活照允許"
					content = "同意給對方看生活照";
				}
				if (Objects.equals(BEHAVIOR_REFUSE_PICTURES_VIEWABLE, behavior)) {
					//"生活照不允許"
					content = "不同意給對方看生活照";
				}
				if (Objects.equals(BEHAVIOR_RATE, behavior)) {
					//"評價"
					content = "您給對方評價";
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
							loverService.behaviorOfConversation()
						);
					if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
						content = String.format(
							"對方和您要求 💗 %d ME 點",
							Math.abs(points)
						);
					}
					if (Objects.equals(BEHAVIOR_RETURN_FARE, behavior)) {
						content = String.format(
							"對方退回您給的 💗 %d ME 點",
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
							loverService.behaviorOfConversation()
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
				if (Objects.equals(BEHAVIOR_PICTURES_VIEWABLE, behavior)) {
					//"生活照授權"
					content = "對方向您要求生活照授權";
				}
				if (Objects.equals(BEHAVIOR_ACCEPT_PICTURES_VIEWABLE, behavior)) {
					//"生活照允許"
					content = "對方同意給您看生活照";
				}
				if (Objects.equals(BEHAVIOR_REFUSE_PICTURES_VIEWABLE, behavior)) {
					//"生活照不允許"
					content = "對方不同意給您看生活照";
				}
				if (Objects.equals(BEHAVIOR_RATE, behavior)) {
					//"評價"
					content = "對方給您評價";
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

			if (unreads > 0) {
				//未读数
				conversationElement.setAttribute(
					"notSeenCount",
					Integer.toString(unreads)
				);
			}
		}//for

		return document;
	}

	public JSONArray loadMoreInboxList(Lover me, int page) {
		boolean isMale = me.getGender();

		List<History> conversations = historyService.latestPageableConversations(me, PageRequest.of(page, 12));
		JSONArray array = new JSONArray();
		if (Objects.nonNull(conversations)) {
			for (History history : conversations) {
				JSONObject j = new JSONObject();

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
							loverService.behaviorOfConversation()
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
								loverService.behaviorOfConversation()
							);
						if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
							content = String.format(
								"您已和對方要求 💗 %d ME 點",
								Math.abs(points)
							);
						}
						if (Objects.equals(BEHAVIOR_RETURN_FARE, behavior)) {
							content = String.format(
								"您已退回對方給您的 💗 %d ME 點",
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
					if (Objects.equals(BEHAVIOR_PICTURES_VIEWABLE, behavior)) {
						//"生活照授權"
						content = "向對方要求生活照授權";
					}
					if (Objects.equals(BEHAVIOR_ACCEPT_PICTURES_VIEWABLE, behavior)) {
						//"生活照允許"
						content = "同意給對方看生活照";
					}
					if (Objects.equals(BEHAVIOR_REFUSE_PICTURES_VIEWABLE, behavior)) {
						//"生活照不允許"
						content = "不同意給對方看生活照";
					}
					if (Objects.equals(BEHAVIOR_RATE, behavior)) {
						//"評價"
						content = "您給對方評價";
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
								loverService.behaviorOfConversation()
							);
						if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
							content = String.format(
								"對方和您要求 💗 %d ME 點",
								Math.abs(points)
							);
						}
						if (Objects.equals(BEHAVIOR_RETURN_FARE, behavior)) {
							content = String.format(
								"對方退回您給的 💗 %d ME 點",
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
								loverService.behaviorOfConversation()
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
					if (Objects.equals(BEHAVIOR_PICTURES_VIEWABLE, behavior)) {
						//"生活照授權"
						content = "對方向您要求生活照授權";
					}
					if (Objects.equals(BEHAVIOR_ACCEPT_PICTURES_VIEWABLE, behavior)) {
						//"生活照允許"
						content = "對方同意給您看生活照";
					}
					if (Objects.equals(BEHAVIOR_REFUSE_PICTURES_VIEWABLE, behavior)) {
						//"生活照不允許"
						content = "對方不同意給您看生活照";
					}
					if (Objects.equals(BEHAVIOR_RATE, behavior)) {
						//"評價"
						content = "對方給您評價";
					}
					identifier = initiative.getIdentifier();
					profileImage = String.format(
						"https://%s/profileImage/%s",
						Servant.STATIC_HOST,
						initiative.getProfileImage()
					);
					nickname = initiative.getNickname();
				}//if;else
				j.put(
					"identifier",
					identifier.toString()
				).put(
					"profileImage",
					profileImage
				).put(
					"nickname",
					nickname
				);

				if (Objects.equals(BEHAVIOR_CHAT_MORE, behavior) || Objects.equals(BEHAVIOR_GREETING, behavior) || Objects.equals(BEHAVIOR_GROUP_GREETING, behavior)) {
					//"聊聊"、"打招呼"、"群发"等行为则捞招呼语
					content = history.getGreeting();
				}
				j.put(
					"content",
					content
				).put(
					"occurredTime",
					calculateOccurredTime(history.getOccurred())
				).put(
					"isMatchedOrIsVip",
					eitherMatchedOrVip.toString()
				);

				if (unreads > 0) {
					//未读数
					j.put(
						"notSeenCount",
						Integer.toString(unreads)
					);
				}
				array.put(j);
			}//for
		}
		return array;
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
				loverService.behaviorOfConversation()
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
	public JSONObject updateInbox(Lover me, Lover freind) {
		boolean isMale = me.getGender();
		History history = historyService.lastConversation(
			me,
			freind
		);
		JSONObject jSONObject = new JSONObject();

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
					loverService.behaviorOfConversation()
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
						loverService.behaviorOfConversation()
					);
				if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
					content = String.format(
						"您已和對方要求 💗 %d ME 點",
						Math.abs(points)
					);
				}
				if (Objects.equals(BEHAVIOR_RETURN_FARE, behavior)) {
					content = String.format(
						"您已退回對方給您的 💗 %d ME 點",
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
			if (Objects.equals(BEHAVIOR_PICTURES_VIEWABLE, behavior)) {
				//"生活照授權"
				content = "向對方要求生活照授權";
			}
			if (Objects.equals(BEHAVIOR_ACCEPT_PICTURES_VIEWABLE, behavior)) {
				//"生活照允許"
				content = "同意給對方看生活照";
			}
			if (Objects.equals(BEHAVIOR_REFUSE_PICTURES_VIEWABLE, behavior)) {
				//"生活照不允許"
				content = "不同意給對方看生活照";
			}
			if (Objects.equals(BEHAVIOR_RATE, behavior)) {
				//"評價"
				content = "您給對方評價";
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
						loverService.behaviorOfConversation()
					);
				if (Objects.equals(BEHAVIOR_ASK_FOR_FARE, behavior)) {
					content = String.format(
						"對方和您要求 💗 %d ME 點",
						Math.abs(points)
					);
				}
				if (Objects.equals(BEHAVIOR_RETURN_FARE, behavior)) {
					content = String.format(
						"對方退回您給的 💗 %d ME 點",
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
						loverService.behaviorOfConversation()
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
			if (Objects.equals(BEHAVIOR_PICTURES_VIEWABLE, behavior)) {
				//"生活照授權"
				content = "對方向您要求生活照授權";
			}
			if (Objects.equals(BEHAVIOR_ACCEPT_PICTURES_VIEWABLE, behavior)) {
				//"生活照允許"
				content = "對方同意給您看生活照";
			}
			if (Objects.equals(BEHAVIOR_REFUSE_PICTURES_VIEWABLE, behavior)) {
				//"生活照不允許"
				content = "對方不同意給您看生活照";
			}
			if (Objects.equals(BEHAVIOR_RATE, behavior)) {
				//"評價"
				content = "對方給您評價";
			}
			identifier = initiative.getIdentifier();
			profileImage = String.format(
				"https://%s/profileImage/%s",
				Servant.STATIC_HOST,
				initiative.getProfileImage()
			);
			nickname = initiative.getNickname();
		}//if;else
		jSONObject.put(
			"identifier",
			identifier.toString()
		).put(
			"profileImage",
			profileImage
		).put(
			"nickname",
			nickname
		);

		if (Objects.equals(BEHAVIOR_CHAT_MORE, behavior) || Objects.equals(BEHAVIOR_GREETING, behavior) || Objects.equals(BEHAVIOR_GROUP_GREETING, behavior)) {
			//"聊聊"、"打招呼"、"群发"等行为则捞招呼语
			content = history.getGreeting();
		}
		jSONObject.put(
			"content",
			content
		).put(
			"occurredTime",
			calculateOccurredTime(history.getOccurred())
		).put(
			"isMatchedOrIsVip",
			eitherMatchedOrVip.toString()
		);

		if (unreads > 0) {
			//未读数
			jSONObject.put(
				"notSeenCount",
				Integer.toString(unreads)
			);
		}
		return jSONObject;
	}
}
