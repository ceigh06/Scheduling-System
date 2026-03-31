package controller.admin;

import model.user.User;
import view.admin.AdminMainframe;
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
        AdminMainframe.addContentPanel(viewProfile, "Profile");
        AdminMainframe.showPanel("Profile");
    }
    
    private void createProfile() {
        viewProfile = new ViewProfile();
        
        
    }

    private void onLogoutClicked() {
        user = null;
        AdminMainframe.setNavBarVisible(false);
        AdminMainframe.showPanel("login");
    }

    private void onBackClicked(){
        if (user.getUserType() == "Student") {
            // new StudentController(user);
        } else if (user.getUserType() == "Faculty") {
            //new FacultyController(user);
        } else if (user.getUserType() == "Admin") {
            new AdminController(user);
        }
    }

}
