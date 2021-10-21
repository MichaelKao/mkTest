package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.ForumThread;

/**
 * 数据访问对象：论坛绪
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>, JpaSpecificationExecutor<ForumThread> {

	public Page<ForumThread> findByIdIn(List<Long> id, Pageable pageable);

	/**
	 * @param identifier 识别码
	 * @return 论坛绪
	 */
	public ForumThread findOneByIdentifier(UUID identifier);
}
