package tw.musemodel.dingzhiqingren.service;

import java.util.Objects;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.musemodel.dingzhiqingren.entity.History;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.HistoryRepository;

/**
 * 服务层：历程
 *
 * @author p@musemodel.tw
 */
@Service
public class HistoryService {

	private final static Logger LOGGER = LoggerFactory.getLogger(HistoryService.class);

	@Autowired
	private HistoryRepository historyRepository;

	/**
	 * 历程：充值行为
	 */
	public static final Behavior BEHAVIOR_CHARGED = Behavior.CHU_ZHI;

	/**
	 * 历程：车马费(男对女)行为
	 */
	public static final Behavior BEHAVIOR_FARE = Behavior.CHE_MA_FEI;

	/**
	 * 历程：给我赖(男对女)行为
	 */
	public static final Behavior BEHAVIOR_GIMME_YOUR_LINE_INVITATION = Behavior.JI_WO_LAI;

	/**
	 * 历程：打招呼(女对男)行为
	 */
	public static final Behavior BEHAVIOR_GREETING = Behavior.DA_ZHAO_HU;

	/**
	 * 历程：给你赖(女对男)行为
	 */
	public static final Behavior BEHAVIOR_INVITE_ME_AS_LINE_FRIEND = Behavior.JI_NI_LAI;

	/**
	 * 历程：月费行为
	 */
	public static final Behavior BEHAVIOR_MONTHLY_CHARGED = Behavior.YUE_FEI;

	/**
	 * 历程：看过我行为
	 */
	public static final Behavior BEHAVIOR_PEEK = Behavior.KAN_GUO_WO;

	/**
	 * 车马费(男对女)
	 *
	 * @param initiative 男生
	 * @param passive 女生
	 * @param points 点数
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject fare(Lover initiative, Lover passive, short points) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("gimmeYourLineInvitation.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("gimmeYourLineInvitation.passiveMustntBeNull");
		}
		if (Objects.equals(initiative.getGender(), false)) {
			throw new RuntimeException("gimmeYourLineInvitation.initiativeMustBeMale");
		}
		if (Objects.equals(passive.getGender(), true)) {
			throw new RuntimeException("gimmeYourLineInvitation.passiveMustBeFemale");
		}
		//TODO:	男生剩余点数够不够？扣除点数！
		History history = new History(
			initiative,
			passive,
			BEHAVIOR_FARE,
			points
		);
		history = historyRepository.saveAndFlush(history);
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history).
			toJSONObject();
	}

	/**
	 * 给我赖(男对女)
	 *
	 * @param initiative 男生
	 * @param passive 女生
	 * @param greetingMessage 招呼语
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject gimmeYourLineInvitation(Lover initiative, Lover passive, String greetingMessage) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("gimmeYourLineInvitation.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("gimmeYourLineInvitation.passiveMustntBeNull");
		}
		if (Objects.equals(initiative.getGender(), false)) {
			throw new RuntimeException("gimmeYourLineInvitation.initiativeMustBeMale");
		}
		if (Objects.equals(passive.getGender(), true)) {
			throw new RuntimeException("gimmeYourLineInvitation.passiveMustBeFemale");
		}
		//TODO:	男生剩余点数够不够？扣除点数！
		History history = new History(
			initiative,
			passive,
			BEHAVIOR_GIMME_YOUR_LINE_INVITATION,
			(short) 0
		);
		greetingMessage = Objects.isNull(greetingMessage) || greetingMessage.isBlank() ? initiative.getGreeting() : greetingMessage;
		history.setGreeting(
			Objects.isNull(greetingMessage) || greetingMessage.isBlank() ? null : greetingMessage.trim()
		);
		history = historyRepository.saveAndFlush(history);
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history).
			toJSONObject();
	}

	/**
	 * 打招呼(女对男)
	 *
	 * @param initiative 女生
	 * @param passive 男生
	 * @param greetingMessage 招呼语
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject greet(Lover initiative, Lover passive, String greetingMessage) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("greet.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("greet.passiveMustntBeNull");
		}
		if (Objects.equals(initiative.getGender(), true)) {
			throw new RuntimeException("greet.initiativeMustBeFemale");
		}
		if (Objects.equals(passive.getGender(), false)) {
			throw new RuntimeException("greet.passiveMustBeMale");
		}

		History history = new History(
			initiative,
			passive,
			BEHAVIOR_GREETING
		);
		greetingMessage = Objects.isNull(greetingMessage) || greetingMessage.isBlank() ? initiative.getGreeting() : greetingMessage;
		history.setGreeting(
			Objects.isNull(greetingMessage) || greetingMessage.isBlank() ? null : greetingMessage.trim()
		);
		history = historyRepository.saveAndFlush(history);
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history).
			toJSONObject();
	}

	/**
	 * 给你赖(女对男)
	 *
	 * @param initiative 女生
	 * @param passive 男生
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject inviteMeAsLineFriend(Lover initiative, Lover passive) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("inviteMeAsLineFriend.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("inviteMeAsLineFriend.passiveMustntBeNull");
		}
		if (Objects.equals(initiative.getGender(), true)) {
			throw new RuntimeException("inviteMeAsLineFriend.initiativeMustBeFemale");
		}
		if (Objects.equals(passive.getGender(), false)) {
			throw new RuntimeException("inviteMeAsLineFriend.passiveMustBeMale");
		}
		//TODO:	男生有要求过吗？女生已给过吗？
		String inviteMeAsLineFriend = initiative.getInviteMeAsLineFriend();
		if (Objects.isNull(inviteMeAsLineFriend) || inviteMeAsLineFriend.isBlank()) {
			throw new RuntimeException("inviteMeAsLineFriend.mustntBeNull");
		}

		History history = new History(
			initiative,
			passive,
			BEHAVIOR_INVITE_ME_AS_LINE_FRIEND
		);
		history.setGreeting(inviteMeAsLineFriend);
		history = historyRepository.saveAndFlush(history);
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history).
			toJSONObject();
	}

	/**
	 * 看过我
	 *
	 * @param initiative 谁看了
	 * @param passive 看了谁
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject peek(Lover initiative, Lover passive) {
		if (Objects.isNull(initiative)) {
			throw new IllegalArgumentException("peek.initiativeMustntBeNull");
		}
		if (Objects.isNull(passive)) {
			throw new IllegalArgumentException("peek.passiveMustntBeNull");
		}
		if (Objects.equals(initiative, passive)) {
			throw new RuntimeException("peek.mustBeDifferent");
		}
		if (Objects.equals(initiative.getGender(), passive.getGender())) {
			throw new RuntimeException("peek.mustBeStraight");
		}

		History history = historyRepository.saveAndFlush(new History(
			initiative,
			passive,
			BEHAVIOR_PEEK
		));
		return new JavaScriptObjectNotation().
			withResponse(true).
			withResult(history).
			toJSONObject();
	}
}
