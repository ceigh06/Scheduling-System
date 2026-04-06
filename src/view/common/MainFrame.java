package view.common;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.user.Faculty;
import model.user.Student;
import model.user.User;

public class MainFrame {

    private static JFrame frame;
    private static JPanel contentPanel;
    private static CardLayout cardLayout;
    private static TitleHeader titleHeader;
    private static NavigationBar navBar;
    private static JPanel navPanel;
    private static User currentUser;

    public static void init() {
        frame = new JFrame("Scheduling System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(450, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // top panel, fixed lang din sya
        // kumabaga ito yung default header
        JPanel topPanel = TitleHeader.createHeader("RoomMate");//FRONTEND TASK: instead na name ng app, pwede ba ung Logo Icon

        // swapping of contents here, JPanel only
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // bottom panel, always has navigation bar unless wants to hide
        navBar = new NavigationBar(frame);
        navPanel = navBar.getNavBar();
        // Assemble frame

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.BLUE);
        centerWrapper.setPreferredSize(new Dimension(450, 520));
        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        frame.add(centerWrapper, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerWrapper, BorderLayout.CENTER);
        frame.add(navPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void restoreNavBarDefaultState() {
        navBar.resetToDefault();
    }

    // Add a content panel
    // default header is always "RoomFindr"
    public static void addContentPanel(JPanel panel, String name) {
        contentPanel.add(panel, name);
    }

    // Switch content and update header
    public static void showPanel(String name, String newTitle) {
        cardLayout.show(contentPanel, name);
        TitleHeader.headerTitle.setText(newTitle);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Switch without changing title
    public static void showPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    public static void setNavBarVisible(boolean visible) {
        navPanel.setVisible(visible);
        frame.revalidate();
        frame.repaint();
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void setNotification(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    // HEADER BUTTON REGISTRATION METHODS
    //(Student-side) Request History
    public static void setOnRequestHistoryPanel(Student user, MouseAdapter action) {
        titleHeader = new TitleHeader();
        titleHeader.setOnRequestHistoryPanel(user, action);
    }
    
    //(Faculty-side) View my Schedule
    public static void setOnViewSchedulePanel(Faculty user, MouseAdapter action) {
        titleHeader = new TitleHeader();
        titleHeader.setOnViewSchedulePanel(user, action);
    }

    public static void setIconType(Student user) {
        titleHeader = new TitleHeader();
        titleHeader.setIconType(user);
    }

    public static void setIconType(Faculty user) {
        titleHeader = new TitleHeader();
        titleHeader.setIconType(user);
    }
    // NAV BAR REGISTRATION METHODS
    public static void setOnBrowsePanel(MouseAdapter action) {
        navBar.setOnBrowsePanel(action);
    }

    public static void setOnHomePanel(MouseAdapter action) {
        navBar.setOnHomePanel(action);
    }

    public static void setOnRequestPanel(MouseAdapter action) {
        navBar.setOnRequestPanel(action);
    }

     public static void setOnArchivePanel(MouseAdapter action) {
        navBar.setOnArchivePanel(action);
    }

    public static void setOnProfilePanel(MouseAdapter action) {
        navBar.setOnProfilePanel(action);
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // This method also refreshes the nav bar to reflect the new user type
    public static void setCurrentUser(User user, boolean refreshNavBar) {
        currentUser = user;
        if (refreshNavBar && navBar != null) {
            navBar.rebuildForUser(user);
        } else if (refreshNavBar) {
            navBar = new NavigationBar(frame, user);
            navPanel.add(navBar.getNavBar());
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
