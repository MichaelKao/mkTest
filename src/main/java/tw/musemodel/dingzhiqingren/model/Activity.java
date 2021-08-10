package tw.musemodel.dingzhiqingren.model;

import java.util.Date;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 动态记录
 *
 * @author m@musemodel.tw
 */
public class Activity implements Comparable<Activity> {

	private Lover initiative;

	private Lover passive;

	private Behavior behavior;

	private Date occurred;

	private Short points;

	private String greeting;

	private Date seen;

	private Date reply;

	private String sender;

	@Override
	public int compareTo(Activity other) {
		return occurred.compareTo(other.occurred);
	}

	public Activity() {
	}

	public Activity(Lover initiative, Lover passive, Behavior behavior,
		Date occurred, Short points, String greeting, Date seen, Date reply) {
		this.initiative = initiative;
		this.passive = passive;
		this.behavior = behavior;
		this.occurred = occurred;
		this.points = points;
		this.greeting = greeting;
		this.seen = seen;
		this.reply = reply;
	}

	/**
	 * 聊天歷史紀錄
	 *
	 * @param sender
	 * @param occurred
	 * @param greeting
	 * @param seen
	 */
	public Activity(String sender, Date occurred, String greeting, Date seen) {
		this.sender = sender;
		this.occurred = occurred;
		this.greeting = greeting;
		this.seen = seen;
	}

	public Lover getInitiative() {
		return initiative;
	}

	public void setInitiative(Lover initiative) {
		this.initiative = initiative;
	}

	public Lover getPassive() {
		return passive;
	}

	public void setPassive(Lover passive) {
		this.passive = passive;
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

	public Date getOccurred() {
		return occurred;
	}

	public void setOccurred(Date occurred) {
		this.occurred = occurred;
	}

	public Short getPoints() {
		return points;
	}

	public void setPoints(Short points) {
		this.points = points;
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public Date getSeen() {
		return seen;
	}

	public void setSeen(Date seen) {
		this.seen = seen;
	}

	public Date getReply() {
		return reply;
	}

	public void setReply(Date reply) {
		this.reply = reply;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
