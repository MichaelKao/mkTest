package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

/**
 * 特权
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "shou_quan", uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"qing_ren",
		"shen_fen"
	})
})
public class Privilege implements Serializable {

	private static final long serialVersionUID = -1408448069301463905L;

	/**
	 * 主键
	 */
	@EmbeddedId
	private PrivilegeKey id;

	/**
	 * 情人
	 */
	@ManyToOne
	@MapsId("loverId")
	@JoinColumn(name = "qing_ren")
	private Lover lover;

	/**
	 * 身份
	 */
	@ManyToOne
	@MapsId("roleId")
	@JoinColumn(name = "shen_fen")
	private Role role;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
