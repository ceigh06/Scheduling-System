package controller.faculty;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import view.common.BrowseBuilding;
import view.common.MainFrame;
import view.landing.Landing;
import model.user.User;

public class FacultyController {

    User user;

    public FacultyController(User user) {

        this.user = user;

        MainFrame.addContentPanel(new Landing(), "FacultyLanding");
        MainFrame.showPanel("FacultyLanding");
        MainFrame.setNavBarVisible(true);


        MainFrame.setOnBrowsePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    onBrowseClicked();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        MainFrame.setOnHomePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onHomeClicked();
            }
        });
        
        MainFrame.setOnRequestPanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRequestClicked();
            }
        });
        
        MainFrame.setOnProfilePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onProfileClicked();
            }
        });
    }

    public void onBrowseClicked() throws SQLException {
        new RoomsController(user);
    }

    public void onHomeClicked() {
        MainFrame.showPanel("FacultyLanding");
    }   

    public void onRequestClicked() {
       new FacultyController(user);
    }

    public void onProfileClicked() {
        new ProfileController(user);
    }
}
