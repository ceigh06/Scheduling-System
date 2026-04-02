package controller.shared;

import java.sql.SQLException;

import controller.student.StudentController;
import dao.StudentDAO;
import model.user.Student;
import model.user.User;
import view.common.MainFrame;
import view.common.ViewProfile;

public class ProfileController {

    User user;
    ViewProfile viewProfile;

    public ProfileController(User user) {
        this.user = user;
        viewProfile = new ViewProfile();
        Student student = new StudentDAO().get(user.getUserID());
        viewProfile.loadUser(student);
        showProfile();

        viewProfile.setOnBackClicked(e -> onBackClicked());
        viewProfile.setOnLogoutClicked(e -> onLogoutClicked());
    }

    void showProfile() {
        MainFrame.addContentPanel(viewProfile, "Profile");
        MainFrame.showPanel("Profile");
    }

    private void onLogoutClicked() {
        MainFrame.restoreNavBarDefaultState();
        user = null;
        MainFrame.setNavBarVisible(false);
        MainFrame.showPanel("login");
    }

    private void onBackClicked() {
        try {
            new StudentController(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
    }

}
