package tw.musemodel.dingzhiqingren.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Activated;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.SignUp;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * 根控制器
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

	private final static Logger LOGGER = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	private LoverService loverService;

	@Autowired
	private Servant servant;

	@Autowired
	private LoverRepository loverRepository;

	/**
	 * 首页
	 *
	 * @param authentication 认证
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/")
	@ResponseBody
	ModelAndView index(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", "首頁");
		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"me",
				authentication.getName()
			);
		}

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 激活页面
	 *
	 * @param username 手机号含国码
	 * @param authentication 认证
	 * @param request 请求
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/activation.asp")
	ModelAndView activate(Authentication authentication, HttpServletRequest request) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			LOGGER.debug(
				"authentication 不是空值"
			);
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute(
			"path",
			request.getRequestURL().toString().replaceAll(
				"\\.asp$",
				".json"
			)
		);
		documentElement.setAttribute("title", "激活帳戶");

		ModelAndView modelAndView = new ModelAndView("activation");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 激活
	 *
	 * @param username 手机号含国码
	 * @param string 激活码
	 * @param authentication 认证
	 * @return 杰森字符串
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/activation.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String activate(@RequestParam String string, Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new JavaScriptObjectNotation().
				withReason("activation.cannotBeAuthenticated").
				withResponse(false).
				toString();
		}

		JSONObject jsonObject;
		try {
			jsonObject = loverService.activate(string);
		} catch (NoSuchElementException ignore) {
			jsonObject = new JavaScriptObjectNotation().
				withReason("activation.notFound").
				withResponse(false).
				toJSONObject();
		} catch (RuntimeException runtimeException) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(runtimeException.getMessage()).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 初始化密码
	 *
	 * @param authentication 认证
	 * @param request 请求
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/activated.asp")
	ModelAndView activated(@RequestParam UUID id, Authentication authentication, HttpServletRequest request) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			LOGGER.debug(
				String.format(
					"%s.activated(\n\tUUID = {},\n\tAuthentication = {}\n\tHttpServletRequest = {}\n);//登入状态下禁止初始化密码",
					getClass().getName()
				),
				id,
				authentication,
				request
			);
			return new ModelAndView("redirect:/");
		}

		Lover lover = loverService.loadByIdentifier(id);
		if (Objects.isNull(lover)) {
			LOGGER.debug(
				String.format(
					"%s.activated(\n\tUUID = {},\n\tAuthentication = {}\n\tHttpServletRequest = {}\n);//初始化密码时找不到情人",
					getClass().getName()
				),
				id,
				authentication,
				request
			);
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute(
			"path",
			request.getRequestURL().toString()
		);
		documentElement.setAttribute("title", "初始化密碼");

		Element identifierElement = document.createElement("identifier");
		identifierElement.setTextContent(
			lover.getIdentifier().toString()
		);
		documentElement.appendChild(identifierElement);

		ModelAndView modelAndView = new ModelAndView("activated");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 初始化密码
	 *
	 * @param activated 激活后初始化
	 * @param authentication 认证
	 * @param request 请求
	 * @return
	 */
	@PostMapping(path = "/activated.asp", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String activated(Activated activated, Authentication authentication, HttpServletRequest request) {
		if (!servant.isNull(authentication)) {
			LOGGER.debug(
				String.format(
					"%s.activated(\n\tActivated = {},\n\tAuthentication = {}\n\tHttpServletRequest = {}\n);//登入状态下禁止初始化密码",
					getClass().getName()
				),
				activated,
				authentication,
				request
			);
			return new JavaScriptObjectNotation().
				withReason("activated.cannotBeAuthenticated").
				withResponse(false).
				toString();
		}
		return loverService.activated(activated, request).toString();
	}

	/**
	 * @param authentication 认证
	 * @return 杰森
	 * @throws JsonProcessingException
	 */
	@GetMapping(path = "/authentication.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Authentication authentication(Authentication authentication) throws JsonProcessingException {
		return authentication;
	}

	/**
	 * 重新激活页面
	 *
	 * @param authentication 认证
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/reactivate.asp")
	ModelAndView reactivate(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", "重新激活");

		Element countriesElement = document.createElement("countries");
		servant.getCountries().stream().map(country -> {
			String callingCode = country.getCallingCode();
			Element optionElement = document.createElement("option");
			optionElement.setAttribute("value", callingCode);
			optionElement.setTextContent(
				String.format(
					"+%s (%s)",
					callingCode,
					country.getName()
				)
			);
			return optionElement;
		}).forEachOrdered(countryElement -> {
			countriesElement.appendChild(countryElement);
		});
		documentElement.appendChild(countriesElement);

		ModelAndView modelAndView = new ModelAndView("reactivate");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	@PostMapping(path = "/reactivate.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String reactivate(@RequestParam String username, Authentication authentication, HttpServletRequest request) {
		if (!servant.isNull(authentication)) {
			return new JavaScriptObjectNotation().
				withReason("reactivate.cannotBeAuthenticated").
				withResponse(false).
				toString();
		}

		JSONObject jsonObject;
		try {
			jsonObject = loverService.reactivate(username, request);
		} catch (NoSuchElementException ignore) {
			jsonObject = new JavaScriptObjectNotation().
				withReason("reactivate.notFound").
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 登入
	 *
	 * @param authentication 认证
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/signIn.asp")
	ModelAndView signIn(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", "登入");

		Element countriesElement = document.createElement("countries");
		servant.getCountries().stream().map(country -> {
			String callingCode = country.getCallingCode();
			Element optionElement = document.createElement("option");
			optionElement.setAttribute("value", callingCode);
			optionElement.setTextContent(
				String.format(
					"+%s (%s)",
					callingCode,
					country.getName()
				)
			);
			return optionElement;
		}).forEachOrdered(countryElement -> {
			countriesElement.appendChild(countryElement);
		});
		documentElement.appendChild(countriesElement);

		ModelAndView modelAndView = new ModelAndView("signIn");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 登出
	 *
	 * @param session
	 * @return 重定向
	 */
	@GetMapping(path = "/signOut.asp")
	ModelAndView signOut(HttpSession session) {
		session.invalidate();
		return servant.redirectToRoot();
	}

	/**
	 * 新建帐户页面
	 *
	 * @param authentication 认证
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/signUp.asp")
	ModelAndView signUp(Authentication authentication) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", "新建帳戶");

		Element countriesElement = document.createElement("countries");
		servant.getCountries().stream().map(country -> {
			Element optionElement = document.createElement("option");
			optionElement.setAttribute(
				"value",
				country.getId().toString()
			);
			optionElement.setTextContent(
				String.format(
					"+%s (%s)",
					country.getCallingCode(),
					country.getName()
				)
			);
			return optionElement;
		}).forEachOrdered(countryElement -> {
			countriesElement.appendChild(countryElement);
		});
		documentElement.appendChild(countriesElement);

		ModelAndView modelAndView = new ModelAndView("signUp");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 新建帐户
	 *
	 * @param signUp 模型
	 * @param authentication 认证
	 * @param request 请求
	 * @return 重定向
	 */
	@PostMapping(path = "/signUp.asp")
	@ResponseBody
	ModelAndView signUp(SignUp signUp, Authentication authentication, HttpServletRequest request) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		try {
			loverService.signUp(signUp, request);
		} catch (RuntimeException runtimeException) {
			Document document = servant.parseDocument();
			Element documentElement = document.getDocumentElement();
			documentElement.setAttribute("title", "新建帳戶");

			Element countriesElement = document.createElement("countries");
			servant.getCountries().stream().map(country -> {
				Short id = country.getId();
				Element optionElement = document.createElement("option");
				if (Objects.equals(id, signUp.getCountry())) {
					optionElement.setAttribute(
						"selected",
						null
					);
				}
				optionElement.setAttribute(
					"value",
					country.getId().toString()
				);
				optionElement.setTextContent(
					String.format(
						"+%s (%s)",
						country.getCallingCode(),
						country.getName()
					)
				);
				return optionElement;
			}).forEachOrdered(countryElement -> {
				countriesElement.appendChild(countryElement);
			});
			documentElement.appendChild(countriesElement);

			Element loginElement = document.createElement("login");
			loginElement.setAttribute(
				"reason",
				runtimeException.getMessage()
			);
			loginElement.setTextContent(signUp.getLogin());
			documentElement.appendChild(loginElement);

			ModelAndView modelAndView = new ModelAndView("signUp");
			modelAndView.getModelMap().addAttribute(document);
			return modelAndView;
		}
		return new ModelAndView("redirect:/activation.asp");
	}

	@GetMapping(path = "/{lover:\\d+}/cellular.json", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String cellular(@PathVariable Lover lover) throws JsonProcessingException {
		return String.format(
			"+%s%s",
			lover.getCountry().getCallingCode(),
			lover.getLogin()
		);
	}

	@GetMapping(path = "/lovers.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<Lover> lovers() {
		return loverRepository.findAll();
	}

	@GetMapping(path = "/shadow", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	@Secured({"ROLE_ANONYMOUS"})
	String shadow(@RequestParam String shadow) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(shadow);
	}

	@GetMapping(path = "/@{webhook}", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String webhook(@PathVariable String webhook) {
		return webhook.replaceAll("^@", "");
	}
}
