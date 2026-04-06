package controller.shared;

import java.awt.HeadlessException;
import java.sql.SQLException;
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
import view.common.ViewSchedule;

public class SearchRoomsController {
    String timeIn;
    String timeOut;
    User user;
    RequestSchedule requestSchedule; // since the request schedule is not yet created, we can just initialize it here
                                     // and load the data later when the user confirms the search.

    public SearchRoomsController(User user) {
        this.user = user;
        try {
            showSearch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void showSearch() throws SQLException {
        boolean isFaculty = false;
        if (user.getUserType().equals("Faculty"))
            isFaculty = true;

        SearchRooms1 searchRooms = new SearchRooms1(user);

        BuildingDAO buildingDAO = new BuildingDAO();// dao
        List<Building> buildings = buildingDAO.getAllBuilding(); // model
        searchRooms.loadBuilding(buildings);

        List<Course> courses = new ArrayList<>();
        CourseDAO courseDAO = new CourseDAO();
        if (isFaculty) {
            courses = courseDAO.getFacultyCourses(user.getUserID()); // courses
        } else {
            courses = courseDAO.getStudentCourse(user.getUserID());
        }

        searchRooms.loadCourse(courses);

        MainFrame.addContentPanel(searchRooms, "SearchRooms");
        MainFrame.showPanel("SearchRooms");

        handleTimeChangeSilent(searchRooms);

        searchRooms.setOnTimeInChanged(e -> handleTimeChange(searchRooms));
        searchRooms.setOnTimeOutChanged(e -> handleTimeChange(searchRooms));

        if (isFaculty) {
            searchRooms.setOnCourseChanged(e -> {
                loadSection(searchRooms);
            });
        }

        searchRooms.setOnClearButton(e -> {
            searchRooms.clearAll();
        });

        searchRooms.setOnConfirmButton(e -> {
            onConfirmClicked(searchRooms);
        });
    }

    void handleTimeChangeSilent(SearchRooms1 searchRooms) {
        // set spinners to 7:00 AM and 8:00 AM as defaults
        searchRooms.setTimeIn(7, 0, "AM");
        searchRooms.setTimeOut(8, 0, "AM");

        timeIn = DateTimeBuilder.formatTo12Hour(7, 0);
        timeOut = DateTimeBuilder.formatTo12Hour(8, 0);
    }

    void loadSection(SearchRooms1 searchRooms1) {
        Course selectedCourse = searchRooms1.getCourse();
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        List<Section> sections = scheduleDAO.getSectionByFacultyCourse(selectedCourse.getCode(), user.getUserID());
        searchRooms1.loadSection(sections);
    }

    void handleTimeChange(SearchRooms1 searchRooms) {
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

        // convert to 24hr
        int inHour24 = inHour;
        if (inMer.equals("PM") && inHour != 12)
            inHour24 += 12;
        if (inMer.equals("AM") && inHour == 12)
            inHour24 = 0;

        int outHour24 = outHour;
        if (outMer.equals("PM") && outHour != 12)
            outHour24 += 12;
        if (outMer.equals("AM") && outHour == 12)
            outHour24 = 0;

        String notification = null;

        // clamp time in: 7AM to 7PM
        if (inHour24 < 7) {
            inHour24 = 7;
            notification = "Time In cannot be earlier than 7:00 AM.";
        } else if (inHour24 > 19) {
            inHour24 = 19;
            notification = "Time In cannot be later than 7:00 PM.";
        }

        int in12 = inHour24 > 12 ? inHour24 - 12 : (inHour24 == 0 ? 12 : inHour24);
        String inMerNew = inHour24 >= 12 ? "PM" : "AM";
        searchRooms.setTimeIn(in12, inMinute, inMerNew);

        // clamp time out — order matters, hard cap first
        if (outHour24 > 20) {
            outHour24 = 20;
            notification = "Time Out cannot exceed 8:00 PM.";
        } else if (outHour24 > inHour24 + 3) {
            outHour24 = inHour24 + 3;
            notification = "Maximum duration is 3 hours.";
        } else if (outHour24 < inHour24 + 1) {
            outHour24 = inHour24 + 1;
            notification = "Minimum duration is 1 hour.";
        }

        int out12 = outHour24 > 12 ? outHour24 - 12 : (outHour24 == 0 ? 12 : outHour24);
        String outMerNew = outHour24 >= 12 ? "PM" : "AM";
        searchRooms.setTimeOut(out12, outMinute, outMerNew);

        // store formatted
        timeIn = DateTimeBuilder.formatTo12Hour(inHour24, inMinute);
        timeOut = DateTimeBuilder.formatTo12Hour(outHour24, outMinute);

        // show notification after spinners are set to avoid re-entry issues
        if (notification != null) {
            JOptionPane.showMessageDialog(null, notification, "Invalid Time", JOptionPane.WARNING_MESSAGE);
        }
    }

    void onConfirmClicked(SearchRooms1 searchRooms) {
        try {
            if (searchRooms.getChosenBuildings().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please select a building");
                return;

            }

            else if (searchRooms.getTimeIn() == null || searchRooms.getTimeOut() == null) {
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
        } catch (HeadlessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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
                    room.loadSchedules(new ScheduleDAO().getRoom(room.getRoomCode())); // load the schedules for
                                                                                       // each
                                                                                       // room to check
                    List<Schedule> schedules = room.getSchedules();
                    if (schedules == null) {
                        schedules = new ArrayList<>();
                        System.out.println("Schedules is null for room: " + room.getRoomCode());
                        // catching incase the room has no schedule
                    }

                    boolean isValidCapacity = room.getCapacity() >= capacity;
                    boolean isValidFloor = room.getFloor() == floor || floor == 0 ? true : false;
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
            SearchRooms1 searchRooms1) { // process the
        // request here
        requestSchedule = new RequestSchedule();
        Student student = new StudentDAO().get(user.getUserID());
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        if (user.getUserType().equals("Faculty")) {
            requestSchedule.load(-1, "", String.valueOf(searchRooms1.getSection().getSectionKey()), course.getCode(),
                    user.getUserID(),
                    timeIn, timeOut, DateTimeBuilder.getDayName(), "3", 0, DateTimeBuilder.getCurrentDate(),
                    user.getUserID()); // load the request schedule with the data from the search form. The ID and
                                       // RoomCode are set to default values since they are not yet created.
        } else {
            requestSchedule.load(-1, "", String.valueOf(student.getSectionKey()), course.getCode(),
                    scheduleDAO.getFacultyIDByStudentCourse(student.getUserID(), course.getCode()),
                    timeIn, timeOut, DateTimeBuilder.getDayName(), "1", 0, DateTimeBuilder.getCurrentDate(),
                    student.getUserID()); // load the request schedule with the data from the search form. The ID and
                                          // RoomCode are set to default values since they are not yet created.
        }

    }

    void showRoomBrowser(List<Room> availableRooms, Course course) throws SQLException {
        RoomBrowser roomBrowser = new RoomBrowser(null, availableRooms);
        MainFrame.addContentPanel(roomBrowser, "RoomBrowser");
        MainFrame.showPanel("RoomBrowser");

        roomBrowser.setOnBackButton(e -> {
            MainFrame.showPanel("SearchRooms");
        });

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
