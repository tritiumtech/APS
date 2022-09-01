package utils;

import entities.Job;

import java.io.Serializable;
import java.util.Comparator;

public class ComparatorByExpiryJIT implements Comparator<Job>, Serializable {

    @Override
    public int compare(Job t1, Job t2) {
        int res = t1.deliveryDate.compareTo(t2.deliveryDate);
        if (res == 0) {
            return Float.compare(t1.duration, t2.duration);
        } else
            return res;
    }

}
