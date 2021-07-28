package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 透过 LINE 接收网站服务通知
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "line_notify_authentication", schema = "public")
public class LineNotifyAuthentication implements java.io.Serializable {

	private static final long serialVersionUID = -5683882748788156154L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Basic(optional = false)
	@Column(name = "\"state\"", nullable = false)
	@NotNull
	private String state;

	@Basic(optional = false)
	@JoinColumn(name = "sucker", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	@NotNull
	private Lover sucker;

	/**
	 * 默认建构子
	 */
	public LineNotifyAuthentication() {
	}

	/**
	 * @param state 响应 CSRF 攻击的令牌
	 * @param sucker 情人
	 */
	public LineNotifyAuthentication(String state, Lover sucker) {
		this.state = state;
		this.sucker = sucker;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LineNotifyAuthentication)) {
			return false;
		}
		LineNotifyAuthentication other = (LineNotifyAuthentication) object;
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
	 * @return 响应 CSRF 攻击的令牌
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state 响应 CSRF 攻击的令牌
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return 情人
	 */
	public Lover getSucker() {
		return sucker;
	}

	/**
	 * @param sucker 情人
	 */
	public void setSucker(Lover sucker) {
		this.sucker = sucker;
	}
}
