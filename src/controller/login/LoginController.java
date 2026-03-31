package controller.login;

import controller.admin.AdminController;
import controller.faculty.FacultyController;
import controller.student.StudentController;
import java.sql.SQLException;
import model.user.User;
import utilities.LoginValidator;
import view.admin.AdminMainframe;
import view.common.MainFrame;
import view.landing.Login;

public class LoginController {
    User authenticatedUser;
    //switch method to determine which login page to show
        // user -> create controller pass the frame.


    private static Login loginView; // makes it sure that we can clear the fields from other controllers when logging out

    public LoginController() {
        
        MainFrame.setNavBarVisible(false);
        loginView = new Login();
        MainFrame.addContentPanel(loginView, "login");
        MainFrame.showPanel("login", "Log In");

        attachLoginListener(loginView);
    }

    //
     public static void clearLoginFields() {
        if (loginView != null) {
            loginView.clearFields();
        }
    }

    void attachLoginListener(Login view) {
        view.setOnLoginButton(e -> {
            if (LoginValidator.validate(view.getUsername(), view.getPassword())) {
                authenticatedUser = LoginValidator.getAuthenticatedUser();
                
                view.clearFields();
                createUserDashBoard(); //redirect to dashboard

            } else {
                MainFrame.setNotification(LoginValidator.getErrorMessage());
            }
        });
    }

    void createUserDashBoard() {
        if (authenticatedUser.getUserType().equals("Student")) {
            System.out.println("Student");
            try {
                new StudentController(authenticatedUser);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (authenticatedUser.getUserType().equals("Faculty")) {
            System.out.println("Faculty");
            new FacultyController(authenticatedUser);
        } else if (authenticatedUser.getUserType().equals("Admin")) { // admin
            System.out.println("Admin");
            MainFrame.getFrame().setVisible(false);
            AdminMainframe.init();
            new AdminController(authenticatedUser);
        }
    }

}
