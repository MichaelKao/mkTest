package tw.musemodel.dingzhiqingren.controller;

import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.LoverService;
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

	@Autowired
	private LoverService loverService;

	@PostMapping(path = "/toCharacterData.xml")
	@ResponseBody
	ModelAndView toCharacterDataXml(@RequestBody String payload, HttpServletResponse response) throws Exception {
		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();
		CDATASection cdataSection = document.createCDATASection(payload);
		documentElement.appendChild(cdataSection);

		ModelAndView modelAndView = new ModelAndView("javascript");
		modelAndView.getModelMap().addAttribute(document);
		return modelAndView;
	}

	@GetMapping(path = "/{me:\\d+}/descendants.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String descendants(@PathVariable Lover me, @RequestParam(defaultValue = "0") final int p, @RequestParam(defaultValue = "5") final int s) {
		return loverService.
			getReferralCodeAndDescendants(me, p, s).
			toString();
	}

	@GetMapping(path = "/{lover:\\d+}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Lover lover(@PathVariable Lover lover) {
		return lover;
	}

	@GetMapping(path = "/{lover:\\d+}/followed.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Collection<Lover> followed(@PathVariable Lover lover) {
		return loverService.getThoseWhoFollowMe(lover);
	}

	@GetMapping(path = "/{lover:\\d+}/following.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Collection<Lover> following(@PathVariable Lover lover) {
		return loverService.getThoseIFollow(lover);
	}
}
