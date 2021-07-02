package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * LINE User Profile
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "line_user_profile", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"user_id"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineUserProfile implements Serializable {

	private static final long serialVersionUID = -966878246596398373L;

	@Basic(optional = false)
	@Column(nullable = false)
	@Id
	private Integer id;

	@JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private Lover lover;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "\"language\"")
	private String language;

	@Column(name = "picture_url")
	private String pictureUrl;

	@Column(name = "status_message")
	private String statusMessage;

	public LineUserProfile() {
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LineUserProfile)) {
			return false;
		}
		LineUserProfile other = (LineUserProfile) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "tw.musemodel.dingzhiqingren.LineUserProfile[ id=" + id + " ]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Lover getLover() {
		return lover;
	}

	public void setLover(Lover lover) {
		this.lover = lover;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
