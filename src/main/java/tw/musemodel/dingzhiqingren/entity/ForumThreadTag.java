package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 论坛绪关键字词
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(catalog = "youngme", schema = "public", name = "lun_tan_guan_jian_zi_ci", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"zi_ci"})
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForumThreadTag implements Serializable {

        private static final long serialVersionUID = 2866131732084090451L;

        /**
         * 主键
         */
        @Basic(optional = false)
        @Column(nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Short id;

        /**
         * 字词
         */
        @Basic(optional = false)
        @Column(name = "zi_ci", nullable = false)
        @NotNull
        private String phrase;

        /**
         * 论坛绪标签
         */
        @OneToMany(mappedBy = "forumThreadTag")
        @JsonIgnore
        private Set<ForumThreadHashTag> forumHashTags;

        @Override
        public String toString() {
                try {
                        return new JsonMapper().writeValueAsString(this);
                } catch (JsonProcessingException ignore) {
                        return Objects.isNull(id) ? "null" : id.toString();
                }
        }
}
