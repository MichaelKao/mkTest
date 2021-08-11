package tw.musemodel.dingzhiqingren.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
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

	private static final int MAX_CHARACTERS = 1000;

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
	private LoverRepository loverRepository;

	@Autowired
	private LoverService loverService;

	/**
	 * 暂时的最近活跃；实作完成后删除
	 *
	 * @param lover 用户号
	 * @return
	 */
	@GetMapping(path = "/{lover:\\d+}/latestActive.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Collection<Integer> latestActive(@PathVariable Lover lover) {
		List<Lover> lovers = loverService.latestActiveOnTheWall(
			lover,
			0,
			100
		).getContent();
		List<Integer> integers = new ArrayList<>();
		lovers.forEach(sucker -> {
			integers.add(sucker.getId());
		});
		return integers;
	}

	/**
	 * 暂时的最近活跃；实作完成后删除
	 *
	 * @param lover 用户号
	 * @return
	 */
	@GetMapping(path = "/{lover:\\d+}/latestActive.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Collection<Integer> latestRegistered(@PathVariable Lover lover) {
		List<Lover> lovers = loverService.latestRegisteredOnTheWall(
			lover,
			0,
			100
		).getContent();
		List<Integer> integers = new ArrayList<>();
		lovers.forEach(sucker -> {
			integers.add(sucker.getId());
		});
		return integers;
	}
}
