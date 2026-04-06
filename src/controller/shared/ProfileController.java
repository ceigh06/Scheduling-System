package controller.shared;

import controller.login.LoginController;
import dao.FacultyDAO;
import dao.LookUpDAO;
import dao.StudentDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

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
        LookUpDAO lookUp = new LookUpDAO();
        switch (registeredUser) {
            case "Student":
                List<String> dataStudent = new ArrayList<>();
                Student student = new StudentDAO().get(user.getUserID());
                dataStudent.add(lookUp.getFullStudentName(student.getStudentID()));
                dataStudent.add(lookUp.getFullCollege(student.getStudentID()));
                dataStudent.add(lookUp.getFullProgramName(student.getStudentID()));
                dataStudent.add(lookUp.getFullSectionName(student.getSectionKey()));
                viewProfile.loadUser(student, dataStudent);
                break;
            case "Faculty":
                List<String> dataFaculty = new ArrayList<>();
                Faculty faculty = new FacultyDAO().get(user.getUserID());
                dataFaculty.add(lookUp.getFullFacultyName(faculty.getFacultyID()));
                dataFaculty.add(lookUp.getFullCollegeFaculty(faculty.getCollegeCode()));
                viewProfile.loadUser(faculty, dataFaculty);
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
        MainFrame.showPanel("Profile", "My Profile");
    }

    private void onLogoutClicked() {
        MainFrame.restoreNavBarDefaultState();
        TitleHeader.removeIconFromHeader(user.getUserType());
        user = null;
        LoginController.clearLoginFields();
        LoginValidator.clearAuthenticatedUser();
        MainFrame.setNavBarVisible(false);
        MainFrame.showPanel("login", "Login Page");
    }

    private void onBackClicked() throws SQLException {
        if (user.getUserType() == "Student") {
            MainFrame.showPanel("Landing", "RoomMate");
        } else if (user.getUserType() == "Faculty") {
            MainFrame.showPanel("Landing", "RoomMate");
        } else if (user.getUserType() == "Admin") {
            MainFrame.showPanel("AdminLanding", "RoomMate");
        }
    }

}
