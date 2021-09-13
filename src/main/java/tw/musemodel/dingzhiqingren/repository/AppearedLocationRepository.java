package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.embedded.AppearedLocation;
import tw.musemodel.dingzhiqingren.entity.embedded.AppearedLocationKey;

/**
 * 数据访问对象：出没地区
 *
 * @author p@musemodel.tw
 */
@Repository
public interface AppearedLocationRepository extends JpaRepository<AppearedLocation, AppearedLocationKey> {

	/**
	 * @param location 地区
	 * @return 某地区的出没情人
	 */
	public Collection<AppearedLocation> findByLocation(Location location);

	/**
	 * @param mofo 情人
	 * @return 某情人的出没地区
	 */
	public Collection<AppearedLocation> findByLover(@Param("lover") Lover mofo);

	/**
	 * @param mofo 情人
	 * @param location 地区
	 * @return 出没地区
	 */
	public Optional<AppearedLocation> findOneByLoverAndLocation(@Param("lover") Lover mofo, Location location);
}
