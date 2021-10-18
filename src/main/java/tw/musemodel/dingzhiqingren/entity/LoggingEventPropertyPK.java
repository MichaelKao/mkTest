/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class LoggingEventPropertyPK implements Serializable {

	private static final long serialVersionUID = -7901437460781189284L;

	@Basic(optional = false)
	@Column(name = "event_id", nullable = false)
	@NotNull
	private long eventId;

	@Basic(optional = false)
	@Column(name = "mapped_key", nullable = false)
	@NotNull
	private String mappedKey;

	public LoggingEventPropertyPK() {
	}

	public LoggingEventPropertyPK(long eventId, String mappedKey) {
		this.eventId = eventId;
		this.mappedKey = mappedKey;
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
