package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
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

/**
 * 激活
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(schema = "yuepao", name = "ji_huo", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"zi_fu_chuan"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activation implements Serializable {

	private static final long serialVersionUID = -7559145786659983065L;

	@Basic(optional = false)
	@Column(nullable = false)
	@Id
	private Integer id;

	@JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private Lover lover;

	@Column(name = "zi_fu_chuan")
	private String string;

	@Basic(optional = false)
	@Column(name = "dao_qi", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiry;

	@Column(name = "shi_chuo")
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurred;

	/**
	 * 默认构造器
	 */
	public Activation() {
	}

	/**
	 * @param lover 情人
	 * @param string 字符串
	 * @param expiry 到期时戳
	 */
	public Activation(Integer id, Lover lover, String string, Date expiry) {
		this.id = id;
		this.lover = lover;
		this.string = string;
		this.expiry = expiry;
	}

	/**
	 * @param id 主键
	 */
	protected Activation(Integer id) {
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
		if (!(object instanceof Activation)) {
			return false;
		}
		Activation other = (Activation) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.musemodel.dingzhiqingren.entity.Activation[ id=" + id + " ]";
	}

	/**
	 *
	 * @return 主键
	 */
	public Integer getId() {
		return id;
	}

	/**
	 *
	 * @param id 主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 *
	 * @return 情人
	 */
	public Lover getLover() {
		return lover;
	}

	/**
	 *
	 * @param lover 情人
	 */
	public void setLover(Lover lover) {
		this.lover = lover;
	}

	/**
	 *
	 * @return 字符串
	 */
	public String getString() {
		return string;
	}

	/**
	 * @param string 字符串
	 */
	public void setString(String string) {
		this.string = string;
	}

	/**
	 * @return 到期时戳
	 */
	public Date getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry 到期时戳
	 */
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return 发生时戳
	 */
	public Date getOccurred() {
		return occurred;
	}

	/**
	 * @param occurred 发生时戳
	 */
	public void setOccurred(Date occurred) {
		this.occurred = occurred;
	}
}
