package tw.musemodel.dingzhiqingren.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Country;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 数据访问对象：情人
 *
 * @author p@musemodel.tw
 */
@Repository
public interface LoverRepository extends JpaRepository<Lover, Integer> {

	public long countByCountryAndLogin(Country country, String login);

	public Lover findOneByIdentifier(UUID identifier);
}
