package tw.musemodel.dingzhiqingren.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query("SELECT l FROM Lover l WHERE l.gender = :gender AND l.delete IS NULL")
	public List<Lover> findByGender(Boolean gender);

	/**
	 * @param identifier 识别码
	 * @return 情人
	 */
	public Optional<Lover> findByIdentifier(UUID identifier);

	public Lover findByReferralCode(String referralCode);

	/**
	 * 找下线用户。
	 *
	 * @param referrer 上线用户
	 * @return 下线用户号们
	 */
	public List<Lover> findByReferrerOrderByRegisteredDesc(Lover referrer);

	/**
	 * 某段时间区间内注册的用户号。
	 *
	 * @param since 开始时戳
	 * @param until 结束时戳
	 * @return 用户号们
	 */
	public List<Lover> findByRegisteredBetweenOrderByRegisteredDesc(Date since, Date until);

	public List<Lover> findByRelief(Boolean relief);

	public Lover findOneByIdentifier(UUID identifier);

	public Page<Lover> findAllByGenderOrderByIdAsc(Boolean gender, Pageable pageable);
}
