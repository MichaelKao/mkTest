package tw.com.ecpay.ecpg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 綠界回傳參數說明：回傳 3D URL 或授權結果
 *
 * @author m@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplyPayToken {

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
	 * 默认构造器
	 */
	public ReplyPayToken() {
	}

	/**
	 * @return 特店编号
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId 特店编号
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return 回传数据
	 */
	public RpHeader getRpHeader() {
		return rpHeader;
	}

	/**
	 * @param rpHeader 回传数据
	 */
	public void setRpHeader(RpHeader rpHeader) {
		this.rpHeader = rpHeader;
	}

	/**
	 * @return 回传代码
	 */
	public Integer getTransCode() {
		return transCode;
	}

	/**
	 * @param transCode 1 代表传输数据(merchantID、rqHeader、data)接收成功，其余均为失败。
	 */
	public void setTransCode(Integer transCode) {
		this.transCode = transCode;
	}

	/**
	 * @return 回传讯息
	 */
	public String getTransMsg() {
		return transMsg;
	}

	/**
	 * @param transMsg 回传讯息
	 */
	public void setTransMsg(String transMsg) {
		this.transMsg = transMsg;
	}

	/**
	 * @return 加密数据
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data 加密过 JSON 格式的数据。
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 回传数据
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

		/**
		 * @return 回传时间
		 */
		public Long getTimestamp() {
			return timestamp;
		}

		/**
		 * @param timestamp 时间戳 Unix timestamp。
		 */
		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}
	}
	
	/**
	 * 
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
		
		@JsonProperty("ThreeDInfo")
		private ThreeDInfo threeDInfo;
		
		@JsonProperty("CardInfo")
		private CardInfo cardInfo;
		
		@JsonProperty("ATMInfo")
		private ATMInfo atmInfo;
		
		@JsonProperty("CVSInfo")
		private CVSInfo cvsInfo;
		
		@JsonProperty("BarcodeInfo")
		private BarcodeInfo barcodeInfo;
		
		@JsonProperty("CustomField")
		private String CustomField;

		/**
		 * 默認構造器
		 */
		public Data() {
		}

		/**
		 * @return 交易狀態
		 */
		public Integer getRtnCode() {
			return rtnCode;
		}

		/**
		 * 1 代表 API 執行成功，其餘代碼均為失敗。
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
		 * @return 3D 驗證資訊
		 */
		public ThreeDInfo getThreeDInfo() {
			return threeDInfo;
		}

		/**
		 * @param threeDInfo 3D 驗證資訊
		 */
		public void setThreeDInfo(ThreeDInfo threeDInfo) {
			this.threeDInfo = threeDInfo;
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
		 * @return ATM 資訊
		 */
		public ATMInfo getAtmInfo() {
			return atmInfo;
		}

		/**
		 * @param atmInfo ATM 資訊
		 */
		public void setAtmInfo(ATMInfo atmInfo) {
			this.atmInfo = atmInfo;
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
		 * @return 廠商自訂欄位
		 */
		public String getCustomField() {
			return CustomField;
		}

		/**
		 * @param CustomField 廠商自訂欄位
		 */
		public void setCustomField(String CustomField) {
			this.CustomField = CustomField;
		}
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class OrderInfo {
			
			@JsonProperty("MerchantTradeNo")
			private String merchantTradeNo;
			
			@JsonProperty("TradeNo")
			private String tradeNo;
			
			@JsonProperty("TradeAmt")
			private Integer tradeAmt;
			
			@JsonProperty("TradeDate")
			private String tradeDate;
			
			@JsonProperty("PaymentType")
			private String paymentType;
			
			@JsonProperty("PaymentDate")
			private String paymentDate;
			
			@JsonProperty("ChargeFee")
			private Integer chargeFee;
			
			@JsonProperty("TradeStatus")
			private String tradeStatus;

			/**
			 * 默認構造器
			 */
			public OrderInfo() {
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
			 * @param tradeNo 綠界交易編號
			 */
			public void setTradeNo(String tradeNo) {
				this.tradeNo = tradeNo;
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
			 * @return 訂單成立時間
			 */
			public String getTradeDate() {
				return tradeDate;
			}

			/**
			 * yyyy/MM/dd HH:mm:ss
			 * @param tradeDate 訂單成立時間
			 */
			public void setTradeDate(String tradeDate) {
				this.tradeDate = tradeDate;
			}

			/**
			 * @return 付款方式
			 */
			public String getPaymentType() {
				return paymentType;
			}

			/**
			 * @param paymentType 付款方式
			 */
			public void setPaymentType(String paymentType) {
				this.paymentType = paymentType;
			}

			/**
			 * @return 付款時間
			 */
			public String getPaymentDate() {
				return paymentDate;
			}

			/**
			 * yyyy/MM/dd HH:mm:ss
			 * @param paymentDate 付款時間
			 */
			public void setPaymentDate(String paymentDate) {
				this.paymentDate = paymentDate;
			}

			/**
			 * @return 手續費
			 */
			public Integer getChargeFee() {
				return chargeFee;
			}

			/**
			 * @param chargeFee 手續費
			 */
			public void setChargeFee(Integer chargeFee) {
				this.chargeFee = chargeFee;
			}

			/**
			 * @return 交易狀態
			 */
			public String getTradeStatus() {
				return tradeStatus;
			}

			/**
			 * @param tradeStatus 交易狀態
			 */
			public void setTradeStatus(String tradeStatus) {
				this.tradeStatus = tradeStatus;
			}
		}
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class ThreeDInfo {
			
			@JsonProperty("ThreeDURL")
			private String threeDURL;

			/**
			 * 默認構造器
			 */
			public ThreeDInfo() {
			}

			/**
			 * @return 3D 驗證 URL
			 */
			public String getThreeDURL() {
				return threeDURL;
			}

			/**
			 * 3D 驗證連結，請勿使用 iframe 方式開啟。
			 * @param threeDURL 3D 驗證 URL
			 */
			public void setThreeDURL(String threeDURL) {
				this.threeDURL = threeDURL;
			}
		}
		
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
			
			@JsonProperty("Stage")
			private Short stage;
			
			@JsonProperty("Stast")
			private Integer stast;
			
			@JsonProperty("Staed")
			private Integer staed;
			
			@JsonProperty("Eci")
			private Short eci;
			
			@JsonProperty("Card6No")
			private String card6No;
			
			@JsonProperty("Card4No")
			private String card4No;
			
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
			 * @return 分期期數
			 */
			public Short getStage() {
				return stage;
			}

			/**
			 * @param stage 分期期數
			 */
			public void setStage(Short stage) {
				this.stage = stage;
			}

			/**
			 * @return 首期金額
			 */
			public Integer getStast() {
				return stast;
			}

			/**
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
			 * @param staed 各期金額
			 */
			public void setStaed(Integer staed) {
				this.staed = staed;
			}

			/**
			 * @return 3D(VBV) 回傳值
			 */
			public Short getEci() {
				return eci;
			}

			/**
			 * @param eci 3D(VBV) 回傳值
			 */
			public void setEci(Short eci) {
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
			 * @return 紅利扣點
			 */
			public Integer getRedDan() {
				return redDan;
			}

			/**
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
			 * @param periodAmount 訂單建立時的每次要授權金額
			 */
			public void setPeriodAmount(Integer periodAmount) {
				this.periodAmount = periodAmount;
			}

			/**
			 * @return 目前已成功授權的次數
			 */
			public Integer getTotalSuccessTimes() {
				return totalSuccessTimes;
			}

			/**
			 * @param totalSuccessTimes 目前已成功授權的次數
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
			 * @param totalSuccessAmount 目前已成功授權的金額合計
			 */
			public void setTotalSuccessAmount(Long totalSuccessAmount) {
				this.totalSuccessAmount = totalSuccessAmount;
			}
		}
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class ATMInfo {
			
			@JsonProperty("BankCode")
			private String bankCode;
			
			@JsonProperty("vAccount")
			private String vAccount;
			
			@JsonProperty("ExpireDate")
			private String expireDate;

			/**
			 * 默認構造器
			 */
			public ATMInfo() {
			}

			/**
			 * @return 繳費銀行代碼
			 */
			public String getBankCode() {
				return bankCode;
			}

			/**
			 * @param bankCode 繳費銀行代碼
			 */
			public void setBankCode(String bankCode) {
				this.bankCode = bankCode;
			}

			/**
			 * @return 繳費虛擬帳號
			 */
			public String getvAccount() {
				return vAccount;
			}

			/**
			 * @param vAccount 繳費虛擬帳號
			 */
			public void setvAccount(String vAccount) {
				this.vAccount = vAccount;
			}

			/**
			 * @return 繳費期限
			 */
			public String getExpireDate() {
				return expireDate;
			}

			/**
			 * 格式為 yyyy/MM/dd
			 * @param expireDate 繳費期限
			 */
			public void setExpireDate(String expireDate) {
				this.expireDate = expireDate;
			}
		}
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class CVSInfo {
			
			@JsonProperty("PaymentNo")
			private String paymentNo;
			
			@JsonProperty("ExpireDate")
			private String expireDate;
			
			@JsonProperty("PaymentURL")
			private String paymentURL;

			/**
			 * 默認構造器
			 */
			public CVSInfo() {
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
			 * @return 繳費期限
			 */
			public String getExpireDate() {
				return expireDate;
			}

			/**
			 * 格式為 yyyy/MM/dd HH:mm:ss
			 * @param expireDate 繳費期限
			 */
			public void setExpireDate(String expireDate) {
				this.expireDate = expireDate;
			}

			/**
			 * @return 繳費連結
			 */
			public String getPaymentURL() {
				return paymentURL;
			}

			/**
			 * 由綠界科技提供手機上顯示的三段式繳費條碼網頁
			 * @param paymentURL 繳費連結
			 */
			public void setPaymentURL(String paymentURL) {
				this.paymentURL = paymentURL;
			}			
		}
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class BarcodeInfo {
			
			@JsonProperty("ExpireDate")
			private String expireDate;
			
			@JsonProperty("Barcode1")
			private String barcode1;
			
			@JsonProperty("Barcode2")
			private String barcode2;
			
			@JsonProperty("Barcode3")
			private String barcode3;

			/**
			 * 默認構造器
			 */
			public BarcodeInfo() {
			}

			/**
			 * @return 繳費期限
			 */
			public String getExpireDate() {
				return expireDate;
			}

			/**
			 * 格式為 yyyy/MM/dd HH:mm:ss
			 * @param expireDate 繳費期限	
			 */
			public void setExpireDate(String expireDate) {
				this.expireDate = expireDate;
			}

			/**
			 * @return 條碼第一段號碼
			 */
			public String getBarcode1() {
				return barcode1;
			}

			/**
			 * 格式為 9 碼數字
			 * @param barcode1 條碼第一段號碼
			 */
			public void setBarcode1(String barcode1) {
				this.barcode1 = barcode1;
			}

			/**
			 * @return 條碼第二段號碼
			 */
			public String getBarcode2() {
				return barcode2;
			}

			/**
			 * 格式為 16 碼數字
			 * @param barcode2 條碼第二段號碼
			 */
			public void setBarcode2(String barcode2) {
				this.barcode2 = barcode2;
			}

			/**
			 * @return 條碼第三段號碼
			 */
			public String getBarcode3() {
				return barcode3;
			}

			/**
			 * 格式為 15 碼數字
			 * @param barcode3 條碼第三段號碼
			 */
			public void setBarcode3(String barcode3) {
				this.barcode3 = barcode3;
			}
		}
	}
}
