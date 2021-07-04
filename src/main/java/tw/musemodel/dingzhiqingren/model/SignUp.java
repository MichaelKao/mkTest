package tw.musemodel.dingzhiqingren.model;

import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * @author p@musemodel.tw
 */
public class SignUp {

	@NotEmpty
	@NotNull
	private Short country;

	@NotEmpty
	@NotNull
	private String login;

	@NotEmpty
	@NotNull
	private Boolean gender;

	@NotEmpty
	@NotNull
	@DateTimeFormat(iso = ISO.DATE, fallbackPatterns = {"dd-MM-yyyy"})
	private Date birthday;

	public SignUp() {
	}

	@Override
	public String toString() {
		return new StringBuilder("{").
			append("\"country\":").append(Objects.isNull(country) ? "null" : country).append(",").
			append("\"login\":").append(Objects.isNull(login) ? "null" : "\"" + login + "\"").append("}").
			toString();
	}

	public Short getCountry() {
		return country;
	}

	public void setCountry(Short country) {
		this.country = country;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
