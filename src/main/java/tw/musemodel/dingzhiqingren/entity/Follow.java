package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

/**
 * 追踪(收藏)谁、被追踪(收藏)
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "shou_cang", uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"shou_cang_de",
		"bei_shou_cang"
	})
})
public class Follow implements Serializable {

	private static final long serialVersionUID = 3839190022651998640L;

	/**
	 * 主键
	 */
	@EmbeddedId
	private FollowKey id;

	/**
	 * 追踪(收藏)谁
	 */
	@ManyToOne
	@MapsId("followingId")
	@JoinColumn(name = "shou_cang_de")
	private Lover following;

	/**
	 * 被追踪(收藏)
	 */
	@ManyToOne
	@MapsId("followedId")
	@JoinColumn(name = "bei_shou_cang")
	private Lover followed;

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(id) ? "null" : id.toString();
		}
	}
}
