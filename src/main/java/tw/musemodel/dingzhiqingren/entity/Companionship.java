package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import tw.musemodel.dingzhiqingren.entity.embedded.DesiredCompanionship;

/**
 * 友谊
 *
 * @author m@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "fu_wu", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"fu_wu_biao_qian"})
})
public class Companionship implements java.io.Serializable {

	private static final long serialVersionUID = -6700464522011040939L;

	/**
	 * 主键
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	/**
	 * 友谊标签(i18n 键)
	 */
	@Basic(optional = false)
	@Column(name = "fu_wu_biao_qian", nullable = false)
	private String name;

	/**
	 * 哪些期望陪伴
	 */
	@OneToMany(mappedBy = "companionship")
	private Set<DesiredCompanionship> lovers;

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Companionship)) {
			return false;
		}
		Companionship other = (Companionship) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
