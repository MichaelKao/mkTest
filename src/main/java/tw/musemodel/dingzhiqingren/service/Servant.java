package tw.musemodel.dingzhiqingren.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.Country;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Role;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.CountryRepository;
import tw.musemodel.dingzhiqingren.repository.RoleRepository;

/**
 * 服务层：仆役
 *
 * @author p@musemodel.tw
 */
@Service
public class Servant {

	private static final Logger LOGGER = LoggerFactory.getLogger(Servant.class);

	private static final String EMPTY_DOCUMENT_URI = "classpath:/skeleton/default.xml";

	@Autowired
	private Environment environment;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LoverService loverService;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * 东八时区
	 */
	public static final ZoneId ASIA_TAIPEI_ZONE_ID = ZoneId.of("Asia/Taipei");

	/**
	 * 东八时区
	 */
	public static final TimeZone ASIA_TAIPEI_TIME_ZONE = TimeZone.getTimeZone(ASIA_TAIPEI_ZONE_ID);

	/**
	 * 一个月有(算)几天
	 */
	public static final Integer DAYS_IN_A_MONTH = 30;

	/**
	 * yyyy-MM-dd 的 DateTimeFormatter
	 */
	public static final DateTimeFormatter DATE_TIME_FORMATTER_yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * Facebook 像素 ID
	 */
	public static final String FACEBOOK_PIXEL_ID = System.getenv("FACEBOOK_PIXEL_ID");

	/**
	 * JSON 映射器
	 */
	public static final JsonMapper JSON_MAPPER = new JsonMapper();

	/**
	 * 美观的 JSON 映射器
	 */
	public static final ObjectWriter JSON_WRITER_WITH_DEFAULT_PRETTY_PRINTER = new JsonMapper().writerWithDefaultPrettyPrinter();

	/**
	 * 透过 LINE 接收其它网站服务通知
	 */
	public static final String LINE_NOTIFY_ACCESS_TOKEN = System.getenv("LINE_NOTIFY_ACCESS_TOKEN");

	/**
	 * 本地服务器域名
	 */
	public static final String LOCALHOST = System.getenv("LOCALHOST");

	/**
	 * Google Analytics 评估 ID
	 */
	public static final String MEASUREMENT_ID = System.getenv("MEASUREMENT_ID");

	/**
	 * 1 天的毫秒数
	 */
	public static final Integer MILLISECONDS_IN_A_DAY = 86400000;

	/**
	 * 30 天的毫秒数
	 */
	public static final Long MILLISECONDS_OF_30_DAYS = 2592000000L;

	/**
	 * 涂鸦墙各区块每夜几笔
	 */
	public static final Integer PAGE_SIZE_ON_THE_WALL = Integer.valueOf(System.getenv("PAGE_SIZE_ON_THE_WALL"));

	/**
	 * 万能天神
	 */
	public static final String ROLE_ADMINISTRATOR = "ROLE_ALMIGHTY";

	/**
	 * 财务会计
	 */
	public static final String ROLE_ACCOUNTANT = "ROLE_FINANCE";

	/**
	 * 一般用户
	 */
	public static final String ROLE_ADVENTURER = "ROLE_YONGHU";

	/**
	 * 静态资源网域
	 */
	public static final String SCHEME_HTTPS = "https";

	/**
	 * yyyy-MM-dd 的 SimpleDateFormat
	 */
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 静态资源网域
	 */
	public static final String STATIC_HOST = System.getenv("STATIC_HOST");

	/**
	 * 语言环境：臺湾(繁体中文)
	 */
	public static final Locale TAIWAN = Locale.TAIWAN;

	/**
	 * 中华民族日期时间格式化器
	 */
	public static final DateTimeFormatter TAIWAN_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");

	/**
	 * 中华民族日期时间格式
	 */
	public static final SimpleDateFormat TAIWAN_SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");

	/**
	 * 系统暂存目录
	 */
	public static final File TEMPORARY_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

	/**
	 * 协调世界时
	 */
	public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

	/**
	 * 协调世界时
	 */
	public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone(UTC_ZONE_ID);

	/**
	 * 一种针对 Unicode 的可变长度字符编码
	 */
	public static final Charset UTF_8 = StandardCharsets.UTF_8;

	/**
	 * 服务器时区
	 */
	public static final ZoneId ZONE_ID = ZoneId.of(System.getenv("ZONE_ID"));

