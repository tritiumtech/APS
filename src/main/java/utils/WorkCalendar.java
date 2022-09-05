package utils;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Amos Zhou
 * Deals with complexities introduced by differences in work calendars in
 * different countries and different entities (company- or department-
 * specific).
 */
public class WorkCalendar {
    public LocalTime workStartTime;
    public LocalTime workEndTime;
    public List<TimeSlot> shifts = new ArrayList<TimeSlot>();

    public WorkCalendar(LocalTime workStartTime, LocalTime workEndTime) {
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
    }

    public static void main(String[] args) {
        float a = -1.2f;
        int b = (int) a;
        LocalTime t1 = LocalTime.parse("09:00");
        LocalTime t2 = LocalTime.parse("21:00");
        WorkCalendar calendar = new WorkCalendar(t1, t2);
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime newDate1 = calendar.addWorkDays(now, 5);
        ZonedDateTime newDate = calendar.addWorkDays(now, 5.5f);
        float x = calendar.workDaysBetween(newDate1, now);
        float y = calendar.workDaysBetween(now, newDate1);
        System.out.println(x);
        System.out.println(y);
        int num = t2.toSecondOfDay() - t1.toSecondOfDay();
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
     *
     * @param baseDate
     * @param daysToAdd
     * @return
     */
    public ZonedDateTime addWorkDays(ZonedDateTime baseDate, float daysToAdd) {
        // TODO need to implement holidays.
        // 先处理整数天
        int numOfDays = (int) daysToAdd;
        ZonedDateTime newDate = addWorkDays(baseDate, numOfDays);

        long workSeconds = workEndTime.toSecondOfDay() - workStartTime.toSecondOfDay();
        long secondsToDate = baseDate.toLocalTime().toSecondOfDay() - workStartTime.toSecondOfDay();
        long secondsToAdd = (long) ((daysToAdd - numOfDays) * workSeconds); //e.g. -0.2*8*3600

        // 计算不满一日的部分。需考虑工作日标准上班时间来进行计算，精确到秒
        if (daysToAdd < 0) {
            // 往回退
            if (secondsToAdd + secondsToDate < 0) {
                // Reset to end of the last work day
                newDate = lastWorkDate(newDate).withHour(workEndTime.getHour()).withMinute(workEndTime.getMinute()).withSecond(workEndTime.getSecond());
                newDate = newDate.minusSeconds(-secondsToAdd - secondsToDate);
            } else {
                // Dial back secondsToAdd seconds
                newDate = newDate.minusSeconds(secondsToAdd);
            }
        } else {
            // 往后算
            if (secondsToAdd + secondsToDate > workSeconds) {
                // Reset to the start of the next work day
                newDate = nextWorkDate(newDate).withHour(workStartTime.getHour()).withMinute(workStartTime.getMinute()).withSecond(workStartTime.getSecond());
                newDate = newDate.plusSeconds(secondsToAdd + secondsToDate - workSeconds);
            } else {
                // Dial forward secondsToAdd seconds
                newDate = newDate.plusSeconds(secondsToAdd);
            }
        }
        return newDate;
    }

    public ZonedDateTime addWorkDays(ZonedDateTime baseDate, int daysToAdd) {
        ZonedDateTime newDate = baseDate;
        if (daysToAdd > 0) {
            for (int i = 0; i < daysToAdd; i++) {
                baseDate = this.nextWorkDate(baseDate);
            }
        } else {
            for (int i = 0; i < -daysToAdd; i++) {
                baseDate = this.lastWorkDate(baseDate);
            }
        }
        return baseDate;
    }

    public ZonedDateTime addWorkHours(ZonedDateTime baseDate, float hoursToAdd) {
        float workdayHours = (workEndTime.toSecondOfDay() - workStartTime.toSecondOfDay())/3600f;
        float daysToAdd = hoursToAdd / workdayHours;
        return addWorkDays(baseDate, daysToAdd);
    }

    public ZonedDateTime addWorkMinutes(ZonedDateTime baseDate, float minutesToAdd) {
        return null;
    }

    public ZonedDateTime nextWorkDate(ZonedDateTime baseDate) {
        baseDate = baseDate.plusDays(1);
        while (baseDate.getDayOfWeek() == DayOfWeek.SATURDAY || baseDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            baseDate = baseDate.plusDays(1);
        }
        return baseDate;
    }

    public ZonedDateTime lastWorkDate(ZonedDateTime baseDate) {
        baseDate = baseDate.minusDays(1);
        while (baseDate.getDayOfWeek() == DayOfWeek.SATURDAY || baseDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            baseDate = baseDate.minusDays(1);
        }
        return baseDate;
    }

    /**
     * 计算两个时间点之间的工作时间。时间的衡量采用直观计时方式，整数部分为工作日（无论单日工作时长多少），小数部分为按单日
     * 工作时长计算的百分比。例如，若单日工作时长为10小时，5.5代表5.5*10=55个工作时长。
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public float workDaysBetween(ZonedDateTime startDate, ZonedDateTime endDate) {
        int dayCount = 0;
        float timePart = 0.0f;
        // 往后算 e.g. startDate=2022-09-01 11:00:00, endDate=2022-09-03 10:00:00 / 2022-09-03 15:00:00
        if (endDate.isAfter(startDate)) {
            // 平移到同一天
            while (!endDate.toLocalDate().isBefore(nextWorkDate(startDate).toLocalDate())) {
                dayCount += 1;
                startDate = nextWorkDate(startDate);
            }
        } else {
            // 往前推
            if (endDate.isBefore(startDate)) {
                // 平移到同一天
                while (!endDate.toLocalDate().isAfter(lastWorkDate(startDate).toLocalDate())) {
                    dayCount -= 1;
                    startDate = lastWorkDate(startDate);
                }
            }
        }
        // 比时间部分，多退少补
        timePart = (float) (endDate.toLocalTime().toSecondOfDay() - startDate.toLocalTime().toSecondOfDay())
                / (workEndTime.toSecondOfDay() - workStartTime.toSecondOfDay());
        return (float) dayCount + timePart;
    }
}

