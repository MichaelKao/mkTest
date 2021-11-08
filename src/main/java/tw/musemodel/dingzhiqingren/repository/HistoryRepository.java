package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Activity;

/**
 * 数据访问对象：历程
 *
 * @author p@musemodel.tw
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, JpaSpecificationExecutor<History> {

        public List<History> findByPassiveAndBehaviorOrderByOccurredDesc(Lover passive, Behavior behavior);

        public Page<History> findByPassiveAndBehaviorOrderByOccurredDesc(Lover passive, Behavior behavior, Pageable pageable);

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
        public Long sumByPassiveAndBehaviorHearts(Lover passive, Behavior behavior);

        /**
         * @author p@usemodel.tw
         * @param behaviors 行为们
         * @param pageable 可分页
         * @return 符合行为们的历程们
         */
        public List<History> findByBehaviorInOrderByOccurredDesc(Collection<Behavior> behaviors, Pageable pageable);

        public List<History> findByBehaviorInOrderByOccurredDesc(Collection<Behavior> behaviors);

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

        public List<History> findByInitiativeAndBehaviorNotInOrderByIdDesc(Lover initiative, Collection<Behavior> behaviors);

        public List<History> findByPassiveAndBehaviorNotInOrderByIdDesc(Lover passive, Collection<Behavior> behaviors);

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

        public int countByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(Lover initiative, Lover passive, Collection<Behavior> behaviors);

        public List<History> findByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(Lover initiative, Lover passive, Collection<Behavior> behaviors);

        public int countByInitiativeAndBehaviorOrderByOccurredDesc(Lover initiative, Behavior behavior);

        public History findByBehaviorAndHistory(Behavior behavior, History history);

        @Query("SELECT new tw.musemodel.dingzhiqingren.model.Activity(h.initiative, h.behavior, h.occurred) "
                + "FROM History h WHERE h.initiative = :initiative AND h.behavior = :behavior AND h.occurred >= :occurred "
                + "GROUP BY h.initiative, h.behavior, h.occurred "
                + "ORDER BY h.occurred DESC")
        public List<Activity> findGroupGreetingListGroupBy(Lover initiative, Behavior behavior, Date occurred);

        public List<History> findByInitiativeAndBehaviorAndOccurred(Lover initiative, Behavior behavior, Date occurred);

        public List<History> findByBehaviorOrderByOccurredDesc(Behavior behavior);

        @Query("SELECT DISTINCT h.passive "
                + "FROM History h "
                + "WHERE h.initiative = :initiative "
                + "AND h.behavior = :behavior "
                + "AND h.occurred >= :occurred1 "
                + "AND h.occurred <= :occurred2 ")
        public List<Lover> findDistinctPassive(Lover initiative, Behavior behavior, Date occurred1, Date occurred2);

        public History findTop1ByInitiativeAndBehaviorOrderByOccurredDesc(Lover initiative, Behavior behavior);

        public List<History> findByInitiativeAndBehaviorInOrderByOccurredDesc(Lover initiative, Collection<Behavior> behaviors);
}
