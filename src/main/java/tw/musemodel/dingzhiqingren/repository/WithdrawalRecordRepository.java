package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;

/**
 * 数据访问对象：甜心提領車馬費紀錄
 *
 * @author m@musemodel.tw
 */
@Repository
public interface WithdrawalRecordRepository extends JpaRepository<WithdrawalRecord, Integer> {

	@Query("SELECT SUM(wr.points) FROM WithdrawalRecord wr WHERE wr.honey = :honey AND (wr.status = null OR wr.status = 'true')")
	public Long sumPoinsByHoney(Lover honey);

	public List<WithdrawalRecord> findAllByHoneyOrderByTimestampDesc(Lover honey);

	public List<WithdrawalRecord> findAllByOrderByTimestampDesc();
}
