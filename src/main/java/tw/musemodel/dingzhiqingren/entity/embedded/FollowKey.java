package tw.musemodel.dingzhiqingren.entity.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

/**
 * 复合主键：追踪(收藏)谁、被追踪(收藏)
 *
 * @author p@musemodel.tw
 */
@Data
@Embeddable
public class FollowKey implements Serializable {

	private static final long serialVersionUID = -436228977379086290L;

	@Column(name = "shou_cang_de")
	Integer followingId;

	@Column(name = "bei_shou_cang")
	Integer followedId;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return "null";
		}
	}
}
