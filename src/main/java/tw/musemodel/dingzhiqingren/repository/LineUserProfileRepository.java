package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.LineUserProfile;

/**
 * 数据访问对象：LINE User Profile
 *
 * @author p@musemodel.tw
 */
@Repository
public interface LineUserProfileRepository extends JpaRepository<LineUserProfile, Integer> {
}
