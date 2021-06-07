package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Country;

/**
 * 数据访问对象：国家
 *
 * @author p@musemodel.tw
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Short> {
}
