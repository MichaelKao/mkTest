package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.WithdrawalInfo;

/**
 * 数据访问对象：甜心提領車馬費方式
 *
 * @author m@musemodel.tw
 */
@Repository
public interface WithdrawalInfoRepository extends JpaRepository<WithdrawalInfo, Integer> {

	public int countByHoney(Lover honey);

	public WithdrawalInfo findByHoney(Lover honey);
}
