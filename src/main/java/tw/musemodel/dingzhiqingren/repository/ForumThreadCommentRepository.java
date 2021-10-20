package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.ForumThreadComment;

/**
 * 数据访问对象：论坛绪留言
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ForumThreadCommentRepository extends JpaRepository<ForumThreadComment, Long> {
}
