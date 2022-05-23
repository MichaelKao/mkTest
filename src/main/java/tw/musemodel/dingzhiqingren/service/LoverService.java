package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.Specifications;
import tw.musemodel.dingzhiqingren.WebSocketServer;
import tw.musemodel.dingzhiqingren.entity.Activation;
import tw.musemodel.dingzhiqingren.entity.Allowance;
import tw.musemodel.dingzhiqingren.entity.AnnualIncome;
import tw.musemodel.dingzhiqingren.entity.Companionship;
import tw.musemodel.dingzhiqingren.entity.Country;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Lover.MaleSpecies;
import tw.musemodel.dingzhiqingren.entity.OneOff;
import tw.musemodel.dingzhiqingren.entity.Picture;
import tw.musemodel.dingzhiqingren.entity.Privilege;
import tw.musemodel.dingzhiqingren.entity.PrivilegeKey;
import tw.musemodel.dingzhiqingren.entity.ResetShadow;
import tw.musemodel.dingzhiqingren.entity.Role;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.entity.TrialCode;
import tw.musemodel.dingzhiqingren.entity.UsedTrialCode;
import tw.musemodel.dingzhiqingren.entity.User;
import tw.musemodel.dingzhiqingren.entity.WithdrawalInfo;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord.WayOfWithdrawal;
import tw.musemodel.dingzhiqingren.entity.embedded.AppearedLocation;
import tw.musemodel.dingzhiqingren.entity.embedded.AppearedLocationKey;
import tw.musemodel.dingzhiqingren.entity.embedded.Blacklist;
import tw.musemodel.dingzhiqingren.entity.embedded.BlacklistKey;
import tw.musemodel.dingzhiqingren.entity.embedded.DesiredCompanionship;
import tw.musemodel.dingzhiqingren.entity.embedded.DesiredCompanionshipKey;
import tw.musemodel.dingzhiqingren.entity.embedded.Follow;
import tw.musemodel.dingzhiqingren.event.SignedUpEvent;
import tw.musemodel.dingzhiqingren.model.Activated;
import tw.musemodel.dingzhiqingren.model.Activity;
import tw.musemodel.dingzhiqingren.model.Descendant;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.ResetPassword;
import tw.musemodel.dingzhiqingren.model.SignUp;
import tw.musemodel.dingzhiqingren.repository.ActivationRepository;
import tw.musemodel.dingzhiqingren.repository.AllowanceRepository;
import tw.musemodel.dingzhiqingren.repository.AnnualIncomeRepository;
import tw.musemodel.dingzhiqingren.repository.AppearedLocationRepository;
import tw.musemodel.dingzhiqingren.repository.BlacklistRepository;
import tw.musemodel.dingzhiqingren.repository.CompanionshipRepository;
import tw.musemodel.dingzhiqingren.repository.CountryRepository;
import tw.musemodel.dingzhiqingren.repository.DesiredCompanionshipRepository;
import tw.musemodel.dingzhiqingren.repository.FollowRepository;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;
import tw.musemodel.dingzhiqingren.repository.LocationRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.OneOffRepository;
import tw.musemodel.dingzhiqingren.repository.PictureRepository;
import tw.musemodel.dingzhiqingren.repository.PrivilegeRepository;
import tw.musemodel.dingzhiqingren.repository.ResetShadowRepository;
import tw.musemodel.dingzhiqingren.repository.RoleRepository;
import tw.musemodel.dingzhiqingren.repository.StopRecurringPaymentApplicationRepository;
import tw.musemodel.dingzhiqingren.repository.TrialCodeRepository;
import tw.musemodel.dingzhiqingren.repository.UsedTrialCodeRepository;
import tw.musemodel.dingzhiqingren.repository.UserRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalInfoRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalRecordRepository;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;
import static tw.musemodel.dingzhiqingren.service.Servant.PAGE_SIZE_ON_THE_WALL;
import static tw.musemodel.dingzhiqingren.service.Servant.UTF_8;

import tw.musemodel.dingzhiqingren.service.vo.CompanionshipWithInfo;
import tw.musemodel.dingzhiqingren.specification.LoverSpecification;

/**
 * 服务层：情人
 *
 * @author p@musemodel.tw
 */
