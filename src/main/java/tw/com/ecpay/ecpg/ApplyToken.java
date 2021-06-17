package tw.com.ecpay.ecpg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 向绿界取得厂商验证码
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyToken {

	@JsonProperty("MerchantID")
	private String merchantId;

	@JsonProperty("RqHeader")
	private RqHeader rqHeader;

	@JsonProperty("Data")
	private String data;

	/**
	 * 默认构造器
	 */
	public ApplyToken() {
	}

	/**
	 * @return 特店编号
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId 若为平台商时，参数请带平台商所绑的特店编号。
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
	 * @param data 加密过 JSON 格式的数据。
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
		private Long timestamp;

		@JsonProperty("Revision")
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
		 * @param timestamp 时间戳 Unix timestamp；注意事项：若时间戳跟绿界服务器接收到时间超过 10
		 * 分钟时，交易会失败无法进行。
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
		 * @param revision 请参考文件封面的文件版号，例：1.0.0。
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
	 *
	 * <h3>介接路径</h3>
	 * <ul>
	 * <li>正式环境：https://ecpg.ecpay.com.tw/Merchant/GetTokenbyTrade</li>
	 * <li>测试环境：https://ecpg-stage.ecpay.com.tw/Merchant/GetTokenbyTrade</li>
	 * </ul>
	 *
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

		@JsonProperty("PlatformID")
		private String platformID;

		@JsonProperty("MerchantID")
		private String merchantId;

		@JsonProperty("RememberCard")
		private Short rememberCard;

		@JsonProperty("PaymentUIType")
		private Short paymentUIType;

		@JsonProperty("ChoosePaymentList")
		private String choosePaymentList;

		@JsonProperty("OrderInfo")
		private OrderInfo orderInfo;

		@JsonProperty("CardInfo")
		private CardInfo cardInfo;

		/**
		 * 默认构造器
		 */
		public Data() {
		}

		/**
		 * @return 特约合作平台商代号
		 */
		public String getPlatformID() {
			return platformID;
		}

		/**
		 * @param platformID
		 * 为专案合作的平台商使用。一般特店或平台商本身介接，则参数请带空值。若为专案合作平台商的特店使用时，则参数请带平台商所绑的特店编号
		 * MerchantID。
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
		 * @param rememberCard <ul><li>0：否</li><li>1：是</li></ul>
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
		 * @param paymentUIType
		 * <ul><li>0：信用卡定期定额</li><li>2：付款选择清单页</li></ul>
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
		 * @param choosePaymentList paymentUIType 如选择 2，则必填。
		 * <ul><li>0：全部付款方式</li><li>1：信用卡一次付清</li><li>2：信用卡分期付款</li><li>3：ATM</li><li>4：超商代码</li><li>5：超商条码</li></ul>
		 * <p>
		 * 可多选，例：1,2,3。 </p>
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
		 * @param cardInfo 以下情况为必填：<ol><li>paymentUIType 选择 0 或 1
		 * </li><li>paymentUIType 选择 2，且 choosePaymentList 选择 0，1 或
		 * 2</li></ol>
		 */
		public void setCardInfo(CardInfo cardInfo) {
			this.cardInfo = cardInfo;
		}

		/**
		 * 订单资讯
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class OrderInfo {

			@JsonProperty("MerchantTradeDate")
			private String merchantTradeDate;

			@JsonProperty("MerchantTradeNo")
			private String merchantTradeNo;

			@JsonProperty("TotalAmount")
			private Integer totalAmount;

			@JsonProperty("ReturnURL")
			private String returnURL;

			@JsonProperty("TradeDesc")
			private String tradeDesc;

			@JsonProperty("ItemName")
			private String itemName;

			/**
			 * 默认构造器
			 */
			public OrderInfo() {
			}

			/**
			 * @return 厂商交易时间
			 */
			public String getMerchantTradeDate() {
				return merchantTradeDate;
			}

			/**
			 * @param merchantTradeDate yyyy/MM/dd HH:mm:ss
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
			 * @param merchantTradeNo 均为唯一值，不可重复使用；英数字大小写混合。
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
			 * @param totalAmount
			 * 请带整数，不可有小数点。仅限新台币。各付款金额的限制，请参考：https://www.ecpay.com.tw/CascadeFAQ/CascadeFAQ_Qa?nID=3605。
			 */
			public void setTotalAmount(Integer totalAmount) {
				this.totalAmount = totalAmount;
			}

			/**
			 * @return 付款回传结果
			 */
			public String getReturnURL() {
				return returnURL;
			}

			/**
			 * @param returnURL 当消费者付款完成后，绿界会将付款结果参数以幕后(server
			 * POST)回传到该网址；详细说明请参考付款结果通知。
			 * <h3>注意事项</h3><ol><li>请勿设定与 client 端接收付款结果网址
			 * orderResultURL 相同位置，避免程式判断错误。 </li><li>请在收到 server
			 * 端付款结果通知后，请正确回应 1|OK 给绿界。
			 * </li></ol>
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
			 * @param itemName 商品名称以 # 分开。
			 */
			public void setItemName(String itemName) {
				this.itemName = itemName;
			}
		}

		/**
		 * 信用卡资讯
		 *
		 * @author p@musemodel.tw
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class CardInfo {

			@JsonProperty("Redeem")
			private String redeem;

			@JsonProperty("PeriodAmount")
			private Short periodAmount;

			@JsonProperty("PeriodType")
			private String periodType;

			@JsonProperty("Frequency")
			private Short frequency;

			@JsonProperty("ExecTimes")
			private Short execTimes;

			@JsonProperty("OrderResultURL")
			private String orderResultURL;

			@JsonProperty("PeriodReturnURL")
			private String periodReturnURL;

			@JsonProperty("CreditInstallment")
			private String creditInstallment;

			/**
			 * 默认构造器
			 */
			public CardInfo() {
				redeem = "0";
			}

			/**
			 * @return 使用信用卡红利
			 */
			public String getRedeem() {
				return redeem;
			}

			/**
			 * @param redeem 默认为不使用；0：不使用，1：使用。
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
			 * @param periodAmount 当 TokenData#paymentUIType 为 0
			 * 时，此栏位必填。
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
			 * @param periodType 当 TokenData#paymentUIType 为 0
			 * 时，此栏位必填；D：以天为周期，M：以月为周期，Y：以年为周期。
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
			 * @param frequency 当 TokenData#paymentUIType 为 0
			 * 时，此栏位必填；注意事项：至少要大于等于 1 次以上。当 periodType 设为 D 时，最多可设
			 * 365 次。当 periodType 设为 M 时，最多可设 12 次。当 periodType 设为 Y
			 * 时，最多可设 1 次。
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
			 * @param execTimes 当 TokenData#paymentUIType 为 0
			 * 时，此栏位必填。注意事项：至少要大于 1 次以上。当 PeriodType 设为 D 时，最多可设 999
			 * 次。当 PeriodType 设为 M 时，最多可设 99 次。当 PeriodType 设为 Y
			 * 时，最多可设 9 次。
			 */
			public void setExecTimes(Short execTimes) {
				this.execTimes = execTimes;
			}

			/**
			 * @return 3D 验证回传付款结果网址
			 */
			public String getOrderResultURL() {
				return orderResultURL;
			}

			/**
			 * @param orderResultURL 使用 3D
			 * 验证时，当消费者付款完成后，绿界会将付款结果参数以幕前(Client POST)回传到该网址。
			 */
			public void setOrderResultURL(String orderResultURL) {
				this.orderResultURL = orderResultURL;
			}

			/**
			 * @return 定期定额执行结果回应网址
			 */
			public String getPeriodReturnURL() {
				return periodReturnURL;
			}

			/**
			 * @param periodReturnURL 当 TokenData#paymentUIType 为 0
			 * 时，此栏位必填；若交易是信用卡定期定额的方式，则每次执行授权完，会将授权结果回传到这个设定的网址。
			 */
			public void setPeriodReturnURL(String periodReturnURL) {
				this.periodReturnURL = periodReturnURL;
			}

			/**
			 * @return 刷卡分期期数
			 */
			public String getCreditInstallment() {
				return creditInstallment;
			}

			/**
			 * @param creditInstallment 当 Token#choosePaymentList 为
			 * 0 或 Token#choosePaymentList 有选择 2
			 * 时，此栏位必填；支援多期数请以逗号分隔，例：3,6,12,18,24。
			 */
			public void setCreditInstallment(String creditInstallment) {
				this.creditInstallment = creditInstallment;
			}
		}
	}
}
