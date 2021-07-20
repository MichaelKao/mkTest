package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * 提取車馬費紀錄
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "ti_qu_che_ma_fei")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalRecord implements java.io.Serializable {

	private static final long serialVersionUID = 4701919192690927987L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Basic(optional = false)
	@JoinColumn(name = "honey", nullable = false, referencedColumnName = "id")
	@ManyToOne
	private Lover honey;

	@Basic(optional = false)
	@Column(name = "jin_e", nullable = false)
	private Long points;

	@Column(name = "zhuang_tai", nullable = false)
	private Boolean status;

	@Column(name = "ti_qu_feng_shi", nullable = false)
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private WayOfWithdrawal way;

	@Column(name = "shi_bai_yuan_yin")
	private String failReason;

	@Column(name = "shi_chuo", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	@Basic(optional = false)
	@JoinColumn(name = "li_cheng", nullable = false, referencedColumnName = "id")
	@ManyToOne
	private History history;

	/**
	 * 默认构造器
	 */
	public WithdrawalRecord() {
		status = null;
		timestamp = new Date(System.currentTimeMillis());
	}

	public WithdrawalRecord(Lover honey, Long points, WayOfWithdrawal way, History history) {
		this();
		this.honey = honey;
		this.points = points;
		this.way = way;
		this.history = history;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof WithdrawalRecord)) {
			return false;
		}
		WithdrawalRecord other = (WithdrawalRecord) object;
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
	public Integer getId() {
		return id;
	}

	/**
	 * @param id 主鍵
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return 甜心
	 */
	public Lover getHoney() {
		return honey;
	}

	/**
	 * @param honey 甜心
	 */
	public void setHoney(Lover honey) {
		this.honey = honey;
	}

	/**
	 * @return 提領金額
	 */
	public Long getPoints() {
		return points;
	}

	/**
	 * @param points 提領金額
	 */
	public void setPoints(Long points) {
		this.points = points;
	}

	/**
	 * @return 狀態
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status 狀態
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return 提領方式
	 */
	public WayOfWithdrawal getWay() {
		return way;
	}

	/**
	 * @param way 提領方式
	 */
	public void setWay(WayOfWithdrawal way) {
		this.way = way;
	}

	/**
	 * @return 時戳
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp 時戳
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return 失敗原因
	 */
	public String getFailReason() {
		return failReason;
	}

	/**
	 * @param failReason 失敗原因
	 */
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	/**
	 * @return 歷程
	 */
	public History getHistory() {
		return history;
	}

	/**
	 * @param history 歷程
	 */
	public void setHistory(History history) {
		this.history = history;
	}

	/**
	 * 提取方式
	 *
	 * @author m@musemodel.tw
	 */
	public enum WayOfWithdrawal {

		WIRE_TRANSFER("WIRE_TRANSFER", 1),
		PAYPAL("PAYPAL", 2);

		private String label;

		private int index;

		private WayOfWithdrawal(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}
}
