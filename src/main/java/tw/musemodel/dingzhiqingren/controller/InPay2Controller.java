package tw.musemodel.dingzhiqingren.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/inpay2")
public class InPay2Controller {

	private final static Logger LOGGER = LoggerFactory.getLogger(InPay2Controller.class);

	@GetMapping(path = "/aes128cbc", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	String aes128cbc() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String key = "A123456789012345";
		StringBuilder keyStringBuilder = new StringBuilder(16);
		keyStringBuilder.append(key);
		while (keyStringBuilder.length() < 16) {
			keyStringBuilder.append("0");
		}
		if (keyStringBuilder.length() > 16) {
			keyStringBuilder.setLength(16);
		}
		LOGGER.debug(
			"ＫＥＹ\n\n{}\n",
			keyStringBuilder.toString().getBytes(
				StandardCharsets.UTF_8.toString()
			)
		);
		SecretKeySpec secretKeySpec = new SecretKeySpec(
			keyStringBuilder.toString().getBytes(
				StandardCharsets.UTF_8.toString()
			),
			"AES"
		);

		String iv = "B123456789012345";
		StringBuilder ivStringBuilder = new StringBuilder(16);
		ivStringBuilder.append(iv);
		while (ivStringBuilder.length() < 16) {
			ivStringBuilder.append("0");
		}
		if (ivStringBuilder.length() > 16) {
			ivStringBuilder.setLength(16);
		}
		LOGGER.debug(
			"ＩＶ\n\n{}\n",
			ivStringBuilder.toString().getBytes(
				StandardCharsets.UTF_8.toString()
			)
		);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(
			ivStringBuilder.toString().getBytes(
				StandardCharsets.UTF_8.toString()
			)
		);

		String data = "{\"Name\":\"Test\",\"ID\":\"A123456789\"}";
		String urlEncoded = URLEncoder.encode(
			data,
			StandardCharsets.UTF_8.toString()
		);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(
			Cipher.ENCRYPT_MODE,
			secretKeySpec,
			ivParameterSpec
		);

		return String.format("%s\n%s\n%s\n%s",
			keyStringBuilder.toString(),
			ivStringBuilder.toString(),
			Arrays.toString(cipher.doFinal(urlEncoded.getBytes(
				StandardCharsets.UTF_8.toString()
			))),
			Base64.getEncoder().encodeToString(cipher.doFinal(
				urlEncoded.getBytes(
					StandardCharsets.UTF_8.toString()
				)
			))
		);
	}
}
