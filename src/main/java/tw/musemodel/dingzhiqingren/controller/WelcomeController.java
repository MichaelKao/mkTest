package tw.musemodel.dingzhiqingren.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.time.format.DateTimeFormatter;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
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
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Picture;
import tw.musemodel.dingzhiqingren.entity.Plan;
import tw.musemodel.dingzhiqingren.entity.ServiceTag;
import tw.musemodel.dingzhiqingren.entity.StopRecurringPaymentApplication;
import tw.musemodel.dingzhiqingren.model.Activated;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.ResetPassword;
import tw.musemodel.dingzhiqingren.model.SignUp;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LineGivenRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.PictureRepository;
import tw.musemodel.dingzhiqingren.repository.PlanRepository;
import tw.musemodel.dingzhiqingren.repository.StopRecurringPaymentApplicationRepository;
import tw.musemodel.dingzhiqingren.service.AmazonWebServices;
import tw.musemodel.dingzhiqingren.service.DashboardService;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;
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
			if (!loverService.isEligible(me)) {
				return new ModelAndView("redirect:/me.asp");
			}

			// 確認性別
			Boolean gender = me.getGender();

			documentElement.setAttribute(
				gender ? "male" : "female",
				null
			);

			documentElement.setAttribute(
				"identifier",
				me.getIdentifier().toString()
			);

			// 身分
			boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
			boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
			if (isAlmighty) {
				documentElement.setAttribute(
					"almighty",
					null
				);
			}
			if (isFinance) {
				documentElement.setAttribute(
					"finance",
					null
				);
			}

			// 登入後的甜心或男仕列表
			document = loverService.indexDocument(document, me, locale);

			// 通知數
			if (loverService.annoucementCount(me) > 0) {
				documentElement.setAttribute(
					"announcement",
					Integer.toString(loverService.annoucementCount(me))
				);
			}

			// 未讀訊息數
			if (loverService.unreadMessages(me) > 0) {
				documentElement.setAttribute(
					"inbox",
					Integer.toString(loverService.unreadMessages(me))
				);
			}

			// 有無連動 LINE notify
			if (loverService.hasLineNotify(me)) {
				documentElement.setAttribute(
					"lineNotify",
					null
				);
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
	@SuppressWarnings("null")
	String seeMoreLover(@RequestParam int p, @RequestParam String type, Authentication authentication, Locale locale) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Page<Lover> page = null;
		if (!me.getGender()) {
			if ("vip".equals(type)) {
				page = loverService.vipOnTheWall(
					me,
					p,
					PAGE_SIZE_ON_THE_WALL
				);//甜心才会显示的贵宾会员列表区块
			}
		}
		if ("relief".equals(type)) {
			page = loverService.relievingOnTheWall(
				me,
				p,
				PAGE_SIZE_ON_THE_WALL
			);//安心认证列表区块
		}
		if ("active".equals(type)) {
			page = loverService.latestActiveOnTheWall(
				me,
				p,
				PAGE_SIZE_ON_THE_WALL
			);//最近活跃列表区块
		}
		if ("register".equals(type)) {
			page = loverService.latestRegisteredOnTheWall(
				me,
				p,
				PAGE_SIZE_ON_THE_WALL
			);//最新注册列表区块
		}

		JSONObject jsonObject = new JSONObject();
		if (Objects.nonNull(page) && page.getTotalPages() == p + 1) {
			jsonObject.put("lastPage", true);
		}
		return jsonObject.
			put("response", true).
			put(
				"result",
				loverService.seeMoreLover(page, locale)
			).
			toString();
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
		Document document = servant.parseDocument();
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

		Integer id = Integer.parseUnsignedInt(hexadecimalId, 16);
		Lover mofo = loverRepository.findById(id).orElseThrow();

		/*
		 文件
		 */
		Document document = servant.parseDocument();
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
				toString();
		}
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
			return Servant.redirectToRoot();
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
		return Servant.redirectToRoot();
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
	 * 看自己的個人檔案
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/profile/")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView self(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = loverService.readDocument(me, locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.profile",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		documentElement.setAttribute(
			"referralCode",
			me.getReferralCode()
		);

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

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
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
	 * 看某人(也可能是自己)的個人檔案
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/profile/{identifier}/")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView profile(@PathVariable UUID identifier, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		// 識別碼的帳號
		Lover lover = loverService.loadByIdentifier(identifier);

		Document document = loverService.readDocument(lover, locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.profile",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		// 本人是否為 VIP
		if (loverService.isVVIP(me)) {
			documentElement.setAttribute(
				"vip",
				null
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		// 有登入狀態
		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		if (gender) {
			// 是否已交換 LINE
			History history = historyRepository.findByInitiativeAndPassiveAndBehavior(me, lover, Behavior.LAI_KOU_DIAN);

			if (Objects.nonNull(history)) {
				documentElement.setAttribute(
					"matched",
					null
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

		// 是否已封鎖此人
		if (me.getBlocking().contains(lover)) {
			documentElement.setAttribute(
				"blocking",
				null
			);
		}

		// 是否已被此人封鎖
		if (me.getBlockedBy().contains(lover)) {
			documentElement.setAttribute(
				"blockedBy",
				null
			);
		}

		// 是否解鎖生活照
		History history = historyRepository.findByInitiativeAndPassiveAndBehavior(me, lover, BEHAVIOR_PICTURES_VIEWABLE);
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
	 * 顯示自己的編輯頁面
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/me.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView editPage(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

		Document document = loverService.writeDocument(me, locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.editProfile",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 本人是否為 VVIP
		if (loverService.isVVIP(me)) {
			documentElement.setAttribute(
				"vip",
				null
			);
		}

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

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
	 * 我的收藏
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/favorite.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView favorite(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.favorite",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		Set<Lover> following = me.getFollowing();
		document = loverService.loversSimpleInfo(document, following);

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
	 * 誰看過我
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/looksMe.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView whoLooksMe(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.looksMe",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		List<History> histories = historyRepository.findByPassiveAndBehaviorOrderByOccurredDesc(
			me,
			Behavior.KAN_GUO_WO
		);

		Set<Lover> peekers = new HashSet<Lover>();
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
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
						servant.toTaipeiZonedDateTime(
							history.getOccurred()
						).withZoneSameInstant(Servant.ASIA_TAIPEI)
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
						servant.STATIC_HOST,
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
			}
		}

		ModelAndView modelAndView = new ModelAndView("looksMe");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 相片管理頁面
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/album.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView album(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.album",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		Element profileImageElement = document.createElement("profileImage");
		if (Objects.nonNull(me.getProfileImage())) {
			profileImageElement.setTextContent(
				String.format(
					"https://%s/profileImage/%s",
					servant.STATIC_HOST,
					me.getProfileImage()
				)
			);
		}
		documentElement.appendChild(profileImageElement);

		List<Picture> pictures = pictureRepository.findByLover(me);
		for (Picture picture : pictures) {
			String identifier = picture.getIdentifier().toString();
			Element pictureElement = document.createElement("picture");
			pictureElement.setAttribute("picIdentifier", identifier);
			pictureElement.setTextContent(
				String.format(
					"https://%s/pictures/%s",
					servant.STATIC_HOST,
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
	 * 选择充值方案
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/recharge.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView recharge(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return servant.redirectToRoot();
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);//我谁⁉️
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.recharge",
			null,
			locale
		));
		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);
		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		boolean gender = me.getGender();
		if (!gender) {
			return servant.redirectToRoot();
		}//女性则重导向首页
		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
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
			historyRepository.countByInitiative(me) > 0 ? historyRepository.sumByInitiativeHearts(me).toString() : "0"
		);

		ModelAndView modelAndView = new ModelAndView("recharge");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 充值方案
	 *
	 * @param plan
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/recharge/{plan:\\d}.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView recharge(@PathVariable Plan plan, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return servant.redirectToRoot();
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);//我谁⁉️
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.recharge",
			null,
			locale
		));
		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);
		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		boolean gender = me.getGender();
		if (!gender) {
			return servant.redirectToRoot();
		}//女性则重导向首页
		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		Element planElement = document.createElement("plan");
		planElement.setAttribute("id", plan.getId().toString());
		documentElement.appendChild(planElement);

		ModelAndView modelAndView = new ModelAndView("inpay2/ECPayPayment");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 動態紀錄
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/activeLogs.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView activeLogs(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return new ModelAndView("redirect:/");
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = historyService.historiesToDocument(me);
		Element documentElement = document.getDocumentElement();

		documentElement.setAttribute("title", messageSource.getMessage(
			"title.activeLogs",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		if (gender) {
			documentElement.setAttribute(
				"greeting",
				me.getGreeting()
			);
		}
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
			));

		// 本人是否為長期貴賓 VVIP
		if (loverService.isVVIP(me)) {
			documentElement.setAttribute(
				"vvip",
				null
			);
		}
		// 本人是否為短期貴賓 VIP
		if (loverService.isVIP(me)) {
			documentElement.setAttribute(
				"vip",
				null
			);
		}

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		ModelAndView modelAndView = new ModelAndView("activeLogs");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 升級貴賓
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/upgrade.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView upgrade(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return servant.redirectToRoot();
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);//我谁⁉️
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.upgrade",
			null,
			locale
		));
		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);
		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		/*
		 确认性别
		 */
		boolean gender = me.getGender();
		if (!gender) {
			return servant.redirectToRoot();
		}//女性则重导向首页
		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		if (loverService.isVVIP(me)) {
			documentElement.setAttribute(
				"vvip",
				null
			);
			if (dashboardService.isEligible(me)) {
				documentElement.setAttribute(
					"isEligibleToStopRecurring",
					Boolean.toString(dashboardService.isEligible(me))
				);
			}
			documentElement.setAttribute(
				"vvipExpiry",
				loverService.DATE_TIME_FORMATTER.format(
					servant.toTaipeiZonedDateTime(
						me.getVip()
					).withZoneSameInstant(
						Servant.ASIA_TAIPEI
					)
				)
			);
		}//是否为 VVIP⁉️

		if (loverService.isVIP(me)) {
			documentElement.setAttribute(
				"vip",
				null
			);
			documentElement.setAttribute(
				"vipExpiry",
				loverService.DATE_TIME_FORMATTER.format(
					servant.toTaipeiZonedDateTime(
						me.getVip()
					).withZoneSameInstant(
						Servant.ASIA_TAIPEI
					)
				)
			);
		}//是否为 VIP⁉️

		ModelAndView modelAndView = new ModelAndView("upgrade");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 升級長期貴賓或短期貴賓
	 *
	 * @param vipType
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/upgrade/{vipType}.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView upgradeLongTerm(@PathVariable int vipType, Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return Servant.redirectToRoot();
		}

		Lover me = loverService.loadByUsername(
			authentication.getName()
		);//我谁⁉️
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.upgrade",
			null,
			locale
		));
		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);
		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		/*
		 确认性别
		 */
		boolean gender = me.getGender();
		if (!gender) {
			return servant.redirectToRoot();
		}//女性则重导向首页
		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		if (loverService.isVVIP(me)) {
			documentElement.setAttribute(
				"vvip",
				null
			);
		}//是否为 VVIP⁉️

		if (loverService.isVIP(me)) {
			documentElement.setAttribute(
				"vip",
				null
			);
		}//是否为 VIP⁉️

		String view = null;
		if (vipType == 1) {
			if (loverService.isVVIP(me) || loverService.isVIP(me)) {
				return servant.redirectToRoot();
			} //若是長期或者短期貴賓則不能再升級短期貴賓
			view = "upgradeShortTerm";
		} else if (vipType == 2) {
			if (loverService.isVVIP(me)) {
				return servant.redirectToRoot();
			}//若是長期期貴賓則不能再升級長期貴賓，短期貴賓可以升級
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
	 * 服務條款
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/terms.asp")
	ModelAndView terms(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.terms",
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
			// 身分
			boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
			boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
			if (isAlmighty) {
				documentElement.setAttribute(
					"almighty",
					null
				);
			}
			if (isFinance) {
				documentElement.setAttribute(
					"finance",
					null
				);
			}

			// 確認性別
			Boolean gender = me.getGender();

			documentElement.setAttribute(
				gender ? "male" : "female",
				null
			);

			// 通知數
			if (loverService.annoucementCount(me) > 0) {
				documentElement.setAttribute(
					"announcement",
					Integer.toString(loverService.annoucementCount(me))
				);
			}

			// 未讀訊息數
			if (loverService.unreadMessages(me) > 0) {
				documentElement.setAttribute(
					"inbox",
					Integer.toString(loverService.unreadMessages(me))
				);
			}

			documentElement.setAttribute(
				"identifier",
				me.getIdentifier().toString()
			);
		}

		ModelAndView modelAndView = new ModelAndView("terms");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 隱私權政策
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/privacy.asp")
	ModelAndView privacy(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.privacy",
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
			// 身分
			boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
			boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
			if (isAlmighty) {
				documentElement.setAttribute(
					"almighty",
					null
				);
			}
			if (isFinance) {
				documentElement.setAttribute(
					"finance",
					null
				);
			}

			// 確認性別
			Boolean gender = me.getGender();

			documentElement.setAttribute(
				gender ? "male" : "female",
				null
			);

			// 通知數
			if (loverService.annoucementCount(me) > 0) {
				documentElement.setAttribute(
					"announcement",
					Integer.toString(loverService.annoucementCount(me))
				);
			}

			// 未讀訊息數
			if (loverService.unreadMessages(me) > 0) {
				documentElement.setAttribute(
					"inbox",
					Integer.toString(loverService.unreadMessages(me))
				);
			}

			documentElement.setAttribute(
				"identifier",
				me.getIdentifier().toString()
			);
		}

		ModelAndView modelAndView = new ModelAndView("privacy");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 甜心提取車馬費
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/withdrawal.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView withdrawal(Authentication authentication, Locale locale) throws SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			servant.redirectToRoot();
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = loverService.withdrawalDocument(me, locale);
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.withdrawal",
			null,
			locale
		));

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean gender = me.getGender();

		documentElement.setAttribute(
			gender ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		if (!servant.isNull(authentication)) {
			documentElement.setAttribute(
				"signIn",
				authentication.getName()
			);
		}

		// 男仕返回首頁
		if (gender) {
			servant.redirectToRoot();
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

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
	@Secured({Servant.ROLE_ADVENTURER})
	@ResponseBody
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
	String loaction(@RequestParam ServiceTag service, Authentication authentication, Locale locale) {
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
	 * @param location
	 * @param serviceTag
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws JsonProcessingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/search.json")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView search(@RequestParam(required = false) Location location, @RequestParam(required = false) ServiceTag serviceTag, Authentication authentication, Locale locale) throws JsonProcessingException, SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return Servant.redirectToRoot();
		}
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.search",
			null,
			locale
		));

		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean isMale = me.getGender();

		documentElement.setAttribute(
			isMale ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		Collection<Lover> lovers = loverRepository.findAll(
			LoverSpecification.search(
				me,
				location,
				serviceTag
			)
		);

		document = loverService.loversSimpleInfo(document, lovers);

		// 搜尋到幾筆資料
		documentElement.setAttribute(
			"count",
			Integer.toString(lovers.size())
		);

		String searchName = null;
		if (Objects.nonNull(location)) {
			searchName = messageSource.getMessage(
				location.getName(),
				null,
				locale
			);
		}
		if (Objects.nonNull(serviceTag)) {
			searchName = messageSource.getMessage(
				serviceTag.getName(),
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
	 * 顯示二維碼
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
	 * 群發打招呼
	 *
	 * @param location
	 * @param serviceTag
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws JsonProcessingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/groupGreeting.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView groupGreeting(@RequestParam(required = false) Location location, @RequestParam(required = false) ServiceTag serviceTag, Authentication authentication, Locale locale) throws JsonProcessingException, SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return Servant.redirectToRoot();
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.groupGreeting",
			null,
			locale
		));

		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean isMale = me.getGender();

		documentElement.setAttribute(
			isMale ? "male" : "female",
			null
		);

		// 男仕不需群發打招呼
		if (isMale) {
			servant.redirectToRoot();
		}

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		documentElement.setAttribute(
			"greeting",
			me.getGreeting()
		);

		document = loverService.groupGreetingDocument(document, me);

		ModelAndView modelAndView = new ModelAndView("groupGreeting");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 群發打招呼
	 *
	 * @param greetingMessage
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return
	 */
	@PostMapping(path = "/groupGreeting.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String groupGreeting(@RequestParam(required = false) String greetingMessage, Authentication authentication, Locale locale) {
		LOGGER.debug(
			"3079\t群發打招呼:\n\n{}\n",
			greetingMessage
		);
		if (servant.isNull(authentication)) {
			LOGGER.debug(
				"3084\t群發驗證身分:\n\n{}\n",
				authentication
			);
			return servant.mustBeAuthenticated(locale);
		}
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		LOGGER.debug(
			"3093\t群發找出本人:\n\n{}\n",
			me
		);

		JSONObject jsonObject;
		try {
			LOGGER.debug(
				"3100\t群發開始前:\n\n{}\n",
				greetingMessage
			);
			jsonObject = loverService.groupGreeting(me, greetingMessage, locale);
			LOGGER.debug(
				"3105\t群發完成:\n\n{}\n",
				jsonObject
			);
		} catch (Exception exception) {
			LOGGER.debug(
				"3110\t群發超出例外:\n\n{}\n",
				exception.getMessage()
			);
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
	 * 設定
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws JsonProcessingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/setting.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView setting(Authentication authentication, Locale locale) throws JsonProcessingException, SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return Servant.redirectToRoot();
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.setting",
			null,
			locale
		));

		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean isMale = me.getGender();

		documentElement.setAttribute(
			isMale ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		// 有無連動 LINE notify
		if (loverService.hasLineNotify(me)) {
			documentElement.setAttribute(
				"lineNotify",
				null
			);
		}

		ModelAndView modelAndView = new ModelAndView("setting");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 聊天室
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws JsonProcessingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/chatroom/{identifier}/")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView chatRoom(@PathVariable UUID identifier, Authentication authentication, Locale locale) throws JsonProcessingException, SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return Servant.redirectToRoot();
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}
		// 聊天對象
		Lover partner = loverService.loadByIdentifier(identifier);
		if (Objects.equals(me, partner)) {
			return new ModelAndView("redirect:/");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.chatroom",
			null,
			locale
		));

		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean isMale = me.getGender();

		documentElement.setAttribute(
			"gender",
			isMale.toString()
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		documentElement.setAttribute(
			"selfIdentifier",
			me.getIdentifier().toString()
		);

		// 有無連動 LINE notify
		if (loverService.hasLineNotify(me)) {
			documentElement.setAttribute(
				"lineNotify",
				null
			);
		}

		// 是否已封鎖此人
		if (me.getBlocking().contains(partner)) {
			documentElement.setAttribute(
				"blocking",
				null
			);
		}

		// 是否已被此人封鎖
		if (me.getBlockedBy().contains(partner)) {
			documentElement.setAttribute(
				"blockedBy",
				null
			);
		}

		document = webSocketService.chatroom(document, me, partner);

		ModelAndView modelAndView = new ModelAndView("chatroom");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 收信匣
	 *
	 * @param authentication 认证
	 * @param locale 语言环境
	 * @return 网页
	 * @throws JsonProcessingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@GetMapping(path = "/inbox.asp")
	@Secured({Servant.ROLE_ADVENTURER})
	ModelAndView inbox(Authentication authentication, Locale locale) throws JsonProcessingException, SAXException, IOException, ParserConfigurationException {
		if (servant.isNull(authentication)) {
			return Servant.redirectToRoot();
		}

		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		if (!loverService.isEligible(me)) {
			return new ModelAndView("redirect:/me.asp");
		}

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.inbox",
			null,
			locale
		));

		documentElement.setAttribute(
			"signIn",
			authentication.getName()
		);

		// 身分
		boolean isAlmighty = servant.hasRole(me, "ROLE_ALMIGHTY");
		boolean isFinance = servant.hasRole(me, "ROLE_FINANCE");
		if (isAlmighty) {
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (isFinance) {
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		// 確認性別
		Boolean isMale = me.getGender();

		documentElement.setAttribute(
			isMale ? "male" : "female",
			null
		);

		// 通知數
		if (loverService.annoucementCount(me) > 0) {
			documentElement.setAttribute(
				"announcement",
				Integer.toString(loverService.annoucementCount(me))
			);
		}

		// 未讀訊息數
		if (loverService.unreadMessages(me) > 0) {
			documentElement.setAttribute(
				"inbox",
				Integer.toString(loverService.unreadMessages(me))
			);
		}

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		// 有無連動 LINE notify
		if (loverService.hasLineNotify(me)) {
			documentElement.setAttribute(
				"lineNotify",
				null
			);
		}

		document = webSocketService.inbox(document, me);

		ModelAndView modelAndView = new ModelAndView("inbox");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
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
		if (!dashboardService.isEligible(me)) {
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
		StopRecurringPaymentApplication stopRecurringPaymentApplication = new StopRecurringPaymentApplication(me);
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
	void pictures(@PathVariable String picIdentifier, Authentication authentication, HttpServletResponse response) throws IOException, WriterException {
		File tmp = null;
		try {
			S3Object o = amazonWebServices.AMAZON_S3.getObject(amazonWebServices.BUCKET_NAME, "pictures/" + picIdentifier);
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
			ServletOutputStream responseOutputStream = response.getOutputStream();
			responseOutputStream.write(imgByte);
			responseOutputStream.flush();
			responseOutputStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 取得照片授權
	 *
	 * @param femaleUUID
	 * @param authentication
	 * @param locale
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
	 * @param locale
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
	 * @param locale
	 * @return
	 */
	@PostMapping(path = "/moreRate.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	@SuppressWarnings("null")
	String moreRate(@RequestParam int p, @RequestParam UUID whom, Authentication authentication, Locale locale) {
		// 本人
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);

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
}
