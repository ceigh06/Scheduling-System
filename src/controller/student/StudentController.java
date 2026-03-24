package controller.student;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import controller.admin.SearchRoomsController;
import view.common.BrowseBuilding;
import view.common.MainFrame;
import view.landing.Landing;

import model.user.User;

public class StudentController {

    User user;

    public StudentController(User user) {
        this.user = user;

        MainFrame.addContentPanel(new Landing(), "StudentLanding");
        MainFrame.showPanel("StudentLanding");
        MainFrame.setNavBarVisible(true);


    }

    public void onSearchClicked() throws SQLException {
        new SearchRoomsController();
    }

    public void onBrowseClicked() throws SQLException {
        new RoomsController(user);
    }

    public void onHomeClicked() {
        MainFrame.showPanel("StudentLanding");
    }   

    public void onRequestClicked() {
       new FacultyController(user);
    }

    public void onProfileClicked() {
        new ProfileController(user);
    }
}
