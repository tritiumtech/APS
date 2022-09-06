package utils;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Author: Amos Zhou
 * Deals with complexities introduced by differences in work calendars in
 * different countries and different entities (company- or department-
 * specific).
 */
public class WorkCalendar {
    public List<TimeSlot> shifts = new ArrayList<TimeSlot>();
    public List<LocalDate> holidays = new ArrayList<LocalDate>();
    public TimeZone timezone;

    public WorkCalendar(String timezone) {
        this.timezone = TimeZone.getTimeZone(timezone);
    }

    public static void main(String[] args) {
        float a = -1.2f;
        int b = (int) a;
        LocalTime t1 = LocalTime.parse("09:00");
        LocalTime t2 = LocalTime.parse("21:00");
        String timezone = "Australia/Melbourn";

        ZonedDateTime now = ZonedDateTime.parse("2022-09-06T23:10:00+08:00[Asia/Shanghai]");
        ZonedDateTime nowInMel = now.withZoneSameInstant(ZoneId.of("Australia/Melbourne"));

        TimeZone x1 = TimeZone.getTimeZone(timezone);
        WorkCalendar calendar = new WorkCalendar(timezone);
        calendar.addHoliday(LocalDate.parse("2022-09-08"));
        LocalDate x2 = LocalDate.parse("2022-09-08");
        System.out.println();

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

    public void addShift(String startTime, String endTime) throws DateTimeParseException {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        TimeSlot shift = new TimeSlot(start, end);
        this.shifts.add(shift);
    }

    /**
     * 增加一个法定假日。假日跟时区无关，故采用LocalDate。
     *
     * @param holiday
     */
    public void addHoliday(LocalDate holiday) {
        this.holidays.add(holiday);
    }

    /**
     * 判断一个带时区的时间是否在属于公共假日。首先将带时区的时间转化为当前工作日历所在的时区，再做下一步判断。
     * @param dt 一个带时区的时间
     * @return
     */
    public boolean isHoliday(ZonedDateTime dt) {
        LocalDate local = dt.withZoneSameInstant(timezone.toZoneId()).toLocalDate();
        if (holidays.contains(local)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个带时区的时间是否在当前工作日历所在市区处于周末
     * @param dt
     * @return
     */
    public boolean isWeekend(ZonedDateTime dt) {
        LocalDate local = dt.withZoneSameInstant(timezone.toZoneId()).toLocalDate();
        if(local.getDayOfWeek() ==  DayOfWeek.SATURDAY || local.getDayOfWeek() ==  DayOfWeek.SUNDAY)
            return true;
        return false;
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
        // 先处理整数天
        int numOfDays = (int) daysToAdd;
        ZonedDateTime newDate = addWorkDays(baseDate, numOfDays);

        long workSeconds = 0;//workEndTime.toSecondOfDay() - workStartTime.toSecondOfDay();
        long secondsToDate = 0;//baseDate.toLocalTime().toSecondOfDay() - workStartTime.toSecondOfDay();
        long secondsToAdd = (long) ((daysToAdd - numOfDays) * workSeconds); //e.g. -0.2*8*3600

        // 计算不满一日的部分。需考虑工作日标准上班时间来进行计算，精确到秒
        if (daysToAdd < 0) {
            // 往回退
            if (secondsToAdd + secondsToDate < 0) {
                // Reset to end of the last work day
                newDate = ZonedDateTime.now();//lastWorkDate(newDate).withHour(workEndTime.getHour()).withMinute(workEndTime.getMinute()).withSecond(workEndTime.getSecond());
                newDate = newDate.minusSeconds(-secondsToAdd - secondsToDate);
            } else {
                // Dial back secondsToAdd seconds
                newDate = newDate.minusSeconds(secondsToAdd);
            }
        } else {
            // 往后算
            if (secondsToAdd + secondsToDate > workSeconds) {
                // Reset to the start of the next work day
                newDate = ZonedDateTime.now();//nextWorkDate(newDate).withHour(workStartTime.getHour()).withMinute(workStartTime.getMinute()).withSecond(workStartTime.getSecond());
                newDate = newDate.plusSeconds(secondsToAdd + secondsToDate - workSeconds);
            } else {
                // Dial forward secondsToAdd seconds
                newDate = newDate.plusSeconds(secondsToAdd);
            }
        }
        return newDate;
    }

    /**
     * 首先将带时区的时间转化为本地时间，然后按逐个遍历TimeSlot，返回增加daysToAdd个工作日后最终结束时间
     * @param baseDate
     * @param daysToAdd
     * @return
     */
    public ZonedDateTime addWorkDays(ZonedDateTime baseDt, int daysToAdd) {
        LocalDateTime baseDate = baseDt.withZoneSameInstant(timezone.toZoneId()).toLocalDateTime();
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
        float workdayHours = 0;// (workEndTime.toSecondOfDay() - workStartTime.toSecondOfDay()) / 3600f;
        float daysToAdd = hoursToAdd / workdayHours;
        return addWorkDays(baseDate, daysToAdd);
    }

    public ZonedDateTime addWorkMinutes(ZonedDateTime baseDate, float minutesToAdd) {
        return null;
    }

    public LocalDateTime nextWorkDate(LocalDate baseDate) {
        baseDate = baseDate.plusDays(1);
        while (isWeekend(baseDate)||isHoliday(baseDate)) {
            baseDate = baseDate.plusDays(1);
        }
        return baseDate;
    }

    private boolean isHoliday(LocalDateTime datetime) {
        if (holidays.contains(datetime)) {
            return true;
        }
        return false;
    }

    private boolean isWeekend(LocalDateTime datetime) {
        if(datetime.getDayOfWeek() ==  DayOfWeek.SATURDAY || datetime.getDayOfWeek() ==  DayOfWeek.SUNDAY)
            return true;
        return false;
    }

    public ZonedDateTime lastWorkDate(ZonedDateTime baseDate) {
        baseDate = baseDate.minusDays(1);
        while (isWeekend(baseDate)||isHoliday(baseDate)) {
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
        timePart = (float) (endDate.toLocalTime().toSecondOfDay() - startDate.toLocalTime().toSecondOfDay());
        /// (workEndTime.toSecondOfDay() - workStartTime.toSecondOfDay());
        return (float) dayCount + timePart;
    }
}

