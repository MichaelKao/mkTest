package tw.musemodel.dingzhiqingren;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.ChatMessage;
import tw.musemodel.dingzhiqingren.model.WebsocketState;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;
import tw.musemodel.dingzhiqingren.service.LoverService;

@Component
@ServerEndpoint(value = "/webSocket/chat/{identifier}")
public class WebSocketChatServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketChatServer.class);

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LoverService loverService;

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
		LOGGER.debug(String.format(
			"%s加入 webSocket, 目前人數為%s",
			identifier,
			online
		));
	}

	@OnMessage
	public void onMessage(Session userSession, String message) {
		ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
		Lover sender = loverService.loadByIdentifier(
			UUID.fromString(chatMessage.getSender())
		);
		Lover receiver = loverService.loadByIdentifier(
			UUID.fromString(chatMessage.getReceiver())
		);

		if ("history".equals(chatMessage.getType())) {
			List<History> histories = historyRepository.findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(sender, receiver, BEHAVIOR_GIMME_YOUR_LINE_INVITATION);
			List<String> historyData = new ArrayList<String>();
			for (History history : histories) {
				historyData.add(history.getGreeting());
			}
			String historyMsg = gson.toJson(historyData);
			ChatMessage cmHistory = new ChatMessage("history", chatMessage.getSender(), chatMessage.getReceiver(), historyMsg);
			if (Objects.nonNull(userSession) && userSession.isOpen()) {
				userSession.getAsyncRemote().sendText(gson.toJson(cmHistory));
				LOGGER.debug(String.format(
					"歷史紀錄：%s",
					gson.toJson(cmHistory)
				));
				return;
			}
		}

		Session receiverSession = sessionPools.get(receiver);
		if (receiverSession != null && receiverSession.isOpen()) {
			receiverSession.getAsyncRemote().sendText(message);
			userSession.getAsyncRemote().sendText(message);
		}

		// 加到歷程
		History history = new History(
			sender,
			receiver,
			BEHAVIOR_GIMME_YOUR_LINE_INVITATION
		);
		history.setGreeting(chatMessage.getMessage());
		historyRepository.saveAndFlush(history);

		LOGGER.debug(String.format(
			"收到的訊息(message)：%s",
			message
		));
	}

	@OnError
	public void onError(Session userSession, Throwable e) {
		LOGGER.debug(
			"WebSocket 發生錯誤：%s",
			e.toString()
		);
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
			"%s關閉 webSocket 連線, 目前人數為%s",
			userClose,
			online
		);
	}

	public static void addOnlineCount() {
		online.incrementAndGet();
	}

	public static void subOnlineCount() {
		online.decrementAndGet();
	}
}
