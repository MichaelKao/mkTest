package tw.musemodel.dingzhiqingren.model.comparator;

import java.util.Comparator;
import lombok.NoArgsConstructor;
import tw.musemodel.dingzhiqingren.model.Activity;

/**
 * 较新的动态记录优先排序
 *
 * @author p@musemodel.tw
 */
@NoArgsConstructor
public class ActivityOccurredComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity first, Activity second) {
		return first.getOccurred().compareTo(
			second.getOccurred()
		);
	}

	@Override
	public Comparator<Activity> reversed() {
		return Comparator.reverseOrder();
	}
}
