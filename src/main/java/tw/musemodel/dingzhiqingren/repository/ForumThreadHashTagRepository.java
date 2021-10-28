package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.entity.ForumThreadHashTag;

/**
 * 数据访问对象：论坛绪标签
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ForumThreadHashTagRepository extends JpaRepository<ForumThreadHashTag, Long> {

        public List<ForumThreadHashTag> findByForumThread(ForumThread forumThread);
}
