package controller.admin;

import controller.login.LoginController;
import model.user.User;
import utilities.LoginValidator;
import view.common.MainFrame;
import view.common.ViewProfile;

public class ProfileController {

    User user;
    ViewProfile viewProfile;

    public ProfileController(User user) {

        this.user = user;
        createProfile();
        showProfile();

        viewProfile.setOnBackClicked(e -> onBackClicked());
        viewProfile.setOnLogoutClicked(e -> onLogoutClicked());
    }

    void showProfile() {
        MainFrame.addContentPanel(viewProfile, "Profile");
        MainFrame.showPanel("Profile");
    }

    private void createProfile() {
        viewProfile = new ViewProfile();

    }

    private void onLogoutClicked() {
        MainFrame.restoreNavBarDefaultState();
        user = null;
        LoginController.clearLoginFields();
        LoginValidator.clearAuthenticatedUser();
        MainFrame.setNavBarVisible(false);
        MainFrame.showPanel("login");
    }

    private void onBackClicked() {
        if (user.getUserType() == "Student") {
            // new StudentController(user);
        } else if (user.getUserType() == "Faculty") {
            //new FacultyController(user);
        } else if (user.getUserType() == "Admin") {
            new AdminController(user);
        }
    }

}
