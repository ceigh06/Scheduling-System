package controller.shared;

import controller.admin.ArchiveController;
import controller.admin.EditScheduleController;
import dao.CourseDAO;
import dao.StudentDAO;
import dao.schedule.ScheduleDAO;
import java.sql.SQLException;
import java.util.List;
import model.Course;
import model.Room;
import model.Section;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.Student;
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
    // search rooms
    void showRoomSchedule(User user, Room selectedRoom, RequestSchedule requestSchedule) {

        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        List<Schedule> activeSchedules = scheduleDAO
                .filterActiveSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));
        selectedRoom.loadSchedules(activeSchedules); // schedules for room

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom);
        viewSchedule.loadClassSchedule(selectedRoom);
        viewSchedule.loadConfirmationPanel();

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule");

        viewSchedule.setOnConfirmClicked(e -> {
            new RequestController(requestSchedule, user);
        });

    }

    // browse rooms
    // constructor for browse workflow.
    // needs to build a request schedule through the forms
    public BookingController(User user, Room selectedRoom) {
        this.user = user;
        showRoomSchedule(user, selectedRoom);
    }

    public BookingController(User user, Room selectedRoom, boolean viewArchives) {
        this.user = user;
        showRoomSchedule(user, selectedRoom, viewArchives);
    }

    void showRoomSchedule(User user, Room selectedRoom) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        List<Schedule> activeSchedules = scheduleDAO
                .filterActiveSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));
        
        selectedRoom.loadSchedules(activeSchedules); // schedules for room

        // doesnt catch if the courses is null.

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom);
        viewSchedule.loadClassSchedule(selectedRoom);

        if (user.getUserType().equals("Admin")) {
            viewSchedule.setOnScheduleClicked(schedule -> {
                try {
                    onScheduleEditClicked(schedule, selectedRoom);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            viewSchedule.setOnBackClicked(e -> {
                MainFrame.showPanel("RoomBrowser");
            });

            MainFrame.addContentPanel(viewSchedule, "Schedule");
            MainFrame.showPanel("Schedule");
            return;
        }

        viewSchedule.loadFormPanel(user.getUserType().equals("Faculty"));
        attachShowRoomScheduleListeners(viewSchedule, selectedRoom,user.getUserType().equals("Faculty"));
        // attaches form listeners
        
        

        if (user.getUserType().equals("Faculty")) {
            List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID()); // courses
            viewSchedule.loadCourse(facultyCourses);
            viewSchedule.loadConfirmationPanel();

            viewSchedule.setOnCourseChanged(e -> {
                loadSection(viewSchedule);
            });

        } else if (user.getUserType().equals("Student")) {
            List<Course> studentCourses = courseDAO.getStudentCourse(user.getUserID()); // courses
            viewSchedule.loadCourse(studentCourses);
            viewSchedule.loadConfirmationPanel();

        }

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule");

    }

     void showRoomSchedule(User user, Room selectedRoom, boolean viewArchives) {
        if(!viewArchives){
            showRoomSchedule(user, selectedRoom);
            return;
        }
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        List<Schedule> inactiveSchedules = scheduleDAO
                .filterInactiveSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));
        selectedRoom.loadSchedules(inactiveSchedules); // schedules for room

        // doesnt catch if the courses is null.

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom, viewArchives);
        viewSchedule.loadClassSchedule(selectedRoom, viewArchives);

        if (user.getUserType().equals("Admin")) {
            viewSchedule.setOnScheduleClicked(schedule -> {
                try {
                    onScheduleClicked(schedule, selectedRoom, viewArchives);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            viewSchedule.setOnBackClicked(e -> {
                MainFrame.showPanel("RoomBrowser");
            });

            MainFrame.addContentPanel(viewSchedule, "Schedule");
            MainFrame.showPanel("Schedule");
            return;
        }

        viewSchedule.loadFormPanel(user.getUserType().equals("Faculty"));
        attachShowRoomScheduleListeners(viewSchedule, selectedRoom, user.getUserType().equals("Faculty"));
        // attaches form listeners

        if (user.getUserType().equals("Faculty")) {
            List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID()); // courses
            viewSchedule.loadCourse(facultyCourses);
            viewSchedule.loadConfirmationPanel();

            viewSchedule.setOnCourseChanged(e -> {
                loadSection(viewSchedule);
            });

        } else if (user.getUserType().equals("Student")) {
            List<Course> studentCourses = courseDAO.getStudentCourse(user.getUserID()); // courses
            viewSchedule.loadCourse(studentCourses);
            viewSchedule.loadConfirmationPanel();

        }

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule");

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

    void attachShowRoomScheduleListeners(ViewSchedule viewSchedule, Room selectedRoom, boolean isFaculty) {

        viewSchedule.setOnBackClicked(e -> {
            MainFrame.showPanel("RoomBrowser");
        });

        viewSchedule.setOnLabBtn(e -> {
            viewSchedule.setIsLec(false);
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                System.out.println("Overlaps with existing schedule at " + timeIn);
            }
        });

        viewSchedule.setOnLecBtn(e -> {
            viewSchedule.setIsLec(true);
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                System.out.println("Overlaps with existing schedule at " + timeIn);
            }
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

        viewSchedule.setOnConfirmClicked(e -> {
            if (viewSchedule.getCourse() == null && !isFaculty) {
                MainFrame.setNotification("Please Choose a Course");
                return;
            }

            if ((viewSchedule.getCourse() == null || viewSchedule.getSection() == null) && isFaculty) {
                MainFrame.setNotification("Please Choose a Course and Section First");
                return;
            }

            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                MainFrame.setNotification("Your request overlaps with the room schedule!");
                return;
            }

            RequestSchedule requestSchedule = new RequestSchedule();
            String status = "";
            String section = "";
            String facultyID = "";

            if (isFaculty) {
                status = "3";
                section = String.valueOf(viewSchedule.getSection().getSectionKey());
                facultyID = user.getUserID();
            } else {
                StudentDAO studentDAO = new StudentDAO();
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                Student student = studentDAO.get(user.getUserID());
                status = "1";
                section = String.valueOf(student.getSectionKey());
                facultyID = scheduleDAO.getFacultyIDByStudentCourse(user.getUserID(),
                        viewSchedule.getCourse().getCode());
            }

            requestSchedule.load(-1, selectedRoom.getRoomCode(),
                    section, viewSchedule.getCourse().getCode(),
                    facultyID, timeIn, timeOut, DateTimeBuilder.getDayName(), status, 0,
                    DateTimeBuilder.getCurrentDate(), user.getUserID());

            new RequestController(requestSchedule, user);
        });
    }

    public void onScheduleEditClicked(Schedule schedule, Room room) throws SQLException {

        new EditScheduleController(schedule, room, user);
    }

    public void onScheduleClicked(Schedule schedule, Room room, Boolean viewArchives) throws SQLException {

        new ArchiveController(user, schedule, room, viewArchives);
    }
}
