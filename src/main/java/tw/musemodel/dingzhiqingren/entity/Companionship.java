package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 服務
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "fu_wu", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"fu_wu_biao_qian"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Companionship implements java.io.Serializable {

	private static final long serialVersionUID = -6700464522011040939L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	@Basic(optional = false)
	@Column(name = "fu_wu_biao_qian", nullable = false)
	private String name;

	@ManyToMany(mappedBy = "companionships", fetch = FetchType.EAGER)
	private Set<Lover> lovers;

	/**
	 * 默认构造器
	 */
	public Companionship() {
	}

	/**
	 * @param name 县市名(i18n 键)
	 */
	public Companionship(String name) {
		this.name = name;
	}

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

	/**
	 * @return 主键
	 */
	public Short getId() {
		return id;
	}

	/**
	 * @param id 主键
	 */
	public void setId(Short id) {
		this.id = id;
	}

	/**
	 * @return 服務標籤(i18n 键)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 服務標籤(i18n 键)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 情人們
	 */
	public Set<Lover> getLovers() {
		return lovers;
	}

	/**
	 * @return 情人們
	 */
	public void setLovers(Set<Lover> lovers) {
		this.lovers = lovers;
	}
}
