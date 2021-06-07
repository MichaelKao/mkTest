package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Role;

/**
 * 数据访问对象：身份
 *
 * @author p@musemodel.tw
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Short> {

	public Role findOneByTextualRepresentation(String textualRepresentation);
}
