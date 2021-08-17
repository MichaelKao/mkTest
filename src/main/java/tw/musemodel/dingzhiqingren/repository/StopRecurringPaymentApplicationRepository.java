package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;

/**
 * 数据访问对象：申请解除定期定额
 *
 * @author p@musemodel.tw
 */
@Repository
public interface StopRecurringPaymentApplicationRepository extends JpaRepository<StopRecurringPaymentApplication, Long> {

	/**
	 * @param applicant 申请人
	 * @return 该用户号解除定期定额的申请
	 */
	public Collection<StopRecurringPaymentApplication> findByApplicantOrderByCreatedAtDesc(Lover applicant);
}
