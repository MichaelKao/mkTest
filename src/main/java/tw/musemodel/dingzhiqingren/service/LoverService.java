package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.Specifications;
import tw.musemodel.dingzhiqingren.WebSocketServer;
import tw.musemodel.dingzhiqingren.entity.Activation;
import tw.musemodel.dingzhiqingren.entity.Allowance;
import tw.musemodel.dingzhiqingren.entity.AnnualIncome;
import tw.musemodel.dingzhiqingren.entity.Country;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Picture;
import tw.musemodel.dingzhiqingren.entity.Role;
import tw.musemodel.dingzhiqingren.entity.ServiceTag;
import tw.musemodel.dingzhiqingren.entity.User;
import tw.musemodel.dingzhiqingren.entity.WithdrawalInfo;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord.WayOfWithdrawal;
import tw.musemodel.dingzhiqingren.event.SignedUpEvent;
import tw.musemodel.dingzhiqingren.model.Activated;
import tw.musemodel.dingzhiqingren.model.EachWithdrawal;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.SignUp;
import tw.musemodel.dingzhiqingren.repository.ActivationRepository;
import tw.musemodel.dingzhiqingren.repository.AllowanceRepository;
import tw.musemodel.dingzhiqingren.repository.AnnualIncomeRepository;
import tw.musemodel.dingzhiqingren.repository.CountryRepository;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LocationRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.PictureRepository;
import tw.musemodel.dingzhiqingren.repository.ServiceTagRepository;
import tw.musemodel.dingzhiqingren.repository.UserRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalInfoRepository;
import tw.musemodel.dingzhiqingren.repository.WithdrawalRecordRepository;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;
import static tw.musemodel.dingzhiqingren.service.Servant.PAGE_SIZE_ON_THE_WALL;
import tw.musemodel.dingzhiqingren.specification.LoverSpecification;

/**
 * 服务层：情人
 *
 * @author p@musemodel.tw
 */
@Service
public class LoverService {

	private final static Logger LOGGER = LoggerFactory.getLogger(LoverService.class);

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

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final static Short NUMBER_OF_GROUP_GREETING = Short.valueOf(System.getenv("NUMBER_OF_GROUP_GREETING"));

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
	private Servant servant;

	@Autowired
	private ActivationRepository activationRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AmazonWebServices amazonWebServices;

	@Autowired
	private PictureRepository pictureRepository;

	@Autowired
	private LineMessagingService lineMessagingService;

	@Autowired
	private AnnualIncomeRepository annualIncomeRepository;

	@Autowired
	private AllowanceRepository allowanceRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private ServiceTagRepository serviceTagRepository;

	@Autowired
	private WithdrawalRecordRepository withdrawalRecordRepository;

	@Autowired
	private WithdrawalInfoRepository withdrawalInfoRepository;

