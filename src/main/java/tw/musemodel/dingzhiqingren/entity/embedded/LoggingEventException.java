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
@Table(name = "logging_event_exception", catalog = "youngme", schema = "public")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class LoggingEventException implements Serializable {

	private static final long serialVersionUID = 5553350548247080515L;

	@EmbeddedId
	protected LoggingEventExceptionPK loggingEventExceptionPK;

	@Column
	private String traceLine;

	@JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private LoggingEvent loggingEvent;

	public LoggingEventException() {
	}

	public LoggingEventException(LoggingEventExceptionPK loggingEventExceptionPK) {
		this.loggingEventExceptionPK = loggingEventExceptionPK;
	}

	public LoggingEventException(long eventId, short i) {
		this.loggingEventExceptionPK = new LoggingEventExceptionPK(eventId, i);
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(loggingEventExceptionPK) ? "null" : loggingEventExceptionPK.toString();
		}
	}
}
