package tw.musemodel.dingzhiqingren;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;

@Controller
@SpringBootApplication
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoverService loverService;

	/**
	 * 暂时的强改密码。
	 *
	 * @param lover 用户号
	 * @param shadow 密码
	 * @return 更新后的用户号
	 */
	@GetMapping(path = "/shadow/{lover:\\d+}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Secured({Servant.ROLE_ADMINISTRATOR})
	Lover passwd(@PathVariable Lover lover, @RequestParam String shadow) {
		lover.setShadow(passwordEncoder.encode(shadow));
		return loverService.saveLover(lover);
	}
}
