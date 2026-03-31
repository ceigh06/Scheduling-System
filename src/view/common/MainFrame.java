package view.common;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame {

    private static JFrame frame;
    private static JPanel contentPanel;
    private static JPanel requestPanel;
    private static CardLayout cardLayout;
    private static JLabel headerTitle;
    private static NavigationBar navBar;
    private static JPanel navPanel;

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
        JPanel topPanel = createHeader("RoomMate");

        // swapping of contents here, JPanel only
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // bottom panel, always has navigation bar unless wants to hide
        navBar = new NavigationBar(frame);
        navPanel = navBar.getNavBar();

        // Assemble frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(navPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void restoreNavBarDefaultState() {
        navBar.resetToDefault();
    }

    private static JPanel createHeader(String title) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(500, 50));
        topPanel.setBackground(new Color(139, 0, 0));

        // this is to balance the space added from the button on the east side
        JPanel leftSpacer = new JPanel();
        leftSpacer.setOpaque(false);
        leftSpacer.setPreferredSize(new Dimension(50, 50));

        // ito yung section
        headerTitle = new JLabel(title, JLabel.CENTER);
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Georgia", Font.BOLD, 26));
        headerTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        requestPanel = new JPanel();
        requestPanel.setOpaque(false);
        requestPanel.setPreferredSize(new Dimension(50, 50));
        requestPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        ImageIcon img = new ImageIcon(MainFrame.class.getResource("/resources/images/icons/Home.png")); // sample only
        Image scaled = img.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel reqHistoryIcon = new JLabel(new ImageIcon(scaled));
        requestPanel.add(reqHistoryIcon);

        requestPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // insert logic here
                System.out.println("Request History: Im working.");
            }
        });

        topPanel.add(leftSpacer, BorderLayout.WEST);
        topPanel.add(headerTitle, BorderLayout.CENTER);
        topPanel.add(requestPanel, BorderLayout.EAST);
        return topPanel;
    }

    // Add a content panel
    // default header is always "RoomFindr"
    public static void addContentPanel(JPanel panel, String name) {
        contentPanel.add(panel, name);
    }

    // Switch content and update header
    public static void showPanel(String name, String newTitle) {
        cardLayout.show(contentPanel, name);
        headerTitle.setText(newTitle);
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

    public static void setOnProfilePanel(MouseAdapter action) {
        navBar.setOnProfilePanel(action);
    }
}