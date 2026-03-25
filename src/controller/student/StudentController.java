package controller.student;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import controller.student.SearchRoomsController;
import view.common.BrowseBuilding;
import view.common.MainFrame;
import view.common.NavigationBar;
import view.landing.Landing;

import model.user.User;

public class StudentController {

    User user;

    public StudentController(User user) throws SQLException {
        this.user = user;

        Landing landing = new Landing();

        MainFrame.addContentPanel(landing, "StudentLanding");
        MainFrame.showPanel("StudentLanding");
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
        MainFrame.showPanel("StudentLanding");
    }   

    public void onRequestClicked() {
       new RequestsController(user);
    }

    public void onProfileClicked() {
        new ProfileController(user);
    }
}
