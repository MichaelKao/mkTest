package tw.musemodel.dingzhiqingren.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tw.musemodel.dingzhiqingren.service.AmazonWebServices;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	private AmazonWebServices amazonWebServices;

	@Autowired
	private Servant servant;

	private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");

	private static final String AWS_ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");

	private static final String AWS_SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");

	private static final AmazonS3 AMAZON_S3 = AmazonS3ClientBuilder.
		standard().
		withCredentials(new AWSStaticCredentialsProvider(
			new BasicAWSCredentials(
				AWS_ACCESS_KEY_ID,
				AWS_SECRET_ACCESS_KEY
			)
		)).withRegion(Regions.AP_SOUTHEAST_1).build();

	private static final String BUCKET_NAME = System.getenv("S3_BUCKET");

	private final static int MAX_CHARACTERS = 1000;

	@PostMapping(path = "/upload")
	@ResponseBody
	ModelAndView uploadProfileImage(@RequestParam("file") MultipartFile multipartFile, Locale locale) throws SAXException, IOException, ParserConfigurationException {

		File file = new File(TEMP_DIRECTORY, Long.toString(
			System.currentTimeMillis()
		));
		multipartFile.transferTo(file);
		LOGGER.debug("[上傳]上傳伺服器成功，準備上傳S3。");

		PutObjectResult putObjectResult = AMAZON_S3.putObject(
			new PutObjectRequest(
				BUCKET_NAME + "/profileImage",
				"test",
				file
			)
		);
		LOGGER.debug("[上傳]上傳S3成功\n{}", putObjectResult);
		file.delete();
		LOGGER.debug("[上傳]刪除伺服器檔案成功");

		Document document = servant.parseDocument();
		Element documentElement = document.getDocumentElement();

		ModelAndView modelAndView = new ModelAndView("upload");
		modelAndView.
			getModelMap().
			addAttribute(document);
		LOGGER.debug("[上傳]上傳{}", modelAndView);
		return modelAndView;
	}

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
}
