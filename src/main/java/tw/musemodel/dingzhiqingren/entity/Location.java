package tw.musemodel.dingzhiqingren.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
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
import tw.musemodel.dingzhiqingren.entity.embedded.AppearedLocation;

/**
 * 地区
 *
 * @author m@musemodel.tw
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
@Table(name = "di_qu", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"xian_shi_ming"})
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements java.io.Serializable {

        private static final long serialVersionUID = -8980376544581458604L;

        /**
         * 主键
         */
        @Basic(optional = false)
        @Column(nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Short id;

        /**
         * 县市名(i18n 键)
         */
        @Basic(optional = false)
        @Column(name = "xian_shi_ming", nullable = false)
        private String name;

        /**
         * 此地区所在的养蜜
         */
        @OneToMany(cascade = CascadeType.ALL, mappedBy = "residence")
        @JsonBackReference
        private Collection<Lover> residents;

        /**
         * 哪些养蜜出没
         */
        @OneToMany(mappedBy = "location")
        @JsonIgnore
        private Set<AppearedLocation> lovers;

        /**
         * 默认构造器
         */
        public Location() {
        }

        /**
         * @param name 县市名(i18n 键)
         */
        public Location(String name) {
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
                if (!(object instanceof Location)) {
                        return false;
                }
                Location other = (Location) object;
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
         * @return 县市名(i18n 键)
         */
        public String getName() {
                return name;
        }

        /**
         * @param name 县市名(i18n 键)
         */
        public void setName(String name) {
                this.name = name;
        }

        /**
         * @return 居住在此地的养蜜
         */
        public Collection<Lover> getResidents() {
                return residents;
        }

        /**
         * @param residents 居住在此地的养蜜
         */
        public void setResidents(Collection<Lover> residents) {
                this.residents = residents;
        }

        /**
         * @return 此地区出没的养蜜
         */
        public Set<AppearedLocation> getLovers() {
                return lovers;
        }

        /**
         * @param lovers 此地区出没的养蜜
         */
        public void setLovers(Set<AppearedLocation> lovers) {
                this.lovers = lovers;
        }
}
