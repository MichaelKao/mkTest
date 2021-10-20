package tw.musemodel.dingzhiqingren.controller;

import java.util.Collection;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.ForumService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

/**
 * 控制器：论坛
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/forum")
public class ForumController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForumController.class);

	@Autowired
	private ForumService forumService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private Servant servant;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	Collection<ForumThread> index(@RequestParam(defaultValue = "1") int p, @RequestParam(defaultValue = "10") int s, Authentication authentication) {
		Lover me = loverService.loadByUsername(
			authentication.getName()
		);
		return forumService.getAll(me.getGender(), p, s).getContent();
	}

	@GetMapping(path = "/{identifier:^[0-9a-z]{8}-([0-9a-z]{4}-){3}[0-9a-z]{12}$}.asp", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADVENTURER})
	ForumThread getOne(@PathVariable UUID identifier) {
		return forumService.getOneThread(identifier);
	}
}
