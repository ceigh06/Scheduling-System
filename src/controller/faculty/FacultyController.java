package controller.faculty;

import java.awt.event.MouseAdapter;
import java.sql.SQLException;

import view.common.BrowseBuilding;
import view.common.MainFrame;

public class FacultyController {
    MainFrame frame;
    public FacultyController(MainFrame frame) {
        this.frame = frame;

        MainFrame.setOnBrowsePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    onBrowseClicked();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        MainFrame.setOnHomePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onHomeClicked();
            }
        });
        
        MainFrame.setOnRequestPanel(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onRequestClicked();
            }
        });
        
        MainFrame.setOnProfilePanel(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onProfileClicked();
            }
        });
    }

    public void onBrowseClicked() throws SQLException {
        new RoomsController(frame);
    }

    public void onHomeClicked() {

    }   

    public void onRequestClicked() {
        
    }

    public void onProfileClicked() {
        new ProfileController(frame);
    }
}
