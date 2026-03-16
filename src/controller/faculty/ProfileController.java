package controller.faculty;

import view.common.MainFrame;
import view.common.ViewProfile;

public class ProfileController {

    MainFrame frame;

    public ProfileController(MainFrame frame) {
        
        this.frame = frame;
        showProfile();
    }
    
    private void showProfile() {
        ViewProfile viewProfile = new ViewProfile();

        frame.addContentPanel(viewProfile, "ViewProfile");
        frame.showPanel("ViewProfile");
    }

    public void onBackClicked(){
        
    }

}
