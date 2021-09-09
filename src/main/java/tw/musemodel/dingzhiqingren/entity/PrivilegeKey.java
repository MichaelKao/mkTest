package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

/**
 * 复合主键：特权
 *
 * @author p@musemodel.tw
 */
@Data
@Embeddable
public class PrivilegeKey implements Serializable {

	private static final long serialVersionUID = 1911223323320178283L;

	@Column(name = "qing_ren")
	Integer loverId;

	@Column(name = "shen_fen")
	Short roleId;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return "null";
		}
	}
}
