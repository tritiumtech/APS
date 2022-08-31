package utils;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * Author: Amos Zhou
 * Deals with complexities introduced by differences in work calendars in
 * different countries and different entities (company- or department-
 * specific).
 */
public class WorkCalendar {

    public ZonedDateTime addWorkDays(ZonedDateTime baseDate, float daysToAdd){
        return null;
    }

    public ZonedDateTime addWorkHours(ZonedDateTime baseDate, float hoursToAdd){
        return null;
    }

    public ZonedDateTime addWorkMinutes(ZonedDateTime baseDate, float minutesToAdd){
        return null;
    }

    public ZonedDateTime nextWorkInstant(){
        return null;
    }

    public float workDaysBetween(ZonedDateTime startDate, ZonedDateTime endDate) {
        return -1;
    }

    public float workTimeBetween(ZonedDateTime startDate, ZonedDateTime endDate) {
        return -1;
    }

    public static void main(String args[]) {
        LocalDate d = LocalDate.now(); // 当前日期
        LocalTime t = LocalTime.now(); // 当前时间
        LocalDateTime dt = LocalDateTime.now(); // 当前日期和时间
        LocalDate dt2 = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.println(d); // 严格按照ISO 8601格式打印
        System.out.println(t); // 严格按照ISO 8601格式打印
        System.out.println(dt); // 严格按照ISO 8601格式打印
        System.out.println(dt2); // 严格按照ISO 8601格式打印

        LocalDateTime start = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 9, 19, 25, 30);
        Duration du = Duration.between(start, end);
        System.out.println(du); // PT1235H10M30S
        Period p = LocalDate.of(2019, 11, 19).until(LocalDate.of(2020, 1, 9));
        System.out.println(p.getMonths()); // P1M21D
        System.out.println(ZonedDateTime.now());
    }
}
