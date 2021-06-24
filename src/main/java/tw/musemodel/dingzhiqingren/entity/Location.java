package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
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
 * 地區
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(schema = "yuepao", name = "di_qu", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"xian_shi_ming"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {	

	private static final long serialVersionUID = -8980376544581458604L;
	
	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	@Basic(optional = false)
	@Column(name = "xian_shi_ming", nullable = false)
	private String city;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
	@JsonManagedReference
	private Collection<Lover> lovers;

	/**
	 * 建構子
	 */
	public Location() {
	}

	public Location(String city) {
		this.city = city;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + Objects.hashCode(this.id);
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Location)) {
			return false;
		}
		Location other = (Location) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}
	
	@Override
	public String toString() {
		return "tw.musemodel.dingzhiqingren.Location[ id=" + id + " ]";
	}

	/**
	 * @return 主鍵
	 */
	public Short getId() {
		return id;
	}

	/**
	 * @param id 主鍵
	 */
	public void setId(Short id) {
		this.id = id;
	}

	/**
	 * @return 縣市名
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city 縣市名
	 */
	public void setCity(String city) {
		this.city = city;
	}
}
