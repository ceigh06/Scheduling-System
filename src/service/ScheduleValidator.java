package service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.schedule.Schedule;

public class ScheduleValidator {

    private static final DateTimeFormatter FORMAT_12H = DateTimeFormatter.ofPattern("h:mm a");

    public static boolean isOverlapping(String timeIn, String timeOut, List<Schedule> schedules) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime timeStart = LocalTime.parse(timeIn, format);
        LocalTime timeStop = LocalTime.parse(timeOut, format);

        for (Schedule sched : schedules) {
            // Strip nanoseconds if present (e.g., "07:00:00.0000000" -> "07:00:00")
            String timeInStr = sched.getTimeIn().split("\\.")[0];
            String timeOutStr = sched.getTimeOut().split("\\.")[0];

            LocalTime schedTimeIn = LocalTime.parse(timeInStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime schedTimeOut = LocalTime.parse(timeOutStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

            boolean overlaps = timeStart.isBefore(schedTimeOut) && timeStop.isAfter(schedTimeIn);

            if (overlaps)
                return true;
        }
        return false;
    }

    public static boolean isIncoming(String timeIn) {
        LocalTime start = LocalTime.parse(timeIn, FORMAT_12H);
        return LocalTime.now().isBefore(start);
    }

    /**
     * Check if schedule is currently ongoing - uses current system time
     */
    public static boolean isOngoing(String timeIn, String timeOut) {
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.parse(timeIn, FORMAT_12H);
        LocalTime end = LocalTime.parse(timeOut, FORMAT_12H);
        return !now.isBefore(start) && !now.isAfter(end);
    }

    /**
     * Check if schedule has ended - uses current system time
     */
    public static boolean isPast(String timeOut) {
        LocalTime end = LocalTime.parse(timeOut, FORMAT_12H);
        return LocalTime.now().isAfter(end);
    }
}