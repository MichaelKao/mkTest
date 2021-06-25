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
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * 情人
 *
 * @author p@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(schema = "yuepao", name = "qing_ren", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"shi_bie_ma"})
})
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lover implements java.io.Serializable {

	private static final long serialVersionUID = 5470899666401402787L;

	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Basic(optional = false)
	@Column(name = "shi_bie_ma", nullable = false)
	private UUID identifier;

	@JoinColumn(name = "guo_jia", nullable = false, referencedColumnName = "id")
	@ManyToOne(optional = false)
	@JsonManagedReference
	private Country country;

	@Basic(optional = false)
	@Column(name = "zhang_hao", nullable = false)
	private String login;

	@Column(name = "mi_ma")
	@JsonIgnore
	private String shadow;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private Activation activation;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private LineUserProfile lineUserProfile;

	@Column(name = "huo_yue")
	@Temporal(TemporalType.TIMESTAMP)
	private Date active;

	@Column(name = "dao_qi")
	@Temporal(TemporalType.TIMESTAMP)
	private Date vip;

	@JoinColumn(name = "di_qu", referencedColumnName = "id")
	@ManyToOne
	@JsonManagedReference
	private Location location;

	@Column(name = "ni_cheng")
	private String nickname;

	@Column(name = "sheng_ri")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(name = "xing_bie")
	private Boolean gender;

	@Column(name = "da_tou")
	private String profileImage;

	@Column(name = "zi_jie")
	private String aboutMe;

	@Column(name = "ha_luo")
	private String greeting;

	@Column(name = "ti_xing")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private BodyType bodyType;

	@Column(name = "shen_gao")
	private Short height;

	@Column(name = "ti_zhong")
	private Short weight;

	@Column(name = "xue_li")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Education education;

	@Column(name = "hun_yin")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Marriage marriage;

	@Column(name = "zhi_ye")
	private String occupation;

	@Column(name = "chou_yan")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Smoking smoking;

	@Column(name = "yin_jiu")
	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	private Drinking drinking;

	@Column(name = "tian_jia_hao_you")
	private String inviteMeAsLineFriend;

	@Column(name = "li_xiang_dui_xiang")
	private String idealConditions;

	@JoinTable(
		name = "shou_quan",
		joinColumns = {
			@JoinColumn(name = "qing_ren", referencedColumnName = "id", nullable = false)
		},
		inverseJoinColumns = {
			@JoinColumn(name = "shen_fen", referencedColumnName = "id", nullable = false)
		}
	)
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonManagedReference
	private Collection<Role> roles;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lover")
	@JsonBackReference
	private Collection<Picture> pictures;

	/**
	 * 默认构造器
	 */
	public Lover() {
	}

	/**
	 * @param id 主键
	 */
	protected Lover(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

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
	 * @return 主键
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id 主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return 识别码
	 */
	public UUID getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier 识别码
	 */
	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return 国码
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country 国码
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return 帐号(手机号)
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login 帐号(手机号)
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return 密码
	 */
	public String getShadow() {
		return shadow;
	}

	/**
	 * @param shadow 密码
	 */
	public void setShadow(String shadow) {
		this.shadow = shadow;
	}

	/**
	 * @return 激活
	 */
	public Activation getActivation() {
		return activation;
	}

	/**
	 * @param activation 激活
	 */
	public void setActivation(Activation activation) {
		this.activation = activation;
	}

	/**
	 * @return LINE User Profile
	 */
	public LineUserProfile getLineUserProfile() {
		return lineUserProfile;
	}

	/**
	 * @param lineUserProfile LINE User Profile
	 */
	public void setLineUserProfile(LineUserProfile lineUserProfile) {
		this.lineUserProfile = lineUserProfile;
	}

	/**
	 * @return 活跃
	 */
	public Date getActive() {
		return active;
	}

	/**
	 * @param active 活跃
	 */
	public void setActive(Date active) {
		this.active = active;
	}

	/**
	 * @return VIP
	 */
	public Date getVip() {
		return vip;
	}

	/**
	 * @param vip VIP
	 */
	public void setVip(Date vip) {
		this.vip = vip;
	}

	/**
	 * @return 地区
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location 地区
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return 昵称
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname 昵称
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return 生日
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday 生日
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return 性别
	 */
	public Boolean getGender() {
		return gender;
	}

	/**
	 * @param gender 性别
	 */
	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	/**
	 * @return 大头
	 */
	public String getProfileImage() {
		return profileImage;
	}

	/**
	 * @param profileImage 大头
	 */
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	/**
	 * @return 自介
	 */
	public String getAboutMe() {
		return aboutMe;
	}

	/**
	 * @param aboutMe 自介
	 */
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	/**
	 * @return 哈啰
	 */
	public String getGreeting() {
		return greeting;
	}

	/**
	 * @param greeting 哈啰
	 */
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	/**
	 * @return 体型
	 */
	public BodyType getBodyType() {
		return bodyType;
	}

	/**
	 * @param bodyType 体型
	 */
	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

	/**
	 * @return 身高
	 */
	public Short getHeight() {
		return height;
	}

	/**
	 * @param height 身高
	 */
	public void setHeight(Short height) {
		this.height = height;
	}

	/**
	 * @return 体重
	 */
	public Short getWeight() {
		return weight;
	}

	/**
	 * @param weight 体重
	 */
	public void setWeight(Short weight) {
		this.weight = weight;
	}

	/**
	 * @return 学历
	 */
	public Education getEducation() {
		return education;
	}

	/**
	 * @param education 学历
	 */
	public void setEducation(Education education) {
		this.education = education;
	}

	/**
	 * @return 婚姻
	 */
	public Marriage getMarriage() {
		return marriage;
	}

	/**
	 * @param marriage 婚姻
	 */
	public void setMarriage(Marriage marriage) {
		this.marriage = marriage;
	}

	/**
	 * @return 职业
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * @param occupation 职业
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * @return 抽烟
	 */
	public Smoking getSmoking() {
		return smoking;
	}

	/**
	 * @param smoking 抽烟
	 */
	public void setSmoking(Smoking smoking) {
		this.smoking = smoking;
	}

	/**
	 * @return 饮酒
	 */
	public Drinking getDrinking() {
		return drinking;
	}

	/**
	 * @param drinking 饮酒
	 */
	public void setDrinking(Drinking drinking) {
		this.drinking = drinking;
	}

	/**
	 * @return 添加 LINE 好友
	 */
	public String getInviteMeAsLineFriend() {
		return inviteMeAsLineFriend;
	}

	/**
	 * @param inviteMeAsLineFriend 添加 LINE 好友
	 */
	public void setInviteMeAsLineFriend(String inviteMeAsLineFriend) {
		this.inviteMeAsLineFriend = inviteMeAsLineFriend;
	}

	/**
	 * @return 理想对象
	 */
	public String getIdealConditions() {
		return idealConditions;
	}

	/**
	 * @param idealConditions 理想对象
	 */
	public void setIdealConditions(String idealConditions) {
		this.idealConditions = idealConditions;
	}

	/**
	 * @return 身份
	 */
	public Collection<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles 身份
	 */
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return 照片集
	 */
	public Collection<Picture> getPictures() {
		return pictures;
	}

	/**
	 * @param pictures 照片集
	 */
	public void setPictures(Collection<Picture> pictures) {
		this.pictures = pictures;
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
}
