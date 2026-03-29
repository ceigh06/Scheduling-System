package controller.faculty;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import dao.BuildingDAO;
import dao.CourseDAO;
import dao.RoomDAO;
import dao.schedule.ScheduleDAO;
import model.Building;
import model.Course;
import model.Room;
import model.user.Student;
import model.user.User;
import service.ScheduleValidator;
import view.common.BrowseBuilding;
import view.common.MainFrame;
import view.common.RoomBrowser;
import view.common.ViewSchedule;

public class RoomsController {

    User user;

    RoomsController(User user) throws SQLException {
        this.user = user;
        showBrowseBuilding();
    }

    // 1.1
    void showBrowseBuilding() throws SQLException {
        BrowseBuilding browseBuilding = new BrowseBuilding(); // view
        BuildingDAO buildingDAO = new BuildingDAO();// dao
        List<Building> buildings = buildingDAO.getAllBuilding(); // model

        browseBuilding.loadBuilding(buildings);

        browseBuilding.setOnBuildingClicked(building -> { // model is used as a reference to know which button is
                                                          // clicked
            try {
                showRoomBrowser(building);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        MainFrame.addContentPanel(browseBuilding, "BrowseBuilding");
        MainFrame.showPanel("BrowseBuilding");
    }

    // 1.2
    void showRoomBrowser(Building building) throws SQLException {

        RoomDAO roomDAO = new RoomDAO();
        List<Room> rooms = roomDAO.getAllRooms(building.getCode().trim());
        RoomBrowser roomBrowser = new RoomBrowser(building.getName(), rooms);

        MainFrame.addContentPanel(roomBrowser, "RoomBrowser");
        MainFrame.showPanel("RoomBrowser");

        roomBrowser.setOnBackButton(e -> {
            MainFrame.showPanel("BrowseBuilding");
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

    // 1.3
    private String timeIn = "";
    private String timeOut = "";

    void showRoomSchedule(Room selectedRoom) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        selectedRoom.loadSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode())); // schedules for room
        List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID()); // courses
        // doesnt catch if the courses is null.

        ViewSchedule viewSchedule = new ViewSchedule(selectedRoom); // load the
        viewSchedule.loadClassSchedule(selectedRoom);

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