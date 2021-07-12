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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 提取車馬費資訊
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "ti_qu_feng_shi_zi_xun")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalInfo implements java.io.Serializable {

	private static final long serialVersionUID = -6227030647890189857L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Basic(optional = false)
	@JoinColumn(name = "honey", nullable = false, referencedColumnName = "id")
	@OneToOne
	private Lover honey;

	@Column(name = "wire_transfer_bank_code", nullable = false)
	private String wireTransferBankCode;

	@Column(name = "wire_transfer_branch_code", nullable = false)
	private String wireTransferBranchCode;

	@Column(name = "wire_transfer_account_name", nullable = false)
	private String wireTransferAccountName;

	@Column(name = "wire_transfer_account_number", nullable = false)
	private String wireTransferAccountNumber;

	/**
	 * 默认构造器
	 */
	public WithdrawalInfo() {
	}

	public WithdrawalInfo(Lover honey, String wireTransferBankCode, String wireTransferBranchCode,
		String wireTransferAccountName, String wireTransferAccountNumber) {
		this.honey = honey;
		this.wireTransferBankCode = wireTransferBankCode;
		this.wireTransferBranchCode = wireTransferBranchCode;
		this.wireTransferAccountName = wireTransferAccountName;
		this.wireTransferAccountNumber = wireTransferAccountNumber;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof WithdrawalInfo)) {
			return false;
		}
		WithdrawalInfo other = (WithdrawalInfo) object;
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
	 * @return 銀行代碼
	 */
	public String getWireTransferBankCode() {
		return wireTransferBankCode;
	}

	/**
	 * @param wireTransferBankCode 銀行代碼
	 */
	public void setWireTransferBankCode(String wireTransferBankCode) {
		this.wireTransferBankCode = wireTransferBankCode;
	}

	/**
	 * @return 分行代碼
	 */
	public String getWireTransferBranchCode() {
		return wireTransferBranchCode;
	}

	/**
	 * @param wireTransferBranchCode 分行代碼
	 */
	public void setWireTransferBranchCode(String wireTransferBranchCode) {
		this.wireTransferBranchCode = wireTransferBranchCode;
	}

	/**
	 * @return 戶名
	 */
	public String getWireTransferAccountName() {
		return wireTransferAccountName;
	}

	/**
	 * @param wireTransferAccountName 戶名
	 */
	public void setWireTransferAccountName(String wireTransferAccountName) {
		this.wireTransferAccountName = wireTransferAccountName;
	}

	/**
	 * @return 匯款帳號
	 */
	public String getWireTransferAccountNumber() {
		return wireTransferAccountNumber;
	}

	/**
	 * @param wireTransferAccountNumber 匯款帳號
	 */
	public void setWireTransferAccountNumber(String wireTransferAccountNumber) {
		this.wireTransferAccountNumber = wireTransferAccountNumber;
	}

}
