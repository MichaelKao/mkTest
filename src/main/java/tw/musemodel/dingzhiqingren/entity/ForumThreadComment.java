package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 论坛绪留言
 *
 * @author p@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(catalog = "youngme", schema = "public", name = "lun_tan_liu_yan", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"shi_bie_ma"})
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForumThreadComment implements Serializable {

        private static final long serialVersionUID = 8682333666479940996L;

        @Basic(optional = false)
        @Column(nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Long id;

        @Basic(optional = false)
        @Column(name = "shi_bie_ma", nullable = false)
        @NotNull
        private UUID identifier;

        @Basic(optional = false)
        @Column(name = "shi_chuo", nullable = false)
        @NotNull
        @Temporal(TemporalType.TIMESTAMP)
        private Date created;

        /**
         * 论坛绪
         */
        @JoinColumn(name = "lun_tan", nullable = false, referencedColumnName = "id")
        @ManyToOne(optional = false)
        @JsonManagedReference
        private ForumThread forumThread;

        /**
         * 评论者
         */
        @JoinColumn(name = "ping_lun_zhe", nullable = false, referencedColumnName = "id")
        @ManyToOne
        private Lover commenter;

        /**
         * 内容
         */
        @Column(name = "nei_rong", nullable = false)
        @NotNull
        private String content;

        /**
         * 默认构造器
         */
        public ForumThreadComment() {
                identifier = UUID.randomUUID();
        }

        /**
         * @param forumThread 论坛绪
         * @param commenter 评论者
         * @param content 内容
         */
        public ForumThreadComment(ForumThread forumThread, Lover commenter, String content) {
                this.forumThread = forumThread;
                this.commenter = commenter;
                this.content = content;
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
