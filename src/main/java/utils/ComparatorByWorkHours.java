package utils;

import com.base.sbc.aps.entity.TaskMatter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

public class ComparatorByWorkHours implements Comparator<TaskMatter>, Serializable {
    public int compare(TaskMatter t1, TaskMatter t2) {
        BigDecimal t1Hours = t1.getWorkHours();
        BigDecimal t2Hours = t2.getWorkHours();
        int res = t1Hours.compareTo(t2Hours);

        if (res == 0) {
            return t1.getGiveDate().compareTo(t2.getGiveDate());
        } else
            return res;
    }
}
