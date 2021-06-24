package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Location;

/**
 * 数据访问对象：地區
 *
 * @author m@musemodel.tw
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Short> {
}
