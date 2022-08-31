package utils;

import com.base.sbc.aps.entity.TaskMatter;
import com.base.sbc.config.utils.DateUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class ComparatorByDistance implements Comparator<TaskMatter>, Serializable {
	public int compare(TaskMatter t1, TaskMatter t2) {

		Date latestStartDt1 = DateUtils.addMinutes(t1.getGiveDate(), -t1.getWorkMinutes());
		Date latestStartDt2 =DateUtils.addMinutes(t2.getGiveDate(), -t2.getWorkMinutes());

		return latestStartDt1.compareTo(latestStartDt2);
	}
}
