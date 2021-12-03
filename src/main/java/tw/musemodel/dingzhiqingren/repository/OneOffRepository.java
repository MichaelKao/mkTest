package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.OneOff;

/**
 * 数据访问对象：一次性
 *
 * @author m@musemodel.tw
 */
@Repository
public interface OneOffRepository extends JpaRepository<OneOff, Integer> {
}
