package tw.musemodel.dingzhiqingren.controller;

import java.awt.geom.Point2D;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
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

	@GetMapping(path = "/angle.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String angle(@RequestParam Double x1, @RequestParam Double y1, @RequestParam Double x2, @RequestParam Double y2) {
		Point2D pointA = new Point2D.Double(x1, y1),
			pointB = new Point2D.Double(x2, y2);
		double radian = Math.atan2(
			(pointB.getY() - pointA.getY()),
			(pointB.getX() - pointA.getX())
		);
		double theta = radian * (180 / Math.PI);
		return new JSONObject().
			put("弧度", radian).
			put("角度", theta).
			toString();
	}
}
