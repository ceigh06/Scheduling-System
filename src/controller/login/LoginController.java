package controller.login;

import controller.admin.AdminController;
import controller.faculty.FacultyController;
import controller.student.StudentController;
import dao.schedule.RequestScheduleDAO;

import java.sql.SQLException;

import org.jfree.chart.title.Title;

import model.user.Faculty;
import model.user.Student;
import model.user.User;
import utilities.DBConnection;
import utilities.DateTimeBuilder;
import utilities.LoginValidator;
import view.common.MainFrame;
import view.common.TitleHeader;
import view.landing.Login;

public class LoginController {
    User authenticatedUser;
    // switch method to determine which login page to show
    // user -> create controller pass the frame.

    private static Login loginView; // makes it sure that we can clear the fields from other controllers when
                                    // logging out

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
                try {
                    createUserDashBoard();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            } else {
                MainFrame.setNotification(LoginValidator.getErrorMessage());
            }
        });
    }

    void createUserDashBoard() throws SQLException {
        // RequestScheduleDAO.voidOverdueRequest(); // removes the overdue requests schedules
        DBConnection.disconnect(); // logs out the db connection with the user login.

        if (authenticatedUser.getUserType().equals("Student")) {
            new DBConnection("26.218.110.33:1433", "SchedulingSystem", "student_user",
                    "1234");
            if (DateTimeBuilder.getDayName().equals("Sunday")) {
                MainFrame.setNotification("Room scheduling is not available every Sunday.");
                return;
            }
            System.out.println("Student");
            try {

                new StudentController(authenticatedUser);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (authenticatedUser.getUserType().equals("Faculty")) {
            new DBConnection("26.218.110.33:1433", "SchedulingSystem", "faculty_user",
                    "1234");
            if (DateTimeBuilder.getDayName().equals("Sunday")) {
                MainFrame.setNotification("Room scheduling is not available every Sunday.");
                return;
            }
            System.out.println("Faculty");
            new FacultyController(authenticatedUser);
        } else if (authenticatedUser.getUserType().equals("Admin")) { // admin
            new DBConnection("26.218.110.33:1433", "SchedulingSystem", "admin_user",
                    "1234");
            System.out.println("Admin");
            new AdminController(authenticatedUser);
        }
    }

}
