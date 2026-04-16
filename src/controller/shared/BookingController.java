package controller.shared;

import controller.admin.ArchiveController;
import controller.admin.EditScheduleController;
import dao.CourseDAO;
import dao.StudentDAO;
import dao.schedule.ScheduleDAO;
import java.sql.SQLException;
import java.time.LocalTime;
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
    private boolean isOverlapping = false;
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

    // OVERLOADED METHOD
    // SEARCH ROOMS WORKFLOW
    void showRoomSchedule(User user, Room selectedRoom, RequestSchedule requestSchedule) {

        ScheduleDAO scheduleDAO = new ScheduleDAO();

        List<Schedule> activeSchedules = scheduleDAO
                .filterActiveSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));
        selectedRoom.loadSchedules(activeSchedules); // schedules for room

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom);
        viewSchedule.loadClassSchedule(selectedRoom);
        viewSchedule.loadConfirmationPanel();

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule", "View Schedule");

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
                    onScheduleEditClicked(schedule);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            viewSchedule.setOnBackClicked(e -> {
                MainFrame.showPanel("RoomBrowser", "Browse Room");
            });

            MainFrame.addContentPanel(viewSchedule, "Schedule");
            MainFrame.showPanel("Schedule", "View Schedule");
            return;
        }

        viewSchedule.loadFormPanel(user.getUserType().equals("Faculty"));
        attachShowRoomScheduleListeners(viewSchedule, selectedRoom, user.getUserType().equals("Faculty"));
        // attaches form listeners
        handleTimeChange(viewSchedule);

        if (user.getUserType().equals("Faculty")) {
            List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID()); // courses
            viewSchedule.loadConfirmationPanel();
            viewSchedule.loadCourse(facultyCourses); // ← first course selected here
            loadSection(viewSchedule);

            Course firstCourse = viewSchedule.getCourse();
            if (firstCourse != null) {
                if (firstCourse.isMajor()) {
                    viewSchedule.setLabEnable(true);
                    viewSchedule.setIsLec(true);
                } else {
                    viewSchedule.setLabEnable(false);
                    viewSchedule.setIsLec(true);
                }
                handleTimeChange(viewSchedule);
            }

            viewSchedule.setOnCourseChanged(e -> {
                loadSection(viewSchedule);

                if (viewSchedule.getCourse().isMajor()) {
                    viewSchedule.setLabEnable(true);
                    viewSchedule.setIsLec(true);
                    handleTimeChange(viewSchedule);
                } else {
                    viewSchedule.setLabEnable(false);
                    viewSchedule.setIsLec(true);
                    handleTimeChange(viewSchedule);
                }
            });

        } else if (user.getUserType().equals("Student")) {
            List<Course> studentCourses = courseDAO.getStudentCourse(user.getUserID()); // courses
            viewSchedule.loadCourse(studentCourses);
            viewSchedule.loadConfirmationPanel();

            Course firstCourse = viewSchedule.getCourse();
            if (firstCourse != null) {
                if (firstCourse.isMajor()) {
                    viewSchedule.setLabEnable(true);
                    viewSchedule.setIsLec(true);
                } else {
                    viewSchedule.setLabEnable(false);
                    viewSchedule.setIsLec(true);
                }
                handleTimeChange(viewSchedule);
            }

            viewSchedule.setOnCourseChanged(e -> {
                if (viewSchedule.getCourse().isMajor()) {
                    viewSchedule.setLabEnable(true);
                    viewSchedule.setIsLec(true);
                    handleTimeChange(viewSchedule);
                } else {
                    viewSchedule.setLabEnable(false);
                    viewSchedule.setIsLec(true);
                    handleTimeChange(viewSchedule);
                }
            });

        }

        MainFrame.addContentPanel(viewSchedule, "Schedule");
        MainFrame.showPanel("Schedule", "View Schedule");

    }

    // ADMIN
    // used for archiving schedule

    public BookingController(User user, Room selectedRoom, boolean viewArchives) {
        this.user = user;
        showRoomSchedule(user, selectedRoom, viewArchives);
    }

    void showRoomSchedule(User user, Room selectedRoom, boolean viewArchives) {
        if (!viewArchives) {
            showRoomSchedule(user, selectedRoom);
            return;
        }
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        List<Schedule> inactiveSchedules = scheduleDAO
                .filterInactiveSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));

        selectedRoom.loadSchedules(inactiveSchedules); // schedules for room
        new ViewScheduleController(user, selectedRoom);

    }

    void loadSection(ViewSchedule viewSchedule) {
        // will be loaded ones the courses are picked
        Course selectedCourse = viewSchedule.getCourse();

        if (selectedCourse == null)
            return; // guard clause

        ScheduleDAO scheduleDAO = new ScheduleDAO();
        List<Section> sections = scheduleDAO.getSectionByFacultyCourse(selectedCourse.getCode(), user.getUserID());
        viewSchedule.loadSection(sections);
    }

    void handleTimeChange(ViewSchedule viewSchedule) {
        int hour = viewSchedule.getHour();
        int minute = viewSchedule.getMinute();
        String meridiem = viewSchedule.getMeridiem();

        // Convert to 24-hour for calculation
        int hour24 = hour;
        if (meridiem.equals("PM") && hour != 12) {
            hour24 += 12;
        } else if (meridiem.equals("AM") && hour == 12) {
            hour24 = 0; // midnight
        }

        int duration = viewSchedule.getIsLec() ? 2 : 3;
        int nowHour = LocalTime.now().getHour();
        int minHour = Math.max(7, nowHour);
        int maxStartHour = 20 - duration;

        String newMeridiem = meridiem;
        int displayHour = hour;

        // Clamp minimum (can't start before 7am or now)
        if (hour24 < minHour) {
            hour24 = minHour;
        }

        // Clamp maximum (must end by 8pm)
        if (hour24 > maxStartHour) {
            hour24 = maxStartHour;
        }

        // Convert back to 12-hour format
        if (hour24 == 0) {
            displayHour = 12;
            newMeridiem = "AM";
        } else if (hour24 < 12) {
            displayHour = hour24;
            newMeridiem = "AM";
        } else if (hour24 == 12) {
            displayHour = 12;
            newMeridiem = "PM";
        } else {
            displayHour = hour24 - 12;
            newMeridiem = "PM";
        }

        // ALWAYS update the spinner to match calculated value
        // This prevents the "visual vs actual" mismatch
        viewSchedule.setTimeIn(displayHour, minute, newMeridiem);

        // Calculate timeout
        int timeOutHour24 = hour24 + duration;
        timeIn = DateTimeBuilder.formatTo12Hour(hour24, minute);
        timeOut = DateTimeBuilder.formatTo12Hour(timeOutHour24, minute);
        viewSchedule.setTimeOut(timeOut);

        System.out.println("Set spinner to: " + displayHour + " " + newMeridiem + " (24h: " + hour24 + ")");
    }

    void attachShowRoomScheduleListeners(ViewSchedule viewSchedule, Room selectedRoom, boolean isFaculty) {

        viewSchedule.setOnBackClicked(e -> {
            MainFrame.showPanel("RoomBrowser", "Browse Room");
        });

        viewSchedule.setOnLabBtn(e -> {
            viewSchedule.setIsLec(false);
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                isOverlapping = true;
                viewSchedule.showOverlappingMessage(isOverlapping);
                return;
            }
            isOverlapping = false;
            viewSchedule.showOverlappingMessage(isOverlapping);
        });

        viewSchedule.setOnLecBtn(e -> {
            viewSchedule.setIsLec(true);
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                isOverlapping = true;
                viewSchedule.showOverlappingMessage(isOverlapping);
                return;
            }
            isOverlapping = false;
            viewSchedule.showOverlappingMessage(isOverlapping);
        });

        viewSchedule.setOnHourChanged(e -> {
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                isOverlapping = true;
                viewSchedule.showOverlappingMessage(isOverlapping);
                return;
            }
            isOverlapping = false;
            viewSchedule.showOverlappingMessage(isOverlapping);
        });

        viewSchedule.setOnMinuteChanged(e -> {
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                isOverlapping = true;
                viewSchedule.showOverlappingMessage(isOverlapping);
                return;
            }
            isOverlapping = false;
            viewSchedule.showOverlappingMessage(isOverlapping);
        });

        viewSchedule.setOnMeridiemChanged(e -> {
            handleTimeChange(viewSchedule);
            if (ScheduleValidator.isOverlapping(timeIn, timeOut, selectedRoom.getSchedules())) {
                isOverlapping = true;
                viewSchedule.showOverlappingMessage(isOverlapping);
                return;
            }
            isOverlapping = false;
            viewSchedule.showOverlappingMessage(isOverlapping);
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

    public void onScheduleEditClicked(Schedule schedule) throws SQLException {

        new EditScheduleController(schedule, user);
    }

    public void onScheduleClicked(Schedule schedule, Room room, Boolean viewArchives) throws SQLException {

        new ArchiveController(user, schedule, viewArchives);
    }
}
