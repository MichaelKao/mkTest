package tw.musemodel.dingzhiqingren;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@ServerEndpoint(value = "/webSocket/{identifier}")
public class WebSocketServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

	//靜態常數，用來記錄當前的再現連接數。應該用執行緒較安全。
	private static AtomicInteger online = new AtomicInteger();

	//concurrent 是 thread safe；用來存放每個客戶端對應的 WebSocketServer 對象。
	private static Map<String, Session> sessionPools = new ConcurrentHashMap<>();

	/**
	 * 發送通知(訊息)
	 *
	 * @param session 與某個客戶端的連接會話，需要通過它來給客戶端發送數據
	 * @param message 通知(訊息)內容
	 * @throws IOException
	 */
	public void sendMessage(Session session, String message) throws IOException {
		if (session != null) {
			session.getBasicRemote().sendText(message);
		}
	}

	/**
	 * 連線
	 *
	 * @param session
	 * @param identifier
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "identifier") String identifier) {
		sessionPools.put(identifier, session);
		addOnlineCount();
		LOGGER.debug(String.format(
			"%s加入 webSocket, 目前人數為%s",
			identifier,
			online
		));
	}

	/**
	 * 關閉連線
	 *
	 * @param identifier
	 */
	@OnClose
	public void onClose(@PathParam(value = "identifier") String identifier) {
		sessionPools.remove(identifier);
		subOnlineCount();
		LOGGER.debug(String.format(
			"%s關閉 webSocket 連線, 目前人數為%s",
			identifier,
			online
		));
	}

	/**
	 * 收到客戶端訊息
	 *
	 * @param message
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(String message) throws IOException {
		for (Session session : sessionPools.values()) {
			try {
				sendMessage(session, message);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 發生錯誤
	 *
	 * @param session
	 * @param throwable
	 */
	@OnError
	public void onError(Session session, Throwable throwable) {
		LOGGER.debug("WebSocket 發生錯誤");
		throwable.printStackTrace();
	}

	/**
	 * 給指定用戶發送通知(訊息)
	 *
	 * @param identifier 用戶識別碼
	 * @param message 通知(訊息)
	 */
	public void sendNotification(String identifier, String message) {
		Session session = sessionPools.get(identifier);
		try {
			sendMessage(session, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addOnlineCount() {
		online.incrementAndGet();
	}

	public static void subOnlineCount() {
		online.decrementAndGet();
	}
}
