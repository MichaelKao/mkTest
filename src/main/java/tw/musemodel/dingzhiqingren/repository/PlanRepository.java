package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Plan;

/**
 * 数据访问对象：储值方案
 *
 * @author p@musemodel.tw
 */
@Repository
public interface PlanRepository extends JpaRepository<Plan, Short> {
}
