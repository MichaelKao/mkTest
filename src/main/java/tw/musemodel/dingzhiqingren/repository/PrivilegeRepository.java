package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Privilege;
import tw.musemodel.dingzhiqingren.entity.PrivilegeKey;
import tw.musemodel.dingzhiqingren.entity.Role;

/**
 * 数据访问对象：特权
 *
 * @author p@musemodel.tw
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, PrivilegeKey> {

        /**
         * @param mofo 某咪郎
         * @return 某咪郎的身份
         */
        public Collection<Privilege> findByLover(@Param("lover") Lover mofo);

        /**
         * @param role 身份
         * @return 哪些人有此身份
         */
        public Collection<Privilege> findByRole(Role role);

        public Optional<Privilege> findOneByLoverAndRole(@Param("lover") Lover mofo, Role role);
}
