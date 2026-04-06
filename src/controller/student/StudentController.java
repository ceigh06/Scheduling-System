package controller.student;

import controller.shared.BookingController;
import controller.shared.ProfileController;
import controller.shared.RoomsController;
import controller.shared.SearchRoomsController;
import dao.schedule.RequestScheduleDAO;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import model.Room;
import model.user.Student;
import model.user.User;
import view.common.MainFrame;
import view.common.TitleHeader;
import view.landing.Landing;

public class StudentController {

    User user;

    public StudentController(User user) throws SQLException {
        this.user = user;

        Landing landing = new Landing();
        List<Room> mostRequestedRooms = new RequestScheduleDAO().getMostRequestedRoomsThisWeek(5);
        List<Room> mostAvailableRooms = new RequestScheduleDAO().getMostAvailableRooms(5);
        landing.loadLandingContent();
        landing.loadRooms("Most Requested Rooms This Week", mostRequestedRooms);
        landing.loadRooms("Most Available Rooms Today", mostAvailableRooms);

        landing.setOnRoomClicked(room -> {
            new BookingController(user, room);
        });

        MainFrame.setCurrentUser(user, true);
        MainFrame.addContentPanel(landing, "Landing");
        MainFrame.showPanel("Landing", "RoomMate");
        Student student = new Student();
        MainFrame.setIconType(student);
        TitleHeader.addIconToHeader();
        MainFrame.setNavBarVisible(true);

        landing.setOnSearchAction(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    MainFrame.restoreNavBarDefaultState();
                    onSearchClicked();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        MainFrame.setOnBrowsePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    onBrowseClicked();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        MainFrame.setOnHomePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onHomeClicked();
            }
        });

        MainFrame.setOnRequestPanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRequestClicked();
            }
        });

        MainFrame.setOnProfilePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onProfileClicked();
            }
        });

        MainFrame.setOnRequestHistoryPanel(student, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RequestHistoryController(user);
            }
        });

    }

    public void onSearchClicked() throws SQLException {
        new SearchRoomsController(user);
    }

    public void onBrowseClicked() throws SQLException {
        new RoomsController(user);
    }

    public void onHomeClicked() {
        MainFrame.showPanel("Landing", "RoomMate");
    }

    public void onRequestClicked() {
        new RequestsController(user);
    }

    public void onProfileClicked() {
        new ProfileController(user);
    }

    public void onRequestHistoryClicked() {
        new RequestHistoryController(user);
    }
}
