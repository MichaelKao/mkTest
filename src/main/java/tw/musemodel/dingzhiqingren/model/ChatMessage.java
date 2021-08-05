package tw.musemodel.dingzhiqingren.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author m@musemodel.tw
 */
public class ChatMessage {

	@NotEmpty
	@NotNull
	private String type;

	@NotEmpty
	@NotNull
	private String sender;

	@NotEmpty
	@NotNull
	private String receiver;

	@NotEmpty
	@NotNull
	private String message;

	public ChatMessage() {
	}

	public ChatMessage(String type, String sender, String receiver, String message) {
		super();
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
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
}
