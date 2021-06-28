package tw.musemodel.dingzhiqingren.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 数据访问对象：历程
 *
 * @author p@musemodel.tw
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

	public List<History> findByPassiveAndBehavior(Lover passive, Behavior behavior);

	public Long countByInitiativeAndPassiveAndBehavior(Lover initiative, Lover passive, Behavior behavior);

	public Long countByInitiativeAndBehaviorAndOccurredBetween(Lover initiative, Behavior behavior, Date occurredSince, Date occurredUntil);

	@Query("SELECT SUM(h.points) FROM History h WHERE h.initiative = :initiative")
	public Long sumByInitiative(Lover initiative);
}
