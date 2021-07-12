package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.ServiceTag;

/**
 * 数据访问对象：服務
 *
 * @author m@musemodel.tw
 */
@Repository
public interface ServiceTagRepository extends JpaRepository<ServiceTag, Short> {
}
