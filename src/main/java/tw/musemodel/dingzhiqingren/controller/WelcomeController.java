package tw.musemodel.dingzhiqingren.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Picture;
import tw.musemodel.dingzhiqingren.entity.Plan;
import tw.musemodel.dingzhiqingren.model.Activated;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.SignUp;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.PictureRepository;
import tw.musemodel.dingzhiqingren.repository.PlanRepository;
import tw.musemodel.dingzhiqingren.service.AmazonWebServices;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * 控制器：根
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

	private final static Logger LOGGER = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AmazonWebServices amazonWebServices;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private Servant servant;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private PictureRepository pictureRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private LineGivenRepository lineGivenRepository;

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
	ModelAndView index(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.home",
			null,
			locale
		));
		// 登入狀態
		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
			// 本人
			Lover me = loverService.loadByUsername(
				authentication.getName()
			);

			// 確認性別
			Boolean isMale = loverService.isMale(me);
			// 通知數、顯示的 lovers 資料
			int announcement = 0;
			List<Lover> lovers = new ArrayList<Lover>();
			if (isMale) {
				lovers = loverRepository.findAllByGender(false);
				historyRepository.countByPassive(me, Behavior.KAN_GUO_WO, Behavior.LAI_TUI_DIAN);
			}
			if (!isMale) {
				lovers = loverRepository.findAllByGender(true);
				historyRepository.countByFemalePassive(me, Behavior.KAN_GUO_WO, Behavior.LAI_TUI_DIAN);
			}

			if (announcement > 0) {
				documentElement.setAttribute(
					"announcement",
					Integer.toString(announcement)
				);
			}

			for (Lover lover : lovers) {
				Element loverElement = document.createElement("lover");
				documentElement.appendChild(loverElement);

				if (Objects.nonNull(lover.getNickname())) {
					Element nicknameElement = document.createElement("nickname");
					nicknameElement.setTextContent(lover.getNickname());
					loverElement.appendChild(nicknameElement);
				}

				if (Objects.nonNull(lover.getBirthday())) {
					Element ageElement = document.createElement("age");
					ageElement.setTextContent(loverService.calculateAge(lover).toString());
					loverElement.appendChild(ageElement);
				}

				Element identifierElement = document.createElement("identifier");
				identifierElement.setTextContent(lover.getIdentifier().toString());
				loverElement.appendChild(identifierElement);

				Element profileImageElement = document.createElement("profileImage");
				profileImageElement.setTextContent("https://www.youngme.vip/profileImage/" + lover.getProfileImage());
				loverElement.appendChild(profileImageElement);
			}
		}

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.
			getModelMap().
			addAttribute(document).
			addAttribute(messageSource);
		LOGGER.debug("首頁\n\n{}", modelAndView);
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
	@GetMapping(path = "/activate.asp")
	ModelAndView activate(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			LOGGER.debug("已登入故激活页面重导至首页");
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.activate",
			null,
			locale
		));

		Element formElement = document.createElement("form");
		formElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"activate.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(formElement);

		ModelAndView modelAndView = new ModelAndView("activate");
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
	@PostMapping(path = "/activate.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String activate(@RequestParam String string, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"activate.mustntBeAuthenticated",
					null,
					locale
				)).
				withResponse(false).
				toString();
		}

		JSONObject jsonObject;
		try {
			jsonObject = loverService.activate(string, locale);
		} catch (NoSuchElementException ignore) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"activate.notFound",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		} catch (RuntimeException runtimeException) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					runtimeException.getMessage(),
					null,
					locale
				)).
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
	ModelAndView activated(@RequestParam UUID id, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			LOGGER.debug("登入状态下无法初始化密码");
			return new ModelAndView("redirect:/");
		}

		Lover lover = loverService.loadByIdentifier(id);
		if (Objects.isNull(lover)) {
			LOGGER.debug("初始化密码时找不到情人");
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.activated",
			null,
			locale
		));

		Element formElement = document.createElement("form");
		formElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"activated.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(formElement);

		Element identifierElement = document.createElement("identifier");
		identifierElement.setTextContent(
			lover.getIdentifier().toString()
		);
		formElement.appendChild(identifierElement);

		Element shadowElement = document.createElement("shadow");
		shadowElement.setAttribute("i18n", messageSource.getMessage(
			"activated.form.shadow",
			null,
			locale
		));
		formElement.appendChild(shadowElement);

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
	String activated(Activated activated, Authentication authentication, HttpServletRequest request, Locale locale) {
		if (!servant.isNull(authentication)) {
			LOGGER.debug("登入状态下禁止初始化密码");
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"activated.mustntBeAuthenticated",
					null,
					locale
				)).
				withResponse(false).
				toString();
		}
		return loverService.activated(activated, request, locale).toString();
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
	ModelAndView reactivate(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument(
			"classpath:/skeleton/reactivate.xml"
		);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.reactivate",
			null,
			locale
		));

		Element formElement = document.createElement("form");
		formElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"reactivate.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(formElement);

		Element countryElement = document.createElement("country");
		servant.getCountries().stream().map(country -> {
			String callingCode = country.getCallingCode();
			Element optionElement = document.createElement("option");
			optionElement.setAttribute("value", callingCode);
			optionElement.setTextContent(
				String.format(
					"+%s (%s)",
					callingCode,
					messageSource.getMessage(
						String.format(
							"country.%s",
							country.getName()
						),
						null,
						locale
					)
				)
			);
			return optionElement;
		}).forEachOrdered(optionElement -> {
			countryElement.appendChild(optionElement);
		});
		formElement.appendChild(countryElement);

		ModelAndView modelAndView = new ModelAndView("reactivate");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	@PostMapping(path = "/reactivate.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String reactivate(@RequestParam String username, Authentication authentication, HttpServletRequest request, Locale locale) {
		if (!servant.isNull(authentication)) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"reactivate.mustntBeAuthenticated",
					null,
					locale
				)).
				withResponse(false).
				toString();
		}

		JSONObject jsonObject;
		try {
			jsonObject = loverService.reactivate(username, request, locale);
		} catch (NoSuchElementException ignore) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"reactivate.notFound",
					null,
					locale
				)).
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
	ModelAndView signIn(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument(
			"classpath:/skeleton/signIn.xml"
		);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.signIn",
			null,
			locale
		));

		Element formElement = document.createElement("form");
		formElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"signIn.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(formElement);

		Element countryElement = document.createElement("country");
		servant.getCountries().stream().map(country -> {
			String callingCode = country.getCallingCode();
			Element optionElement = document.createElement("option");
			optionElement.setAttribute("value", callingCode);
			optionElement.setTextContent(
				String.format(
					"+%s (%s)",
					callingCode,
					messageSource.getMessage(
						String.format(
							"country.%s",
							country.getName()
						),
						null,
						locale
					)
				)
			);
			return optionElement;
		}).forEachOrdered(optionElement -> {
			countryElement.appendChild(optionElement);
		});
		formElement.appendChild(countryElement);

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
	ModelAndView signUp(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.signUp",
			null,
			locale
		));

		Element formElement = document.createElement("form");
		formElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"signUp.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(formElement);

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
					messageSource.getMessage(
						String.format(
							"country.%s",
							country.getName()
						),
						null,
						locale
					)
				)
			);
			return optionElement;
		}).forEachOrdered(countryElement -> {
			countriesElement.appendChild(countryElement);
		});
		formElement.appendChild(countriesElement);

		Element genderElement = document.createElement("gender");
		genderElement.setAttribute(
			"female",
			messageSource.getMessage(
				"gender.female",
				null,
				locale
			)
		);
		genderElement.setAttribute(
			"male",
			messageSource.getMessage(
				"gender.male",
				null,
				locale
			)
		);
		formElement.appendChild(genderElement);

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
	String signUp(SignUp signUp, Authentication authentication, HttpServletRequest request, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"signUp.mustntBeAuthenticated",
					null,
					locale
				)).
				withResponse(false).
				toString();
		}

		if (Objects.isNull(signUp.getGender())) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"signUp.mustSetGender",
					null,
					locale
				)).
				withResponse(false).
				toString();
		}

		JSONObject jsonObject;
		try {
			jsonObject = loverService.signUp(signUp, request, locale);
		} catch (RuntimeException runtimeException) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					runtimeException.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
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

	/**
	 * 看自己的個人檔案
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/profile/")
	@Secured({"ROLE_YONGHU"})
	ModelAndView self(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Document document = loverService.readDocument(me, locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.profile",
			null,
			locale
		));

		// 有登入狀態
		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"me",
			null
		);

		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 看某人(也可能是自己)的個人檔案
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/profile/{identifier}/")
	@Secured({"ROLE_YONGHU"})
	ModelAndView profile(@PathVariable UUID identifier, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 確認性別
		Boolean isMale = loverService.isMale(me);

		// 識別碼的帳號
		Lover lover = loverService.loadByIdentifier(identifier);

		Document document = loverService.readDocument(lover, locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.profile",
			null,
			locale
		));

		// 有登入狀態
		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			isMale ? "male" : "female",
			null
		);

		// 是否已交換過 LINE
		LineGiven lineGiven = lineGivenRepository.findByFemaleAndMale(lover, me);
		if (Objects.nonNull(lineGiven)) {
			if (Objects.nonNull(lineGiven.getResponse())) {
				documentElement.setAttribute(
					lineGiven.getResponse() ? "match" : "noMatch",
					lover.getInviteMeAsLineFriend()
				);
			}
		}

		// 是否收藏
		Set<Lover> following = me.getFollowing();
		if (Objects.nonNull(following)) {
			Element followElement = document.createElement("follow");
			for (Lover followed : following) {
				if (Objects.equals(followed, lover)) {
					documentElement.appendChild(followElement);
					break;
				}
			}
		}

		// 此頁是否為本人
		if (Objects.equals(me, lover)) {
			documentElement.setAttribute(
				"me",
				null
			);
		}

		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 顯示自己的編輯頁面
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/me.asp")
	@Secured({"ROLE_YONGHU"})
	ModelAndView editPage(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 確認性別
		Boolean isMale = loverService.isMale(me);

		Document document = loverService.writeDocument(me, locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.editProfile",
			null,
			locale
		));

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			isMale ? "male" : "female",
			null
		);

		ModelAndView modelAndView = new ModelAndView("editProfile");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 修改自己的個人檔案
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/me.asp")
	@Secured({"ROLE_YONGHU"})
	@ResponseBody
	String editProfile(Lover model, @RequestParam(name = "birth", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday, Authentication authentication, Locale locale) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		if (!Objects.isNull(model.getNickname())) {
			me.setNickname(model.getNickname());
		}

		if (!Objects.isNull(birthday)) {
			ZonedDateTime birth = ZonedDateTime.of(LocalDate.ofInstant(birthday.toInstant(),
				Servant.ASIA_TAIPEI
			),
				LocalTime.MIN,
				Servant.ASIA_TAIPEI
			);
			birthday.setTime(birth.toEpochSecond() * 1000);
			me.setBirthday(birthday);
		}

		if (!Objects.isNull(model.getHeight())) {
			me.setHeight(model.getHeight());
		}

		if (!Objects.isNull(model.getWeight())) {
			me.setWeight(model.getWeight());
		}

		if (!Objects.isNull(model.getOccupation())) {
			me.setOccupation(model.getOccupation());
		}

		if (!Objects.isNull(model.getInviteMeAsLineFriend())) {
			me.setInviteMeAsLineFriend(model.getInviteMeAsLineFriend());
		}

		if (!Objects.isNull(model.getBodyType())) {
			me.setBodyType(model.getBodyType());
		}

		if (!Objects.isNull(model.getEducation())) {
			me.setEducation(model.getEducation());
		}

		if (!Objects.isNull(model.getMarriage())) {
			me.setMarriage(model.getMarriage());
		}

		if (!Objects.isNull(model.getSmoking())) {
			me.setSmoking(model.getSmoking());
		}

		if (!Objects.isNull(model.getDrinking())) {
			me.setDrinking(model.getDrinking());
		}

		if (!Objects.isNull(model.getAboutMe())) {
			String aboutMe = model.getAboutMe();
			me.setAboutMe(aboutMe);
		}

		if (!Objects.isNull(model.getIdealConditions())) {
			String idealConditions = model.getIdealConditions();
			me.setIdealConditions(idealConditions);
		}

		if (!Objects.isNull(model.getGreeting())) {
			me.setGreeting(model.getGreeting());
		}

		loverRepository.saveAndFlush(me);

		return new JavaScriptObjectNotation().
			withReason("Update successfully").
			withResponse(true).
			withRedirect("/profile/").
			toJSONObject().toString();
	}

	/**
	 * 我的收藏
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/favorite.asp")
	@Secured({"ROLE_YONGHU"})
	ModelAndView favorite(Authentication authentication, Locale locale)
		throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.favorite",
			null,
			locale
		));

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		Set<Lover> following = me.getFollowing();
		for (Lover followed : following) {

			Element followElement = document.createElement("follow");
			documentElement.appendChild(followElement);
			followElement.setAttribute(
				"identifier",
				followed.getIdentifier().toString()
			);
			followElement.setAttribute(
				"profileImage",
				"https://www.youngme.vip/profileImage/" + followed.getProfileImage()
			);
			if (Objects.nonNull(followed.getNickname())) {
				followElement.setAttribute(
					"nickname",
					followed.getNickname()
				);
			}
			if (Objects.nonNull(followed.getBirthday())) {
				followElement.setAttribute(
					"age",
					loverService.calculateAge(followed).toString()
				);
			}
		}

		ModelAndView modelAndView = new ModelAndView("favorite");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 收藏
	 *
	 * @param identifier
	 * @param authentication
	 * @return
	 */
	@PostMapping(path = "/favorite.json")
	@Secured({"ROLE_YONGHU"})
	@ResponseBody
	String favorite(@RequestParam UUID identifier, Authentication authentication) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 識別碼的帳號
		Lover lover = loverService.loadByIdentifier(identifier);

		Set<Lover> following = me.getFollowing();
		for (Lover followed : following) {
			if (Objects.equals(lover, followed)) {
				following.remove(followed);
				me.setFollowing(following);
				loverRepository.saveAndFlush(me);
				History history = new History(
					me,
					lover,
					Behavior.BU_SHOU_CANG
				);
				historyRepository.saveAndFlush(history);
				return new JavaScriptObjectNotation().
					withReason("已取消收藏" + followed.getNickname()).
					withResponse(true).
					toJSONObject().toString();
			}
		}

		following.add(lover);
		me.setFollowing(following);
		loverRepository.saveAndFlush(me);
		History history = new History(
			me,
			lover,
			Behavior.SHOU_CANG
		);
		historyRepository.saveAndFlush(history);
		return new JavaScriptObjectNotation().
			withReason("已收藏" + lover.getNickname()).
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 誰看過我
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/looksMe.asp")
	@Secured({"ROLE_YONGHU"})
	ModelAndView
		whoLooksMe(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.looksMe",
			null,
			locale
		));

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		List<History> histories = historyRepository.findByPassiveAndBehavior(
			me,
			Behavior.KAN_GUO_WO
		);

		Set<Lover> peekers = new HashSet<Lover>();
		for (History history : histories) {
			if (!peekers.contains(history.getInitiative())) {
				peekers.add(history.getInitiative());
				Lover peeker = history.getInitiative();
				// 到訪次數
				Long times = historyRepository.countByInitiativeAndPassiveAndBehavior(
					peeker, me, Behavior.KAN_GUO_WO
				);
				Element peekerElement = document.createElement("peeker");
				documentElement.appendChild(peekerElement);
				peekerElement.setAttribute(
					"times",
					times.toString()
				);
				peekerElement.setAttribute(
					"identifier",
					peeker.getIdentifier().toString()
				);
				peekerElement.setAttribute(
					"profileImage",
					"https://www.youngme.vip/profileImage/" + peeker.getProfileImage()
				);
				if (Objects.nonNull(peeker.getNickname())) {
					peekerElement.setAttribute(
						"nickname",
						peeker.getNickname()
					);
				}
				if (Objects.nonNull(peeker.getBirthday())) {
					peekerElement.setAttribute(
						"age",
						loverService.calculateAge(peeker).toString()
					);
				}
			}
		}

		ModelAndView modelAndView = new ModelAndView("looksMe");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 相片管理頁面
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/album.asp")
	@Secured({"ROLE_YONGHU"})
	ModelAndView album(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.album",
			null,
			locale
		));

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Element profileImageElement = document.createElement("profileImage");
		if (Objects.nonNull(me.getProfileImage())) {
			profileImageElement.setTextContent(
				"http://www.youngme.vip/profileImage/" + me.getProfileImage()
			);
		}
		documentElement.appendChild(profileImageElement);

		List<Picture> pictures = pictureRepository.findByLover(me);
		for (Picture picture : pictures) {
			String identifier = picture.getIdentifier().toString();
			Element pictureElement = document.createElement("picture");
			pictureElement.setAttribute("picIdentifier", identifier);
			pictureElement.setTextContent(
				"http://www.youngme.vip/pictures/" + identifier
			);
			documentElement.appendChild(pictureElement);
		}

		ModelAndView modelAndView = new ModelAndView("album");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 上傳大頭照
	 *
	 * @param authentication
	 * @param locale
	 * @param file
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/uploadProfileImage")
	@Secured({"ROLE_YONGHU"})
	@ResponseBody
	String uploadProfileImage(@RequestParam("file") MultipartFile multipartFile, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		amazonWebServices.deletePhotoFromS3Bucket(
			"/profileImage", me.getProfileImage()
		);

		String fileName = UUID.randomUUID().toString();
		amazonWebServices.uploadPhotoToS3Bucket(
			multipartFile, fileName, "/profileImage"
		);
		me.setProfileImage(fileName);
		loverRepository.saveAndFlush(me);

		return new JavaScriptObjectNotation().
			withReason("Upload successfully.").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 上傳照片
	 *
	 * @param authentication
	 * @param locale
	 * @param multipartFile
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/uploadPicture")
	@Secured({"ROLE_YONGHU"})
	@ResponseBody
	String uploadPicture(@RequestParam("file") MultipartFile multipartFile, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		UUID fileName = UUID.randomUUID();

		amazonWebServices.uploadPhotoToS3Bucket(
			multipartFile,
			fileName.toString(),
			"/pictures"
		);

		Picture picture = new Picture();
		picture.setLover(me);
		picture.setIdentifier(fileName);
		picture.setOccurred(new Date(System.currentTimeMillis()));
		picture = pictureRepository.saveAndFlush(picture);

		return new JavaScriptObjectNotation().
			withReason("Upload successfully.").
			withResponse(true).
			withResult(picture.toString()).
			toJSONObject().toString();
	}

	/**
	 * 刪除照片
	 *
	 * @param authentication
	 * @param locale
	 * @param index
	 * @return
	 */
	@PostMapping(value = "/deletePicture")
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String deletePicture(@RequestParam UUID identifier, Authentication authentication, Locale locale) {
		pictureRepository.deleteById(
			pictureRepository.findOneByIdentifier(identifier).getId()
		);
		pictureRepository.flush();

		amazonWebServices.deletePhotoFromS3Bucket(
			"/pictures", identifier.toString()
		);

		return new JavaScriptObjectNotation().
			withReason("Delete successfully").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 儲值愛心
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/deposit.asp")
	@Secured({"ROLE_YONGHU"})
	ModelAndView deposit(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.deposit",
			null,
			locale
		));

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		for (Plan plan : planRepository.findAll()) {
			Element planElement = document.createElement("plan");
			planElement.setAttribute("points", Short.toString(plan.getPoints()));
			planElement.setAttribute("amount", Integer.toString(plan.getAmount()));
			planElement.setTextContent("方案" + plan.getId().toString());
			documentElement.appendChild(planElement);
		}

		Element heartsElement = document.createElement("hearts");
		documentElement.appendChild(heartsElement);
		heartsElement.setTextContent(
			historyRepository.countByInitiative(me) > 0
			? historyRepository.sumByInitiativeHearts(me).toString() : "0"
		);

		ModelAndView modelAndView = new ModelAndView("deposit");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 動態紀錄
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/activeLogs.asp")
	@Secured({"ROLE_YONGHU"})
	ModelAndView transaction(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Document document = historyService.historiesToDocument(me);
		Element documentElement = document.getDocumentElement();

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.activeLogs",
			null,
			locale
		));

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		ModelAndView modelAndView = new ModelAndView("activeLogs");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 升級 VIP
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/upgrade.asp")
	@Secured({"ROLE_YONGHU"})
	ModelAndView upgrade(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.upgrade",
			null,
			locale
		));

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		ModelAndView modelAndView = new ModelAndView("upgrade");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 车马费(男对女)
	 *
	 * @param female 女生
	 * @param points 点数
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/fare.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String fare(@RequestParam("whom") UUID femaleUUID, @RequestParam(name = "howMany") short points, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover male = loverService.loadByUsername(
			authentication.getName()
		);

		Lover female = loverService.loadByIdentifier(femaleUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.fare(
				male,
				female,
				points,
				locale
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 给我赖(男对女)
	 *
	 * @param female 女生
	 * @param greetingMessage 招呼语
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/stalking.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String gimmeYourLineInvitation(@RequestParam("whom") UUID femaleUUID, @RequestParam(name = "what", required = false) String greetingMessage, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover male = loverService.loadByUsername(
			authentication.getName()
		);

		Lover female = loverService.loadByIdentifier(femaleUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.gimme(
				male,
				female,
				greetingMessage,
				locale
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 打招呼(女对男)
	 *
	 * @param male 男生
	 * @param greetingMessage 招呼语
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/greet.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String greet(@RequestParam("whom") UUID maleUUID, @RequestParam(name = "what", required = false) String greetingMessage, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover female = loverService.loadByUsername(
			authentication.getName()
		);

		Lover male = loverService.loadByIdentifier(maleUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.greet(
				female,
				male,
				greetingMessage,
				locale
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 给你赖(女对男)
	 *
	 * @param male 男生
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/stalked.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String inviteMeAsLineFriend(@RequestParam("whom") UUID maleUUID, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover female = loverService.loadByUsername(
			authentication.getName()
		);

		Lover male = loverService.loadByIdentifier(maleUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.inviteMeAsLineFriend(
				female,
				male,
				locale
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
			if (Objects.equals(exception.getMessage(), "inviteMeAsLineFriend.mustntBeNull")) {
				jsonObject.put("redirect", "/me.asp");
			}
		}
		return jsonObject.toString();
	}

	/**
	 * 不給你賴
	 *
	 * @param maleUUID
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/notStalked.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String refuseToBeLineFriend(@RequestParam("whom") UUID maleUUID, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover female = loverService.loadByUsername(
			authentication.getName()
		);

		Lover male = loverService.loadByIdentifier(maleUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.refuseToBeLineFriend(
				female,
				male,
				locale
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 看过我
	 *
	 * @param masochism 谁被看
	 * @param authentication 用户凭证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/peek.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({"ROLE_YONGHU"})
	String peek(@RequestParam("whom") UUID masochismUUID, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover sadism = loverService.loadByUsername(
			authentication.getName()
		);

		Lover masochism = loverService.loadByIdentifier(masochismUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.peek(
				sadism,
				masochism
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					exception.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}
}
