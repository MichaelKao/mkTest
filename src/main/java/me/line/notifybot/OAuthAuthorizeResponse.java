package me.line.notifybot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;

/**
 * OAuth2 Authorization Response.
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthAuthorizeResponse implements java.io.Serializable {

	private static final long serialVersionUID = -3476245294033481128L;

	/**
	 * A code for acquiring access tokens
	 */
	private String code;

	/**
	 * Directly sends the assigned state parameter
	 */
	private String state;

	/**
	 * Assigns error codes defined by OAuth2
	 * https://tools.ietf.org/html/rfc6749#section-4.1.2
	 */
	private String error;

	/**
	 * An optional huma-readable text providing additional information, used
	 * to assist the client developer in understanding the error that
	 * occurred.
	 */
	@JsonProperty("error_description")
	private String errorDescription;

	/**
	 * 默认建构子
	 */
	public OAuthAuthorizeResponse() {
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (state != null ? state.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof OAuthAuthorizeResponse)) {
			return false;
		}
		OAuthAuthorizeResponse other = (OAuthAuthorizeResponse) object;
		return !((this.state == null && other.state != null) || (this.state != null && !this.state.equals(other.state)));
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(state) ? "null" : state;
		}
	}

	/**
	 * @return 获取访问令牌的代码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code 获取访问令牌的代码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return 直接发送分配的状态参数
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state 直接发送分配的状态参数
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return 分配由 OAuth2 定义的错误代码。
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error 分配由 OAuth2 定义的错误代码。
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return 提供附加信息的可选人类可读文本，用于帮助客户端开发人员了解发生的错误。
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @param errorDescription 提供附加信息的可选人类可读文本，用于帮助客户端开发人员了解发生的错误。
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
