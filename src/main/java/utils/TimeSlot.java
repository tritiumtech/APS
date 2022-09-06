package utils;

import java.time.LocalTime;

public class TimeSlot {
    public LocalTime startTime;
    public LocalTime endTime;

    public TimeSlot(LocalTime start, LocalTime end) {
        startTime = start;
        endTime = end;
    }
}
