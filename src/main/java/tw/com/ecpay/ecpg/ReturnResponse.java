package tw.com.ecpay.ecpg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * 付款結果通知
 *
 * @author m@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnResponse {

	@JsonProperty("MerchantID")
	private String merchantId;

	@JsonProperty("RpHeader")
	private RpHeader rpHeader;

	@JsonProperty("TransCode")
	private Integer transCode;

	@JsonProperty("TransMsg")
	private String transMsg;

	@JsonProperty("Data")
	private String data;

	/**
	 * 默認構造器
	 */
	public ReturnResponse() {
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return "null";
		}
	}

	/**
	 * @return 特店編號
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId 特店編號
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return 回傳資料
	 */
	public RpHeader getRpHeader() {
		return rpHeader;
	}

	/**
	 * @param rpHeader 回傳資料
	 */
	public void setRpHeader(RpHeader rpHeader) {
		this.rpHeader = rpHeader;
	}

	/**
	 * @return 回傳代碼
	 */
	public Integer getTransCode() {
		return transCode;
	}

	/**
	 * 1 代表傳輸資料(MerchantID、RqHeader、Data)接收成功，其餘均為失敗
	 *
	 * @param transCode 回傳代碼
	 */
	public void setTransCode(Integer transCode) {
		this.transCode = transCode;
	}

	/**
	 * @return 回傳訊息
	 */
	public String getTransMsg() {
		return transMsg;
	}

	/**
	 * @param transMsg 回傳訊息
	 */
	public void setTransMsg(String transMsg) {
		this.transMsg = transMsg;
	}

	/**
	 * @return 加密資料
	 */
	public String getData() {
		return data;
	}

	/**
	 * 加密過 JSON 格式的資料。
	 *
	 * @param data 加密資料
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 回传数据
	 *
	 * @author m@musemodel.tw
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class RpHeader {

		@JsonProperty("Timestamp")
		private Long timestamp;

		/**
		 * 默认构造器
		 */
		public RpHeader() {
		}

		@Override
		public String toString() {
			try {
				return new JsonMapper().writeValueAsString(this);
			} catch (JsonProcessingException ignore) {
				return "null";
			}
		}

		/**
		 * @return 回传时间
		 */
		public Long getTimestamp() {
			return timestamp;
		}

		/**
		 * 时间戳 Unix timestamp。
		 *
		 * @param timestamp 回传时间
		 */
		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}
	}

	/**
	 * 應用場景 I：Server 端方式(Server
	 * POST)(ReturnURL)當消費者付款完成後，廠商接受綠界的付款結果訊息，並回應接收訊息。
	 *
	 * <ul>
	 * <li>Step1. 綠界：以 Server POST 方式傳送付款結果訊息至廠商的 Server 網址(ReturnURL)</li>
	 * <li>Step2. 廠商：收到綠界的付款結果訊息，回應 1|OK</li>
	 * </ul>
	 *
	 *
	 * 應用場景 II：當訂單是使用信用卡定期定額的交易時，在每次授權成功後，綠界會傳送付款通知結果。
	 * <ul>
	 * <li>Step 1. 綠界：第二次授權是由綠界的排程進行授權，交易授權成功後以 Server POST
	 * 方式傳送付款通知至廠商傳送的付款完成通知回傳網址[PeriodReturnURL]。</li>
	 * <li>Step 2. 廠商：收到綠界的付款通知訊息後，需比對檢查碼是否相符。</li>
	 * </ul>
	 *
	 * @author m@musemodel.tw
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data {

		@JsonProperty("RtnCode")
		private Integer rtnCode;

		@JsonProperty("RtnMsg")
		private String rtnMsg;

		@JsonProperty("PlatformID")
		private String platformId;

		@JsonProperty("MerchantID")
		private String merchantId;

		@JsonProperty("OrderInfo")
		private OrderInfo orderInfo;

		@JsonProperty("CVSInfo")
		private CVSInfo cvsInfo;

		@JsonProperty("BarcodeInfo")
		private BarcodeInfo barcodeInfo;

		@JsonProperty("ATMInfo")
		private ATMInfo atmInfo;

		@JsonProperty("CardInfo")
		private CardInfo cardInfo;

		@JsonProperty("CustomField")
		private String customField;

		/**
		 * 默認構造器
		 */
		public Data() {
		}

		@Override
		public String toString() {
			try {
				return new JsonMapper().writeValueAsString(this);
			} catch (JsonProcessingException ignore) {
				return "null";
			}
		}

		/**
		 * @return 交易狀態
		 */
		public Integer getRtnCode() {
			return rtnCode;
		}

		/**
		 * 1 代表 API 執行成功，其餘代碼均為失敗。
		 *
		 * @param rtnCode 交易狀態
		 */
		public void setRtnCode(Integer rtnCode) {
			this.rtnCode = rtnCode;
		}

		/**
		 * @return 回應訊息
		 */
		public String getRtnMsg() {
			return rtnMsg;
		}

		/**
		 * @param rtnMsg 回應訊息
		 */
		public void setRtnMsg(String rtnMsg) {
			this.rtnMsg = rtnMsg;
		}

		/**
		 * @return 平台商編號
		 */
		public String getPlatformId() {
			return platformId;
		}

		/**
		 * 特約合作平台商特店編號。
		 *
		 * @param platformId 平台商編號
		 */
		public void setPlatformId(String platformId) {
			this.platformId = platformId;
		}

		/**
		 * @return 廠商編號
		 */
		public String getMerchantId() {
			return merchantId;
		}

		/**
		 * @param merchantId 廠商編號
		 */
		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		/**
		 * @return 訂單資訊
		 */
		public OrderInfo getOrderInfo() {
			return orderInfo;
		}

		/**
		 * @param orderInfo 訂單資訊
		 */
		public void setOrderInfo(OrderInfo orderInfo) {
			this.orderInfo = orderInfo;
		}

		/**
		 * @return 超商代码资讯
		 */
		public CVSInfo getCVSInfo() {
			return cvsInfo;
		}

		/**
		 * @param cvsInfo 超商代码资讯
		 */
		public void setCVSInfo(CVSInfo cvsInfo) {
			this.cvsInfo = cvsInfo;
		}

		/**
		 * @return 超商條碼資訊
		 */
		public BarcodeInfo getBarcodeInfo() {
			return barcodeInfo;
		}

		/**
		 * @param barcodeInfo 超商條碼資訊
		 */
		public void setBarcodeInfo(BarcodeInfo barcodeInfo) {
			this.barcodeInfo = barcodeInfo;
		}

		/**
		 * @return ATM 資訊
		 */
		public ATMInfo getATMInfo() {
			return atmInfo;
		}

		/**
		 * @param atmInfo ATM 資訊
		 */
		public void setATMInfo(ATMInfo atmInfo) {
			this.atmInfo = atmInfo;
		}

		/**
		 * @return 信用卡授權資訊
		 */
		public CardInfo getCardInfo() {
			return cardInfo;
		}

		/**
		 * @param cardInfo 信用卡授權資訊
		 */
		public void setCardInfo(CardInfo cardInfo) {
			this.cardInfo = cardInfo;
		}

		/**
		 * @return 廠商自訂欄位
		 */
		public String getCustomField() {
			return customField;
		}

		/**
		 * 提供特店使用客制化欄位
		 *
		 * @param customField 廠商自訂欄位
		 */
		public void setCustomField(String customField) {
			this.customField = customField;
		}

		/**
		 * 订单资讯
		 *
		 * @author m@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class OrderInfo {

			@JsonProperty("MerchantTradeNo")
			private String merchantTradeNo;

			@JsonProperty("TradeNo")
			private String tradeNo;

			@JsonProperty("PaymentDate")
			private String paymentDate;

			@JsonProperty("TradeAmt")
			private Integer tradeAmt;

			@JsonProperty("PaymentType")
			private String paymentType;

			@JsonProperty("TradeDate")
			private String tradeDate;

			@JsonProperty("ChargeFee")
			private Float chargeFee;

			@JsonProperty("TradeStatus")
			private String tradeStatus;

			/**
			 * 默認構造器
			 */
			public OrderInfo() {
			}

			@Override
			public String toString() {
				try {
					return new JsonMapper().writeValueAsString(this);
				} catch (JsonProcessingException ignore) {
					return "null";
				}
			}

			/**
			 * @return 特店交易編號
			 */
			public String getMerchantTradeNo() {
				return merchantTradeNo;
			}

			/**
			 * @param merchantTradeNo 特店交易編號
			 */
			public void setMerchantTradeNo(String merchantTradeNo) {
				this.merchantTradeNo = merchantTradeNo;
			}

			/**
			 * @return 綠界交易編號
			 */
			public String getTradeNo() {
				return tradeNo;
			}

			/**
			 * 請保存綠界的交易編號與特店交易編號[MerchantTradeNo]的關聯。
			 *
			 * @param tradeNo 綠界交易編號
			 */
			public void setTradeNo(String tradeNo) {
				this.tradeNo = tradeNo;
			}

			/**
			 * @return 付款時間
			 */
			public String getPaymentDate() {
				return paymentDate;
			}

			/**
			 * yyyy/MM/dd HH:mm:ss
			 *
			 * @param paymentDate 付款時間
			 */
			public void setPaymentDate(String paymentDate) {
				this.paymentDate = paymentDate;
			}

			/**
			 * @return 交易金額
			 */
			public Integer getTradeAmt() {
				return tradeAmt;
			}

			/**
			 * @param tradeAmt 交易金額
			 */
			public void setTradeAmt(Integer tradeAmt) {
				this.tradeAmt = tradeAmt;
			}

			/**
			 * @return 特店選擇的付款方式
			 */
			public String getPaymentType() {
				return paymentType;
			}

			/**
			 * @param paymentType 特店選擇的付款方式
			 */
			public void setPaymentType(String paymentType) {
				this.paymentType = paymentType;
			}

			/**
			 * @return 訂單成立時間
			 */
			public String getTradeDate() {
				return tradeDate;
			}

			/**
			 * yyyy/MM/dd HH:mm:ss
			 *
			 * @param tradeDate 訂單成立時間
			 */
			public void setTradeDate(String tradeDate) {
				this.tradeDate = tradeDate;
			}

			/**
			 * @return 手續費
			 */
			public Float getChargeFee() {
				return chargeFee;
			}

			/**
			 * @param chargeFee 手續費
			 */
			public void setChargeFee(Float chargeFee) {
				this.chargeFee = chargeFee;
			}

			/**
			 * @return 交易狀態
			 */
			public String getTradeStatus() {
				return tradeStatus;
			}

			/**
			 * 回傳值：若為 0 時，代表交易訂單成立未付款 若為 1 時，代表交易訂單成立已付款。
			 *
			 * @param tradeStatus 交易狀態
			 */
			public void setTradeStatus(String tradeStatus) {
				this.tradeStatus = tradeStatus;
			}
		}

		/**
		 * CVS 资讯
		 *
		 * @author m@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class CVSInfo {

			@JsonProperty("PayFrom")
			private String payFrom;

			@JsonProperty("PaymentNo")
			private String paymentNo;

			@JsonProperty("PaymentURL")
			private String paymentUrl;

			/**
			 * 默認構造器
			 */
			public CVSInfo() {
			}

			@Override
			public String toString() {
				try {
					return new JsonMapper().writeValueAsString(this);
				} catch (JsonProcessingException ignore) {
					return "null";
				}
			}

			/**
			 * @return 繳費超商
			 */
			public String getPayFrom() {
				return payFrom;
			}

			/**
			 * <ul>
			 * <li>family：全家</li>
			 * <li>hilife：萊爾富</li>
			 * <li>okmart：OK 超商</li>
			 * <li>ibon：7-11</li>
			 * </ul>
			 *
			 * @param payFrom 繳費超商
			 */
			public void setPayFrom(String payFrom) {
				this.payFrom = payFrom;
			}

			/**
			 * @return 繳費代碼
			 */
			public String getPaymentNo() {
				return paymentNo;
			}

			/**
			 * @param paymentNo 繳費代碼
			 */
			public void setPaymentNo(String paymentNo) {
				this.paymentNo = paymentNo;
			}

			/**
			 * @return 繳費連結
			 */
			public String getPaymentUrl() {
				return paymentUrl;
			}

			/**
			 * @param paymentUrl 繳費連結
			 */
			public void setPaymentUrl(String paymentUrl) {
				this.paymentUrl = paymentUrl;
			}
		}

		/**
		 * Barcode 资讯
		 *
		 * @author m@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class BarcodeInfo {

			@JsonProperty("PayFrom")
			private String payFrom;

			/**
			 * 默認構造器
			 */
			public BarcodeInfo() {
			}

			@Override
			public String toString() {
				try {
					return new JsonMapper().writeValueAsString(this);
				} catch (JsonProcessingException ignore) {
					return "null";
				}
			}

			/**
			 * @return 繳費超商
			 */
			public String getPayFrom() {
				return payFrom;
			}

			/**
			 * family：全家 hilife：萊爾富 okmart：OK 超商 ibon：7-11
			 *
			 * @param payFrom 繳費超商
			 */
			public void setPayFrom(String payFrom) {
				this.payFrom = payFrom;
			}
		}

		/**
		 * ATM 资讯
		 *
		 * @author m@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class ATMInfo {

			@JsonProperty("ATMAccBank")
			private String atmAccBank;

			@JsonProperty("ATMAccNo")
			private String atmAccNo;

			/**
			 * 默認構造器
			 */
			public ATMInfo() {
			}

			@Override
			public String toString() {
				try {
					return new JsonMapper().writeValueAsString(this);
				} catch (JsonProcessingException ignore) {
					return "null";
				}
			}

			/**
			 * @return 付款人銀行代碼
			 */
			public String getATMAccBank() {
				return atmAccBank;
			}

			/**
			 * @param atmAccBank 付款人銀行代碼
			 */
			public void setATMAccBank(String atmAccBank) {
				this.atmAccBank = atmAccBank;
			}

			/**
			 * @return 付款人銀行帳號後五碼
			 */
			public String getATMAccNo() {
				return atmAccNo;
			}

			/**
			 * @param atmAccNo 付款人銀行帳號後五碼
			 */
			public void setATMAccNo(String atmAccNo) {
				this.atmAccNo = atmAccNo;
			}
		}

		/**
		 * 授权资讯
		 *
		 * @author m@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class CardInfo {

			@JsonProperty("AuthCode")
			private String authCode;

			@JsonProperty("Gwsr")
			private Integer gwsr;

			@JsonProperty("ProcessDate")
			private String processDate;

			@JsonProperty("Amount")
			private Integer amount;

			@JsonProperty("Eci")
			private Integer eci;

			@JsonProperty("Card6No")
			private String card6No;

			@JsonProperty("Card4No")
			private String card4No;

			@JsonProperty("Stage")
			private Integer stage;

			@JsonProperty("Stast")
			private Integer stast;

			@JsonProperty("Staed")
			private Integer staed;

			@JsonProperty("RedDan")
			private Integer redDan;

			@JsonProperty("RedDeAmt")
			private Integer redDeAmt;

			@JsonProperty("RedOkAmt")
			private Integer redOkAmt;

			@JsonProperty("RedYet")
			private Integer redYet;

			@JsonProperty("PeriodType")
			private String periodType;

			@JsonProperty("Frequency")
			private Short frequency;

			@JsonProperty("ExecTimes")
			private Short execTimes;

			@JsonProperty("PeriodAmount")
			private Integer periodAmount;

			@JsonProperty("TotalSuccessTimes")
			private Integer totalSuccessTimes;

			@JsonProperty("TotalSuccessAmount")
			private Long totalSuccessAmount;

			/**
			 * 默認構造器
			 */
			public CardInfo() {
			}

			@Override
			public String toString() {
				try {
					return new JsonMapper().writeValueAsString(this);
				} catch (JsonProcessingException ignore) {
					return "null";
				}
			}

			/**
			 * @return 銀行授權碼
			 */
			public String getAuthCode() {
				return authCode;
			}

			/**
			 * @param authCode 銀行授權碼
			 */
			public void setAuthCode(String authCode) {
				this.authCode = authCode;
			}

			/**
			 * @return 授權交易單號
			 */
			public Integer getGwsr() {
				return gwsr;
			}

			/**
			 * @param gwsr 授權交易單號
			 */
			public void setGwsr(Integer gwsr) {
				this.gwsr = gwsr;
			}

			/**
			 * @return 交易時間
			 */
			public String getProcessDate() {
				return processDate;
			}

			/**
			 * yyyy/MM/dd HH:mm:ss
			 *
			 * @param processDate 交易時間
			 */
			public void setProcessDate(String processDate) {
				this.processDate = processDate;
			}

			/**
			 * @return 金額
			 */
			public Integer getAmount() {
				return amount;
			}

			/**
			 * @param amount 金額
			 */
			public void setAmount(Integer amount) {
				this.amount = amount;
			}

			/**
			 * @return 3D(VBV) 回傳值
			 */
			public Integer getEci() {
				return eci;
			}

			/**
			 * Eci=5,6,2,1 代表該筆交易為 3D 交易
			 *
			 * @param eci 3D(VBV) 回傳值
			 */
			public void setEci(Integer eci) {
				this.eci = eci;
			}

			/**
			 * @return 信用卡卡號前六碼
			 */
			public String getCard6No() {
				return card6No;
			}

			/**
			 * @param card6No 信用卡卡號前六碼
			 */
			public void setCard6No(String card6No) {
				this.card6No = card6No;
			}

			/**
			 * @return 信用卡卡號末四碼
			 */
			public String getCard4No() {
				return card4No;
			}

			/**
			 * @param card4No 信用卡卡號末四碼
			 */
			public void setCard4No(String card4No) {
				this.card4No = card4No;
			}

			/**
			 * @return 分期期數
			 */
			public Integer getStage() {
				return stage;
			}

			/**
			 * 分期付款時回傳
			 *
			 * @param stage 分期期數
			 */
			public void setStage(Integer stage) {
				this.stage = stage;
			}

			/**
			 * @return 首期金額
			 */
			public Integer getStast() {
				return stast;
			}

			/**
			 * 分期付款時回傳
			 *
			 * @param stast 首期金額
			 */
			public void setStast(Integer stast) {
				this.stast = stast;
			}

			/**
			 * @return 各期金額
			 */
			public Integer getStaed() {
				return staed;
			}

			/**
			 * 分期付款時回傳
			 *
			 * @param staed 各期金額
			 */
			public void setStaed(Integer staed) {
				this.staed = staed;
			}

			/**
			 * @return 紅利扣點
			 */
			public Integer getRedDan() {
				return redDan;
			}

			/**
			 * 紅利折扺時回傳
			 *
			 * @param redDan 紅利扣點
			 */
			public void setRedDan(Integer redDan) {
				this.redDan = redDan;
			}

			/**
			 * @return 紅利折抵金額
			 */
			public Integer getRedDeAmt() {
				return redDeAmt;
			}

			/**
			 * 紅利折扺時回傳
			 *
			 * @param redDeAmt 紅利折抵金額
			 */
			public void setRedDeAmt(Integer redDeAmt) {
				this.redDeAmt = redDeAmt;
			}

			/**
			 * @return 實際扣款金額
			 */
			public Integer getRedOkAmt() {
				return redOkAmt;
			}

			/**
			 * 紅利折扺時回傳
			 *
			 * @param redOkAmt 實際扣款金額
			 */
			public void setRedOkAmt(Integer redOkAmt) {
				this.redOkAmt = redOkAmt;
			}

			/**
			 * @return 紅利剩餘點數
			 */
			public Integer getRedYet() {
				return redYet;
			}

			/**
			 * 紅利折扺時回傳
			 *
			 * @param redYet 紅利剩餘點數
			 */
			public void setRedYet(Integer redYet) {
				this.redYet = redYet;
			}

			/**
			 * @return 週期種類
			 */
			public String getPeriodType() {
				return periodType;
			}

			/**
			 * 定期定額時才會回傳
			 *
			 * @param periodType 週期種類
			 */
			public void setPeriodType(String periodType) {
				this.periodType = periodType;
			}

			/**
			 * @return 執行頻率
			 */
			public Short getFrequency() {
				return frequency;
			}

			/**
			 * 定期定額時才會回傳
			 *
			 * @param frequency 執行頻率
			 */
			public void setFrequency(Short frequency) {
				this.frequency = frequency;
			}

			/**
			 * @return 執行次數
			 */
			public Short getExecTimes() {
				return execTimes;
			}

			/**
			 * 定期定額時才會回傳
			 *
			 * @param execTimes 執行次數
			 */
			public void setExecTimes(Short execTimes) {
				this.execTimes = execTimes;
			}

			/**
			 * @return 訂單建立時的每次要授權金額
			 */
			public Integer getPeriodAmount() {
				return periodAmount;
			}

			/**
			 * 定期定額時才會回傳
			 *
			 * @param periodAmount 訂單建立時的每次要授權金額
			 */
			public void setPeriodAmount(Integer periodAmount) {
				this.periodAmount = periodAmount;
			}

			/**
			 * @return 目前已成功授权的次数
			 */
			public Integer getTotalSuccessTimes() {
				return totalSuccessTimes;
			}

			/**
			 * 定期定額時才會回傳
			 *
			 * @param totalSuccessTimes 目前已成功授权的次数
			 */
			public void setTotalSuccessTimes(Integer totalSuccessTimes) {
				this.totalSuccessTimes = totalSuccessTimes;
			}

			/**
			 * @return 目前已成功授權的金額合計
			 */
			public Long getTotalSuccessAmount() {
				return totalSuccessAmount;
			}

			/**
			 * 定期定額時才會回傳
			 *
			 * @param totalSuccessAmount 目前已成功授權的金額合計
			 */
			public void setTotalSuccessAmount(Long totalSuccessAmount) {
				this.totalSuccessAmount = totalSuccessAmount;
			}
		}
	}
}
