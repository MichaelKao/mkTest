package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.AnnualIncome;

/**
 * 数据访问对象：年收入
 *
 * @author m@musemodel.tw
 */
@Repository
public interface AnnualIncomeRepository extends JpaRepository<AnnualIncome, Short> {

	public List<AnnualIncome> findAllByOrderByIdAsc();
}
