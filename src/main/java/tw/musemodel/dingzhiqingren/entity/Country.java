package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 国家
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "guo_jia", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"guo_ma"}),
	@UniqueConstraint(columnNames = {"guo_ming"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country implements java.io.Serializable {

	private static final long serialVersionUID = 3590335394075995522L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	@Basic(optional = false)
	@Column(name = "guo_ma", nullable = false)
	private String callingCode;

	@Basic(optional = false)
	@Column(name = "guo_ming", nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "country")
	@JsonBackReference
	private Collection<Lover> lovers;

	/**
	 * 默认构造器
	 */
	public Country() {
	}

	/**
	 * @param id 主键
	 */
	protected Country(Short id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Country)) {
			return false;
		}
		Country other = (Country) object;
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
	 * @return 国码
	 */
	public String getCallingCode() {
		return callingCode;
	}

	/**
	 * @param callingCode 国码
	 */
	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

	/**
	 * @return 国名(i18n 键)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 国名(i18n 键)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 情人们
	 */
	public Collection<Lover> getLovers() {
		return lovers;
	}

	/**
	 * @param lovers 情人们
	 */
	public void setLovers(Collection<Lover> lovers) {
		this.lovers = lovers;
	}
}
