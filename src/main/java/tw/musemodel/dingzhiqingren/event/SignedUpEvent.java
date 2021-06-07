package tw.musemodel.dingzhiqingren.event;

import java.util.Locale;
import org.springframework.context.ApplicationEvent;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 事件：新建帐户
 *
 * @author p@musemodel.tw
 */
public class SignedUpEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8601277022895338929L;

	private Lover lover;

	private String domainName;

	private Locale locale;

	/**
	 * @param lover 情人
	 * @param domainName 网域名称
	 * @param locale 语言环境
	 */
	public SignedUpEvent(Lover lover, String domainName, Locale locale) {
		super(lover);
		this.lover = lover;
		this.domainName = domainName;
		this.locale = locale;
	}

	/**
	 * @return 帐户
	 */
	public Lover getLover() {
		return lover;
	}

	/**
	 * @param lover 帐户
	 */
	public void setLover(Lover lover) {
		this.lover = lover;
	}

	/**
	 * @return 网域名称
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @param domainName 网域名称
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * @return 语言环境
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale 语言环境
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
