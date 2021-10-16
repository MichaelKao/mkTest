package tw.musemodel.dingzhiqingren.controller;

import com.jhlabs.image.OilFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;
import tw.musemodel.dingzhiqingren.specification.LoverSpecification;

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
	private Servant servant;

	@Autowired
	private HistoryRepository historyRepository;

	@GetMapping(path = "/isDevelopment", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String isDevelopment() {
		return Boolean.toString(servant.isDevelopment());
	}

	@GetMapping(path = "/isTesting", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String isTesting() {
		return Boolean.toString(servant.isTesting());
	}

	@GetMapping(path = "/oilPainting/{filename:\\d+}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	void oilPainting(@PathVariable String filename, @RequestParam(defaultValue = "10") int levels, @RequestParam(defaultValue = "10") int range, HttpServletResponse response) throws IOException {
		OilFilter oilFilter = new OilFilter();
		oilFilter.setLevels(levels);
		oilFilter.setRange(range);

		BufferedImage sourceBufferedImage = ImageIO.read(
			new ClassPathResource(
				String.format(
					"skeleton/%s.jpg",
					filename
				)
			).getInputStream()
		), targetBufferedImage = new BufferedImage(
			sourceBufferedImage.getWidth(),
			sourceBufferedImage.getHeight(),
			sourceBufferedImage.getType()
		);

		oilFilter.filter(
			sourceBufferedImage,
			targetBufferedImage
		);
		ImageIO.write(
			targetBufferedImage,
			"JPG",
			response.getOutputStream()
		);
	}

	@GetMapping(path = "/seen.asp", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Collection<History> seen(@RequestParam(defaultValue = "3", name = "init") int initiative, @RequestParam(defaultValue = "3", name = "pass") int passive) throws InterruptedException {
		Collection<History> histories = new ArrayList<>();

//		for (Lover mofo : loverService.fetchRandomly(initiative)) {
//			boolean gender = !mofo.getGender();
//			for (Lover sucker : loverService.fetchRandomly(passive, gender)) {
//				histories.add(historyRepository.saveAndFlush(
//					new History(
//						mofo,
//						sucker,
//						HistoryService.BEHAVIOR_PEEK,
//						new Date(
//							System.currentTimeMillis() - Servant.randomInteger(999)
//						)
//					)
//				));
//			}
//		}
		for (Lover mofo : loverService.fetchRandomEligibles(initiative)) {
			boolean gender = !mofo.getGender();
			for (Lover sucker : loverService.fetchRandomly(passive, gender)) {
				histories.add(historyRepository.saveAndFlush(
					new History(
						mofo,
						sucker,
						HistoryService.BEHAVIOR_PEEK,
						new Date(
							System.currentTimeMillis() - Servant.randomInteger(999)
						)
					)
				));
			}
		}

		return histories;
	}
}
