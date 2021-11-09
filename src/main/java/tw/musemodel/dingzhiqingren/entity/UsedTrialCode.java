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
 * 使用的網紅體驗碼
 *
 * @author m@musemodel.tw
 */
@Data
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(catalog = "youngme", schema = "public", name = "shi_yong_ti_yan_ma", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"qing_ren", "ti_yan_ma"})})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedTrialCode implements Serializable {

        private static final long serialVersionUID = -7976268786428447596L;

        @Basic(optional = false)
        @Column(nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Long id;

        /**
         * 情人
         */
        @JoinColumn(name = "qing_ren", referencedColumnName = "id", nullable = false)
        @ManyToOne(optional = false)
        private Lover lover;

        /**
         * 體驗碼
         */
        @JoinColumn(name = "ti_yan_ma", referencedColumnName = "id", nullable = false)
        @ManyToOne(optional = false)
        private TrialCode trialCode;

        /**
         * 預設建構子
         */
        public UsedTrialCode() {
        }

        /**
         *
         * @param lover
         * @param trialCode
         */
        public UsedTrialCode(Lover lover, TrialCode trialCode) {
                this.lover = lover;
                this.trialCode = trialCode;
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
