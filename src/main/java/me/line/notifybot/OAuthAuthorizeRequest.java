package me.line.notifybot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import javax.validation.constraints.NotBlank;

/**
 * OAuth2 Authorization Response.
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthAuthorizeRequest {

	/**
	 * Assigns "code"
	 */
	@JsonProperty("response_type")
	@NotBlank
	private String responseType;

	/**
	 * Assigns the client ID of the generated OAuth
	 */
	@JsonProperty("client_id")
	@NotBlank
	private String clientId;

	/**
	 * Assigns the generated redirect URI
	 */
	@JsonProperty("redirect_uri")
	@NotBlank
	private URI redirectUri;

	/**
	 * Assigns "notify"
	 */
	@NotBlank
	private String scope;

	/**
	 * Assigns a token that can be used for responding to CSRF attacks CSRF
	 * attacks are typically countered by assigning a hash value generated
	 * from a user's session ID, and then verifying the state parameter
	 * variable when it attempts to access redirect_uri. LINE Notify is
	 * designed with web applications in mind, and requires state parameter
	 * variables.
	 */
	@NotBlank
	private String state;

	/**
	 * By assigning "form_post", sends POST request to redirect_uri by form
	 * post instead of redirecting.
	 */
	@JsonProperty("response_mode")
	private String responseMode;

	/**
	 * 默认建构子
	 */
	public OAuthAuthorizeRequest() {
		responseType = "code";
		scope = "notify";
		responseMode = "form_post";
	}

	/**
	 * @return 分配 "code"
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * @param responseType 分配 "code"
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * @return 分配生成的 OAuth 的客户端 ID
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId 分配生成的 OAuth 的客户端 ID
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return 分配生成的重定向 URI
	 */
	public URI getRedirectUri() {
		return redirectUri;
	}

	/**
	 * @param redirectUri 分配生成的重定向 URI
	 */
	public void setRedirectUri(URI redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * @return 分配 "notify"
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope 分配 "notify"
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return 分配可用于响应 CSRF 攻击的令牌，CSRF 攻击通常通过分配从用户会话 ID 生成的哈希值来反击，然后在尝试访问
	 * redirect_uri 时验证状态参数变量。
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state 分配可用于响应 CSRF 攻击的令牌，CSRF 攻击通常通过分配从用户会话 ID
	 * 生成的哈希值来反击，然后在尝试访问 redirect_uri 时验证状态参数变量。
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return 分配 "form_post" 以通过表单发布而不是重定向将 POST 请求发送到 redirect_uri
	 */
	public String getResponseMode() {
		return responseMode;
	}

	/**
	 * @param responseMode 分配 "form_post" 以通过表单发布而不是重定向将 POST 请求发送到
	 * redirect_uri
	 */
	public void setResponseMode(String responseMode) {
		this.responseMode = responseMode;
	}
}
