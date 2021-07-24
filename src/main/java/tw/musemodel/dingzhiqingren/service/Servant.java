package tw.musemodel.dingzhiqingren.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
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
	private MessageSource messageSource;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * 东八时区
	 */
	public static final ZoneId ASIA_TAIPEI = ZoneId.of("Asia/Taipei");

	public static final JsonMapper JSON_MAPPER = new JsonMapper();

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
	public static final String STATIC_HOST = System.getenv("STATIC_HOST");

	/**
	 * 中华民族日期时间格式化器
	 */
	public static final DateTimeFormatter TAIWAN_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");

	/**
	 * 语言环境：臺湾(繁体中文)
	 */
	public static final Locale TAIWAN = Locale.TAIWAN;

	/**
	 * 系统暂存目录
	 */
	public static final File TEMPORARY_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

	/**
	 * 协调世界时
	 */
	public static final ZoneId UTC = ZoneId.of("UTC");

	/**
	 * 一种针对 Unicode 的可变长度字符编码
	 */
	public static final Charset UTF_8 = StandardCharsets.UTF_8;

	/**
	 * 服务器时区
	 */
	public static final ZoneId ZONE_ID = ZoneId.of(System.getenv("ZONE_ID"));

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
		return new Date(ZonedDateTime.ofInstant(
			ZonedDateTime.of(
				LocalDate.ofInstant(
					new Date(timeMillis).toInstant(),
					ASIA_TAIPEI
				),
				LocalTime.MAX,
				ASIA_TAIPEI
			).toInstant(),
			UTC
		).toInstant().toEpochMilli());
	}

	/**
	 * 一天最早的时戳
	 *
	 * @param timeMillis 自 1970 年 1 月 1 日格林威治标准时间 00:00:00 以来的毫秒数
	 * @return java.util.Date
	 */
	public static Date minimumToday(long timeMillis) {
		return new Date(ZonedDateTime.ofInstant(
			ZonedDateTime.of(
				LocalDate.ofInstant(
					new Date(timeMillis).toInstant(),
					ASIA_TAIPEI
				),
				LocalTime.MIN,
				ASIA_TAIPEI
			).toInstant(),
			UTC
		).toInstant().toEpochMilli());
	}

	public List<Country> getCountries() {
		return countryRepository.findAll();
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

	public Document parseDocument() throws SAXException, IOException, ParserConfigurationException {
		return parseDocument(EMPTY_DOCUMENT_URI);
	}

	public Document parseDocument(String uri) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
	}

	/**
	 * 重定向到首页
	 *
	 * @return org.​springframework.​web.​servlet.ModelAndView
	 */
	public ModelAndView redirectToRoot() {
		return new ModelAndView("redirect:/");
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

	public ZonedDateTime toTaipeiZonedDateTime(Instant instant) {
		return ZonedDateTime.ofInstant(instant, ASIA_TAIPEI);
	}

	public ZonedDateTime toTaipeiZonedDateTime(Date date) {
		return Servant.this.toTaipeiZonedDateTime(date.toInstant());
	}

	/**
	 * 確認身分
	 *
	 * @param zhangHao
	 * @param roleName
	 * @return
	 */
	public boolean hasRole(Lover lover, String roleName) {
		return Objects.nonNull(lover) && lover.getRoles().contains(roleRepository.findOneByTextualRepresentation(roleName));
	}
}
