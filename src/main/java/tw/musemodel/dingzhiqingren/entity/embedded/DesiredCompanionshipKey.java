package tw.musemodel.dingzhiqingren.entity.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

/**
 * 复合主键：期望的陪伴
 *
 * @author p@musemodel.tw
 */
@Data
@Embeddable
public class DesiredCompanionshipKey implements Serializable {

	private static final long serialVersionUID = -4729803767055215977L;

	@Column(name = "qing_ren")
	Integer loverId;

	@Column(name = "fu_wu")
	Short companionshipId;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return "null";
		}
	}
}
