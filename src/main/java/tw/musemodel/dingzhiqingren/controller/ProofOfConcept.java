package tw.musemodel.dingzhiqingren.controller;

import com.jhlabs.image.OilFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
}
