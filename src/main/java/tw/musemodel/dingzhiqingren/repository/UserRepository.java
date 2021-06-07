package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.User;

/**
 * 数据访问对象：用户
 *
 * @author p@musemodel.tw
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findOneByUsername(@Param("username") String username);
}
