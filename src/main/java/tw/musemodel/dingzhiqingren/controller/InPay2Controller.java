package tw.musemodel.dingzhiqingren.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.com.ecpay.ecpg.OrderResultResponse;
import tw.com.ecpay.ecpg.ReturnResponse;
import tw.musemodel.dingzhiqingren.service.Inpay2Service;

/**
 * 控制器：站内付 2.0
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/inpay2")
public class InPay2Controller {

	private final static Logger LOGGER = LoggerFactory.getLogger(InPay2Controller.class);

	private final static JsonMapper JSON_MAPPER = new JsonMapper();

	private static final String OKAY = "1|OK";

	@Autowired
	private Inpay2Service inpay2Service;

	/**
	 * 建立交易。
	 *
	 * @return @throws Exception
	 */
	@PostMapping(path = "/createPayment/{payToken}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String createPayment(@PathVariable final String payToken, final HttpSession session) {
		return inpay2Service.createPayment(payToken, session);
	}

	/**
	 * 取得厂商验证码。
	 *
	 * @return 厂商验证码 JSON 对象
	 * @throws Exception
	 */
	@PostMapping(path = "/getTokenByTrade.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String getTokenByTrade(final HttpSession session, Locale locale) throws JsonProcessingException {
		LOGGER.debug(
			"語系：{}",
			locale
		);
		return inpay2Service.getTokenByTrade(session);
	}

	/**
	 * 绿界以幕前方式传送付款结果。
	 *
	 * @param resultData 付款结果
	 * @return 给绿界的响应
	 */
	@PostMapping(path = "/orderResult.asp", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	OrderResultResponse.Data handleOrderResult(@RequestParam("ResultData") String resultData) throws Exception {
		LOGGER.info(
			String.format(
				"绿界以幕前方式传送付款结果。\n%s#handleOrderResult(\n\tString resultData = {}\n);",
				getClass().getName()
			),
			resultData
		);
		OrderResultResponse orderResultResponse = JSON_MAPPER.readValue(
			resultData,
			OrderResultResponse.class
		);
		OrderResultResponse.Data data = JSON_MAPPER.readValue(
			inpay2Service.decrypt(orderResultResponse.getData()),
			OrderResultResponse.Data.class
		);
		return data;
	}

	/**
	 * 绿界以幕后方式传送付款结果。
	 *
	 * @param requestBody 付款结果
	 * @return 给绿界的响应
	 */
	@PostMapping(path = "/return.asp")
	@ResponseBody
	ResponseEntity<String> handleReturn(@RequestBody String requestBody) throws Exception {
		ReturnResponse returnResponse = JSON_MAPPER.readValue(
			requestBody,
			ReturnResponse.class
		);
		ReturnResponse.Data data = JSON_MAPPER.readValue(
			inpay2Service.decrypt(returnResponse.getData()),
			ReturnResponse.Data.class
		);
		LOGGER.info(
			String.format(
				"绿界以幕后方式传送付款结果。\n%s#handleReturn(\n\tString requestBody = {}\n);\n{}",
				getClass().getName()
			),
			requestBody,
			data
		);
		return new ResponseEntity<>(
			OKAY,
			HttpStatus.OK
		);
	}

	@GetMapping(path = "/orderResult.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String orderResult() throws Exception {
		return inpay2Service.decrypt("O29c4hMFtVUU3qu5/+w0jGJUI0QIIw8PR51W4dYfKGdZIMqsYZtyxAQIS6FWH6cvuNDvEeOB/1FNC1UfCF9Ln5pwdDGLRQzAjMnLozuMnDOUcxoHVxqNSiQjwStb6VP5PD9fMsDVFHRVvTtY9EMTEyRie4fubM3iIW4aOx9ZOGyMRnm1PLCxfJ0yUzbOL0VHP5Zb1BC/M+POHAAxrxWv0aRE432hQHmuLmNG4LsIW0wIJdeVCjOo2uKjBYzOyZ2EhJnNPiBuxQcT6DTZLOgEQfIpdaMKLIRPuWdTN87alwD5qkyv89eh4aQG0H/fsG+nm1cVUuyON+qe8y2aw3Qi8D0JbTWbpAPhxG6PlDQ+hnRNKyJ7vGGQj5tOMEtjCd4fMjIyTvQuZY755iO8zMFkH2F54jTtnioM1a9kIoQBissfY5H0VtZ0WgjJPmf1LqdnqfcxhZ4O54p5ITf6iDcxlzHI2WVCGCVZjkOaWAjxB42Kbt6rdn02w+wC/xeK2ozLFVl8d8yZwktZG1TBy+6XccKfFrGwbFAXUJbQYFJ/VAUNB28S1BHvDPxAoRTGY/f0M+fcBFd/2H8Fy3wj0NpasB5J3/OIg6ZFwCXxa8Yp+hQZ7djicmaog8vPBhUUvfIEBmB1IuV9FT6DMChOxGKrGKN9BQRxPhZim/2myHbaLkP6+Lr7dMoE9iuHUeX44FBS21ElSmfN96NXza77rvswGb98y1J+r9SP6C5/hJBTlXzrkz440wFx8eMyVSHqZwEM/5CjAULRtyUQbI058up+nstXKM4SsG27pfsG/8Xst9uv1JX1eWiI+bhMJDz+2NOJi57aj/c0mW/6Q43GWWRHWzt27pv9RrlQPcjSdzpTk9yZZv2vCTxqHxo/fcz+RAn7hjU058QM1NkHQEH/U/1/5yivtPLieNtmOKjTm7jKuW96F90lnKUVUB88YxCueGshiaZpJXvc1Is27LFj6GV5HlnLR7e/v1N6IdTb0Jp7iA9FyXaQSMuA2v64Dg/PIm3VMbF6jDruTnBvVb3AcmdfR4fCaKPEoBRBETeORjkvCikZZsmuBc4mYK0HwJx2pPb6NrrYU+8qiLHm3XsTvrkZG3JaXLwgs42IQPkXk1OM9XNWS4S0SZ4+T3fg4LMXgQ81VT2pGJnvoSpBTUbsiO4srA==");
	}

	@GetMapping(path = "/return.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String rEtUrn() throws Exception {
		return inpay2Service.decrypt("rl1RbEIgjjHBLm8k4Qga6uK0pu4A6DIHP5AbJ910VpXGvEybHqN1wRbrGNU5Xnzto8xXPsdtrosjTjx8r3Uwcd7Tj+jhKAFhiIsAMB4jqFwafMxcxpkgu5z1wqeXtYBjQhtQiFWIu7YIarakFDLU6DIQcD+nkYvLylkgrRPvIOTHHzhqNc5Aode/7PY/+xKjbIEZ7XfTRBfRkvFcsBZmW60KJCp/rfyCed3KztY8VC+jjRmBhGA8z4ikUyP4qPUaLXy8tiQh8ptKRYcCdlYFU//Q2mTHPzYLXXL9y3xzZ+O1+1Egt7N/bqlLcpnb32XvFNfUG47dXCe1bCeSg1rXiO07vJQSTC0t6Jx5zclbGOKsKbTV3UqMe25RODvbRveI2aenkZXJlYhUX2owuU7sHK0K9sEY4g5ZCWUTNtHi16ToOS1uKyAEeRKawYgpDHXZmmWiAYuVjSR+d+h2lktw4Hp1pjuUj4HsIzy66fQ/uemmNKXVgafV998gFNT9+/OtR0yjrmLZBeufHmHBYQ/bjE+png0gxbhM4IdcgZV8P+BSh4Zy8uIDVwmdRF2knvazn3my5Y0yEK3q8sXoMMD8amc601BdoUCfncCgkkmJtGav9RKlxxTVcCWJOKxSvnquMjquijwkaNjY08jkWfbmmDUB9hMQnlWp6IcCf8VJXAB7x7PbI1S2tdpNFXgVRoDmQr8MIW5zSTxVJAay+CDr63ak5vRnZ7nxx+SzoWKaxVYCPe73mP36BTwoi5jePQMTWXsO3U5qlXn//Z1YTpG1WkpWJH4bYCvup8QRxthCLCBX47J5gH4xkMHM5qD7apP1zyHQmSwjnh+fTnW6arDPA2zfRBPOP0J024Qc3F7Z7nsQfakBoHLJxWxCQ9yq+4YLptrfdE2V+5p04jKHLsMQ4PpnZMX2t45E7y8e5TJ3wH0F4mwWRXhGj85mXxHb1lNOPB3YAOQbfDzzCt7c/v/fvpi9N2ctaDfMTRwFayuPAUrEzrkqUpo+BuMECMsPe1x+OhzWRILyBCjLYpPxP4JzwCkOgDYbLgolgIx1+V0oURA3ZEe2P3F/prbKSxYBKtksO1C7qb2lo4E3hL384Z48S9rNcUd/eevpUhi2ZFIKFlcz08N+vMXjtWi2JK0SVFRUbi7KbwLKvNrwxiUibr2nIo2trstAuqPHgSHQEtZ1Jw5iIwjodOE6JQYAFeGGaoWjzUzmTkaCzno4PVAMlloEdbuUihPZdqqh4cffxeaejtBYA0I1M92OoADTdvjrRP7c2j+tc+BXso/lxWmWAfx1/RaccfFa8VHRO2uNz9aJdb07DO2ZGHItVgfmbCIB6Dr65rM4JeFKdWLf+XKV0jGniapbrBuq1TCXY+DfEjmWOdFYEuFMAGzPV6Wgl2JUMZ5jCZGhM6a9zxyyLzrsHPm7BajOKZ3z/1ZvXYWNN8Pz5DX/qYAdXwGhlkY7yhMVV2kkFcCFvFw9M8xYEU5koWW/Hg==");
	}
}
