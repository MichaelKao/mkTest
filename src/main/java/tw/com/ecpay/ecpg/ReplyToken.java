package tw.com.ecpay.ecpg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 绿界回传厂商验证码
 *
 * @author p@musemodel.tw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplyToken {

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
	public ReplyToken() {
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
	 *
	 * @author p@musemodel.tw
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
	 * 加密过 JSON 格式的数据
	 *
	 * @author p@musemodel.tw
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data {
	}
}
