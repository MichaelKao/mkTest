package tw.musemodel.dingzhiqingren.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 请求参数模型：重设密码
 *
 * @author p@musemodel.tw
 */
@Data
public class ResetPassword {

	/**
	 * 国家
	 */
	@NotBlank
	private Short country;

	/**
	 * 帐号(手机号)
	 */
	@NotBlank
	private String login;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return new StringBuilder("{").
				append("\"country\":").append(Objects.isNull(country) ? "null" : country).append(",").
				append("\"login\":").append(Objects.isNull(login) ? "null" : "\"" + login + "\"").append("}").
				toString();
		}
	}
}
