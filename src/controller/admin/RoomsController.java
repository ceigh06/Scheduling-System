package controller.admin;

import dao.BuildingDAO;
import dao.CourseDAO;
import dao.RoomDAO;
import dao.schedule.ScheduleDAO;
import java.sql.SQLException;
import java.util.List;
import model.Building;
import model.Course;
import model.Room;
import model.user.User;
import view.admin.AdminMainframe;
import view.admin.AdminViewSchedule;
import view.common.BrowseBuilding;
import view.common.RoomBrowser;

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

        browseBuilding.setOnBuildingClicked(building -> { // model is used as a reference to know which button is clicked
            try {
                showRoomBrowser(building);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        AdminMainframe.addContentPanel(browseBuilding, "BrowseBuilding");
        AdminMainframe.showPanel("BrowseBuilding");
    }

    // 1.2
    void showRoomBrowser(Building building) throws SQLException {

        RoomDAO roomDAO = new RoomDAO();
        List<Room> rooms = roomDAO.getAllRooms(building.getCode().trim());
        RoomBrowser roomBrowser = new RoomBrowser(building.getName(), rooms);

        AdminMainframe.addContentPanel(roomBrowser, "RoomBrowser");
        AdminMainframe.showPanel("RoomBrowser");

        roomBrowser.setOnBackButton(e -> {
            AdminMainframe.showPanel("BrowseBuilding");
        });

        roomBrowser.setOnConfirmButton(e -> {
            Room selectedRoom = roomBrowser.getSelectedRoom();
            if (selectedRoom == null) {
                AdminMainframe.setNotification("Please Choose a Room First");
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

        AdminViewSchedule viewSchedule = new AdminViewSchedule(selectedRoom); // load the view with the room and its schedules
        viewSchedule.loadClassSchedule(selectedRoom);

        attachShowRoomScheduleListeners(viewSchedule);

        AdminMainframe.addContentPanel(viewSchedule, "Schedule");
        AdminMainframe.showPanel("Schedule");

    }



    void attachShowRoomScheduleListeners(AdminViewSchedule viewSchedule) {
        viewSchedule.setOnBackClicked(e -> {
            AdminMainframe.showPanel("RoomBrowser");
        });

        viewSchedule.setOnConfirmClicked(e -> {

        });

    }
}