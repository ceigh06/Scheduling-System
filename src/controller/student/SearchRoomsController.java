package controller.student;

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
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.Student;
import model.user.User;
import service.ScheduleValidator;
import view.common.MainFrame;
import view.common.RoomBrowser;
import view.common.SearchRooms1;
import view.common.ViewSchedule;
import dao.BuildingDAO;

public class SearchRoomsController {
    
    User user;
    RequestSchedule requestSchedule; // since the request schedule is not yet created, we can just initialize it here and load the data later when the user confirms the search.
    
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

        CourseDAO enrolledCoursesDAO = new CourseDAO();
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

                requestSchedule = new RequestSchedule();
                Student student = new StudentDAO().get(user.getUserID()); 
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                requestSchedule.load(-1, "", String.valueOf(student.getSectionKey()), course.getCode(), scheduleDAO.getFacultyIDByStudentCourse(student.getUserID(), course.getCode()),
                 timeIn, timeOut, "", "1", 0, "DateRequestedNotInitialized", student.getUserID()); // load the request schedule with the data from the search form. The ID and RoomCode are set to default values since they are not yet created.
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
