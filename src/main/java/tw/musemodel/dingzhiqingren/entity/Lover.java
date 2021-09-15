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
@Entity
@SuppressWarnings("PersistenceUnitPresent")
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
	 * 收藏
	 */
	@OneToMany(mappedBy = "following")
	@JsonIgnore
	private Set<Follow> following;

	/**
	 * 被收藏
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
	 * @return 重设密码
	 */
	public ResetShadow getResetShadow() {
		return resetShadow;
	}

	/**
	 * @param resetShadow 重设密码
	 */
	public void setResetShadow(ResetShadow resetShadow) {
		this.resetShadow = resetShadow;
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
	 * @return VIP 有效期
	 */
	public Date getVip() {
		return vip;
	}

	/**
	 * @param vip VIP 有效期
	 */
	public void setVip(Date vip) {
		this.vip = vip;
	}

	/**
	 * @return 所在地区
	 */
	public Location getResidence() {
		return residence;
	}

	/**
	 * @param residence 所在地区
	 */
	public void setResidence(Location residence) {
		this.residence = residence;
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
	public Set<Privilege> getRoles() {
		return roles;
	}

	/**
	 * @param roles 身份
	 */
	public void setRoles(Set<Privilege> roles) {
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
	 * @return 哪些甜心给了(男士)即时通信
	 */
	public Set<LineGiven> getGirls() {
		return girls;
	}

	/**
	 * @param girls 哪些甜心给了(男士)即时通信
	 */
	public void setGirls(Set<LineGiven> girls) {
		this.girls = girls;
	}

	/**
	 * @return (甜心)给了哪些男士即时通信
	 */
	public Set<LineGiven> getGuys() {
		return guys;
	}

	/**
	 * @param guys (甜心)给了哪些男士即时通信
	 */
	public void setGuys(Set<LineGiven> guys) {
		this.guys = guys;
	}

	/**
	 * @return 收藏
	 */
	public Set<Follow> getFollowing() {
		return following;
	}

	/**
	 * @param following 收藏
	 */
	public void setFollowing(Set<Follow> following) {
		this.following = following;
	}

	/**
	 * @return 被收藏
	 */
	public Set<Follow> getFollowed() {
		return followed;
	}

	/**
	 * @param followed 被收藏
	 */
	public void setFollowed(Set<Follow> followed) {
		this.followed = followed;
	}

	/**
	 * @return 年收入
	 */
	public AnnualIncome getAnnualIncome() {
		return annualIncome;
	}

	/**
	 * @param annualIncome 年收入
	 */
	public void setAnnualIncome(AnnualIncome annualIncome) {
		this.annualIncome = annualIncome;
	}

	/**
	 * @return 零用錢
	 */
	public Allowance getAllowance() {
		return allowance;
	}

	/**
	 * @param allowance 零用錢
	 */
	public void setAllowance(Allowance allowance) {
		this.allowance = allowance;
	}

	/**
	 * 刪除帳號
	 *
	 * @return
	 */
	public String getDelete() {
		return delete;
	}

	/**
	 * 刪除帳號
	 *
	 * @param delete
	 */
	public void setDelete(String delete) {
		this.delete = delete;
	}

	/**
	 * @return 提領資訊
	 */
	public WithdrawalInfo getWithdrawalInfo() {
		return withdrawalInfo;
	}

	/**
	 * @param withdrawalInfo 提領資訊
	 */
	public void setWithdrawalInfo(WithdrawalInfo withdrawalInfo) {
		this.withdrawalInfo = withdrawalInfo;
	}

	/**
	 * @return 出没地区
	 */
	public Set<AppearedLocation> getLocations() {
		return locations;
	}

	/**
	 * @param locations 出没地区
	 */
	public void setLocations(Set<AppearedLocation> locations) {
		this.locations = locations;
	}

	/**
	 * @return 友谊
	 */
	public Set<DesiredCompanionship> getCompanionships() {
		return companionships;
	}

	/**
	 * @param companionships 友谊
	 */
	public void setCompanionships(Set<DesiredCompanionship> companionships) {
		this.companionships = companionships;
	}

	/**
	 * @return 安心認證
	 */
	public Boolean getRelief() {
		return relief;
	}

	/**
	 * @param relief 安心認證
	 */
	public void setRelief(Boolean relief) {
		this.relief = relief;
	}

	/**
	 * @return 註冊時戳
	 */
	public Date getRegistered() {
		return registered;
	}

	/**
	 * @param registered 註冊時戳
	 */
	public void setRegistered(Date registered) {
		this.registered = registered;
	}

	/**
	 * @return 相處關係
	 */
	public Relationship getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship 相處關係
	 */
	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return LINE 网站服务通知访问令牌
	 */
	public String getLineNotifyAccessToken() {
		return lineNotifyAccessToken;
	}

	/**
	 * @param lineNotifyAccessToken LINE 网站服务通知访问令牌
	 */
	public void setLineNotifyAccessToken(String lineNotifyAccessToken) {
		this.lineNotifyAccessToken = lineNotifyAccessToken;
	}

	/**
	 * @return 推荐码
	 */
	public String getReferralCode() {
		return referralCode;
	}

	/**
	 * @param referralCode 推荐码
	 */
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	/**
	 * @return 推荐人
	 */
	public Lover getReferrer() {
		return referrer;
	}

	/**
	 * @param referrer 推荐人
	 */
	public void setReferrer(Lover referrer) {
		this.referrer = referrer;
	}

	/**
	 * @return 男士种类
	 */
	public MaleSpecies getMaleSpecies() {
		return maleSpecies;
	}

	/**
	 * @param maleSpecies 男士种类
	 */
	public void setMaleSpecies(MaleSpecies maleSpecies) {
		this.maleSpecies = maleSpecies;
	}

	/**
	 * @return 哪些拉黑了我
	 */
	public Set<Blacklist> getBlockers() {
		return blockers;
	}

	/**
	 * @param blockers 哪些拉黑了我
	 */
	public void setBlockers(Set<Blacklist> blockers) {
		this.blockers = blockers;
	}

	/**
	 * @return 我拉黑了哪些
	 */
	public Set<Blacklist> getBlocked() {
		return blocked;
	}

	/**
	 * @param blocked 我拉黑了哪些
	 */
	public void setBlocked(Set<Blacklist> blocked) {
		this.blocked = blocked;
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
