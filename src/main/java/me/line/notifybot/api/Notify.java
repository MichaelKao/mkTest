package me.line.notifybot.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;

/**
 * Sends notifications to users or groups.
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notify {

	/**
	 * Response only❗️
	 *
	 * Value according to HTTP status code
	 * <ul>
	 * <li>200: Success・Access token valid</li>
	 * <li>401: Invalid access token</li>
	 * </ul>
	 */
	@JsonProperty("status")
	private Short status;

	/**
	 * Response only❗️
	 *
	 * Message visible to end-user
	 */
	@JsonProperty("message")
	private String message;

	/**
	 * 默认建构子
	 */
	public Notify() {
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(status) ? "null" : status.toString();
		}
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return Value according to HTTP status code
	 */
	public Short getStatus() {
		return status;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @param status Value according to HTTP status code
	 */
	public void setStatus(Short status) {
		this.status = status;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return Message visible to end-user
	 */
	public String getMessage() {
		return message;
	}
}
