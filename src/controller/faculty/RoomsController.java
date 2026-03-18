package controller.faculty;

import java.sql.SQLException;
import java.util.List;

import dao.BuildingDAO;
import dao.RoomDAO;
import model.Building;
import model.Room;
import view.common.BrowseBuilding;
import view.common.MainFrame;
import view.common.RoomBrowser;

public class RoomsController {
    MainFrame frame;

    RoomsController(MainFrame frame) throws SQLException {
        this.frame = frame;
        showBrowseBuilding();
    }

    void showBrowseBuilding() throws SQLException {
        BrowseBuilding browseBuilding = new BrowseBuilding(); //view
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

    void showRoomBrowser(Building building) throws SQLException {
        
        RoomDAO roomDAO = new RoomDAO();
        List<Room> rooms = roomDAO.getAllRooms(building.getCode().trim());
        RoomBrowser roomBrowser = new RoomBrowser(building.getName(), rooms);
        
        MainFrame.addContentPanel(roomBrowser, "RoomBrowser");
        MainFrame.showPanel("RoomBrowser");
    }
    
    
}
