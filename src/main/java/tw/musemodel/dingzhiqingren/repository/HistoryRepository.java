package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 数据访问对象：历程
 *
 * @author p@musemodel.tw
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

	@Query("SELECT SUM(h.points) FROM History h WHERE h.initiative = :initiative")
	public Long sumByInitiative(Lover initiative);
}
