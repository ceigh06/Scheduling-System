package controller.student;

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
    }

    void showProfile() {
        MainFrame.addContentPanel(viewProfile, "Profile");
        MainFrame.showPanel("Profile");
    }
    
}