	/**
	 * 一天最早的时戳。
	 *
	 * @param year 年
	 * @param month 月
	 * @param dayOfMonth 日
	 * @return 日期时间时戳
	 */
	public static final Date earliestDate(int year, int month, int dayOfMonth) {
		Calendar calendar = new GregorianCalendar(Servant.ASIA_TAIPEI_TIME_ZONE);
		calendar.set(year, month - 1, dayOfMonth, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 十六进制转换为十进制。
	 *
	 * @param hexadecimal 十六进制数的字符串
	 * @return 十进制数
	 */
	public static final Integer hexadecimalToDecimal(String hexadecimal) {
		return Integer.parseUnsignedInt(hexadecimal, 16);
	}

	/**
	 * 这货是 Line 的邀请链结吗？
	 *
	 * @param uri 链接网址
	 * @return 是不是(真伪布林值)
	 */
	public static final boolean isLine(URI uri) {
		String scheme = uri.getScheme();
		if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
			return false;
		}

		String host = uri.getHost();
		if (host.equalsIgnoreCase("line.me") || host.equalsIgnoreCase("line.naver.jp") || host.equalsIgnoreCase("lin.ee")) {
			return uri.getPath().matches("^/ti/[gp]/\\S{10}$");
		}

		return false;
	}

	/**
	 * 这货是微信的邀请链结吗？
	 *
	 * @param uri 链接网址
	 * @return 是不是(真伪布林值)
	 */
	public static final boolean isWeChat(URI uri) {
		String scheme = uri.getScheme();
		if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
			return false;
		}

		String host = uri.getHost();
		if (host.equalsIgnoreCase("u.wechat.com")) {
			return uri.getPath().matches("^/\\S{23}$");
		}

		return false;
	}

	/**
	 * 一天最晚的时戳。
	 *
	 * @param year 年
	 * @param month 月
	 * @param dayOfMonth 日
	 * @return 日期时间时戳
	 */
	public static final Date latestDate(int year, int month, int dayOfMonth) {
		Calendar calendar = new GregorianCalendar(Servant.ASIA_TAIPEI_TIME_ZONE);
		calendar.setTime(earliestDate(year, month, dayOfMonth));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}

