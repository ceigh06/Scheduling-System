package controller.student;

import java.sql.SQLException;
import java.util.List;

import dao.BuildingDAO;
import model.Building;
import model.user.User;
import view.common.MainFrame;
import view.common.SearchRooms1;
import dao.BuildingDAO;

public class SearchRoomsController {

    User user;

    public SearchRoomsController(User user)  {
        this.user = user;
        try {
            showSearch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void showSearch() throws SQLException {
        SearchRooms1 searchRooms = new SearchRooms1();

        BuildingDAO buildingDAO = new BuildingDAO();// dao
        List<Building> buildings = buildingDAO.getAllBuilding(); // model
        searchRooms.loadBuilding(buildings);
        MainFrame.addContentPanel(searchRooms, "SearchRooms");
        MainFrame.showPanel("SearchRooms");

        
    }

}
