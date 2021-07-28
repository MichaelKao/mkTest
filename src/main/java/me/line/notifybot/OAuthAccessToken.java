package me.line.notifybot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;

/**
 * OAuth2 Access Token.
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthAccessToken {

	/**
	 * Assigns "authorization_code"
	 */
	@JsonProperty("grant_type")
	private String grantType;

	/**
	 * Assigns a code parameter value generated during redirection
	 */
	@JsonProperty("code")
	private String code;

	/**
	 * Assigns redirect_uri to assigned authorization endpoint API
	 */
	@JsonProperty("redirect_uri")
	private URI redirectUri;

	/**
	 * Assigns client ID to issued OAuth
	 */
	@JsonProperty("client_id")
	private String clientId;

	/**
	 * Assigns secret to issued OAuth
	 */
	@JsonProperty("client_secret")
	private String clientSecret;

	/**
	 * An access token for authentication. Used for calling the notification
	 * API. This access token has no expiration date.
	 */
	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("status")
	private Short status;

	@JsonProperty("message")
	private String message;

	/**
	 * 默认建构子
	 */
	public OAuthAccessToken() {
	}

	/**
	 * 建构子；仅用于请求时❗️
	 *
	 * @param code 分配重定向期间生成的代码参数值
	 * @param redirectUri 重定向端点
	 * @param clientId 客户端 ID
	 * @param clientSecret 密钥
	 */
	public OAuthAccessToken(String code, URI redirectUri, String clientId, String clientSecret) {
		grantType = "authorization_code";
		this.code = code;
		this.redirectUri = redirectUri;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return "null";
		}
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 分配 "authorization_code"
	 */
	public String getGrantType() {
		return grantType;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param grantType 分配 "authorization_code"
	 */
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 分配重定向期间生成的代码参数值
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param code 分配重定向期间生成的代码参数值
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 将 redirect_uri 分配给授权端点 API
	 */
	public URI getRedirectUri() {
		return redirectUri;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param redirectUri 将 redirect_uri 分配给授权端点 API
	 */
	public void setRedirectUri(URI redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 将客户端 ID 分配给发出的 OAuth
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param clientId 将客户端 ID 分配给发出的 OAuth
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @return 为已发布的 OAuth 分配密钥
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * 仅用于请求时❗️
	 *
	 * @param clientSecret 为已发布的 OAuth 分配密钥
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @return 用于身份验证的访问令牌。用于调用通知 API。此访问令牌没有到期日期。
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 仅用于响应时❗️
	 *
	 * @param accessToken 用于身份验证的访问令牌。用于调用通知 API。此访问令牌没有到期日期。
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
