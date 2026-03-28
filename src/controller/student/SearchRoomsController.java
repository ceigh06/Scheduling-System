package controller.student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BuildingDAO;
import dao.EnrolledCoursesDAO;
import dao.RoomDAO;
import model.Building;
import model.Course;
import model.Room;
import model.schedule.Schedule;
import model.user.User;
import view.common.MainFrame;
import view.common.RoomBrowser;
import view.common.SearchRooms1;
import dao.BuildingDAO;

public class SearchRoomsController {

    User user;

    public SearchRoomsController(User user) {
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

        EnrolledCoursesDAO enrolledCoursesDAO = new EnrolledCoursesDAO();
        List<Course> courses = enrolledCoursesDAO.getStudentCourse(user.getUserID());
        searchRooms.loadCourse(courses);

        MainFrame.addContentPanel(searchRooms, "SearchRooms");
        MainFrame.showPanel("SearchRooms");

        searchRooms.setOnClearButton(e -> {
            searchRooms.clearAll();
        });

        searchRooms.setOnConfirmButton(e -> {
            try {
                Schedule requestSchedule = new Schedule();
                List<Building> checkedBuildings = searchRooms.getChosenBuildings();
                String timeIn = searchRooms.getTimeIn();
                String timeOut = searchRooms.getTimeOut();
                Course courseCode = searchRooms.getCourse();
                String floor = searchRooms.getFloorLevel();
                String capacity = searchRooms.getCapacity();

                showRoomBrowser();

            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    void showRoomBrowser() {

    }

}
