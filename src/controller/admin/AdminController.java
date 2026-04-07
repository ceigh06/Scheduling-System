package controller.admin;

import controller.shared.ProfileController;
import controller.shared.RoomsController;
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
    private Report1 report1;
    private Report2 report2;
    private Report3 report3;

  
    private ReportOneController reportOneController;
    private ReportTwoController reportTwoController;
    private ReportThreeController reportThreeController;
    private AdminLanding adminLanding;

    public AdminController(User user) throws SQLException {
        this.user = user;

        adminLanding = new AdminLanding();
        MainFrame.setCurrentUser(user, true);
        MainFrame.addContentPanel(adminLanding, "AdminLanding");
        MainFrame.showPanel("AdminLanding", "RoomMate");
        MainFrame.setNavBarVisible(true);

        adminLanding.setOnTotalBtn(e -> onTotalReportClicked());
        adminLanding.setOnMostBtn(e -> onMostReportClicked());
        adminLanding.setOnPeakBtn(e -> {
            try {
                onPeakReportClicked();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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

        MainFrame.setOnArchivePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    onArchiveClicked();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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
            reportOneController = new ReportOneController();
            reportOneController.initView();
            report1 = reportOneController.getView();
            MainFrame.addContentPanel(report1, "Report1");
        }
        MainFrame.showPanel("Report1", "Admin Reports");
        MainFrame.restoreNavBarDefaultState();
    }

    public void onMostReportClicked() {
        if (report2 == null) {
            reportTwoController = new ReportTwoController();
            reportTwoController.loadData();
            reportTwoController.initView();
            report2 = reportTwoController.getView();
            MainFrame.addContentPanel(report2, "Report2");
        }
        MainFrame.showPanel("Report2", "Admin Reports");
        MainFrame.restoreNavBarDefaultState();
    }

    public void onPeakReportClicked() throws SQLException {
        
        reportThreeController = new ReportThreeController();
        reportThreeController.initView();
        report3 = reportThreeController.getView();
        MainFrame.addContentPanel(report3, "Report3");
        
        MainFrame.showPanel("Report3", "Admin Reports");
        MainFrame.restoreNavBarDefaultState();
    }

    public void onBrowseClicked() throws SQLException {
        new RoomsController(user);
    }

    public void onHomeClicked() {
        // Refresh all report data and landing card labels
        if (reportOneController   != null) reportOneController.refreshData();
        if (reportTwoController   != null) reportTwoController.refreshData();
        if (reportThreeController != null) reportThreeController.refreshData();
        adminLanding.refresh();
        MainFrame.showPanel("AdminLanding", "Home");
        MainFrame.restoreNavBarDefaultState();
    }

    public void onArchiveClicked() throws SQLException {
        new ArchiveController(user);
    }

    public void onProfileClicked() {
        new ProfileController(user);
    }
}