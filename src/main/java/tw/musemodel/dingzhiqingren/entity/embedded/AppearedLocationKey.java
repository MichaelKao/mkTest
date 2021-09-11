package tw.musemodel.dingzhiqingren.entity.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

/**
 * 复合主键：出没地区
 *
 * @author p@musemodel.tw
 */
@Data
@Embeddable
public class AppearedLocationKey implements Serializable {

	private static final long serialVersionUID = -2314994582745624757L;

	@Column(name = "qing_ren")
	Integer loverId;

	@Column(name = "di_qu")
	Short locationId;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return "null";
		}
	}
}
