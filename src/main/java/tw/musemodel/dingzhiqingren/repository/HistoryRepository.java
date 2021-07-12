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

	public int countByInitiative(Lover initiative);

	@Query("SELECT COUNT(h.id) FROM History h WHERE h.passive = :passive AND h.behavior != :behavior1 AND h.behavior != :behavior2 AND h.seen is null")
	public int countByFemalePassive(Lover passive, Behavior behavior1, Behavior behavior2);

	@Query("SELECT COUNT(h.id) FROM History h WHERE h.passive = :passive AND h.behavior != :behavior1 OR ( h.initiative = :passive AND h.behavior = :behavior2 ) AND h.seen is null")
	public int countByPassive(Lover passive, Behavior behavior1, Behavior behavior2);

	@Query("SELECT SUM(h.points) FROM History h WHERE h.initiative = :initiative")
	public Long sumByInitiativeHearts(Lover initiative);

	@Query("SELECT SUM(h.points) FROM History h WHERE h.passive = :passive AND h.behavior = :behavior")
	public Long sumByPassiveAndBehaviorHearts(Lover passive, Behavior behavior);

	public List<History> findByInitiativeAndBehaviorNot(Lover initiative, Behavior behavior);

	public List<History> findByPassiveAndBehaviorNot(Lover passive, Behavior behavior);

	public History findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(Lover initiative, Lover passive, Behavior behavior);
}
