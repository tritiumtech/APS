package utils;

import entities.Job;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class ComparatorByCriticalRatio implements Comparator<Job>, Serializable {
    @Override
    public int compare(Job t1, Job t2) {
        ZonedDateTime now = ZonedDateTime.now();

        ZonedDateTime t1EndDt = t1.calcEndDt(now, t1.duration);
        ZonedDateTime t2EndDt = t2.calcEndDt(now, t2.duration);

        float distance1 = t1.workTimeDifference(t1EndDt, t1.deliveryDate);
        float distance2 = t2.workTimeDifference(t2EndDt, t2.deliveryDate);

        float ratio1 = t1.duration == 0 ? distance1 / t1.duration : 0;
        float ratio2 = t2.duration == 0 ? distance1 / t2.duration : 0;

        return Float.compare(ratio1, ratio2);
    }
}
