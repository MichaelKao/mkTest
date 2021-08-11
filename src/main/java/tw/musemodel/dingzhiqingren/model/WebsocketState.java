package tw.musemodel.dingzhiqingren.model;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author m@musemodel.tw
 */
public class WebsocketState {

	@NotEmpty
	@NotNull
	private String type;

	@NotEmpty
	@NotNull
	private String user;

	@NotEmpty
	@NotNull
	private Set<String> users;

	public WebsocketState() {
	}

	public WebsocketState(String type, String user, Set<String> users) {
		super();
		this.type = type;
		this.user = user;
		this.users = users;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}
}
