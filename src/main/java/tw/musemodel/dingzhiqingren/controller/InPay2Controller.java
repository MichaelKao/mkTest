package tw.musemodel.dingzhiqingren.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Plan;
import tw.musemodel.dingzhiqingren.service.Inpay2Service;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * 控制器：站内付 2.0
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/inpay2")
public class InPay2Controller {

	private final static Logger LOGGER = LoggerFactory.getLogger(InPay2Controller.class);

	private static final String OKAY = "1|OK";

	@Autowired
	private Servant servant;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Inpay2Service inpay2Service;

	@Autowired
	private LoverService loverService;

	/**
	 * 建立交易。
	 *
	 * @param payToken 付款代码(有效期限为30分钟)
	 * @param session 分配给会话的标识符
	 * @return 绿界回传支付令牌对象字符串
	 */
	@PostMapping(path = "/createPayment/{payToken}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String createPayment(@PathVariable final String payToken, final HttpSession session) {
		return inpay2Service.createPayment(payToken, session);
	}

	/**
	 * 取得厂商验证码(信用卡定期定额)以升级长期贵宾。
	 *
	 * @param session 分配给会话的标识符
	 * @param locale 语言环境
	 * @return 厂商验证码 JSON 对象
	 * @throws com.​fasterxml.​jackson.​core.JsonProcessingException
	 */
	@PostMapping(path = "/getPeriodTokenByTrade.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String getPeriodTokenByTrade(final @RequestParam UUID me, final HttpSession session, final Locale locale) throws JsonProcessingException {
		return inpay2Service.getPeriodTokenByTrade(
			loverService.loadByIdentifier(me),
			session,
			locale
		);
	}

	/**
	 * 取得厂商验证码(付款选择清单)以升级短期贵宾。
	 *
	 * @param session 分配给会话的标识符
	 * @param locale 语言环境
	 * @return 厂商验证码 JSON 对象
	 * @throws com.​fasterxml.​jackson.​core.JsonProcessingException
	 */
	@PostMapping(path = "/getTokenByTradeForShortTerm.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String getTokenByTradeForShortTerm(final @RequestParam UUID me, final HttpSession session, final Locale locale) throws JsonProcessingException {
		return inpay2Service.getTokenByTrade(
			loverService.loadByIdentifier(me),
			session,
			locale
		);
	}

	/**
	 * 取得厂商验证码(付款选择清单)以充值。
	 *
	 * @param session 分配给会话的标识符
	 * @return 厂商验证码 JSON 对象
	 * @throws com.​fasterxml.​jackson.​core.JsonProcessingException
	 */
	@PostMapping(path = "/getTokenByTrade.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String getTokenByTrade(final @RequestParam UUID me, final @RequestParam Plan plan, final HttpSession session) throws JsonProcessingException {
		return inpay2Service.getTokenByTrade(
			plan,
			loverService.loadByIdentifier(me),
			session
		);
	}

