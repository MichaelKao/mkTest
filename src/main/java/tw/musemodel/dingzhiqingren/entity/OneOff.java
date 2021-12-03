package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 一次性
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "yi_ci_xing")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneOff implements java.io.Serializable {

	private static final long serialVersionUID = 6884914470913418453L;

	@Basic(optional = false)
	@Column(nullable = false)
	@Id
	private Integer id;

	/**
	 * 首頁第一次提示導覽框
	 */
	@Basic(optional = false)
	@Column(name = "shou_ye_dao_lan")
	@NotNull
	private Boolean indexGuidance;

	/**
	 * 默认构造器
	 */
	public OneOff() {
	}

	public OneOff(Integer id, Boolean indexGuidance) {
		this.id = id;
		this.indexGuidance = indexGuidance;
	}

	/**
	 * 主鍵
	 *
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 主鍵
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 首頁第一次提示導覽框
	 *
	 * @return
	 */
	public Boolean getIndexGuidance() {
		return indexGuidance;
	}

	/**
	 *
	 * @param indexGuidance 首頁第一次提示導覽框
	 */
	public void setIndexGuidance(Boolean indexGuidance) {
		this.indexGuidance = indexGuidance;
	}
}
