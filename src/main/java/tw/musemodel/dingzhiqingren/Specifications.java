package tw.musemodel.dingzhiqingren;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;

/**
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

	/**
	 * 甜心目前可以提領的紀錄
	 *
	 * @param passive
	 * @return
	 */
	public static Specification<History> withdrawal(Lover passive) {
		return (Root<History> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			Root<History> historyRoot = root;
			Subquery<WithdrawalRecord> subquery = criteriaQuery.subquery(
				WithdrawalRecord.class
			);
			Root<WithdrawalRecord> withdrawalRecordRoot = subquery.from(WithdrawalRecord.class);
			criteriaQuery.orderBy(criteriaBuilder.desc(
				historyRoot.get("occurred"))
			);

			List<Predicate> predicates = new ArrayList<>();

			predicates.add(
				criteriaBuilder.equal(
					historyRoot.get("behavior"),
					Behavior.CHE_MA_FEI
				)
			);
			predicates.add(
				criteriaBuilder.and(
					criteriaBuilder.equal(
						historyRoot.get("behavior"),
						Behavior.LAI_KOU_DIAN
					),
					criteriaBuilder.equal(
						historyRoot.get("points"),
						-100
					)
				)
			);

			Calendar cal = Calendar.getInstance();
			cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			Date sevenDaysAgo = cal.getTime();

			return criteriaBuilder.and(
				criteriaBuilder.equal(
					historyRoot.get("passive"),
					passive
				),
				criteriaBuilder.or(
					predicates.toArray(new Predicate[0])
				),
				criteriaBuilder.lessThanOrEqualTo(
					historyRoot.get("occurred"),
					sevenDaysAgo
				),
				criteriaBuilder.not(
					criteriaBuilder.exists(
						subquery.
							select(withdrawalRecordRoot).
							where(
								criteriaBuilder.equal(
									historyRoot.get("id"), withdrawalRecordRoot.get("id")
								)
							)
					)
				)
			);
		};
	}

	/**
	 * 尚未能提領的紀錄
	 *
	 * @param passive
	 * @return
	 */
	public static Specification<History> notAbleTowithdrawal(Lover passive) {
		return (Root<History> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			Root<History> historyRoot = root;
			Subquery<WithdrawalRecord> subquery = criteriaQuery.subquery(
				WithdrawalRecord.class
			);
			Root<WithdrawalRecord> withdrawalRecordRoot = subquery.from(WithdrawalRecord.class);
			criteriaQuery.orderBy(criteriaBuilder.desc(
				historyRoot.get("occurred"))
			);

			List<Predicate> predicates = new ArrayList<>();

			predicates.add(
				criteriaBuilder.equal(
					historyRoot.get("behavior"),
					Behavior.CHE_MA_FEI
				)
			);
			predicates.add(
				criteriaBuilder.and(
					criteriaBuilder.equal(
						historyRoot.get("behavior"),
						Behavior.LAI_KOU_DIAN
					),
					criteriaBuilder.equal(
						historyRoot.get("points"),
						-100
					)
				)
			);

			Calendar cal = Calendar.getInstance();
			cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			Date sevenDaysAgo = cal.getTime();

			return criteriaBuilder.and(
				criteriaBuilder.equal(
					historyRoot.get("passive"),
					passive
				),
				criteriaBuilder.or(
					predicates.toArray(new Predicate[0])
				),
				criteriaBuilder.greaterThanOrEqualTo(
					historyRoot.get("occurred"),
					sevenDaysAgo
				),
				criteriaBuilder.not(
					criteriaBuilder.exists(
						subquery.
							select(withdrawalRecordRoot).
							where(
								criteriaBuilder.equal(
									historyRoot.get("id"), withdrawalRecordRoot.get("id")
								)
							)
					)
				)
			);
		};
	}
}
