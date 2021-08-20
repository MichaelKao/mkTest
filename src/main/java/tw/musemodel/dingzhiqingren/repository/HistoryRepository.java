package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	/**
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @param behaviors 行为们
	 * @return 符合条件的历程数量
	 */
	public long countByInitiativeAndPassiveAndBehaviorIn(Lover initiative, Lover passive, Collection<Behavior> behaviors);

	public Long countByInitiativeAndPassiveAndBehavior(Lover initiative, Lover passive, Behavior behavior);

	public Long countByInitiativeAndBehaviorAndOccurredBetween(Lover initiative, Behavior behavior, Date occurredSince, Date occurredUntil);

	public int countByInitiativeAndPassiveAndBehaviorAndOccurredBetween(Lover initiative, Lover passive, Behavior behavior, Date occurredSince, Date occurredUntil);

	public Long countByInitiativeAndPassiveAndBehaviorAndReplyNotNull(Lover initiative, Lover passive, Behavior behavior);

	public int countByInitiative(Lover initiative);

	@Query("SELECT SUM(h.points) FROM History h WHERE h.initiative = :initiative")
	public Long sumByInitiativeHearts(Lover initiative);

	@Query("SELECT SUM(h.points) FROM History h WHERE h.passive = :passive AND h.behavior = :behavior")
	public Long sumHeartsByPassiveAndBehavior(Lover passive, Behavior behavior);

	/**
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @param behaviors 行为们
	 * @param id 主键
	 * @return 符合条件的历程们
	 */
	public List<History> findByInitiativeAndPassiveAndBehaviorInAndIdNotOrderByOccurredDesc(Lover initiative, Lover passive, Collection<Behavior> behaviors, Long id);

	/**
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @param behaviors 行为们
	 * @return 符合条件的历程们
	 */
	public List<History> findByInitiativeAndPassiveAndBehaviorInOrderByOccurredDesc(Lover initiative, Lover passive, Collection<Behavior> behaviors);

	public List<History> findByPassiveAndBehaviorAndOccurredBefore(Lover passive, Behavior behavior, Date occurred);

	public List<History> findByInitiativeAndBehaviorNot(Lover initiative, Behavior behavior);

	public List<History> findByPassiveAndBehaviorNot(Lover passive, Behavior behavior);

	/**
	 * @param initiative 主动方
	 * @param passive 被动方
	 * @param behaviors 行为们
	 * @return 符合条件的历程
	 */
	public History findTop1ByInitiativeAndPassiveAndBehaviorInOrderByOccurredDesc(Lover initiative, Lover passive, Collection<Behavior> behaviors);

	public History findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(Lover initiative, Lover passive, Behavior behavior);

	public History findTop1ByInitiativeAndBehaviorOrderByIdDesc(Lover initiative, Behavior behavior);

	public History findByInitiativeAndPassiveAndBehavior(Lover initiative, Lover passive, Behavior behavior);

	public List<History> findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(Lover initiative, Lover passive, Behavior behavior);

	public List<History> findByInitiativeAndBehaviorAndOccurredGreaterThanOrderByOccurredDesc(Lover initiative, Behavior behavior, Date occurred);

	@Query("SELECT h1 FROM History h1"
		+ " WHERE EXISTS (SELECT h2.passive, max(h2.occurred) FROM History h2"
		+ " WHERE behavior IN :behaviors"
		+ " AND initiative = :initiative"
		+ " GROUP by h2.passive"
		+ " HAVING h1.passive = h2.passive and h1.occurred = max(h2.occurred))"
		+ " ORDER BY h1.occurred DESC")
	public List<History> findReferencedInitiative(Lover initiative, @Param("behaviors") List<Behavior> behaviors);

	@Query("SELECT h1 FROM History h1"
		+ " WHERE EXISTS (SELECT h2.initiative, max(h2.occurred) FROM History h2"
		+ " WHERE behavior IN :behaviors"
		+ " AND passive = :passive"
		+ " GROUP by h2.initiative"
		+ " HAVING h1.initiative = h2.initiative and h1.occurred = max(h2.occurred))"
		+ " ORDER BY h1.occurred DESC")
	public List<History> findReferencedPassive(Lover passive, @Param("behaviors") List<Behavior> behaviors);
}
