package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Collection;
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
@Table(schema = "yuepao", name = "guo_jia", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"guo_ma"}),
	@UniqueConstraint(columnNames = {"guo_ming"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country implements Serializable {

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
	@JsonManagedReference
	private Collection<Lover> lovers;

	public Country() {
	}

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
		return "tw.musemodel.dingzhiqingren.Country[ id=" + id + " ]";
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Lover> getLovers() {
		return lovers;
	}

	public void setLovers(Collection<Lover> lovers) {
		this.lovers = lovers;
	}
}
