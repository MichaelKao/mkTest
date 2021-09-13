package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.embedded.Follow;
import tw.musemodel.dingzhiqingren.entity.embedded.FollowKey;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 数据访问对象：追踪(收藏)谁、被追踪(收藏)
 *
 * @author p@musemodel.tw
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowKey> {

	/**
	 * @param mofo 某咪郎
	 * @return 被谁追踪(收藏)
	 */
	public Collection<Follow> findByFollowed(@Param("followed") Lover mofo);

	/**
	 * @param mofo 某咪郎
	 * @return 追踪(收藏)了谁
	 */
	public Collection<Follow> findByFollowing(@Param("following") Lover mofo);
}
