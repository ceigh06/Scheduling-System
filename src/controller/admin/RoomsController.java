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
import model.schedule.Schedule;
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

    // 1.1 - BrowseBuilding pattern (already correct)
    void showBrowseBuilding() throws SQLException {
        BrowseBuilding browseBuilding = new BrowseBuilding();
        BuildingDAO buildingDAO = new BuildingDAO();
        List<Building> buildings = buildingDAO.getAllBuilding();

        browseBuilding.loadBuilding(buildings);

        // Pattern: Click immediately navigates
        browseBuilding.setOnBuildingClicked(building -> {
            try {
                showRoomBrowser(building);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        AdminMainframe.addContentPanel(browseBuilding, "BrowseBuilding");
        AdminMainframe.showPanel("BrowseBuilding");
    }

    // 1.2 - RoomBrowser now follows BrowseBuilding pattern
    void showRoomBrowser(Building building) throws SQLException {
        RoomDAO roomDAO = new RoomDAO();
        List<Room> rooms = roomDAO.getAllRooms(building.getCode().trim());
        
        // Create RoomBrowser with rooms
        RoomBrowser roomBrowser = new RoomBrowser(building.getName(), rooms);

        // NEW: Follow BrowseBuilding pattern - click room immediately navigates
        // (Assuming RoomBrowser has setOnRoomClicked like BrowseBuilding has setOnBuildingClicked)
        roomBrowser.setOnRoomClicked(room -> {
            showRoomSchedule(room);
        });

        // Back button still needed for navigation
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

        AdminMainframe.addContentPanel(roomBrowser, "RoomBrowser");
        AdminMainframe.showPanel("RoomBrowser");
    }

    // 1.3 - AdminViewSchedule also follows same pattern
    void showRoomSchedule(Room selectedRoom) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        CourseDAO courseDAO = new CourseDAO();

        selectedRoom.loadSchedules(scheduleDAO.getRoom(selectedRoom.getRoomCode()));
        List<Course> facultyCourses = courseDAO.getFacultyCourses(user.getUserID());

        AdminViewSchedule viewSchedule = new AdminViewSchedule(selectedRoom);
        viewSchedule.loadClassSchedule(selectedRoom);

        // Pattern: Click schedule immediately navigates to edit
        viewSchedule.setOnScheduleClicked(schedule -> {
            onScheduleEditClicked(schedule, selectedRoom);
        });

        // Back button
        viewSchedule.setOnBackClicked(e -> {
            AdminMainframe.showPanel("RoomBrowser");
        });

        // Remove or keep confirm based on your needs
        viewSchedule.setOnConfirmClicked(e -> {
            // Optional: handle confirm action
        });

        AdminMainframe.addContentPanel(viewSchedule, "Schedule");
        AdminMainframe.showPanel("Schedule");
    }

    // Schedule edit handler
    public void onScheduleEditClicked(Schedule schedule, Room room) {
        new EditRoomScheduleController(schedule, room, user);
    }
}