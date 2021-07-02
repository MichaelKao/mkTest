package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.LineGivenPK;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 數據存取對象：給不給賴
 *
 * @author m@musemodel.tw
 */
@Repository
public interface LineGivenRepository extends JpaRepository<LineGiven, LineGivenPK> {

	public LineGiven findByFemaleAndMale(Lover female, Lover male);
}