@Service
public class LoverService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoverService.class);

	private static final String AWS_ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");

	private static final String AWS_SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");


	private static final AmazonSNS AMAZON_SNS = AmazonSNSClientBuilder.
		standard().
		withCredentials(new AWSStaticCredentialsProvider(
			new BasicAWSCredentials(
				AWS_ACCESS_KEY_ID,
				AWS_SECRET_ACCESS_KEY
			)
		)).withRegion(Regions.AP_SOUTHEAST_1).build();

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static final Short NUMBER_OF_GROUP_GREETING = Short.valueOf(System.getenv("NUMBER_OF_GROUP_GREETING"));

	public static final double ME_Point_To_NTD=0.6;

	public static Object DATE_TIME_FORMATTER(String yyyyMMdd_HHmm) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private WebSocketServer webSocketServer;

	@Autowired
	private AmazonWebServices amazonWebServices;

	@Autowired
	private LineMessagingService lineMessagingService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private Servant servant;

	@Autowired
	private ActivationRepository activationRepository;

	@Autowired
	private AllowanceRepository allowanceRepository;

	@Autowired
	private AnnualIncomeRepository annualIncomeRepository;

	@Autowired
	private AppearedLocationRepository appearedLocationRepository;

	@Autowired
	private BlacklistRepository blacklistRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private DesiredCompanionshipRepository desiredCompanionshipRepository;

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LineGivenRepository lineGivenRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private PictureRepository pictureRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private ResetShadowRepository resetShadowRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CompanionshipRepository companionshipRepository;

	@Autowired
	private UsedTrialCodeRepository usedTrialCodeRepository;

	@Autowired
	private OneOffRepository oneOffRepository;

	@Autowired
	private StopRecurringPaymentApplicationRepository stopRecurringPaymentApplicationRepository;

	@Value("classpath:sql/我拉黑了谁.sql")
	private Resource thoseIBlockResource;

	@Value("classpath:sql/谁拉黑了我.sql")
	private Resource thoseWhoBlockMeResource;

	@Value("classpath:sql/甜心可群發的對象.sql")
	private Resource groupGreetingList;

	@Value("classpath:sql/安心认证.sql")
	private Resource relievingOnTheWall;

	@Value("classpath:sql/男仕贵宾会员.sql")
	private Resource vvipOnTheWall;

	@Autowired
	private TrialCodeRepository trialCodeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WithdrawalInfoRepository withdrawalInfoRepository;

	@Autowired
	private WithdrawalRecordRepository withdrawalRecordRepository;

	@Autowired
	private DashboardService dashboardService;



	/**
	 * 找上线用户。
	 *
	 * @param mofo 下线用户
	 * @param ancestors 已知上线用户
	 * @return 上线用户们
	 */
	private Collection<Integer> findAncestry(Lover mofo, Collection<Integer> ancestors) {
		Lover ancestor = mofo.getReferrer();

		/*
		 此用户号无上线则直接返回现有已知上线用户
		 */
		if (Objects.isNull(ancestor)) {
			return ancestors;
		}

		/*
		 此用户号有上线并加入已知上线用户
		 */
		Integer id = ancestor.getId();
		ancestors.add(id);

		/*
		 重叠的推荐用户
		 */
		if (isOverlapped(id, ancestors)) {
			throw new RuntimeException("signUp.overlap");
		}

		return findAncestry(ancestor, ancestors);
	}

	/**
	 * 找下线用户。
	 *
	 * @param me 上线用户
	 * @return 下线用户们
	 */
	@Transactional(readOnly = true)
	private List<Descendant> findDescendants(Lover me) {
		List<Descendant> descendants = new ArrayList<>();
		for (Lover mofo : loverRepository.findByReferrerOrderByRegisteredDesc(me)) {
			Date deadline = mofo.getVip();

			Descendant descendant = new Descendant(
				mofo.getIdentifier(),
				mofo.getNickname(),
				servant.DATE_TIME_FORMATTER_yyyyMMddHHmm.format(
					Servant.toTaipeiZonedDateTime(
						mofo.getRegistered()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				)
			);
			descendant.setVip(
				Objects.nonNull(deadline) && deadline.after(new Date(System.currentTimeMillis()))
			);

			descendants.add(descendant);
		}
		return descendants;
	}

	/**
	 * 有无重叠的上线用户。
	 *
	 * @param ancestors 已知上线用户
	 * @return 真伪布林值
	 */
	private boolean hasOverlappedAncestor(Collection<Integer> ancestors) {
		Set<Integer> set = new HashSet<>();
		for (Integer integer : ancestors) {
			if (!set.add(integer)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 某用户号主键是否与已知上线用户中重叠。
	 *
	 * @param integer 用户号主键
	 * @param ancestors 已知上线用户
	 * @return 真伪布林值
	 */
	private boolean isOverlapped(Integer integer, Collection<Integer> ancestors) {
		Map<String, Integer> map = new HashMap<>();
		for (Integer ancestor : ancestors) {
			String key = ancestor.toString();
			if (map.containsKey(key)) {
				if (key.equals(integer.toString())) {
					return true;
				} else {
					map.put(key, map.get(key) + 1);
				}
			}
			map.put(key, 1);
		}
		return false;
	}

	/**
	 * 某年龄对应至今的时戳。
	 *
	 * @param age 年龄
	 * @return 时戳
	 */
	public static Date getDateByAge(int age) {
		Calendar calendar = new GregorianCalendar(
			Servant.ASIA_TAIPEI_TIME_ZONE,
			Locale.TAIWAN);
		calendar.add(Calendar.YEAR, -age);
		return calendar.getTime();
	}

	/**
	 * 某段时间区间内注册的用户号。
	 *
	 * @param since 开始日期
	 * @param until 结束日期
	 * @return 用户号们
	 */
	public Collection<Lover> accountsCreatedOfCertainPeriod(Date since, Date until) {
		Calendar sinceCalendar = new GregorianCalendar(
			Servant.ASIA_TAIPEI_TIME_ZONE,
			Locale.TAIWAN
		), untilCalendar = new GregorianCalendar(
			Servant.ASIA_TAIPEI_TIME_ZONE,
			Locale.TAIWAN
		);

		sinceCalendar.setTime(since);
		untilCalendar.setTime(until);

		return loverRepository.findByRegisteredBetweenOrderByRegisteredDesc(
			Servant.earliestDate(
				sinceCalendar.get(Calendar.YEAR),
				sinceCalendar.get(Calendar.MONTH) + 1,
				sinceCalendar.get(Calendar.DAY_OF_MONTH)
			),
			Servant.latestDate(
				untilCalendar.get(Calendar.YEAR),
				untilCalendar.get(Calendar.MONTH) + 1,
				untilCalendar.get(Calendar.DAY_OF_MONTH)
			)
		);
	}

	/**
	 * 某一天内注册的用户号。
	 *
	 * @param year 年
	 * @param month 月
	 * @param dayOfMonth 日
	 * @return 用户号们
	 */
	public Collection<Lover> accountsCreatedOfTheDay(int year, int month, int dayOfMonth) {
		return loverRepository.findByRegisteredBetweenOrderByRegisteredDesc(
			Servant.earliestDate(year, month, dayOfMonth),
			Servant.latestDate(year, month, dayOfMonth)
		);
	}

	/**
	 * 某段时间区间内注册的用户号依性別計算數量。
	 *
	 * @param gender
	 * @param year 年
	 * @param month 月
	 * @param dayOfMonth 日
	 * @return 用户号们
	 */
	public long countAccountsCreatedOfTheDay(Boolean gender, int year, int month, int dayOfMonth) {
		return loverRepository.countByGenderAndRegisteredBetweenAndFakeOrderByRegisteredDesc(
			gender,
			Servant.earliestDate(year, month, dayOfMonth),
			Servant.latestDate(year, month, dayOfMonth),
			false
		);
	}

	/**
	 * 激活
	 *
	 * @param inputString 激活码
	 * @param locale 语言环境
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject activate(String inputString, Locale locale) {
		Activation activation = activationRepository.
			findOneByString(inputString).
			orElseThrow();
		if (Objects.isNull(activation.getString()) || Objects.nonNull(activation.getOccurred())) {
			throw new RuntimeException("activate.activated");
		}

		activation.setString(null);
		activation.setOccurred(new Date(System.currentTimeMillis()));
		activation = activationRepository.saveAndFlush(activation);

		Lover lover = activation.getLover();

		/*
		 初始化身份
		 */
		Role role = servant.getRole(Servant.ROLE_ADVENTURER);
		PrivilegeKey privilegeKey = new PrivilegeKey();
		privilegeKey.setLoverId(lover.getId());
		privilegeKey.setRoleId(role.getId());
		Privilege privilege = new Privilege();
		privilege.setId(privilegeKey);
		privilege.setLover(lover);
		privilege.setRole(role);
		privilegeRepository.saveAndFlush(privilege);

		/*
		 初始化推荐码
		 */
		String string = RandomStringUtils.randomAlphanumeric(8);
		while (loverRepository.countByReferralCode(string) > 0) {
			string = RandomStringUtils.randomAlphanumeric(8);
		}
		lover.setReferralCode(string);
		loverRepository.saveAndFlush(lover);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"activate.done",
				null,
				locale
			)).
			withRedirect(String.format(
				"/activated.asp?id=%s",
				activation.getLover().getIdentifier().toString()
			)).
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 用户已激活。
	 *
	 * @param activated 激活时送来的参数与值
	 * @param request 请求
	 * @param locale 语言环境
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject activated(Activated activated, HttpServletRequest request, Locale locale) {
		Lover lover = loadByIdentifier(activated.getIdentifier());
		if (Objects.isNull(lover)) {
			LOGGER.debug("初始化密码时找不到情人");
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"activated.mustntBeAuthenticated",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}

		/*
		 初始化密码
		 */
		String shadow = activated.getShadow();
		lover.setShadow(
			passwordEncoder.encode(shadow)
		);

		lover = loverRepository.saveAndFlush(lover);

		/*
		 使用户登入
		 */
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					String.format(
						"%s%s",
						lover.getCountry().getCallingCode(),
						lover.getLogin()
					),
					shadow
				)
			)
		);
		HttpSession session = request.getSession(true);
		session.setAttribute(
			HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
			securityContext
		);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"activated.done",
				null,
				locale
			)).
			withRedirect("/fillinProfile.asp").
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 计算用户年龄。
	 *
	 * @param lover 用户号
	 * @return 足岁岁数
	 */
	@Transactional(readOnly = true)
	public Integer calculateAge(Lover lover) {
		if (Objects.isNull(lover)) {
			throw new RuntimeException("calculateAge.loverMustntBeNull");
		}

		return calculateAge(lover.getBirthday());
	}

	/**
	 * 以日期计算年龄。
	 *
	 * @param birthday 出生年月日
	 * @return 足岁岁数
	 */
	@Transactional(readOnly = true)
	public Integer calculateAge(Date birthday) {
		if (Objects.isNull(birthday)) {
			throw new RuntimeException("calculateAge.birthdayMustntBeNull");
		}

		Calendar birth = new GregorianCalendar(),
			current = new GregorianCalendar();
		birth.setTime(birthday);

		int age = current.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
		int birthDayOfYear = birth.get(Calendar.DAY_OF_YEAR),
			currentDayOfYear = current.get(Calendar.DAY_OF_YEAR);
		if (age == 17 && birthDayOfYear >= currentDayOfYear) {
			age = 18;
		}

		return age;
	}

	/**
	 * 確認用戶的舊密碼是否正確
	 *
	 * @param username
	 * @param originalPassword
	 * @param locale
	 * @return
	 */
	@Transactional(readOnly = true)
	public Boolean checkPassword(String username, String originalPassword, Locale locale) {
		User user = userRepository.findOneByUsername(username);
		if (passwordEncoder.matches(originalPassword, user.getPassword())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 更新用户号的密码。
	 *
	 * @param mofo 用户号
	 * @param password 新密码
	 * @return 用户号
	 */
	@Transactional
	public Lover changePassword(Lover mofo, String password) {
		mofo.setShadow(passwordEncoder.encode(password));
		return loverRepository.saveAndFlush(mofo);
	}

	@Transactional(readOnly = true)
	public Collection<Lover> fetchRandomEligibles(int count) {
		Set<Lover> suckers = new HashSet<>();

		long total = loverRepository.count(
			LoverSpecification.latestActiveAndLegit()
		);
		for (int i = 0; i < count; ++i) {
			suckers.addAll(
				loverRepository.findAll(
					LoverSpecification.latestActiveAndLegit(),
					PageRequest.of(
						(int) (Math.random() * total),
						1
					)
				).getContent()
			);
		}

		return suckers;
	}

	@Transactional(readOnly = true)
	public Collection<Lover> fetchRandomEligibles(int count, boolean gender) {
		List<Lover> lovers = new ArrayList<>();

		long total = loverRepository.count(
			LoverSpecification.latestActiveAndLegit()
		);
		for (int i = 0; i < count; ++i) {
			lovers.addAll(
				loverRepository.findAll(
					LoverSpecification.latestActiveAndLegit(gender),
					PageRequest.of(
						(int) (Math.random() * total),
						1
					)
				).getContent()
			);
		}

		return lovers;
	}

	@Transactional(readOnly = true)
	public Collection<Lover> fetchRandomly(int count) {
		List<Lover> lovers = new ArrayList<>();

		for (int i = 0; i < count; ++i) {
			lovers.addAll(
				loverRepository.findAll(
					PageRequest.of(
						(int) (Math.random() * loverRepository.countByFake(false)),
						1
					)
				).getContent()
			);
		}

		return lovers;
	}

	@Transactional(readOnly = true)
	public Collection<Lover> fetchRandomly(int count, boolean gender) {
		List<Lover> lovers = new ArrayList<>();

		for (int i = 0; i < count; ++i) {
			lovers.addAll(
				loverRepository.findByFakeAndGender(
					false,
					gender,
					PageRequest.of(
						(int) (Math.random() * loverRepository.countByFakeAndGender(false, gender)),
						1
					)
				).getContent()
			);
		}

		return lovers;
	}

	/**
	 * 期待某种友谊的家伙们。
	 *
	 * @param companionship 友谊
	 * @return 用户号键值
	 */
	@Transactional(readOnly = true)
	public Collection<Integer> findByCompanion(Companionship companionship) {
		Collection<Integer> collection = new ArrayList<>();

		desiredCompanionshipRepository.findByCompanionship(companionship).forEach(desiredCompanionship -> {
			collection.add(
				desiredCompanionship.getLover().getId()
			);
		});

		return new HashSet<>(collection);
	}

	/**
	 * 出没于某地区的家伙们。
	 *
	 * @param location 地区
	 * @return 用户号键值
	 */
	@Transactional(readOnly = true)
	public Collection<Integer> findByLocation(Location location) {
		Collection<Integer> collection = new ArrayList<>();

		appearedLocationRepository.findByLocation(location).forEach(appearedLocation -> {
			collection.add(
				appearedLocation.getLover().getId()
			);
		});

		return new HashSet<>(collection);
	}

	/**
	 * 友谊及地区。
	 *
	 * @param companionship 友谊
	 * @param location 地区
	 * @return 友谊及地区的主键
	 */
	@Transactional(readOnly = true)
	public Collection<Integer> findInceptions(Companionship companionship, Location location) {
		Collection<Integer> collection = new ArrayList<>();

		collection.addAll(findByCompanion(companionship));
		collection.addAll(findByLocation(location));

		return new HashSet<>(collection);
	}

	/**
	 * 谁拉黑了某咪郎
	 *
	 * @param someone 某咪郎
	 * @return 把某咪郎拉黑的用户号们
	 */
	@Transactional(readOnly = true)
	public Collection<Lover> getBlockeds(Lover someone) {
		Collection<Lover> blockeds = new ArrayList<>();

		blacklistRepository.findByBlocked(someone).forEach(blacklist -> {
			blockeds.add(
				blacklist.getBlocker()
			);
		});

		return blockeds;
	}

	/**
	 * 某咪郎拉黑了谁
	 *
	 * @param someone 某咪郎
	 * @return 被某咪郎拉黑的用户号们
	 */
	@Transactional(readOnly = true)
	public Collection<Lover> getBlockers(Lover someone) {
		Collection<Lover> blockers = new ArrayList<>();

		blacklistRepository.findByBlocker(someone).forEach(blacklist -> {
			blockers.add(
				blacklist.getBlocked()
			);
		});

		return blockers;
	}

	/**
	 * 取得某咪郎的期望的友谊。
	 *
	 * @param someone 某咪郎
	 * @return 某咪郎的期望友谊
	 */
	@Transactional(readOnly = true)
	public Collection<Companionship> getCompanionships(Lover someone) {
		Collection<Companionship> companionships = new ArrayList<>();

		desiredCompanionshipRepository.findByLover(someone).forEach(desiredCompanionship -> {
			companionships.add(
				desiredCompanionship.getCompanionship()
			);
		});

		return companionships;
	}
	public Collection<CompanionshipWithInfo> getCompanionshipsWithInfo(Lover someone) {
		Collection<CompanionshipWithInfo> companionships = new ArrayList<>();

		desiredCompanionshipRepository.findByLover(someone).forEach(desiredCompanionship -> {
			CompanionshipWithInfo info=new CompanionshipWithInfo();

			info.setServiceId(desiredCompanionship.getId().getCompanionshipId());

			int hour=0;

			if (desiredCompanionship.getHour()==0){
				hour=1;
			}else{
				hour=desiredCompanionship.getHour();
			}
			int point=0;

			if (desiredCompanionship.getPoint()==0){
				point=1200;
			}else{
				point=desiredCompanionship.getPoint();
			}

			info.setHour(hour);
			info.setPoint(point);

			companionships.add(info);
		});

		return companionships;
	}

	/**
	 * 捞用户号时应排除的家伙们。
	 *
	 * @param someone 某咪郎
	 * @return 黑名单
	 */
	@Transactional(readOnly = true)
	public Collection<Integer> getExceptions(Lover someone) {
		Set<Integer> exceptions = new HashSet<>();

		exceptions.addAll(getThoseIBlock(someone));//某咪郎拉黑的用户们
		exceptions.addAll(getThoseWhoBlockMe(someone));//拉黑某咪郎的用户们
		return exceptions;
	}

	/**
	 * 取得某咪郎的出没地区。
	 *
	 * @param someone 某咪郎
	 * @return 某咪郎的出没地区
	 */
	@Transactional(readOnly = true)
	public Collection<Location> getLocations(Lover someone) {
		return getLocations(someone, false);
	}

	/**
	 * 取得某咪郎的出没地区并随机排列。
	 *
	 * @param someone 某咪郎
	 * @param random 随机排列
	 * @return 某咪郎的出没地区
	 */
	@Transactional(readOnly = true)
	public Collection<Location> getLocations(Lover someone, boolean random) {
		Collection<Location> locations = new ArrayList<>();

		appearedLocationRepository.findByLover(someone).forEach(appearedLocation -> {
			locations.add(appearedLocation.getLocation());
		});

		if (random) {
			Collections.shuffle(new ArrayList<>(locations));
		}

		return locations;
	}

	/**
	 * 找下线用户；支持分页。
	 *
	 * @param lover 上线用户
	 * @param PAGE 第几页
	 * @param SIZE 每页几笔
	 * @return 杰森格式对象
	 */
	@Transactional(readOnly = true)
	public JSONObject getReferralCodeAndDescendants(final Lover lover, final int PAGE, final int SIZE) {
		List<Descendant> descendants = findDescendants(lover);
		final int numberOfElements = descendants.size(),
			numberOfElementsUpToPage = SIZE * (PAGE + 1);

		Pageable pageable = PageRequest.of(PAGE, SIZE);
		Page<Descendant> page = new PageImpl<>(
			descendants.subList(
				SIZE * PAGE,
				numberOfElementsUpToPage > numberOfElements ? numberOfElements : numberOfElementsUpToPage
			),
			pageable,
			numberOfElements
		);

		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(new JSONObject().
				put("referralCode",
					lover.getReferralCode()
				).//上线用户的推荐码
				put("invitedCode",
					Objects.nonNull(lover.getReferrer()) ? lover.getReferrer().getReferralCode() : "無"
				).//上线用户的上线用户
				put("invitedUserName",
					Objects.nonNull(lover.getReferrer()) ? lover.getReferrer().getNickname() : null
				).//上线用户的上线用户id
				put(
					"descendants",
					page.getContent()
				).//下线用户们
				put(
					"pagination",
					new JSONObject().
						put(
							"next",
							page.hasNext() ? page.nextOrLastPageable().getPageNumber() : null
						).
						put(
							"previous",
							page.hasPrevious() ? page.previousOrFirstPageable().getPageNumber() : null
						)
				)//分页元数据
			).
			toJSONObject();
	}

	/**
	 * 我拉黑了谁。
	 *
	 * @param someone 某咪郎
	 * @return 我拉黑的用户键值
	 */
	@Transactional(readOnly = true)
	public Collection<Integer> getThoseIBlock(Lover someone) {
		Collection<Integer> ids;

		try {
			ids = jdbcTemplate.query(
				FileCopyUtils.copyToString(new InputStreamReader(
					thoseIBlockResource.getInputStream(),
					Servant.UTF_8
				)),
				(ps) -> {
					ps.setInt(1, someone.getId());
				},
				(resultSet, rowNum) -> resultSet.getInt("id")
			);
		} catch (IOException iOException) {
			ids = new ArrayList<>();
			LOGGER.debug("我拉黑了谁。", iOException);
		}

		return ids;
	}

	/**
	 * 谁拉黑了我。
	 *
	 * @param someone 某咪郎
	 * @return 拉黑我的用户键值
	 */
	@Transactional(readOnly = true)
	public Collection<Integer> getThoseWhoBlockMe(Lover someone) {
		Collection<Integer> ids;
		try {
			ids = jdbcTemplate.query(
				FileCopyUtils.copyToString(new InputStreamReader(
					thoseWhoBlockMeResource.getInputStream(),
					Servant.UTF_8
				)),
				(ps) -> {
					ps.setInt(1, someone.getId());
				},
				(resultSet, rowNum) -> resultSet.getInt("id")
			);
		} catch (IOException iOException) {
			ids = new ArrayList<>();
			LOGGER.debug("谁拉黑了我。", iOException);
		}

		return ids;
	}

	/**
	 * 追踪(收藏)了谁。
	 *
	 * @param someone 某咪郎
	 * @return 某咪郎追踪(收藏)了的情人
	 */
	@Transactional(readOnly = true)
	public Collection<Lover> getThoseIFollow(Lover someone) {
		List<Lover> followed = new ArrayList();
		for (Follow follow : followRepository.findByFollowing(someone)) {
			followed.add(follow.getFollowed());
		}
		return followed;
	}

	/**
	 * 被谁追踪(收藏)。
	 *
	 * @param someone 某咪郎
	 * @return 追踪(收藏)了某咪郎的情人
	 */
	@Transactional(readOnly = true)
	public Collection<Lover> getThoseWhoFollowMe(Lover someone) {
		List<Lover> following = new ArrayList();
		for (Follow follow : followRepository.findByFollowed(someone)) {
			following.add(follow.getFollowing());
		}
		return following;
	}

	/**
	 * 这家伙有没有某种身份。
	 *
	 * @param mofo 某咪郎
	 * @param roleName 角色名称
	 * @return 真或伪(布林值)
	 */
	@Transactional(readOnly = true)
	public boolean hasRole(Lover mofo, String roleName) {
		try {
			final Role role = roleRepository.findOneByTextualRepresentation(roleName);
			if (privilegeRepository.findByLover(mofo).stream().anyMatch(privilege -> (Objects.equals(privilege.getRole(), role)))) {
				return true;
			}
		} catch (Exception ignore) {
		}
		return false;
	}

	/**
	 * 未封号的用户号们，以活跃降幂排序；适用于首页的最近活跃列表区块。
	 *
	 * @param someone 某咪郎
	 * @param p 第几页
	 * @param s 每页几笔
	 * @return 未封号的(甜心|男士)们
	 */
	@Transactional(readOnly = true)
	public Page<Lover> latestActiveOnTheWall(Lover someone, int p, int s) {
		return loverRepository.findAll(
			LoverSpecification.latestActiveOnTheWall(
				someone,
				new HashSet<>(getExceptions(someone))
			),
			PageRequest.of(p, s)
		);
	}

	/**
	 * 未封号的用户号们，以註冊时间降幂排序；用于首页的最新注册列表区块。
	 *
	 * @param someone 用户号
	 * @param p 第几页
	 * @param s 每页几笔
	 * @return 以註冊时间排序的(甜心|男士)们
	 */
	@Transactional(readOnly = true)
	public Page<Lover> latestRegisteredOnTheWall(Lover someone, int p, int s) {
		return loverRepository.findAll(
			LoverSpecification.latestRegisteredOnTheWall(
				someone,
				new HashSet<>(getExceptions(someone))
			),
			PageRequest.of(p, s)
		);
	}

	/**
	 * 以识别码找用户号。
	 *
	 * @param identifier 识别码
	 * @return 用户号
	 */
	@Transactional(readOnly = true)
	public Lover loadByIdentifier(UUID identifier) {
		return loverRepository.findOneByIdentifier(identifier);
	}

	/**
	 * 以用户名找用户号。
	 *
	 * @param username 国码➕手机号
	 * @return 用户号
	 */
	@Transactional(readOnly = true)
	public Lover loadByUsername(String username) {
		User user = userRepository.findOneByUsername(username);
		if (Objects.isNull(user)) {
			return null;
		}
		return loverRepository.findById(user.getId()).orElseThrow();
	}

	/**
	 * 解析二维码为字符串。
	 *
	 * @param inputStream 二维码输入串流
	 * @param locale 语言环境
	 * @return 杰森对象
	 */
	public JSONObject qrCodeToString(InputStream inputStream, Locale locale) {
		try {
			return new JavaScriptObjectNotation().
				withResult(new QRCodeReader().
					decode(new BinaryBitmap(
						new HybridBinarizer(
							new BufferedImageLuminanceSource(
								ImageIO.read(inputStream)
							)
						)
					)).
					getText()
				).
				withResponse(true).
				toJSONObject();
		} catch (NotFoundException | ChecksumException | FormatException | IOException exception) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"lineFriendInvitation.wrongQRCode",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
	}

	/**
	 * 再激活。
	 *
	 * @param username 国码➕手机号
	 * @param request 请求
	 * @param locale 语言环境
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject reactivate(String username, HttpServletRequest request, Locale locale) {
		Lover lover = loadByUsername(username);
		if (Objects.isNull(lover)) {
			LOGGER.debug("重新激活时找不到情人");
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"reactivate.notFound",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}

		Activation activation = activationRepository.findById(
			lover.getId()
		).orElseThrow();
		if (Objects.isNull(activation.getString()) || Objects.nonNull(activation.getOccurred())) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"reactivate.bringOwlsToAthens",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}

		String string = RandomStringUtils.randomNumeric(6);
		while (activationRepository.countByString(string) > 0) {
			string = RandomStringUtils.randomNumeric(6);
		}
		activation.setString(string);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		Date expiry = calendar.getTime();
		activation.setExpiry(expiry);

		activation = activationRepository.saveAndFlush(activation);

		// 暫時將激活碼送到 Line notify
		List<String> accessTokens = new ArrayList<>();
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_FIRST);
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_SECOND);
		LineMessagingService.notifyDev(
			accessTokens,
			String.format(
				"手機號碼 %s 的激活碼：%s️",
				String.format(
					"0%s",
					lover.getLogin()
				),
				string
			)
		);

		PublishResult publishResult = null;
		if (!servant.isDevelopment() && !servant.isTesting()) {
			publishResult = AMAZON_SNS.publish(
				new PublishRequest().
					withMessage(messageSource.getMessage(
						"reactivate.sms",
						new String[]{
							activation.getString()
						},
						locale
					)).
					withPhoneNumber(String.format(
						"+%s%s",
						lover.getCountry().getCallingCode(),
						lover.getLogin()
					))
			);

			LOGGER.debug(
				"%s.reactivate();\n再激活：{}",
				publishResult
			);
		} else {
			LOGGER.debug("开发模式下再激活不发送短信息。");
		}

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"reactivate.done",
				new String[]{
					activation.getString(),
					String.format(
						"https://%s/activate.asp",
						request.getServerName()
					)
				},
				locale
			)).
			withRedirect("/activate.asp").
			withResponse(true).
			withResult(publishResult).
			toJSONObject();
	}

	/**
	 * 从头重新排序主键值。
	 *
	 * @return 重新排序的用户号们
	 */
	@Transactional
	public int[] reorderPrimaryKey() {
		List<SqlParameterSource> sqlParameterSources = new ArrayList<>();
		List<Integer> jiuList = jdbcTemplate.query(
			"SELECT\"id\"FROM\"qing_ren\"ORDER BY\"id\"",
			(resultSet, rowNum) -> resultSet.getInt("id")
		);
		int xin = 0;
		for (int jiu : jiuList) {
			++xin;
			if (xin < jiu) {
				sqlParameterSources.add(new MapSqlParameterSource().
					addValue(
						"xin",
						xin,
						Types.INTEGER
					).
					addValue(
						"jiu",
						jiu,
						Types.INTEGER
					)
				);
			}
		}
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
			jdbcTemplate.getDataSource()
		);
		return namedParameterJdbcTemplate.batchUpdate(
			"UPDATE\"qing_ren\"SET\"id\"=:xin WHERE\"id\"=:jiu",
			sqlParameterSources.toArray(new SqlParameterSource[0])
		);
	}

	/**
	 * 用户欲重设密码。
	 *
	 * @param resetPassword
	 * @param locale 语言环境
	 * @return 杰森格式对象
	 */
	@Transactional
	public JSONObject resetPassword(ResetPassword resetPassword, Locale locale) {
		/*
		 组合完整用户号
		 */
		Country country = countryRepository.
			findById(resetPassword.getCountry()).
			orElseThrow();
		String login = resetPassword.getLogin(),
			username = String.format(
				"%s%s",
				country.getCallingCode(),
				login
			);

		/*
		 有无此用户号
		 */
		Lover mofo = loverService.loadByUsername(username);
		if (Objects.isNull(mofo)) {
			throw new NoSuchElementException(messageSource.getMessage(
				"_.userNotFound",
				null,
				locale
			));
		}

		/*
		 此用户号曾欲重设密码吗
		 */
		ResetShadow resetShadow;
		try {
			resetShadow = resetShadowRepository.
				findOneByLover(mofo).
				orElseThrow();
		} catch (NoSuchElementException ignore) {
			resetShadow = new ResetShadow();
			resetShadow.setId(mofo.getId());
			resetShadow.setLover(mofo);
		}

		/*
		 有效期
		 */
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		Date expiry = calendar.getTime();
		resetShadow.setExpiry(expiry);

		/*
		 重设密码字符串
		 */
		String string = RandomStringUtils.randomNumeric(6);
		while (activationRepository.countByString(string) > 0) {
			string = RandomStringUtils.randomNumeric(6);
		}
		resetShadow.setString(string);
		resetShadow.setOccurred(new Date(System.currentTimeMillis()));
		resetShadow = resetShadowRepository.saveAndFlush(resetShadow);

		String uri = String.format(
			"/resetPassword/%s/",
			String.format(
				"%1$" + 8 + "s",
				Integer.toHexString(mofo.getId())
			).replace(' ', '0')
		);

		if (!servant.isDevelopment() && !servant.isTesting()) {
			AMAZON_SNS.publish(
				new PublishRequest().
					withMessage(messageSource.getMessage(
						"resetPassword.sms",
						new String[]{
							resetShadow.getString()
						},
						locale
					)).
					withPhoneNumber(String.format(
						"+%s%s",
						country.getCallingCode(),
						login
					))
			);
		} else {
			LOGGER.debug("开发模式下重设密码不发送短信息。");
		}

		// 暫時將激活碼送到 Line notify
		List<String> accessTokens = new ArrayList<>();
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_FIRST);
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_SECOND);
		LineMessagingService.notifyDev(
			accessTokens,
			String.format(
				"去 %s 輸入驗證碼 %s",
				uri,
				resetShadow.getString()
			)
		);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"resetShadow.appliedFor",
				null,
				locale
			)).
			withRedirect(uri).
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 用户重设密码。
	 *
	 * @param hexadecimalId 十六进制主键
	 * @param string 字符串
	 * @param shadow 新密码
	 * @param locale 语言环境
	 * @return 杰森格式对象
	 */
	@Transactional
	public JSONObject resetPassword(String hexadecimalId, String string, String shadow, Locale locale) {
		/*
		 有无此用户号
		 */
		Lover mofo;
		try {
			mofo = loverRepository.findById(
				Servant.hexadecimalToDecimal(hexadecimalId)
			).orElseThrow();
		} catch (NoSuchElementException ignore) {
			throw new RuntimeException(messageSource.getMessage(
				"_.userNotFound",
				null,
				locale
			));
		}

		/*
		 此用户号曾欲重设密码吗
		 */
		ResetShadow resetPassword;
		try {
			resetPassword = resetShadowRepository.
				findOneByLover(mofo).
				orElseThrow();
		} catch (NoSuchElementException ignore) {
			throw new RuntimeException(messageSource.getMessage(
				"resetPassword.notFound",
				null,
				locale
			));
		}

		/*
		 核对验证码
		 */
		if (!string.equalsIgnoreCase(resetPassword.getString())) {
			throw new RuntimeException(messageSource.getMessage(
				"resetPassword.notAuthentic",
				null,
				locale
			));
		}

		/*
		 重设密码并清除重设密码申请
		 */
		changePassword(mofo, shadow);
		resetPassword.setString(null);
		resetShadowRepository.saveAndFlush(resetPassword);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"resetPassword.done",
				null,
				locale
			)).
			withRedirect("/signIn.asp").
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 储存用户号。
	 *
	 * @param lover 用户号
	 * @return 用户号
	 */
	@Transactional
	public Lover saveLover(Lover lover) {
		return loverRepository.saveAndFlush(lover);
	}

	/**
	 * 登录。
	 *
	 * @param signUp 登录时送来的参数与值
	 * @param request 请求
	 * @param locale 语言环境
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject signUp(SignUp signUp, HttpServletRequest request, Locale locale) {
		Country country = countryRepository.
			findById(signUp.getCountry()).
			orElseThrow();
		String login = signUp.getLogin();

		/*
		 用户号已存在⁉️
		 */
		if (loverRepository.countByCountryAndLogin(country, login) > 0) {
			throw new RuntimeException("signUp.exists");
		}

		/*
		 已成年⁉️
		 */
		if (calculateAge(signUp.getBirthday()) < 18) {
			throw new RuntimeException("signUp.mustBeAtLeast18yrsOld");
		}

		UUID identifier = UUID.randomUUID();
		String referralCode = signUp.getReferralCode();

		Lover lover = new Lover();
		lover.setIdentifier(identifier);
		lover.setCountry(country);
		lover.setLogin(login);
		lover.setGender(signUp.getGender());
		lover.setBirthday(signUp.getBirthday());
		lover.setProfileImage(identifier.toString());
