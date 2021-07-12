package tw.musemodel.dingzhiqingren.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.service.HistoryService;
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

	private final static Logger LOGGER = LoggerFactory.getLogger(ProofOfConcept.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Servant servant;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@PostMapping(path = "/isLine.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String isLine(@RequestParam(name = "file") MultipartFile multipartFile, Locale locale) throws URISyntaxException {
		JavaScriptObjectNotation json = new JavaScriptObjectNotation();

		String anchor;
		try (InputStream inputStream = multipartFile.getInputStream()) {
			JSONObject jsonObject = loverService.qrCodeToString(
				inputStream,
				locale
			);
			anchor = jsonObject.getString("result");
			if (!jsonObject.getBoolean("response")) {
				return json.
					withReason(json.getReason()).
					withResponse(false).
					toString();
			}
		} catch (IOException ioException) {
			return json.
				withReason(ioException.getLocalizedMessage()).
				withResponse(false).
				toString();
		}

		URI uri = new URI(anchor);
		json.setResult(uri);

		String scheme = uri.getScheme();
		if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
			return json.
				withReason("lineFriendInvitation.wrongScheme").
				withResponse(false).
				toString();
		}

		if (uri.getHost().equalsIgnoreCase("line.me") || uri.getHost().equalsIgnoreCase("line.naver.jp")) {
			boolean response = uri.getPath().matches("^/ti/[gp]/\\S{10}$");
			json.setResponse(response);
			if (!response) {
				json.setReason("lineFriendInvitation.wrongPath");
			}
		} else if (uri.getHost().equalsIgnoreCase("lin.ee")) {
			boolean response = uri.getPath().matches("^/ti/[gp]/\\S{10}$");
			json.setResponse(response);
			if (!response) {
				json.setReason("lineFriendInvitation.wrongPath");
			}
		} else {
			json.
				withReason("lineFriendInvitation.wrongHost").
				setResponse(false);
		}

		return json.toString();
	}
}
