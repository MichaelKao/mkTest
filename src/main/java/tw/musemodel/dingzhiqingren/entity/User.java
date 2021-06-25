package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户；对应到 view 而非 table。
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings({"PersistenceUnitPresent", "ValidPrimaryTableName"})
@Table(schema = "yuepao", name = "users")
public class User implements java.io.Serializable {

	private static final long serialVersionUID = -6959800738346640019L;

	@Column
	@Id
	private Integer id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private boolean enabled;

	public User() {
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof User)) {
			return false;
		}
		User other = (User) object;
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
	 * @return 国码➕手机号
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username 国码➕手机号
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return 是否有效(简讯验证过)
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled 是否有效(简讯验证过)
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
