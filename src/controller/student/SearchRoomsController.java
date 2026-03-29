package controller.student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BuildingDAO;
import dao.EnrolledCoursesDAO;
import dao.RoomDAO;
import dao.schedule.ScheduleDAO;
import model.Building;
import model.Course;
import model.Room;
import model.schedule.Schedule;
import model.user.User;
import service.ScheduleValidator;
import view.common.MainFrame;
import view.common.RoomBrowser;
import view.common.SearchRooms1;
import view.common.ViewSchedule;
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
                RoomDAO roomDAO = new RoomDAO();
                List<Building> checkedBuildings = searchRooms.getChosenBuildings();
                String timeIn = searchRooms.getTimeIn();
                String timeOut = searchRooms.getTimeOut();
                Course course = searchRooms.getCourse();
                int capacity = searchRooms.getCapacity();
                int floor = searchRooms.getFloorLevel();

                List<Room> availableRooms = new ArrayList<>();
                for (Building building : checkedBuildings) {
                    List<Room> roomsToCheck = roomDAO.getAllRooms(building.getCode());
                    for (Room room : roomsToCheck) {
                        List<Schedule> schedules = room.getSchedules();
                        if (schedules == null) {
                            schedules = new ArrayList<>();
                        }

                        boolean isValidCapacity = room.getCapacity() >= capacity;
                        boolean isValidFloor = room.getFloor() == floor || floor == 0 ? true : false;
                        boolean isOverlap = !ScheduleValidator.isOverlapping(timeIn, timeOut, schedules);

                        if (isValidCapacity && isValidFloor && isOverlap) {
                            availableRooms.add(room);
                        }
                    }
                }
                showRoomBrowser(availableRooms, course);

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
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
            if (selectedRoom == null){
                MainFrame.setNotification("Please Choose a Room First");
                roomBrowser.clearSelection();
            }
            showRoomSchedule(selectedRoom);
        });
    }

    void showRoomSchedule(Room selectedRoom){
        
    }
}
