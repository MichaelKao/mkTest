package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.File;
import java.io.IOException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;

/**
 * 服務層：AWS
 *
 * @author m@musemodel.tw
 */
@Service
public class AmazonWebServices {

	private final static Logger LOGGER = LoggerFactory.getLogger(AmazonWebServices.class);

	private static final String AWS_ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");

	private static final String AWS_SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");

	public static final AmazonS3 AMAZON_S3 = AmazonS3ClientBuilder.
		standard().
		withCredentials(new AWSStaticCredentialsProvider(
			new BasicAWSCredentials(
				AWS_ACCESS_KEY_ID,
				AWS_SECRET_ACCESS_KEY
			)
		)).withRegion(Regions.AP_SOUTHEAST_1).build();

	public static final String BUCKET_NAME = System.getenv("S3_BUCKET");

	private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");

	public JSONObject copyDefaultImageToLover(String name, Lover lover) {
		String folder = "profileImage/";

		String defaultImage = null;
		if (lover.getGender()) {
			defaultImage = "meKing.png";
		} else if (!lover.getGender()) {
			defaultImage = "meQueen.png";
		}
		CopyObjectResult copyObjectResult = AMAZON_S3.copyObject(new CopyObjectRequest(
			BUCKET_NAME,
			folder + defaultImage,
			BUCKET_NAME,
			folder + name
		));

		return new JavaScriptObjectNotation().
			withReason("Uploaded.").
			withResponse(true).
			withResult(copyObjectResult).
			toJSONObject();
	}

	public JSONObject uploadPhotoToS3Bucket(MultipartFile multipartFile, String fileName, String bucketName) throws IOException {
		File file = new File(TEMP_DIRECTORY, Long.toString(
			System.currentTimeMillis()
		));
			multipartFile.transferTo(file);
		PutObjectResult putObjectResult = AMAZON_S3.putObject(
			new PutObjectRequest(
				BUCKET_NAME + bucketName,
				fileName,
				file
			)
		);
		file.delete();

		return new JavaScriptObjectNotation().
			withReason("Uploaded.").
			withResponse(true).
			withResult(putObjectResult).
			toJSONObject();
	}

	public JSONObject deletePhotoFromS3Bucket(String folder, String fileName) {
		AMAZON_S3.deleteObject(new DeleteObjectRequest(
			String.format(
				"%s" + folder,
				BUCKET_NAME
			),
			fileName
		));

		return new JavaScriptObjectNotation().
			withReason("Deleted.").
			withResponse(true).
			toJSONObject();
	}
}