	public List<Lover> loadLovers() {
		return loverRepository.findAll();
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

		/*
		 初始化身份
		 */
		Collection<Role> roles = new HashSet<>();
		roles.add(servant.getRole(Servant.ROLE_ADVENTURER));
		lover.setRoles(roles);

		/*
		 初始化推荐码
		 */
		String string = RandomStringUtils.randomAlphanumeric(8);
		while (loverRepository.countByReferralCode(string) > 0) {
			string = RandomStringUtils.randomAlphanumeric(8);
		}
		lover.setReferralCode(string);

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
			SPRING_SECURITY_CONTEXT_KEY,
			securityContext
		);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"activated.done",
				null,
				locale
			)).
			withRedirect("/me.asp").
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
		List<String> accessTokens = new ArrayList<String>();
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_FIRST);
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_SECOND);
		lineMessagingService.notifyDev(
			accessTokens,
			String.format(
				"手機號碼 %s 的激活碼：%s️",
				String.format(
					"0%s",
					lover.getLogin()
				),
				string
			));

		PublishResult publishResult = AMAZON_SNS.publish(
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

		if (loverRepository.countByCountryAndLogin(country, login) > 0) {
			throw new RuntimeException("signUp.exists");
		}

		if (calculateAge(signUp.getBirthday()) < 18) {
			throw new RuntimeException("signUp.mustBeAtLeast18yrsOld");
		}

		UUID identifier = UUID.randomUUID();

		Lover lover = new Lover();
		lover.setIdentifier(identifier);
		lover.setCountry(country);
		lover.setLogin(login);
		lover.setGender(signUp.getGender());
		lover.setBirthday(signUp.getBirthday());
		lover.setProfileImage(identifier.toString());
		lover.setAboutMe("正在找尋一段比閨蜜還親密的養蜜關係");
		lover.setIdealConditions("合得來的養蜜");
		lover.setGreeting("嗨，有機會成為養蜜嗎?");
		lover.setRegistered(new Date(System.currentTimeMillis()));
		if (Objects.nonNull(signUp.getReferralCode())) {
			Lover referrer = loverRepository.findByReferralCode(signUp.getReferralCode());
			lover.setReferrer(referrer);
		}
		lover = loverRepository.saveAndFlush(lover);

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
		List<String> accessTokens = new ArrayList<String>();
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_FIRST);
		accessTokens.add(LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_SECOND);
		lineMessagingService.notifyDev(
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
	 * 是否为 VIP⁉️
	 *
	 * @param lover 用户
	 * @return 布尔值
	 */
	public boolean isVIP(Lover lover) {
		return Objects.nonNull(lover.getVip()) && lover.getVip().after(new Date(System.currentTimeMillis()))
			&& Objects.nonNull(lover.getMaleSpecies()) && Objects.equals(lover.getMaleSpecies(), Lover.MaleSpecies.VIP);
	}

	public boolean isVVIP(Lover lover) {
		// 貴賓到期日
		Date vip = lover.getVip();
		Lover.MaleSpecies maleSpecies = lover.getMaleSpecies();
		return Objects.nonNull(vip)
			&& vip.after(new Date(System.currentTimeMillis()))
			&& Objects.nonNull(maleSpecies)
			&& Objects.equals(maleSpecies, Lover.MaleSpecies.VVIP);
	}

	public Document readDocument(Lover lover, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		Element loverElement = document.createElement("lover");
		loverElement.setAttribute("identifier", lover.getIdentifier().toString());
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

		if (lover.getGender() && Objects.nonNull(lover.getVip()) && lover.getVip().after(new Date())) {
			loverElement.setAttribute("vip", null);
		}

		if (Objects.nonNull(lover.getRelief())) {
			Boolean relief = lover.getRelief();
			loverElement.setAttribute(
				"relief",
				relief.toString()
			);
		}

		String inviteMeAsFreind = lover.getInviteMeAsLineFriend();
		if (Objects.nonNull(inviteMeAsFreind) && !inviteMeAsFreind.isBlank() && !inviteMeAsFreind.isEmpty()) {
			String uri = lover.getInviteMeAsLineFriend();
			Boolean isLine = Servant.isLine(URI.create(uri));
			Boolean isWeChat = Servant.isWeChat(URI.create(uri));

			loverElement.setAttribute(
				"socialMedia",
				isLine ? "line" : "weChat"
			);
		}

		Element profileImageElement = document.createElement("profileImage");
		if (Objects.nonNull(lover.getProfileImage())) {
			profileImageElement.setTextContent(
				String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					lover.getProfileImage()
				)
			);
		}
		loverElement.appendChild(profileImageElement);

		List<Picture> pictures = pictureRepository.findByLover(lover);
		for (Picture picture : pictures) {
			Element pictureElement = document.createElement("picture");
			pictureElement.setTextContent(
				String.format(
					"https://%s/pictures/%s",
					Servant.STATIC_HOST,
					picture.getIdentifier().toString()
				)
			);
			loverElement.appendChild(pictureElement);
		}

		if (Objects.nonNull(lover.getLocations())) {
			for (Location location : lover.getLocations()) {
				Element locationElement = document.createElement("location");
				locationElement.setAttribute("id", location.getId().toString());
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

		if (Objects.nonNull(lover.getServices())) {
			for (ServiceTag service : lover.getServices()) {
				Element serviceElement = document.createElement("service");
				serviceElement.setAttribute("id", service.getId().toString());
				serviceElement.setTextContent(
					messageSource.getMessage(
						service.getName(),
						null,
						locale
					)
				);
				loverElement.appendChild(serviceElement);
			}
		}

		if (Objects.nonNull(lover.getNickname())) {
			Element nicknameElement = document.createElement("nickname");
			nicknameElement.setTextContent(lover.getNickname());
			loverElement.appendChild(nicknameElement);
		}

		Date birth = lover.getBirthday();
		if (Objects.nonNull(birth)) {
			Element ageElement = document.createElement("age");
			ageElement.setTextContent(
				calculateAge(lover).toString()
			);
			loverElement.appendChild(ageElement);
		}

		if (Objects.nonNull(lover.getGender())) {
			Boolean gender = lover.getGender();
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

		if (Objects.nonNull(lover.getAboutMe())) {
			String html = servant.markdownToHtml(lover.getAboutMe());
			Element aboutMeElement = document.createElement("aboutMe");
			CDATASection cDATASection = document.createCDATASection(html);
			aboutMeElement.appendChild(cDATASection);
			loverElement.appendChild(aboutMeElement);
		}

		if (Objects.nonNull(lover.getBodyType())) {
			Element bodyTypeElement = document.createElement("bodyType");
			bodyTypeElement.setTextContent(
				messageSource.getMessage(
					lover.getBodyType().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(bodyTypeElement);
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

		if (Objects.nonNull(lover.getEducation())) {
			Element educationElement = document.createElement("education");
			educationElement.setTextContent(
				messageSource.getMessage(
					lover.getEducation().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(educationElement);
		}

		if (Objects.nonNull(lover.getMarriage())) {
			Element marriageElement = document.createElement("marriage");
			marriageElement.setTextContent(
				messageSource.getMessage(
					lover.getMarriage().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(marriageElement);
		}

		if (Objects.nonNull(lover.getOccupation())) {
			Element occupationElement = document.createElement("occupation");
			occupationElement.setTextContent(lover.getOccupation());
			loverElement.appendChild(occupationElement);
		}

		if (Objects.nonNull(lover.getSmoking())) {
			Element smokingElement = document.createElement("smoking");
			smokingElement.setTextContent(
				messageSource.getMessage(
					lover.getSmoking().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(smokingElement);
		}

		if (Objects.nonNull(lover.getDrinking())) {
			Element drinkingElement = document.createElement("drinking");
			drinkingElement.setTextContent(
				messageSource.getMessage(
					lover.getDrinking().toString(),
					null,
					locale
				)
			);
			loverElement.appendChild(drinkingElement);
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

		if (lover.getGender() && Objects.nonNull(lover.getAnnualIncome())) {
			Element annualIncomeElement = document.createElement("annualIncome");
			AnnualIncome annualIncome = lover.getAnnualIncome();
			annualIncomeElement.setTextContent(
				messageSource.getMessage(
					annualIncome.getName(),
					null,
					locale
				)
			);
			loverElement.appendChild(annualIncomeElement);
		}

		if (!lover.getGender() && Objects.nonNull(lover.getAllowance())) {
			Element allowanceElement = document.createElement("allowance");
			Allowance allowance = lover.getAllowance();
			allowanceElement.setTextContent(
				messageSource.getMessage(
					allowance.getName(),
					null,
					locale
				)
			);
			loverElement.appendChild(allowanceElement);
		}

		if (Objects.nonNull(lover.getIdealConditions())) {
			String html = servant.markdownToHtml(lover.getIdealConditions());
			Element idealConditionsElement = document.createElement("idealConditions");
			CDATASection cDATASection = document.createCDATASection(html);
			idealConditionsElement.appendChild(cDATASection);
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

		if (Objects.nonNull(lover.getActive())) {
			Element activeElement = document.createElement("active");
			activeElement.setTextContent(
				DATE_TIME_FORMATTER.format(
					servant.toTaipeiZonedDateTime(
						lover.getActive()
					).withZoneSameInstant(
						Servant.ASIA_TAIPEI
					)
				)
			);
			loverElement.appendChild(activeElement);
		}

		List<History> rateList = historyRepository.findByPassiveAndBehaviorOrderByOccurredDesc(lover, History.Behavior.PING_JIA);
		if (Objects.nonNull(rateList)) {
			for (History rate : rateList) {
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
					DATE_TIME_FORMATTER.format(
						servant.toTaipeiZonedDateTime(
							rate.getOccurred()
						).withZoneSameInstant(Servant.ASIA_TAIPEI)
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
			}
		}

		return document;
	}

	public Document writeDocument(Lover lover, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
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

		for (Location location : locationRepository.findAll()) {
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
			for (Location loc : lover.getLocations()) {
				if (Objects.equals(loc, location)) {
					locationElement.setAttribute(
						"locationSelected", ""
					);
				}
			}
			loverElement.appendChild(locationElement);
		}

		for (ServiceTag service : serviceTagRepository.findAll()) {
			Element serviceElement = document.createElement("service");
			serviceElement.setTextContent(
				messageSource.getMessage(
					service.getName(),
					null,
					locale
				)
			);
			serviceElement.setAttribute(
				"serviceID", service.getId().toString()
			);
			for (ServiceTag ser : lover.getServices()) {
				if (Objects.equals(ser, service)) {
					serviceElement.setAttribute(
						"serviceSelected", ""
					);
				}
			}
			loverElement.appendChild(serviceElement);
		}

		if (lover.getGender()) {
			for (AnnualIncome annualIncome : annualIncomeRepository.findAllByOrderByIdAsc()) {
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
				loverElement.appendChild(annualIncomeElement);
			}
		}

		if (!lover.getGender()) {
			for (Allowance allowance : allowanceRepository.findAllByOrderByIdAsc()) {
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
				loverElement.appendChild(allowanceElement);
			}
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

		if (Objects.isNull(model.getOccupation())) {
			return new JavaScriptObjectNotation().
				withReason("請填寫職業").
				withResponse(false).
				toJSONObject();
		}

		if (!lover.getGender() && (model.getInviteMeAsLineFriend().isBlank() || model.getInviteMeAsLineFriend().isEmpty())) {
			return new JavaScriptObjectNotation().
				withReason("請上傳 LINE 或 WeChat 的 QRcode").
				withResponse(false).
				toJSONObject();
		}

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

		if (Objects.isNull(model.getRelationship())) {
			return new JavaScriptObjectNotation().
				withReason("請擇一相處關係").
				withResponse(false).
				toJSONObject();
		}

		if (lover.getGender() && Objects.isNull(model.getAnnualIncome())) {
			return new JavaScriptObjectNotation().
				withReason("請擇一年收入").
				withResponse(false).
				toJSONObject();
		}

		if (!lover.getGender() && Objects.isNull(model.getAllowance())) {
			return new JavaScriptObjectNotation().
				withReason("請擇一期望零用金").
				withResponse(false).
				toJSONObject();
		}

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

		if (lover.getLocations().size() < 1) {
			return new JavaScriptObjectNotation().
				withReason("請至少填入一個地區").
				withResponse(false).
				toJSONObject();
		}

		if (lover.getServices().size() < 1) {
			return new JavaScriptObjectNotation().
				withReason("請至少填入服務標籤").
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
			withRedirect("/profile/").
			toJSONObject();
	}

	public Document withdrawalDocument(Lover lover, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		Long leftPoints = honeyLeftPointsBefore7Days(lover);
		documentElement.setAttribute(
			"points",
			leftPoints.toString()
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
			DATE_TIME_FORMATTER.format(
				servant.toTaipeiZonedDateTime(
					before7DaysAgo()
				).withZoneSameInstant(Servant.ASIA_TAIPEI)
			));

		// 目前可提領的紀錄
		for (History history : historyRepository.findAll(Specifications.withdrawal(lover))) {
			Element recordElement = document.createElement("record");
			documentElement.appendChild(recordElement);

			recordElement.setAttribute(
				"date",
				DATE_TIME_FORMATTER.format(
					servant.toTaipeiZonedDateTime(
						history.getOccurred()
					).withZoneSameInstant(Servant.ASIA_TAIPEI)
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

			Short points = Objects.equals(history.getBehavior().name(), "CHE_MA_FEI") ? history.getPoints() : (short) (history.getPoints() / 2);
			recordElement.setAttribute(
				"points",
				Integer.toString(Math.abs(points))
			);
		}

		// 等待匯款中的記錄、提領歷史紀錄
		for (EachWithdrawal eachWithdrawal : withdrawalRecordRepository.findHoneyAllGroupByHoneyAndStatusAndWayAndTimeStamp(lover)) {
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
				DATE_TIME_FORMATTER.format(
					servant.toTaipeiZonedDateTime(
						timestamp
					).withZoneSameInstant(Servant.ASIA_TAIPEI)
				));

			recordElement.setAttribute(
				"points",
				eachWithdrawal.getPoints().toString()
			);

			Boolean status = eachWithdrawal.getStatus();
			recordElement.setAttribute(
				"status",
				status.toString()
			);

			for (WithdrawalRecord withdrawalRecord
				: withdrawalRecordRepository.findByHoneyAndStatusAndTimestamp(honey, status, timestamp)) {
				Element historyElement = document.createElement("history");
				recordElement.appendChild(historyElement);
				historyElement.setAttribute(
					"date",
					DATE_TIME_FORMATTER.format(
						servant.toTaipeiZonedDateTime(
							withdrawalRecord.getHistory().getOccurred()
						).withZoneSameInstant(Servant.ASIA_TAIPEI)
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
					"points",
					Short.toString(withdrawalRecord.getPoints())
				);

			}
		}

		return document;
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
			Short points = Objects.equals(history.getBehavior(), BEHAVIOR_FARE) ? history.getPoints() : (short) (history.getPoints() / 2);
			WithdrawalRecord withdrawalRecord = new WithdrawalRecord(honey, (short) -points, WayOfWithdrawal.WIRE_TRANSFER, current);
			withdrawalRecord.setId(history.getId());
			withdrawalRecordRepository.saveAndFlush(withdrawalRecord);
		}

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
	 * 甜心剩餘的可提領車馬費
	 *
	 * @param honey
	 * @return
	 */
	@Transactional
	public Long honeyLeftPointsBefore7Days(Lover honey) {
		List<History> fareList = historyRepository.findByPassiveAndBehaviorAndOccurredBefore(honey, BEHAVIOR_FARE, before7DaysAgo());
		Long fareSum = 0L;
		for (History history : fareList) {
			fareSum += Math.abs(history.getPoints());
		}
		List<History> lineList = historyRepository.findByPassiveAndBehaviorAndOccurredBefore(honey, BEHAVIOR_LAI_KOU_DIAN, before7DaysAgo());
		Long lineSum = 0L;
		for (History history : lineList) {
			lineSum += Math.abs(history.getPoints()) / 2;
		}
		Long withdrawnPoints = withdrawalRecordRepository.sumPoinsByHoney(honey);
		withdrawnPoints = Objects.nonNull(withdrawnPoints) ? withdrawnPoints : 0;

		Long leftPoints = (fareSum + lineSum) - withdrawnPoints;

		return leftPoints;
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
	 * 更新地點
	 *
	 * @param location
	 * @param honey
	 * @return
	 */
	@Transactional
	public JSONObject updateLocation(Location location, Lover honey) {
		Set<Location> locations = honey.getLocations();
		for (Location loc : locations) {
			if (Objects.equals(loc, location)) {
				locations.remove(loc);
				honey.setLocations(locations);
				loverRepository.saveAndFlush(honey);
				return new JavaScriptObjectNotation().
					withResponse(true).
					toJSONObject();
			}
		}

		locations.add(location);
		honey.setLocations(locations);
		loverRepository.saveAndFlush(honey);
		return new JavaScriptObjectNotation().
			withResponse(true).
			toJSONObject();
	}

	/**
	 * 更新服務
	 *
	 * @param service
	 * @param honey
	 * @return
	 */
	@Transactional
	public JSONObject updateService(ServiceTag service, Lover honey) {
		Set<ServiceTag> services = honey.getServices();
		for (ServiceTag ser : services) {
			if (Objects.equals(ser, service)) {
				services.remove(ser);
				honey.setServices(services);
				loverRepository.saveAndFlush(honey);
				return new JavaScriptObjectNotation().
					withResponse(true).
					toJSONObject();
			}
		}

		services.add(service);
		honey.setServices(services);
		loverRepository.saveAndFlush(honey);
		return new JavaScriptObjectNotation().
			withResponse(true).
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
		List<Behavior> behaviors = behaviorsOfAnnocement(lover);
		return historyRepository.findAll(Specifications.passive(lover, behaviors));
	}

	/**
	 * 需通知的行為
	 *
	 * @param lover
	 * @return
	 */
	public List<Behavior> behaviorsOfAnnocement(Lover lover) {
		Boolean gender = lover.getGender();
		List<Behavior> behaviors = new ArrayList<Behavior>();
		behaviors.add(BEHAVIOR_CERTIFICATION_SUCCESS);
		behaviors.add(BEHAVIOR_CERTIFICATION_FAIL);
		if (gender) {
			behaviors.add(BEHAVIOR_INVITE_ME_AS_LINE_FRIEND);
			behaviors.add(BEHAVIOR_REFUSE_TO_BE_LINE_FRIEND);
			behaviors.add(BEHAVIOR_GREETING);

		}
		if (!gender) {
			behaviors.add(BEHAVIOR_GIMME_YOUR_LINE_INVITATION);
			behaviors.add(BEHAVIOR_FARE);
			behaviors.add(BEHAVIOR_WITHDRAWAL_FAIL);
			behaviors.add(BEHAVIOR_WITHDRAWAL_SUCCESS);
		}
		return behaviors;
	}

	/**
	 * 收藏/搜尋頁面的情人基本資訊
	 *
	 * @param document
	 * @param lovers
	 * @return
	 */
	public Document loversSimpleInfo(Document document, Collection<Lover> lovers) {
		Element documentElement = document.getDocumentElement();

		for (Lover lover : lovers) {
			if (Objects.nonNull(lover.getDelete())) {
				continue;
			}
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
			if (isVVIP(lover)) {
				loverElement.setAttribute("vip", null);
			}
			if (Objects.nonNull(lover.getRelief())) {
				Boolean relief = lover.getRelief();
				loverElement.setAttribute(
					"relief",
					relief ? "true" : "false"
				);
			}
		}

		return document;
	}

	/**
	 * 首頁的列表 document
	 *
	 * @param document
	 * @param lover
	 * @param locale
	 * @return
	 */
	public Document indexDocument(Document document, Lover lover, Locale locale) {
		Element documentElement = document.getDocumentElement();

		Element areaElement = document.createElement("area");
		documentElement.appendChild(areaElement);

		if (!lover.getGender()) {
			areaElement.appendChild(createElement(
				document.createElement("vip"),
				vipOnTheWall(
					lover,
					0,
					PAGE_SIZE_ON_THE_WALL
				),
				locale
			));//甜心才会显示的贵宾会员列表区块
		}

		areaElement.appendChild(createElement(
			document.createElement("relief"),
			relievingOnTheWall(
				lover,
				0,
				PAGE_SIZE_ON_THE_WALL
			),
			locale
		));//安心认证列表区块

		areaElement.appendChild(createElement(
			document.createElement("active"),
			latestActiveOnTheWall(
				lover,
				0,
				PAGE_SIZE_ON_THE_WALL
			),
			locale
		));//最近活跃列表区块

		areaElement.appendChild(createElement(
			document.createElement("register"),
			latestRegisteredOnTheWall(
				lover,
				0,
				PAGE_SIZE_ON_THE_WALL
			),
			locale
		));//最新注册列表区块

		return document;
	}

	/**
	 * 首頁的列表 Element
	 *
	 * @param element
	 * @param page
	 * @param locale
	 * @return
	 */
	public Element createElement(Element element, Page<Lover> page, Locale locale) {
		if (page.getTotalPages() <= 1) {
			element.setAttribute("lastPage", null);
		}
		Document document = element.getOwnerDocument();
		for (Lover lover : page.getContent()) {
			Element sectionElement = document.createElement("section");
			element.appendChild(sectionElement);
			if (Objects.nonNull(lover.getNickname())) {
				Element nicknameElement = document.createElement("nickname");
				nicknameElement.setTextContent(lover.getNickname());
				sectionElement.appendChild(nicknameElement);
			}
			if (isVVIP(lover)) {
				sectionElement.setAttribute("vip", null);
			}
			if (Objects.nonNull(lover.getRelief())) {
				Boolean relief = lover.getRelief();
				sectionElement.setAttribute(
					"relief",
					relief.toString()
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
				sectionElement.appendChild(relationshipElement);
			}
			if (Objects.nonNull(lover.getLocations())) {
				Set<Location> locations = lover.getLocations();
				List<Location> locationList = new ArrayList<>(locations);
				Collections.shuffle(locationList);
				int count = 0;
				for (Location location : locationList) {
					count += 1;
					if (count <= 3) {
						Element locationElement = document.createElement("location");
						locationElement.setTextContent(
							messageSource.getMessage(
								location.getName(),
								null,
								locale
							)
						);
						sectionElement.appendChild(locationElement);
					}
				}
			}
			Element ageElement = document.createElement("age");
			ageElement.setTextContent(calculateAge(lover).toString());
			sectionElement.appendChild(ageElement);

			Element identifierElement = document.createElement("identifier");
			identifierElement.setTextContent(lover.getIdentifier().toString());
			sectionElement.appendChild(identifierElement);

			Element profileImageElement = document.createElement("profileImage");
			profileImageElement.setTextContent(
				String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					lover.getProfileImage()
				)
			);
			sectionElement.appendChild(profileImageElement);
		}
		return element;
	}

	/**
	 * 看更多甜心/男仕
	 *
	 * @param page
	 * @param locale
	 * @return
	 */
	public JSONArray seeMore(Page<Lover> page, Locale locale) {
		JSONArray jsonArray = new JSONArray();
		for (Lover lover : page.getContent()) {
			JSONObject json = new JSONObject();
			json.put("nickname", lover.getNickname());
			if (isVVIP(lover)) {
				json.put("vip", true);
			}
			if (Objects.nonNull(lover.getRelief()) && lover.getRelief()) {
				json.put("relief", true);
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
			if (Objects.nonNull(lover.getLocations())) {
				JSONArray jaLocations = new JSONArray();
				Set<Location> locations = lover.getLocations();
				List<Location> locationList = new ArrayList<>(locations);
				Collections.shuffle(locationList);
				int count = 0;
				for (Location location : locationList) {
					JSONObject joLocation = new JSONObject();
					count += 1;
					if (count <= 3) {
						joLocation.put(
							"location",
							messageSource.getMessage(
								location.getName(),
								null,
								locale
							)
						);
						jaLocations.put(joLocation);
					}
				}
				json.put(
					"location", jaLocations
				);
			}
			json.put("profileImage", String.format(
				"https://%s/profileImage/%s",
				Servant.STATIC_HOST,
				lover.getProfileImage()
			));
			jsonArray.put(json);
		}
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
		return loverRepository.findAll(
			LoverSpecification.relievingOnTheWall(mofo),
			PageRequest.of(p, s)
		);
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
	 * 未封号的情人们，以活跃降幂排序；用于首页的最近活跃列表区块。
	 *
	 * @param mofo 用户号
	 * @param p 第几页
	 * @param s 每页几笔
	 * @return 以活跃排序的男士们
	 */
	@Transactional(readOnly = true)
	public Page<Lover> latestActiveOnTheWall(Lover mofo, int p, int s) {
		return loverRepository.findAll(
			LoverSpecification.latestActiveOnTheWall(mofo),
			PageRequest.of(p, s)
		);
	}

	/**
	 * 未封号的情人们，以註冊时间降幂排序；用于首页的最新注册列表区块。
	 *
	 * @param mofo 用户号
	 * @param p 第几页
	 * @param s 每页几笔
	 * @return 以註冊时间排序的甜心们
	 */
	@Transactional(readOnly = true)
	public Page<Lover> latestRegisteredOnTheWall(Lover mofo, int p, int s) {
		return loverRepository.findAll(
			LoverSpecification.latestRegisteredOnTheWall(mofo),
			PageRequest.of(p, s)
		);
	}

	/**
	 * @param mofo 用户号
	 * @param p 第几页
	 * @param s 每页几笔
	 * @return 貴賓男士们
	 */
	@Transactional(readOnly = true)
	public Page<Lover> vipOnTheWall(Lover mofo, int p, int s) {
		return loverRepository.findAll(
			LoverSpecification.vipOnTheWall(mofo),
			PageRequest.of(p, s)
		);
	}

	public JSONObject groupGreeting(Lover female, String greetingMessage, Locale locale) {
		LOGGER.debug(
			"2143\t群發進到服務層:\n\n{}\n",
			greetingMessage
		);
		if (Objects.isNull(greetingMessage) || greetingMessage.isBlank()) { //招呼語不能為空
			LOGGER.debug(
				"2148\t群發招呼語為空?:\n\n{}\n",
				greetingMessage
			);
			throw new RuntimeException("greet.greetingMessageMustntBeNull");
		}
		if (within12hrsFromLastGroupGreeting(female)) { //24小時內已群發過打招呼
			LOGGER.debug(
				"2155\t上次群發在12小時內:\n\n{}\n"
			);
			throw new RuntimeException("groupGreeting.within24hrsHasSent");
		}

		LOGGER.debug(
			"2161\t{}發出群發打招呼:\n\n{}\n",
			female.getNickname(),
			greetingMessage
		);

		Set<Location> locations = female.getLocations();
		LOGGER.debug(
			"2168\t女神的地區s\n\n{}\n",
			locations
		);
		List<Lover> lovers = loverRepository.findAll(LoverSpecification.malesListForGroupGreeting(true, locations));
		LOGGER.debug(
			"2173\t和女神一樣地區的男士\n\n{}\n",
			lovers
		);
		Date current = new Date(System.currentTimeMillis());
		int count = 0;
		for (Lover male : lovers) {
			LOGGER.debug(
				"2180\t和女神一樣地區的各個男士\n\n{}\n",
				male.getNickname()
			);
			// 發給 30 位男仕
			if (count >= NUMBER_OF_GROUP_GREETING) {
				break;
			}
			count += 1;
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
			webSocketServer.sendNotification(
				male.getIdentifier().toString(),
				String.format(
					"%s向你打招呼：「%s」",
					female.getNickname(),
					greetingMessage
				));
			if (hasLineNotify(male)) {
				lineMessagingService.notify(
					male,
					String.format(
						"有位甜心向你打招呼！馬上查看 https://%s/activeLogs.asp",
						servant.LOCALHOST
					));
			}
		}
		historyRepository.flush();

		LOGGER.debug(
			"2217\t完成群發\n\n{}\n",
			messageSource.getMessage(
				"groupGreeting.done",
				null,
				locale
			)
		);

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

		History lastHistory = historyRepository.findTop1ByInitiativeAndBehaviorOrderByIdDesc(female, BEHAVIOR_GROUP_GREETING);
		if (Objects.nonNull(lastHistory)) {
			Boolean within24hr = within12hrsFromLastGroupGreeting(female);
			if (within24hr) {
				documentElement.setAttribute(
					"within24hr",
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(lastHistory.getOccurred())
				);
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -5);
		Date fiveDaysAgo = cal.getTime();

		// 查看五天內的招呼紀錄
		List<History> histories = historyRepository.findByInitiativeAndBehaviorAndOccurredGreaterThanOrderByOccurredDesc(female, BEHAVIOR_GROUP_GREETING, fiveDaysAgo);
		for (History history : histories) {
			Element historyElement = document.createElement("history");
			documentElement.appendChild(historyElement);
			historyElement.setAttribute("date", DATE_TIME_FORMATTER.format(
				servant.toTaipeiZonedDateTime(
					history.getOccurred()
				).withZoneSameInstant(Servant.ASIA_TAIPEI)
			));

			Lover male = history.getPassive();

			historyElement.setAttribute("nickname", male.getNickname());
			historyElement.setAttribute("age", calculateAge(male).toString());
			historyElement.setAttribute("profileImage", male.getProfileImage());
			historyElement.setAttribute("identifier", male.getIdentifier().toString());
			if (isVVIP(history.getPassive())) {
				historyElement.setAttribute("vip", null);
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

		return document;
	}

	public boolean within12hrsFromLastGroupGreeting(Lover female) {
		Date gpDate = null;
		Date nowDate = null;
		History history = historyRepository.findTop1ByInitiativeAndBehaviorOrderByIdDesc(female, BEHAVIOR_GROUP_GREETING);
		if (Objects.nonNull(history)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(history.getOccurred());
			calendar.add(Calendar.HOUR, 12);
			gpDate = calendar.getTime();
			nowDate = new Date();
		}
		return nowDate.before(gpDate);
	}

	/**
	 * 是否已經完成填寫註冊個人資訊
	 *
	 * @param lover
	 * @return
	 */
	public boolean isEligible(Lover lover) {
		String login = lover.getLogin();
		if (Objects.isNull(login) || login.isBlank()) {
			return false;
		}//帐号(手机号)
		String shadow = lover.getShadow();
		if (Objects.isNull(shadow) || shadow.isBlank()) {
			return false;
		}//密码
		String nickname = lover.getNickname();
		if (Objects.isNull(nickname) || nickname.isBlank()) {
			return false;
		}//昵称
		String aboutMe = lover.getAboutMe();
		if (Objects.isNull(aboutMe) || aboutMe.isBlank()) {
			return false;
		}//自介
		String greeting = lover.getGreeting();
		if (Objects.isNull(greeting) || greeting.isBlank()) {
			return false;
		}//哈啰
		if (Objects.isNull(lover.getBodyType())) {
			return false;
		}//体型
		if (Objects.isNull(lover.getHeight())) {
			return false;
		}//身高
		if (Objects.isNull(lover.getWeight())) {
			return false;
		}//体重
		if (Objects.isNull(lover.getEducation())) {
			return false;
		}//学历
		if (Objects.isNull(lover.getMarriage())) {
			return false;
		}//婚姻
		String occupation = lover.getOccupation();
		if (Objects.isNull(occupation) || occupation.isBlank()) {
			return false;
		}//职业
		if (Objects.isNull(lover.getSmoking())) {
			return false;
		}//抽烟习惯
		if (Objects.isNull(lover.getDrinking())) {
			return false;
		}//饮酒习惯
		String idealConditions = lover.getIdealConditions();
		if (Objects.isNull(idealConditions) || idealConditions.isBlank()) {
			return false;
		}//简述理想对象条件
		if (Objects.nonNull(lover.getDelete())) {
			return false;
		}//封号
		if (Objects.isNull(lover.getRelationship())) {
			return false;
		}//预期关系
		if (lover.getGender()) {
			if (Objects.isNull(lover.getAnnualIncome())) {
				return false;
			}//年收入
		} else {
			String inviteMeAsFriend = lover.getInviteMeAsLineFriend();
			if (Objects.isNull(inviteMeAsFriend) || inviteMeAsFriend.isBlank()) {
				return false;
			}//添加好友链结
			if (Objects.isNull(lover.getAllowance())) {
				return false;
			}//期望零用钱
		}
		if (lover.getLocations().isEmpty() || lover.getServices().isEmpty()) {
			return false;
		}//(地点|服务)标签
		return true;
	}

	/**
	 * 封鎖
	 *
	 * @param initiative
	 * @param passive
	 * @param locale
	 * @return
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

		Collection<Lover> blocking = initiative.getBlocking();
		for (Lover blocked : blocking) {
			if (Objects.equals(passive, blocked)) {
				LOGGER.debug("測試{}", blocked);
				blocking.remove(blocked);
				initiative.setBlocking(blocking);
				loverRepository.saveAndFlush(initiative);
				return new JavaScriptObjectNotation().
					withReason(messageSource.getMessage(
						"unblock.done",
						null,
						locale
					)).
					withResponse(true).
					toJSONObject();
			}
		}

		blocking.add(passive);
		initiative.setBlocking(blocking);
		loverRepository.saveAndFlush(initiative);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"block.done",
				null,
				locale
			)).
			withResponse(true).
			toJSONObject();
	}
}
