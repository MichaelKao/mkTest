package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.embedded.AccessEventHeader;
import tw.musemodel.dingzhiqingren.entity.embedded.AccessEventHeaderPK;

/**
 * @author p@musemodel.tw
 */
@Repository
public interface AccessEventHeaderRepository extends JpaRepository<AccessEventHeader, AccessEventHeaderPK> {
}
