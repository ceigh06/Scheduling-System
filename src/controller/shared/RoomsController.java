package controller.shared;

import java.sql.SQLException;
import java.util.List;

import dao.BuildingDAO;
import dao.RoomDAO;
import model.Building;
import model.Room;
import model.user.User;
import view.common.BrowseBuilding;
import view.common.MainFrame;
import view.common.RoomBrowser;

public class RoomsController {

    User user;

    public RoomsController(User user) throws SQLException {
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
            new BookingController(user, selectedRoom);
        });

    }

}