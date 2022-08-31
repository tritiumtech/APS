package utils;
import entities.Job;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public class ComparatorByCriticalRatio implements Comparator<Job>, Serializable {
    @Override
    public int compare(Job t1, Job t2) {
        Date now = new Date();

        Date t1EndDt = WorkCalendar.addWorkMinutes(now, t1.getWorkMinutes());
        Date t2EndDt = WorkCalendar.addWorkMinutes(now, t2.getWorkMinutes());

        float distance1 = WorkCalendar.hoursDifference(t1EndDt, t1.getGiveDate());
        float distance2 = WorkCalendar.hoursDifference(t2EndDt, t2.getGiveDate());

        BigDecimal ratio1 = BigDecimalUtil.div(new BigDecimal(distance1), t1.getWorkHours());
        BigDecimal ratio2 = BigDecimalUtil.div(new BigDecimal(distance2), t2.getWorkHours());

        return ratio1.compareTo(ratio2);
    }
}
