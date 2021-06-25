package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * 生活照
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(schema = "yuepao", name = "sheng_huo_zhao", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"shi_bie_ma"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Picture implements Serializable {

	private static final long serialVersionUID = 7171822056959116416L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@JoinColumn(name = "qing_ren", nullable = false, referencedColumnName = "id")
	@ManyToOne(optional = false)
	@JsonManagedReference
	private Lover lover;

	@Basic(optional = false)
	@Column(name = "shi_bie_ma", nullable = false)
	private UUID identifier;

	@Basic(optional = false)
	@Column(name = "shi_chuo", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurred;

	/**
	 * 默认构造器
	 */
	public Picture() {
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Picture)) {
			return false;
		}
		Picture other = (Picture) object;
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
	public Integer getId() {
		return id;
	}

	/**
	 * @param id 主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return 情人
	 */
	public Lover getLover() {
		return lover;
	}

	/**
	 * @param lover 情人
	 */
	public void setLover(Lover lover) {
		this.lover = lover;
	}

	/**
	 * @return 識別碼
	 */
	public UUID getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier 識別碼
	 */
	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return 時戳
	 */
	public Date getOccurred() {
		return occurred;
	}

	/**
	 * @param occurred 時戳
	 */
	public void setOccurred(Date occurred) {
		this.occurred = occurred;
	}
}
