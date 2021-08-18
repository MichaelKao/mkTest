package tw.musemodel.dingzhiqingren.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.DashboardService;
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

	@GetMapping(path = "/showAllPix.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	boolean showAllPix(@RequestParam Lover a, @RequestParam Lover b) {
		return historyService.toggleShowAllPictures(a, b);
	}

	@Autowired
	private DashboardService dashboardService;

	@GetMapping(path = "/{lover:\\d+}/huh.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	boolean huhPix(@PathVariable Lover lover) {
		return dashboardService.isEligible(lover);
	}
}
