package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
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
import lombok.Data;

/**
 * 论坛绪标签
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(catalog = "youngme", schema = "public", name = "lun_tan_biao_qian", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"guan_jian_zi_ci", "lun_tan"})})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForumThreadHashTag implements Serializable {

        private static final long serialVersionUID = -8918268739628447596L;

        @Basic(optional = false)
        @Column(nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Long id;

        /**
         * 关键字词
         */
        @JoinColumn(name = "guan_jian_zi_ci", referencedColumnName = "id", nullable = false)
        @ManyToOne(optional = false)
        private ForumThreadTag forumThreadTag;

        /**
         * 论坛绪
         */
        @JoinColumn(name = "lun_tan", referencedColumnName = "id", nullable = false)
        @ManyToOne(optional = false)
        private ForumThread forumThread;

        /**
         * @param identifier 识别码
         * @param forumThread 论坛绪
         */
        public ForumThreadHashTag(ForumThreadTag forumThreadTag, ForumThread forumThread) {
                this.forumThreadTag = forumThreadTag;
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
