package tw.com.ecpay.ecpg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 向绿界取得厂商验证码
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenRequest {

	@JsonProperty("MerchantID")
	@NotBlank(message = "inpay2.token.MerchantID.NotBlank")
	@Size(max = 10)
	private String merchantId;

	@JsonProperty("RqHeader")
	@NotNull(message = "inpay2.token.RqHeader.NotNull")
	private RqHeader rqHeader;

	@JsonProperty("Data")
	@NotBlank(message = "inpay2.token.Data.NotBlank")
	private String data;

	/**
	 * 默认构造器
	 */
	public TokenRequest() {
	}

	/**
	 * @return 特店编号
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * 若为平台商时，参数请带平台商所绑的特店编号。
	 *
	 * @param merchantId 特店编号
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return 传输数据
	 */
	public RqHeader getRqHeader() {
		return rqHeader;
	}

	/**
	 * @param rqHeader 传输数据
	 */
	public void setRqHeader(RqHeader rqHeader) {
		this.rqHeader = rqHeader;
	}

	/**
	 * @return 加密数据
	 */
	public String getData() {
		return data;
	}

	/**
	 * 加密过 JSON 格式的数据。
	 *
	 * @param data 加密数据
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 传输数据
	 *
	 * @author p@musemodel.tw
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class RqHeader {

		@JsonProperty("Timestamp")
		@NotNull(message = "inpay2.token.RqHeader.Timestamp.NotNull")
		private Long timestamp;

		@JsonProperty("Revision")
		@NotBlank(message = "inpay2.token.RqHeader.Revision.NotBlank")
		@Size(max = 10)
		private String revision;

		/**
		 * 默认构造器
		 */
		public RqHeader() {
		}

		/**
		 * @return 传输时间
		 */
		public Long getTimestamp() {
			return timestamp;
		}

		/**
		 * 时间戳 Unix timestamp；注意事项：若时间戳跟绿界服务器接收到时间超过 10 分钟时，交易会失败无法进行。
		 *
		 * @param timestamp 传输时间
		 */
		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}

		/**
		 * @return 串接文件版号
		 */
		public String getRevision() {
			return revision;
		}

		/**
		 * 请参考文件封面的文件版号，例：1.0.0。
		 *
		 * @param revision 串接文件版号
		 */
		public void setRevision(String revision) {
			this.revision = revision;
		}
	}

	/**
	 * 取得厂商验证码(服务器)
	 *
	 * <h3>应用场景</h3>
	 * <p>
	 * 在使用绿界站内付 2.0
	 * 金流服务之前，厂商服务器必须先向绿界服务器取得一组厂商验证码(token)。厂商服务器得到验证码后，必须将验证码传给厂商网站网页做产生站内付
	 * 2.0 金流画面功能之用。 </p>
	 * <h3>介接路径</h3>
	 * <ul>
	 * <li>正式环境：https://ecpg.ecpay.com.tw/Merchant/GetTokenbyTrade</li>
	 * <li>测试环境：https://ecpg-stage.ecpay.com.tw/Merchant/GetTokenbyTrade</li>
	 * </ul>
	 * <h3>厂商传入参数(JSON 格式)</h3>
	 * <ul>
	 * <li>Content Type：application/json</li>
	 * <li>HTTP Method：POST</li>
	 * </ul>
	 *
	 * @author p@musemodel.tw
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data {

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("PlatformID")
		@Size(max = 10)
		private String platformID;

		@JsonProperty("MerchantID")
		@NotBlank(message = "inpay2.token.Data.MerchantID.NotBlank")
		@Size(max = 10)
		private String merchantId;

		@JsonProperty("RememberCard")
		@NotNull(message = "inpay2.token.Data.RememberCard.NotNull")
		private Short rememberCard;

		@JsonProperty("PaymentUIType")
		@NotNull(message = "inpay2.token.Data.PaymentUIType.NotNull")
		private Short paymentUIType;

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("ChoosePaymentList")
		@Size(max = 30)
		private String choosePaymentList;

		@JsonProperty("OrderInfo")
		@NotNull(message = "inpay2.token.Data.OrderInfo.NotNull")
		private OrderInfo orderInfo;

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("CardInfo")
		private CardInfo cardInfo;

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("ATMInfo")
		private ATMInfo atmInfo;

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("CVSInfo")
		private CVSInfo cvsInfo;

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("BarcodeInfo")
		private BarcodeInfo barcodeInfo;

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("ConsumerInfo")
		private ConsumerInfo consumerInfo;

		@JsonInclude(Include.NON_NULL)
		@JsonProperty("CustomField")
		@Size(max = 200)
		private String customField;

		/**
		 * 默认构造器
		 */
		public Data() {
		}

		/**
		 * 构造器。
		 *
		 * @param merchantId 特店编号
		 * @param rememberCard 是否使用记忆卡号功能
		 * @param paymentUIType 画面的呈现方式
		 */
		public Data(String merchantId, Short rememberCard, Short paymentUIType) {
			this.merchantId = merchantId;
			this.rememberCard = rememberCard;
			this.paymentUIType = paymentUIType;
		}

		/**
		 * @return 特约合作平台商代号
		 */
		public String getPlatformID() {
			return platformID;
		}

		/**
		 * 为专案合作的平台商使用。一般特店或平台商本身介接，则参数请带空值。若为专案合作平台商的特店使用时，则参数请带平台商所绑的特店编号
		 * MerchantID。
		 *
		 * @param platformID 特约合作平台商代号
		 */
		public void setPlatformID(String platformID) {
			this.platformID = platformID;
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
		 * @return 是否使用记忆卡号功能
		 */
		public Short getRememberCard() {
			return rememberCard;
		}

		/**
		 * <ul>
		 * <li>0：否</li>
		 * <li>1：是</li>
		 * </ul>
		 *
		 * @param rememberCard 是否使用记忆卡号功能
		 */
		public void setRememberCard(Short rememberCard) {
			this.rememberCard = rememberCard;
		}

		/**
		 * @return 画面的呈现方式
		 */
		public Short getPaymentUIType() {
			return paymentUIType;
		}

		/**
		 * <ul>
		 * <li>0：信用卡定期定额</li>
		 * <li>2：付款选择清单页</li>
		 * </ul>
		 *
		 * @param paymentUIType 画面的呈现方式
		 */
		public void setPaymentUIType(Short paymentUIType) {
			this.paymentUIType = paymentUIType;
		}

		/**
		 * @return 欲使用的付款方式
		 */
		public String getChoosePaymentList() {
			return choosePaymentList;
		}

		/**
		 * paymentUIType 如选择 2，则必填。
		 * <ul>
		 * <li>0：全部付款方式</li>
		 * <li>1：信用卡一次付清</li>
		 * <li>2：信用卡分期付款</li>
		 * <li>3：ATM</li>
		 * <li>4：超商代码</li>
		 * <li>5：超商条码</li>
		 * </ul>
		 *
		 * <p>
		 * 可多选，例：1,2,3。 </p>
		 *
		 * @param choosePaymentList 欲使用的付款方式
		 */
		public void setChoosePaymentList(String choosePaymentList) {
			this.choosePaymentList = choosePaymentList;
		}

		/**
		 * @return 订单资讯
		 */
		public OrderInfo getOrderInfo() {
			return orderInfo;
		}

		/**
		 * @param orderInfo 订单资讯
		 */
		public void setOrderInfo(OrderInfo orderInfo) {
			this.orderInfo = orderInfo;
		}

		/**
		 * @return 信用卡资讯
		 */
		public CardInfo getCardInfo() {
			return cardInfo;
		}

		/**
		 * 以下情况为必填：
		 * <ol>
		 * <li>paymentUIType 选择 0 或 1</li>
		 * <li>paymentUIType 选择 2，且 choosePaymentList 选择 0，1 或 2</li>
		 * </ol>
		 *
		 * @param cardInfo 信用卡资讯
		 */
		public void setCardInfo(CardInfo cardInfo) {
			this.cardInfo = cardInfo;
		}

		/**
		 * @return ATM 资讯
		 */
		public ATMInfo getAtmInfo() {
			return atmInfo;
		}

		/**
		 * choosePaymentList 如选择 0 或 3 则必填。
		 *
		 * @param atmInfo ATM 资讯
		 */
		public void setAtmInfo(ATMInfo atmInfo) {
			this.atmInfo = atmInfo;
		}

		/**
		 * @return 超商代码资讯
		 */
		public CVSInfo getCvsInfo() {
			return cvsInfo;
		}

		/**
		 * choosePaymentList 如选择 0 或 4 则必填。
		 *
		 * @param cvsInfo 超商代码资讯
		 */
		public void setCvsInfo(CVSInfo cvsInfo) {
			this.cvsInfo = cvsInfo;
		}

		/**
		 * @return 超商条码资讯
		 */
		public BarcodeInfo getBarcodeInfo() {
			return barcodeInfo;
		}

		/**
		 * choosePaymentList 如选择 0 或 5 则必填。
		 *
		 * @param barcodeInfo 超商条码资讯
		 */
		public void setBarcodeInfo(BarcodeInfo barcodeInfo) {
			this.barcodeInfo = barcodeInfo;
		}

		/**
		 * @return 消费者资讯
		 */
		public ConsumerInfo getConsumerInfo() {
			return consumerInfo;
		}

		/**
		 * @param consumerInfo 消费者资讯
		 */
		public void setConsumerInfo(ConsumerInfo consumerInfo) {
			this.consumerInfo = consumerInfo;
		}

		/**
		 * @return 厂商自订栏位
		 */
		public String getCustomField() {
			return customField;
		}

		/**
		 * 提供厂商使用客制化栏位。
		 *
		 * @param customField 厂商自订栏位
		 */
		public void setCustomField(String customField) {
			this.customField = customField;
		}

		/**
		 * 订单资讯
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class OrderInfo {

			@JsonProperty("MerchantTradeDate")
			@NotBlank(message = "inpay2.token.Data.OrderInfo.MerchantTradeDate.NotBlank")
			@Size(max = 20)
			private String merchantTradeDate;

			@JsonProperty("MerchantTradeNo")
			@NotBlank(message = "inpay2.token.Data.OrderInfo.MerchantTradeNo.NotBlank")
			@Size(max = 20)
			private String merchantTradeNo;

			@JsonProperty("TotalAmount")
			@NotNull(message = "inpay2.token.Data.OrderInfo.TotalAmount.NotNull")
			private Integer totalAmount;

			@JsonProperty("ReturnURL")
			@NotBlank(message = "inpay2.token.Data.OrderInfo.ReturnURL.NotBlank")
			@Size(max = 200)
			private String returnURL;

			@JsonProperty("TradeDesc")
			@NotBlank(message = "inpay2.token.Data.OrderInfo.TradeDesc.NotBlank")
			@Size(max = 200)
			private String tradeDesc;

			@JsonProperty("ItemName")
			@NotBlank(message = "inpay2.token.Data.OrderInfo.ItemName.NotBlank")
			@Size(max = 400)
			private String itemName;

			/**
			 * 默认构造器
			 */
			public OrderInfo() {
			}

			/**
			 * 构造器。
			 *
			 * @param merchantTradeDate 厂商交易时间
			 * @param merchantTradeNo 特店交易编号
			 * @param totalAmount 交易金额
			 * @param returnURL 付款回传结果网址
			 * @param tradeDesc 交易描述
			 * @param itemName 商品名称
			 */
			public OrderInfo(String merchantTradeDate, String merchantTradeNo, Integer totalAmount, String returnURL, String tradeDesc, String itemName) {
				this.merchantTradeDate = merchantTradeDate;
				this.merchantTradeNo = merchantTradeNo;
				this.totalAmount = totalAmount;
				this.returnURL = returnURL;
				this.tradeDesc = tradeDesc;
				this.itemName = itemName;
			}

			/**
			 * @return 厂商交易时间
			 */
			public String getMerchantTradeDate() {
				return merchantTradeDate;
			}

			/**
			 * yyyy/MM/dd HH:mm:ss
			 *
			 * @param merchantTradeDate 厂商交易时间
			 */
			public void setMerchantTradeDate(String merchantTradeDate) {
				this.merchantTradeDate = merchantTradeDate;
			}

			/**
			 * @return 特店交易编号
			 */
			public String getMerchantTradeNo() {
				return merchantTradeNo;
			}

			/**
			 * 均为唯一值，不可重复使用；英数字大小写混合。
			 *
			 * @param merchantTradeNo 特店交易编号
			 */
			public void setMerchantTradeNo(String merchantTradeNo) {
				this.merchantTradeNo = merchantTradeNo;
			}

			/**
			 * @return 交易金额
			 */
			public Integer getTotalAmount() {
				return totalAmount;
			}

			/**
			 * 请带整数，不可有小数点。仅限新台币。各付款金额的限制，请参考：https://www.ecpay.com.tw/CascadeFAQ/CascadeFAQ_Qa?nID=3605。
			 *
			 * @param totalAmount 交易金额
			 */
			public void setTotalAmount(Integer totalAmount) {
				this.totalAmount = totalAmount;
			}

			/**
			 * @return 付款回传结果网址
			 */
			public String getReturnURL() {
				return returnURL;
			}

			/**
			 * <p>
			 * 当消费者付款完成后，绿界会将付款结果参数以幕后(server
			 * POST)回传到该网址；详细说明请参考付款结果通知。</p>
			 *
			 * <h3>注意事项</h3>
			 * <ol>
			 * <li>请勿设定与 client 端接收付款结果网址 orderResultUrl
			 * 相同位置，避免程式判断错误。 </li>
			 * <li>请在收到 server 端付款结果通知后，请正确回应 1|OK 给绿界。</li>
			 * </ol>
			 *
			 * @param returnURL 付款回传结果网址
			 */
			public void setReturnURL(String returnURL) {
				this.returnURL = returnURL;
			}

			/**
			 * @return 交易描述
			 */
			public String getTradeDesc() {
				return tradeDesc;
			}

			/**
			 * @param tradeDesc 交易描述
			 */
			public void setTradeDesc(String tradeDesc) {
				this.tradeDesc = tradeDesc;
			}

			/**
			 * @return 商品名称
			 */
			public String getItemName() {
				return itemName;
			}

			/**
			 * 商品名称以 # 分开。
			 *
			 * @param itemName 商品名称
			 */
			public void setItemName(String itemName) {
				this.itemName = itemName;
			}
		}

		/**
		 * <h1>信用卡资讯</h1>
		 *
		 * <p>
		 * 以下情况为必填：
		 * <ol>
		 * <li>paymentUIType 选择 0 或 1</li>
		 * <li>paymentUIType 选择 2，且 choosePaymentList 选择 0，1 或 2</li>
		 * </ol>
		 * </p>
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class CardInfo {

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Redeem")
			private String redeem;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("PeriodAmount")
			private Short periodAmount;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("PeriodType")
			private String periodType;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Frequency")
			private Short frequency;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("ExecTimes")
			private Short execTimes;

			@JsonProperty("OrderResultURL")
			@NotBlank(message = "inpay2.token.Data.CardInfo.OrderResultURL.NotBlank")
			@Size(max = 200)
			private String orderResultUrl;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("PeriodReturnURL")
			@Size(max = 200)
			private String periodReturnUrl;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("CreditInstallment")
			private String creditInstallment;

			/**
			 * 默认构造器
			 */
			public CardInfo() {
				redeem = "0";
			}

			/**
			 * 构造器。
			 *
			 * @param orderResultUrl 3D 验证回传付款结果网址
			 */
			public CardInfo(String orderResultUrl) {
				this.orderResultUrl = orderResultUrl;
			}

			/**
			 * @return 使用信用卡红利
			 */
			public String getRedeem() {
				return redeem;
			}

			/**
			 * 默认为不使用；0：不使用，1：使用。
			 *
			 * @param redeem 使用信用卡红利
			 */
			public void setRedeem(String redeem) {
				this.redeem = redeem;
			}

			/**
			 * @return 定期定额每次授权金额
			 */
			public Short getPeriodAmount() {
				return periodAmount;
			}

			/**
			 * 当 TokenData#paymentUIType 为 0 时，此栏位必填。
			 *
			 * @param periodAmount 定期定额每次授权金额
			 */
			public void setPeriodAmount(Short periodAmount) {
				this.periodAmount = periodAmount;
			}

			/**
			 * @return 定期定额周期种类
			 */
			public String getPeriodType() {
				return periodType;
			}

			/**
			 * 当 TokenData#paymentUIType 为 0
			 * 时，此栏位必填；D：以天为周期，M：以月为周期，Y：以年为周期。
			 *
			 * @param periodType 定期定额周期种类
			 */
			public void setPeriodType(String periodType) {
				this.periodType = periodType;
			}

			/**
			 * @return 定期定额执行频率
			 */
			public Short getFrequency() {
				return frequency;
			}

			/**
			 * 当 TokenData#paymentUIType 为 0 时，此栏位必填；注意事项：至少要大于等于 1
			 * 次以上。当 periodType 设为 D 时，最多可设 365 次。当 periodType 设为 M
			 * 时，最多可设 12 次。当 periodType 设为 Y 时，最多可设 1 次。
			 *
			 * @param frequency 定期定额执行频率
			 */
			public void setFrequency(Short frequency) {
				this.frequency = frequency;
			}

			/**
			 * @return 定期定额执行次数
			 */
			public Short getExecTimes() {
				return execTimes;
			}

			/**
			 * 当 TokenData#paymentUIType 为 0 时，此栏位必填。注意事项：至少要大于 1
			 * 次以上。当 PeriodType 设为 D 时，最多可设 999 次。当 PeriodType 设为 M
			 * 时，最多可设 99 次。当 PeriodType 设为 Y 时，最多可设 9 次。
			 *
			 * @param execTimes 定期定额执行次数
			 */
			public void setExecTimes(Short execTimes) {
				this.execTimes = execTimes;
			}

			/**
			 * @return 3D 验证回传付款结果网址
			 */
			public String getOrderResultUrl() {
				return orderResultUrl;
			}

			/**
			 * 使用 3D 验证时，当消费者付款完成后，绿界会将付款结果参数以幕前(Client POST)回传到该网址。
			 *
			 * @param orderResultUrl 3D 验证回传付款结果网址
			 */
			public void setOrderResultUrl(String orderResultUrl) {
				this.orderResultUrl = orderResultUrl;
			}

			/**
			 * @return 定期定额执行结果回应网址
			 */
			public String getPeriodReturnUrl() {
				return periodReturnUrl;
			}

			/**
			 * 当 TokenData#paymentUIType 为 0
			 * 时，此栏位必填；若交易是信用卡定期定额的方式，则每次执行授权完，会将授权结果回传到这个设定的网址。
			 *
			 * @param periodReturnUrl 定期定额执行结果回应网址
			 */
			public void setPeriodReturnUrl(String periodReturnUrl) {
				this.periodReturnUrl = periodReturnUrl;
			}

			/**
			 * @return 刷卡分期期数
			 */
			public String getCreditInstallment() {
				return creditInstallment;
			}

			/**
			 * 当 Token#choosePaymentList 为 0 或
			 * Token#choosePaymentList 有选择 2
			 * 时，此栏位必填；支援多期数请以逗号分隔，例：3,6,12,18,24。
			 *
			 * @param creditInstallment 刷卡分期期数
			 */
			public void setCreditInstallment(String creditInstallment) {
				this.creditInstallment = creditInstallment;
			}
		}

		/**
		 * <h1>ATM 资讯</h1>
		 *
		 * <p>
		 * choosePaymentList 如选择 0 或 3 则必填。</p>
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class ATMInfo {

			@JsonProperty("ExpireDate")
			@NotNull(message = "inpay2.token.Data.ATMInfo.ExpireDate.NotNull")
			private Short expireDate;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("ATMBankCode")
			@Size(max = 10)
			private String atmBankCode;

			/**
			 * 默认构造器
			 */
			public ATMInfo() {
			}

			/**
			 * 构造器。
			 *
			 * @param expireDate 允许缴费有效天数
			 */
			public ATMInfo(Short expireDate) {
				this.expireDate = expireDate;
			}

			/**
			 * @return 允许缴费有效天数
			 */
			public Short getExpireDate() {
				return expireDate;
			}

			/**
			 * 以天为单位；最长 60 天，最短 1 天，默认为 3 天。
			 *
			 * @param expireDate 允许缴费有效天数
			 */
			public void setExpireDate(Short expireDate) {
				this.expireDate = expireDate;
			}

			/**
			 * @return ATM 银行代码
			 */
			public String getAtmBankCode() {
				return atmBankCode;
			}

			/**
			 * 缴费银行代码；若未传入，依系统默认银行为主。
			 *
			 * @param atmBankCode ATM 银行代码
			 */
			public void setAtmBankCode(String atmBankCode) {
				this.atmBankCode = atmBankCode;
			}
		}

		/**
		 * <h1>超商代码资讯</h1>
		 * <p>
		 * choosePaymentList 如选择 0 或 4 则必填。</p>
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class CVSInfo {

			@JsonProperty("StoreExpireDate")
			@NotNull(message = "inpay2.token.Data.CVSInfo.StoreExpireDate.NotNull")
			private Integer storeExpireDate;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("CVSCode")
			@Size(max = 10)
			private String cvsCode;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Desc_1")
			@Size(max = 20)
			private String desc1;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Desc_2")
			@Size(max = 20)
			private String desc2;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Desc_3")
			@Size(max = 20)
			private String desc3;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Desc_4")
			@Size(max = 20)
			private String desc4;

			/**
			 * 默认构造器
			 */
			public CVSInfo() {
			}

			/**
			 * 构造器。
			 *
			 * @param storeExpireDate 超商缴费截止时间
			 */
			public CVSInfo(Integer storeExpireDate) {
				this.storeExpireDate = storeExpireDate;
			}

			/**
			 * @return 超商缴费截止时间
			 */
			public Integer getStoreExpireDate() {
				return storeExpireDate;
			}

			/**
			 * <p>
			 * 以分钟为单位，若未设定此参数，默认为 10080 分钟(7 天)。</p>
			 *
			 * <p>
			 * 若需设定此参数，请于建立订单时将此参数送给绿界。提醒您，CVS 带入数值不可超过 86400
			 * 分钟，超过时一律以 86400 分钟计(60 天)，例：08/01 的 20:15 购买商品，缴费期限为
			 * 7 天，表示 08/08 的 20:15 前必须前往超商缴费。</p>
			 *
			 * @param storeExpireDate 超商缴费截止时间
			 */
			public void setStoreExpireDate(Integer storeExpireDate) {
				this.storeExpireDate = storeExpireDate;
			}

			/**
			 * @return 超商代码
			 */
			public String getCvsCode() {
				return cvsCode;
			}

			/**
			 * <ul>
			 * <li>CVS：超商代码缴款(不指定超商)</li>
			 * <li>OK：OK超商代码缴款</li>
			 * <li>FAMILY：全家超商代码缴款</li>
			 * <li>HILIFE：莱尔富超商代码缴款</li>
			 * <li>IBON：7-11 ibon 代码缴款</li>
			 * </ul>
			 *
			 * <p>
			 * 若未传入，默认为 CVS。</p>
			 *
			 * @param cvsCode 超商代码
			 */
			public void setCvsCode(String cvsCode) {
				this.cvsCode = cvsCode;
			}

			/**
			 * @return 交易描述 1
			 */
			public String getDesc1() {
				return desc1;
			}

			/**
			 * 若缴费超商为 family (全家)或 ibon (7-11)时，会显示在超商缴费平台萤幕上。
			 *
			 * @param desc1 交易描述 1
			 */
			public void setDesc1(String desc1) {
				this.desc1 = desc1;
			}

			/**
			 * @return 交易描述 2
			 */
			public String getDesc2() {
				return desc2;
			}

			/**
			 * 若缴费超商为 family (全家)或 ibon (7-11)时，会显示在超商缴费平台萤幕上。
			 *
			 * @param desc2 交易描述 2
			 */
			public void setDesc2(String desc2) {
				this.desc2 = desc2;
			}

			/**
			 * @return 交易描述 3
			 */
			public String getDesc3() {
				return desc3;
			}

			/**
			 * 若缴费超商为 family (全家)或 ibon (7-11)时，会显示在超商缴费平台萤幕上。
			 *
			 * @param desc3 交易描述 3
			 */
			public void setDesc3(String desc3) {
				this.desc3
					= desc3;
			}

			/**
			 * @return 交易描述 4
			 */
			public String getDesc4() {
				return desc4;
			}

			/**
			 * 若缴费超商为 family (全家)或 ibon (7-11)时，会显示在超商缴费平台萤幕上。
			 *
			 * @param desc4 交易描述 4
			 */
			public void setDesc4(String desc4) {
				this.desc4 = desc4;
			}
		}

		/**
		 * <h1>超商条码资讯</h1>
		 *
		 * <p>
		 * choosePaymentList 如选择 0 或 5 则必填。</p>
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class BarcodeInfo {

			@JsonProperty("StoreExpireDate")
			@NotNull(message = "inpay2.token.Data.BarcodeInfo.StoreExpireDate.NotNull")
			private Short storeExpireDate;

			/**
			 * 默认构造器
			 */
			public BarcodeInfo() {
			}

			/**
			 * 构造器。
			 *
			 * @param storeExpireDate 超商缴费截止时间
			 */
			public BarcodeInfo(Short storeExpireDate) {
				this.storeExpireDate = storeExpireDate;
			}

			/**
			 * @return 超商缴费截止时间
			 */
			public Short getStoreExpireDate() {
				return storeExpireDate;
			}

			/**
			 * 以天为单位，默认为 7 天。
			 *
			 * @param storeExpireDate 超商缴费截止时间
			 */
			public void setStoreExpireDate(Short storeExpireDate) {
				this.storeExpireDate = storeExpireDate;
			}
		}

		/**
		 * 消费者资讯
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class ConsumerInfo {

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("MerchantMemberID")
			@Size(max = 60)
			private String merchantMemberId;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Email")
			@Size(max = 30)
			private String email;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Phone")
			@Size(max = 60)
			private String phone;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Name")
			@Size(max = 50)
			private String name;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("CountryCode")
			@Size(max = 3)
			private String countryCode;

			@JsonInclude(Include.NON_NULL)
			@JsonProperty("Address")
			@Size(max = 50)
			private String address;

			/**
			 * 默认构造器
			 */
			public ConsumerInfo() {
			}

			/**
			 * @return 消费者会员编号
			 */
			public String getMerchantMemberId() {
				return merchantMemberId;
			}

			/**
			 * 当 rememberCard = 1，此栏位必填。
			 *
			 * @param merchantMemberId 消费者会员编号
			 */
			public void setMerchantMemberId(String merchantMemberId) {
				this.merchantMemberId = merchantMemberId;
			}

			/**
			 * @return 信用卡持卡人电子信箱
			 */
			public String getEmail() {
				return email;
			}

			/**
			 * @param email 信用卡持卡人电子信箱
			 */
			public void setEmail(String email) {
				this.email = email;
			}

			/**
			 * @return 信用卡持卡人电话
			 */
			public String getPhone() {
				return phone;
			}

			/**
			 * @param phone 信用卡持卡人电话
			 */
			public void setPhone(String phone) {
				this.phone = phone;
			}

			/**
			 * @return 信用卡持卡人姓名
			 */
			public String getName() {
				return name;
			}

			/**
			 * @param name 信用卡持卡人姓名
			 */
			public void setName(String name) {
				this.name = name;
			}

			/**
			 * @return 国别码
			 */
			public String getCountryCode() {
				return countryCode;
			}

			/**
			 * 持卡人帐单地址国别码，请参考 ISO-3166；台湾请填写 158。
			 *
			 * @param countryCode 国别码
			 */
			public void setCountryCode(String countryCode) {
				this.countryCode = countryCode;
			}

			/**
			 * @return 地址
			 */
			public String getAddress() {
				return address;
			}

			/**
			 * 持卡人帐单地址。
			 *
			 * @param address 地址
			 */
			public void setAddress(String address) {
				this.address = address;
			}
		}
	}
}
