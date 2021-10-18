package tw.musemodel.dingzhiqingren.entity.embedded;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "access_event", catalog = "youngme", schema = "public")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class AccessEvent implements Serializable {

	private static final long serialVersionUID = 4729979298853936197L;

	@Basic(optional = false)
	@Column(name = "event_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long eventId;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurred;

	@Basic(optional = false)
	@Column(nullable = false)
	@NotNull
	private long epoch;

	@Column(name = "request_uri")
	private String requestUri;

	@Column(name = "request_url")
	private String requestUrl;

	@Column(name = "remote_host")
	private String remoteHost;

	@Column(name = "remote_user")
	private String remoteUser;

	@Column(name = "remote_addr")
	private String remoteAddr;

	@Column
	private String protocol;

	@Column
	private String method;

	@Column(name = "server_name")
	private String serverName;

	@Column(name = "post_content")
	private String postContent;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "accessEvent")
	private Collection<AccessEventHeader> accessEventHeaders;

	public AccessEvent() {
	}

	protected AccessEvent(Long eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(eventId) ? "null" : eventId.toString();
		}
	}
}
