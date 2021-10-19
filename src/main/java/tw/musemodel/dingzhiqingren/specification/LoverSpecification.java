package tw.musemodel.dingzhiqingren.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import tw.musemodel.dingzhiqingren.entity.Allowance;
import tw.musemodel.dingzhiqingren.entity.AnnualIncome;
import tw.musemodel.dingzhiqingren.entity.Companionship;
import tw.musemodel.dingzhiqingren.entity.Companionship_;
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Location_;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Lover_;
import tw.musemodel.dingzhiqingren.service.LoverService;

/**
 * @author p@musemodel.tw
 */
public class LoverSpecification {

        private final static Logger LOGGER = LoggerFactory.getLogger(LoverSpecification.class);

        /**
         * 黑名单。
         *
         * @param lover 用户号
         * @return 用户号拉黑的其它用户号们
         */
        //private static Predicate blacklist(Lover lover, Root<Lover> root, CriteriaBuilder criteriaBuilder) {
        //	Predicate predicate = criteriaBuilder.conjunction();
        //	Collection<Predicate> predicates = new ArrayList<>();
        //
        //	predicates.add(
        //		criteriaBuilder.not(
        //			criteriaBuilder.isMember(
        //				lover,
        //				root.get(Lover_.blocked)
        //			)
        //		)
        //	);
        //
        //	predicate.getExpressions().addAll(predicates);
        //	return predicate;
        //}
        //
        /**
         * 被列入黑名单。
         *
         * @param lover 用户号
         * @return 把用户号拉黑的其它用户号们
         */
        //private static Predicate blacklisted(Lover lover, Root<Lover> root, CriteriaBuilder criteriaBuilder) {
        //	Predicate predicate = criteriaBuilder.conjunction();
        //	Collection<Predicate> predicates = new ArrayList<>();
        //
        //	predicates.add(criteriaBuilder.isMember(
        //		lover,
        //		root.get(Lover_.blocking)
        //	).not());
        //
        //	predicate.getExpressions().addAll(predicates);
        //	return predicate;
        //}
//
        /**
         * 该填的栏位都有填的用户号们。
         *
         * @param gender 性别
         * @return 合格用户号们
         */
        private static Predicate eligible(Root<Lover> root, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                Collection<Predicate> predicates = new ArrayList<>();

                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.login)
                ));//有帐号(手机号)
                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.shadow)
                ));//有密码
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
                ));//有抽烟习惯
                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.drinking)
                ));//有饮酒习惯
                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.idealConditions)
                ));//有简述理想对象条件
                predicates.add(criteriaBuilder.isNull(
                        root.get(Lover_.delete)
                ));//未封号
                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.relationship)
                ));//有预期关系
                predicates.add(criteriaBuilder.isFalse(
                        root.get(Lover_.fake)
                ));//가짜 계좌

                predicate.getExpressions().addAll(predicates);
                return predicate;
        }

        /**
         * 该填的栏位都有填的异性用户号们。
         *
         * @param gender 性别
         * @return 合格异性用户号们
         */
        private static Predicate eligible(boolean gender, Root<Lover> root, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                Collection<Predicate> predicates = new ArrayList<>();

                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.login)
                ));//有帐号(手机号)
                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.shadow)
                ));//有密码
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
                ));//有抽烟习惯
                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.drinking)
                ));//有饮酒习惯
                predicates.add(criteriaBuilder.isNotNull(
                        root.get(Lover_.idealConditions)
                ));//有简述理想对象条件
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
         * 过滤(搜寻)用户号。
         *
         * @param mofo 用户号
         * @param maximumAge 最大年龄
         * @param minimumAge 最小年龄
         * @param maximumHeight 最大身高
         * @param minimumHeight 最小身高
         * @param maximumWeight 最大体重
         * @param minimumWeight 最小体重
         * @param bodyType 体型
         * @param education 学历
         * @param marriage 婚姻
         * @param smoking 抽烟
         * @param drinking 饮酒
         * @param annualIncome 年收入
         * @param allowance 零用钱
         * @param inceptions 友谊及地区
         * @param exceptions 拉黑及黑名单
         * @return
         */
        public static Specification<Lover> filter(
                Lover mofo,
                String nickname,
                Integer maximumAge, Integer minimumAge,
                Short maximumHeight, Short minimumHeight,
                Short maximumWeight, Short minimumWeight,
                Lover.BodyType bodyType,
                Lover.Education education,
                Lover.Marriage marriage,
                Lover.Smoking smoking,
                Lover.Drinking drinking,
                AnnualIncome annualIncome,
                Allowance allowance,
                Collection<Integer> inceptions,
                Set<Integer> exceptions
        ) {
                return (root, criteriaQuery, criteriaBuilder) -> {
                        boolean gender = !mofo.getGender();
                        Collection<Predicate> predicates = new ArrayList<>();
                        predicates.add(criteriaBuilder.equal(
                                root.get(Lover_.gender),
                                gender
                        ));//性别
                        predicates.add(root.
                                get(Lover_.delete).
                                isNull()
                        );//未封号
                        predicates.add(eligible(
                                gender,
                                root,
                                criteriaBuilder
                        ));//合格用户号们

                        LOGGER.debug("測試testlover, nickname{}", nickname);
                        LOGGER.debug("測試testlover, maximumAge{}", maximumAge);
                        LOGGER.debug("測試testlover, minimumAge{}", minimumAge);
                        LOGGER.debug("測試testlover, maximumHeight{}", maximumHeight);
                        LOGGER.debug("測試testlover, minimumHeight{}", minimumHeight);
                        LOGGER.debug("測試testlover, maximumWeight{}", maximumWeight);
                        LOGGER.debug("測試testlover, minimumWeight{}", minimumWeight);
                        LOGGER.debug("測試testlover, bodyType{}", bodyType);
                        LOGGER.debug("測試testlover, education{}", education);
                        LOGGER.debug("測試testlover, marriage{}", marriage);
                        LOGGER.debug("測試testlover, smoking{}", smoking);
                        LOGGER.debug("測試testlover, drinking{}", drinking);
                        LOGGER.debug("測試testlover, annualIncome{}", annualIncome);
                        LOGGER.debug("測試testlover, allowance{}", allowance);
                        LOGGER.debug("測試testlover, inceptions{}", inceptions);
                        LOGGER.debug("測試testlover, inceptions.isEmpty{}", inceptions.isEmpty());
                        LOGGER.debug("測試testlover, exceptions{}", exceptions);

                        if (!nickname.isEmpty() && !nickname.isBlank()) {
                                predicates.add(
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(root.get(Lover_.NICKNAME)),
                                                String.format("%%%s%%", nickname.toLowerCase())
                                        )
                                );
                        }

                        if (Objects.nonNull(maximumAge) && Objects.nonNull(minimumAge)) {
                                predicates.add(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                                root.get(Lover_.birthday),
                                                LoverService.getDateByAge(maximumAge)
                                        )
                                );
                                predicates.add(
                                        criteriaBuilder.lessThanOrEqualTo(
                                                root.get(Lover_.birthday),
                                                LoverService.getDateByAge(minimumAge)
                                        )
                                );
                        }//年龄
                        if (Objects.nonNull(maximumHeight) && Objects.nonNull(minimumHeight)) {
                                predicates.add(
                                        criteriaBuilder.lessThanOrEqualTo(
                                                root.get(Lover_.height),
                                                maximumHeight
                                        )
                                );
                                predicates.add(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                                root.get(Lover_.height),
                                                minimumHeight
                                        )
                                );
                        }//身高
                        if (Objects.nonNull(maximumWeight) && Objects.nonNull(minimumWeight)) {
                                predicates.add(
                                        criteriaBuilder.lessThanOrEqualTo(
                                                root.get(Lover_.weight),
                                                maximumWeight
                                        )
                                );
                                predicates.add(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                                root.get(Lover_.weight),
                                                minimumWeight
                                        )
                                );
                        }//体重
                        if (Objects.nonNull(bodyType)) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(Lover_.bodyType),
                                                bodyType
                                        )
                                );
                        }//体型
                        if (Objects.nonNull(education)) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(Lover_.education),
                                                education
                                        )
                                );
                        }//学历
                        if (Objects.nonNull(marriage)) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(Lover_.marriage),
                                                marriage
                                        )
                                );
                        }//婚姻
                        if (Objects.nonNull(smoking)) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(Lover_.smoking),
                                                smoking
                                        )
                                );
                        }//抽烟
                        if (Objects.nonNull(drinking)) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(Lover_.drinking),
                                                drinking
                                        )
                                );
                        }//饮酒
                        if (Objects.nonNull(annualIncome)) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(Lover_.annualIncome),
                                                annualIncome
                                        )
                                );
                        }//年收入
                        if (Objects.nonNull(allowance)) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(Lover_.allowance),
                                                allowance
                                        )
                                );
                        }//零用钱
                        if (!inceptions.isEmpty()) {
                                predicates.add(root.
                                        get(Lover_.id).
                                        in(inceptions)
                                );
                        }//地区及友谊
                        predicates.add(
                                criteriaBuilder.not(
                                        root.get(Lover_.id).in(exceptions)
                                )
                        );//黑名单

                        Predicate predicate = criteriaBuilder.conjunction();
                        predicate.getExpressions().addAll(predicates);
                        return predicate;
                };
        }

        /**
         * 未封号的用户号们，以活跃降幂排序；适用于首页的最近活跃列表区块。
         *
         * @return 未封号的(甜心|男士)们
         */
        public static Specification<Lover> latestActiveAndLegit() {
                return (root, criteriaQuery, criteriaBuilder) -> {
                        criteriaQuery.orderBy(
                                criteriaBuilder.desc(
                                        root.get(Lover_.active)
                                )
                        );//以活跃降幂排序

                        return criteriaBuilder.and(
                                eligible(root, criteriaBuilder)//合格用户号们
                        );
                };
        }

        /**
         * 未封号的用户号们，以活跃降幂排序；适用于首页的最近活跃列表区块。
         *
         * @return 未封号的(甜心|男士)们
         */
        public static Specification<Lover> latestActiveAndLegit(boolean gender) {
                return (root, criteriaQuery, criteriaBuilder) -> {
                        criteriaQuery.orderBy(
                                criteriaBuilder.desc(
                                        root.get(Lover_.active)
                                )
                        );//以活跃降幂排序

                        return criteriaBuilder.and(
                                eligible(root, criteriaBuilder),//合格用户号们
                                criteriaBuilder.equal(
                                        root.get(Lover_.gender),
                                        gender
                                )
                        );
                };
        }

        /**
         * 未封号的用户号们，以活跃降幂排序；适用于首页的最近活跃列表区块。
         *
         * @param mofo 用户号
         * @param exceptions 例外
         * @return 未封号的异性用户号们
         */
        public static Specification<Lover> latestActiveOnTheWall(Lover mofo, Set<Integer> exceptions) {
                return (root, criteriaQuery, criteriaBuilder) -> {
                        criteriaQuery.orderBy(
                                criteriaBuilder.desc(
                                        root.get(Lover_.active)
                                )
                        );//以活跃降幂排序

                        return criteriaBuilder.and(
                                eligible(!mofo.getGender(), root, criteriaBuilder),//合格用户号们
                                //blacklist(mofo, root, criteriaBuilder),//黑名单
                                //blacklisted(mofo, root, criteriaBuilder)//被列入黑名单
                                criteriaBuilder.not(
                                        root.get(Lover_.id).in(exceptions)
                                )
                        );
                };
        }

        /**
         * 未封号的用户号们，以註冊时间降幂排序；适用于首页的最新注册列表区块。
         *
         * @param mofo 用户号
         * @param exceptions 例外
         * @return 未封号的(甜心|男士)们
         */
        public static Specification<Lover> latestRegisteredOnTheWall(Lover mofo, Set<Integer> exceptions) {
                return (root, criteriaQuery, criteriaBuilder) -> {
                        criteriaQuery.orderBy(
                                criteriaBuilder.desc(
                                        root.get(Lover_.registered)
                                )
                        );//以活跃降幂排序

                        return criteriaBuilder.and(
                                eligible(!mofo.getGender(), root, criteriaBuilder),//合格用户号们
                                //blacklist(mofo, root, criteriaBuilder),//黑名单
                                //blacklisted(mofo, root, criteriaBuilder)//被列入黑名单
                                criteriaBuilder.not(
                                        root.get(Lover_.id).in(exceptions)
                                )
                        );
                };
        }

        /**
         * 未封号并通过安心认证的用户号们，以活跃降幂排序；适用于首页的安心认证列表区块。
         *
         * @param mofo 用户号
         * @param exceptions 例外
         * @return 未封号的(甜心|男士)们
         */
        public static Specification<Lover> relievingOnTheWall(Lover mofo, Set<Integer> exceptions) {
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
                                eligible(!mofo.getGender(), root, criteriaBuilder),//合格用户号们
                                //blacklist(mofo, root, criteriaBuilder),//黑名单
                                //blacklisted(mofo, root, criteriaBuilder)//被列入黑名单
                                criteriaBuilder.not(
                                        root.get(Lover_.id).in(exceptions)
                                )
                        );
                };
        }

        /**
         * 搜寻。
         *
         * @param mofo 用户号
         * @param location 地区
         * @param companionship 服务
         * @param exceptions 例外
         * @return 符合条件的(甜心|男士)们
         */
        public static Specification<Lover> search(Lover mofo, Location location, Companionship companionship, Set<Integer> exceptions) {
                return (root, criteriaQuery, criteriaBuilder) -> {
                        boolean gender = !mofo.getGender();
                        Collection<Predicate> predicates = new ArrayList<>();
                        predicates.add(criteriaBuilder.equal(
                                root.get(Lover_.gender),
                                gender
                        ));//性别
                        predicates.add(root.
                                get(Lover_.delete).
                                isNull()
                        );//未封号

                        predicates.add(eligible(
                                gender,
                                root,
                                criteriaBuilder
                        ));//合格用户号们
                        //predicates.add(blacklist(
                        //	mofo,
                        //	root,
                        //	criteriaBuilder
                        //));//黑名单
                        //predicates.add(blacklisted(
                        //	mofo,
                        //	root,
                        //	criteriaBuilder
                        //));//被列入黑名单
                        //predicates.add(
                        //	criteriaBuilder.not(
                        //		root.get(Lover_.id).in(exceptions)
                        //	)
                        //);//黑名单

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
			 友谊
                         */
                        if (Objects.nonNull(companionship)) {
                                Subquery<Companionship> subqueryCompanionship = criteriaQuery.subquery(
                                        Companionship.class
                                );
                                Root<Companionship> serviceTagRoot = subqueryCompanionship.from(
                                        Companionship.class
                                );
                                subqueryCompanionship.select(
                                        serviceTagRoot.
                                                join(
                                                        Companionship_.lovers,
                                                        JoinType.LEFT
                                                ).
                                                get("id")
                                );
                                subqueryCompanionship.where(criteriaBuilder.equal(
                                        serviceTagRoot.get(Companionship_.id),
                                        companionship.getId()
                                ));
                                predicates.add(root.
                                        get(Lover_.id).
                                        in(subqueryCompanionship)
                                );
                        }

                        Predicate predicate = criteriaBuilder.conjunction();
                        predicate.getExpressions().addAll(predicates);
                        return predicate;
                };
        }

        public static Specification<Lover> search(Lover mofo, Collection<Integer> inceptions, Set<Integer> exceptions) {
                return (root, criteriaQuery, criteriaBuilder) -> {
                        boolean gender = !mofo.getGender();
                        Collection<Predicate> predicates = new ArrayList<>();
                        predicates.add(criteriaBuilder.equal(
                                root.get(Lover_.gender),
                                gender
                        ));//性别
                        predicates.add(root.
                                get(Lover_.delete).
                                isNull()
                        );//未封号

                        predicates.add(eligible(
                                gender,
                                root,
                                criteriaBuilder
                        ));//合格用户号们
                        //predicates.add(blacklist(
                        //	mofo,
                        //	root,
                        //	criteriaBuilder
                        //));//黑名单
                        //predicates.add(blacklisted(
                        //	mofo,
                        //	root,
                        //	criteriaBuilder
                        //));//被列入黑名单
                        predicates.add(root.
                                get(Lover_.id).
                                in(inceptions)
                        );//地区及友谊
                        predicates.add(
                                criteriaBuilder.not(
                                        root.get(Lover_.id).in(exceptions)
                                )
                        );//黑名单

                        Predicate predicate = criteriaBuilder.conjunction();
                        predicate.getExpressions().addAll(predicates);
                        return predicate;
                };
        }

        /**
         * 未封号的贵宾(仅男士)们，以活跃降幂排序；适用于首页的贵宾会员列表区块。
         *
         * @param mofo 用户号
         * @param exceptions 例外
         * @return 未封号的贵宾(仅男士)们
         */
        public static Specification<Lover> vipOnTheWall(Lover mofo, Set<Integer> exceptions) {
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
                                        Lover.MaleSpecies.VVIP
                                ),//须为 VVIP
                                eligible(!mofo.getGender(), root, criteriaBuilder),//合格用户号们
                                //blacklist(mofo, root, criteriaBuilder),//黑名单
                                //blacklisted(mofo, root, criteriaBuilder)//被列入黑名单
                                criteriaBuilder.not(
                                        root.get(Lover_.id).in(exceptions)
                                )
                        );
                };
        }
}
