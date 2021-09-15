package tw.musemodel.dingzhiqingren.entity.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

/**
 * 复合主键：拉黑
 *
 * @author p@musemodel.tw
 */
@Data
@Embeddable
public class BlacklistKey implements Serializable {

	private static final long serialVersionUID = 6222505982217919696L;

	@Column(name = "zhu_dong_fang")
	Integer initiativeId;

	@Column(name = "bei_dong_fang")
	Integer passiveId;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return "null";
		}
	}
}
