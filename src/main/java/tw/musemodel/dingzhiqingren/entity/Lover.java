package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 情人
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(schema = "yuepao", name = "qing_ren", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"shi_bie_ma"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lover implements Serializable {

	private static final long serialVersionUID = 5470899666401402787L;

	public enum Carrer {
	XXX,
	YYY
}
	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Basic(optional = false)
	@Column(name = "shi_bie_ma", nullable = false)
	private UUID identifier;

	@JoinColumn(name = "guo_jia", nullable = false, referencedColumnName = "id")
	@ManyToOne(optional = false)
	@JsonBackReference
	private Country country;

	@Basic(optional = false)
	@Column(name = "zhang_hao", nullable = false)
	private String login;

	@Column(name = "mi_ma")
	private String shadow;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private LineUserProfile lineUserProfile;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private Activation activation;

	@JoinTable(
		name = "shou_quan",
		joinColumns = {
			@JoinColumn(name = "qing_ren", referencedColumnName = "id", nullable = false)
		},
		inverseJoinColumns = {
			@JoinColumn(name = "shen_fen", referencedColumnName = "id", nullable = false)
		}
	)
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Role> roles;

	public Lover() {
	}

	protected Lover(Integer id) {
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
		if (!(object instanceof Lover)) {
			return false;
		}
		Lover other = (Lover) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.musemodel.dingzhiqingren.Lover[ id=" + id + " ]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UUID getIdentifier() {
		return identifier;
	}

	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getShadow() {
		return shadow;
	}

	public void setShadow(String shadow) {
		this.shadow = shadow;
	}

	public LineUserProfile getLineUserProfile() {
		return lineUserProfile;
	}

	public void setLineUserProfile(LineUserProfile lineUserProfile) {
		this.lineUserProfile = lineUserProfile;
	}

	public Activation getActivation() {
		return activation;
	}

	public void setActivation(Activation activation) {
		this.activation = activation;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
}
