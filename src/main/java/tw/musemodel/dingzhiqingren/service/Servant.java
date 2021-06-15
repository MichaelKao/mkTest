package tw.musemodel.dingzhiqingren.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.Country;
import tw.musemodel.dingzhiqingren.entity.Role;
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
	private CountryRepository countryRepository;

	@Autowired
	private RoleRepository roleRepository;

	public final static Charset UTF_8 = StandardCharsets.UTF_8;

	public final static String ROLE_ADMINISTRATOR = "ROLE_ALMIGHTY";

	public final static String ROLE_ACCOUNTANT = "ROLE_FINANCE";

	public final static String ROLE_ADVENTURER = "ROLE_YONGHU";

	public boolean isNull(Authentication authentication) {
		return Objects.isNull(authentication);
	}

	public boolean isNull(UUID uuid) {
		return Objects.isNull(uuid);
	}

	public List<Country> getCountries() {
		return countryRepository.findAll();
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
}
