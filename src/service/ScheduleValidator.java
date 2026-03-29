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
            LocalTime schedTimeIn = LocalTime.parse(sched.getTimeIn(), format);
            LocalTime schedTimeOut = LocalTime.parse(sched.getTimeOut(), format);

            boolean overlaps = timeStart.isBefore(schedTimeOut) && timeStop.isAfter(schedTimeIn);
            
            if (overlaps) return true;
        }
        return false;
    }
}