package tw.musemodel.dingzhiqingren.model;

/**
 * @author m@musemodel.tw
 */
public class ChatMessage {

	private String type;

	private String sender;

	private String receiver;

	private String message;

	private String historyMsgs;

	private String behavior;

	/**
	 * 12小時內發送訊息的次數
	 */
	private int msgCount;

	public ChatMessage() {
	}

	public ChatMessage(String type, String sender, String receiver, String message) {
		super();
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
	}

	public ChatMessage(String type, String historyMsgs) {
		super();
		this.type = type;
		this.historyMsgs = historyMsgs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHistoryMsgs() {
		return historyMsgs;
	}

	public void setHistoryMsgs(String historyMsgs) {
		this.historyMsgs = historyMsgs;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public String getBehavior() {
		return behavior;
	}

	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}
}
