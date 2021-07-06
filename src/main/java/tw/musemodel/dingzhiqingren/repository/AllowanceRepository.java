package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Allowance;

/**
 * 数据访问对象：零用錢
 *
 * @author m@musemodel.tw
 */
@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Short> {

	public List<Allowance> findAllByOrderByIdAsc();
}
