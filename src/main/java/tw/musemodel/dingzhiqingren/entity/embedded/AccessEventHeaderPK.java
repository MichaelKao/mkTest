package tw.musemodel.dingzhiqingren.entity.embedded;

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
public class AccessEventHeaderPK implements Serializable {

	private static final long serialVersionUID = -6452586627899305865L;

	@Basic(optional = false)
	@Column(name = "event_id", nullable = false)
	@NotNull
	private long eventId;

	@Basic(optional = false)
	@Column(name = "header_key", nullable = false)
	@NotNull
	private String headerKey;

	public AccessEventHeaderPK() {
	}

	public AccessEventHeaderPK(long eventId, String headerKey) {
		this.eventId = eventId;
		this.headerKey = headerKey;
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
