package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * 给不给赖
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "gei_bu_gei_lai")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineGiven implements java.io.Serializable {

	private static final long serialVersionUID = 2786894097864889670L;

	@EmbeddedId
	protected LineGivenKey id;

	@Column(name = "jie_guo")
	private Boolean response;

	@ManyToOne(optional = false)
	@JoinColumn(name = "nu_sheng", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@MapsId("femaleId")
	private Lover girl;

	@ManyToOne(optional = false)
	@JoinColumn(name = "nan_sheng", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@MapsId("maleId")
	private Lover guy;

	public LineGiven() {
	}

	public LineGiven(LineGivenKey id, Boolean response) {
		this.id = id;
		this.response = response;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 19 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LineGiven other = (LineGiven) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}

	/**
	 * @return 複合主鍵
	 */
	public LineGivenKey getId() {
		return id;
	}

	/**
	 * @param id 複合主鍵
	 */
	public void setId(LineGivenKey id) {
		this.id = id;
	}

	/**
	 * @return 回應結果
	 */
	public Boolean getResponse() {
		return response;
	}

	/**
	 * @param response 回應結果
	 */
	public void setResponse(Boolean response) {
		this.response = response;
	}

	/**
	 * @return 女生
	 */
	public Lover getGirl() {
		return girl;
	}

	/**
	 * @param girl 女生
	 */
	public void setGirl(Lover girl) {
		this.girl = girl;
	}

	/**
	 * @return 男生
	 */
	public Lover getGuy() {
		return guy;
	}

	/**
	 * @param guy 男生
	 */
	public void setGuy(Lover guy) {
		this.guy = guy;
	}
}
