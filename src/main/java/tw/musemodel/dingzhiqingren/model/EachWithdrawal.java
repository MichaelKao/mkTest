package tw.musemodel.dingzhiqingren.model;

import java.util.Date;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.WithdrawalRecord.WayOfWithdrawal;

/**
 * 提領紀錄
 *
 * @author m@musemodel.tw
 */
public class EachWithdrawal {

	private Lover honey;

	private Long points;

	private Boolean status;

	private WayOfWithdrawal way;

	private Date timestamp;

	public EachWithdrawal() {
	}

	public EachWithdrawal(Lover honey, Long points, Boolean status, WayOfWithdrawal way, Date timestamp) {
		this.honey = honey;
		this.points = points;
		this.status = status;
		this.way = way;
		this.timestamp = timestamp;
	}

	/**
	 * @return 甜心
	 */
	public Lover getHoney() {
		return honey;
	}

	/**
	 * @param honey 甜心
	 */
	public void setHoney(Lover honey) {
		this.honey = honey;
	}

	/**
	 * @return 總額
	 */
	public Long getPoints() {
		return points;
	}

	/**
	 * @param points 總額
	 */
	public void setPoints(Long points) {
		this.points = points;
	}

	/**
	 * @return 狀態
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status 狀態
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return 提領方式
	 */
	public WayOfWithdrawal getWay() {
		return way;
	}

	/**
	 * @param way 提領方式
	 */
	public void setWay(WayOfWithdrawal way) {
		this.way = way;
	}

	/**
	 * @return 時戳
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp 時戳
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
