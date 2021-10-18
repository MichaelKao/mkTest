package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author p@musemodel.tw
 */
@Data
@Embeddable
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoggingEventExceptionPK implements Serializable {

	private static final long serialVersionUID = -8754558311876918284L;

	@Basic(optional = false)
	@Column(name = "event_id", nullable = false)
	@NotNull
	private long eventId;

	@Basic(optional = false)
	@Column(nullable = false)
	@NotNull
	private short i;

	public LoggingEventExceptionPK() {
	}

	public LoggingEventExceptionPK(long eventId, short i) {
		this.eventId = eventId;
		this.i = i;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return null;
		}
	}
}
