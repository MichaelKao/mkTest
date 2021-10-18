package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.embedded.LoggingEvent;

/**
 * @author p@musemodel.tw
 */
@Repository
public interface LoggingEventRepository extends JpaRepository<LoggingEvent, Long> {
}
