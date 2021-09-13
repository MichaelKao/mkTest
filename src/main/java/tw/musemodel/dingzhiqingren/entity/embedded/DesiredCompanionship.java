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
import tw.musemodel.dingzhiqingren.entity.Companionship;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 期望的陪伴
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "qing_ren_yu_fu_wu", uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"qing_ren",
		"fu_wu"
	})
})
public class DesiredCompanionship implements Serializable {

	private static final long serialVersionUID = -5609294658365500173L;

	/**
	 * 主键
	 */
	@EmbeddedId
	private DesiredCompanionshipKey id;

	/**
	 * 情人
	 */
	@JoinColumn(name = "qing_ren")
	@ManyToOne
	@MapsId("loverId")
	private Lover lover;

	/**
	 * 友谊
	 */
	@JoinColumn(name = "fu_wu")
	@ManyToOne
	@MapsId("companionshipId")
	private Companionship companionship;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
