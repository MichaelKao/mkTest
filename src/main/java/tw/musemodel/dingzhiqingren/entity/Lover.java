package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import tw.musemodel.dingzhiqingren.entity.embedded.AppearedLocation;
import tw.musemodel.dingzhiqingren.entity.embedded.Blacklist;
import tw.musemodel.dingzhiqingren.entity.embedded.DesiredCompanionship;
import tw.musemodel.dingzhiqingren.entity.embedded.Follow;

/**
 * 养蜜
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings({"EqualsAndHashcode", "PersistenceUnitPresent"})
@Table(name = "qing_ren", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"shi_bie_ma"})
})
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lover implements java.io.Serializable {

	private static final long serialVersionUID = 7620248150717440860L;

	/**
	 * 主键
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	/**
	 * 识别码
	 */
	@Basic(optional = false)
	@Column(name = "shi_bie_ma", nullable = false)
	private UUID identifier;

	/**
	 * 国家
	 */
	@JoinColumn(name = "guo_jia", nullable = false, referencedColumnName = "id")
	@ManyToOne(optional = false)
	@JsonManagedReference
	private Country country;

	/**
	 * 帐号(手机号)
	 */
	@Column(name = "zhang_hao", nullable = false)
	private String login;

	/**
	 * 密码
	 */
	@Column(name = "mi_ma")
	@JsonIgnore
	private String shadow;

	/**
	 * 重设密码
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private ResetShadow resetShadow;

	/**
	 * 激活
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private Activation activation;

	/**
	 * LINE 用户信息
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private LineUserProfile lineUserProfile;

	/**
	 * 最后活跃时间
	 */
	@Column(name = "huo_yue")
	@Temporal(TemporalType.TIMESTAMP)
	private Date active;

	/**
	 * VIP 有效期
	 */
	@Column(name = "dao_qi")
	@Temporal(TemporalType.TIMESTAMP)
	private Date vip;

	/**
	 * 所在地区
	 */
	@JoinColumn(name = "di_qu", referencedColumnName = "id")
	@ManyToOne
	@JsonManagedReference
	private Location residence;

	/**
	 * 昵称
	 */
	@Column(name = "ni_cheng")
	private String nickname;

	/**
	 * 生日
	 */
	@Column(name = "sheng_ri")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	/**
	 * 性别
	 */
	@Column(name = "xing_bie")
	private Boolean gender;

	/**
	 * 大头
	 */
	@Column(name = "da_tou")
	private String profileImage;

	/**
	 * 自介
	 */
	@Column(name = "zi_jie")
	private String aboutMe;

	/**
	 * 招呼语
	 */
	@Column(name = "ha_luo")
	private String greeting;

	/**
	 * 体型
	 */
	@Column(name = "ti_xing")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private BodyType bodyType;

	/**
	 * 身高
	 */
	@Column(name = "shen_gao")
	private Short height;

	/**
	 * 体重
	 */
	@Column(name = "ti_zhong")
	private Short weight;

	/**
	 * 学历
	 */
	@Column(name = "xue_li")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Education education;

	/**
	 * 婚姻
	 */
	@Column(name = "hun_yin")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Marriage marriage;

	/**
	 * 职业
	 */
	@Column(name = "zhi_ye")
	private String occupation;

	/**
	 * 抽烟
	 */
	@Column(name = "chou_yan")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Smoking smoking;

	/**
	 * 饮酒
	 */
	@Column(name = "yin_jiu")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Drinking drinking;

	/**
	 * 添加 LINE 好友的链结
	 */
	@Column(name = "tian_jia_hao_you")
	private String inviteMeAsLineFriend;

	/**
	 * 理想对象条件
	 */
	@Column(name = "li_xiang_dui_xiang")
	private String idealConditions;

	/**
	 * 身份
	 */
	@OneToMany(mappedBy = "role")
	@JsonIgnore
	private Set<Privilege> roles;

	/**
	 * 生活照
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lover")
	@JsonBackReference
	private Collection<Picture> pictures;

	/**
	 * 哪些甜心给了(男士)即时通信
	 */
	@OneToMany(mappedBy = "girl")
	@JsonIgnore
	private Set<LineGiven> girls;

	/**
	 * (甜心)给了哪些男士即时通信
	 */
	@OneToMany(mappedBy = "guy")
	@JsonIgnore
	private Set<LineGiven> guys;

	/**
	 * 收藏(追踪)
	 */
	@OneToMany(mappedBy = "following")
	@JsonIgnore
	private Set<Follow> following;

	/**
	 * 被收藏(被追踪)
	 */
	@OneToMany(mappedBy = "followed")
	@JsonIgnore
	private Set<Follow> followed;

	/**
	 * 年收入
	 */
	@JoinColumn(name = "nian_shou_ru", referencedColumnName = "id")
	@ManyToOne
	@JsonManagedReference
	private AnnualIncome annualIncome;

	/**
	 * 零用钱
	 */
	@JoinColumn(name = "ling_yong_qian", referencedColumnName = "id")
	@ManyToOne
	@JsonManagedReference
	private Allowance allowance;

	/**
	 * 砍号
	 */
	@Column(name = "shan_chu")
	private String delete;

	/**
	 * 提取车马费信息
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "honey")
	private WithdrawalInfo withdrawalInfo;

	/**
	 * 出没哪些地区
	 */
	@OneToMany(mappedBy = "lover")
	@JsonIgnore
	private Set<AppearedLocation> locations;

	/**
	 * 期望哪些陪伴
	 */
	@OneToMany(mappedBy = "lover")
	@JsonIgnore
	private Set<DesiredCompanionship> companionships;

	/**
	 * 通过安心认证
	 */
	@Column(name = "an_xin")
	private Boolean relief;

	/**
	 * 註冊时间
	 */
	@Column(name = "zhu_ce_shi_chuo")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registered;

	/**
	 * 相處關係
	 */
	@Column(name = "xiang_chu_guan_xi")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Relationship relationship;

	/**
	 * 相處關係
	 */
	@Column(name = "line_notify_access_token")
	private String lineNotifyAccessToken;

	/**
	 * 推荐码
	 */
	@Column(name = "tui_jian_ma")
	private String referralCode;

	/**
	 * 推荐人
	 */
	@JoinColumn(name = "tui_jian_ren", referencedColumnName = "id")
	@ManyToOne
	@JsonManagedReference
	private Lover referrer;

	/**
	 * 男士种类
	 */
	@Column(name = "nan_shi_zhong_lei")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private MaleSpecies maleSpecies;

	/**
	 * 가짜 계좌
	 */
	@Column(name = "wei")
	private boolean fake;

	/**
	 * 哪些拉黑了我
	 */
	@OneToMany(mappedBy = "blocker")
	@JsonIgnore
	private Set<Blacklist> blockers;

	/**
	 * 我拉黑了哪些
	 */
	@OneToMany(mappedBy = "blocked")
	@JsonIgnore
	private Set<Blacklist> blocked;

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Lover)) {
			return false;
		}
		Lover other = (Lover) object;
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
	 * 体型
	 *
	 * @author m@musemodel.tw
	 */
	public enum BodyType {

		PING_JUN("fit", 1),
		MIAO_TIAO("slim", 2),
		YUN_DONG("sporty", 3),
		QU_XIAN("curvy", 4),
		WEI_PANG("chubby", 5),
		FENG_MAN("plump", 6);

		private String label;

		private int index;

		private BodyType(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}

	/**
	 * 学历
	 *
	 * @author m@musemodel.tw
	 */
	public enum Education {

		GUO_XIAO("國小", 1),
		GUO_ZHONG("國中", 2),
		GAO_ZHONG("高中", 3),
		GAO_ZHI("高職", 4),
		ZHUAN_KE("專科", 5),
		DA_XUE("大學", 6),
		YAN_JIU_SUO("研究所", 7);

		private String label;

		private int index;

		private Education(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}

	/**
	 * 婚姻
	 *
	 * @author m@musemodel.tw
	 */
	public enum Marriage {

		DAN_SHEN("單身", 1),
		SI_HUI("死會", 2),
		YI_HUN("已婚", 3);

		private String label;

		private int index;

		private Marriage(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}

	/**
	 * 抽烟
	 *
	 * @author m@musemodel.tw
	 */
	public enum Smoking {

		BU_CHOU_YAN("不抽菸", 1),
		OU_ER_CHOU("偶爾抽", 2),
		JING_CHANG_CHOU("經常抽", 3);

		private String label;

		private int index;

		private Smoking(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}

	/**
	 * 饮酒
	 *
	 * @author m@musemodel.tw
	 */
	public enum Drinking {

		BU_HE_JIU("不喝酒", 1),
		OU_ER_HE("偶爾喝", 2),
		JING_CHANG_HE("經爾喝", 3);

		private String label;

		private int index;

		private Drinking(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}

	/**
	 * 相處關係
	 *
	 * @author m@musemodel.tw
	 */
	public enum Relationship {

		CHANG_QI("長期", 1),
		DUAN_QI("短期", 2),
		CHANG_DUAN_JIE_KE("長短皆可", 3),
		DAN_CI_YUE_HUI("單次約會", 4);

		private String label;

		private int index;

		private Relationship(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}

	/**
	 * 男士种类
	 *
	 * @author p@musemodel.tw
	 */
	public enum MaleSpecies {

		VIP("VIP", 1),
		VVIP("VVIP", 2);

		private String label;

		private int index;

		private MaleSpecies(String label, int index) {
			this.label = label;
			this.index = index;
		}
	}
}
