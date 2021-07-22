package tw.musemodel.dingzhiqingren.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.model.EachWithdrawal;

/**
 * 数据访问对象：甜心提領車馬費紀錄
 *
 * @author m@musemodel.tw
 */
@Repository
public interface WithdrawalRecordRepository extends JpaRepository<WithdrawalRecord, Integer> {

	@Query("SELECT SUM(wr.points) FROM WithdrawalRecord wr WHERE wr.honey = :honey AND (wr.status = 'false' OR wr.status = 'true')")
	public Long sumPoinsByHoney(Lover honey);

	public List<WithdrawalRecord> findAllByHoneyAndStatusOrderByTimestampDesc(Lover honey, Boolean status);

	public List<WithdrawalRecord> findByHoneyAndStatusAndTimestamp(Lover honey, Boolean status, Date timestamp);

	@Transactional
	@Modifying
	@Query("DELETE FROM WithdrawalRecord wr WHERE wr.honey = :honey AND wr.status = :status AND wr.timestamp = :timestamp")
	public void deleteByHoneyAndStatusAndTimestamp(Lover honey, Boolean status, Date timestamp);

	@Query("SELECT new tw.musemodel.dingzhiqingren.model.EachWithdrawal(wr.honey, SUM(wr.points), wr.status, wr.way, wr.timestamp) FROM WithdrawalRecord wr GROUP BY wr.honey, wr.status, wr.way, wr.timestamp")
	public List<EachWithdrawal> findAllGroupByHoneyAndStatusAndWayAndTimeStamp();

	@Query("SELECT new tw.musemodel.dingzhiqingren.model.EachWithdrawal(wr.honey, SUM(wr.points), wr.status, wr.way, wr.timestamp) FROM WithdrawalRecord wr WHERE wr.honey = :honey GROUP BY wr.honey, wr.status, wr.way, wr.timestamp")
	public List<EachWithdrawal> findHoneyAllGroupByHoneyAndStatusAndWayAndTimeStamp(Lover honey);
}
