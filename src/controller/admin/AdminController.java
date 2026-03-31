package controller.admin;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import model.user.User;
import view.admin.AdminMainframe;
import view.landing.AdminLanding;

public class AdminController {

    User user;

    public AdminController(User user) {

        this.user = user;

        AdminMainframe.addContentPanel(new AdminLanding(), "AdminLanding");
        AdminMainframe.showPanel("AdminLanding");
        AdminMainframe.setNavBarVisible(true);

        AdminMainframe.setOnBrowsePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { 
                try {
                    onBrowseClicked();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        AdminMainframe.setOnHomePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onHomeClicked();
            }
        });

        AdminMainframe.setOnProfilePanel(new MouseAdapter() {
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
        AdminMainframe.showPanel("AdminLanding", "Home");
    }

    public void onProfileClicked() {
        new ProfileController(user);
    }
}