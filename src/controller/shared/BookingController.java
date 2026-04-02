package controller.shared;

import dao.CourseDAO;
import dao.schedule.ScheduleDAO;
import java.util.List;
import model.Course;
import model.Room;
import model.schedule.RequestSchedule;
import model.user.User;
import service.ScheduleValidator;
import utilities.DateTimeBuilder;
import view.common.MainFrame;
import view.common.ViewSchedule;

public class BookingController {
    // 1.3
    private String timeIn = "";
    private String timeOut = "";

    // Overloaded constructor for student
    // fields are filled already and we just need to show the schedule and load the
    // data.
    public BookingController(User user, Room selectedRoom, RequestSchedule requestSchedule) {
        showRoomSchedule(user, selectedRoom, requestSchedule);
    }

    // for student workflow, no form just show the schedule and confirmation.
    void showRoomSchedule(User user, Room selectedRoom, RequestSchedule requestSchedule) {

        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        selectedRoom.loadSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode())); // schedules for room

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom); // load the
        viewSchedule.loadClassSchedule(selectedRoom);
        viewSchedule.loadConfirmationPanel();

        attachShowRoomScheduleListeners(viewSchedule);

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule");

        viewSchedule.setOnConfirmClicked(e -> {
            new RequestController(requestSchedule);
        });

    }

    // constructor for browse workflow.
    // needs to build a request schedule through the forms
    public BookingController(User user, Room selectedRoom) {
        showRoomSchedule(user, selectedRoom);
    }

    void showRoomSchedule(User user, Room selectedRoom) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        selectedRoom.loadSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode())); // schedules for room
        List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID()); // courses
        // doesnt catch if the courses is null.

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom);
        viewSchedule.loadClassSchedule(selectedRoom);
        viewSchedule.loadFormPanel();
        viewSchedule.loadConfirmationPanel();

        viewSchedule.loadCourse(facultyCourses);
        attachShowRoomScheduleListeners(viewSchedule);

        viewSchedule.setOnHourChanged(e -> {
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                System.out.println("Overlaps with existing schedule at " + timeIn);
            }
        });

        viewSchedule.setOnMinuteChanged(e -> {
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                System.out.println("Overlaps with existing schedule at" + timeIn);
            }
        });

        viewSchedule.setOnMeridiemChanged(e -> {
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                System.out.println("Overlaps with existing schedule at " + timeIn);
            }
        });

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule");

        viewSchedule.setOnConfirmClicked(e -> {
            RequestSchedule requestSchedule = new RequestSchedule();
            // new RequestController(requestSchedule);
        });

    }

    void handleTimeChange(ViewSchedule viewSchedule) {
        int hour = viewSchedule.getHour();
        int minute = viewSchedule.getMinute();
        String meridiem = viewSchedule.getMeridiem();

        int hour24;
        if (meridiem.equals("AM")) {
            hour24 = (hour == 12) ? 0 : hour;
        } else {
            hour24 = (hour == 12) ? 12 : hour + 12;
        }

        // Clamp to school hours: 7 AM (7) to 8 PM (20)
        if (hour24 < 7)
            hour24 = 7;
        if (hour24 > 20)
            hour24 = 20;

        int duration = viewSchedule.getIsLec() ? 1 : 3;
        int timeOutHour24 = hour24 + duration;

        // Also clamp timeout — can't end past 8PM
        if (timeOutHour24 > 20) {
            timeOutHour24 = 20;
            hour24 = 20 - duration; // push timeIn back too
        }

        timeIn = DateTimeBuilder.formatTo12Hour(hour24, minute);
        timeOut = DateTimeBuilder.formatTo12Hour(timeOutHour24, minute);

        viewSchedule.setTimeOut(timeOut);
    }

    void attachShowRoomScheduleListeners(ViewSchedule viewSchedule) {
        viewSchedule.setOnBackClicked(e -> {
            MainFrame.showPanel("RoomBrowser");
        });

        viewSchedule.setOnConfirmClicked(e -> {

        });

        viewSchedule.setOnLabBtn(e -> {
            viewSchedule.setIsLec(false);
            handleTimeChange(viewSchedule);
        });

        viewSchedule.setOnLecBtn(e -> {
            viewSchedule.setIsLec(true);
            handleTimeChange(viewSchedule);
        });
    }
}
