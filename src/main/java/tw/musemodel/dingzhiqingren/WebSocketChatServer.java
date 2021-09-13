package tw.musemodel.dingzhiqingren;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Activity;
import tw.musemodel.dingzhiqingren.model.ChatMessage;
import tw.musemodel.dingzhiqingren.model.WebsocketState;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.WebSocketService;

@Component
@ServerEndpoint(value = "/webSocket/chat/{identifier}")
public class WebSocketChatServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketChatServer.class);

	static HistoryRepository historyRepository;

	@Autowired
	public void setHistoryRepository(HistoryRepository historyRepository) {
		WebSocketChatServer.historyRepository = historyRepository;
	}

	static LoverService loverService;

	@Autowired
	public void setLoverService(LoverService loverService) {
		WebSocketChatServer.loverService = loverService;
	}

	static WebSocketService webSocketService;

	@Autowired
	public void setWebSocketService(WebSocketService webSocketService) {
		WebSocketChatServer.webSocketService = webSocketService;
	}

	static LineGivenRepository lineGivenRepository;

	@Autowired
	public void setLineGivenRepository(LineGivenRepository lineGivenRepository) {
		WebSocketChatServer.lineGivenRepository = lineGivenRepository;
	}

	static WebSocketServer webSocketServer;

	@Autowired
	public void setWebSocketServer(WebSocketServer webSocketServer) {
		WebSocketChatServer.webSocketServer = webSocketServer;
	}

	//靜態常數，用來記錄當前的再現連接數。應該用執行緒較安全。
	private static AtomicInteger online = new AtomicInteger();

	//concurrent 是 thread safe；用來存放每個客戶端對應的 WebSocketChatServer 對象。
	private static Map<String, Session> sessionPools = new ConcurrentHashMap<>();

	Gson gson = new Gson();

	@OnOpen
	public void onOpen(@PathParam("identifier") String identifier, Session userSession) throws IOException {
		// save the new user in the map
		sessionPools.put(identifier, userSession);
		addOnlineCount();
		// Sends all the connected users to the new user
		Set<String> users = sessionPools.keySet();
		WebsocketState stateMessage = new WebsocketState("open", identifier, users);
		String stateMessageJson = gson.toJson(stateMessage);
		Collection<Session> sessions = sessionPools.values();
		for (Session session : sessions) {
			if (session.isOpen()) {
				session.getAsyncRemote().sendText(stateMessageJson);
			}
		}
		LOGGER.debug(
			String.format(
				"%s加入 webSocket, 目前人數為%s",
				identifier,
				online
			));
	}

	@OnMessage
	public void onMessage(Session userSession, String message) {
		ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
		String receiverConn = chatMessage.getReceiver() + chatMessage.getSender();

		Lover sender = loverService.
			loadByIdentifier(
				UUID.fromString(chatMessage.getSender())
			);
		Lover receiver = loverService.
			loadByIdentifier(
				UUID.fromString(chatMessage.getReceiver())
			);

		Lover male = null;
		Lover female = null;
		if (sender.getGender()) {
			male = sender;
			female = receiver;
		} else if (!sender.getGender()) {
			male = receiver;
			female = sender;
		}

		// 列出歷史紀錄
		if ("history".equals(chatMessage.getType())) {
			List<Activity> wholeHistoryMsgs = webSocketService.wholeHistoryMsgs(male, female);

			String historyMsgs = gson.toJson(wholeHistoryMsgs);
			LOGGER.debug(String.format(
				"訊息歷史紀錄toJson：%s",
				historyMsgs
			));
			ChatMessage cmHistory = new ChatMessage("history", historyMsgs);
			if (Objects.nonNull(userSession) && userSession.isOpen()) {
				userSession.getAsyncRemote().sendText(gson.toJson(cmHistory));
				LOGGER.debug(String.format(
					"訊息歷史紀錄：%s",
					gson.toJson(cmHistory)
				));
				return;
			}
			return;
		}

		if ("button".equals(chatMessage.getType())) {
			Session receiverSession = sessionPools.get(receiverConn);
			if (receiverSession != null && receiverSession.isOpen()) {
				receiverSession.getAsyncRemote().sendText(message);
			}
			if (userSession != null && userSession.isOpen()) {
				userSession.getAsyncRemote().sendText(message);
			}
			return;
		}

		if ("chat".equals(chatMessage.getType()) && "YAO_CHE_MA_FEI".equals(chatMessage.getBehavior())) {
			History history = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(female, male, BEHAVIOR_ASK_FOR_FARE);
			chatMessage.setId(history.getId().toString());
			message = gson.toJson(chatMessage);
			Session receiverSession = sessionPools.get(receiverConn);
			if (receiverSession != null && receiverSession.isOpen()) {
				receiverSession.getAsyncRemote().sendText(message);
			}
			if (userSession != null && userSession.isOpen()) {
				userSession.getAsyncRemote().sendText(message);
			}
			return;
		}

		if ("chat".equals(chatMessage.getType()) && "CHE_MA_FEI".equals(chatMessage.getBehavior())) {
			Session receiverSession = sessionPools.get(receiverConn);
			if (receiverSession != null && receiverSession.isOpen()) {
				receiverSession.getAsyncRemote().sendText(message);
			}
			if (userSession != null && userSession.isOpen()) {
				userSession.getAsyncRemote().sendText(message);
			}
			return;
		}

		Behavior behavior = null;
		if (sender.getGender()) {
			behavior = BEHAVIOR_CHAT_MORE;
		} else {
			behavior = BEHAVIOR_GREETING;
		}

		// 加到歷程
		History history = new History(
			sender,
			receiver,
			behavior
		);
		history.setGreeting(chatMessage.getMessage());
		historyRepository.saveAndFlush(history);

		Session receiverSession = sessionPools.get(receiverConn);

		LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(female, male);
		if (sender.getGender()) {
			if ((!loverService.isVIP(male) && !loverService.isVVIP(male))
				|| (Objects.isNull(lineGiven) || Objects.isNull(lineGiven.getResponse()) || !lineGiven.getResponse())) {
				int msgsCount = webSocketService.msgsCountWithin12Hrs(male, female);
				chatMessage.setMsgCount(msgsCount);
				message = gson.toJson(chatMessage);
			}
		}

		if (receiverSession != null && receiverSession.isOpen()) {
			receiverSession.getAsyncRemote().sendText(message);
			history.setSeen(new Date(System.currentTimeMillis()));
			historyRepository.saveAndFlush(history);
		}
		if (userSession != null && userSession.isOpen()) {
			userSession.getAsyncRemote().sendText(message);
		}

		webSocketServer.sendNotification(
			chatMessage.getReceiver().toString(),
			"inbox你收到一則訊息!"
		);
	}

	@OnError
	public void onError(Session userSession, Throwable e) {
		LOGGER.debug(
			String.format(
				"WebSocket 發生錯誤：%s",
				e.toString()
			));
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		String userClose = null;
		Set<String> users = sessionPools.keySet();
		for (String user : users) {
			if (sessionPools.get(user).equals(userSession)) {
				userClose = user;
				sessionPools.remove(user);
				subOnlineCount();
				break;
			}
		}

		if (userClose != null) {
			WebsocketState stateMessage = new WebsocketState("close", userClose, users);
			String stateMessageJson = gson.toJson(stateMessage);
			Collection<Session> sessions = sessionPools.values();
			for (Session session : sessions) {
				session.getAsyncRemote().sendText(stateMessageJson);
			}
		}

		LOGGER.debug(
			String.format(
				"%s關閉 webSocket 連線, 目前人數為%s",
				userClose,
				online
			));
		LOGGER.debug(
			String.format(
				"關閉原因：%s || %s",
				reason.getCloseCode().getCode(),
				reason.toString()
			));
	}

	public static void addOnlineCount() {
		online.incrementAndGet();
	}

	public static void subOnlineCount() {
		online.decrementAndGet();
	}
}
