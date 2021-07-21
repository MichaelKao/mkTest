package tw.musemodel.dingzhiqingren.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.Location;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
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

	private static final int MAX_CHARACTERS = 1000;

	@Autowired
	private LoverRepository loverRepository;

	@PostMapping(path = "/upload")
	@ResponseBody
	void uploadProfileImage(@RequestParam("file") MultipartFile multipartFile) throws SAXException, IOException, ParserConfigurationException {
		LOGGER.debug("[上傳]準備上傳伺服器。{}", multipartFile);
		File file = new File(TEMP_DIRECTORY, Long.toString(
			System.currentTimeMillis()
		));
		multipartFile.transferTo(file);
		LOGGER.debug("[上傳]上傳伺服器成功，準備上傳S3。{}", file);

		PutObjectResult putObjectResult = AMAZON_S3.putObject(
			new PutObjectRequest(
				BUCKET_NAME,
				"test",
				file
			)
		);
		LOGGER.debug("[上傳]上傳S3成功\n{}", putObjectResult);
		file.delete();
		LOGGER.debug("[上傳]刪除伺服器檔案成功");
	}

	@PostMapping(path = "/uploadS3")
	@ResponseBody
	void uploadDirectly(@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
		LOGGER.debug("[上傳S3]準備上傳S3。{}", multipartFile);
		ObjectMetadata data = new ObjectMetadata();
		data.setContentType(multipartFile.getContentType());
		data.setContentLength(multipartFile.getSize());
		LOGGER.debug("[上傳S3]設定完ObjectMetadata。{}", data);
		AMAZON_S3.putObject(BUCKET_NAME, "testS3", multipartFile.getInputStream(), data);
		LOGGER.debug("[上傳S3]完成上傳。");
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

	@GetMapping(path = "/area.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Set<Lover> area(@RequestParam List<Location> locations) {
		List<Lover> lovers = new ArrayList<>();
		for (Location location : locations) {
			lovers.addAll(
				loverRepository.findAll(
					LoverSpecification.appearedAreas(
						true,
						location
					)
				)
			);
		}
		return new HashSet<>(lovers);
	}
}
