package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.TrialCode;

/**
 * 数据访问对象：体验码
 *
 * @author p@musemodel.tw
 */
@Repository
public interface TrialCodeRepository extends JpaRepository<TrialCode, Short> {
}
