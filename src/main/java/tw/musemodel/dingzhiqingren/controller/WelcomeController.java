package tw.musemodel.dingzhiqingren.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
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
import tw.musemodel.dingzhiqingren.entity.Companionship;
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.LineGiven;
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Picture;
import tw.musemodel.dingzhiqingren.entity.Plan;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.entity.TrialCode;
import tw.musemodel.dingzhiqingren.model.Activated;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.ResetPassword;
import tw.musemodel.dingzhiqingren.model.SignUp;
import tw.musemodel.dingzhiqingren.repository.AllowanceRepository;
import tw.musemodel.dingzhiqingren.repository.AnnualIncomeRepository;
import tw.musemodel.dingzhiqingren.repository.CompanionshipRepository;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;
import tw.musemodel.dingzhiqingren.repository.LocationRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.PictureRepository;
import tw.musemodel.dingzhiqingren.repository.PlanRepository;
import tw.musemodel.dingzhiqingren.repository.StopRecurringPaymentApplicationRepository;
import tw.musemodel.dingzhiqingren.repository.TrialCodeRepository;
import tw.musemodel.dingzhiqingren.service.AmazonWebServices;
import tw.musemodel.dingzhiqingren.service.DashboardService;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;
import static tw.musemodel.dingzhiqingren.service.Servant.PAGE_SIZE_ON_THE_WALL;
import tw.musemodel.dingzhiqingren.service.WebSocketService;
import tw.musemodel.dingzhiqingren.specification.LoverSpecification;

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
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AmazonWebServices amazonWebServices;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private WebSocketService webSocketService;

	@Autowired
	private Servant servant;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LineGivenRepository lineGivenRepository;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private PictureRepository pictureRepository;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private StopRecurringPaymentApplicationRepository stopRecurringPaymentApplicationRepository;

	@Autowired
	private TrialCodeRepository trialCodeRepository;

	@Autowired
	private AnnualIncomeRepository annualIncomeRepository;

	@Autowired
	private AllowanceRepository allowanceRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CompanionshipRepository companionshipRepository;

	/**
	 * 首页
	 *
	 * @param vipPage
	 * @param reliefPage
	 * @param activePage
	 * @param registerPage
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/")
	ModelAndView index(
		@CookieValue(defaultValue = "0", name = "vipPage") int vipPage,
		@CookieValue(defaultValue = "0", name = "reliefPage") int reliefPage,
		@CookieValue(defaultValue = "0", name = "activePage") int activePage,
		@CookieValue(defaultValue = "0", name = "registerPage") int registerPage,
		Authentication authentication,
		Locale locale
	) throws IOException {
		Document document = Servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.home",
			null,
			locale
		));

		/*
		 登入状态下
		 */
		if (!servant.isNull(authentication)) {
			Lover me = loverService.loadByUsername(
				authentication.getName()
			);//登录者
			if (!loverService.isEligible(me)) {
				//未完成填写注册个人资讯
				return Servant.redirectToProfile();
			}
			documentElement.setAttribute(
				"identifier",
				me.getIdentifier().toString()
			);//识别码
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);//帐号(国码➕手机号)
			documentElement.setAttribute(
				me.getGender() ? "male" : "female",
				null
			);//性别

			/*
			 身份
			 */
			if (loverService.hasRole(me, Servant.ROLE_ADMINISTRATOR)) {
				documentElement.setAttribute(
					"almighty",
					null
				);
			}
			if (loverService.hasRole(me, Servant.ROLE_ACCOUNTANT)) {
				documentElement.setAttribute(
					"finance",
					null
				);
			}

			document = loverService.indexDocument(
				document,
				me,
				vipPage,
				reliefPage,
				activePage,
				registerPage,
				locale
			);//登入后的甜心或男仕列表

			if (loverService.annoucementCount(me) > 0) {
				documentElement.setAttribute(
					"announcement",
					Integer.toString(loverService.annoucementCount(me))
				);
			}//通知数

			if (loverService.unreadMessages(me) > 0) {
				documentElement.setAttribute(
					"inbox",
					Integer.toString(loverService.unreadMessages(me))
				);
			}//未读讯息数

			if (loverService.hasLineNotify(me)) {
				documentElement.setAttribute(
					"lineNotify",
					null
				);
			}//有无连动 LINE Notify
		}//if(登入状态下)

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 首頁更多甜心/男仕
	 *
	 * @param p 第几页
	 * @param type
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 * @throws JsonProcessingException
	 */
	@PostMapping(path = "/seeMoreLover.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	@SuppressWarnings({"UnusedAssignment", "null"})
	String seeMoreLover(@RequestParam int p, @RequestParam String type, HttpServletResponse response, Authentication authentication, Locale locale) {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);//本人

		Page<Lover> page = null;
		Cookie currentPageTypeCookie = null;
		if (!me.getGender()) {
			if ("vip".equals(type)) {
				page = loverService.vipOnTheWall(
					me,
					p,
					PAGE_SIZE_ON_THE_WALL
				);//甜心才会显示的贵宾会员列表区块
				Cookie cookie = new Cookie(
					"vipPage",
					Integer.toString(p)
				);
				cookie.setSecure(true);
				response.addCookie(cookie);
				currentPageTypeCookie = new Cookie(
					"currentPageType",
					"vip"
				);
				currentPageTypeCookie.setSecure(true);
				response.addCookie(currentPageTypeCookie);
			}
		}
		if ("relief".equals(type)) {
			page = loverService.relievingOnTheWall(
				me,
				p,
				PAGE_SIZE_ON_THE_WALL
			);//安心认证列表区块
			Cookie cookie = new Cookie(
				"reliefPage",
				Integer.toString(p)
			);
			cookie.setSecure(true);
			response.addCookie(cookie);
			currentPageTypeCookie = new Cookie(
				"currentPageType",
				"relief"
			);
			currentPageTypeCookie.setSecure(true);
			response.addCookie(currentPageTypeCookie);
		}
		if ("active".equals(type)) {
			page = loverService.latestActiveOnTheWall(
				me,
				p,
				PAGE_SIZE_ON_THE_WALL
			);//最近活跃列表区块
			Cookie cookie = new Cookie(
				"activePage",
				Integer.toString(p)
			);
			cookie.setSecure(true);
			response.addCookie(cookie);
			currentPageTypeCookie = new Cookie(
				"currentPageType",
				"active"
			);
			currentPageTypeCookie.setSecure(true);
			response.addCookie(currentPageTypeCookie);
		}
		if ("register".equals(type)) {
			page = loverService.latestRegisteredOnTheWall(
				me,
				p,
				PAGE_SIZE_ON_THE_WALL
			);//最新注册列表区块
			Cookie cookie = new Cookie(
				"registerPage",
				Integer.toString(p)
			);
			cookie.setSecure(true);
			response.addCookie(cookie);
			currentPageTypeCookie = new Cookie(
				"currentPageType",
				"register"
			);
			currentPageTypeCookie.setSecure(true);
			response.addCookie(currentPageTypeCookie);
		}

		JSONObject jsonObject = new JSONObject();
		return jsonObject.
			put("response", true).
			put(
				"result",
				loverService.seeMoreLover(me, page, locale)
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
			).toString();
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
			return Servant.redirectToRoot();
		}

		Document document = Servant.parseDocument();
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

		Document document = Servant.parseDocument();
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
	 * 用户自主更新密码。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 用户号
	 */
	@PostMapping(path = "/password.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String changePassword(@RequestParam String password, Authentication authentication, Locale locale) {
		Lover me = loverService.loadByUsername(authentication.getName());
		if (password.isBlank() || password.isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"resetPassword.shadowMustntBeNull",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().
				toString();
		}
		loverService.changePassword(me, password);
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"changePassword.done",
				null,
				locale
			)).
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 重新激活页面。
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

		Document document = Servant.parseDocument(
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

	/**
	 * 再激活。
	 *
	 * @param username 用户名(国码➕手机号)
	 * @param request 请求
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/reactivate.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String reactivate(@RequestParam String username, HttpServletRequest request, Authentication authentication, Locale locale) {
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
	 * 从头重新排序用户号们主键值。
	 *
	 * @param authentication 认证
	 * @return 重新排序的用户号们
	 */
	@GetMapping(path = "/reorder.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADMINISTRATOR})
	List<List<Object>> reorder() {
		List<List<Object>> thingies = new ArrayList<>();
		thingies.add(Arrays.
			stream(loverService.reorderPrimaryKey()).
			boxed().
			collect(Collectors.toList())
		);
		thingies.add(Arrays.
			stream(historyService.reorderPrimaryKey()).
			boxed().
			collect(Collectors.toList())
		);
		return thingies;
	}

	/**
	 * 用户欲重设密码页面。
	 *
	 * @param request 请求
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 */
	@GetMapping(path = "/resetPassword/")
	ModelAndView resetPassword(HttpServletRequest request, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			LOGGER.debug("已登入故重设密码页面重导至首页");
			return Servant.redirectToRoot();
		}

		/*
		 文件
		 */
		Document document = Servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.resetPassword",
			null,
			locale
		));
		documentElement.setAttribute("uri", request.getRequestURI());

		/*
		 表单
		 */
		Element formElement = document.createElement("form");
		formElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"resetPassword.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(formElement);

		/*
		 表单的国家栏
		 */
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

		ModelAndView modelAndView = new ModelAndView("resetPassword");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 用户欲重设密码。
	 *
	 * @param resetPassword 请求参数模型
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森格式对象
	 */
	@PostMapping(path = "/resetPassword/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String resetPassword(ResetPassword resetPassword, Authentication authentication, Locale locale) {
		if (!servant.isNull(authentication)) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"resetPassword.mustntBeAuthenticated",
					null,
					locale
				)).
				withResponse(false).
				toString();
		}

		try {
			return loverService.
				resetPassword(resetPassword, locale).
				toString();
		} catch (RuntimeException runtimeException) {
			return new JavaScriptObjectNotation().
				withReason(runtimeException.getMessage()).
				withResponse(false).
				toString();
		}
	}

	/**
	 * 用户准备重设密码页面。
	 *
	 * @param hexadecimalId
	 * @param request 请求
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 */
	@GetMapping(path = "/resetPassword/{hexadecimalId:^[0-7][0-9a-f]{7}$}/", produces = MediaType.TEXT_PLAIN_VALUE)
	ModelAndView resetPassword(@PathVariable String hexadecimalId, HttpServletRequest request, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			LOGGER.debug("已登入故激活页面重导至首页");
			return Servant.redirectToRoot();
		}

		/*
		 文件
		 */
		Document document = Servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.resetPassword",
			null,
			locale
		));

		/*
		 表单
		 */
		Element formElement = document.createElement("form");
		formElement.setAttribute(
			"i18n-submit",
			messageSource.getMessage(
				"resetPassword.form.submit",
				null,
				locale
			)
		);
		documentElement.appendChild(formElement);
		formElement.setAttribute("uri", request.getRequestURI());

		ModelAndView modelAndView = new ModelAndView("resettingPassword");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 用户重设密码。
	 *
	 * @param hexadecimalId 十六进制主键
	 * @param string 字符串
	 * @param shadow 新密码
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/resetPassword/{hexadecimalId:^[0-7][0-9a-f]{7}$}/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String resettingPassword(@PathVariable String hexadecimalId, @RequestParam String string, @RequestParam String shadow, Authentication authentication, Locale locale) {
		if (!servant.isNull(authentication)) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"resetPassword.mustntBeAuthenticated",
					null,
					locale
				)).
				withResponse(false).
				toString();
		}

		if (shadow.isBlank() || shadow.isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"resetPassword.shadowMustntBeNull",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().
				toString();
		}

		try {
			return loverService.resetPassword(
				hexadecimalId,
				string,
				shadow,
				locale
			).toString();
		} catch (RuntimeException runtimeException) {
			return new JavaScriptObjectNotation().
				withReason(runtimeException.getMessage()).
				withResponse(false).
				toJSONObject().
				toString();
		}
	}

	/**
	 * 登入
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/signIn.asp")
	ModelAndView signIn(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return Servant.redirectToRoot();
		}

		Document document = Servant.parseDocument(
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
		return Servant.redirectToRoot();
	}

	/**
	 * 新建帐户页面。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/signUp.asp")
	ModelAndView signUp(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (!servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		Document document = Servant.parseDocument();
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
	 * @param request 请求
	 * @param authentication 认证
	 * @return 杰森格式对象
	 */
	@PostMapping(path = "/signUp.asp")
	@ResponseBody
	String signUp(SignUp signUp, HttpServletRequest request, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
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

		if (!signUp.getReferralCode().isBlank() && Objects.isNull(loverRepository.findByReferralCode(signUp.getReferralCode()))) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"signUp.referralCodeNotExist",
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

	/**
	 * 看自己的个人资料。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/profile/")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView self(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = loverService.readDocument(me, locale);
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.profile",
			null,
			locale
		));//网页标题

		documentElement.setAttribute(
			"referralCode",
			me.getReferralCode()
		);

		documentElement.setAttribute(
			"me",
			null
		);

		// 本人看到解鎖的生活照
		documentElement.setAttribute(
			"unlockedPix",
			String.format(
				"https://%s/pictures/",
				Servant.STATIC_HOST
			)
		);

		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 看某人(也可能是自己)的个人资料。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/profile/{identifier}/")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView profile(@PathVariable UUID identifier, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Lover mofo = loverService.loadByIdentifier(identifier);//被看光光的家伙

		Document document = loverService.readDocument(
			mofo,
			locale
		);
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.profile",
			null,
			locale
		));//网页标题

		if (loverService.isVVIP(me)) {
			//长期贵宾
			documentElement.setAttribute(
				"vvip",
				null
			);
		}

		if (loverService.isVIP(me)) {
			//短期贵宾
			documentElement.setAttribute(
				"vip",
				null
			);
		}

		/*
		 登录者是否追踪此页情人
		 */
		for (Lover followed : loverService.getThoseIFollow(me)) {
			if (Objects.equals(followed, mofo)) {
				documentElement.appendChild(
					document.createElement("follow")
				);
				break;
			}
		}

		// 此頁是否為本人
		if (Objects.equals(me, mofo)) {
			documentElement.setAttribute(
				"me",
				null
			);
		}

		// 是否已封鎖此人
		if (loverService.getBlockers(me).contains(mofo)) {
			documentElement.setAttribute(
				"blocking",
				null
			);
		}

		// 是否已被此人封鎖
		if (loverService.getBlockeds(me).contains(mofo)) {
			documentElement.setAttribute(
				"blockedBy",
				null
			);
		}

		// 是否解鎖生活照
		History history = historyRepository.findByInitiativeAndPassiveAndBehavior(me, mofo, HistoryService.BEHAVIOR_PICTURES_VIEWABLE);
		if (Objects.nonNull(history) && history.getShowAllPictures()) {
			documentElement.setAttribute(
				"unlockedPix",
				String.format(
					"https://%s/pictures/",
					Servant.STATIC_HOST
				)
			);
		} else {
			documentElement.setAttribute(
				"lockedPix",
				String.format(
					"https://%s/lockedPic/",
					Servant.LOCALHOST
				)
			);
			if (Objects.nonNull(history) && !history.getShowAllPictures()) {
				documentElement.setAttribute(
					"waitForAuth",
					null
				);
			}
		}

		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 编辑个人资料。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/me.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView me(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Document document = loverService.writeDocument(me, locale);
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.editProfile",
			null,
			locale
		));//网页标题

		if (loverService.isVVIP(me)) {
			//长期贵宾
			documentElement.setAttribute(
				"vip",
				null
			);
		}

		ModelAndView modelAndView = new ModelAndView("editProfile");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 修改自己的個人檔案
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/me.asp")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String editProfile(Lover model, Authentication authentication, Locale locale) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jSONObject = loverService.updatePersonalInfo(me, model);

		return jSONObject.toString();
	}

	/**
	 * 我的收藏(追踪了谁)。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/favorite.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView favorite(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.favorite",
			null,
			locale
		));//网页标题

		document = loverService.loversSimpleInfo(
			document,
			loverService.getThoseIFollow(me),
			me,
			locale
		);

		ModelAndView modelAndView = new ModelAndView("favorite");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 收藏
	 *
	 * @param identifier
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/favorite.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String favorite(@RequestParam UUID identifier, Authentication authentication, Locale locale) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 識別碼的帳號
		Lover lover = loverService.loadByIdentifier(identifier);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.follow(
				me,
				lover,
				locale
			);
		} catch (Exception exception) {
			jsonObject = new JavaScriptObjectNotation().
				//withReason(messageSource.getMessage(
				//	exception.getMessage(),
				//	null,
				//	locale
				//)).
				withReason(
					exception.getMessage()
				).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}

	/**
	 * 封鎖
	 *
	 * @param identifier
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/block.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String block(@RequestParam UUID identifier, Authentication authentication, Locale locale) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		// 識別碼的帳號
		Lover lover = loverService.loadByIdentifier(identifier);

		JSONObject jsonObject;
		try {
			jsonObject = loverService.block(
				me,
				lover,
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
	 * 谁看过我。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/looksMe.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView seenMe(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.looksMe",
			null,
			locale
		));//网页标题

		List<History> histories = historyRepository.findByPassiveAndBehaviorOrderByOccurredDesc(
			me,
			HistoryService.BEHAVIOR_PEEK
		);

		Collection<Lover> following = loverService.getThoseIFollow(me);
		Set<Lover> peekers = new HashSet<>();
		for (History history : histories) {
			if (!peekers.contains(history.getInitiative()) && Objects.isNull(history.getInitiative().getDelete())) {
				peekers.add(history.getInitiative());
				Lover peeker = history.getInitiative();
				// 到訪次數
				Long times = historyRepository.countByInitiativeAndPassiveAndBehavior(
					peeker, me, Behavior.KAN_GUO_WO
				);
				Element peekerElement = document.createElement("peeker");
				documentElement.appendChild(peekerElement);
				peekerElement.setAttribute(
					"date",
					servant.DATE_TIME_FORMATTER_yyyyMMddHHmm.format(
						Servant.toTaipeiZonedDateTime(
							history.getOccurred()
						).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
					));
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
					String.format(
						"https://%s/profileImage/%s",
						Servant.STATIC_HOST,
						peeker.getProfileImage()
					)
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
				// 是否為長期貴賓 vvip
				if (loverService.isVVIP(peeker)) {
					peekerElement.setAttribute("vvip", null);
				}
				// 是否為短期貴賓 vip
				if (loverService.isVIP(peeker)) {
					peekerElement.setAttribute("vip", null);
				}
				if (Objects.nonNull(peeker.getRelief())) {
					Boolean relief = peeker.getRelief();
					peekerElement.setAttribute(
						"relief",
						relief ? "true" : "false"
					);
				}
				// 是否收藏對方
				if (Objects.nonNull(following) && following.contains(peeker)) {
					peekerElement.setAttribute(
						"following",
						null
					);
				}
				if (Objects.nonNull(peeker.getRelationship())) {
					Element relationshipElement = document.createElement("relationship");
					relationshipElement.setTextContent(
						messageSource.getMessage(
							peeker.getRelationship().toString(),
							null,
							locale
						)
					);
					peekerElement.appendChild(relationshipElement);
				}

				/*
				 出没地区
				 */
				Collection<Location> locations = loverService.getLocations(peeker, true);
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
							peekerElement.appendChild(locationElement);
						}
					}
				}
			}
		}

		ModelAndView modelAndView = new ModelAndView("looksMe");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 相本。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/album.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView album(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.album",
			null,
			locale
		));//网页标题

		/*
		 头像
		 */
		Element profileImageElement = document.createElement("profileImage");
		if (Objects.nonNull(me.getProfileImage())) {
			profileImageElement.setTextContent(
				String.format(
					"https://%s/profileImage/%s",
					Servant.STATIC_HOST,
					me.getProfileImage()
				)
			);
		}
		documentElement.appendChild(profileImageElement);

		/*
		 相片
		 */
		List<Picture> pictures = pictureRepository.findByLover(me);
		for (Picture picture : pictures) {
			String identifier = picture.getIdentifier().toString();
			Element pictureElement = document.createElement("picture");
			pictureElement.setAttribute("picIdentifier", identifier);
			pictureElement.setTextContent(
				String.format(
					"https://%s/pictures/%s",
					Servant.STATIC_HOST,
					identifier
				)
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
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @param file
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/uploadProfileImage")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String uploadProfileImage(@RequestParam("file") MultipartFile multipartFile, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		/*
		 解析上传的头贴是否为二维码
		 */
		try {
			JSONObject json = loverService.qrCodeToString(
				multipartFile.getInputStream(),
				locale
			);
			if (json.getString("result").matches("^http.*$")) {
				return new JavaScriptObjectNotation().
					withReason(
						messageSource.getMessage(
							"uploadProfileImage.cannotBeQRCode",
							null,
							locale
						)
					).
					withResponse(false).
					toString();
			}
		} catch (JSONException ignore) {
			LOGGER.debug("上传的头贴大概率估计不是二维码");
		}

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
	 * @param multipartFile
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/uploadPicture")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String uploadPicture(@RequestParam("file") MultipartFile multipartFile, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		/*
		 解析上传的照片是否为二维码
		 */
		try {
			JSONObject json = loverService.qrCodeToString(
				multipartFile.getInputStream(),
				locale
			);
			if (json.getString("result").matches("^http.*$")) {
				return new JavaScriptObjectNotation().
					withReason(
						messageSource.getMessage(
							"uploadProfileImage.cannotBeQRCode",
							null,
							locale
						)
					).
					withResponse(false).
					toString();
			}
		} catch (JSONException ignore) {
			LOGGER.debug("上传的照片大概率估计不是二维码");
		}

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
	 * @param identifier
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(value = "/deletePicture")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
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
	 * 选择充值方案。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/recharge.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	@SuppressWarnings("null")
	ModelAndView recharge(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}
		if (!me.getGender()) {
			//甜心无法充值
			return Servant.redirectToRoot();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.recharge",
			null,
			locale
		));//网页标题
		planRepository.findAll().stream().map(plan -> {
			Element planElement = document.createElement("plan");
			planElement.setAttribute("points", Short.toString(plan.getPoints()));
			planElement.setAttribute("amount", Integer.toString(plan.getAmount()));
			return planElement;
		}).forEachOrdered(planElement -> {
			documentElement.appendChild(planElement);
		});

		Element heartsElement = document.createElement("hearts");
		documentElement.appendChild(heartsElement);
		heartsElement.setTextContent(historyService.maleLeftPoints(me).toString());

		ModelAndView modelAndView = new ModelAndView("recharge");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 充值方案。
	 *
	 * @param plan
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/recharge/{plan:\\d}.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView recharge(@PathVariable Plan plan, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}
		if (!me.getGender()) {
			//甜心无法充值
			return Servant.redirectToRoot();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);
		if (servant.isDevelopment() || servant.isTesting()) {
			documentElement.setAttribute("development", "true");
		}

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.recharge",
			null,
			locale
		));//网页标题

		Element planElement = document.createElement("plan");
		planElement.setAttribute("id", plan.getId().toString());
		documentElement.appendChild(planElement);

		ModelAndView modelAndView = new ModelAndView("inpay2/ECPayPayment");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 动态日志。
	 *
	 * @param p 第几页
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/activities.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView activities(@RequestParam(defaultValue = "1") int p, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = historyService.historiesToDocument(
			me,
			p < 1 ? 0 : p - 1,
			10
		);
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.activeLogs",
			null,
			locale
		));//网页标题

		if (me.getGender()) {
			documentElement.setAttribute(
				"greeting",
				me.getGreeting()
			);//⁉️请 m@musemodel.tw 补个注解呗
		}

		documentElement.setAttribute(
			"i18n-confirm",
			messageSource.getMessage(
				"confirm.submit",
				null,
				locale
			)
		);//确认按钮

		documentElement.setAttribute(
			"i18n-cancel",
			messageSource.getMessage(
				"cancel",
				null,
				locale
			)
		);//取消按钮

		if (loverService.isVVIP(me)) {
			documentElement.setAttribute(
				"vvip",
				null
			);//长期贵宾
		}

		if (loverService.isVIP(me)) {
			documentElement.setAttribute(
				"vip",
				null
			);//短期贵宾
		}

		ModelAndView modelAndView = new ModelAndView("activities");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 升级贵宾。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/upgrade.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView upgrade(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}
		if (!me.getGender()) {
			//甜心无法升级贵宾
			return Servant.redirectToRoot();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.upgrade",
				null,
				locale
			)
		);//网页标题

		/*
		 长期贵宾
		 */
		if (loverService.isVVIP(me)) {
			documentElement.setAttribute(
				"vvip",
				null
			);

			if (dashboardService.isRecurringPaymentStoppable(me)) {
				documentElement.setAttribute(
					"isEligibleToStopRecurring",
					Boolean.toString(dashboardService.isRecurringPaymentStoppable(me))
				);
			}

			documentElement.setAttribute(
				"vvipExpiry",
				LoverService.DATE_TIME_FORMATTER.format(
					Servant.toTaipeiZonedDateTime(
						me.getVip()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID
					)
				)
			);
		}

		/*
		 短期贵宾但非体验
		 */
		if (loverService.isVIP(me)) {
			documentElement.setAttribute(
				"vip",
				null
			);

			documentElement.setAttribute(
				"vipExpiry",
				LoverService.DATE_TIME_FORMATTER.format(
					Servant.toTaipeiZonedDateTime(
						me.getVip()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID
					)
				)
			);
		}

		/*
		 短期体验
		 */
		if (loverService.isTrial(me)) {
			documentElement.setAttribute(
				"trial",
				null
			);
			documentElement.setAttribute(
				"trialExpiry",
				LoverService.DATE_TIME_FORMATTER.format(
					Servant.toTaipeiZonedDateTime(
						me.getVip()
					).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID
					)
				)
			);
		}

		if (Objects.isNull(me.getVip())) {
			documentElement.setAttribute(
				"ableToTrial",
				null
			);//具有体验资格
		}

		ModelAndView modelAndView = new ModelAndView("upgrade");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 升级长期贵宾或短期贵宾。
	 *
	 * @param vipType
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/upgrade/{vipType}.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView upgradeLongTerm(@PathVariable int vipType, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}
		if (!me.getGender()) {
			//甜心无法升级贵宾
			return Servant.redirectToRoot();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);
		if (servant.isDevelopment() || servant.isTesting()) {
			documentElement.setAttribute("development", "true");
		}

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.upgrade",
				null,
				locale
			)
		);//网页标题

		boolean isInTrialPeriod = loverService.isTrial(me),//在体验期内
			isShortTermVip = loverService.isVIP(me),//短期贵宾
			isLongTermVip = loverService.isVVIP(me);//长期贵宾

		if (isLongTermVip) {
			//长期贵宾
			documentElement.setAttribute(
				"vvip",
				null
			);
		}

		if (isShortTermVip) {
			//短期贵宾但非体验
			documentElement.setAttribute(
				"vip",
				null
			);
		}

		String view = null;
		if (vipType == 1) {
			if (isLongTermVip || isShortTermVip) {
				//长期贵宾，或，短期贵宾；则无法升级❗️
				return Servant.redirectToRoot();
			}
			view = "upgradeShortTerm";
		} else if (vipType == 2) {
			if (isLongTermVip) {
				//长期贵宾无法再升级❗
				return Servant.redirectToRoot();
			}
			view = "upgradeLongTerm";
		}

		ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 车马费(男对女)
	 *
	 * @param femaleUUID 女生
	 * @param points 点数
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/fare.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
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
	 * 男仕回覆要求車馬費
	 *
	 * @param femaleUUID
	 * @param historyId
	 * @param result
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/resFare.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String resFare(@RequestParam("whom") UUID femaleUUID, @RequestParam Long historyId, @RequestParam Boolean result, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		History reqHistory = historyRepository.findById(historyId).orElse(null);
		reqHistory.setReply(new Date(System.currentTimeMillis()));
		historyRepository.saveAndFlush(reqHistory);

		JSONObject jsonObject;
		if (!result) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"resFare.refuse",
					null,
					locale
				)).
				withResponse(true).
				toJSONObject();
			return jsonObject.put("resultStatus", false).toString();
		}

		Lover male = loverService.loadByUsername(
			authentication.getName()
		);

		Lover female = loverService.loadByIdentifier(femaleUUID);

		try {
			jsonObject = historyService.fare(
				male,
				female,
				reqHistory.getPoints(),
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
		return jsonObject.put("resultStatus", true).toString();
	}

	/**
	 * 要求車馬費
	 *
	 * @param maleUUID
	 * @param points
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/reqFare.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String reqFare(@RequestParam("whom") UUID maleUUID, @RequestParam(name = "howMany") short points, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover female = loverService.loadByUsername(
			authentication.getName()
		);

		Lover male = loverService.loadByIdentifier(maleUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.reqFare(
				female,
				male,
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
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/stalking.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String gimmeYourLineInvitation(@RequestParam("whom") UUID femaleUUID, Authentication authentication, Locale locale) {
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
	 * @param maleUUID 男生
	 * @param greetingMessage 招呼语
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/greet.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
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
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/stalked.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
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
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/notStalked.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
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
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 杰森对象字符串
	 */
	@PostMapping(path = "/peek.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
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

	/**
	 * 服务条款。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/terms.asp")
	ModelAndView terms(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement;
		if (servant.isNull(authentication)) {
			//未登录
			documentElement = document.getDocumentElement();
		} else {
			//已登录
			documentElement = servant.documentElement(
				document,
				authentication
			);
		}

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.terms",
				null,
				locale
			)
		);//网页标题

		ModelAndView modelAndView = new ModelAndView("terms");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 隐私权政策。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/privacy.asp")
	ModelAndView privacy(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = Servant.parseDocument();

		Element documentElement;
		if (servant.isNull(authentication)) {
			//未登录
			LOGGER.debug("測試isNull()");
			documentElement = document.getDocumentElement();
		} else {
			//已登录
			documentElement = servant.documentElement(
				document,
				authentication
			);
		}

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.privacy",
				null,
				locale
			)
		);//网页标题

		ModelAndView modelAndView = new ModelAndView("privacy");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 甜心提取车马费。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/withdrawal.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView withdrawal(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}
		if (me.getGender()) {
			//男士无法提取车马费
			return Servant.redirectToRoot();
		}

		Document document = loverService.withdrawalDocument(me, locale);
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.withdrawal",
				null,
				locale
			)
		);//网页标题

		ModelAndView modelAndView = new ModelAndView("withdrawal");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 甜心提取車馬費(銀行匯款)
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/wireTransfer.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String wireTransfer(@RequestParam String wireTransferBankCode, @RequestParam String wireTransferBranchCode, @RequestParam String wireTransferAccountName, @RequestParam String wireTransferAccountNumber, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = loverService.wireTransfer(
				wireTransferBankCode,
				wireTransferBranchCode,
				wireTransferAccountName,
				wireTransferAccountNumber,
				me,
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
	 * 刪除帳號
	 *
	 * @param authentication 认证
	 * @return
	 */
	@PostMapping(path = "/deleteAccount")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String deleteAccount(Authentication authentication) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		me.setDelete(authentication.getName());
		me.setLogin(null);
		me.setShadow(null);
		loverRepository.saveAndFlush(me);

		return new JavaScriptObjectNotation().
			withRedirect("/signOut.asp").
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 星級評價 新增或編輯
	 *
	 * @param rate
	 * @param comment
	 * @param whom
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/rate.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	@SuppressWarnings("UseSpecificCatch")
	String rate(@RequestParam String rate, @RequestParam String comment, @RequestParam UUID whom, Authentication authentication, Locale locale) {
		if (rate.isBlank() || rate.isEmpty()) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"rate.rateMustntBeNull",
					null,
					locale
				)).
				withResponse(false).toString();
		}
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover initiate = loverService.loadByUsername(
			authentication.getName()
		);

		Lover passive = loverService.loadByIdentifier(whom);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.rate(
				initiate,
				passive,
				Short.parseShort(rate),
				comment,
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
	 * 更新服務地區
	 *
	 * @param location
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/location.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String loaction(@RequestParam Location location, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = loverService.updateLocation(location, me);
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
	 * 更新服務標籤
	 *
	 * @param service
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/service.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String loaction(@RequestParam Companionship service, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = loverService.updateService(service, me);
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
	 * 上傳手持身分證
	 *
	 * @param multipartFile
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@PostMapping(path = "/uploadIdentity")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String uploadIdentity(@RequestParam("file") MultipartFile multipartFile, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		amazonWebServices.uploadPhotoToS3Bucket(
			multipartFile,
			me.getId().toString(),
			"/identity"
		);

		me.setRelief(false);
		loverRepository.saveAndFlush(me);

		return new JavaScriptObjectNotation().
			withReason(
				messageSource.getMessage(
					"uploadIdentity.done",
					null,
					locale
				)
			).
			withResponse(true).
			withResult(me.toString()).
			toJSONObject().toString();
	}

	/**
	 * 上傳 LINE QRcode
	 *
	 * @param multipartFile
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 * @throws URISyntaxException
	 */
	@PostMapping(path = "/isLine.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String isLine(@RequestParam(name = "file") MultipartFile multipartFile, Authentication authentication, Locale locale) throws URISyntaxException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		JavaScriptObjectNotation json = new JavaScriptObjectNotation();

		String anchor;
		try (InputStream inputStream = multipartFile.getInputStream()) {
			JSONObject jsonObject = loverService.qrCodeToString(
				inputStream,
				locale
			);
			anchor = jsonObject.getString("result");
			if (!jsonObject.getBoolean("response")) {
				return json.
					withReason(json.getReason()).
					withResponse(false).
					toString();
			}
		} catch (IOException ioException) {
			return json.
				withReason(ioException.getLocalizedMessage()).
				withResponse(false).
				toString();
		}

		URI uri = new URI(anchor);
		if (Servant.isLine(uri) || Servant.isWeChat(uri)) {
			json.setResult(uri);
		}
		me.setInviteMeAsLineFriend(anchor);
		loverRepository.saveAndFlush(me);

		return json.toString();
	}

	/**
	 * 男士打開已同意的LINE連結
	 *
	 * @param femaleUUID
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/maleOpenLine.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String openLine(@RequestParam("whom") UUID femaleUUID, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover male = loverService.loadByUsername(
			authentication.getName()
		);

		Lover female = loverService.loadByIdentifier(femaleUUID);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.openLine(
				male,
				female,
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
	 * 搜尋頁
	 *
	 * @param location 地区
	 * @param companionship 服务
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/search.json")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView search(@RequestParam(required = false) Location location, @RequestParam(required = false) Companionship companionship, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.search",
				null,
				locale
			)
		);//网页标题

		Collection<Lover> lovers = loverRepository.findAll(
			LoverSpecification.search(
				me,
				new HashSet<>(loverService.findInceptions(
					companionship,
					location
				)),
				new HashSet<>(loverService.getExceptions(me))
			)
		);

		document = loverService.loversSimpleInfo(
			document,
			lovers,
			me,
			locale
		);

		documentElement.setAttribute(
			"count",
			Integer.toString(
				lovers.size()
			)
		);//搜寻到几笔资料

		String searchName = null;
		if (Objects.nonNull(location)) {
			searchName = messageSource.getMessage(
				location.getName(),
				null,
				locale
			);
		}
		if (Objects.nonNull(companionship)) {
			searchName = messageSource.getMessage(
				companionship.getName(),
				null,
				locale
			);
		}
		documentElement.setAttribute(
			"searchName",
			searchName
		);

		ModelAndView modelAndView = new ModelAndView("search");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 显示二维码。
	 *
	 * @param girlIdentifier
	 * @param response
	 * @param authentication 认证
	 * @throws IOException
	 * @throws WriterException
	 */
	@GetMapping(path = "/{girlIdentifier}.png", produces = MediaType.IMAGE_PNG_VALUE)
	@Secured({Servant.ROLE_ADVENTURER})
	void erWeiMa(@PathVariable final UUID girlIdentifier, HttpServletResponse response, Authentication authentication) throws IOException, WriterException {
		if (servant.isNull(authentication)) {
			return;
		}
		Lover guy = loverService.loadByUsername(
			authentication.getName()
		);
		Lover girl = loverService.loadByIdentifier(girlIdentifier);
		if (Objects.isNull(girl)) {
			return;
		}

		LineGiven lineGiven = lineGivenRepository.findByGirlAndGuy(girl, guy);
		if (Objects.nonNull(lineGiven)) {
			Boolean agreed = lineGiven.getResponse();
			if (Objects.nonNull(agreed) && agreed) {
				response.setHeader("Content-Disposition", "inline");
				MatrixToImageWriter.writeToStream(
					new QRCodeWriter().encode(
						girl.getInviteMeAsLineFriend(),
						BarcodeFormat.QR_CODE,
						256,
						256
					),
					"png",
					response.getOutputStream()
				);
			}
		}
	}

	/**
	 * 群发打招呼。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws JsonProcessingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/groupGreeting.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView groupGreeting(Authentication authentication, Locale locale) throws JsonProcessingException, SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}
		if (me.getGender()) {
			//男士禁用群发打招呼
			return Servant.redirectToRoot();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.groupGreeting",
				null,
				locale
			)
		);//网页标题

		documentElement.setAttribute(
			"greeting",
			me.getGreeting()
		);//默认招呼语

		document = loverService.groupGreetingDocument(document, me);

		ModelAndView modelAndView = new ModelAndView("groupGreeting");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 群发打招呼。
	 *
	 * @param greetingMessage
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/groupGreeting.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String groupGreeting(@RequestParam(required = false) String greetingMessage, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = loverService.groupGreeting(me, greetingMessage, locale);
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
	 * 设定。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/setting.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView setting(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.setting",
				null,
				locale
			)
		);//网页标题

		document = loverService.settingDocument(document, me);

		ModelAndView modelAndView = new ModelAndView("setting");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 聊天室。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/chatroom/{identifier}/")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView chatRoom(@PathVariable UUID identifier, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		/*
		 聊天对象
		 */
		Lover partner = loverService.loadByIdentifier(identifier);

		Document document = Servant.parseDocument();
		document = webSocketService.chatroom(document, me, partner);
		document = webSocketService.inbox(document, me);
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.chatroom",
				null,
				locale
			)
		);//网页标题

		if (Objects.equals(me, partner)) {
			return Servant.redirectToRoot();
		}

		documentElement.setAttribute(
			"selfIdentifier",
			me.getIdentifier().toString()
		);

		if (loverService.getBlockers(me).contains(partner)) {
			documentElement.setAttribute(
				"blocking",
				null
			);//是否已封锁此人
		}

		if (loverService.getBlockeds(me).contains(partner)) {
			documentElement.setAttribute(
				"blockedBy",
				null
			);//是否已被此人封锁
		}

		ModelAndView modelAndView = new ModelAndView("chatroom");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 收信匣。
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页页面
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/inbox.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView inbox(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.inbox",
				null,
				locale
			)
		);//网页标题

		document = webSocketService.inbox(document, me);

		ModelAndView modelAndView = new ModelAndView("inbox");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	@PostMapping(path = "/loadMoreInboxList.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String loadMoreInboxList(@RequestParam int p, Authentication authentication, Locale locale) throws TransformerException, IOException {

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		List<ForumThread> list = new ArrayList<>();

		return webSocketService.loadMoreInboxList(
			me,
			p
		).toString();
	}

	/**
	 * 解除定期定額長期貴賓
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/stopRecurring.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String stopRecurring(Authentication authentication, Locale locale) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!dashboardService.isRecurringPaymentStoppable(me)) {
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

		History history = historyRepository.findTop1ByInitiativeAndBehaviorOrderByOccurredDesc(me, Behavior.YUE_FEI);
		StopRecurringPaymentApplication stopRecurringPaymentApplication = new StopRecurringPaymentApplication(me, history);
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
	 * 未解鎖的生活照
	 *
	 * @param identifier
	 * @param picIdentifier
	 * @param authentication
	 * @param response
	 * @throws IOException
	 * @throws WriterException
	 */
	@GetMapping(path = "/lockedPic/{picIdentifier}.png", produces = MediaType.IMAGE_PNG_VALUE)
	@Secured({"ROLE_YONGHU"})
	@SuppressWarnings("UnusedAssignment")
	void pictures(@PathVariable String picIdentifier, Authentication authentication, HttpServletResponse response) throws IOException, WriterException {
		File tmp = null;
		try {
			S3Object o = AmazonWebServices.AMAZON_S3.getObject(
				AmazonWebServices.BUCKET_NAME,
				"pictures/" + picIdentifier
			);
			S3ObjectInputStream s3is = o.getObjectContent();
			tmp = File.createTempFile("s3test", ".png");
			Files.copy(s3is, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

			try {
				BufferedImage image = ImageIO.read(tmp);

				int radius = 33;
				int size = radius * 2 + 1;
				float weight = 1.0f / (size * size);
				float[] data = new float[size * size];

				for (int i = 0; i < data.length; i++) {
					data[i] = weight;
				}
				ConvolveOp op = new ConvolveOp(
					new Kernel(size, size, data)
				);
				BufferedImage i = op.filter(image, null);
				ImageIO.write(i, "png", jpegOutputStream);
			} catch (IllegalArgumentException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			tmp.delete();
			byte[] imgByte = jpegOutputStream.toByteArray();

			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/png");

			try (ServletOutputStream responseOutputStream = response.getOutputStream()) {
				responseOutputStream.write(imgByte);
				responseOutputStream.flush();
			}
		} catch (IOException ex) {
		}
	}

	/**
	 * 取得照片授權
	 *
	 * @param femaleUUID
	 * @param authentication
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/picturesAuth.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String picturesAuth(@RequestParam("whom") UUID uuid, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Lover another = loverService.loadByIdentifier(uuid);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.picturesAuth(
				me,
				another
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
	 * 接受給對方看生活照
	 *
	 * @param uuid
	 * @param authentication
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/acceptPixAuth.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String acceptPixAuth(@RequestParam("whom") UUID uuid, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Lover another = loverService.loadByIdentifier(uuid);

		JSONObject jsonObject;
		try {
			jsonObject = historyService.acceptPixAuth(
				me,
				another
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
	 * 看更多留言
	 *
	 * @param p
	 * @param type
	 * @param authentication
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/moreRate.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	@SuppressWarnings("null")
	String moreRate(@RequestParam int p, @RequestParam UUID whom, Authentication authentication, Locale locale) {
		// 誰
		Lover lover = loverService.loadByIdentifier(whom);

		Page<History> ratePage = historyRepository.
			findByPassiveAndBehaviorOrderByOccurredDesc(
				lover,
				History.Behavior.PING_JIA,
				PageRequest.of(p, 3)
			);

		JSONObject jsonObject = new JSONObject();
		if (Objects.nonNull(ratePage) && ratePage.getTotalPages() == p + 1) {
			jsonObject.put("lastPage", true);
		}
		return jsonObject.
			put("response", true).
			put(
				"result",
				loverService.moreRate(ratePage, locale)
			).
			toString();
	}

	/**
	 * 顯示下線名單(分頁)
	 *
	 * @param p
	 * @param s
	 * @param authentication
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/descendants.json")
	@ResponseBody
	String descendants(@RequestParam(defaultValue = "0") final int p, @RequestParam(defaultValue = "5") final int s, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		return loverService.
			getReferralCodeAndDescendants(me, p, s).
			toString();
	}

	/**
	 * 體驗一日VIP
	 *
	 * @param p
	 * @param s
	 * @param authentication
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/trial.json")
	@ResponseBody
	String trial(@RequestParam String code, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		if (!loverService.isValidCode(code)) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"trial.codeDoesntExist",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject().toString();
		}

		TrialCode trialCode = trialCodeRepository.findByCode(code);

		try {
			loverService.trial(me, trialCode);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"trial.done",
				null,
				locale
			)).
			withResponse(true).
			toJSONObject().toString();
	}

	/**
	 * 退回車馬費
	 *
	 * @param history
	 * @param authentication
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/returnFare.json")
	@ResponseBody
	String returnFare(@RequestParam History history, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		JSONObject jsonObject;
		try {
			jsonObject = historyService.returnFare(history, locale);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}
		return jsonObject.toString();
	}

	/**
	 * 邀請碼頁面
	 *
	 * @param authentication
	 * @param locale 语言环境
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/referralCode.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView refferalCode(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.referralCode",
				null,
				locale
			)
		);//网页标题

		Element referralCodeElement = document.createElement("referralCode");
		referralCodeElement.setTextContent(me.getReferralCode());
		documentElement.appendChild(referralCodeElement);

		ModelAndView modelAndView = new ModelAndView("referralCode");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 條件搜尋頁面
	 *
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/filter.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView filter(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			//补齐个人资料
			return Servant.redirectToProfile();
		}

		Document document = Servant.parseDocument();
		Element documentElement = servant.documentElement(
			document,
			authentication
		);

		documentElement.setAttribute(
			"title",
			messageSource.getMessage(
				"title.filter",
				null,
				locale
			)
		);//网页标题

		document = loverService.filterDocument(document, me, locale);

		ModelAndView modelAndView = new ModelAndView("filter");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 條件搜尋
	 *
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
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/filter.json")
	@Secured({Servant.ROLE_ADVENTURER})
	@ResponseBody
	String filter(@RequestParam int p, @RequestParam(required = false) String nickname,
		@RequestParam Integer maximumAge, @RequestParam Integer minimumAge,
		@RequestParam Short maximumHeight, @RequestParam Short minimumHeight,
		@RequestParam Short maximumWeight, @RequestParam Short minimumWeight,
		@RequestParam(required = false) Lover.BodyType bodyType, @RequestParam(required = false) Lover.Education education,
		@RequestParam(required = false) Lover.Marriage marriage, @RequestParam(required = false) Lover.Smoking smoking,
		@RequestParam(required = false) Lover.Drinking drinking, @RequestParam Short annualIncome,
		@RequestParam Short allowance, @RequestParam Short location, @RequestParam("service") Short companionship,
		Authentication authentication, Locale locale) {

		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		JSONObject jsonObject;
		try {
			jsonObject = loverService.filter(
				me,
				p,
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
				annualIncomeRepository.findById(annualIncome).orElse(null),
				allowanceRepository.findById(allowance).orElse(null),
				locationRepository.findById(location).orElse(null),
				companionshipRepository.findById(companionship).orElse(null),
				locale
			);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}
		return jsonObject.toString();
	}

	@PostMapping(path = "/unblock.json")
	@ResponseBody
	String unblock(@RequestParam UUID blockedIdentifier, Authentication authentication, Locale locale) {
		if (servant.isNull(authentication)) {
			return servant.mustBeAuthenticated(locale);
		}
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		Lover blocked = loverService.loadByIdentifier(
			blockedIdentifier
		);

		JSONObject jsonObject;
		try {
			jsonObject = loverService.unlock(me, blocked);
		} catch (Exception exception) {
			return new JavaScriptObjectNotation().
				withReason(exception.getMessage()).
				withResponse(false).
				toJSONObject().toString();
		}
		return jsonObject.toString();
	}

	@PostMapping(path = "/loadMoreActivities.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String loadMoreActivities(@RequestParam int p, Authentication authentication, Locale locale) throws TransformerException, IOException {

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		return historyService.loadMoreActivities(
			me,
			p,
			10
		).toString();
	}

	/**
	 * 聊天室載入更多對話
	 *
	 * @param friend
	 * @param p
	 * @param authentication
	 * @param locale
	 * @return
	 * @throws TransformerException
	 * @throws IOException
	 */
	@PostMapping(path = "/loadMoreMsgs.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String loadMoreMsgs(@RequestParam UUID friend, @RequestParam int p, Authentication authentication, Locale locale) throws TransformerException, IOException {

		return new Gson().toJson(webSocketService.wholeHistoryMsgs(
			loverService.loadByUsername(
				authentication.getName()
			),
			loverService.loadByIdentifier(
				friend
			),
			p
		));
	}

	/**
	 * 傳送訊息後更新左邊訊息欄
	 *
	 * @param friend
	 * @param authentication
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/updateInbox.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	String updateInbox(@RequestParam UUID friend, Authentication authentication, Locale locale) {
		return webSocketService.updateInbox(
			loverService.loadByUsername(
				authentication.getName()
			),
			loverService.loadByIdentifier(
				friend
			)
		).toString();
	}

	/**
	 * AJAX 產生右邊對話框後將訊息轉為已讀
	 *
	 * @param friend
	 * @param authentication
	 * @param locale
	 */
	@PostMapping(path = "/openChatRoom.json")
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	void openChatRoom(@RequestParam UUID friend, Authentication authentication, Locale locale) {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		Lover chatPartner = loverService.loadByIdentifier(
			friend
		);
		// 將訊息改成已讀
		List<History> unreadMessages = historyRepository.
			findByInitiativeAndPassiveAndBehaviorInAndSeenNullOrderByOccurredDesc(
				chatPartner,
				me,
				loverService.behaviorOfConversation()
			);
		for (History history : unreadMessages) {
			history.setSeen(new Date(System.currentTimeMillis()));
			historyRepository.saveAndFlush(history);
		}
	}

	@PostMapping(path = "/shadow/{someone:\\d+}.json", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADMINISTRATOR})
	@Transactional
	String shadow(@PathVariable Lover someone, @RequestParam String shadow) {
		shadow = passwordEncoder.encode(shadow);
		someone.setShadow(shadow);
		loverRepository.saveAndFlush(someone);
		return shadow;
	}
}
