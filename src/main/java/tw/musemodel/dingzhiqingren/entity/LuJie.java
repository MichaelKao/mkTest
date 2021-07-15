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
import javax.validation.constraints.Size;

/**
 * 绿界
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "lu_jie", uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"\"MerchantTradeDate\""
	})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LuJie implements java.io.Serializable {

	private static final long serialVersionUID = -4281476222101128901L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column(name = "session_id")
	private String sessionId;

	@Column(length = 20, name = "\"MerchantTradeDate\"")
	@Size(max = 20)
	private String merchantTradeDate;

	@Column(length = 20, name = "\"MerchantTradeNo\"")
	@Size(max = 20)
	private String merchantTradeNo;

	@Column(name = "\"TotalAmount\"")
	private Integer totalAmount;

	@Column(length = 400, name = "\"ItemName\"")
	@Size(max = 400)
	private String itemName;

	@Column(length = 20, name = "\"TradeNo\"")
	@Size(max = 20)
	private String tradeNo;

	@Column(name = "\"TradeAmt\"")
	private Integer tradeAmt;

	@Column(length = 20, name = "\"TradeDate\"")
	@Size(max = 20)
	private String tradeDate;

	@Column(length = 20, name = "\"PaymentType\"")
	@Size(max = 20)
	private String paymentType;

	@Column(length = 20, name = "\"PaymentDate\"")
	@Size(max = 20)
	private String paymentDate;

	@Column(name = "\"ChargeFee\"")
	private Float chargeFee;

	@Column(length = 8, name = "\"TradeStatus\"")
	@Size(max = 8)
	private String tradeStatus;

	@Column(length = 3, name = "\"BankCode\"")
	@Size(max = 3)
	private String bankCode;

	@Column(length = 16, name = "\"vAccount\"")
	@Size(max = 16)
	private String vAccount;

	@Column(length = 3, name = "\"ATMAccBank\"")
	@Size(max = 3)
	private String atmAccBank;

	@Column(length = 5, name = "\"ATMAccNo\"")
	@Size(max = 5)
	private String atmAccNo;

	@Column(length = 20, name = "\"Barcode1\"")
	@Size(max = 20)
	private String barcode1;

	@Column(length = 20, name = "\"Barcode2\"")
	@Size(max = 20)
	private String barcode2;

	@Column(length = 20, name = "\"Barcode3\"")
	@Size(max = 20)
	private String barcode3;

	@Column(length = 10, name = "\"BarcodeInfoPayFrom\"")
	@Size(max = 10)
	private String barcodeInfoPayFrom;

	@Column(length = 14, name = "\"PaymentNo\"")
	@Size(max = 14)
	private String paymentNo;

	@Column(length = 10, name = "\"CVSInfoPayFrom\"")
	@Size(max = 10)
	private String cvsInfoPayFrom;

	@Column(length = 6, name = "\"AuthCode\"")
	@Size(max = 6)
	private String authCode;

	@Column(name = "\"Gwsr\"")
	private Integer gwsr;

	@Column(name = "\"Amount\"")
	private Integer amount;

	@Column(name = "\"Stage\"")
	private Integer stage;

	@Column(name = "\"Stast\"")
	private Integer stast;

	@Column(name = "\"Staed\"")
	private Integer staed;

	@Column(name = "\"Eci\"")
	private Integer eci;

	@Column(length = 6, name = "\"Card6No\"")
	@Size(max = 6)
	private String card6No;

	@Column(length = 4, name = "\"Card4No\"")
	@Size(max = 4)
	private String card4No;

	@Column(length = 1, name = "\"PeriodType\"")
	@Size(max = 1)
	private String periodType;

	@Column(name = "\"Frequency\"")
	private Short frequency;

	@Column(name = "\"ExecTimes\"")
	private Short execTimes;

	@Column(name = "\"PeriodAmount\"")
	private Integer periodAmount;

	@Column(length = 20, name = "\"ProcessDate\"")
	@Size(max = 20)
	private String processDate;

	@Column(name = "\"TotalSuccessTimes\"")
	private Integer totalSuccessTimes;

	@Column(length = 60, name = "\"MerchantMemberID\"")
	@Size(max = 60)
	private String merchantMemberId;

	@Column(length = 200, name = "\"CustomField\"")
	@Size(max = 200)
	private String customField;

//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "luJie")
//	@JsonIgnore
//	private Collection<History> histories;
	/**
	 * 默认构造器
	 */
	public LuJie() {
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LuJie)) {
			return false;
		}
		LuJie other = (LuJie) object;
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
	 * @return 主键
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id 主键
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 分配给会话的标识符
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId 分配给会话的标识符
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return 订单资讯：厂商交易时间
	 */
	public String getMerchantTradeDate() {
		return merchantTradeDate;
	}

	/**
	 * @param merchantTradeDate 订单资讯：厂商交易时间
	 */
	public void setMerchantTradeDate(String merchantTradeDate) {
		this.merchantTradeDate = merchantTradeDate;
	}

	/**
	 * @return 订单资讯：特店交易编号
	 */
	public String getMerchantTradeNo() {
		return merchantTradeNo;
	}

	/**
	 * @param merchantTradeNo 订单资讯：特店交易编号
	 */
	public void setMerchantTradeNo(String merchantTradeNo) {
		this.merchantTradeNo = merchantTradeNo;
	}

	/**
	 * @return 订单资讯：交易金额
	 */
	public Integer getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount 订单资讯：交易金额
	 */
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return 订单资讯：商品名称
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName 订单资讯：商品名称
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return 订单资讯：绿界交易编号
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * @param tradeNo 订单资讯：绿界交易编号
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * @return 订单资讯：交易金额
	 */
	public Integer getTradeAmt() {
		return tradeAmt;
	}

	/**
	 * @param tradeAmt 订单资讯：交易金额
	 */
	public void setTradeAmt(Integer tradeAmt) {
		this.tradeAmt = tradeAmt;
	}

	/**
	 * @return 订单资讯：订单成立时间
	 */
	public String getTradeDate() {
		return tradeDate;
	}

	/**
	 * @param tradeDate 订单资讯：订单成立时间
	 */
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	/**
	 * @return 订单资讯：付款方式
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType 订单资讯：付款方式
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return 订单资讯：付款时间
	 */
	public String getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate 订单资讯：付款时间
	 */
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return 订单资讯：手续费
	 */
	public Float getChargeFee() {
		return chargeFee;
	}

	/**
	 * @param chargeFee 订单资讯：手续费
	 */
	public void setChargeFee(Float chargeFee) {
		this.chargeFee = chargeFee;
	}

	/**
	 * @return 订单资讯：交易状态
	 */
	public String getTradeStatus() {
		return tradeStatus;
	}

	/**
	 * @param tradeStatus 订单资讯：交易状态
	 */
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	/**
	 * @return ATM 资讯：缴费银行代码
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode ATM 资讯：缴费银行代码
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return ATM 资讯：缴费虚拟帐号
	 */
	public String getVAccount() {
		return vAccount;
	}

	/**
	 * @param vAccount ATM 资讯：缴费虚拟帐号
	 */
	public void setVAccount(String vAccount) {
		this.vAccount = vAccount;
	}

	/**
	 * @return ATM 资讯：付款人银行代码
	 */
	public String getATMAccBank() {
		return atmAccBank;
	}

	/**
	 * @param atmAccBank ATM 资讯：付款人银行代码
	 */
	public void setATMAccBank(String atmAccBank) {
		this.atmAccBank = atmAccBank;
	}

	/**
	 * @return ATM 资讯：付款人银行帐号后五码
	 */
	public String getATMAccNo() {
		return atmAccNo;
	}

	/**
	 * @param atmAccNo ATM 资讯：付款人银行帐号后五码
	 */
	public void setATMAccNo(String atmAccNo) {
		this.atmAccNo = atmAccNo;
	}

	/**
	 * @return 超商条码资讯：条码第一段号码
	 */
	public String getBarcode1() {
		return barcode1;
	}

	/**
	 * @param barcode1 超商条码资讯：条码第一段号码
	 */
	public void setBarcode1(String barcode1) {
		this.barcode1 = barcode1;
	}

	/**
	 * @return 超商条码资讯：条码第二段号码
	 */
	public String getBarcode2() {
		return barcode2;
	}

	/**
	 * @param barcode2 超商条码资讯：条码第二段号码
	 */
	public void setBarcode2(String barcode2) {
		this.barcode2 = barcode2;
	}

	/**
	 * @return 超商条码资讯：条码第三段号码
	 */
	public String getBarcode3() {
		return barcode3;
	}

	/**
	 * @param barcode3 超商条码资讯：条码第三段号码
	 */
	public void setBarcode3(String barcode3) {
		this.barcode3 = barcode3;
	}

	/**
	 * @return 超商条码资讯：缴费超商
	 */
	public String getBarcodeInfoPayFrom() {
		return barcodeInfoPayFrom;
	}

	/**
	 * @param barcodeInfoPayFrom 超商条码资讯：缴费超商
	 */
	public void setBarcodeInfoPayFrom(String barcodeInfoPayFrom) {
		this.barcodeInfoPayFrom = barcodeInfoPayFrom;
	}

	/**
	 * @return 超商代码资讯：缴费代码
	 */
	public String getPaymentNo() {
		return paymentNo;
	}

	/**
	 * @param paymentNo 超商代码资讯：缴费代码
	 */
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	/**
	 * @return 超商代码资讯：缴费超商
	 */
	public String getCVSInfoPayFrom() {
		return cvsInfoPayFrom;
	}

	/**
	 * @param cvsInfoPayFrom 超商代码资讯：缴费超商
	 */
	public void setCVSInfoPayFrom(String cvsInfoPayFrom) {
		this.cvsInfoPayFrom = cvsInfoPayFrom;
	}

	/**
	 * @return 信用卡资讯：银行授权码
	 */
	public String getAuthCode() {
		return authCode;
	}

	/**
	 * @param authCode 信用卡资讯：银行授权码
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * @return 信用卡资讯：授权交易单号
	 */
	public Integer getGwsr() {
		return gwsr;
	}

	/**
	 * @param gwsr 信用卡资讯：授权交易单号
	 */
	public void setGwsr(Integer gwsr) {
		this.gwsr = gwsr;
	}

	/**
	 * @return 信用卡资讯：金额
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount 信用卡资讯：金额
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * @return 信用卡资讯：分期期数
	 */
	public Integer getStage() {
		return stage;
	}

	/**
	 * @param stage 信用卡资讯：分期期数
	 */
	public void setStage(Integer stage) {
		this.stage = stage;
	}

	/**
	 * @return 信用卡资讯：首期金额
	 */
	public Integer getStast() {
		return stast;
	}

	/**
	 * @param stast 信用卡资讯：首期金额
	 */
	public void setStast(Integer stast) {
		this.stast = stast;
	}

	/**
	 * @return 信用卡资讯：各期金额
	 */
	public Integer getStaed() {
		return staed;
	}

	/**
	 * @param staed 信用卡资讯：各期金额
	 */
	public void setStaed(Integer staed) {
		this.staed = staed;
	}

	/**
	 * @return 信用卡资讯：3D(VBV) 回传值
	 */
	public Integer getEci() {
		return eci;
	}

	/**
	 * @param eci 信用卡资讯：3D(VBV) 回传值
	 */
	public void setEci(Integer eci) {
		this.eci = eci;
	}

	/**
	 * @return 信用卡资讯：信用卡卡号前六码
	 */
	public String getCard6No() {
		return card6No;
	}

	/**
	 * @param card6No 信用卡资讯：信用卡卡号前六码
	 */
	public void setCard6No(String card6No) {
		this.card6No = card6No;
	}

	/**
	 * @return 信用卡资讯：信用卡卡号末四码
	 */
	public String getCard4No() {
		return card4No;
	}

	/**
	 * @param card4No 信用卡资讯：信用卡卡号末四码
	 */
	public void setCard4No(String card4No) {
		this.card4No = card4No;
	}

	/**
	 * @return 信用卡资讯：定期定额周期种类
	 */
	public String getPeriodType() {
		return periodType;
	}

	/**
	 * @param periodType 信用卡资讯：定期定额周期种类
	 */
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	/**
	 * @return 信用卡资讯：定期定额执行频率
	 */
	public Short getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency 信用卡资讯：定期定额执行频率
	 */
	public void setFrequency(Short frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return 信用卡资讯：定期定额执行次数
	 */
	public Short getExecTimes() {
		return execTimes;
	}

	/**
	 * @param execTimes 信用卡资讯：定期定额执行次数
	 */
	public void setExecTimes(Short execTimes) {
		this.execTimes = execTimes;
	}

	/**
	 * @return 信用卡资讯：定期定额每次授权金额
	 */
	public Integer getPeriodAmount() {
		return periodAmount;
	}

	/**
	 * @param periodAmount 信用卡资讯：定期定额每次授权金额
	 */
	public void setPeriodAmount(Integer periodAmount) {
		this.periodAmount = periodAmount;
	}

	/**
	 * @return 信用卡资讯：交易时间
	 */
	public String getProcessDate() {
		return processDate;
	}

	/**
	 * @param processDate 信用卡资讯：交易时间
	 */
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	/**
	 * @return 目前已成功授权的次数
	 */
	public Integer getTotalSuccessTimes() {
		return totalSuccessTimes;
	}

	/**
	 * @param totalSuccessTimes 目前已成功授权的次数
	 */
	public void setTotalSuccessTimes(Integer totalSuccessTimes) {
		this.totalSuccessTimes = totalSuccessTimes;
	}

	/**
	 * @return 消费者资讯：消费者会员编号
	 */
	public String getMerchantMemberId() {
		return merchantMemberId;
	}

	/**
	 * @param merchantMemberId 消费者资讯：消费者会员编号
	 */
	public void setMerchantMemberId(String merchantMemberId) {
		this.merchantMemberId = merchantMemberId;
	}

	/**
	 * @return 特店自订栏位：厂商自订栏位
	 */
	public String getCustomField() {
		return customField;
	}

	/**
	 * @param customField 特店自订栏位：厂商自订栏位
	 */
	public void setCustomField(String customField) {
		this.customField = customField;
	}

//	/**
//	 * @return 历程
//	 */
//	public Collection<History> getHistories() {
//		return histories;
//	}
//
//	/**
//	 * @param histories 历程
//	 */
//	public void setHistories(Collection<History> histories) {
//		this.histories = histories;
//	}
}
