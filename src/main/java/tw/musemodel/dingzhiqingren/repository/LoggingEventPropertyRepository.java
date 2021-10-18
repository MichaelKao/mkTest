package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.embedded.LoggingEventProperty;
import tw.musemodel.dingzhiqingren.entity.embedded.LoggingEventPropertyPK;

/**
 * @author p@musemodel.tw
 */
@Repository
public interface LoggingEventPropertyRepository extends JpaRepository<LoggingEventProperty, LoggingEventPropertyPK> {
}
