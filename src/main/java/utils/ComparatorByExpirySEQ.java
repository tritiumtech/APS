package utils;

import entities.Job;

import java.io.Serializable;
import java.util.Comparator;

public class ComparatorByExpirySEQ implements Comparator<Job>, Serializable {

    @Override
    public int compare(Job t1, Job t2) {
        int res = t2.deliveryDt.compareTo(t1.deliveryDt);
        if (res == 0) {
            return Float.compare(t2.duration, t1.duration);
        } else
            return res;
    }

}
