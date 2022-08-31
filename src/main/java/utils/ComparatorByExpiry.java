package utils;

import com.base.sbc.aps.entity.TaskMatter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

public class ComparatorByExpiry implements Comparator<TaskMatter>, Serializable {

	@Override
	public int compare(TaskMatter t1, TaskMatter t2) {
		int res = t1.getGiveDate().compareTo(t2.getGiveDate());
		BigDecimal t1Hours = t1.getWorkHours();
		BigDecimal t2Hours = t2.getWorkHours();
		if (res == 0) {
			return t1Hours.compareTo(t2Hours);
		} else
			return res;
	}

}
