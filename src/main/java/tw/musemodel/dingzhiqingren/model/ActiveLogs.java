package tw.musemodel.dingzhiqingren.model;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * @author m@musemodel.tw
 */
public class ActiveLogs implements Comparable<ActiveLogs> {

	private Lover initiative;

	private Lover passive;

	private Behavior behavior;

	private Date occurred;

	private Short points;

	private String greeting;

	@Override
	public int compareTo(ActiveLogs other) {
		return occurred.compareTo(other.occurred);
	}

	@NotEmpty
	@NotNull
	private Date read;

	public ActiveLogs() {
	}

	public ActiveLogs(Lover initiative, Lover passive, Behavior behavior, Date occurred, Short points, String greeting, Date read) {
		this.initiative = initiative;
		this.passive = passive;
		this.behavior = behavior;
		this.occurred = occurred;
		this.points = points;
		this.greeting = greeting;
		this.read = read;
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

	public Date getRead() {
		return read;
	}

	public void setRead(Date read) {
		this.read = read;
	}
}
