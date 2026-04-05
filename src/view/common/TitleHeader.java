package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.user.Faculty;
import model.user.Student;

public class TitleHeader {
    public static JPanel headerButtonPanel;
    public static JLabel headerTitle;
    private static String iconPath = "";
    private static JPanel topPanel;

    public static JPanel createHeader(String title) {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(500, 50));
        topPanel.setBackground(new Color(91, 112, 121));

        // this is to balance the space added from the button on the east side
        JPanel leftSpacer = new JPanel();
        leftSpacer.setOpaque(false);
        leftSpacer.setPreferredSize(new Dimension(50, 50));

        // ito yung section
        headerTitle = new JLabel(title, JLabel.CENTER);
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Georgia", Font.BOLD, 26));
        headerTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        headerButtonPanel = new JPanel();
        headerButtonPanel.setOpaque(false);
        headerButtonPanel.setPreferredSize(new Dimension(50, 50));
        headerButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        topPanel.add(leftSpacer, BorderLayout.WEST);
        topPanel.add(headerTitle, BorderLayout.CENTER);
        topPanel.add(headerButtonPanel, BorderLayout.EAST);
        return topPanel;
    }

    private static JLabel createIconLabel() {
        ImageIcon img = new ImageIcon(MainFrame.class.getResource(iconPath)); // sample only
        Image scaled = img.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        JLabel reqHistoryIcon = new JLabel(new ImageIcon(scaled));
        return reqHistoryIcon;
    }

    public static void addIconToHeader() {
        headerButtonPanel.add(createIconLabel());
    }

    public static void removeIconFromHeader(String userType) {
        if(userType.equalsIgnoreCase("student") || userType.equalsIgnoreCase("faculty")) {
            headerButtonPanel.removeMouseListener(headerButtonPanel.getMouseListeners()[0]);
        }
        headerButtonPanel.removeAll();
    }

    public void setIconType(Student user) {
        iconPath = "/resources/images/icons/Request History (STUDENT).png";
    }

    public void setIconType(Faculty user) {
        iconPath = "/resources/images/icons/My Schedule (FACULTY).png";
    }

    //student-side request history
    public void setOnRequestHistoryPanel(Student user, MouseAdapter action) {
        headerButtonPanel.addMouseListener(action);
    }

    //faculty-side view my schedule
    public void setOnViewSchedulePanel(Faculty user, MouseAdapter action) {
        headerButtonPanel.addMouseListener(action);
    }
}