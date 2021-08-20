package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * 历程
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "li_cheng")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class History implements java.io.Serializable {

	private static final long serialVersionUID = 7414060697467971708L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "zhu_dong_de", nullable = false, referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Lover initiative;

	@JoinColumn(name = "bei_dong_de", nullable = false, referencedColumnName = "id")
	@ManyToOne
	private Lover passive;

	@Column(name = "xing_wei")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Behavior behavior;

	@Column(name = "shi_chuo")
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurred;

	@Column(name = "yi_du")
	@Temporal(TemporalType.TIMESTAMP)
	private Date seen;

	@Basic(optional = false)
	@Column(name = "dian_shu", nullable = false)
	private Short points;

	@JoinColumn(name = "lu_jie", referencedColumnName = "id")
	@ManyToOne
	@JsonManagedReference
	private LuJie luJie;

	@Column(name = "zhao_hu_yu")
	private String greeting;

	@Column(name = "xing_ji")
	private Short rate;

	@Column(name = "ping_jia")
	private String comment;

	@Column(name = "hui_ying")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reply;

	@Column(name = "fang_xing_sheng_huo_zhao")
	private Boolean showAllPictures;

	/**
	 * 默认构造器
	 */
	public History() {
		occurred = new Date(System.currentTimeMillis());
		points = 0;
		showAllPictures = Boolean.FALSE;
	}

	/**
	 * 构造器：适用于「升级为 VIP」。
	 *
	 * @param initiative 用户(应限男性)
	 * @param occurred 时戳
	 * @param luJie 绿界
	 */
	public History(Lover initiative, Date occurred, LuJie luJie) {
		this.initiative = initiative;
		this.behavior = Behavior.YUE_FEI;
		this.occurred = occurred;
		points = 0;
		this.luJie = luJie;
	}

	/**
	 * 构造器：适用于「充值」。
	 *
	 * @param initiative 用户(应限男性)
	 * @param occurred 时戳
	 * @param points 点数
	 * @param luJie 绿界
	 */
	public History(Lover initiative, Date occurred, short points, LuJie luJie) {
		this.initiative = initiative;
		this.behavior = Behavior.CHU_ZHI;
		this.occurred = occurred;
		this.points = points;
		this.luJie = luJie;
	}

	/**
	 * 构造器：适用于「看过我」、「放行生活照」。
	 *
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @param behavior 行为
	 */
	public History(Lover initiative, Lover passive, Behavior behavior) {
		this();
		this.initiative = initiative;
		this.passive = passive;
		this.behavior = behavior;
	}

	/**
	 * 构造器：适用于「车马费」、「给我赖」。
	 *
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @param behavior 行为
	 * @param points 点数
	 */
	public History(Lover initiative, Lover passive, Behavior behavior, short points) {
		this();
		this.initiative = initiative;
		this.passive = passive;
		this.behavior = behavior;
		this.points = points;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof History)) {
			return false;
		}
		History other = (History) object;
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
	 * @return 主动方
	 */
	public Lover getInitiative() {
		return initiative;
	}

	/**
	 * @param initiative 主动方
	 */
	public void setInitiative(Lover initiative) {
		this.initiative = initiative;
	}

	/**
	 * @return 被动方
	 */
	public Lover getPassive() {
		return passive;
	}

	/**
	 * @param passive 被动方
	 */
	public void setPassive(Lover passive) {
		this.passive = passive;
	}

	/**
	 * @return 行为
	 */
	public Behavior getBehavior() {
		return behavior;
	}

	/**
	 * @param behavior 行为
	 */
	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

	/**
	 * @return 时戳
	 */
	public Date getOccurred() {
		return occurred;
	}

	/**
	 * @param occurred 时戳
	 */
	public void setOccurred(Date occurred) {
		this.occurred = occurred;
	}

	/**
	 * @return 已读
	 */
	public Date getSeen() {
		return seen;
	}

	/**
	 * @param seen 已读
	 */
	public void setSeen(Date seen) {
		this.seen = seen;
	}

	/**
	 * @return 点数
	 */
	public Short getPoints() {
		return points;
	}

	/**
	 * @param points 点数
	 */
	public void setPoints(Short points) {
		this.points = points;
	}

	/**
	 * @return 绿界
	 */
	public LuJie getLuJie() {
		return luJie;
	}

	/**
	 * @param luJie 绿界
	 */
	public void setLuJie(LuJie luJie) {
		this.luJie = luJie;
	}

	/**
	 * @return 招呼语
	 */
	public String getGreeting() {
		return greeting;
	}

	/**
	 * @param greeting 招呼语
	 */
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	/**
	 * @return 星級
	 */
	public Short getRate() {
		return rate;
	}

	/**
	 * @param rate 星級
	 */
	public void setRate(Short rate) {
		this.rate = rate;
	}

	/**
	 * @return 評價
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment 評價
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return 回應
	 */
	public Date getReply() {
		return reply;
	}

	/**
	 * @param reply 回應
	 */
	public void setReply(Date reply) {
		this.reply = reply;
	}

	/**
	 * @return 放行生活照
	 */
	public Boolean getShowAllPictures() {
		return showAllPictures;
	}

	/**
	 * @param showAllPictures 放行生活照
	 */
	public void setShowAllPictures(Boolean showAllPictures) {
		this.showAllPictures = showAllPictures;
	}

	/**
	 * 行为
	 *
	 * @author p@musemodel.tw
	 */
	public enum Behavior {

		/**
		 * 月费
		 */
		YUE_FEI("YUE_FEI", 1),
		/**
		 * 充值
		 */
		CHU_ZHI("CHU_ZHI", 2),
		/**
		 * 给我赖(男士向甜心索取)
		 */
		JI_WO_LAI("JI_WO_LAI", 3),
		/**
		 * 给你赖(甜心诱惑男士向其索取)
		 */
		JI_NI_LAI("JI_NI_LAI", 4),
		/**
		 * 不给赖(甜心拒绝给男士)
		 */
		BU_JI_LAI("BU_JI_LAI", 5),
		/**
		 * 打招呼
		 */
		DA_ZHAO_HU("DA_ZHAO_HU", 6),
		/**
		 * 看过我
		 */
		KAN_GUO_WO("KAN_GUO_WO", 7),
		/**
		 * 车马费
		 */
		CHE_MA_FEI("CHE_MA_FEI", 8),
		/**
		 * 赖扣点
		 */
		LAI_KOU_DIAN("LAI_KOU_DIAN", 9),
		/**
		 * 收藏
		 */
		SHOU_CANG("SHOU_CANG", 10),
		/**
		 * 取消收藏
		 */
		BU_SHOU_CANG("BU_SHOU_CANG", 11),
		/**
		 * 评价
		 */
		PING_JIA("PING_JIA", 12),
		/**
		 * 提領失敗
		 */
		TI_LING_SHI_BAI("TI_LING_SHI_BAI", 13),
		/**
		 * 提領成功
		 */
		TI_LING_CHENG_GONG("TI_LING_CHENG_GONG", 14),
		/**
		 * 安心認證通過
		 */
		AN_XIN_SHI_BAI("AN_XIN_SHI_BAI", 15),
		/**
		 * 安心認證不通過
		 */
		AN_XIN_CHENG_GONG("AN_XIN_CHENG_GONG", 16),
		/**
		 * 群發
		 */
		QUN_FA("QUN_FA", 17),
		/**
		 * 聊聊
		 */
		LIAO_LIAO("LIAO_LIAO", 18),
		/**
		 * 要車馬費
		 */
		YAO_CHE_MA_FEI("YAO_CHE_MA_FEI", 19),
		/**
		 * 放行生活照
		 */
		FANG_XING_SHENG_HUO_ZHAO("FANG_XING_SHENG_HUO_ZHAO", 20);

		private String label;

		private int index;

		private Behavior(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}
}