	/**
	 * 绿界以幕前方式传送付款结果。
	 *
	 * @param resultData 付款结果
	 * @return 给绿界的响应
	 */
	@PostMapping(path = "/orderResult.asp")
	ModelAndView orderResult(@RequestParam("ResultData") String resultData, Authentication authentication, Locale locale) throws Exception {
		JSONObject jsonObject = inpay2Service.
			handleOrderResult(resultData);

		Lover me = loverService.loadByUsername(jsonObject.
			getJSONObject("result").
			getString("customField")
		);//目前登录的用户

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.orderResult",
			null,
			locale
		));

		if (loverService.hasRole(me, Servant.ROLE_ADMINISTRATOR)) {
			//万能天神
			documentElement.setAttribute(
				"almighty",
				null
			);
		}
		if (loverService.hasRole(me, Servant.ROLE_ACCOUNTANT)) {
			//财务会计
			documentElement.setAttribute(
				"finance",
				null
			);
		}

		documentElement.setAttribute(
			me.getGender() ? "male" : "female",
			null
		);

		documentElement.setAttribute("signIn", null);//登入中

		String itemName = jsonObject.
			getJSONObject("result").
			getString("itemName");
		Boolean isSuccessful = jsonObject.
			getBoolean("response");
		Integer totalAmount = jsonObject.
			getJSONObject("result").
			getInt("totalAmount");
		if (isSuccessful) {
			documentElement.setAttribute("orderResult", "success");
			documentElement.setAttribute(
				"date",
				jsonObject.
					getJSONObject("result").
					getString("paymentDate")
			);
			documentElement.setAttribute(
				"message",
				messageSource.getMessage(
					"payment.success",
					null,
					locale
				));
			documentElement.setAttribute(
				"amount",
				totalAmount.toString()
			);
			String result = null;
			if (Objects.equals(itemName, "1")) {
				/*
				 充值方案 1
				 */
				result = messageSource.getMessage(
					"recharge.3000",
					null,
					locale
				);
			} else if (Objects.equals(itemName, "2")) {
				/*
				 充值方案 2
				 */
				result = messageSource.getMessage(
					"recharge.5000",
					null,
					locale
				);
			} else if (Objects.equals(itemName, "3")) {
				/*
				 充值方案 3
				 */
				result = messageSource.getMessage(
					"recharge.10000",
					null,
					locale
				);
			} else if (Objects.equals(itemName, "長期貴賓")) {
				/*
				 升级为長期贵宾
				 */
				result = messageSource.getMessage(
					"longTerm.itemName",
					null,
					locale
				);
			} else if (Objects.equals(itemName, "短期貴賓")) {
				/*
				 升级为短期贵宾
				 */
				result = messageSource.getMessage(
					"shortTerm.itemName",
					null,
					locale
				);
			}
			documentElement.setAttribute("result", result);
			documentElement.setAttribute("redirect", "/");
		} else {
			documentElement.setAttribute("orderResult", "fail");
			documentElement.setAttribute(
				"message",
				messageSource.getMessage(
					"payment.failed",
					null,
					locale
				));
			documentElement.setAttribute(
				"reason",
				jsonObject.get("reason").toString()
			);
			documentElement.setAttribute("redirect", "/");
		}//if;else
		Element fbqElement = document.createElement("fbq");
		fbqElement.appendChild(document.createCDATASection(
			String.format(
				new BufferedReader(new InputStreamReader(
					new ClassPathResource("skeleton/orderResult.js.txt").
						getInputStream(),
					Servant.UTF_8
				)).lines().collect(Collectors.joining("\n")),
				totalAmount,
				isSuccessful.toString()
			)
		));
		documentElement.appendChild(fbqElement);

		ModelAndView modelAndView = new ModelAndView("orderResult");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	/**
	 * 绿界以幕后方式传送第 n 次付款结果。
	 *
	 * @param requestBody 付款结果
	 * @return 给绿界的响应
	 */
	@PostMapping(path = "/periodReturn.asp")
	@ResponseBody
	ResponseEntity<String> handlePeriodReturn(@RequestBody String requestBody) throws Exception {
		JSONObject jsonObject = inpay2Service.handlePeriodReturn(requestBody);
		if (jsonObject.getBoolean("response")) {
			return new ResponseEntity<>(
				OKAY,
				HttpStatus.OK
			);
		} else {
			return new ResponseEntity<>(
				null,
				HttpStatus.OK
			);
		}
	}

	/**
	 * 绿界以幕后方式传送付款结果。
	 *
	 * @param requestBody 付款结果
	 * @return 给绿界的响应
	 */
	@PostMapping(path = "/return.asp")
	@ResponseBody
	ResponseEntity<String> handleReturn(@RequestBody String requestBody) throws Exception {
		JSONObject jsonObject = inpay2Service.handleReturn(requestBody);
		if (jsonObject.getBoolean("response")) {
			return new ResponseEntity<>(
				OKAY,
				HttpStatus.OK
			);
		} else {
			return new ResponseEntity<>(
				null,
				HttpStatus.OK
			);
		}
	}
}
