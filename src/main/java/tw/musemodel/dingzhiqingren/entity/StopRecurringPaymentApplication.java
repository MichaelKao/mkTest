package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 申请解除定期定额
 *
 * @author p@musemodel.tw
 */
@Entity
@Data
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "shen_qing_jie_chu_ding_qi_ding_e")
public class StopRecurringPaymentApplication implements Serializable {

	private static final long serialVersionUID = -5650062539323230592L;

	/**
	 * 主键
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	/**
	 * 申请人
	 */
	@Basic(optional = false)
	@JoinColumn(name = "shei_shen_qing", nullable = false, referencedColumnName = "id")
	@ManyToOne(optional = false)
	@NotNull
	private Lover applicant;

	/**
	 * 申请时戳
	 */
	@Basic(optional = false)
	@Column(name = "shen_qing_shi", nullable = false)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	/**
	 * 处理人
	 */
	@JoinColumn(name = "shei_chu_li", referencedColumnName = "id")
	@ManyToOne
	private Lover handler;

	/**
	 * 处理时戳
	 */
	@Column(name = "chu_li_shi")
	@Temporal(TemporalType.TIMESTAMP)
	private Date handledAt;

	/**
	 * 歷程
	 */
	@JoinColumn(name = "li_cheng", nullable = false, referencedColumnName = "id")
	@ManyToOne
	private History history;

	/**
	 * 信箱
	 */
	@Column(name = "xin_xiang", nullable = false)
	private String email;

	/**
	 * 信用卡後四碼
	 */
	@Column(name = "hou_si_ma", nullable = false, length = 4)
	private String lastFourDigits;

	/**
	 * 默认构造器
	 */
	public StopRecurringPaymentApplication() {
		createdAt = new Date(System.currentTimeMillis());
	}

	/**
	 * 构造器
	 *
	 * @param applicant 申请人
	 */
	public StopRecurringPaymentApplication(Lover applicant, History history, String email, String lastFourDigits) {
		this();
		this.applicant = applicant;
		this.history = history;
		this.email = email;
		this.lastFourDigits = lastFourDigits;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
