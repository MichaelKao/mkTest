package tw.musemodel.dingzhiqingren.controller;

import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import tw.musemodel.dingzhiqingren.service.LoverService;

/**
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/poc")
public class ProofOfConcept {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@GetMapping(path = "/points/{identifier}")
	@ResponseBody
	@SuppressWarnings("null")
	public long points(@PathVariable UUID identifier) {
		Lover lover = loverService.loadByIdentifier(identifier);
		if (Objects.isNull(lover)) {
			return Long.MIN_VALUE;
		}
		Long points = historyService.points(lover);
		return Objects.isNull(points) ? 0 : points;
	}
}
