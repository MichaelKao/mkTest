package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Companionship;

/**
 * 数据访问对象：友谊
 *
 * @author m@musemodel.tw
 */
@Repository
public interface CompanionshipRepository extends JpaRepository<Companionship, Short> {
}
