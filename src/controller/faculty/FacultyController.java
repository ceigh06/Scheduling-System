package controller.faculty;

import controller.shared.ProfileController;
import controller.shared.RoomsController;
import controller.shared.SearchRoomsController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import model.user.Faculty;
import model.user.User;
import view.common.MainFrame;
import view.common.TitleHeader;
import view.landing.Landing;

public class FacultyController {

    User user;

    public FacultyController(User user) {

        this.user = user;
        MainFrame.setCurrentUser(user, true);
        Landing landing = new Landing();
        MainFrame.addContentPanel(landing, "FacultyLanding");
        MainFrame.showPanel("FacultyLanding");
        Faculty faculty = new Faculty();
        MainFrame.setIconType(faculty);
        TitleHeader.addIconToHeader();
        MainFrame.setNavBarVisible(true);

        landing.setOnSearchAction(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    MainFrame.restoreNavBarDefaultState();
                    onSearchClicked();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

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

    public void onSearchClicked() throws SQLException {
        new SearchRoomsController(user);
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
