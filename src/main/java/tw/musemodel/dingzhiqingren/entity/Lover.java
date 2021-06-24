package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Lover implements Serializable {

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
	@JsonBackReference
	private Country country;

	@Basic(optional = false)
	@Column(name = "zhang_hao", nullable = false)
	private String login;

	@Column(name = "mi_ma")
	private String shadow;

	@JoinColumn(name = "di_qu", referencedColumnName = "id")
	@ManyToOne
	@JsonBackReference
	private Location location;

	@Column(name = "ni_cheng")
	private String nickname;

	@Column(name = "sheng_ri")
	@Temporal(TemporalType.TIMESTAMP)
	private Date birthday;

	@Column(name = "xing_bie")
	private Boolean gender;

	@Column(name = "da_tou")
	private String photo;

	@Column(name = "zi_jie")
	private String introduction;

	@Column(name = "ha_luo")
	private String hello;

	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	@Column(name = "ti_xing")
	private BodyType bodyType;

	@Column(name = "shen_gao")
	private Short height;

	@Column(name = "ti_zhong")
	private Short weight;

	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	@Column(name = "xue_li")
	private Education education;

	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	@Column(name = "hun_yin")
	private Marriage marriage;

	@Column(name = "zhi_ye")
	private String occupation;

	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	@Column(name = "chou_yan")
	private Smoking smoking;

	@Enumerated(EnumType.STRING)
	@Type(type = "pgsql_enum")
	@Column(name = "yin_jiu")
	private Drinking drinking;

	@Column(name = "tian_jia_hao_you")
	private String lineID;

	@Column(name = "li_xiang_dui_xiang")
	private String idealType;

	@Column(name = "huo_yue")
	private Date active;

	@Column(name = "dao_qi")
	private Date vip;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private LineUserProfile lineUserProfile;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "lover")
	private Activation activation;

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
	private Collection<Role> roles;

	/**
	 * 建構子
	 */
	public Lover() {
	}

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
		return "tw.musemodel.dingzhiqingren.Lover[ id=" + id + " ]";
	}

	/**
	 * @return 主鍵
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id 主鍵
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return 識別碼
	 */
	public UUID getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier 識別碼
	 */
	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return 國碼
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country 國碼
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return 帳號
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login 帳號
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return 密碼
	 */
	public String getShadow() {
		return shadow;
	}

	/**
	 * @param shadow 密碼
	 */
	public void setShadow(String shadow) {
		this.shadow = shadow;
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
	 * @return 身分
	 */
	public Collection<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles 身分
	 */
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return 地區
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location 地區
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return 暱稱
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname 暱稱
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
	 * @return 性別
	 */
	public Boolean getGender() {
		return gender;
	}

	/**
	 * @param gender 性別
	 */
	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	/**
	 * @return 大頭照
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * @param photo 大頭照
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	/**
	 * @return 自我介紹
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction 自我介紹
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * @return 打招呼
	 */
	public String getHello() {
		return hello;
	}

	/**
	 * @param hello 打招呼
	 */
	public void setHello(String hello) {
		this.hello = hello;
	}

	/**
	 * @return 體型
	 */
	public BodyType getBodyType() {
		return bodyType;
	}

	/**
	 * @param bodyType 體型
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
	 * @return 體重
	 */
	public Short getWeight() {
		return weight;
	}

	/**
	 * @param weight 體重
	 */
	public void setWeight(Short weight) {
		this.weight = weight;
	}

	/**
	 * @return 學歷
	 */
	public Education getEducation() {
		return education;
	}

	/**
	 * @param education 學歷
	 */
	public void setEducation(Education education) {
		this.education = education;
	}

	/**
	 * @return 婚姻狀態
	 */
	public Marriage getMarriage() {
		return marriage;
	}

	/**
	 * @param marriage 婚姻狀態
	 */
	public void setMarriage(Marriage marriage) {
		this.marriage = marriage;
	}

	/**
	 * @return 職業
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * @param occupation 職業
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * @return 抽菸習慣
	 */
	public Smoking getSmoking() {
		return smoking;
	}

	/**
	 * @param smoking 抽菸習慣
	 */
	public void setSmoking(Smoking smoking) {
		this.smoking = smoking;
	}

	/**
	 * @return 飲酒習慣
	 */
	public Drinking getDrinking() {
		return drinking;
	}

	/**
	 * @param drinking 飲酒習慣
	 */
	public void setDrinking(Drinking drinking) {
		this.drinking = drinking;
	}

	/**
	 * @return 添加 Line 好友
	 */
	public String getLineID() {
		return lineID;
	}

	/**
	 * @param lineID 添加 Line 好友
	 */
	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	/**
	 * @return 理想型
	 */
	public String getIdealType() {
		return idealType;
	}

	/**
	 * @param idealType 理想型
	 */
	public void setIdealType(String idealType) {
		this.idealType = idealType;
	}

	/**
	 * @return 活躍
	 */
	public Date getActive() {
		return active;
	}

	/**
	 * @param active 活躍
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
}
