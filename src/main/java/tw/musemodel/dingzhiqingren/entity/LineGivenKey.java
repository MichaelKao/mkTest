package tw.musemodel.dingzhiqingren.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author m@musemodel.tw
 */
@Embeddable
public class LineGivenKey implements Serializable {

	private static final long serialVersionUID = 4958473382527438476L;

	@Basic(optional = false)
	@Column(name = "nu_sheng", nullable = false)
	private Integer female;

	@Basic(optional = false)
	@Column(name = "nan_sheng", nullable = false)
	private Integer male;

	public LineGivenKey() {
	}

	/**
	 * @param female
	 * @param male
	 */
	public LineGivenKey(Integer female, Integer male) {
		this.female = female;
		this.male = male;
	}

	@Override
	public int hashCode() {
		int hash = 7;
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
		final LineGivenKey other = (LineGivenKey) obj;
		if (!Objects.equals(this.female, other.female)) {
			return false;
		}
		if (!Objects.equals(this.male, other.male)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "LineGivenPK{" + "female=" + female + ", male=" + male + '}';
	}

	/**
	 * @return 女生
	 */
	public Integer getFemale() {
		return female;
	}

	/**
	 * @param female 女生
	 */
	public void setFemale(Integer female) {
		this.female = female;
	}

	/**
	 * @return 男生
	 */
	public Integer getMale() {
		return male;
	}

	/**
	 * @param male 男生
	 */
	public void setMale(Integer male) {
		this.male = male;
	}
}
