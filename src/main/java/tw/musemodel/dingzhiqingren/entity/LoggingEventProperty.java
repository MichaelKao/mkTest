package tw.musemodel.dingzhiqingren.entity;

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
 * The logging_event_property is used to store the keys and values contained in
 * the MDC or the Context.
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "logging_event_property", catalog = "youngme", schema = "public")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class LoggingEventProperty implements Serializable {

	private static final long serialVersionUID = 5519728631078496134L;

	@EmbeddedId
	protected LoggingEventPropertyPK loggingEventPropertyPK;

	@Column(name = "mapped_value")
	private String mappedValue;

	@JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private LoggingEvent loggingEvent;

	public LoggingEventProperty() {
	}

	public LoggingEventProperty(LoggingEventPropertyPK loggingEventPropertyPK) {
		this.loggingEventPropertyPK = loggingEventPropertyPK;
	}

	public LoggingEventProperty(long eventId, String mappedKey) {
		this.loggingEventPropertyPK = new LoggingEventPropertyPK(eventId, mappedKey);
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(loggingEventPropertyPK) ? "null" : loggingEventPropertyPK.toString();
		}
	}
}
