package tw.musemodel.dingzhiqingren.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.service.Inpay2Service;

/**
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/poc")
public class ProofOfConcept {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	private Inpay2Service inpay2Service;

	@GetMapping(path = "/decrypt", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String decrypt() throws Exception {
		return inpay2Service.decrypt("7woM9RorZKAtXJRVccAb0qhHYm+5lnlhBzyfh5EZdNck7PacNsRHgv/Jvp//ajJidqcQcs0UmAgPQVjXQHeziw==");
	}

	@GetMapping(path = "/encrypt", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String encrypt() throws Exception {
		return inpay2Service.encrypt("{\"Name\":\"Test\",\"ID\":\"A123456789\"}");
	}
}
