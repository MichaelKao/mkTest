package tw.musemodel.dingzhiqingren.controller;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
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
	private LoverService loverService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 暂时的强改密码；实作完成后删除
	 *
	 * @param lover 用户号
	 * @param shadow 密码
	 * @return 更新后的用户号
	 */
	@PostMapping(path = "/{lover:\\d+}/passwd.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Lover passwd(@PathVariable Lover lover, @RequestParam String shadow) {
		lover.setShadow(passwordEncoder.encode(shadow));
		return loverService.saveLover(lover);
	}

	@Autowired
	private HistoryRepository historyRepository;

	@GetMapping(path = "/showAllPix.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	History showAllPix(@RequestParam Lover a, @RequestParam Lover b) {
		History history = historyRepository.findTop1ByInitiativeAndPassiveAndBehaviorOrderByIdDesc(a,
			b,
			History.Behavior.FANG_XING_SHENG_HUO_ZHAO
		);
		if (Objects.isNull(history)) {
			history = new History(a, b, History.Behavior.FANG_XING_SHENG_HUO_ZHAO);
		}
		Boolean showAllPictures = history.getShowAllPictures();
		history.setShowAllPictures(
			!(Objects.isNull(showAllPictures) || showAllPictures)
		);
		return historyRepository.saveAndFlush(history);
	}
}
