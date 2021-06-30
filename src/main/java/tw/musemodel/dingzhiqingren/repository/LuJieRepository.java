package tw.musemodel.dingzhiqingren.repository;

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

	public LuJie findTop1BySessionIdOrderByIdDesc(String sessionId);
}
