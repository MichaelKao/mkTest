package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 论坛绪
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(catalog = "youngme", schema = "public", name = "lun_tan", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"shi_bie_ma"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForumThread implements Serializable {

	private static final long serialVersionUID = 7521465629770527419L;

	/**
	 * 主键
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	/**
	 * 识别码
	 */
	@Basic(optional = false)
	@Column(name = "shi_bie_ma", nullable = false)
	@NotNull
	private UUID identifier;

	/**
	 * 时戳
	 */
	@Basic(optional = false)
	@Column(name = "shi_chuo", nullable = false)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	/**
	 * 性别
	 */
	@Basic(optional = false)
	@Column(name = "xing_bie", nullable = false)
	@NotNull
	private boolean gender;

	/**
	 * 作者
	 */
	@JoinColumn(name = "zuo_zhe", nullable = false, referencedColumnName = "id")
	@ManyToOne
	private Lover author;

	/**
	 * 标题
	 */
	@Basic(optional = false)
	@Column(name = "biao_ti", nullable = false)
	@NotNull
	private String title;

	/**
	 * 内容
	 */
	@Column(name = "nei_rong")
	private String content;

	/**
	 * 论坛绪插图
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "forumThread")
	@JsonBackReference
	private Collection<ForumThreadIllustration> forumThreadIllustrations;

	/**
	 * 论坛绪留言
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "forumThread")
	@JsonBackReference
	private Collection<ForumThreadComment> forumThreadComments;

	/**
	 * 论坛绪标签
	 */
	@OneToMany(mappedBy = "forumThread")
	@JsonIgnore
	private Set<ForumThreadHashTag> forumHashTags;

	/**
	 * 默认构造器
	 */
	public ForumThread() {
	}

	/**
	 * @param id 主键
	 */
	protected ForumThread(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
