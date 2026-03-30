package service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.schedule.Schedule;

public class ScheduleValidator {

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
}