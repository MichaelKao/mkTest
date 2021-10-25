package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 论坛绪插图
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(catalog = "youngme", schema = "public", name = "lun_tan_cha_tu", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"shi_bie_ma"})
})
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForumThreadIllustration implements Serializable {

	private static final long serialVersionUID = -1692713227316006622L;

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
	 * 论坛绪
	 */
	@JoinColumn(name = "lun_tan", nullable = false, referencedColumnName = "id")
	@ManyToOne(optional = false)
	@JsonManagedReference
	private ForumThread forumThread;

	/**
	 * 默认构造器
	 */
	public ForumThreadIllustration() {
		identifier = UUID.randomUUID();
	}

	/**
	 * @param identifier 识别码
	 * @param forumThread 论坛绪
	 */
	public ForumThreadIllustration(UUID identifier, ForumThread forumThread) {
		this.identifier = identifier;
		this.forumThread = forumThread;
	}

	/**
	 * @param forumThread 论坛绪
	 */
	public ForumThreadIllustration(ForumThread forumThread) {
		this();
		this.forumThread = forumThread;
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
