package tw.musemodel.dingzhiqingren.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings({"PersistenceUnitPresent", "ValidPrimaryTableName"})
@Table(schema = "yuepao", name = "users")
public class User implements Serializable {

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
