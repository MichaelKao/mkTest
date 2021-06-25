package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

	private static final String BUCKET_NAME = System.getenv("S3_BUCKET");

	private static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY_ID");

	private static final String SECRET_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");

	private static final String REGION = System.getenv("S3_REGION");

	private static final AmazonS3 AMAZON_S3 = AmazonS3ClientBuilder.
		standard().
		withCredentials(new AWSStaticCredentialsProvider(
			new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)
		)).withRegion(REGION).build();

	private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");

	public JSONObject copyDefaultImageToLover(String name) {
		String folder = "profileImage/";

		CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
			BUCKET_NAME,
			folder + "default_image.png",
			BUCKET_NAME,
			folder + name
		);
		AMAZON_S3.copyObject(copyObjectRequest);

		return new JavaScriptObjectNotation().
			withReason("Uploaded.").
			withResponse(true).
			toJSONObject();
	}

	public JSONObject uploadPhotoToS3Bucket(MultipartFile multipartFile, String fileName, String bucketName)
		throws IOException {

		File file = new File(TEMP_DIRECTORY, Long.toString(
			System.currentTimeMillis()
		));
		multipartFile.transferTo(file);
		AMAZON_S3.putObject(
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
			toJSONObject();
	}

	public JSONObject deletePhotoFromS3Bucket(String name) {

		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
			BUCKET_NAME + "/pictures",
			name
		);

		AMAZON_S3.deleteObject(deleteObjectRequest);

		return new JavaScriptObjectNotation().
			withReason("Deleted.").
			withResponse(true).
			toJSONObject();
	}
}
