package tw.com.ecpay.ecpg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 建立交易 (Server)
 * 
 * @author m@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyPayToken {
	
	@JsonProperty("MerchantID")
	@NotBlank(message = "inpay2.payToken.merchantId.NotBlank")
	@Size(max = 10)
	private String merchantId;

	@JsonProperty("RqHeader")
	@NotNull(message = "inpay2.payToken.rqHeader.NotNull")
	private RqHeader rqHeader;

	@JsonProperty("Data")
	@NotBlank(message = "inpay2.payToken.data.NotBlank")
	private String data;

	/**
	 * 默認構造器
	 */
	public ApplyPayToken() {
	}
	
	/**
	 * @return 特店編號
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * 若為平台商時，參數請帶平台商所綁的特店編號
	 * 
	 * @param merchantId 特店編號
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return 傳輸資料
	 */
	public RqHeader getRqHeader() {
		return rqHeader;
	}

	/**
	 * @param rqHeader 傳輸資料
	 */
	public void setRqHeader(RqHeader rqHeader) {
		this.rqHeader = rqHeader;
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
	 * 傳輸資料
	 * 
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class RqHeader {
	
		@JsonProperty("Timestamp")
		@NotNull(message = "inpay2.payToken.rqHeader.timestamp.notNull")
		private Long timestamp;

		@JsonProperty("Revision")
		@NotBlank(message = "inpay2.payToken.rqHeader.revision.notBlank")
		@Size(max = 10)
		private String revision;

		/**
		 * 默認構造器
		 */
		public RqHeader() {
		}

		/**
		 * @return 傳輸時間
		 */
		public Long getTimestamp() {
			return timestamp;
		}

		/**
		 * 時間戳 Unix timestamp；若時間戳跟綠界伺服器接收到時間超過 10 分鐘時，交易會失敗無法進行
		 * 
		 * @param timestamp 傳輸時間
		 */
		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}

		/**
		 * @return 串接文件版號
		 */
		public String getRevision() {
			return revision;
		}

		/**
		 * 請參考文件封面的文件版號。Ex: 1.0.0
		 * 
		 * @param revision 串接文件版號
		 */
		public void setRevision(String revision) {
			this.revision = revision;
		}
	}
	
	/**
	 * 建立交易(Server)
	 *
	 * <h3>应用场景</h3>
	 * <p>廠商 Server 需將從廠商 Web 取得到的 PayToken，送至綠界 Server 建立交易。</p>
	 * <h3>介接路径</h3>
	 * <ul>
	 * <li>正式环境：https://ecpg.ecpay.com.tw/Merchant/CreatePayment</li>
	 * <li>测试环境：https://ecpg-stage.ecpay.com.tw/Merchant/CreatePayment</li>
	 * </ul>
	 * <h3>厂商传入参数(JSON 格式)</h3>
	 * <ul>
	 * <li>Content Type：application/json</li>
	 * <li>HTTP Method：POST</li>
	 * </ul>
	 *
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data {
		
		@JsonInclude(Include.NON_NULL)
		@JsonProperty("PlatformID")
		@Size(max = 10)
		private String platformId;

		@JsonProperty("MerchantID")
		@NotBlank(message = "inpay.payToken.data.merchantId.NotBlank")
		@Size(max = 10)
		private String merchantId;

		@JsonProperty("PayToken")
		@NotBlank(message = "inpay.payToken.data.payToken.NotBlank")
		@Size(max = 64)
		private String payToken;
		
		@JsonProperty("MerchantTradeNo")
		@NotBlank(message = "inpay.payToken.data.merchantTradeNo.NotBlank")
		@Size(max = 20)
		private String merchantTradeNo;

		/**
		 * 默認構造器
		 */
		public Data() {
		}
		
		/**
		 * @param merchantId 廠商編號
		 * @param payToken 付款代碼
		 * @param merchantTradeNo 特店交易編號
		 */
		public Data(String merchantId, String payToken, String merchantTradeNo) {
			this.merchantId = merchantId;
			this.payToken = payToken;
			this.merchantTradeNo = merchantTradeNo;
		}

		/**
		 * @return 特約合作平台商代號
		 */
		public String getPlatformId() {
			return platformId;
		}

		/**
		 * @param platformId 特約合作平台商代號
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
		 * @return 付款代碼
		 */
		public String getPayToken() {
			return payToken;
		}

		/**
		 * @param payToken 付款代碼
		 */
		public void setPayToken(String payToken) {
			this.payToken = payToken;
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
	}
}