	/**
	 * 透过 LINE 接收其它网站服务通知。
	 *
	 * @param message 最多 1000 个字符
	 * @return
	 */
	public static String lineNotify(final String message) {
		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(
				new URIBuilder("https://notify-api.line.me/api/notify").
					setParameters(
						new BasicNameValuePair(
							"message",
							message
						),
						new BasicNameValuePair(
							"notificationDisabled",
							"true"
						)
					).
					build()
			);
			httpPost.setHeader(
				"Content-Type",
				"application/x-www-form-urlencoded"
			);
			httpPost.setHeader(
				"Authorization",
				String.format(
					"Bearer %s",
					LINE_NOTIFY_ACCESS_TOKEN
				)
			);
			closeableHttpClient.execute(httpPost);
			closeableHttpClient.close();
		} catch (URISyntaxException | IOException exception) {
			LOGGER.info(
				String.format(
					"%s.notify(\n\t%s = {});",
					Servant.class,
					String.class
				),
				message,
				exception
			);
		}
		return message;
	}

	/**
	 * 一天最晚的时戳
	 *
	 * @param timeMillis 自 1970 年 1 月 1 日格林威治标准时间 00:00:00 以来的毫秒数
	 * @return java.util.Date
	 */
	public static Date maximumToday(long timeMillis) {
		return new Date(ZonedDateTime.ofInstant(ZonedDateTime.of(LocalDate.ofInstant(new Date(timeMillis).toInstant(),
			ASIA_TAIPEI_ZONE_ID
		),
			LocalTime.MAX,
			ASIA_TAIPEI_ZONE_ID
		).toInstant(),
			UTC_ZONE_ID
		).toInstant().toEpochMilli());
	}

	/**
	 * 一天最早的时戳
	 *
	 * @param timeMillis 自 1970 年 1 月 1 日格林威治标准时间 00:00:00 以来的毫秒数
	 * @return java.util.Date
	 */
	public static Date minimumToday(long timeMillis) {
		return new Date(ZonedDateTime.ofInstant(ZonedDateTime.of(LocalDate.ofInstant(new Date(timeMillis).toInstant(),
			ASIA_TAIPEI_ZONE_ID
		),
			LocalTime.MIN,
			ASIA_TAIPEI_ZONE_ID
		).toInstant(),
			UTC_ZONE_ID
		).toInstant().toEpochMilli());
	}

	/**
	 * 解析为 DOM Document。
	 *
	 * @author p@musemodel.tw
	 * @return org.w3.dom.Docment
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Document parseDocument() throws SAXException, IOException, ParserConfigurationException {
		Document document = parseDocument(EMPTY_DOCUMENT_URI);
		Element documentElement = document.getDocumentElement();

		Element seoElement = document.createElement("seo");
		documentElement.appendChild(seoElement);

		Element facebookPixelElement = document.createElement("facebookPixel");
		facebookPixelElement.setAttribute("id", FACEBOOK_PIXEL_ID);
		facebookPixelElement.appendChild(document.createCDATASection(
			String.format(
				new BufferedReader(new InputStreamReader(
					new ClassPathResource("skeleton/facebookPixel.js").
						getInputStream(),
					UTF_8
				)).lines().collect(Collectors.joining("\n")),
				FACEBOOK_PIXEL_ID
			)
		));
		seoElement.appendChild(facebookPixelElement);

		Element googleAnalyticsElement = document.createElement("googleAnalytics");
		googleAnalyticsElement.setAttribute("id", MEASUREMENT_ID);
		googleAnalyticsElement.appendChild(document.createCDATASection(
			String.format(
				new BufferedReader(new InputStreamReader(
					new ClassPathResource("skeleton/googleAnalytics.js").
						getInputStream(),
					UTF_8
				)).lines().collect(Collectors.joining("\n")),
				MEASUREMENT_ID
			)
		));
		seoElement.appendChild(googleAnalyticsElement);

		return document;
	}

	/**
	 * 解析为 DOM Document。
	 *
	 * @author p@musemodel.tw
	 * @param uri 来源
	 * @return org.w3.dom.Docment
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Document parseDocument(String uri) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
	}

	/**
	 * 重定向到编辑个人资料页面。
	 *
	 * @return org.​springframework.​web.​servlet.ModelAndView
	 */
	public static ModelAndView redirectToProfile() {
		return new ModelAndView("redirect:/me.asp");
	}

	/**
	 * 重定向到首页。
	 *
	 * @return org.​springframework.​web.​servlet.ModelAndView
	 */
	public static ModelAndView redirectToRoot() {
		return new ModelAndView("redirect:/");
	}

	/**
	 * 转换为台湾时区。
	 *
	 * @param date
	 * @return
	 */
	public static ZonedDateTime toTaipeiZonedDateTime(Date date) {
		return toTaipeiZonedDateTime(
			Instant.ofEpochMilli(date.getTime())
		);
	}

	/**
	 * 转换为台湾时区。
	 *
	 * @param instant
	 * @return
	 */
	public static ZonedDateTime toTaipeiZonedDateTime(Instant instant) {
		return ZonedDateTime.ofInstant(instant, ASIA_TAIPEI_ZONE_ID);
	}

	/**
	 * 转换为世界标准时间。
	 *
	 * @param date
	 * @return
	 */
	public static ZonedDateTime toUTC(Date date) {
		return toUTC(date.toInstant());
	}

	/**
	 * 转换为世界标准时间。
	 *
	 * @param instant
	 * @return
	 */
	public static ZonedDateTime toUTC(Instant instant) {
		return ZonedDateTime.ofInstant(instant, UTC_ZONE_ID);
	}

	/**
	 * 构建根元素。
	 *
	 * @param document 文件
	 * @param authentication 认证
	 * @return 根元素
	 */
	@Transactional(readOnly = true)
	public Element documentElement(Document document, Authentication authentication) {
		String login = authentication.getName();

		Lover mofo = loverService.loadByUsername(login);

		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute(
			"signIn",
			login
		);//帐号(国码➕手机号)
		documentElement.setAttribute(
			"identifier",
			mofo.getIdentifier().toString()
		);//识别码
		documentElement.setAttribute(
			mofo.getGender() ? "male" : "female",
			null
		);//性别

		/*
		 身份
		 */
		if (loverService.hasRole(mofo, Servant.ROLE_ADMINISTRATOR)) {
			//万能天神
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (loverService.hasRole(mofo, Servant.ROLE_ACCOUNTANT)) {
			//财务会计
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		Integer numberOfAnnoucements = loverService.annoucementCount(mofo);
		if (numberOfAnnoucements > 0) {
			documentElement.setAttribute(
				"announcement",
				numberOfAnnoucements.toString()
			);//通知数
		}

		Integer numberOfUnreadMessages = loverService.unreadMessages(mofo);
		if (numberOfUnreadMessages > 0) {
			documentElement.setAttribute(
				"inbox",
				numberOfUnreadMessages.toString()
			);//未读讯息数
		}

		if (loverService.hasLineNotify(mofo)) {
			documentElement.setAttribute(
				"lineNotify",
				null
			);//有无连动 LINE Notify
		}

		return documentElement;
	}

	public String[] getActiveProfiles() {
		return environment.getActiveProfiles();
	}

	public List<Country> getCountries() {
		return countryRepository.findAll();
	}

	public boolean isDevelopment() {
		for (String profile : getActiveProfiles()) {
			if ("DEVELOPMENT".equalsIgnoreCase(profile)) {
				return true;
			}
		}
		return false;
	}

	public boolean isTesting() {
		for (String profile : getActiveProfiles()) {
			if ("TESTING".equalsIgnoreCase(profile)) {
				return true;
			}
		}
		return false;
	}

	public boolean isNull(Authentication authentication) {
		return Objects.isNull(authentication);
	}

	public boolean isNull(UUID uuid) {
		return Objects.isNull(uuid);
	}

	public Role getRole(String textualRepresentation) {
		return roleRepository.findOneByTextualRepresentation(
			textualRepresentation
		);
	}

	public Document newDocument() throws ParserConfigurationException {
		return DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().newDocument();
	}

	/**
	 * 将 markdown 转为 HTML
	 *
	 * @param markdown markdown 语句
	 * @return HTML 语句
	 */
	public String markdownToHtml(String markdown) {
		return HtmlRenderer.builder().softbreak("<br/>").build().render(
			Parser.builder().build().parse(
				markdown
			)
		);
	}

	/**
	 * 须登入
	 *
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	public String mustBeAuthenticated(Locale locale) {
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"mustBeAuthenticated",
				null,
				locale
			)).
			withResponse(false).
			toString();
	}
}
