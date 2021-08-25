package tw.musemodel.dingzhiqingren.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.ResetShadow;

/**
 * 数据存取对象：重设密码
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ResetShadowRepository extends JpaRepository<ResetShadow, Integer> {

	/**
	 * @param lover 用户号
	 * @return 计数
	 */
	public long countByLover(Lover lover);

	/**
	 * @param string 字符串
	 * @return 计数
	 */
	public long countByString(String string);

	/**
	 * @param lover 用户号
	 * @return 重设密码
	 */
	public Optional<ResetShadow> findOneByLover(Lover lover);

	/**
	 * @param string 字符串
	 * @return 重设密码
	 */
	public Optional<ResetShadow> findOneByString(String string);
}
