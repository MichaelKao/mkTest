package tw.musemodel.dingzhiqingren.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.LineNotifyAuthentication;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 数据访问对象：透过 LINE 接收网站服务通知
 *
 * @author p@musemodel.tw
 */
@Repository
public interface LineNotifyAuthenticationRepository extends JpaRepository<LineNotifyAuthentication, Integer> {

	public Collection<LineNotifyAuthentication> findBySucker(Lover sucker);

	public Optional<LineNotifyAuthentication> findOneByState(String state);
}
