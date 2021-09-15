package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.embedded.Blacklist;
import tw.musemodel.dingzhiqingren.entity.embedded.BlacklistKey;

/**
 * 数据访问对象：拉黑
 *
 * @author p@musemodel.tw
 */
@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, BlacklistKey> {

	/**
	 * @param blocker 某咪郎
	 * @return 某咪郎拉黑了谁
	 */
	public Collection<Blacklist> findByBlocker(Lover blocker);

	/**
	 * @param blocked 某咪郎
	 * @return 谁拉黑了某咪郎
	 */
	public Collection<Blacklist> findByBlocked(Lover blocked);

	/**
	 * @param blocker 某咪郎
	 * @param blocked 被拉黑的另几咧郎
	 * @return 拉黑
	 */
	public Optional<Blacklist> findOneByBlockerAndBlocked(Lover blocker, Lover blocked);
}
