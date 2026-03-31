package controller.admin;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import model.user.User;
import view.admin.AdminMainframe;
import view.admin.Report1;
import view.admin.Report2;
import view.admin.Report3;
import view.landing.AdminLanding;

public class AdminController {

    User user;

    public AdminController(User user) {

        this.user = user;

        AdminLanding adminLanding = new AdminLanding();
        AdminMainframe.addContentPanel(adminLanding, "AdminLanding");
        AdminMainframe.showPanel("AdminLanding");
        AdminMainframe.setNavBarVisible(true);

        adminLanding.setOnTotalBtn(e -> onTotalReportClicked());
        adminLanding.setOnMostBtn(e -> onMostReportClicked());
        adminLanding.setOnPeakBtn(e -> onPeakReportClicked());

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

    public void onTotalReportClicked() {
        new Report1();
    }

    public void onMostReportClicked() {
        new Report2();

    }

    public void onPeakReportClicked() {
        new Report3();
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
