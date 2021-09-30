package tw.musemodel.dingzhiqingren.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

/**
 * 會員
 *
 * @author m@musemodel.tw
 */
@Data
public class Member implements Serializable {

	private static final long serialVersionUID = 5416297105764712727L;

	/**
	 * 男士(或甜心)的ID
	 */
	private Integer id;

	/**
	 * 男士(或甜心)的识别码
	 */
	private UUID identifier;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 昵称
	 */
	private String login;

	/**
	 * 贵宾到期日
	 */
	private Date vipExpiration;

	/**
	 * 注册时戳
	 */
	private String registered;

	/**
	 * 构造器
	 *
	 * @param identifier 男士(或甜心)的识别码
	 * @param nickname 昵称
	 * @param vipExpiration 贵宾到期日
	 * @param timestamp 注册时戳(未来会改为最后付费成为贵宾的时戳)
	 */
	public Member(Integer id, UUID identifier, String nickname, String login, Date vipExpiration, String registered) {
		this.id = id;
		this.identifier = identifier;
		this.nickname = nickname;
		this.login = login;
		this.vipExpiration = vipExpiration;
		this.registered = registered;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(identifier) ? "null" : identifier.toString();
		}
	}
}
