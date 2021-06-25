package tw.musemodel.dingzhiqingren.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;

/**
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/poc")
public class ProofOfConcept {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	HistoryRepository historyRepository;

	@GetMapping(path = "/history.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<History> history() {
		return historyRepository.findAll();
	}

	@GetMapping(path = "/greet.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	History greet(@RequestParam Lover from, @RequestParam Lover to, @RequestParam(required = false) String greeting) {
		History history = new History();
		history.setInitiative(from);
		history.setPassive(to);
		history.setBehavior(History.Behavior.DA_ZHAO_HU);
		history.setOccurred(new Date(System.currentTimeMillis()));
		if (Objects.nonNull(greeting)) {
			history.setGreeting(greeting);
		} else if (Objects.nonNull(from.getGreeting())) {
			history.setGreeting(from.getGreeting());
		}
		return historyRepository.saveAndFlush(history);
	}
}
