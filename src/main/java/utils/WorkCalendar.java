package utils;

import java.sql.Time;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * Author: Amos Zhou
 * Deals with complexities introduced by differences in work calendars in
 * different countries and different entities (company- or department-
 * specific).
 */
public class WorkCalendar {
    public LocalTime workStartTime;
    public LocalTime workEndTime;

    public WorkCalendar(LocalTime workStartTime, LocalTime workEndTime) {
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
    }

    public static void main(String args[]) {
        float a = -1.2f;
        int b = (int) a;
        WorkCalendar calendar = new WorkCalendar(LocalTime.now(),LocalTime.now());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime newDate =  calendar.addWorkDays(now, -5);
        System.out.println(newDate);
        LocalTime t1 = LocalTime.parse("09:00");
        LocalTime t2 = LocalTime.parse("21:00");
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

    /**
     * 该方法返回按工作日计算的新日期。注意，daysToAdd的表达方式采用直观计时方式，整数部分为工作日（无论单日工作时长多少），小数部分为按单日
     * 工作时长计算的百分比。例如，若单日工作时长为10小时，5.5代表5.5*10=55个工作时长。
     * @param baseDate
     * @param daysToAdd
     * @return
     */
    public ZonedDateTime addWorkDays(ZonedDateTime baseDate, float daysToAdd) {
        // TODO need to implement holidays.
        // 先处理整数天

        return null;
    }

    public ZonedDateTime addWorkDays(ZonedDateTime baseDate, int daysToAdd) {
        ZonedDateTime newDate = baseDate;
        if (daysToAdd > 0) {
            baseDate = this.nextWorkDate(baseDate);
            for (int i = 0; i < daysToAdd; i++) {
                baseDate = baseDate.plusDays(1);
                baseDate = this.nextWorkDate(baseDate);
            }
        } else {
            baseDate = this.lastWorkDate(baseDate);
            for (int i = 0; i < -daysToAdd; i++) {
                baseDate = baseDate.minusDays(1);
                baseDate = this.lastWorkDate(baseDate);
            }
        }
        return baseDate;
    }

    public ZonedDateTime addWorkHours(ZonedDateTime baseDate, float hoursToAdd) {
        return null;
    }

    public ZonedDateTime addWorkMinutes(ZonedDateTime baseDate, float minutesToAdd) {
        return null;
    }

    public ZonedDateTime nextWorkDate(ZonedDateTime baseDate) {
        while (baseDate.getDayOfWeek() == DayOfWeek.SATURDAY || baseDate.getDayOfWeek() == DayOfWeek.SUNDAY){
            baseDate = baseDate.plusDays(1);
        }
        return baseDate;
    }

    public ZonedDateTime lastWorkDate(ZonedDateTime baseDate) {
        while (baseDate.getDayOfWeek() == DayOfWeek.SATURDAY || baseDate.getDayOfWeek() == DayOfWeek.SUNDAY){
            baseDate = baseDate.minusDays(1);
        }
        return baseDate;
    }

    public float workDaysBetween(ZonedDateTime startDate, ZonedDateTime endDate) {
        return -1;
    }

    public float workTimeBetween(ZonedDateTime startDate, ZonedDateTime endDate) {
        return -1;
    }
}
