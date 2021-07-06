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
 * 年收入(男)
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "nian_shou_ru", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"nian_shou_ru"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnualIncome implements java.io.Serializable {

	private static final long serialVersionUID = 9018801917747929519L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	@Basic(optional = false)
	@Column(name = "nian_shou_ru", nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "annualIncome")
	@JsonBackReference
	private Collection<Lover> lovers;

	/**
	 * 默认构造器
	 */
	public AnnualIncome() {
	}

	/**
	 * @param name 年收入(i18n)
	 */
	public AnnualIncome(String name) {
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
		if (!(object instanceof AnnualIncome)) {
			return false;
		}
		AnnualIncome other = (AnnualIncome) object;
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
	 * @return 年收入(i18n)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 年收入(i18n)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 情人們
	 */
	public Collection<Lover> getLovers() {
		return lovers;
	}

	/**
	 * @param lovers 情人們
	 */
	public void setLovers(Collection<Lover> lovers) {
		this.lovers = lovers;
	}
}
