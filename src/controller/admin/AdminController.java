package controller.admin;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import model.user.User;
import view.admin.Report1;
import view.admin.Report2;
import view.admin.Report3;
import view.common.MainFrame;
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
        MainFrame.setCurrentUser(user, true);
        MainFrame.addContentPanel(adminLanding, "AdminLanding");
        MainFrame.showPanel("AdminLanding");
        MainFrame.setNavBarVisible(true);

        adminLanding.setOnTotalBtn(e -> {

            onTotalReportClicked();
        });

        adminLanding.setOnMostBtn(e -> {

            onMostReportClicked();
        });

        adminLanding.setOnPeakBtn(e -> {

            onPeakReportClicked();
        });

        MainFrame.setOnBrowsePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    onBrowseClicked();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        MainFrame.setOnHomePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onHomeClicked();
            }
        });

        MainFrame.setOnProfilePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onProfileClicked();
            }
        });
    }

    public void onTotalReportClicked() {
        if (report1 == null) {
           ReportOneController reportOneController = new ReportOneController();
           reportOneController.initView();
           report1.renderInitialView();
           MainFrame.addContentPanel(reportOneController.getView(), "Report1");
        }
        MainFrame.showPanel("Report1", "Total Room Requests");
    }

    public void onMostReportClicked() {
        // if (report2 == null) {
        //    ReportTwoController reportTwoController = new ReportTwoController();
        //    AdminMainframe.addContentPanel(reportTwoController.getView(), "Report2");
        // }
        // AdminMainframe.showPanel("Report2", "Most Requested Room");
    }

    public void onPeakReportClicked() {
        // if (report3 == null) {
        //     ReportThreeController reportThreeController = new ReportThreeController();
        //     AdminMainframe.addContentPanel(reportThreeController.getView(), "Report3");
        // }
        // AdminMainframe.showPanel("Report3", "Peak Scheduling Hours");
    }

    public void onBrowseClicked() throws SQLException {
        new RoomsController(user);
    }

    public void onHomeClicked() {
        MainFrame.showPanel("AdminLanding", "Home");
    }

    public void onProfileClicked() {
        new ProfileController(user);
    }
}
