package tw.musemodel.dingzhiqingren.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Location_;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Lover_;

/**
 * @author p@musemodel.tw
 */
public class LoverSpecification {

	private final static Logger LOGGER = LoggerFactory.getLogger(LoverSpecification.class);

	/**
	 * 依性别、地区捞出安心的未封号；适用于首页的涂鸦墙。
	 *
	 * @param gender 性别
	 * @param location 地区
	 * @return
	 */
	public static Specification<Lover> relievedOnTheWall(Location location) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			Subquery<Location> subquery = criteriaQuery.subquery(
				Location.class
			);
			Root<Location> locationRoot = subquery.from(
				Location.class
			);
			subquery.select(
				locationRoot.
					join(
						Location_.lovers,
						JoinType.LEFT
					).
					get("id")
			);
			subquery.where(criteriaBuilder.equal(
				locationRoot.get(Location_.id),
				location.getId()
			));

			Collection<Predicate> predicates = new ArrayList<>();
//			predicates.add(criteriaBuilder.equal(
//				root.get(Lover_.gender),
//				gender
//			));//性别
			predicates.add(root.
				get(Lover_.id).
				in(subquery)
			);//地区
			predicates.add(criteriaBuilder.isTrue(
				root.get(Lover_.relief)
			));//通过安心认证
			predicates.add(root.
				get(Lover_.delete).
				isNull()
			);//未封号

			Predicate predicate = criteriaBuilder.conjunction();
			predicate.getExpressions().addAll(predicates);
			return predicate;
		};
	}

	/**
	 * 未封号的情人们，以活跃降幂排序；适用于首页。
	 *
	 * @param gender 性别
	 * @return 未封号的(甜心|男士)们
	 */
	public static Specification<Lover> latestActiveOnTheWall(boolean gender) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.orderBy(
				criteriaBuilder.desc(
					root.get(Lover_.active)
				)
			);//以活跃降幂排序
			return criteriaBuilder.and(
				criteriaBuilder.isNull(
					root.get(Lover_.delete)
				),//未封号
				criteriaBuilder.equal(
					root.get(Lover_.gender),
					gender
				)//性别
			);
		};
	}

	/**
	 * 未封号并通过安心认证的情人们，以活跃降幂排序；适用于首页。
	 *
	 * @param gender 性别
	 * @return 未封号的(甜心|男士)们
	 */
	public static Specification<Lover> relievingOnTheWall(boolean gender) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.orderBy(
				criteriaBuilder.desc(
					root.get(Lover_.active)
				)
			);//以活跃降幂排序
			return criteriaBuilder.and(
				criteriaBuilder.isNull(
					root.get(Lover_.delete)
				),//未封号
				criteriaBuilder.equal(
					root.get(Lover_.gender),
					gender
				),//性别
				criteriaBuilder.isTrue(
					root.get(Lover_.relief)
				)//通过安心认证
			);
		};
	}

	/**
	 * 未封号的贵宾(仅男士)们，以活跃降幂排序；适用于首页。
	 *
	 * @return 未封号的贵宾(仅男士)们
	 */
	public static Specification<Lover> vipOnTheWall() {
		return (root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.orderBy(
				criteriaBuilder.desc(
					root.get(Lover_.active)
				)
			);//以活跃降幂排序
			return criteriaBuilder.and(
				criteriaBuilder.isNull(
					root.get(Lover_.delete)
				),//未封号
				criteriaBuilder.isTrue(
					root.get(Lover_.gender)
				),//男士
				criteriaBuilder.greaterThan(
					root.get(Lover_.vip),
					new Date(System.currentTimeMillis())
				)//贵宾有效期须在目前之后
			);
		};
	}

	/**
	 * @param gender 性别
	 * @param location 出没地区
	 * @return 符合出没地区的
	 */
	public static Specification<Lover> appearedAreas(boolean gender, Location location) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			Collection<Predicate> predicates = new ArrayList<>();

			Subquery<Location> subquery = criteriaQuery.subquery(
				Location.class
			);
			Root<Location> locationRoot = subquery.from(
				Location.class
			);
			subquery.select(
				locationRoot.
					join(
						Location_.lovers,
						JoinType.LEFT
					).
					get("id")
			);
			subquery.where(criteriaBuilder.equal(
				locationRoot.get(Location_.id),
				location.getId()
			));
			predicates.add(
				root.
					get(Lover_.id).
					in(subquery)
			);
			predicates.add(
				root.
					get(Lover_.delete).
					isNull()
			);

			Predicate predicate = criteriaBuilder.conjunction();
			predicate.
				getExpressions().
				//add(
				//	root.
				//		get(Lover_.id).
				//		in(subquery)
				//);
				addAll(predicates);
			return predicate;
		};
	}

	/**
	 * @return 已砍号的
	 */
	public static Specification<Lover> isDeleted() {
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.isNotNull(
				root.get(Lover_.delete)
			);
		};
	}

	/**
	 * @return 甜心们
	 */
	public static Specification<Lover> isFemale() {
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(
				root.get(Lover_.gender),
				false
			);
		};
	}

	/**
	 * @return 男士们
	 */
	public static Specification<Lover> isMale() {
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(
				root.get(Lover_.gender),
				true
			);
		};
	}

	/**
	 * @return 有 VIP 的
	 */
	public static Specification<Lover> isVip() {
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.and(
				criteriaBuilder.equal(
					root.get(Lover_.gender),
					true
				),
				criteriaBuilder.greaterThan(
					root.get(Lover_.vip),
					new Date(System.currentTimeMillis())
				)
			);
		};
	}

	/**
	 * @return 未砍号的
	 */
	public static Specification<Lover> notDeleted() {
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.isNull(
				root.get(Lover_.delete)
			);
		};
	}

	/**
	 * @return 无 VIP 的
	 */
	public static Specification<Lover> notVip() {
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.lessThanOrEqualTo(
				root.get(Lover_.vip),
				new Date(System.currentTimeMillis())
			);
		};
	}
}
