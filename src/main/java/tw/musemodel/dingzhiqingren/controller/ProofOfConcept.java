package tw.musemodel.dingzhiqingren.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	private final static int MAX_CHARACTERS = 1000;

	@PostMapping(path = "/github")
	@ResponseBody
	void github(@RequestBody String payload) {
		payload = URLDecoder.decode(
			payload.replaceAll("^payload=", ""),
			StandardCharsets.UTF_8
		);
		final int length = payload.length();
		for (int i = 0; i < length; i += MAX_CHARACTERS) {
			Servant.lineNotify(payload.substring(
				i,
				i + MAX_CHARACTERS > length ? length : i + MAX_CHARACTERS
			));
		}
		LOGGER.info(
			"來自 GitHub 的網勾\n\n有效载荷：\n{}\n",
			payload
		);
	}
}
