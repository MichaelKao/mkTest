package tw.musemodel.dingzhiqingren.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	 * 取得厂商验证码(信用卡定期定额)。
	 *
	 * @param session 分配给会话的标识符
	 * @param locale 语言环境
	 * @return 厂商验证码 JSON 对象
	 * @throws com.​fasterxml.​jackson.​coreJsonProcessingException
	 */
	@PostMapping(path = "/getPeriodTokenByTrade.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String getPeriodTokenByTrade(final @RequestParam UUID me, final HttpSession session) throws JsonProcessingException {
		return inpay2Service.getPeriodTokenByTrade(
			loverService.loadByIdentifier(me),
			session
		);
	}

	/**
	 * 取得厂商验证码(付款选择清单)。
	 *
	 * @param session 分配给会话的标识符
	 * @return 厂商验证码 JSON 对象
	 * @throws com.​fasterxml.​jackson.​coreJsonProcessingException
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
	ModelAndView orderResult(@RequestParam("ResultData") String resultData, Locale locale, Authentication authentication) throws Exception {
		JSONObject jSONObject = inpay2Service.handleOrderResult(resultData);

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		documentElement.setAttribute("title", messageSource.getMessage(
			"title.orderResult",
			null,
			locale
		));

		// 本人
		Lover me = loverService.loadByUsername(
			jSONObject.getJSONObject("result").getString("customField")
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

		// 登入中
		documentElement.setAttribute(
			"signIn",
			null
		);

		documentElement.setAttribute(
			"identifier",
			me.getIdentifier().toString()
		);

		String itemName = jSONObject.getJSONObject("result").getString("itemName");
		if (jSONObject.getBoolean("response")) {
			documentElement.setAttribute(
				"date",
				jSONObject.getJSONObject("result").getString("paymentDate")
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
				jSONObject.getJSONObject("result").get("totalAmount").toString()
			);
			String result = null;
			if (Objects.equals(itemName, "1")) {
				result = messageSource.getMessage(
					"recharge.3000",
					null,
					locale
				);
			}
			if (Objects.equals(itemName, "2")) {
				result = messageSource.getMessage(
					"recharge.5000",
					null,
					locale
				);
			}
			if (Objects.equals(itemName, "3")) {
				result = messageSource.getMessage(
					"recharge.10000",
					null,
					locale
				);
			}
			if (Objects.equals(itemName, "vip")) {
				result = messageSource.getMessage(
					"upgrade.vip",
					null,
					locale
				);
			}
			documentElement.setAttribute("result", result);
			documentElement.setAttribute("redirect", "/");
		}

		if (!jSONObject.getBoolean("response")) {
			documentElement.setAttribute("fail", null);
			documentElement.setAttribute(
				"message",
				messageSource.getMessage(
					"payment.failed",
					null,
					locale
				));
			documentElement.setAttribute("reason", jSONObject.get("reason").toString());
			documentElement.setAttribute("redirect", "/");
		}
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
