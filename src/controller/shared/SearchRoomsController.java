package controller.shared;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    void loadSection(SearchRooms1 searchRooms1) {
        Course selectedCourse = searchRooms1.getCourse();
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        List<Section> sections = scheduleDAO.getSectionByFacultyCourse(selectedCourse.getCode(), user.getUserID());
        searchRooms1.loadSection(sections);
    }

    void handleTimeChange(SearchRooms1 searchRooms) {
        String rawTimeIn = searchRooms.getTimeIn(); // "7:00 AM"
        String rawTimeOut = searchRooms.getTimeOut(); // "6:00 PM"

        // parse hour, minute, meridiem from "h:mm AM/PM"
        String[] inParts = rawTimeIn.split("[ :]"); // ["7", "00", "AM"]
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

        // clamp time in: 7AM to 7PM (latest start for 1hr min duration)
        if (inHour24 < 7) {
            inHour24 = 7;
            searchRooms.setTimeIn(7, inMinute, "AM");
        }
        if (inHour24 > 19) {
            inHour24 = 19;
            searchRooms.setTimeIn(7, inMinute, "PM");
        }

        // clamp time out: at least timeIn + 1hr, max 8PM
        if (outHour24 < inHour24 + 1) {
            outHour24 = inHour24 + 1;
            int out12 = outHour24 > 12 ? outHour24 - 12 : outHour24;
            String outMerNew = outHour24 >= 12 ? "PM" : "AM";
            searchRooms.setTimeOut(out12, inMinute, outMerNew);
        }
        if (outHour24 > 20) {
            outHour24 = 20;
            searchRooms.setTimeOut(8, inMinute, "PM");
        }

        // store formatted
        timeIn = DateTimeBuilder.formatTo12Hour(inHour24, inMinute);
        timeOut = DateTimeBuilder.formatTo12Hour(outHour24, outMinute);
    }

    void onConfirmClicked(SearchRooms1 searchRooms) {
        if (searchRooms.getTimeIn() == null || searchRooms.getTimeOut() == null) {
            System.out.println("Please put the necessary fields.");
            return;
        } else if (searchRooms.getCourse() == null) {
            System.out.println("Please select a course");
            return;
        } else if ((searchRooms.getCourse() == null || searchRooms.getSection() == null)
                && user.getUserType().equals("Faculty")) {
            System.out.println("Please select a course and section");
            return;
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
                    room.loadSchedules(new ScheduleDAO().getRoom(room.getRoomCode())); // load the schedules for each
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
