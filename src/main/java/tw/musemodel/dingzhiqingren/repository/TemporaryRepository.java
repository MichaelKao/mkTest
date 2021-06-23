package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Temporary;

/**
 */
@Repository
public interface TemporaryRepository extends JpaRepository<Temporary, Long> {

	public Temporary findTop1BySessionIdOrderByIdDesc(String sessionId);
}
