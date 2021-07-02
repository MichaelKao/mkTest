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
 * 身份
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "shen_fen", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"jue_se"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = -8315359739548232325L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	@Basic(optional = false)
	@Column(name = "jue_se", nullable = false)
	private String textualRepresentation;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	@JsonBackReference
	private Collection<Lover> lovers;

	/**
	 * 默认构造器
	 */
	public Role() {
	}

	/**
	 * @param id 主键
	 */
	protected Role(Short id) {
		this.id = id;
	}

	/**
	 * @param name 角色
	 */
	public Role(String name) {
		this.textualRepresentation = name;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Role)) {
			return false;
		}
		Role other = (Role) object;
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
	 * @return 角色
	 */
	public String getTextualRepresentation() {
		return textualRepresentation;
	}

	/**
	 * @param textualRepresentation 角色
	 */
	public void setTextualRepresentation(String textualRepresentation) {
		this.textualRepresentation = textualRepresentation;
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
