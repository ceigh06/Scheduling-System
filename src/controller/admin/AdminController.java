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
    // Store report panels to avoid recreating them
    private Report1 report1;
    private Report2 report2;
    private Report3 report3;

    public AdminController(User user) {
        this.user = user;

        AdminLanding adminLanding = new AdminLanding();
        AdminMainframe.addContentPanel(adminLanding, "AdminLanding");
        AdminMainframe.showPanel("AdminLanding");
        AdminMainframe.setNavBarVisible(true);

        adminLanding.setOnTotalBtn(e -> {

            onTotalReportClicked();
        });

        adminLanding.setOnMostBtn(e -> {

            onMostReportClicked();
        });

        adminLanding.setOnPeakBtn(e -> {

            onPeakReportClicked();
        });

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
        if (report1 == null) {
            report1 = new Report1();
            AdminMainframe.addContentPanel(report1, "Report1");
        }
        AdminMainframe.showPanel("Report1", "Total Room Requests");
    }

    public void onMostReportClicked() {
        if (report2 == null) {
            report2 = new Report2();
            AdminMainframe.addContentPanel(report2, "Report2");
        }
        AdminMainframe.showPanel("Report2", "Most Requested Room");
    }

    public void onPeakReportClicked() {
        if (report3 == null) {
            report3 = new Report3();
            AdminMainframe.addContentPanel(report3, "Report3");
        }
        AdminMainframe.showPanel("Report3", "Peak Scheduling Hours");
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
