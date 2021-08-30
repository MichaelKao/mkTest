package tw.musemodel.dingzhiqingren.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

/**
 * 下线
 *
 * @author p@musemodel.tw
 */
@Data
public class Descendant implements Serializable {

	private static final long serialVersionUID = 7424020631191221349L;

	/**
	 * 男士(或甜心)的识别码
	 */
	private UUID identifier;

	/**
	 * 是否为贵宾
	 */
	private boolean vip;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 注册(未来会改为最后付费成为贵宾的)时戳
	 */
	private Date timestamp;

	/**
	 * 构造器
	 *
	 * @param identifier 男士(或甜心)的识别码
	 * @param nickname 昵称
	 * @param timestamp 注册时戳(未来会改为最后付费成为贵宾的时戳)
	 */
	public Descendant(UUID identifier, String nickname, Date timestamp) {
		this.identifier = identifier;
		this.nickname = nickname;
		this.timestamp = timestamp;
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
