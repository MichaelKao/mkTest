package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * 储值方案
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "chu_zhi_fang_an", catalog = "yuepao", schema = "yuepao", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"ming_cheng"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plan implements java.io.Serializable {

	private static final long serialVersionUID = 6849894470913418453L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Short id;

	@Basic(optional = false)
	@Column(name = "ming_cheng", nullable = false)
	@NotNull
	private String name;

	@Basic(optional = false)
	@Column(name = "dian_shu", nullable = false)
	@NotNull
	private short points;

	@Basic(optional = false)
	@Column(name = "shou_xu_fei", nullable = false)
	@NotNull
	private short fee;

	@Basic(optional = false)
	@Column(name = "jin_e", nullable = false)
	@NotNull
	private int amount;

	/**
	 * 默认构造器
	 */
	public Plan() {
	}

	/**
	 * @param name 方案名称
	 * @param points 点数
	 * @param fee 手续费
	 * @param amount 金额
	 */
	public Plan(String name, short points, short fee, int amount) {
		this.name = name;
		this.points = points;
		this.fee = fee;
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Plan)) {
			return false;
		}
		Plan other = (Plan) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
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
	 * @return 主鍵
	 */
	public Short getId() {
		return id;
	}

	/**
	 * @param id 主鍵
	 */
	public void setId(Short id) {
		this.id = id;
	}

	/**
	 * @return 方案名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 方案名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 点数
	 */
	public short getPoints() {
		return points;
	}

	/**
	 * @param points 点数
	 */
	public void setPoints(short points) {
		this.points = points;
	}

	/**
	 * @return 手续费
	 */
	public short getFee() {
		return fee;
	}

	/**
	 * @param fee 手续费
	 */
	public void setFee(short fee) {
		this.fee = fee;
	}

	/**
	 * @return 金额
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount 金额
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
