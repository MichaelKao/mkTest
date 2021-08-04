package tw.musemodel.dingzhiqingren.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
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
import tw.musemodel.dingzhiqingren.entity.Lover.MaleSpecies;
import tw.musemodel.dingzhiqingren.entity.Lover_;
import tw.musemodel.dingzhiqingren.entity.ServiceTag;
import tw.musemodel.dingzhiqingren.entity.ServiceTag_;

/**
 * @author p@musemodel.tw
 */
public class LoverSpecification {

	private final static Logger LOGGER = LoggerFactory.getLogger(LoverSpecification.class);

	/**
	 * 该填的栏位都有填的用户号。
	 *
	 * @param gender 性别
	 * @return 合格用户号们
	 */
	private static Predicate eligible(Root<Lover> root, CriteriaBuilder criteriaBuilder, boolean gender) {
		Predicate predicate = criteriaBuilder.conjunction();
		Collection<Predicate> predicates = new ArrayList<>();

		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.login)
		));//有帐号(手机号)
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.shadow)
		));//有密码
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.location)
		));//有地区
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.nickname)
		));//有昵称
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.aboutMe)
		));//有自介
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.greeting)
		));//有哈啰
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.bodyType)
		));//有体型
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.height)
		));//有身高
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.weight)
		));//有体重
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.education)
		));//有学历
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.marriage)
		));//有婚姻
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.occupation)
		));//有职业
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.smoking)
		));//有抽烟
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.drinking)
		));//有饮酒
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.idealConditions)
		));//有简述理想对象
		predicates.add(criteriaBuilder.isNull(
			root.get(Lover_.delete)
		));//未封号
		predicates.add(criteriaBuilder.isNotNull(
			root.get(Lover_.relationship)
		));//有预期关系
		if (gender) {
			predicates.add(criteriaBuilder.isTrue(
				root.get(Lover_.gender)
			));//男士
			predicates.add(criteriaBuilder.isNotNull(
				root.get(Lover_.annualIncome)
			));//有年收入
		} else {
			predicates.add(criteriaBuilder.isFalse(
				root.get(Lover_.gender)
			));//甜心
			predicates.add(criteriaBuilder.isNotNull(
				root.get(Lover_.inviteMeAsLineFriend)
			));//有添加好友链结
			predicates.add(criteriaBuilder.isNotNull(
				root.get(Lover_.allowance)
			));//有期望零用钱
		}

		predicate.getExpressions().addAll(predicates);
		return predicate;
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

			return eligible(root, criteriaBuilder, gender);
		};
	}

	/**
	 * 未封号的情人们，以註冊时间降幂排序；适用于首页。
	 *
	 * @param gender 性别
	 * @return 未封号的(甜心|男士)们
	 */
	public static Specification<Lover> latestRegisteredOnTheWall(boolean gender) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.orderBy(
				criteriaBuilder.desc(
					root.get(Lover_.registered)
				)
			);//以活跃降幂排序

			return eligible(root, criteriaBuilder, gender);
		};
	}

	/**
	 * 甜心依照自己服務地區群發打招呼給男仕
	 *
	 * @param gender
	 * @param locations
	 * @return
	 */
	public static Specification<Lover> malesListForGroupGreeting(boolean gender, Set<Location> locations) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			Collection<Predicate> predicates = new ArrayList<>();
			criteriaQuery.orderBy(
				criteriaBuilder.desc(
					root.get(Lover_.active)
				));
			predicates.add(criteriaBuilder.equal(
				root.get(Lover_.gender),
				gender
			));//性别
			predicates.add(root.
				get(Lover_.delete).
				isNull()
			);//未封号

			/*
			 地区
			 */
			if (locations.size() > 0) {
				Subquery<Location> subqueryLocation = criteriaQuery.subquery(
					Location.class
				);
				Root<Location> locationRoot = subqueryLocation.from(
					Location.class
				);
				subqueryLocation.select(
					locationRoot.
						join(
							Location_.lovers,
							JoinType.LEFT
						).
						get("id")
				);
				List<Predicate> locationPredicate = new ArrayList<>();
				for (Location location : locations) {
					locationPredicate.add(
						criteriaBuilder.equal(
							locationRoot.get(Location_.id),
							location.getId()
						)
					);
				}
				subqueryLocation.where(
					criteriaBuilder.or(
						locationPredicate.toArray(new Predicate[0])
					));
				predicates.add(root.
					get(Lover_.id).
					in(subqueryLocation)
				);
			}

			Predicate predicate = criteriaBuilder.conjunction();
			predicate.getExpressions().addAll(predicates);
			return predicate;
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
				criteriaBuilder.isTrue(
					root.get(Lover_.relief)
				),//通过安心认证
				eligible(root, criteriaBuilder, gender)
			);
		};
	}

	/**
	 * 搜寻。
	 *
	 * @param gender 性别
	 * @param location 地区
	 * @param serviceTag 服务
	 * @return 符合条件的(甜心|男士)们
	 */
	public static Specification<Lover> search(boolean gender, Location location, ServiceTag serviceTag) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			Collection<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(
				root.get(Lover_.gender),
				gender
			));//性别
			predicates.add(root.
				get(Lover_.delete).
				isNull()
			);//未封号

			/*
			 地区
			 */
			if (Objects.nonNull(location)) {
				Subquery<Location> subqueryLocation = criteriaQuery.subquery(
					Location.class
				);
				Root<Location> locationRoot = subqueryLocation.from(
					Location.class
				);
				subqueryLocation.select(
					locationRoot.
						join(
							Location_.lovers,
							JoinType.LEFT
						).
						get("id")
				);
				subqueryLocation.where(criteriaBuilder.equal(
					locationRoot.get(Location_.id),
					location.getId()
				));
				predicates.add(root.
					get(Lover_.id).
					in(subqueryLocation)
				);
			}

			/*
			 服务
			 */
			if (Objects.nonNull(serviceTag)) {
				Subquery<ServiceTag> subqueryServiceTag = criteriaQuery.subquery(
					ServiceTag.class
				);
				Root<ServiceTag> serviceTagRoot = subqueryServiceTag.from(
					ServiceTag.class
				);
				subqueryServiceTag.select(
					serviceTagRoot.
						join(
							ServiceTag_.lovers,
							JoinType.LEFT
						).
						get("id")
				);
				subqueryServiceTag.where(criteriaBuilder.equal(
					serviceTagRoot.get(ServiceTag_.id),
					serviceTag.getId()
				));
				predicates.add(root.
					get(Lover_.id).
					in(subqueryServiceTag)
				);
			}

			Predicate predicate = criteriaBuilder.conjunction();
			predicate.getExpressions().addAll(predicates);
			return predicate;
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
				criteriaBuilder.greaterThan(
					root.get(Lover_.vip),
					new Date(System.currentTimeMillis())
				),//贵宾有效期须在目前之后
				criteriaBuilder.equal(
					root.get(Lover_.maleSpecies),
					MaleSpecies.VVIP
				)//须为 VVIP
			);
		};
	}
}
