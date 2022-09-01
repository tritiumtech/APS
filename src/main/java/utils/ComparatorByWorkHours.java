package utils;

import entities.Job;

import java.io.Serializable;
import java.util.Comparator;

public class ComparatorByWorkHours implements Comparator<Job>, Serializable {
    public int compare(Job t1, Job t2) {
        float t1Hours = t1.duration;
        float t2Hours = t2.duration;
        int res = Float.compare(t1Hours, t2Hours);

        if (res == 0) {
            return t1.deliveryDate.compareTo(t2.deliveryDate);
        } else
            return res;
    }
}
