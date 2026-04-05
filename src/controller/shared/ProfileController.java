package controller.shared;

import controller.login.LoginController;
import dao.FacultyDAO;
import dao.StudentDAO;
import java.sql.SQLException;
import model.user.Faculty;
import model.user.Student;
import model.user.User;
import utilities.LoginValidator;
import view.common.MainFrame;
import view.common.TitleHeader;
import view.common.ViewProfile;

public class ProfileController {

    User user;
    ViewProfile viewProfile;

    public ProfileController(User user) {
        this.user = user;
        viewProfile = new ViewProfile();
        String registeredUser = user.getUserType();
        switch (registeredUser) {
            case "Student":
                Student student = new StudentDAO().get(user.getUserID());
                viewProfile.loadUser(student);
                break;
            case "Faculty":
                Faculty faculty = new FacultyDAO().get(user.getUserID());
                viewProfile.loadUser(faculty);
                break;
            case "Admin":
                break;
        }

        showProfile();

        viewProfile.setOnBackClicked(e -> {
            try {
                onBackClicked();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        viewProfile.setOnLogoutClicked(e -> onLogoutClicked());
    }

    void showProfile() {
        MainFrame.addContentPanel(viewProfile, "Profile");
        MainFrame.showPanel("Profile");
    }

    private void onLogoutClicked() {
        MainFrame.restoreNavBarDefaultState();
        user = null;
        LoginController.clearLoginFields();
        LoginValidator.clearAuthenticatedUser();
        MainFrame.setNavBarVisible(false);
        TitleHeader.removeIconFromHeader();
        MainFrame.showPanel("login", "Login Page");
    }

    private void onBackClicked() throws SQLException {
        if (user.getUserType() == "Student") {
            MainFrame.showPanel("StudentLanding");
        } else if (user.getUserType() == "Faculty") {
            MainFrame.showPanel("FacultyLanding");
        } else if (user.getUserType() == "Admin") {
            MainFrame.showPanel("AdminLanding");
        }
    }

}
