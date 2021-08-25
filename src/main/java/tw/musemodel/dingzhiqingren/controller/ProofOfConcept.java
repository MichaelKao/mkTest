package tw.musemodel.dingzhiqingren.controller;

import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import tw.musemodel.dingzhiqingren.service.LoverService;

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
	private HistoryService historyService;

	@GetMapping(path = "/dialogists/{lover:\\d+}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<History> dialogists(@PathVariable Lover lover) {
		return historyService.latestConversations(lover);
	}

	@Autowired
	private LoverService loverService;

	@Autowired
	private LoverRepository loverRepository;
//
//	@GetMapping(path = "/resetShadow/{lover:\\d+}.json", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	String resetShadow(@PathVariable Lover lover, Locale locale) {
//		JSONObject jsonObject = loverService.resetShadow(
//			lover.getIdentifier(),
//			locale
//		);
//	}

	@GetMapping(path = "/h2c/{hexadecimalId:^[0-7][0-9a-f]{7}$}/", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String hexToDec(@PathVariable String hexadecimalId) {
		return Integer.toString(
			Integer.parseUnsignedInt(hexadecimalId, 16)
		);
	}

	@GetMapping(path = "/resetShadow/{hexadecimalId:^[0-9A-Fa-f]\\{8\\}+}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Lover resetShadow(@PathVariable String hexadecimalId, Locale locale) {
		Integer id = Integer.parseUnsignedInt(hexadecimalId, 16);
		return loverRepository.findById(id).orElseThrow();
	}
}
