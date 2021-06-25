package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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

	private final static Logger LOGGER = LoggerFactory.getLogger(Servant.class);

	private final static String EMPTY_DOCUMENT_URI = "classpath:/skeleton/default.xml";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private RoleRepository roleRepository;

	public final static String LOCALHOST = System.getenv("LOCALHOST");

	public final static String ROLE_ADMINISTRATOR = "ROLE_ALMIGHTY";

	public final static String ROLE_ACCOUNTANT = "ROLE_FINANCE";

	public final static String ROLE_ADVENTURER = "ROLE_YONGHU";

	public final static Charset UTF_8 = StandardCharsets.UTF_8;

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
		return roleRepository.findOneByTextualRepresentation(textualRepresentation);
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

	public ModelAndView redirectToRoot() {
		return new ModelAndView("redirect:/");
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

	/**
	 * 中华民族日期时间格式化器
	 */
	public static final DateTimeFormatter ZHONG_HUA_MIN_ZU = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");

	public ZonedDateTime toTaipeiZonedDateTime(Instant instant) {
		return ZonedDateTime.ofInstant(instant, ZONE_ID_TAIPEI);
	}

	public ZonedDateTime toTaipeiZonedDateTime(Date date) {
		return Servant.this.toTaipeiZonedDateTime(date.toInstant());
	}

	/**
	 * 东八区
	 */
	public static final ZoneId ZONE_ID_TAIPEI = ZoneId.of("Asia/Taipei");
}
