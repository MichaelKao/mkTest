package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
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
	protected LineGivenPK lineGivenPK;

	@Column(name = "jie_guo")
	private Boolean response;

	@ManyToOne(optional = false)
	@JoinColumn(name = "nu_sheng", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@MapsId("femaleId")
	private Lover female;

	@ManyToOne(optional = false)
	@JoinColumn(name = "nan_sheng", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@MapsId("maleId")
	private Lover male;

	public LineGiven() {
	}

	public LineGiven(LineGivenPK lineGivenPK, Boolean response) {
		this.lineGivenPK = lineGivenPK;
		this.response = response;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 19 * hash + Objects.hashCode(this.lineGivenPK);
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
		if (!Objects.equals(this.lineGivenPK, other.lineGivenPK)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "LineGiven{" + "lineGivenPK=" + lineGivenPK + '}';
	}

	/**
	 * @return 複合主鍵
	 */
	public LineGivenPK getLineGivenPK() {
		return lineGivenPK;
	}

	/**
	 * @param lineGivenPK 複合主鍵
	 */
	public void setLineGivenPK(LineGivenPK lineGivenPK) {
		this.lineGivenPK = lineGivenPK;
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
	public Lover getFemale() {
		return female;
	}

	/**
	 * @param female 女生
	 */
	public void setFemale(Lover female) {
		this.female = female;
	}

	/**
	 * @return 男生
	 */
	public Lover getMale() {
		return male;
	}

	/**
	 * @param male 男生
	 */
	public void setMale(Lover male) {
		this.male = male;
	}
}
