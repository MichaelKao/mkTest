package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.embedded.LoggingEventException;
import tw.musemodel.dingzhiqingren.entity.embedded.LoggingEventExceptionPK;

/**
 * @author p@musemodel.tw
 */
@Repository
public interface LoggingEventExceptionRepository extends JpaRepository<LoggingEventException, LoggingEventExceptionPK> {
}
