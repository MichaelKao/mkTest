package tw.musemodel.dingzhiqingren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.ForumThreadIllustration;

/**
 * 数据访问对象：论坛绪插图
 *
 * @author p@musemodel.tw
 */
@Repository
public interface ForumThreadIllustrationRepository extends JpaRepository<ForumThreadIllustration, Long> {
}
