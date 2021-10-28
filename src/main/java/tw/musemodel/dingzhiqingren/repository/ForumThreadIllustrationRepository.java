package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.entity.ForumThreadIllustration;

/**
 * 数据访问对象：论坛绪插图
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ForumThreadIllustrationRepository extends JpaRepository<ForumThreadIllustration, Long> {

        public Collection<ForumThreadIllustration> findByForumThread(ForumThread forumThread);
}
