package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 零用錢(女)
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "ling_yong_qian", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ling_yong_qian"})
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Allowance implements java.io.Serializable {

        private static final long serialVersionUID = 4741242805941995659L;

        @Basic(optional = false)
        @Column(nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Short id;

        @Basic(optional = false)
        @Column(name = "ling_yong_qian", nullable = false)
        private String name;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "allowance")
        @JsonBackReference
        private Collection<Lover> lovers;

        /**
         * 默认构造器
         */
        public Allowance() {
        }

        /**
         * @param name 零用錢(i18n)
         */
        public Allowance(String name) {
                this.name = name;
        }

        @Override
        public int hashCode() {
                int hash = 5;
                hash = 53 * hash + Objects.hashCode(this.id);
                return hash;
        }

        @Override
        public boolean equals(Object object) {
                if (!(object instanceof Allowance)) {
                        return false;
                }
                Allowance other = (Allowance) object;
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
        public Short getId() {
                return id;
        }

        /**
         * @param id 主键
         */
        public void setId(Short id) {
                this.id = id;
        }

        /**
         * @return 零用錢(i18n)
         */
        public String getName() {
                return name;
        }

        /**
         * @param name 零用錢(i18n)
         */
        public void setName(String name) {
                this.name = name;
        }

        /**
         * @return 情人們
         */
        public Collection<Lover> getLovers() {
                return lovers;
        }

        /**
         * @param lovers 情人們
         */
        public void setLovers(Collection<Lover> lovers) {
                this.lovers = lovers;
        }
}
