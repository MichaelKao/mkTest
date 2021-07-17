package tw.musemodel.dingzhiqingren;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 *
 * @author m@musemodel.tw
 */
public class Specifications {

	private final static Logger LOGGER = LoggerFactory.getLogger(Specifications.class);

	/**
	 * 根據行為通知被動者
	 *
	 * @param passive
	 * @param behaviors
	 * @return
	 */
	public static Specification<History> passive(Lover passive, List<Behavior> behaviors) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.orderBy(criteriaBuilder.desc(
				root.get("occurred"))
			);

			Root<History> historyRoot = root;
			List<Predicate> predicates = new ArrayList<>();

			for (Behavior behavior : behaviors) {
				predicates.add(
					criteriaBuilder.equal(
						historyRoot.get("behavior"),
						behavior
					)
				);
			}

			return criteriaBuilder.and(
				criteriaBuilder.equal(
					root.get("passive"),
					passive
				),
				criteriaBuilder.or(
					predicates.toArray(new Predicate[0])
				),
				criteriaBuilder.isNull(
					root.get("seen")
				)
			);
		};
	}
}