//		lover.setAboutMe("正在找尋一段比閨蜜還親密的養蜜關係");
		lover.setAboutMe(
				"A. 關於我 (專長、興趣、專業)\n" +
				"\n" +
				"----------------------------------------\n" +
				"\n" +
				"專業吃貨，可以推薦優質餐廳一同品嘗美食\n" +
				"\n" +
				"服飾店員，可以陪逛街幫忙挑選穿搭與改造\n" +
				"\n" +
				"電影影評，可以一同賞析熱門電影加以討論\n" +
				"\n" +
				"B. 注意事項\n" +
				"\n" +
				"--------------------------------------\n" +
				"\n" +
				"非公開場合舉凡 招待所、MTV、KTV、車上\n" +
				"\n" +
				"都無法接受接單");
		lover.setIdealConditions("合得來的養蜜");
		lover.setGreeting("嗨，有機會成為養蜜嗎?");
		lover.setRegistered(new Date(System.currentTimeMillis()));
		if (Objects.nonNull(referralCode) && !referralCode.isBlank()) {
			Lover referrer = loverRepository.findByReferralCode(
				referralCode
			);
			try {
				findAncestry(referrer, new ArrayList<>());
			} catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			lover.setReferrer(referrer);
		}
		lover = loverRepository.saveAndFlush(lover);

		// 將自己加入封鎖名單
		BlacklistKey id = new BlacklistKey();
		id.setInitiativeId(lover.getId());
		id.setPassiveId(lover.getId());

		Blacklist blacklist = new Blacklist();
		blacklist.setId(id);
		blacklist.setBlocker(lover);
		blacklist.setBlocked(lover);
		blacklistRepository.saveAndFlush(blacklist);

		// 上傳預設大頭照
		amazonWebServices.copyDefaultImageToLover(
			identifier.toString(),
			lover
		);

		applicationEventPublisher.publishEvent(new SignedUpEvent(
			lover,
			request.getServerName(),
			request.getLocale()
		));

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"signUp.done",
				null,
				locale
			)).
			withRedirect("/activate.asp").
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 用户已登录。
	 *
	 * @param lover 用户号
	 * @return 激活
	 */
	@Transactional
	public Activation signedUp(Lover lover) {
		String string = RandomStringUtils.randomNumeric(6);
		while (activationRepository.countByString(string) > 0) {
			string = RandomStringUtils.randomNumeric(6);
		}

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		Date expiry = calendar.getTime();

		// 暫時將激活碼送到 Line notify
		List<String> accessTokens = new ArrayList<>();
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_FIRST);
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_SECOND);
		LineMessagingService.notifyDev(
			accessTokens,
			String.format(
				"手機號碼 %s 的激活碼：%s️",
				String.format(
					"0%s",
					lover.getLogin()
				),
				string
			));

		return activationRepository.saveAndFlush(new Activation(
			lover.getId(),
			lover,
			string,
			expiry
		));
	}

	/**
	 * 令用户开始体验，为期一天。
	 *
	 * @param mofo 用户号
	 * @param trialCode
	 * @return 体验截止时戳
	 * @throws RuntimeException 若用户曾付费或曾体验过
	 */
	@Transactional
	public Date trial(Lover mofo, TrialCode trialCode) {
		if (Objects.nonNull(mofo.getVip())) {
			throw new RuntimeException("trial.disqualified");
		}
		long currentTimeMillis = System.currentTimeMillis();
		History history = new History(
			mofo,
			HistoryService.BEHAVIOR_TRIAL_CODE,
			trialCode,
			Long.toString(currentTimeMillis)
		);
		history = historyRepository.saveAndFlush(history);

		UsedTrialCode usedTrialCode = new UsedTrialCode(
			mofo,
			trialCode
		);
		usedTrialCode = usedTrialCodeRepository.saveAndFlush(usedTrialCode);

		mofo.setVip(new Date(
			history.getOccurred().getTime() + Servant.MILLISECONDS_IN_A_DAY
		));
		mofo.setMaleSpecies(MaleSpecies.VIP);
		loverRepository.saveAndFlush(mofo);

		return mofo.getVip();
	}

	@Transactional(readOnly = true)
	public boolean isValidCode(String code) {
		return trialCodeRepository.countByCode(code) > 0;
	}

	/**
	 * 是否為單日體驗 VIP
	 *
	 * @param lover
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isTrial(Lover lover) {
		Date vipExpiration = lover.getVip();
		Lover.MaleSpecies maleSpecies = lover.getMaleSpecies();
		return (Objects.nonNull(vipExpiration) && vipExpiration.after(new Date(System.currentTimeMillis())))
			&& (Objects.nonNull(maleSpecies) && Objects.equals(maleSpecies, Lover.MaleSpecies.VIP))
			&& historyRepository.countByInitiativeAndBehaviorOrderByOccurredDesc(lover, BEHAVIOR_MONTHLY_CHARGED) < 1;
	}

	/**
	 * 是否为短期貴賓 VIP⁉️
	 *
	 * @param lover 用户
	 * @return 布尔值
	 */
	@Transactional(readOnly = true)
	public boolean isVIP(Lover lover) {
		Date vipExpiration = lover.getVip();
		Lover.MaleSpecies maleSpecies = lover.getMaleSpecies();
		return (Objects.nonNull(vipExpiration) && vipExpiration.after(new Date(System.currentTimeMillis())))
			&& (Objects.nonNull(maleSpecies) && Objects.equals(maleSpecies, Lover.MaleSpecies.VIP))
			&& historyRepository.countByInitiativeAndBehaviorOrderByOccurredDesc(lover, BEHAVIOR_MONTHLY_CHARGED) > 0;
	}

	/**
	 * 是否为長期貴賓 VVIP⁉️
	 *
	 * @param lover
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isVVIP(Lover lover) {
		// 貴賓到期日
		Date vipExpiration = lover.getVip();
		Lover.MaleSpecies maleSpecies = lover.getMaleSpecies();
		return (Objects.nonNull(vipExpiration) && vipExpiration.after(new Date(System.currentTimeMillis())))
			&& (Objects.nonNull(maleSpecies) && Objects.equals(maleSpecies, Lover.MaleSpecies.VVIP));
	}

	public Document readDocument(Lover someone, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		Element loverElement = document.createElement("lover");
		loverElement.setAttribute(
			"identifier",
			someone.getIdentifier().toString()
		);
		documentElement.appendChild(loverElement);

		// 確認按鈕
		documentElement.setAttribute(
			"i18n-confirm",
			messageSource.getMessage(
				"confirm.submit",
				null,
				locale
			)
		);

		// 取消按鈕
		documentElement.setAttribute(
			"i18n-cancel",
			messageSource.getMessage(
				"cancel",
				null,
				locale
			)
		);

		// 上一次登入
		loverElement.setAttribute(
			"i18n-lastActive",
			messageSource.getMessage(
				"last.active",
				null,
				locale
			)
		);

		// 是否為長期貴賓 vvip
		if (isVVIP(someone)) {
			loverElement.setAttribute("vvip", null);
		}
		// 是否為短期貴賓 vip
		if (isVIP(someone)) {
			loverElement.setAttribute("vip", null);
		}

		if (Objects.nonNull(someone.getRelief())) {
			Boolean relief = someone.getRelief();
			loverElement.setAttribute(
				"relief",
				relief.toString()
			);
		}

		String inviteMeAsFreind = someone.getInviteMeAsLineFriend();
		if (Objects.nonNull(inviteMeAsFreind) && !inviteMeAsFreind.isBlank() && !inviteMeAsFreind.isEmpty()) {
			String uri = someone.getInviteMeAsLineFriend();
			Boolean isLine = Servant.isLine(URI.create(uri));
			//Boolean isWeChat = Servant.isWeChat(URI.create(uri));

			loverElement.setAttribute(
				"socialMedia",
				isLine ? "line" : "weChat"
			);
		}

		Element profileImageElement = document.createElement("profileImage");
		if (Objects.nonNull(someone.getProfileImage())) {
			profileImageElement.setTextContent(
				String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					someone.getProfileImage()
				)
			);
		}
		loverElement.appendChild(profileImageElement);

		List<Picture> pictures = pictureRepository.findByLover(someone);
		pictures.stream().map(picture -> {
			Element pictureElement = document.createElement("picture");
			pictureElement.setTextContent(
				picture.getIdentifier().toString()
			);
			return pictureElement;
		}).forEachOrdered(pictureElement -> {
			loverElement.appendChild(pictureElement);
		});

		Collection<Location> locations = getLocations(someone);
		if (!locations.isEmpty()) {
			locations.stream().map(location -> {
				Element locationElement = document.createElement("location");
				locationElement.setAttribute("id", location.getId().toString());
				locationElement.setTextContent(
					messageSource.getMessage(
						location.getName(),
						null,
						locale
					)
				);
				return locationElement;
			}).forEachOrdered(locationElement -> {
				loverElement.appendChild(locationElement);
			});
		}

		Collection<Companionship> companionships = getCompanionships(someone);
		if (!companionships.isEmpty()) {
			getCompanionships(someone).stream().map(companionship -> {
				Element serviceElement = document.createElement("service");
				serviceElement.setAttribute(
					"id",
					companionship.getId().toString()
				);
				serviceElement.setTextContent(
					messageSource.getMessage(
						companionship.getName(),
						null,
						locale
					)
				);
				return serviceElement;
			}).forEachOrdered(serviceElement -> {
				loverElement.appendChild(serviceElement);
			});
		}

		if (Objects.nonNull(someone.getNickname())) {
			Element nicknameElement = document.createElement("nickname");
			nicknameElement.setTextContent(someone.getNickname());
			loverElement.appendChild(nicknameElement);
		}

		Date birth = someone.getBirthday();
		if (Objects.nonNull(birth)) {
			Element ageElement = document.createElement("age");
			ageElement.setTextContent(
				calculateAge(someone).toString()
			);
			loverElement.appendChild(ageElement);
		}

		if (Objects.nonNull(someone.getGender())) {
			Boolean gender = someone.getGender();
			Element genderElement = document.createElement("gender");
			genderElement.setTextContent(
				gender ? messageSource.getMessage(
						"gender.male",
						null,
						locale
					) : messageSource.getMessage(
					"gender.female",
					null,
					locale
				)
			);
			genderElement.setAttribute(
				"gender",
				gender ? "male" : "female"
			);
			loverElement.appendChild(genderElement);
		}

		if (Objects.nonNull(someone.getAboutMe())) {
			String html = servant.markdownToHtml(someone.getAboutMe());
			Element aboutMeElement = document.createElement("aboutMe");
			CDATASection cDATASection = document.createCDATASection(html);
			aboutMeElement.appendChild(cDATASection);
			loverElement.appendChild(aboutMeElement);
		}

		if (Objects.nonNull(someone.getBodyType())) {
			Element bodyTypeElement = document.createElement("bodyType");
			bodyTypeElement.setTextContent(
				messageSource.getMessage(
					someone.getBodyType().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(bodyTypeElement);
		}

		if (Objects.nonNull(someone.getHeight())) {
			Element heightElement = document.createElement("height");
			heightElement.setTextContent(someone.getHeight().toString());
			loverElement.appendChild(heightElement);
		}

		if (Objects.nonNull(someone.getWeight())) {
			Element weightElement = document.createElement("weight");
			weightElement.setTextContent(someone.getWeight().toString());
			loverElement.appendChild(weightElement);
		}

		if (Objects.nonNull(someone.getEducation())) {
			Element educationElement = document.createElement("education");
			educationElement.setTextContent(
				messageSource.getMessage(
					someone.getEducation().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(educationElement);
		}

		if (Objects.nonNull(someone.getMarriage())) {
			Element marriageElement = document.createElement("marriage");
			marriageElement.setTextContent(
				messageSource.getMessage(
					someone.getMarriage().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(marriageElement);
		}

		if (Objects.nonNull(someone.getOccupation())) {
			Element occupationElement = document.createElement("occupation");
			occupationElement.setTextContent(someone.getOccupation());
			loverElement.appendChild(occupationElement);
		}

		if (Objects.nonNull(someone.getSmoking())) {
			Element smokingElement = document.createElement("smoking");
			smokingElement.setTextContent(
				messageSource.getMessage(
					someone.getSmoking().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(smokingElement);
		}

		if (Objects.nonNull(someone.getDrinking())) {
			Element drinkingElement = document.createElement("drinking");
			drinkingElement.setTextContent(
				messageSource.getMessage(
					someone.getDrinking().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(drinkingElement);
		}

		if (Objects.nonNull(someone.getRelationship())) {
			Element relationshipElement = document.createElement("relationship");
			relationshipElement.setTextContent(
				messageSource.getMessage(
					someone.getRelationship().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(relationshipElement);
		}

		if (someone.getGender() && Objects.nonNull(someone.getAnnualIncome())) {
			Element annualIncomeElement = document.createElement("annualIncome");
			AnnualIncome annualIncome = someone.getAnnualIncome();
			annualIncomeElement.setTextContent(
				messageSource.getMessage(
					annualIncome.getName(),
					null,
					locale
				)
			);
			loverElement.appendChild(annualIncomeElement);
		}

		if (!someone.getGender() && Objects.nonNull(someone.getAllowance())) {
			Element allowanceElement = document.createElement("allowance");
			Allowance allowance = someone.getAllowance();
			allowanceElement.setTextContent(
				messageSource.getMessage(
					allowance.getName(),
					null,
					locale
				)
			);
			loverElement.appendChild(allowanceElement);
		}

		if (Objects.nonNull(someone.getIdealConditions())) {
			String html = servant.markdownToHtml(someone.getIdealConditions());
			Element idealConditionsElement = document.createElement("idealConditions");
			CDATASection cDATASection = document.createCDATASection(html);
			idealConditionsElement.appendChild(cDATASection);
			loverElement.appendChild(idealConditionsElement);
		}

		if (Objects.nonNull(someone.getInviteMeAsLineFriend())) {
			Element inviteMeAsLineFriendElement = document.createElement("inviteMeAsLineFriend");
			inviteMeAsLineFriendElement.setTextContent(someone.getInviteMeAsLineFriend());
			loverElement.appendChild(inviteMeAsLineFriendElement);
		}

		if (Objects.nonNull(someone.getGreeting())) {
			Element greetingElement = document.createElement("greeting");
			greetingElement.setTextContent(someone.getGreeting());
			loverElement.appendChild(greetingElement);
		}

		if (Objects.nonNull(someone.getActive())) {
			Element activeElement = document.createElement("active");
			activeElement.setTextContent(calendarToString(someone.getActive()));
			loverElement.appendChild(activeElement);
		}

		Page<History> ratePage = historyRepository.
			findByPassiveAndBehaviorOrderByOccurredDesc(
				someone,
				History.Behavior.PING_JIA,
				PageRequest.of(0, 3)
			);
		if (ratePage.getTotalPages() <= 1) {
			loverElement.setAttribute("lastPage", null);
		}

		if (Objects.nonNull(ratePage.getContent())) {
			ratePage.getContent().forEach(rate -> {
				Element rateElement = document.createElement("rate");
				loverElement.appendChild(rateElement);
				rateElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						Servant.STATIC_HOST,
						rate.getInitiative().getProfileImage()
					));
				rateElement.setAttribute(
					"nickname",
					rate.getInitiative().getNickname()
				);
				rateElement.setAttribute(
					"time",
					DATE_FORMATTER.format(
						Servant.toTaipeiZonedDateTime(
							rate.getOccurred()
						).withZoneSameInstant(
							Servant.ASIA_TAIPEI_ZONE_ID
						)
					).replaceAll("\\+\\d{2}$", "")
				);
				rateElement.setAttribute(
					"rate",
					rate.getRate().toString()
				);
				rateElement.setAttribute(
					"comment",
					rate.getComment()
				);
				rateElement.setAttribute(
					"identifier",
					rate.getInitiative().getIdentifier().toString()
				);
			});
			loverElement.setAttribute(
				"totalPages",
				Integer.toString(ratePage.getTotalPages())
			);
		}

		return document;
	}

	@Transactional(readOnly = true)
	public Document writeDocument(Lover lover, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		Element loverElement = document.createElement("lover");
		loverElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"editProfile.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(loverElement);

		// 確認按鈕
		documentElement.setAttribute(
			"i18n-confirm",
			messageSource.getMessage(
				"confirm.submit",
				null,
				locale
			)
		);

		// 取消按鈕
		documentElement.setAttribute(
			"i18n-cancel",
			messageSource.getMessage(
				"cancel",
				null,
				locale
			)
		);

		if (Objects.nonNull(lover.getGender())) {
			Boolean gender = lover.getGender();
			Element genderElement = document.createElement("gender");
			genderElement.setAttribute(
				"gender",
				gender ? "male" : "female"
			);
			loverElement.appendChild(genderElement);
		}

		for (Lover.BodyType bodyType : Lover.BodyType.values()) {
			Element bodyTypeElement = document.createElement("bodyType");
			bodyTypeElement.setTextContent(
				messageSource.getMessage(
					bodyType.toString(),
					null,
					locale
				)
			);
			bodyTypeElement.setAttribute(
				"bodyTypeEnum", bodyType.toString()
			);
			if (Objects.equals(lover.getBodyType(), bodyType)) {
				bodyTypeElement.setAttribute(
					"bodyTypeSelected", ""
				);
			}
			loverElement.appendChild(bodyTypeElement);
		}

		for (Lover.Education education : Lover.Education.values()) {
			Element educationElement = document.createElement("education");
			educationElement.setTextContent(
				messageSource.getMessage(
					education.toString(),
					null,
					locale
				)
			);
			educationElement.setAttribute(
				"educationEnum", education.toString()
			);
			if (Objects.equals(lover.getEducation(), education)) {
				educationElement.setAttribute(
					"educationSelected", ""
				);
			}
			loverElement.appendChild(educationElement);
		}

		for (Lover.Marriage marriage : Lover.Marriage.values()) {
			Element marriageElement = document.createElement("marriage");
			marriageElement.setTextContent(
				messageSource.getMessage(
					marriage.toString(),
					null,
					locale
				)
			);
			marriageElement.setAttribute(
				"marriageEnum", marriage.toString()
			);
			if (Objects.equals(lover.getMarriage(), marriage)) {
				marriageElement.setAttribute(
					"marriageSelected", ""
				);
			}
			loverElement.appendChild(marriageElement);
		}

		for (Lover.Smoking smoking : Lover.Smoking.values()) {
			Element smokingElement = document.createElement("smoking");
			smokingElement.setTextContent(
				messageSource.getMessage(
					smoking.toString(),
					null,
					locale
				)
			);
			smokingElement.setAttribute(
				"smokingEnum", smoking.toString()
			);
			if (Objects.equals(lover.getSmoking(), smoking)) {
				smokingElement.setAttribute(
					"smokingSelected", ""
				);
			}
			loverElement.appendChild(smokingElement);
		}

		for (Lover.Drinking drinking : Lover.Drinking.values()) {
			Element drinkingElement = document.createElement("drinking");
			drinkingElement.setTextContent(
				messageSource.getMessage(
					drinking.toString(),
					null,
					locale
				)
			);
			drinkingElement.setAttribute(
				"drinkingEnum", drinking.toString()
			);
			if (Objects.equals(lover.getDrinking(), drinking)) {
				drinkingElement.setAttribute(
					"drinkingSelected", ""
				);
			}
			loverElement.appendChild(drinkingElement);
		}

		for (Lover.Relationship relationship : Lover.Relationship.values()) {
			Element relationshipElement = document.createElement("relationship");
			relationshipElement.setTextContent(
				messageSource.getMessage(
					relationship.toString(),
					null,
					locale
				)
			);
			relationshipElement.setAttribute(
				"relationshipEnum", relationship.toString()
			);
			if (Objects.equals(lover.getRelationship(), relationship)) {
				relationshipElement.setAttribute(
					"relationshipSelected", ""
				);
			}
			loverElement.appendChild(relationshipElement);
		}

		locationRepository.findAll().stream().map(location -> {
			Element locationElement = document.createElement("location");
			locationElement.setTextContent(
				messageSource.getMessage(
					location.getName(),
					null,
					locale
				)
			);
			locationElement.setAttribute(
				"locationID", location.getId().toString()
			);
			getLocations(lover).stream().filter(l -> (Objects.equals(l, location))).forEachOrdered(_item -> {
				locationElement.setAttribute(
					"locationSelected", ""
				);
			});
			return locationElement;
		}).forEachOrdered(locationElement -> {
			loverElement.appendChild(locationElement);
		});

		companionshipRepository.findAll().stream().map(companionship -> {
			Element serviceElement = document.createElement("service");
			serviceElement.setTextContent(
				messageSource.getMessage(
					companionship.getName(),
					null,
					locale
				)
			);

			serviceElement.setAttribute(
				"serviceID", companionship.getId().toString()
			);

			getCompanionships(lover).stream().filter(c -> (Objects.equals(c, companionship))).forEachOrdered(_item -> {
				serviceElement.setAttribute(
					"serviceSelected", ""
				);
			});

			serviceElement.setAttribute(
					"serviceHour", String.valueOf(1)
			);
			serviceElement.setAttribute(
					"servicePoint", String.valueOf(1200)
			);

			getCompanionshipsWithInfo(lover).stream().filter(c -> (c.getServiceId()==companionship.getId())).forEachOrdered(_item -> {
				serviceElement.setAttribute(
						"serviceHour", String.valueOf(_item.getHour())
				);
				serviceElement.setAttribute(
						"servicePoint", String.valueOf(_item.getPoint())
				);
			});


			return serviceElement;
		}).forEachOrdered(serviceElement -> {
			loverElement.appendChild(serviceElement);
		});

		if (lover.getGender()) {
			annualIncomeRepository.findAllByOrderByIdAsc().stream().map(annualIncome -> {
				Element annualIncomeElement = document.createElement("annualIncome");
				annualIncomeElement.setTextContent(
					messageSource.getMessage(
						annualIncome.getName(),
						null,
						locale
					)
				);
				annualIncomeElement.setAttribute(
					"annualIncomeID", annualIncome.getId().toString()
				);
				if (Objects.equals(lover.getAnnualIncome(), annualIncome)) {
					annualIncomeElement.setAttribute(
						"annualIncomeSelected", ""
					);
				}
				return annualIncomeElement;
			}).forEachOrdered(annualIncomeElement -> {
				loverElement.appendChild(annualIncomeElement);
			});
		}

		if (!lover.getGender()) {
			allowanceRepository.findAllByOrderByIdAsc().stream().map(allowance -> {
				Element allowanceElement = document.createElement("allowance");
				allowanceElement.setTextContent(
					messageSource.getMessage(
						allowance.getName(),
						null,
						locale
					)
				);
				allowanceElement.setAttribute(
					"allowanceID", allowance.getId().toString()
				);
				if (Objects.equals(lover.getAllowance(), allowance)) {
					allowanceElement.setAttribute(
						"allowanceSelected", ""
					);
				}
				return allowanceElement;
			}).forEachOrdered(allowanceElement -> {
				loverElement.appendChild(allowanceElement);
			});
		}

		if (Objects.nonNull(lover.getNickname())) {
			Element nicknameElement = document.createElement("nickname");
			nicknameElement.setTextContent(lover.getNickname());
			loverElement.appendChild(nicknameElement);
		}

		Date birth = lover.getBirthday();
		if (Objects.nonNull(birth)) {
			Element birthdayElement = document.createElement("birthday");
			birthdayElement.setTextContent(
				new SimpleDateFormat("yyyy-MM-dd").format(birth
				)
			);
			loverElement.appendChild(birthdayElement);
		}

		if (Objects.nonNull(lover.getAboutMe())) {
			Element aboutMeElement = document.createElement("aboutMe");
			aboutMeElement.setTextContent(lover.getAboutMe());
			loverElement.appendChild(aboutMeElement);
		}

		if (Objects.nonNull(lover.getHeight())) {
			Element heightElement = document.createElement("height");
			heightElement.setTextContent(lover.getHeight().toString());
			loverElement.appendChild(heightElement);
		}

		if (Objects.nonNull(lover.getWeight())) {
			Element weightElement = document.createElement("weight");
			weightElement.setTextContent(lover.getWeight().toString());
			loverElement.appendChild(weightElement);
		}

		if (Objects.nonNull(lover.getOccupation())) {
			Element occupationElement = document.createElement("occupation");
			occupationElement.setTextContent(lover.getOccupation());
			loverElement.appendChild(occupationElement);
		}

		if (Objects.nonNull(lover.getIdealConditions())) {
			Element idealConditionsElement = document.createElement("idealConditions");
			idealConditionsElement.setTextContent(lover.getIdealConditions());
			loverElement.appendChild(idealConditionsElement);
		}

		if (Objects.nonNull(lover.getInviteMeAsLineFriend())) {
			Element inviteMeAsLineFriendElement = document.createElement("inviteMeAsLineFriend");
			inviteMeAsLineFriendElement.setTextContent(lover.getInviteMeAsLineFriend());
			loverElement.appendChild(inviteMeAsLineFriendElement);
		}

		if (Objects.nonNull(lover.getGreeting())) {
			Element greetingElement = document.createElement("greeting");
			greetingElement.setTextContent(lover.getGreeting());
			loverElement.appendChild(greetingElement);
		}

		return document;
	}

	public JSONObject updatePersonalInfo(Lover lover, Lover model) {

		if (model.getNickname().isBlank() || model.getNickname().isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason("請填寫暱稱").
				withResponse(false).
				toJSONObject();
		}

		if (Objects.isNull(model.getHeight())) {
			return new JavaScriptObjectNotation().
				withReason("請填寫身高").
				withResponse(false).
				toJSONObject();
		}

		if (Objects.isNull(model.getWeight())) {
			return new JavaScriptObjectNotation().
				withReason("請填寫體重").
				withResponse(false).
				toJSONObject();
		}

		if (model.getOccupation().isBlank()) {
			return new JavaScriptObjectNotation().
				withReason("請填寫職業").
				withResponse(false).
				toJSONObject();
		}

//		if (!lover.getGender() && (model.getInviteMeAsLineFriend().isBlank() || model.getInviteMeAsLineFriend().isEmpty())) {
//			return new JavaScriptObjectNotation().
//				withReason("請上傳 LINE 或 WeChat 的 QRcode").
//				withResponse(false).
//				toJSONObject();
//		}

		if (Objects.isNull(model.getBodyType())) {
			return new JavaScriptObjectNotation().
				withReason("請擇一體型").
				withResponse(false).
				toJSONObject();
		}

		if (Objects.isNull(model.getEducation())) {
			return new JavaScriptObjectNotation().
				withReason("請擇一學歷").
				withResponse(false).
				toJSONObject();
		}

		if (Objects.isNull(model.getSmoking())) {
			return new JavaScriptObjectNotation().
				withReason("請擇一吸菸習慣").
				withResponse(false).
				toJSONObject();
		}

		if (Objects.isNull(model.getDrinking())) {
			return new JavaScriptObjectNotation().
				withReason("請擇一飲酒習慣").
				withResponse(false).
				toJSONObject();
		}

//		if (Objects.isNull(model.getRelationship())) {
//			return new JavaScriptObjectNotation().
//				withReason("請擇一相處關係").
//				withResponse(false).
//				toJSONObject();
//		}
//
//		if (lover.getGender() && Objects.isNull(model.getAnnualIncome())) {
//			return new JavaScriptObjectNotation().
//				withReason("請擇一年收入").
//				withResponse(false).
//				toJSONObject();
//		}
//
//		if (!lover.getGender() && Objects.isNull(model.getAllowance())) {
//			return new JavaScriptObjectNotation().
//				withReason("請擇一期望零用金").
//				withResponse(false).
//				toJSONObject();
//		}

		if (model.getAboutMe().isBlank() || model.getAboutMe().isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason("請輸入關於我的內容").
				withResponse(false).
				toJSONObject();
		}

		if (model.getIdealConditions().isBlank() || model.getIdealConditions().isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason("請輸入你的理想對象型").
				withResponse(false).
				toJSONObject();
		}

		if (model.getGreeting().isBlank() || model.getGreeting().isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason("請輸入你的招呼語").
				withResponse(false).
				toJSONObject();
		}

		if (getLocations(lover).isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason("請至少填入一個約會地區").
				withResponse(false).
				toJSONObject();
		}

		if (getCompanionships(lover).isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason("請至少填入一種提供服務").
				withResponse(false).
				toJSONObject();
		}

		lover.setNickname(model.getNickname());
		lover.setHeight(model.getHeight());
		lover.setWeight(model.getWeight());
		lover.setOccupation(model.getOccupation());
		lover.setInviteMeAsLineFriend(model.getInviteMeAsLineFriend());
		lover.setBodyType(model.getBodyType());
		lover.setEducation(model.getEducation());
		lover.setMarriage(model.getMarriage());
		lover.setSmoking(model.getSmoking());
		lover.setDrinking(model.getDrinking());
		lover.setRelationship(model.getRelationship());
		lover.setAnnualIncome(model.getAnnualIncome());
		lover.setAllowance(model.getAllowance());
		lover.setAboutMe(model.getAboutMe());
		lover.setIdealConditions(model.getIdealConditions());
		lover.setGreeting(model.getGreeting());

		loverRepository.saveAndFlush(lover);

		return new JavaScriptObjectNotation().
			withReason("update successfully").
			withResponse(true).
			toJSONObject();
	}

	public Document withdrawalDocument(Lover lover, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		// 甜心剩餘的可提領車馬費(抽一成)
		Long leftPoints = honeyLeftPointsBefore7Days(lover);
		documentElement.setAttribute(
			"totalPoints",
			leftPoints.toString()
		);

		String Relief="";
		if (lover.getRelief() !=null && true){
			Relief="1";
		}else{
			Relief="0";
		}
		documentElement.setAttribute(
			"Relief",
				Relief
		);

		if (withdrawalInfoRepository.countByHoney(lover) > 0) {
			Element wireElement = document.createElement("wire");
			documentElement.appendChild(wireElement);
			wireElement.setAttribute(
				"bankCode",
				withdrawalInfoRepository.findByHoney(lover).getWireTransferBankCode()
			);
			wireElement.setAttribute(
				"branchCode",
				withdrawalInfoRepository.findByHoney(lover).getWireTransferBranchCode()
			);
			wireElement.setAttribute(
				"accountName",
				withdrawalInfoRepository.findByHoney(lover).getWireTransferAccountName()
			);
			wireElement.setAttribute(
				"accountNumber",
				withdrawalInfoRepository.findByHoney(lover).getWireTransferAccountNumber()
			);
		}

		documentElement.setAttribute(
			"before7days",
			DATE_FORMATTER.format(
				Servant.toTaipeiZonedDateTime(
					before7DaysAgo()
				).withZoneSameInstant(
					Servant.ASIA_TAIPEI_ZONE_ID
				)
			));

		// 目前可提領的紀錄
		historyRepository.findAll(Specifications.withdrawal(lover)).forEach(history -> {
			// 已經退回的不列出
			if (Objects.nonNull(historyRepository.findTop1ByBehaviorAndHistory(BEHAVIOR_RETURN_FARE, history))) {
				return;
			}
			Element recordElement = document.createElement("record");
			documentElement.appendChild(recordElement);

			recordElement.setAttribute(
				"date",
				DATE_FORMATTER.format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				));

			recordElement.setAttribute(
				"male",
				history.getInitiative().getNickname()
			);

			recordElement.setAttribute(
				"maleId",
				history.getInitiative().getIdentifier().toString()
			);

			recordElement.setAttribute(
				"type",
				messageSource.getMessage(
					history.getBehavior().name(),
					null,
					locale
				));

			@SuppressWarnings("null")
			Short points = Objects.equals(history.getBehavior().name(), "CHE_MA_FEI") ? history.getPoints() : (short) (history.getPoints() / 2);
			// ME點
			recordElement.setAttribute(
				"mePoints",
				Integer.toString(Math.abs(points))
			);
			// 抽完1成可提領的金額
			recordElement.setAttribute(
				"points",
				Long.toString(LoverService.CalME_Point_ToNTD(Long.valueOf((Math.abs(points)))))
			);
		});

		historyRepository.findAll(Specifications.notAbleTowithdrawal(lover)).forEach(history -> {
			// 已經退回的不列出
			if (Objects.nonNull(historyRepository.findTop1ByBehaviorAndHistory(BEHAVIOR_RETURN_FARE, history))) {
				return;
			}
			Element notAbleToWithdrawalElement = document.createElement("notAbleToWithdrawal");
			documentElement.appendChild(notAbleToWithdrawalElement);

			notAbleToWithdrawalElement.setAttribute(
				"date",
				DATE_FORMATTER.format(
					Servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				));

			notAbleToWithdrawalElement.setAttribute(
				"male",
				history.getInitiative().getNickname()
			);

			notAbleToWithdrawalElement.setAttribute(
				"maleId",
				history.getInitiative().getIdentifier().toString()
			);

			notAbleToWithdrawalElement.setAttribute(
				"type",
				messageSource.getMessage(
					history.getBehavior().name(),
					null,
					locale
				));

			if (historyService.ableToReturnFare(history)) {
				notAbleToWithdrawalElement.setAttribute(
					"returnFareId",
					history.getId().toString()
				);
				notAbleToWithdrawalElement.setAttribute(
					"ableToReturn",
					null
				);
			}

			@SuppressWarnings("null")
			Short points = Objects.equals(history.getBehavior().name(), "CHE_MA_FEI") ? history.getPoints() : (short) (history.getPoints() / 2);
			notAbleToWithdrawalElement.setAttribute(
				"mePoints",
				Integer.toString(Math.abs(points))
			);
			// 抽完1成可提領的金額
			notAbleToWithdrawalElement.setAttribute(
				"points",
				Long.toString(LoverService.CalME_Point_ToNTD(Long.valueOf(Math.abs(points))))
			);
		});

		// 等待匯款中的記錄、提領歷史紀錄
		withdrawalRecordRepository.findHoneyAllGroupByHoneyAndStatusAndWayAndTimeStamp(lover).forEach(eachWithdrawal -> {
			Element recordElement = document.createElement("historyRecord");
			documentElement.appendChild(recordElement);

			Lover honey = eachWithdrawal.getHoney();

			recordElement.setAttribute(
				"way",
				messageSource.getMessage(
					eachWithdrawal.getWay().name(),
					null,
					locale
				)
			);

			Date timestamp = eachWithdrawal.getTimestamp();
			recordElement.setAttribute(
				"date",
				DATE_FORMATTER.format(
					Servant.toTaipeiZonedDateTime(
						timestamp
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				));

			recordElement.setAttribute(
				"mePoints",
				eachWithdrawal.getPoints().toString()
			);
			// 抽完1成可提領的金額
			recordElement.setAttribute(
				"points",
				Long.toString(LoverService.CalME_Point_ToNTD(eachWithdrawal.getPoints()))
			);

			Boolean status = eachWithdrawal.getStatus();
			recordElement.setAttribute(
				"status",
				status.toString()
			);

			withdrawalRecordRepository.findByHoneyAndStatusAndTimestamp(honey, status, timestamp).forEach(withdrawalRecord -> {
				Element historyElement = document.createElement("history");
				recordElement.appendChild(historyElement);
				historyElement.setAttribute(
					"date",
					DATE_FORMATTER.format(
						Servant.toTaipeiZonedDateTime(
							withdrawalRecord.
								getHistory().
								getOccurred()
						).withZoneSameInstant(
							Servant.ASIA_TAIPEI_ZONE_ID
						)
					));

				historyElement.setAttribute(
					"male",
					withdrawalRecord.getHistory().getInitiative().getNickname()
				);

				historyElement.setAttribute(
					"maleId",
					withdrawalRecord.getHistory().getInitiative().getIdentifier().toString()
				);

				historyElement.setAttribute(
					"type",
					messageSource.getMessage(
						withdrawalRecord.getHistory().getBehavior().name(),
						null,
						locale
					));

				historyElement.setAttribute(
					"mePoints",
					Short.toString(withdrawalRecord.getPoints())
				);
				// 抽完1成可提領的金額
				historyElement.setAttribute(
					"points",
					Long.toString(LoverService.CalME_Point_ToNTD(Long.valueOf(withdrawalRecord.getPoints())))
				);
			});
		});

		return document;
	}

	/**
	 * 取消定期定額
	 *
	 * @param lover
	 * @param locale
	 * @param email
	 * @param lastFourDigits
	 * @return
	 */
	public String stopRecurring(Lover lover, String email, String lastFourDigits, Locale locale) {
		if (!email.matches("^([\\w\\.\\-]){1,64}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$")) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"emailIsFail",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().
				toString();
		}
		if (lastFourDigits.isBlank()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"lastFourDigitsIsIsNotNull",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().
				toString();
		}
		if (!lastFourDigits.matches("^\\d{4}$")) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"lastFourDigitsLengthIsFail",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().
				toString();
		}

		if (!dashboardService.isRecurringPaymentStoppable(lover)) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"stopRecurring.isNotEligible",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().
				toString();
		}

		History history = historyRepository.findTop1ByInitiativeAndBehaviorOrderByOccurredDesc(
			lover,
			Behavior.YUE_FEI
		);
		StopRecurringPaymentApplication stopRecurringPaymentApplication = new StopRecurringPaymentApplication(
			lover,
			history,
			email,
			lastFourDigits
		);
		stopRecurringPaymentApplicationRepository.saveAndFlush(stopRecurringPaymentApplication);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"stopRecurring.done",
				null,
				locale
			)).
			withResponse(true).
			toJSONObject().
			toString();
	}

	/**
	 * 使用銀行匯款提領車馬費
	 *
	 * @param wireTransferBankCode
	 * @param wireTransferBranchCode
	 * @param wireTransferAccountName
	 * @param wireTransferAccountNumber
	 * @param honey
	 * @param locale
	 * @return
	 */
	@SuppressWarnings({"UnusedAssignment", "null"})
	@Transactional
	public JSONObject wireTransfer(String wireTransferBankCode, String wireTransferBranchCode, String wireTransferAccountName, String wireTransferAccountNumber, Lover honey, Locale locale) {
		if (wireTransferAccountName.isBlank() || wireTransferAccountName.isEmpty()) {
			throw new IllegalArgumentException("wireTransfer.accountNameMustntBeNull");
		}
		if (wireTransferAccountNumber.isBlank() || wireTransferAccountNumber.isEmpty()) {
			throw new IllegalArgumentException("wireTransfer.accountNumberMustntBeNull");
		}
		if (wireTransferBankCode.isBlank() || wireTransferBankCode.isEmpty()) {
			throw new IllegalArgumentException("wireTransfer.bankCodeMustntBeNull");
		}
		if (wireTransferBranchCode.isBlank() || wireTransferBranchCode.isEmpty()) {
			throw new IllegalArgumentException("wireTransfer.branchCodeMustntBeNull");
		}

		Date current = new Date(System.currentTimeMillis());
		for (History history : historyRepository.findAll(Specifications.withdrawal(honey))) {
			// 已經退回的不列出
			if (Objects.nonNull(historyRepository.findTop1ByBehaviorAndHistory(BEHAVIOR_RETURN_FARE, history))) {
				continue;
			}
			Short points = Objects.equals(history.getBehavior(), BEHAVIOR_FARE) ? history.getPoints() : (short) (history.getPoints() / 2);
			WithdrawalRecord withdrawalRecord = new WithdrawalRecord(honey, (short) -points, WayOfWithdrawal.WIRE_TRANSFER, current);
			withdrawalRecord.setId(history.getId());
			withdrawalRecordRepository.save(withdrawalRecord);
		}
		withdrawalRecordRepository.flush();

		WithdrawalInfo withdrawalInfo = null;
		if (withdrawalInfoRepository.countByHoney(honey) > 0) {
			withdrawalInfo = withdrawalInfoRepository.findByHoney(honey);
			withdrawalInfo.setWireTransferAccountName(wireTransferAccountName);
			withdrawalInfo.setWireTransferAccountNumber(wireTransferAccountNumber);
			withdrawalInfo.setWireTransferBankCode(wireTransferBankCode);
			withdrawalInfo.setWireTransferBranchCode(wireTransferBranchCode);
		} else {
			withdrawalInfo = new WithdrawalInfo(
				honey,
				wireTransferBankCode,
				wireTransferBranchCode,
				wireTransferAccountName,
				wireTransferAccountNumber
			);
		}
		withdrawalInfoRepository.saveAndFlush(withdrawalInfo);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"wireTransfer.done",
				null,
				locale
			)).
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 甜心剩餘的可提領車馬費(抽一成)
	 *
	 * @param honey
	 * @return
	 */
	@Transactional
	@SuppressWarnings("null")
	public Long honeyLeftPointsBefore7Days(Lover honey) {
		List<History> fareList = historyRepository.findByPassiveAndBehaviorAndOccurredBefore(
			honey,
			BEHAVIOR_FARE,
			before7DaysAgo()
		);
		Long fareSum = 0L;
		for (History history : fareList) {
			// 已經退回的不列出
			if (Objects.nonNull(historyRepository.findTop1ByBehaviorAndHistory(BEHAVIOR_RETURN_FARE, history))) {
				continue;
			}
			fareSum += Math.abs(history.getPoints());
		}

		List<History> lineList = historyRepository.findByPassiveAndBehaviorAndOccurredBefore(
			honey,
			BEHAVIOR_LAI_KOU_DIAN,
			before7DaysAgo()
		);
		Long lineSum = 0L;
		for (History history : lineList) {
			lineSum += Math.abs(history.getPoints()) / 2;
		}

		Long withdrawnPoints = withdrawalRecordRepository.sumPoinsByHoney(honey);
		withdrawnPoints = Objects.nonNull(withdrawnPoints) ? withdrawnPoints : 0;

		Long leftPoints = (fareSum + lineSum) - withdrawnPoints;

		return LoverService.CalME_Point_ToNTD(leftPoints);
	}

	public static long CalME_Point_ToNTD(Long pointOrg){

		double pointFee = pointOrg * 0.1;
		double pointGirl=(pointOrg -pointFee) * LoverService.ME_Point_To_NTD;

		return Math.round(pointGirl);
	}

	/**
	 * 七天前。
	 *
	 * @return java.util.Date
	 */
	public Date before7DaysAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		return calendar.getTime();
	}

	/**
	 * 更新出没地区。
	 *
	 * @param location 地区
	 * @param someone 某咪郎
	 * @return 出没地区
	 */
	@Transactional
	public JSONObject updateLocation(Location location, Lover someone) {
		Set<Location> locations = new HashSet<>();
		appearedLocationRepository.findByLover(someone).forEach(appearedLocation -> {
			locations.add(appearedLocation.getLocation());
		});

		AppearedLocation appearedLocation;
		if (locations.contains(location)) {
			/*
			 已存在则删除
			 */
			appearedLocation = appearedLocationRepository.
				findOneByLoverAndLocation(someone, location).
				orElseThrow();
			appearedLocationRepository.delete(appearedLocation);
		} else {
			/*
			 不存在则创建
			 */
			AppearedLocationKey id = new AppearedLocationKey();
			id.setLoverId(someone.getId());
			id.setLocationId(location.getId());

			appearedLocation = new AppearedLocation();
			appearedLocation.setId(id);
			appearedLocation.setLover(someone);
			appearedLocation.setLocation(location);
			appearedLocationRepository.save(appearedLocation);
		}
		appearedLocationRepository.flush();

		return new JavaScriptObjectNotation().
			withResponse(true).
			//withResult(appearedLocation).
			toJSONObject();
	}

	/**
	 * 更新期望陪伴。
	 *
	 * @param companionship 友谊
	 * @param someone 某咪郎
	 * @return 期望陪伴
	 */
	@Transactional
	public JSONObject updateService(Companionship companionship, Lover someone,int hour,int point) {
		Set<Companionship> companionships = new HashSet<>();
		desiredCompanionshipRepository.findByLover(someone).forEach(desiredCompanionship -> {
			companionships.add(desiredCompanionship.getCompanionship());
		});

		DesiredCompanionship desiredCompanionship;
		if (companionships.contains(companionship)) {
			/*
			 已存在则删除
			 */
			desiredCompanionship = desiredCompanionshipRepository.
				findOneByLoverAndCompanionship(someone, companionship).
				orElseThrow();
			desiredCompanionshipRepository.delete(desiredCompanionship);
		} else {
			/*
			 不存在则创建
			 */
			DesiredCompanionshipKey id = new DesiredCompanionshipKey();
			id.setLoverId(someone.getId());
			id.setCompanionshipId(companionship.getId());

			desiredCompanionship = new DesiredCompanionship();
			desiredCompanionship.setId(id);
			desiredCompanionship.setLover(someone);
			desiredCompanionship.setCompanionship(companionship);

			if (hour>0){
				desiredCompanionship.setHour(hour);
			}
			if (point>0){
				desiredCompanionship.setPoint(point);
			}

			desiredCompanionshipRepository.save(desiredCompanionship);
		}
		desiredCompanionshipRepository.flush();

		return new JavaScriptObjectNotation().
			withResponse(true).
			//withResult(desiredCompanionship).
			toJSONObject();
	}

	@Transactional
	public JSONObject updateService2(Lover someone,List<CompanionshipWithInfo> services) {
		Set<Companionship> companionshipsDB = new HashSet<>();
		desiredCompanionshipRepository.findByLover(someone).forEach(desiredCompanionship -> {
			companionshipsDB.add(desiredCompanionship.getCompanionship());
		});

//		for (CompanionshipWithInfo service : services) {
//			for (Companionship companionship : companionshipsDB) {
//				if (companionship.getId()==service.getServiceId()){
//
//				}
//			}
//		}

		//先全刪除
		DesiredCompanionship desiredCompanionship;
		for (Companionship companionship : companionshipsDB) {
			desiredCompanionship = desiredCompanionshipRepository.
					findOneByLoverAndCompanionship(someone, companionship).
					orElseThrow();
			desiredCompanionshipRepository.delete(desiredCompanionship);
		}

		//再新增
		for (CompanionshipWithInfo service : services) {
			Companionship companionship = companionshipRepository.findById((short) service.getServiceId()).get();
			DesiredCompanionshipKey id = new DesiredCompanionshipKey();
			id.setLoverId(someone.getId());
			id.setCompanionshipId((short) service.getServiceId());

			desiredCompanionship = new DesiredCompanionship();
			desiredCompanionship.setId(id);
			desiredCompanionship.setLover(someone);
			desiredCompanionship.setCompanionship(companionship);

			desiredCompanionship.setHour(service.getHour());
			desiredCompanionship.setPoint(service.getPoint());

			desiredCompanionshipRepository.save(desiredCompanionship);
		}

		desiredCompanionshipRepository.flush();


		return new JavaScriptObjectNotation().
				withResponse(true).
				//withResult(desiredCompanionship).
						toJSONObject();
	}

	/**
	 * 通知數
	 *
	 * @param lover
	 * @return
	 */
	public int annoucementCount(Lover lover) {
		int announcement = annoucementHistories(lover).size();
		return announcement;
	}

	/**
	 * 通知的歷程 List
	 *
	 * @param lover
	 * @return
	 */
	public List<History> annoucementHistories(Lover lover) {
		List<Behavior> behaviors = behaviorsOfAnnouncement(lover);
		return historyRepository.findAll(Specifications.passive(lover, behaviors));
	}

	/**
	 * 需通知的行為
	 *
	 * @param lover
	 * @return
	 */
	public List<Behavior> behaviorsOfAnnouncement(Lover lover) {
		List<Behavior> behaviors = new ArrayList<>();
		behaviors.add(BEHAVIOR_CERTIFICATION_SUCCESS);
		behaviors.add(BEHAVIOR_CERTIFICATION_FAIL);
		behaviors.add(BEHAVIOR_RATE);
		behaviors.add(BEHAVIOR_FOLLOW);
		behaviors.add(BEHAVIOR_PEEK);

		return behaviors;
	}

	/**
	 * 收藏/搜尋頁面的情人基本資訊
	 *
	 * @param document
	 * @param lovers
	 * @param me
	 * @param locale
	 * @return
	 */
	public Document loversSimpleInfo(Document document, Collection<Lover> lovers, Lover me, Locale locale) {
		Element documentElement = document.getDocumentElement();

		Collection<Lover> following = loverService.getThoseIFollow(me);

		lovers.stream().filter(lover -> !(Objects.nonNull(lover.getDelete()))).forEachOrdered(lover -> {
			Element loverElement = document.createElement("lover");
			documentElement.appendChild(loverElement);
			loverElement.setAttribute(
				"identifier",
				lover.getIdentifier().toString()
			);
			loverElement.setAttribute(
				"profileImage",
				String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					lover.getProfileImage()
				)
			);
			if (Objects.nonNull(lover.getNickname())) {
				loverElement.setAttribute(
					"nickname",
					lover.getNickname()
				);
			}
			if (Objects.nonNull(lover.getBirthday())) {
				loverElement.setAttribute(
					"age",
					calculateAge(lover).toString()
				);
			}
			// 是否為貴賓
			if (isVVIP(lover) || isVIP(lover) || isTrial(lover)) {
				loverElement.setAttribute("vip", null);
			}
			// 是否有安心認證
			if (Objects.nonNull(lover.getRelief())) {
				Boolean relief = lover.getRelief();
				loverElement.setAttribute(
					"relief",
					relief ? "true" : "false"
				);
			}
			// 是否收藏對方
			if (Objects.nonNull(following) && following.contains(lover)) {
				loverElement.setAttribute(
					"following",
					null
				);
			}
			if (Objects.nonNull(lover.getRelationship())) {
				Element relationshipElement = document.createElement("relationship");
				relationshipElement.setTextContent(
					messageSource.getMessage(
						lover.getRelationship().toString(),
						null,
						locale
					)
				);
				loverElement.appendChild(relationshipElement);
			}

			/*
			 出没地区
			 */
			Collection<Location> locations = getLocations(lover, true);
			if (!locations.isEmpty()) {
				int count = 0;
				for (Location location : locations) {
					++count;
					if (count <= 3) {
						Element locationElement = document.createElement("location");
						locationElement.setTextContent(
							messageSource.getMessage(
								location.getName(),
								null,
								locale
							)
						);
						loverElement.appendChild(locationElement);
					}
				}
			}
		});

		return document;
	}

	/**
	 * 搜尋頁面的情人基本資訊分頁
	 *
	 * @param lovers
	 * @param me
	 * @param locale
	 * @return
	 */
	public JSONObject loversSimpleInfoJSON(Page<Lover> lovers, Lover me, Locale locale) {
		Collection<Lover> following = loverService.getThoseIFollow(me);
		JSONObject jSONObject = new JSONObject();
		JSONArray jSONArray = new JSONArray();

		lovers.getContent().stream().filter(lover -> !(Objects.nonNull(lover.getDelete()))).forEachOrdered(lover -> {
			JSONObject json = new JSONObject();
			json.
				put(
					"identifier",
					lover.getIdentifier().toString()
				).
				put(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						Servant.STATIC_HOST,
						lover.getProfileImage()
					)
				);

			if (Objects.nonNull(lover.getNickname())) {
				json.put(
					"nickname",
					lover.getNickname()
				);
			}
			if (Objects.nonNull(lover.getBirthday())) {
				json.put(
					"age",
					calculateAge(lover).toString()
				);
			}
			// 是否為長期貴賓 vvip
			if (isVVIP(lover) || isVIP(lover) || isTrial(lover)) {
				json.put(
					"vip",
					true
				);
			}
			// 是否有安心認證
			if (Objects.nonNull(lover.getRelief())) {
				Boolean relief = lover.getRelief();
				json.put(
					"relief",
					relief ? true : false
				);
			}
			// 是否收藏對方
			if (Objects.nonNull(following) && following.contains(lover)) {
				json.put(
					"following",
					true
				);
			}
			if (Objects.nonNull(lover.getRelationship())) {
				json.put(
					"relationship",
					messageSource.getMessage(
						lover.getRelationship().toString(),
						null,
						locale
					)
				);
			}

			/*
			 出没地区
			 */
			Collection<Location> locations = getLocations(lover, true);
			if (!locations.isEmpty()) {
				JSONArray locArray = new JSONArray();
				int count = 0;
				for (Location location : locations) {
					++count;
					if (count <= 3) {
						locArray.put(
							messageSource.getMessage(
								location.getName(),
								null,
								locale
							)
						);
					}
				}
				json.put(
					"locations",
					locArray
				);
			}
			jSONArray.put(json);
		});
		jSONObject.
			put(
				"lovers", jSONArray
			).
			put(
				"hasNext",
				lovers.hasNext() ? lovers.nextOrLastPageable().getPageNumber() : null
			).
			put(
				"hasPrev",
				lovers.hasPrevious() ? lovers.previousOrFirstPageable().getPageNumber() : null
			);

		return jSONObject;
	}

	public JSONObject indexJson(Lover me, Integer vipPage, Integer reliefPage, Integer activePage, Integer registerPage, Locale locale) {

		JSONObject jSONObject = new JSONObject();

		if (!me.getGender()) {
			Page<Lover> vip = vipOnTheWall(
				me,
				vipPage < 0 ? 0 : vipPage,
				PAGE_SIZE_ON_THE_WALL
			);
			jSONObject.put(
				"vip",
				createJSON(me, vip, locale)
			);
		}//貴賓列表区块

		Page<Lover> relief = relievingOnTheWall(
			me,
			reliefPage < 0 ? 0 : reliefPage,
			PAGE_SIZE_ON_THE_WALL
		);
		jSONObject.put(
			"relief",
			createJSON(me, relief, locale)
		);//安心认证列表区块

		Page<Lover> active = latestActiveOnTheWall(
			me,
			activePage < 0 ? 0 : activePage,
			PAGE_SIZE_ON_THE_WALL
		);
		jSONObject.put(
			"active",
			createJSON(me, active, locale)
		);//最近活跃列表区块

		Page<Lover> register = latestRegisteredOnTheWall(
			me,
			registerPage < 0 ? 0 : registerPage,
			PAGE_SIZE_ON_THE_WALL
		);
		jSONObject.put(
			"register",
			createJSON(me, register, locale)
		);//最新注册列表区块

		return jSONObject;
	}

	/**
	 * 首頁網頁版 JSON
	 *
	 * @param me
	 * @param page
	 * @param locale
	 * @return
	 */
	public JSONObject createJSON(Lover me, Page<Lover> page, Locale locale) {
		JSONObject jSONObject = new JSONObject();
		if (page.hasNext()) {
			jSONObject.put(
				"hasNext",
				Integer.toString(page.nextOrLastPageable().getPageNumber())
			);
		}
		if (page.hasPrevious()) {
			jSONObject.put(
				"hasPrev",
				Integer.toString(page.previousOrFirstPageable().getPageNumber())
			);
		}
		JSONArray jSONArray = new JSONArray();
		Collection<Lover> following = loverService.getThoseIFollow(me);
		page.getContent().forEach(lover -> {
			JSONObject eachObject = new JSONObject();
			if (Objects.nonNull(lover.getNickname())) {
				eachObject.put(
					"nickname",
					lover.getNickname()
				);
			}
			// 是否為長期貴賓 vvip
			if (isVVIP(lover)) {
				eachObject.put(
					"vvip",
					true
				);
			}
			// 是否為短期貴賓 vip
			if (isVIP(lover)) {
				eachObject.put(
					"vip",
					true
				);
			}
			if (Objects.nonNull(lover.getRelief())) {
				eachObject.put(
					"relief",
					lover.getRelief()
				);
			}
			// 是否收藏對方
			if (Objects.nonNull(following) && following.contains(lover)) {
				eachObject.put(
					"following",
					true
				);
			}
			if (Objects.nonNull(lover.getRelationship())) {
				eachObject.put(
					"relationship",
					messageSource.getMessage(
						lover.getRelationship().toString(),
						null,
						locale
					)
				);
			}

			/*
				   出没地区
			 */
			Collection<Location> locations = getLocations(lover, true);
			JSONArray locArray = new JSONArray();
			if (!locations.isEmpty()) {
				int count = 0;
				for (Location location : locations) {
					++count;
					if (count <= 3) {
						locArray.put(
							messageSource.getMessage(
								location.getName(),
								null,
								locale
							)
						);
					}
				}
			}
			eachObject.
				put(
					"location",
					locArray
				).
				put(
					"age",
					calculateAge(lover).toString()
				).
				put(
					"identifier",
					lover.getIdentifier().toString()
				).
				put(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						Servant.STATIC_HOST,
						lover.getProfileImage()
					)
				);
			jSONArray.put(eachObject);
		});
		jSONObject.put(
			"result",
			jSONArray
		);

		return jSONObject;
	}

	/**
	 * 看更多甜心/男仕
	 *
	 * @param me
	 * @param page
	 * @param locale
	 * @return
	 */
	public JSONArray seeMoreLover(Lover me, Page<Lover> page, Locale locale) {
		JSONArray jsonArray = new JSONArray();
		Collection<Lover> following = loverService.getThoseIFollow(me);
		page.getContent().stream().map(lover -> {
			JSONObject json = new JSONObject();
			json.put("nickname", lover.getNickname());
			if (isVVIP(lover)) {
				json.put("vvip", true);
			}
			if (Objects.nonNull(lover.getRelief()) && lover.getRelief()) {
				json.put("relief", true);
			}
			if (Objects.nonNull(following) && following.contains(lover)) {
				json.put("following", true);
			}
			json.put("age", calculateAge(lover));
			json.put("identifier", lover.getIdentifier());
			if (Objects.nonNull(lover.getRelationship())) {
				json.put(
					"relationship",
					messageSource.getMessage(
						lover.getRelationship().toString(),
						null,
						locale
					));
			}

			/*
			 出没地区
			 */
			Collection<Location> locations = getLocations(lover, true);
			if (!locations.isEmpty()) {
				JSONArray jsonArrayLocation = new JSONArray();
				int count = 0;
				for (Location location : locations) {
					++count;
					if (count <= 3) {
						jsonArrayLocation.put(
							messageSource.getMessage(
								location.getName(),
								null,
								locale
							));
					}
				}
				json.put(
					"location", jsonArrayLocation
				);
			}
			json.put("profileImage", String.format(
				"https://%s/profileImage/%s",
				Servant.STATIC_HOST,
				lover.getProfileImage()
			));
			return json;
		}).forEachOrdered(json -> {
			jsonArray.put(json);
		});
		return jsonArray;
	}

	/**
	 * 未封号的情人们，以活跃降幂排序；用于首页的安心认证列表区块。
	 *
	 * @param mofo 用户号
	 * @param p 第几页
	 * @param s 每页几笔
	 * @return 通过安心认证的甜心们
	 */
	@Transactional(readOnly = true)
	public Page<Lover> relievingOnTheWall(Lover mofo, int p, int s) {
		//return loverRepository.findAll(
		//	LoverSpecification.relievingOnTheWall(
		//		mofo,
		//		new HashSet<>(getExceptions(mofo))
		//	),
		//	PageRequest.of(p, s)
		//);
		try {
			List<Integer> ids = jdbcTemplate.query(
				FileCopyUtils.copyToString(new InputStreamReader(
					relievingOnTheWall.getInputStream(),
					Servant.UTF_8
				)),
				(ps) -> {
					ps.setBoolean(1, !mofo.getGender());
					ps.setInt(2, mofo.getId());
					ps.setInt(3, mofo.getId());
				},
				(resultSet, rowNum) -> resultSet.getInt("id")
			);

			return loverRepository.findByIdIn(
				ids,
				PageRequest.of(p, s)
			);
		} catch (IOException ignore) {
			return Page.empty();
		}
	}

	/**
	 * 这家伙有没有配置 LINE Notify⁉️
	 *
	 * @param sucker 甜心或男仕
	 * @return 布林值
	 */
	@Transactional(readOnly = true)
	public boolean hasLineNotify(Lover sucker) {
		String lineNotifyAccessToken = sucker.getLineNotifyAccessToken();
		return Objects.nonNull(lineNotifyAccessToken) && !lineNotifyAccessToken.isBlank();
	}

	/**
	 * @param mofo 用户号
	 * @param p 第几页
	 * @param s 每页几笔
	 * @return 貴賓们(男士)
	 */
	@Transactional(readOnly = true)
	public Page<Lover> vipOnTheWall(Lover mofo, int p, int s) {
		return loverRepository.findAll(
			LoverSpecification.vipOnTheWall(
				mofo,
				new HashSet<>(getExceptions(mofo))
			),
			PageRequest.of(p, s)
		);
		//try {
		//	List<Integer> ids = jdbcTemplate.query(
		//		FileCopyUtils.copyToString(new InputStreamReader(
		//			vvipOnTheWall.getInputStream(),
		//			Servant.UTF_8
		//		)),
		//		(ps) -> {
		//			ps.setInt(1, mofo.getId());
		//			ps.setInt(2, mofo.getId());
		//		},
		//		(resultSet, rowNum) -> resultSet.getInt("id")
		//	);
		//
		//	return loverRepository.findByIdIn(
		//		ids,
		//		PageRequest.of(p, s)
		//	);
		//} catch (IOException ignore) {
		//	return Page.empty();
		//}
	}

	@Data
	private class Man implements Serializable {

		private static final long serialVersionUID = -7867929167569711132L;

		private final int maleId;

		@Override
		public String toString() {
			try {
				return new JsonMapper().writeValueAsString(this);
			} catch (JsonProcessingException ignore) {
				return "null";
			}
		}
	}

	/**
	 * 群发打招呼。
	 *
	 * @param female 甜心
	 * @param greetingMessage 打招呼语
	 * @param locale 语言环境
	 * @return 杰森格式对象
	 */
	public JSONObject groupGreeting(Lover female, String greetingMessage, Locale locale) {
		if (Objects.isNull(greetingMessage) || greetingMessage.isBlank()) { //招呼語不能為空
			throw new RuntimeException("greet.greetingMessageMustntBeNull");
		}
		if (within12hrsFromLastGroupGreeting(female)) { //24小時內已群發過打招呼
			throw new RuntimeException("groupGreeting.within24hrsHasSent");
		}

		Collection<Location> locations = getLocations(female);

		List<Man> men = jdbcTemplate.query(
			(Connection connection) -> {
				StringBuilder stringBuilder = new StringBuilder();
				locations.forEach(location -> {
					stringBuilder.append(String.format(
						"'%s',",
						location.getId()
					));
				});
				final String SAME_LOCATION = stringBuilder.
					toString().
					replaceAll(
						",$",
						""
					);

				PreparedStatement preparedStatement;
				try {
					preparedStatement = connection.prepareStatement(
						String.format(
							FileCopyUtils.copyToString(
								new InputStreamReader(
									groupGreetingList.getInputStream(),
									UTF_8
								)
							),
							SAME_LOCATION
						)
					);
					preparedStatement.setInt(1, female.getId());
					preparedStatement.setInt(2, female.getId());
					preparedStatement.setInt(3, NUMBER_OF_GROUP_GREETING);
					preparedStatement.setInt(4, female.getId());
					return preparedStatement;
				} catch (IOException ex) {
					return null;
				}
			},
			(ResultSet resultSet, int rowNum) -> {
				return new Man(
					resultSet.getInt("id")
				);
			}
		);

		Date current = new Date(System.currentTimeMillis());
		for (Man man : men) {
			Lover male = loverRepository.findById(man.getMaleId()).orElseThrow();
			History history = new History(
				female,
				male,
				Behavior.QUN_FA,
				(short) 0
			);
			history.setOccurred(current);
			history.setGreeting(greetingMessage);
			historyRepository.save(history);
			// 推送通知給男生
			webSocketServer.sendNotification(male.getIdentifier().toString(),
				String.format("inbox%s向你打招呼：「%s」",
					female.getNickname(),
					greetingMessage
				));
			if (hasLineNotify(male)) {
				lineMessagingService.notify(
					male,
					String.format(
						"有位甜心向你打招呼！馬上查看 https://%s/activities.asp",
						Servant.LOCALHOST
					)
				);
			}
		}

		historyRepository.flush();

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"groupGreeting.done",
				null,
				locale
			)).
			withResponse(true).
			withRedirect("/").
			toJSONObject();
	}

	public Document groupGreetingDocument(Document document, Lover female) {
		Element documentElement = document.getDocumentElement();

		if (within12hrsFromLastGroupGreeting(female)) {
			History lastHistory = historyRepository.findTop1ByInitiativeAndBehaviorOrderByIdDesc(female, BEHAVIOR_GROUP_GREETING);
			documentElement.setAttribute(
				"within24hr",
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(
					Servant.toTaipeiZonedDateTime(
						lastHistory.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
				));
		}

		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -5);
		Date fiveDaysAgo = cal.getTime();

		// 查看五天內的招呼紀錄
		Element recordsElement = document.createElement("records");
		documentElement.appendChild(recordsElement);
		for (Activity activity : historyRepository.findGroupGreetingListGroupBy(female, BEHAVIOR_GROUP_GREETING, fiveDaysAgo)) {
			Element recordElement = document.createElement("record");
			recordsElement.appendChild(recordElement);
			recordElement.setAttribute(
				"date",
				DATE_FORMATTER.format(Servant.
					toTaipeiZonedDateTime(
						activity.getOccurred()
					).
					withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID
					)
				)
			);

			for (History history : historyRepository.findByInitiativeAndBehaviorAndOccurred(female, BEHAVIOR_GROUP_GREETING, activity.getOccurred())) {
				Element historyElement = document.createElement("history");
				recordElement.appendChild(historyElement);
				Lover male = history.getPassive();

				historyElement.setAttribute("nickname", male.getNickname());
				historyElement.setAttribute("age", calculateAge(male).toString());
				historyElement.setAttribute("profileImage", male.getProfileImage());
				historyElement.setAttribute("identifier", male.getIdentifier().toString());
				if (isVVIP(history.getPassive())) {
					historyElement.setAttribute("vvip", null);
				}
				if (Objects.nonNull(male.getRelief())) {
					Boolean relief = male.getRelief();
					historyElement.setAttribute(
						"relief",
						relief ? "true" : "false"
					);
				}
				historyElement.setAttribute(
					"profileImage",
					String.format(
						"https://%s/profileImage/%s",
						Servant.STATIC_HOST,
						male.getProfileImage()
					)
				);
			}
		}
		return document;
	}

	@SuppressWarnings("null")
	public boolean within12hrsFromLastGroupGreeting(Lover female) {
		Date gpDate = null;
		Date nowDate = null;
		History history = historyRepository.findTop1ByInitiativeAndBehaviorOrderByIdDesc(
			female,
			BEHAVIOR_GROUP_GREETING
		);
		if (Objects.nonNull(history)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(history.getOccurred());
			calendar.add(Calendar.HOUR, 12);
			gpDate = calendar.getTime();
			nowDate = new Date();
		} else if (Objects.isNull(history)) {
			return false;
		}
		return nowDate.before(gpDate);
	}

	/**
	 * 是否已经完成填写注册个人资讯
	 *
	 * @param someone 某咪郎
	 * @return 真或伪布林值
	 */
	public boolean isEligible(Lover someone) {
		String login = someone.getLogin();
		if (Objects.isNull(login) || login.isBlank()) {
			return false;
		}//帐号(手机号)
		String shadow = someone.getShadow();
		if (Objects.isNull(shadow) || shadow.isBlank()) {
			return false;
		}//密码
		String nickname = someone.getNickname();
		if (Objects.isNull(nickname) || nickname.isBlank()) {
			return false;
		}//昵称
		String aboutMe = someone.getAboutMe();
		if (Objects.isNull(aboutMe) || aboutMe.isBlank()) {
			return false;
		}//自介
		String greeting = someone.getGreeting();
		if (Objects.isNull(greeting) || greeting.isBlank()) {
			return false;
		}//哈啰
		if (Objects.isNull(someone.getBodyType())) {
			return false;
		}//体型
		if (Objects.isNull(someone.getHeight())) {
			return false;
		}//身高
		if (Objects.isNull(someone.getWeight())) {
			return false;
		}//体重
		if (Objects.isNull(someone.getEducation())) {
			return false;
		}//学历
		if (Objects.isNull(someone.getMarriage())) {
			return false;
		}//婚姻
		String occupation = someone.getOccupation();
		if (Objects.isNull(occupation) || occupation.isBlank()) {
			return false;
		}//职业
		if (Objects.isNull(someone.getSmoking())) {
			return false;
		}//抽烟习惯
		if (Objects.isNull(someone.getDrinking())) {
			return false;
		}//饮酒习惯
		String idealConditions = someone.getIdealConditions();
		if (Objects.isNull(idealConditions) || idealConditions.isBlank()) {
			return false;
		}//简述理想对象条件
		if (Objects.nonNull(someone.getDelete())) {
			return false;
		}//封号
//		if (Objects.isNull(someone.getRelationship())) {
//			return false;
//		}//预期关系
		if (someone.getGender()) {
//			if (Objects.isNull(someone.getAnnualIncome())) {
//				return false;
//			}//年收入
		} else {
//			String inviteMeAsFriend = someone.getInviteMeAsLineFriend();
//			if (Objects.isNull(inviteMeAsFriend) || inviteMeAsFriend.isBlank()) {
//				return false;
//			}//添加好友链结
//			if (Objects.isNull(someone.getAllowance())) {
//				return false;
//			}//期望零用钱
		}
		return !(getLocations(someone).isEmpty() || getCompanionships(someone).isEmpty());//(地区|友谊)标签
	}

	/**
	 * 甲拉黑或漂白乙。
	 *
	 * @param initiative 把别人拉黑的一方
	 * @param passive 要被拉黑的一方
	 * @param locale 语言环境
	 * @return 杰森格式对象
	 */
	public JSONObject block(Lover initiative, Lover passive, Locale locale) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("block.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("block.passiveMustntBeNull");
		}
		if (Objects.equals(initiative, passive)) {
			throw new RuntimeException("block.mustBeDifferent");
		}
		if (Objects.equals(initiative.getGender(), passive.getGender())) {
			throw new RuntimeException("block.mustBeStraight");
		}

		Collection<Lover> blockers = new HashSet<>();
		blacklistRepository.findByBlocker(initiative).forEach(blacklist -> {
			blockers.add(blacklist.getBlocked());
		});

		Blacklist blacklist;
		if (blockers.contains(passive)) {
			/*
			 已存在则删除
			 */
			blacklist = blacklistRepository.findOneByBlockerAndBlocked(initiative, passive).
				orElseThrow();
			blacklistRepository.delete(blacklist);
			blacklistRepository.flush();

			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"unblock.done",
					null,
					locale
				)).
				withResponse(true).
				toJSONObject();
		}

		/*
		 不存在则创建
		 */
		BlacklistKey id = new BlacklistKey();
		id.setInitiativeId(initiative.getId());
		id.setPassiveId(passive.getId());

		blacklist = new Blacklist();
		blacklist.setId(id);
		blacklist.setBlocker(initiative);
		blacklist.setBlocked(passive);
		blacklistRepository.saveAndFlush(blacklist);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"block.done",
				null,
				locale
			)).
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 甜心是否答應給男士通訊軟體
	 *
	 * @param female
	 * @param male
	 * @return
	 */
	public boolean areMatched(Lover female, Lover male) {
		LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(female, male);
		return Objects.nonNull(lineGiven) && Objects.nonNull(lineGiven.getResponse()) && lineGiven.getResponse();
	}

	/**
	 * 未讀訊息幾則
	 *
	 * @param mofo
	 * @return
	 */
	public int unreadMessages(Lover mofo) {
		int unreadCount = historyRepository.
			countByPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(
				mofo,
				behaviorOfConversation()
			);
		return unreadCount;
	}

	/**
	 * 找出未已讀的訊息(針對男女有不一樣的行為)
	 *
	 * @param lover
	 * @return
	 */
	public List<Behavior> behaviorOfConversation() {
		List<Behavior> behaviors = new ArrayList<>();
		behaviors.add(BEHAVIOR_GREETING);
		behaviors.add(BEHAVIOR_GROUP_GREETING);
		behaviors.add(BEHAVIOR_CHAT_MORE);
		behaviors.add(BEHAVIOR_FARE);
		behaviors.add(BEHAVIOR_ASK_FOR_FARE);
		behaviors.add(BEHAVIOR_RETURN_FARE);
		behaviors.add(BEHAVIOR_INVITE_ME_AS_LINE_FRIEND);
		behaviors.add(BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND);
		behaviors.add(BEHAVIOR_GIMME_YOUR_LINE_INVITATION);
		behaviors.add(BEHAVIOR_PICTURES_VIEWABLE);
		behaviors.add(BEHAVIOR_ACCEPT_PICTURES_VIEWABLE);
		return behaviors;
	}

	/**
	 * 日期轉換成字串形式顯示在前端頁面
	 *
	 * @param d
	 * @return
	 */
	public String calendarToString(Date d) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone(
			Servant.ASIA_TAIPEI_ZONE_ID
		));
		calendar.setTime(d);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DATE);
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int am_pm = calendar.get(Calendar.AM_PM); // 0是上午, 1是下午

		String dayString = null;
		switch (day) {
			case 0:
				dayString = "日";
				break;
			case 1:
				dayString = "一";
				break;
			case 2:
				dayString = "二";
				break;
			case 3:
				dayString = "三";
				break;
			case 4:
				dayString = "四";
				break;
			case 5:
				dayString = "五";
				break;
			case 6:
				dayString = "六";
				break;
		}

		String twelveHourClock = null;
		switch (am_pm) {
			case 0:
				twelveHourClock = "上午";
				break;
			case 1:
				twelveHourClock = "下午";
				break;
		}

		return String.format(
			"%d年%d月%d日 週%s, %s%s:%s",
			year,
			month,
			date,
			dayString,
			twelveHourClock,
			hour < 10 ? "0" + Integer.toString(hour) : Integer.toString(hour),
			minute < 10 ? "0" + Integer.toString(minute) : Integer.toString(minute)
		);
	}

	/**
	 * 更多評價
	 *
	 * @param page
	 * @param locale
	 * @return
	 */
	public JSONArray moreRate(Page<History> page, Locale locale) {
		JSONArray jsonArray = new JSONArray();
		page.getContent().stream().map(history -> {
			JSONObject json = new JSONObject();
			json.put("nickname", history.getInitiative().getNickname());
			json.put("identifier", history.getInitiative().getIdentifier());
			json.put("rate", history.getRate());
			json.put("comment", history.getComment());
			json.put("profileImage", String.format(
				"https://%s/profileImage/%s",
				Servant.STATIC_HOST,
				history.getInitiative().getProfileImage()
			));
			return json;
		}).forEachOrdered(json -> {
			jsonArray.put(json);
		});
		return jsonArray;
	}

	/**
	 * 更新最后活跃时戳。
	 *
	 * @param timestamp 自 EPOCH 以来的毫秒数
	 * @param username 帐号(手机号)
	 */
	@Transactional
	public void lastActive(long timestamp, String username) {
		new NamedParameterJdbcTemplate(
			jdbcTemplate.getDataSource()
		).update(
			"UPDATE\"qing_ren\"SET\"huo_yue\"=:active WHERE\"id\"=:id",
			new MapSqlParameterSource().
				addValue(
					"active",
					new Date(timestamp),
					Types.TIMESTAMP
				).
				addValue(
					"id",
					jdbcTemplate.query(
						"SELECT\"id\"FROM\"users\"WHERE\"username\"=?",
						(ps) -> {
							ps.setString(1, username);
						},
						(ResultSet resultSet) -> {
							if (resultSet.next()) {
								return resultSet.getInt("id");
							}
							return null;
						}
					)
				)
		);
	}

	/**
	 * 條件搜尋 Document
	 *
	 * @param document
	 * @param lover
	 * @param locale
	 * @return
	 */
	public Document filterDocument(Document document, Lover lover, Locale locale) {
		Element documentElement = document.getDocumentElement();
		for (Lover.BodyType bodyType : Lover.BodyType.values()) {
			Element bodyTypeElement = document.createElement("bodyType");
			bodyTypeElement.setTextContent(
				messageSource.getMessage(
					bodyType.toString(),
					null,
					locale
				)
			);
			bodyTypeElement.setAttribute(
				"bodyTypeEnum", bodyType.toString()
			);
			documentElement.appendChild(bodyTypeElement);
		}

		for (Lover.Education education : Lover.Education.values()) {
			Element educationElement = document.createElement("education");
			educationElement.setTextContent(
				messageSource.getMessage(
					education.toString(),
					null,
					locale
				)
			);
			educationElement.setAttribute(
				"educationEnum", education.toString()
			);
			documentElement.appendChild(educationElement);
		}

		for (Lover.Marriage marriage : Lover.Marriage.values()) {
			Element marriageElement = document.createElement("marriage");
			marriageElement.setTextContent(
				messageSource.getMessage(
					marriage.toString(),
					null,
					locale
				)
			);
			marriageElement.setAttribute(
				"marriageEnum", marriage.toString()
			);
			documentElement.appendChild(marriageElement);
		}

		for (Lover.Smoking smoking : Lover.Smoking.values()) {
			Element smokingElement = document.createElement("smoking");
			smokingElement.setTextContent(
				messageSource.getMessage(
					smoking.toString(),
					null,
					locale
				)
			);
			smokingElement.setAttribute(
				"smokingEnum", smoking.toString()
			);
			documentElement.appendChild(smokingElement);
		}

		for (Lover.Drinking drinking : Lover.Drinking.values()) {
			Element drinkingElement = document.createElement("drinking");
			drinkingElement.setTextContent(
				messageSource.getMessage(
					drinking.toString(),
					null,
					locale
				)
			);
			drinkingElement.setAttribute(
				"drinkingEnum", drinking.toString()
			);
			documentElement.appendChild(drinkingElement);
		}

		if (!lover.getGender()) {
			annualIncomeRepository.findAllByOrderByIdAsc().stream().map(annualIncome -> {
				Element annualIncomeElement = document.createElement("annualIncome");
				annualIncomeElement.setTextContent(
					messageSource.getMessage(
						annualIncome.getName(),
						null,
						locale
					)
				);
				annualIncomeElement.setAttribute(
					"annualIncomeID", annualIncome.getId().toString()
				);
				return annualIncomeElement;
			}).forEachOrdered(annualIncomeElement -> {
				documentElement.appendChild(annualIncomeElement);
			});
		}

		if (lover.getGender()) {
			allowanceRepository.findAllByOrderByIdAsc().stream().map(allowance -> {
				Element allowanceElement = document.createElement("allowance");
				allowanceElement.setTextContent(
					messageSource.getMessage(
						allowance.getName(),
						null,
						locale
					)
				);
				allowanceElement.setAttribute(
					"allowanceID", allowance.getId().toString()
				);
				return allowanceElement;
			}).forEachOrdered(allowanceElement -> {
				documentElement.appendChild(allowanceElement);
			});
		}

		locationRepository.findAll().stream().map(location -> {
			Element locationElement = document.createElement("location");
			locationElement.setTextContent(
				messageSource.getMessage(
					location.getName(),
					null,
					locale
				)
			);
			locationElement.setAttribute(
				"locationID", location.getId().toString()
			);
			return locationElement;
		}).forEachOrdered(locationElement -> {
			documentElement.appendChild(locationElement);
		});

		companionshipRepository.findAll().stream().map(companionship -> {
			Element serviceElement = document.createElement("service");
			serviceElement.setTextContent(
				messageSource.getMessage(
					companionship.getName(),
					null,
					locale
				)
			);
			serviceElement.setAttribute(
				"serviceID", companionship.getId().toString()
			);
			return serviceElement;
		}).forEachOrdered(serviceElement -> {
			documentElement.appendChild(serviceElement);
		});

		return document;
	}

	/**
	 * 條件搜尋
	 *
	 * @param mofo
	 * @param p
	 * @param nickname
	 * @param maximumAge
	 * @param minimumAge
	 * @param maximumHeight
	 * @param minimumHeight
	 * @param maximumWeight
	 * @param minimumWeight
	 * @param bodyType
	 * @param education
	 * @param marriage
	 * @param smoking
	 * @param drinking
	 * @param annualIncome
	 * @param allowance
	 * @param location
	 * @param companionship
	 * @param locale
	 * @return
	 */
	public JSONObject filter(Lover mofo, int p, String nickname,
		Integer maximumAge, Integer minimumAge,
		Short maximumHeight, Short minimumHeight,
		Short maximumWeight, Short minimumWeight,
		Lover.BodyType bodyType, Lover.Education education,
		Lover.Marriage marriage, Lover.Smoking smoking,
		Lover.Drinking drinking, AnnualIncome annualIncome,
		Allowance allowance, Location location, Companionship companionship,
		Locale locale) {

		Page<Lover> page = loverRepository.findAll(
			LoverSpecification.filter(
				mofo,
				nickname,
				maximumAge,
				minimumAge,
				maximumHeight,
				minimumHeight,
				maximumWeight,
				minimumWeight,
				bodyType,
				education,
				marriage,
				smoking,
				drinking,
				annualIncome,
				allowance,
				findInceptions(companionship, location),
				new HashSet<>(getExceptions(mofo))
			),
			PageRequest.of(p, 10));

		JSONObject jsonObject = new JSONObject();
		return jsonObject.
			put("response", true).
			put(
				"result",
				loverService.seeMoreLover(mofo, page, locale)
			).
			put(
				"pagination",
				new JSONObject().
					put(
						"hasNext",
						page.hasNext() ? page.nextOrLastPageable().getPageNumber() : null
					).
					put(
						"hasPrev",
						page.hasPrevious() ? page.previousOrFirstPageable().getPageNumber() : null
					)
			);
	}

	// 免費會員男生每日可傳10位女生
	public boolean maleAbleToSendMsgsWithinOneDay(Lover male) {
		List<Lover> list = maleHasSentFemaleListWithinOneDay(male);
		return (isVIP(male) || isVVIP(male) || isTrial(male)) || list.size() < 10;
	}

	public List<Lover> maleHasSentFemaleListWithinOneDay(Lover male) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone(
			Servant.ASIA_TAIPEI_ZONE_ID
		));
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DATE);

		Date earliestDate = servant.earliestDate(year, month, date);
		Date latestDate = servant.latestDate(year, month, date);

		return historyRepository.findDistinctPassive(male, BEHAVIOR_CHAT_MORE, earliestDate, latestDate);
	}

	@Transactional(readOnly = true)
	public JSONObject getTrialCodeUsedList(TrialCode trialCode) {
		List<UsedTrialCode> usedTrialCodes
			= usedTrialCodeRepository.findByTrialCode(trialCode);

		JSONObject jSONObject = new JSONObject();
		JSONArray array = new JSONArray();
		for (UsedTrialCode usedTrialCode : usedTrialCodes) {
			Lover lover = usedTrialCode.getLover();
			Date date = historyRepository.findTop1ByInitiativeAndBehaviorOrderByOccurredDesc(lover, BEHAVIOR_TRIAL_CODE).getOccurred();
			JSONObject json = new JSONObject();
			json.
				put(
					"nickname",
					lover.getNickname()
				).
				put(
					"identifier",
					lover.getIdentifier()
				).
				put(
					"date",
					servant.DATE_TIME_FORMATTER_yyyyMMddHHmm.format(
						Servant.toTaipeiZonedDateTime(
							date
						).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
					)
				);
			array.put(json);
		}
		return jSONObject.put("list", array);
	}

	@Transactional(readOnly = true)
	public JSONObject getVipHistory(Lover lover) {

		List<History> histories
			= historyRepository.findByInitiativeAndBehaviorInOrderByOccurredDesc(
				lover,
				Arrays.asList(new History.Behavior[]{
				BEHAVIOR_MONTHLY_CHARGED,
				BEHAVIOR_TRIAL_CODE})
			);

		JSONObject jSONObject = new JSONObject();
		JSONArray array = new JSONArray();
		for (History history : histories) {
			Date date = history.getOccurred();
			String vip = null;
			if (Objects.equals(history.getBehavior(), BEHAVIOR_TRIAL_CODE)) {
				try {
					vip = usedTrialCodeRepository.findByLover(lover).getTrialCode().getCode();
				} catch (NullPointerException e) {
					vip = "單日體驗";
				}
			} else if (Objects.equals(history.getLuJie().getItemName(), "長期貴賓")) {
				vip = "1288";
			} else {
				vip = "1688";
			}
			JSONObject json = new JSONObject();
			json.
				put(
					"vip",
					vip
				).
				put(
					"date",
					servant.DATE_TIME_FORMATTER_yyyyMMddHHmm.format(
						Servant.toTaipeiZonedDateTime(
							date
						).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
					)
				);
			array.put(json);
		}
		return jSONObject.put("list", array);
	}

	public Document settingDocument(Document document, Lover lover) {
		Element documentElement = document.getDocumentElement();

		Collection<Blacklist> blacklists = blacklistRepository.findByBlockerAndBlockedNot(lover, lover);
		for (Blacklist blacklist : blacklists) {
			Element blockedElement = document.createElement("blocked");
			blockedElement.setAttribute(
				"identifier",
				blacklist.getBlocked().getIdentifier().toString()
			);
			blockedElement.setAttribute(
				"nickname",
				blacklist.getBlocked().getNickname()
			);
			documentElement.appendChild(blockedElement);
		}

		return document;
	}

	public JSONObject unlock(Lover blocker, Lover blocked) {
		Blacklist blacklist = blacklistRepository.
			findOneByBlockerAndBlocked(blocker, blocked).get();
		blacklistRepository.delete(blacklist);
		blacklistRepository.flush();

		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(blacklist.getBlocked().getNickname()).
			toJSONObject();
	}

	/**
	 * 是否為小編
	 *
	 * @param lover
	 * @return
	 */
	public Boolean isCustomerService(Lover lover) {
		int count = privilegeRepository.countByLoverAndRole(
			lover,
			roleRepository.findOneByTextualRepresentation("ROLE_XIAOBIAN")
		);
		return count > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * 是否跳過首頁導覽
	 *
	 * @param lover
	 * @return
	 */
	public Boolean oneTimeGuidance(Lover lover) {
		if (!loverService.existOneOffEntity(lover)) {
			oneOffRepository.saveAndFlush(
				new OneOff(lover.getId(), Boolean.TRUE)
			);
			return Boolean.FALSE;
		}

		OneOff oneOff = oneOffRepository.findById(lover.getId()).get();
		Boolean bool = oneOff.getIndexGuidance();

		oneOff.setIndexGuidance(Boolean.TRUE);
		oneOffRepository.saveAndFlush(oneOff);

		return bool;
	}

	/**
	 * 是否存在一次性資料表
	 *
	 * @param lover
	 * @return
	 */
	public Boolean existOneOffEntity(Lover lover) {
		OneOff oneOff = oneOffRepository.findById(lover.getId()).orElse(null);
		return Objects.nonNull(oneOff) ? Boolean.TRUE : Boolean.FALSE;
	}
}
