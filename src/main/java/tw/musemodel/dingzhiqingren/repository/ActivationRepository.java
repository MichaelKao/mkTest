package tw.musemodel.dingzhiqingren.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Activation;

/**
 * 数据存取对象：激活
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ActivationRepository extends JpaRepository<Activation, Integer> {

	/**
	 * @param string 字符串
	 * @return 计数
	 */
	public long countByString(String string);

	/**
	 * @param string 字符串
	 * @return 激活
	 */
	public Optional<Activation> findOneByString(String string);
}
