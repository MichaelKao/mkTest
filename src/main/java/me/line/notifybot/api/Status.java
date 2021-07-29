package me.line.notifybot.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;

/**
 * Checking connection status and the validity of an access token.
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

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
	 * Response only❗️
	 *
	 * If the notification target is a user: "USER"
	 *
	 * If the notification target is a group: "GROUP"
	 */
	private String targetType;

	/**
	 * Response only❗️
	 *
	 * If the notification target is a user, displays user name. If
	 * acquisition fails, displays "null."
	 *
	 * If the notification target is a group, displays group name. If the
	 * target user has already left the group, displays "null."
	 */
	private String target;

	/**
	 * 默认建构子
	 */
	public Status() {
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

	/**
	 * 仅用于响应时❗️
	 *
	 * @param message Message visible to end-user
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return (USER|GROUP)
	 */
	public String getTargetType() {
		return targetType;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @param targetType (USER|GROUP)
	 */
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return user or group name
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @param target user or group name
	 */
	public void setTarget(String target) {
		this.target = target;
	}
}
