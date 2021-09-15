package tw.musemodel.dingzhiqingren.entity.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 拉黑
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "feng_suo", uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"zhu_dong_fang",
		"bei_dong_fang"
	})
})
public class Blacklist implements Serializable {

	private static final long serialVersionUID = -8518672615742337184L;

	/**
	 * 主键
	 */
	@Id
	private BlacklistKey id;

	/**
	 * 谁拉黑
	 */
	@JoinColumn(name = "zhu_dong_fang")
	@ManyToOne
	@MapsId("initiativeId")
	private Lover blocker;

	/**
	 * 拉黑谁
	 */
	@JoinColumn(name = "bei_dong_fang")
	@ManyToOne
	@MapsId("passiveId")
	private Lover blocked;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
