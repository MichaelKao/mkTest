package tw.musemodel.dingzhiqingren.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.service.Inpay2Service;

/**
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/inpay2")
public class InPay2Controller {

	private final static Logger LOGGER = LoggerFactory.getLogger(InPay2Controller.class);

	@Autowired
	private Inpay2Service inpay2Service;

	/**
	 * 建立交易
	 *
	 * @return @throws Exception
	 */
	@PostMapping(path = "/createPayment.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String createPayment() throws Exception {
		return inpay2Service.getTokenByTrade();
	}

	/**
	 * 取得厂商验证码
	 *
	 * @return 厂商验证码 JSON 对象
	 * @throws Exception
	 */
	@PostMapping(path = "/getTokenByTrade.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String getTokenByTrade() throws Exception {
		return inpay2Service.getTokenByTrade();
	}
}
