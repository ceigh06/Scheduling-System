package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.schedule.Schedule;

public class ScheduleValidator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a");

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

    public static boolean isIncoming(String date, String timeIn) {
        LocalDate scheduleDate = LocalDate.parse(date, DATE_FORMAT);
        LocalTime startTime = LocalTime.parse(timeIn, TIME_FORMAT);
        LocalDateTime scheduleStart = LocalDateTime.of(scheduleDate, startTime);

        return LocalDateTime.now().isBefore(scheduleStart);
    }

    public static boolean isOngoing(String date, String timeIn, String timeOut) {
        LocalDate scheduleDate = LocalDate.parse(date, DATE_FORMAT);
        LocalTime startTime = LocalTime.parse(timeIn, TIME_FORMAT);
        LocalTime endTime = LocalTime.parse(timeOut, TIME_FORMAT);

        LocalDateTime scheduleStart = LocalDateTime.of(scheduleDate, startTime);
        LocalDateTime scheduleEnd = LocalDateTime.of(scheduleDate, endTime);
        LocalDateTime now = LocalDateTime.now();

        return !now.isBefore(scheduleStart) && !now.isAfter(scheduleEnd);
    }

    public static boolean isPast(String date, String timeOut) {
        LocalDate scheduleDate = LocalDate.parse(date, DATE_FORMAT);
        LocalTime endTime = LocalTime.parse(timeOut, TIME_FORMAT);
        LocalDateTime scheduleEnd = LocalDateTime.of(scheduleDate, endTime);

        return LocalDateTime.now().isAfter(scheduleEnd);
    }
}