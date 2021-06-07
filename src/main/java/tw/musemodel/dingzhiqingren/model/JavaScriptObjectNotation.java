package tw.musemodel.dingzhiqingren.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;

/**
 * JavaScript 对象表示法
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JavaScriptObjectNotation {

	private String reason;

	private String redirect;

	private boolean response;

	private Object result;

	public JavaScriptObjectNotation() {
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public JSONObject toJSONObject() {
		return new JSONObject(this);
	}

	@Override
	public String toString() {
		return toJSONObject().toString();
	}

	public JavaScriptObjectNotation withReason(String reason) {
		this.reason = reason;
		return this;
	}

	public JavaScriptObjectNotation withRedirect(String redirect) {
		this.redirect = redirect;
		return this;
	}

	public JavaScriptObjectNotation withResponse(boolean response) {
		this.response = response;
		return this;
	}

	public JavaScriptObjectNotation withResult(Object result) {
		this.result = result;
		return this;
	}
}
