package tw.musemodel.dingzhiqingren.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Activity;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;
import static tw.musemodel.dingzhiqingren.service.HistoryService.*;

/**
 * 服务层：聊天室
 *
 * @author m@musemodel.tw
 */
@Service
public class WebSocketService {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);

	private final static int MSG_TWELVE_HRS_TOLERANCE = 3;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LoverService loverService;

	public List<Activity> wholeHistoryMsgs(Lover male, Lover female) {

		List<History> mToFHistories = historyRepository.
			findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(male, female, BEHAVIOR_TALK);
		List<History> fToMhistories = historyRepository.
			findByInitiativeAndPassiveAndBehaviorOrderByOccurredDesc(female, male, BEHAVIOR_GREETING);
		List<Activity> wholeHistoryMsgs = new ArrayList<>();
		for (History history : mToFHistories) {
			Activity activity = new Activity(
				male.getIdentifier().toString(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		for (History history : fToMhistories) {
			Activity activity = new Activity(
				female.getIdentifier().toString(),
				history.getOccurred(),
				history.getGreeting(),
				history.getSeen()
			);
			wholeHistoryMsgs.add(activity);
		}
		Collections.sort(wholeHistoryMsgs);

		return wholeHistoryMsgs;
	}

	/**
	 * 12小時內男生限制發送訊息次數
	 *
	 * @param male
	 * @return
	 */
	public boolean withinRequiredLimit(Lover male, Lover female) {
		Date twelveHrsAgo = null;
		Date nowDate = null;
		Calendar twelveHrs = Calendar.getInstance();
		twelveHrs.add(Calendar.HOUR, -12);
		twelveHrsAgo = twelveHrs.getTime();
		nowDate = new Date();
		int dailyCount = historyRepository.countByInitiativeAndPassiveAndBehaviorAndOccurredBetween(
			male,
			female,
			BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
			twelveHrsAgo,
			nowDate
		);
		return (loverService.isVVIP(male) || loverService.isVIP(male)) && Objects.nonNull(dailyCount) && dailyCount < MSG_TWELVE_HRS_TOLERANCE;
	}
}
