package tw.musemodel.dingzhiqingren.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
public interface HistoryRepository extends JpaRepository<History, Long>, JpaSpecificationExecutor<History> {

	public List<History> findByPassiveAndBehaviorOrderByOccurredDesc(Lover passive, Behavior behavior);

	public Long countByInitiativeAndPassiveAndBehavior(Lover initiative, Lover passive, Behavior behavior);

	public Long countByInitiativeAndBehaviorAndOccurredBetween(Lover initiative, Behavior behavior, Date occurredSince, Date occurredUntil);

	public int countByInitiativeAndPassiveAndBehaviorAndOccurredBetween(Lover initiative, Lover passive, Behavior behavior, Date occurredSince, Date occurredUntil);

	public Long countByInitiativeAndPassiveAndBehaviorAndReplyNotNull(Lover initiative, Lover passive, Behavior behavior);

	public int countByInitiative(Lover initiative);

	@Query("SELECT SUM(h.points) FROM History h WHERE h.initiative = :initiative")
	public Long sumByInitiativeHearts(Lover initiative);

	@Query("SELECT SUM(h.points) FROM History h WHERE h.passive = :passive AND h.behavior = :behavior")
	public Long sumHeartsByPassiveAndBehavior(Lover passive, Behavior behavior);

	public List<History> findByPassiveAndBehaviorAndOccurredBefore(Lover passive, Behavior behavior, Date occurred);

	public List<History> findByInitiativeAndBehaviorNot(Lover initiative, Behavior behavior);

	public List<History> findByPassiveAndBehaviorNot(Lover passive, Behavior behavior);

	public History findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(Lover initiative, Lover passive, Behavior behavior);

	public History findTop1ByInitiativeAndBehaviorOrderByIdDesc(Lover initiative, Behavior behavior);

	public History findByInitiativeAndPassiveAndBehavior(Lover initiative, Lover passive, Behavior behavior);

	public History findByInitiativeAndPassiveAndBehaviorAndReplyNull(Lover initiative, Lover passive, Behavior behavior);

	public List<History> findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(Lover initiative, Lover passive, Behavior behavior);

	public List<History> findByInitiativeAndBehaviorAndOccurredGreaterThanOrderByOccurredDesc(Lover initiative, Behavior behavior, Date occurred);
}
