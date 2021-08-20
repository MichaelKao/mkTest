package tw.musemodel.dingzhiqingren.specification;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History_;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.HistoryService;

/**
 * @author p@musemodel.tw
 */
public class HistorySpecification {

	private final static Logger LOGGER = LoggerFactory.getLogger(HistorySpecification.class);

	/**
	 * 聊天相关行为
	 *
	 * @param root
	 * @param criteriaBuilder
	 * @return
	 */
	private static Predicate conversationBehaviors(Root<History> root, CriteriaBuilder criteriaBuilder) {
		Predicate predicate = criteriaBuilder.conjunction();
		Collection<Predicate> predicates = new ArrayList<>();

		predicates.add(root.
			get(History_.behavior).
			in(HistoryService.BEHAVIORS_OF_CONVERSATIONS)
		);

		predicate.getExpressions().addAll(predicates);
		return predicate;
	}

	/**
	 * 两人之间的对话
	 *
	 * @param mofo1
	 * @param mofo2
	 * @return
	 */
	public static Specification<History> conversationsOfTwo(Lover mofo1, Lover mofo2) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.
				orderBy(
					criteriaBuilder.desc(
						root.get(History_.occurred)
					)
				);//以时戳降幂排序

			return criteriaBuilder.and(
				criteriaBuilder.equal(
					root.get(History_.initiative),
					mofo1
				),
				criteriaBuilder.equal(
					root.get(History_.passive),
					mofo2
				),
				conversationBehaviors(root, criteriaBuilder)//聊天相关行为
			);
		};
	}
}
