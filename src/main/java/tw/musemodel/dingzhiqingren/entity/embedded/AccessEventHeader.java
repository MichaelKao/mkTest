package tw.musemodel.dingzhiqingren.entity.embedded;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "access_event_header", catalog = "youngme", schema = "public")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class AccessEventHeader implements Serializable {

	private static final long serialVersionUID = 5825352232848792942L;

	@EmbeddedId
	protected AccessEventHeaderPK accessEventHeaderPK;

	@Column(name = "header_value")
	private String headerValue;

	@JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private AccessEvent accessEvent;

	public AccessEventHeader() {
	}

	public AccessEventHeader(AccessEventHeaderPK accessEventHeaderPK) {
		this.accessEventHeaderPK = accessEventHeaderPK;
	}

	public AccessEventHeader(long eventId, String headerKey) {
		this.accessEventHeaderPK = new AccessEventHeaderPK(eventId, headerKey);
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(accessEventHeaderPK) ? "null" : accessEventHeaderPK.toString();
		}
	}
}
