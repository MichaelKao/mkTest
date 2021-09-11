package tw.musemodel.dingzhiqingren.entity.embedded;

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
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 出没地区
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "qing_ren_yu_di_qu", uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"qing_ren",
		"di_qu"
	})
})
public class AppearedLocation implements Serializable {

	private static final long serialVersionUID = 8382422345222787556L;

	/**
	 * 主键
	 */
	@EmbeddedId
	private AppearedLocationKey id;

	/**
	 * 情人
	 */
	@ManyToOne
	@MapsId("loverId")
	@JoinColumn(name = "qing_ren")
	private Lover lover;

	/**
	 * 地区
	 */
	@ManyToOne
	@MapsId("locationId")
	@JoinColumn(name = "di_qu")
	private Location location;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
