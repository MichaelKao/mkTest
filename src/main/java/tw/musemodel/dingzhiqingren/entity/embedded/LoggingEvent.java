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
@SuppressWarnings({"PersistenceUnitPresent"})
@Table(name = "logging_event", catalog = "youngme", schema = "public")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class LoggingEvent implements Serializable {

	private static final long serialVersionUID = -466197503803517337L;

	@Basic(optional = false)
	@Column(name = "event_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long eventId;

	@Column(name = "timestmp", nullable = false)
	private Long timestmp;

	@Column(name = "formatted_message", nullable = false)
	private String formattedMessage;

	@Column(name = "logger_name", nullable = false)
	private String loggerName;

	@Column(name = "level_string", nullable = false)
	private String levelString;

	@Column(name = "thread_name")
	private String threadName;

	@Column(name = "arg0")
	private String arg0;

	@Column(name = "arg1")
	private String arg1;

	@Column(name = "arg2")
	private String arg2;

	@Column(name = "arg3")
	private String arg3;

	@Column(name = "reference_flag")
	private Short referenceFlag;

	@Column(name = "caller_filename", nullable = false)
	private String callerFilename;

	@Column(name = "caller_class", nullable = false)
	private String callerClass;

	@Column(name = "caller_method", nullable = false)
	private String callerMethod;

	@Column(name = "caller_line", nullable = false)
	private String callerLine;

	@Basic(optional = false)
	@Column(nullable = false)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurred;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "loggingEvent")
	private Collection<LoggingEventException> loggingEventExceptions;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "loggingEvent")
	private Collection<LoggingEventProperty> loggingEventProperties;

	public LoggingEvent() {
	}

	protected LoggingEvent(Long eventId) {
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
