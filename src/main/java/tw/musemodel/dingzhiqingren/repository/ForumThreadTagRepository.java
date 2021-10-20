package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.ForumThreadTag;

/**
 * 数据访问对象：论坛绪关键字词
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ForumThreadTagRepository extends JpaRepository<ForumThreadTag, Short> {
}
