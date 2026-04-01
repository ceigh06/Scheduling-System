package controller.faculty;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import controller.shared.BookingController;
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

        browseBuilding.setOnBuildingClicked(building -> { // model is used as a reference to know which button is clicked
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
            new BookingController(user, selectedRoom);
        });

    }

    
}