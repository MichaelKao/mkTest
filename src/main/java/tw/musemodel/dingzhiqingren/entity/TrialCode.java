package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

/**
 * 体验码
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "ti_yan_ma", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"zi_fu_chuan"})
})
public class TrialCode implements Serializable {

	private static final long serialVersionUID = 2205915442438779709L;

	/**
	 * 主键
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	/**
	 * 字符串
	 */
	@Column(name = "zi_fu_chuan")
	private String code;

	/**
	 * 网红
	 */
	@Column(name = "wang_hong")
	private String keyOpinionLeader;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
