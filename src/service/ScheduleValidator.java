package service;

import java.time.LocalTime;
import java.util.List;
import model.schedule.Schedule;

public class ScheduleValidator {

    public static boolean isOverlapping(String timeIn, String timeOut, List<Schedule> schedules) {
        LocalTime timeStart = LocalTime.parse(timeIn);
        LocalTime timeStop = LocalTime.parse(timeOut);

        for (Schedule sched : schedules) {
            LocalTime schedTimeIn = LocalTime.parse(sched.getTimeIn());
            LocalTime schedTimeOut = LocalTime.parse(sched.getTimeOut());

            boolean overlaps = timeStart.isBefore(schedTimeOut) && timeStop.isAfter(schedTimeIn);
            
            if (overlaps) return true;
        }
        return false;
    }
}