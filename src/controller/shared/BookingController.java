package controller.shared;

import controller.admin.EditScheduleController;
import dao.CourseDAO;
import dao.schedule.ScheduleDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Room;
import model.Section;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
import service.ScheduleValidator;
import utilities.DateTimeBuilder;
import view.common.MainFrame;
import view.common.ViewSchedule;

public class BookingController {

    // 1.3
    private String timeIn = "";
    private String timeOut = "";
    private User user;

    // Overloaded constructor for student
    // fields are filled already and we just need to show the schedule and load the
    // data.
    public BookingController(User user, Room selectedRoom, RequestSchedule requestSchedule) {
        this.user = user;
        showRoomSchedule(user, selectedRoom, requestSchedule);
    }

    // for student workflow, no form just show the schedule and confirmation.
    void showRoomSchedule(User user, Room selectedRoom, RequestSchedule requestSchedule) {

        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        List<Schedule> activeSchedules = scheduleDAO.filterActiveSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));
        selectedRoom.loadSchedules(activeSchedules); // schedules for room

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom); // load the
        viewSchedule.loadClassSchedule(selectedRoom);
        viewSchedule.loadConfirmationPanel();

        attachShowRoomScheduleListeners(viewSchedule);

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule");

        viewSchedule.setOnConfirmClicked(e -> {
            new RequestController(requestSchedule, user);
        });

    }

    // constructor for browse workflow.
    // needs to build a request schedule through the forms
    public BookingController(User user, Room selectedRoom) {
        this.user = user;
        showRoomSchedule(user, selectedRoom);
    }

    void showRoomSchedule(User user, Room selectedRoom) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        List<Schedule> activeSchedules = scheduleDAO.filterActiveSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));
        selectedRoom.loadSchedules(activeSchedules); // schedules for room
        List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID()); // courses

        // doesnt catch if the courses is null.

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom);
        viewSchedule.loadClassSchedule(selectedRoom);

        if (user.getUserType() != "Admin") {
            viewSchedule.loadFormPanel();
            viewSchedule.loadCourse(facultyCourses);
            viewSchedule.loadConfirmationPanel();

            attachShowRoomScheduleListeners(viewSchedule);

            viewSchedule.setOnCourseChanged(e -> {
                loadSection(viewSchedule);
            });

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
                if (viewSchedule.getCourse() == null || viewSchedule.getSection() == null) {
                    MainFrame.setNotification("Please Choose a Course and Section First");
                    return;
                }
                RequestSchedule requestSchedule = new RequestSchedule();
                requestSchedule.load(-1, selectedRoom.getRoomCode(), String.valueOf(viewSchedule.getSection().getSectionKey()), viewSchedule.getCourse().getCode(), user.getUserID(), timeIn,timeOut, DateTimeBuilder.getDayName(), "3", 0, DateTimeBuilder.getCurrentDate(), user.getUserID());

                new RequestController(requestSchedule, user);
            });
        } else {
            viewSchedule.setOnScheduleClicked(schedule -> {
                onScheduleEditClicked(schedule, selectedRoom);
            });

            viewSchedule.setOnBackClicked(e -> {
                MainFrame.showPanel("RoomBrowser");
            });

            MainFrame.addContentPanel(viewSchedule, "Schedule");
            MainFrame.showPanel("Schedule");
        }
    }

    void loadSection(ViewSchedule viewSchedule) {
        // will be loaded ones the courses are picked
        Course selectedCourse = viewSchedule.getCourse();
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        List<Section> sections = scheduleDAO.getSectionByFacultyCourse(selectedCourse.getCode(), user.getUserID());
        viewSchedule.loadSection(sections);
        if (selectedCourse != null) {
            viewSchedule.loadSection(sections);
        }
    }

    void handleTimeChange(ViewSchedule viewSchedule) {
        int hour = viewSchedule.getHour();
        int minute = viewSchedule.getMinute();
        String meridiem = viewSchedule.getMeridiem();

        int hour24 = hour;
        if (meridiem.equals("PM")) {
            hour24 += 12;
        }

        int duration = viewSchedule.getIsLec() ? 1 : 3;

        // Clamp to school hours: 7 AM (7) to 8 PM (20)
        if (hour24 < 7) {
            hour24 = 7;
            viewSchedule.setTimeIn(hour24, minute, meridiem);
        }
        if (hour24 > 20 - duration) {
            hour24 = 20 - duration; // clamp first
            viewSchedule.setTimeIn(hour24 - 12, minute, meridiem);
        }
        int timeOutHour24 = hour24 + duration;
        System.out.println(hour24 + " " + timeOutHour24);

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

    public void onScheduleEditClicked(Schedule schedule, Room room) {

        new EditScheduleController(schedule, room, user);
    }
}
