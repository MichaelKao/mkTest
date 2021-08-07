package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Country;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 数据访问对象：情人
 *
 * @author p@musemodel.tw
 */
@Repository
public interface LoverRepository extends JpaRepository<Lover, Integer>, JpaSpecificationExecutor<Lover> {

	public long countByCountryAndLogin(Country country, String login);

	public long countByReferralCode(String referralCode);

	public Lover findOneByIdentifier(UUID identifier);

	@Query("SELECT l FROM Lover l WHERE l.gender = :gender AND l.delete IS NULL")
	public List<Lover> findByGender(Boolean gender);

	public List<Lover> findByRelief(Boolean relief);

	public Lover findByReferralCode(String referralCode);
}
