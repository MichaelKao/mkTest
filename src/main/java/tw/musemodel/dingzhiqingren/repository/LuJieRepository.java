package tw.musemodel.dingzhiqingren.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.LuJie;

/**
 * 数据访问对象：绿界
 *
 * @author p@musemodel.tw
 */
@Repository
public interface LuJieRepository extends JpaRepository<LuJie, Long> {

	public long countByMerchantTradeNo(String merchantTradeNo);

	public Optional<LuJie> findOneByMerchantTradeNo(String merchantTradeNo);

	public LuJie findTop1BySessionIdOrderByIdDesc(String sessionId);
}
