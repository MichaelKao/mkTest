package tw.musemodel.dingzhiqingren.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * 控制器：概念验证
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/poc")
public class ProofOfConcept {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	private Servant servant;

	@GetMapping(path = "/isDevelopment", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String isDevelopment() {
		return Boolean.toString(servant.isDevelopment());
	}

	@GetMapping(path = "/isTesting", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String isTesting() {
		return Boolean.toString(servant.isTesting());
	}
}
