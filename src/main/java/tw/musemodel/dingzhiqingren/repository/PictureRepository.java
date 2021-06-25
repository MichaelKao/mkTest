package tw.musemodel.dingzhiqingren.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Picture;

/**
 * 數據存取對象：生活照
 *
 * @author m@musemodel.tw
 */
@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {

	public List<Picture> findByLover(Lover lover);

	public Picture findOneByIdentifier(UUID identifier);
}
