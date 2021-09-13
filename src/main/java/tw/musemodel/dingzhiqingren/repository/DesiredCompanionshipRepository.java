package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Companionship;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.embedded.DesiredCompanionship;
import tw.musemodel.dingzhiqingren.entity.embedded.DesiredCompanionshipKey;

/**
 * 数据访问对象：期望的陪伴
 *
 * @author p@musemodel.tw
 */
@Repository
public interface DesiredCompanionshipRepository extends JpaRepository<DesiredCompanionship, DesiredCompanionshipKey> {

	/**
	 * @param mofo 某咪郎
	 * @return 期望哪些陪伴
	 */
	public Collection<DesiredCompanionship> findByLover(@Param("lover") Lover mofo);

	/**
	 * @param companionship 友谊
	 * @return 哪些期望陪伴
	 */
	public Collection<DesiredCompanionship> findByCompanionship(Companionship companionship);

	/**
	 * @param mofo
	 * @param companionship 友谊
	 * @return 期望陪伴
	 */
	public Optional<DesiredCompanionship> findOneByLoverAndCompanionship(@Param("lover") Lover mofo, Companionship companionship);
}
