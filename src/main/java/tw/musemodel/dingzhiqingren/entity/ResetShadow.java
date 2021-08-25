package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.Data;

/**
 * 重设密码
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "zhong_she_mi_ma", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"zi_fu_chuan"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetShadow implements Serializable {

	private static final long serialVersionUID = -6950020715037756016L;

	/**
	 * 主键
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	@Id
	private Integer id;

	/**
	 * 情人
	 */
	@JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private Lover lover;

	/**
	 * 字符串
	 */
	@Column(name = "zi_fu_chuan")
	private String string;

	/**
	 * 到期时戳
	 */
	@Basic(optional = false)
	@Column(name = "dao_qi", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiry;

	/**
	 * 发生时戳
	 */
	@Column(name = "shi_chuo")
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurred;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
