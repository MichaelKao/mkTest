package tw.musemodel.dingzhiqingren.controller;

import com.jhlabs.image.OilFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Activity;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.service.DashboardService.FinancialStatementOfDepositAndWithdrawal;
import tw.musemodel.dingzhiqingren.service.HistoryService;
import tw.musemodel.dingzhiqingren.service.LineMessagingService;
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
	private JdbcTemplate jdbcTemplate;

	@Value("classpath:sql/男仕贵宾会员.sql")
	private Resource vvipOnTheWall;

	@Value("classpath:sql/安心认证.sql")
	private Resource reliefOnTheWall;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private LoverService loverService;

	@Autowired
	private Servant servant;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LoverRepository loverRepository;

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

	@GetMapping(path = "/activities/{lover:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<Activity> activities(Lover lover) {
		return historyService.activities(
			lover,
			PageRequest.of(0, 10)
		);
	}

	@GetMapping(path = "/inbox2/{lover:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<History> latestPageableConversations(@PathVariable Lover lover, @RequestParam(defaultValue = "1") int p, @RequestParam(defaultValue = "10") int s) {
		return historyService.latestPageableConversations(
			lover,
			PageRequest.of(
				p < 1 ? 0 : p - 1,
				s
			)
		);
	}

	@GetMapping(path = "/chat/{me:\\d+}/{someone:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<History> latestPageableConversations(@PathVariable Lover me, @PathVariable Lover someone, @RequestParam(defaultValue = "1") int p, @RequestParam(defaultValue = "10") int s) {
		return historyService.latestPageableConversations(
			me,
			someone,
			PageRequest.of(
				p < 1 ? 0 : p - 1,
				s
			)
		);
	}

	@GetMapping(path = "/relief/{lover:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	void relief(
		@PathVariable Lover lover,
		@RequestParam(defaultValue = "1") int p,
		@RequestParam(defaultValue = "10") int s,
		@RequestParam(defaultValue = "10") int times
	) throws IOException {
		int total = 0;

		for (int i = 0; i < times; i++) {
			long since = System.currentTimeMillis();
			loverRepository.findAll(
				LoverSpecification.relievingOnTheWall(
					lover,
					new HashSet<>(
						loverService.getExceptions(lover)
					)
				),
				PageRequest.of(
					p < 1 ? 0 : p - 1,
					s
				)
			);
			total += System.currentTimeMillis() - since;
		}
		int byLibrary = total / times;

		total = 0;
		for (int i = 0; i < times; i++) {
			long since = System.currentTimeMillis();
			loverRepository.findByIdIn(
				jdbcTemplate.query(
					FileCopyUtils.copyToString(new InputStreamReader(
						reliefOnTheWall.getInputStream(),
						Servant.UTF_8
					)),
					(ps) -> {
						ps.setInt(1, lover.getId());
						ps.setInt(2, lover.getId());
					},
					(resultSet, rowNum) -> resultSet.getInt("id")
				),
				PageRequest.of(
					p < 1 ? 0 : p - 1,
					s
				)
			);
			total += System.currentTimeMillis() - since;
		}
		int byHand = total / times;

		LineMessagingService.notifyDev(
			Arrays.asList(new String[]{LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_FIRST}),
			String.format(
				"重复 %d 次安心%n懒人：平均 %d 毫秒%n手动：平均 %d 毫秒",
				times,
				byLibrary,
				byHand
			)
		);
	}

	@GetMapping(path = "/vvip/{lover:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	void vvip(
		@PathVariable Lover lover,
		@RequestParam(defaultValue = "1") int p,
		@RequestParam(defaultValue = "10") int s,
		@RequestParam(defaultValue = "10") int times
	) throws IOException {
		int total = 0;

		for (int i = 0; i < times; i++) {
			long since = System.currentTimeMillis();
			loverRepository.findAll(
				LoverSpecification.vipOnTheWall(
					lover,
					new HashSet<>(
						loverService.getExceptions(lover)
					)
				),
				PageRequest.of(
					p < 1 ? 0 : p - 1,
					s
				)
			);
			total += System.currentTimeMillis() - since;
		}
		int byLibrary = total / times;

		total = 0;
		for (int i = 0; i < times; i++) {
			long since = System.currentTimeMillis();
			loverRepository.findByIdIn(
				jdbcTemplate.query(
					FileCopyUtils.copyToString(new InputStreamReader(
						vvipOnTheWall.getInputStream(),
						Servant.UTF_8
					)),
					(ps) -> {
						ps.setInt(1, lover.getId());
						ps.setInt(2, lover.getId());
					},
					(resultSet, rowNum) -> resultSet.getInt("id")
				),
				PageRequest.of(
					p < 1 ? 0 : p - 1,
					s
				)
			);
			total += System.currentTimeMillis() - since;
		}
		int byHand = total / times;

		LineMessagingService.notifyDev(
			Arrays.asList(new String[]{LineMessagingService.LINE_NOTIFY_ACCESS_TOKEN_FIRST}),
			String.format(
				"重复 %d 次贵宾男士%n懒人：平均 %d 毫秒%n人工：平均 %d 毫秒",
				times,
				byLibrary,
				byHand
			)
		);
	}

	@GetMapping(path = "/testStop.json/{email}/{lastFourDigits}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String stopTest(@PathVariable String email, @PathVariable String lastFourDigits) {
		Lover me = loverService.loadByUsername(
			"886987911993"
		);
		return loverService.stopRecurring(me, email, lastFourDigits, Locale.CHINESE);

	}

	@PostMapping(path = "/checkPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String checkPassword(
		@RequestParam String login,
		@RequestParam String shadow,
		Locale locale
	) {
		//確認帳號是否存在
		return loverService.checkPassword(login, shadow, locale).toString();
	}

	@GetMapping(path = "/financialStatementOfDepositAndWithdrawal.xls")
	void financialStatementOfDepositAndWithdrawal(HttpServletResponse response) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row firstRow = sheet.createRow(0);
		firstRow.createCell(0, CellType.STRING).setCellValue("暱稱");
		firstRow.createCell(1, CellType.STRING).setCellValue("帳號");
		firstRow.createCell(2, CellType.STRING).setCellValue("日期時間");
		firstRow.createCell(3, CellType.STRING).setCellValue("儲值");
		firstRow.createCell(4, CellType.STRING).setCellValue("提領");
		sheet.createFreezePane(0, 1);

		CellStyle cellStyleDateTime = workbook.createCellStyle();
		cellStyleDateTime.setDataFormat(workbook.
			getCreationHelper().
			createDataFormat().
			getFormat("yyyy/m/d hh:mm:ss")
		);//格式化时戳(年月日时分秒)

		int rowNumber = 1;
		for (FinancialStatementOfDepositAndWithdrawal financialStatement : historyService.financialStatementOfDepositAndWithdrawal()) {
			Row row = sheet.createRow(rowNumber);

			//昵称
			row.createCell(
				0,
				CellType.STRING
			).setCellValue(
				financialStatement.getNickname()
			);

			//女神主键
			row.createCell(
				1,
				CellType.STRING
			).setCellValue(
				financialStatement.getLogin()
			);

			//时戳
			Cell cell = row.createCell(
				2,
				CellType.STRING
			);
			cell.setCellStyle(cellStyleDateTime);
			cell.setCellValue(
				DateTimeFormatter.
					ofPattern("yyyy-MM-dd HH:mm:ss").
					format(
						Servant.toTaipeiZonedDateTime(
							financialStatement.getTimestamp()
						)
					)
			);

			//储值
			Short deposit = financialStatement.getDeposit();
			if (Objects.nonNull(deposit)) {
				row.createCell(
					3,
					CellType.STRING
				).setCellValue(
					deposit.toString()
				);
			}

			//提领
			Short withdrawal = financialStatement.getWithdrawal();
			if (Objects.nonNull(withdrawal)) {
				row.createCell(
					4,
					CellType.STRING
				).setCellValue(
					withdrawal.toString()
				);
			}

			++rowNumber;
		}//for

		OutputStream outputStream = response.getOutputStream();
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader(
			"Content-Disposition",
			String.format(
				"attachment; filename=\"YM_Financial@%s.xls\"",
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()))
			)
		);
		workbook.write(outputStream);
		outputStream.close();

		workbook.close();
	}
}
