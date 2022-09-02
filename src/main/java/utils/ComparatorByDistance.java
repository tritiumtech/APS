package utils;

import entities.Job;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class ComparatorByDistance implements Comparator<Job>, Serializable {
	public int compare(Job t1, Job t2) {

		ZonedDateTime latestStartDt1 = t1.calcEndDt(t1.deliveryDt, -t1.duration);
		ZonedDateTime latestStartDt2 = t2.calcEndDt(t2.deliveryDt, -t2.duration);

		return latestStartDt1.compareTo(latestStartDt2);
	}
}
