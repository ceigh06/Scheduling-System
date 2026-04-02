package controller.shared;

import dao.CourseDAO;
import dao.schedule.ScheduleDAO;
import java.util.List;
import model.Course;
import model.Room;
import model.schedule.RequestSchedule;
import model.user.User;
import service.ScheduleValidator;
import view.common.MainFrame;
import view.common.ViewSchedule;

public class BookingController {
    // 1.3
    private String timeIn = "";
    private String timeOut = "";

    // constructor for browse workflow.
    // needs to build a request schedule through the forms
    public BookingController(User user, Room selectedRoom) {
        showRoomSchedule(user, selectedRoom);
    }

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
        System.out.println("RequestSchedule{" +
           "ID=" + requestSchedule.getID() +
           ", roomCode='" + requestSchedule.getRoomCode() + '\'' +
           ", sectionKey='" + requestSchedule.getSectionKey() + '\'' +
           ", courseCode='" + requestSchedule.getCourseCode() + '\'' +
           ", facultyID='" + requestSchedule.getFacultyID() + '\'' +
           ", timeIn='" + requestSchedule.getTimeIn() + '\'' +
           ", timeOut='" + requestSchedule.getTimeOut() + '\'' +
           ", scheduledDay='" + requestSchedule.getScheduledDay() + '\'' +
           ", status='" + requestSchedule.getStatus() + '\'' +
           ", isArchived=" + requestSchedule.getIsArchived() +
           ", dateRequested='" + requestSchedule.getDateRequested() + '\'' +
           ", studentNumber='" + requestSchedule.getStudentRequested() + '\'' +
           '}');
    }

    void showRoomSchedule(User user, Room selectedRoom) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        selectedRoom.loadSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode())); // schedules for room
        List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID()); // courses
        // doesnt catch if the courses is null.

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom); // load the
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

    }

    void handleTimeChange(ViewSchedule viewSchedule) {
        int hour = viewSchedule.getHour();
        int timeOutHour;
        int minute = viewSchedule.getMinute();

        if (viewSchedule.getMeridiem().equals("PM") && hour != 12) {
            hour += 12;
        } else if (viewSchedule.getMeridiem().equals("AM") && hour == 12) {
            hour = 0;
        }

        if (viewSchedule.getIsLec() == true) {
            timeOutHour = hour + 1;
        } else {
            timeOutHour = hour + 3;
        }

        // Convert back to 12-hour format for validator
        String timeInMeridiem = (hour >= 12) ? "PM" : "AM";
        int timeIn12Hour = (hour > 12) ? hour - 12 : (hour == 0) ? 12 : hour;
        timeIn = String.format("%d:%02d %s", timeIn12Hour, minute, timeInMeridiem);

        String timeOutMeridiem = (timeOutHour >= 12) ? "PM" : "AM";
        int timeOut12Hour = (timeOutHour > 12) ? timeOutHour - 12 : (timeOutHour == 0) ? 12 : timeOutHour;
        timeOut = String.format("%d:%02d %s", timeOut12Hour, minute, timeOutMeridiem);
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
