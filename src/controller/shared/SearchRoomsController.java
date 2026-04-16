package controller.shared;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.BuildingDAO;
import dao.CourseDAO;
import dao.RoomDAO;
import dao.StudentDAO;
import dao.schedule.ScheduleDAO;
import model.Building;
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
import view.common.RoomBrowser;
import view.common.SearchRooms1;

public class SearchRoomsController {
    String timeIn;
    String timeOut;
    User user;
    RequestSchedule requestSchedule;

    // === SCHEDULE BOUNDS (in 24-hour integers) ===
    private static final int MAX_TIME_IN = 19; // 7:00 PM
    private static final int MIN_DURATION = 1; // hours
    private static final int MAX_DURATION = 3; // hours
    private static final int MAX_TIME_OUT = 20; // 8:00 PM hard cap

    public SearchRoomsController(User user) {
        this.user = user;
        try {
            showSearch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void showSearch() throws SQLException {
        boolean isFaculty = user.getUserType().equals("Faculty");

        SearchRooms1 searchRooms = new SearchRooms1(user);

        BuildingDAO buildingDAO = new BuildingDAO();
        List<Building> buildings = buildingDAO.getAllBuilding();
        searchRooms.loadBuilding(buildings);

        List<Course> courses;
        CourseDAO courseDAO = new CourseDAO();
        if (isFaculty) {
            courses = courseDAO.getFacultyCourses(user.getUserID());
        } else {
            courses = courseDAO.getStudentCourse(user.getUserID());
        }

        searchRooms.loadCourse(courses);
        loadSection(searchRooms);

        MainFrame.addContentPanel(searchRooms, "SearchRooms");
        MainFrame.showPanel("SearchRooms", "Search Rooms");

        handleTimeChangeSilent(searchRooms);

        searchRooms.setOnTimeInChanged(e -> handleTimeChange(searchRooms));
        searchRooms.setOnTimeOutChanged(e -> handleTimeChange(searchRooms));

        if (isFaculty) {
            searchRooms.setOnCourseChanged(e -> loadSection(searchRooms));
        }

        searchRooms.setOnClearButton(e -> searchRooms.clearAll());
        searchRooms.setOnConfirmButton(e -> onConfirmClicked(searchRooms));
    }

    void handleTimeChangeSilent(SearchRooms1 searchRooms) {
        int nowHour = LocalTime.now().getHour();
        int effectiveMin = Math.max(7, nowHour);

        // If it's past 7 AM, start from current time (rounded up to nearest hour)
        int startHour = effectiveMin;
        int startMinute = 0;

        // Optional: if you want to round up to next hour when minutes > 0
        // if (LocalTime.now().getMinute() > 0) {
        // startHour++;
        // }

        // Clamp to valid range
        if (startHour > MAX_TIME_IN) {
            startHour = MAX_TIME_IN; // 7 PM latest start
        }

        int endHour = startHour + MIN_DURATION; // +1 hour default
        if (endHour > MAX_TIME_OUT) {
            endHour = MAX_TIME_OUT;
        }

        // Convert to 12-hour format
        int start12 = to12Hour(startHour);
        String startMer = startHour >= 12 ? "PM" : "AM";

        int end12 = to12Hour(endHour);
        String endMer = endHour >= 12 ? "PM" : "AM";

        searchRooms.setTimeIn(start12, startMinute, startMer);
        searchRooms.setTimeOut(end12, 0, endMer);

        timeIn = DateTimeBuilder.formatTo12Hour(startHour, startMinute);
        timeOut = DateTimeBuilder.formatTo12Hour(endHour, 0);

        System.out.println("Silent init: " + startHour + ":00 -> " + endHour + ":00");
    }

    void loadSection(SearchRooms1 searchRooms1) {
        Course selectedCourse = searchRooms1.getCourse();
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        List<Section> sections = scheduleDAO.getSectionByFacultyCourse(selectedCourse.getCode(), user.getUserID());
        searchRooms1.loadSection(sections);
    }

    void handleTimeChange(SearchRooms1 searchRooms) {
        // --- Read raw values from the view ---
        String rawTimeIn = searchRooms.getTimeIn();
        String rawTimeOut = searchRooms.getTimeOut();

        String[] inParts = rawTimeIn.split("[ :]");
        String[] outParts = rawTimeOut.split("[ :]");

        int inHour = Integer.parseInt(inParts[0]);
        int inMinute = Integer.parseInt(inParts[1]);
        String inMer = inParts[2];

        int outHour = Integer.parseInt(outParts[0]);
        int outMinute = Integer.parseInt(outParts[1]);
        String outMer = outParts[2];

        // --- Convert to 24-hour for arithmetic ---
        int inHour24 = to24Hour(inHour, inMer);
        int outHour24 = to24Hour(outHour, outMer);

        // Convert minutes to total minutes from midnight for precise comparison
        int inTotal = inHour24 * 60 + inMinute;
        int outTotal = outHour24 * 60 + outMinute;


        // =========================================================
        // STEP 1: Clamp Time In within allowed window [7AM .. 7PM]
        // =========================================================
        int nowHour = LocalTime.now().getHour();
        int effectiveMin = Math.max(7, nowHour);

        if (inHour24 < effectiveMin) {
            inHour24 = effectiveMin;
            inMinute = 0;
            inTotal = inHour24 * 60;

        } else if (inHour24 > MAX_TIME_IN) {
            inHour24 = MAX_TIME_IN;
            inMinute = 0;
            inTotal = inHour24 * 60;
        }

        // =========================================================
        // STEP 2: Derive the valid Time Out window from clamped Time In
        // min out = Time In + 1 hour (in total minutes)
        // max out = min(Time In + 3 hours, 8:00 PM)
        // =========================================================
        int minOutTotal = inTotal + MIN_DURATION * 60; // e.g. 8:00 AM if in=7AM
        int maxOutTotal = Math.min(
                inTotal + MAX_DURATION * 60, // e.g. 10:00 AM if in=7AM
                MAX_TIME_OUT * 60 // hard cap: 8:00 PM = 1200 min
        );

        // =========================================================
        // STEP 3: Clamp Time Out within [minOut .. maxOut]
        // ORDER MATTERS: check hard cap first, then range constraints.
        // Old code checked > max hard cap only on outHour24 (ignoring minutes),
        // then fell through to the duration checks — allowing out-of-bounds
        // values to slip past when the hour happened to equal the cap.
        // =========================================================
        if (outTotal > maxOutTotal) {
            // Out-of-range high: could be beyond 8PM *or* beyond +3h from Time In
            outTotal = maxOutTotal;
            if (outTotal == MAX_TIME_OUT * 60) {
            } else {
            }
        } else if (outTotal < minOutTotal) {
            // Out-of-range low: earlier than Time In + 1 hour
            outTotal = minOutTotal;
        }

        // Unpack corrected outTotal back to hour/minute
        outHour24 = outTotal / 60;
        outMinute = outTotal % 60;

        // =========================================================
        // STEP 4: Push corrected values back into the view
        // =========================================================
        int in12 = to12Hour(inHour24);
        String inMerNew = inHour24 >= 12 ? "PM" : "AM";
        searchRooms.setTimeIn(in12, inMinute, inMerNew);

        int out12 = to12Hour(outHour24);
        String outMerNew = outHour24 >= 12 ? "PM" : "AM";
        searchRooms.setTimeOut(out12, outMinute, outMerNew);

        // Store formatted strings for later use in the booking flow
        timeIn = DateTimeBuilder.formatTo12Hour(inHour24, inMinute);
        timeOut = DateTimeBuilder.formatTo12Hour(outHour24, outMinute);

    }

    // --- Helpers ---

    /** Convert 12-hour (1–12) + meridiem to 24-hour (0–23). */
    private int to24Hour(int hour12, String mer) {
        int h = hour12;
        if (mer.equals("PM") && hour12 != 12)
            h += 12;
        if (mer.equals("AM") && hour12 == 12)
            h = 0;
        return h;
    }

    /** Convert 24-hour (0–23) to 12-hour display (1–12). */
    private int to12Hour(int hour24) {
        int h = hour24 % 12;
        return h == 0 ? 12 : h;
    }

    void onConfirmClicked(SearchRooms1 searchRooms) {
        try {
            if (searchRooms.getChosenBuildings().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a building");
                return;
            } else if (searchRooms.getTimeIn() == null || searchRooms.getTimeOut() == null) {
                JOptionPane.showMessageDialog(null, "Please put the necessary fields.");
                return;
            } else if (searchRooms.getCourse() == null) {
                JOptionPane.showMessageDialog(null, "Please select a course");
                return;
            } else if ((searchRooms.getCourse() == null || searchRooms.getSection() == null)
                    && user.getUserType().equals("Faculty")) {
                JOptionPane.showMessageDialog(null, "Please select a course and section");
                return;
            }
        } catch (HeadlessException | SQLException e) {
            e.printStackTrace();
        }

        try {
            RoomDAO roomDAO = new RoomDAO();
            List<Building> checkedBuildings = searchRooms.getChosenBuildings();
            String timeIn = searchRooms.getTimeIn();
            String timeOut = searchRooms.getTimeOut();
            Course course = searchRooms.getCourse();
            int capacity = searchRooms.getCapacity();
            int floor = searchRooms.getFloorLevel();

            buildRequestSchedule(timeIn, timeOut, course, capacity, floor, searchRooms);

            List<Room> availableRooms = new ArrayList<>();
            for (Building building : checkedBuildings) {
                List<Room> roomsToCheck = roomDAO.getAllRooms(building.getCode());
                for (Room room : roomsToCheck) {
                    room.loadSchedules(new ScheduleDAO().getRoom(room.getRoomCode()));
                    List<Schedule> schedules = room.getSchedules();
                    if (schedules == null) {
                        schedules = new ArrayList<>();
                    }
                    boolean isValidCapacity = room.getCapacity() >= capacity;
                    boolean isValidFloor = floor == 0 || room.getFloor() == floor;
                    boolean isOverlap = ScheduleValidator.isOverlapping(timeIn, timeOut, schedules);

                    if (isValidCapacity && isValidFloor && !isOverlap) {
                        availableRooms.add(room);
                    }
                }
            }
            showRoomBrowser(availableRooms, course);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void buildRequestSchedule(String timeIn, String timeOut, Course course, int capacity, int floor,
            SearchRooms1 searchRooms1) {
        requestSchedule = new RequestSchedule();
        Student student = new StudentDAO().get(user.getUserID());
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        if (user.getUserType().equals("Faculty")) {
            requestSchedule.load(-1, "", String.valueOf(searchRooms1.getSection().getSectionKey()), course.getCode(),
                    user.getUserID(),
                    timeIn, timeOut, DateTimeBuilder.getDayName(), "3", 0, DateTimeBuilder.getCurrentDate(),
                    user.getUserID());
        } else {
            requestSchedule.load(-1, "", String.valueOf(student.getSectionKey()), course.getCode(),
                    scheduleDAO.getFacultyIDByStudentCourse(student.getUserID(), course.getCode()),
                    timeIn, timeOut, DateTimeBuilder.getDayName(), "1", 0, DateTimeBuilder.getCurrentDate(),
                    student.getUserID());
        }
    }

    void showRoomBrowser(List<Room> availableRooms, Course course) throws SQLException {
        RoomBrowser roomBrowser = new RoomBrowser(null, availableRooms);
        MainFrame.addContentPanel(roomBrowser, "RoomBrowser");
        MainFrame.showPanel("RoomBrowser", "Browse Room");

        roomBrowser.setOnBackButton(e -> MainFrame.showPanel("SearchRooms", "Search Rooms"));
        roomBrowser.setOnConfirmButton(e -> {
            Room selectedRoom = roomBrowser.getSelectedRoom();
            if (selectedRoom == null) {
                MainFrame.setNotification("Please Choose a Room First");
                roomBrowser.clearSelection();
            }
            showRoomSchedule(selectedRoom);
        });
    }

    void showRoomSchedule(Room selectedRoom) {
        requestSchedule.setRoomCode(selectedRoom.getRoomCode());
        new BookingController(user, selectedRoom, requestSchedule);
    }
}