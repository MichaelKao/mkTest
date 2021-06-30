package tw.musemodel.dingzhiqingren.controller;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
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

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@GetMapping(path = "/points/{identifier}")
	@ResponseBody
	@SuppressWarnings("null")
	long points(@PathVariable UUID identifier) {
		Lover lover = loverService.loadByIdentifier(identifier);
		if (Objects.isNull(lover)) {
			return Long.MIN_VALUE;
		}
		Long points = historyService.points(lover);
		return Objects.isNull(points) ? 0 : points;
	}

	@GetMapping(path = "/gimme")
	@ResponseBody
	String date(@RequestParam Lover male, @RequestParam Lover female, Locale locale) {
		JSONObject jsonObject;
		try {
			jsonObject = historyService.gimme(
				male,
				female,
				"給我賴",
				locale
			);
		} catch (IllegalArgumentException illegalArgumentException) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					illegalArgumentException.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		} catch (RuntimeException runtimeException) {
			jsonObject = new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					runtimeException.getMessage(),
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}
		return jsonObject.toString();
	}
}
