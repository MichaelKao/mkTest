package me.line.notifybot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.util.Objects;

/**
 * OAuth2 Authorization Code.
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthAuthorizationCode {

	/**
	 * Request only❗️
	 *
	 * Assigns "code"
	 */
	@JsonProperty("response_type")
	private String responseType;

	/**
	 * Request only❗️
	 *
	 * Assigns the client ID of the generated OAuth
	 */
	@JsonProperty("client_id")
	private String clientId;

	/**
	 * Request only❗️
	 *
	 * Assigns the generated redirect URI
	 */
	@JsonProperty("redirect_uri")
	private URI redirectUri;

	/**
	 * Request only❗️
	 *
	 * Assigns "notify"
	 */
	private String scope;

	/**
	 * When requesting, assigns a token that can be used for responding to
	 * CSRF attacks CSRF attacks are typically countered by assigning a hash
	 * value generated from a user's session ID, and then verifying the
	 * state parameter variable when it attempts to access redirect_uri.
	 * LINE Notify is designed with web applications in mind, and requires
	 * state parameter variables.
	 *
	 * When getting response, directly sends the assigned state parameter.
	 */
	private String state;

	/**
	 * Request only❗️
	 *
	 * By assigning "form_post", sends POST request to redirect_uri by form
	 * post instead of redirecting.
	 */
	@JsonProperty("response_mode")
	private String responseMode;

	/**
	 * Response only❗️
	 *
	 * A code for acquiring access tokens
	 */
	private String code;

	/**
	 * Response only❗️
	 *
	 * Assigns error codes defined by OAuth2
	 * https://tools.ietf.org/html/rfc6749#section-4.1.2
	 */
	private String error;

	/**
	 * Response only❗️
	 *
	 * An optional huma-readable text providing additional information, used
	 * to assist the client developer in understanding the error that
	 * occurred.
	 */
	@JsonProperty("error_description")
	private String errorDescription;

	/**
	 * 默认建构子
	 */
	public OAuthAuthorizationCode() {
	}

	/**
	 * 建构子；仅用于请求时❗️
	 *
	 * @param clientId 客户端 ID
	 * @param redirectUri 重定向端点
	 * @param state 令牌
	 */
	public OAuthAuthorizationCode(String clientId, URI redirectUri, String state) {
		responseType = "code";
		this.clientId = clientId;
		this.redirectUri = redirectUri;
		scope = "notify";
		this.state = state;
		responseMode = "form_post";
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
	 * 仅用于请求时❗️
	 *
	 * @return 分配 "code"
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param responseType 分配 "code"
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 分配生成的 OAuth 的客户端 ID
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param clientId 分配生成的 OAuth 的客户端 ID
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 分配生成的重定向 URI
	 */
	public URI getRedirectUri() {
		return redirectUri;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param redirectUri 分配生成的重定向 URI
	 */
	public void setRedirectUri(URI redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 分配 "notify"
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param scope 分配 "notify"
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return 请求时为令牌；响应时为直接发送分配的状态参数。
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state 请求时为令牌；响应时为直接发送分配的状态参数。
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 分配 "form_post" 以通过表单发布而不是重定向将 POST 请求发送到 redirect_uri
	 */
	public String getResponseMode() {
		return responseMode;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param responseMode 分配 "form_post" 以通过表单发布而不是重定向将 POST 请求发送到
	 * redirect_uri
	 */
	public void setResponseMode(String responseMode) {
		this.responseMode = responseMode;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return 获取访问令牌的代码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @param code 获取访问令牌的代码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return 分配由 OAuth2 定义的错误代码。
	 */
	public String getError() {
		return error;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @param error 分配由 OAuth2 定义的错误代码。
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return 提供附加信息的可选人类可读文本，用于帮助客户端开发人员了解发生的错误。
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @param errorDescription 提供附加信息的可选人类可读文本，用于帮助客户端开发人员了解发生的错误。
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
