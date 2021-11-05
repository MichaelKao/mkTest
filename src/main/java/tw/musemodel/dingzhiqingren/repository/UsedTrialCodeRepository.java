package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.TrialCode;
import tw.musemodel.dingzhiqingren.entity.UsedTrialCode;

/**
 * 数据访问对象：使用的網紅體驗碼
 *
 * @author m@musemodel.tw
 */
@Repository
public interface UsedTrialCodeRepository extends JpaRepository<UsedTrialCode, Short> {

        public int countByTrialCode(TrialCode trialCode);

        public List<UsedTrialCode> findByTrialCode(TrialCode trialCode);
}
