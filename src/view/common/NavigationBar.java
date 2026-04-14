package view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.user.User;
import view.components.RoundedPanel;

public class NavigationBar {

    private JPanel navPanel;
    private RoundedPanel selectedPanel, browsePanel, homePanel, reqPanel, archPanel, pfPanel;

    public RoundedPanel getSelectedPanel() { // still not sure what is the purpose of this
        return selectedPanel;
    }

    public void setOnBrowsePanel(MouseAdapter action) {
        browsePanel.addMouseListener(action);
    }

    public void setOnHomePanel(MouseAdapter action) {
        homePanel.addMouseListener(action);
    }

    public void setOnRequestPanel(MouseAdapter action) {
        reqPanel.addMouseListener(action);
    }

    public void setOnArchivePanel(MouseAdapter action) {
        archPanel.addMouseListener(action);
    }

    public void setOnProfilePanel(MouseAdapter action) {
        pfPanel.addMouseListener(action);
    }

    public NavigationBar(JFrame frame) {
        navPanel = new JPanel(new GridBagLayout());
        navPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
        // navPanel.setBorder(BorderFactory.createEmptyBorder(-10, 1, -10, 1));
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 1, 5, 1));
        navPanel.setBackground(Color.WHITE);

        homePanel = createOption("/resources/images/icons/Home.png", "Home");
        browsePanel = createOption("/resources/images/icons/Rooms.png", "Browse");
        reqPanel = createOption("/resources/images/icons/Notification.png", "Requests");
        pfPanel = createOption("/resources/images/icons/Profile.png", "Profile");

        addPanel(homePanel, 0);
        addPanel(browsePanel, 1);
        addPanel(reqPanel, 2);
        addPanel(pfPanel, 3);
    }

    // for admin
    public NavigationBar(JFrame frame, User user) {
        navPanel = new JPanel(new GridBagLayout());
        navPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 1, 5, 1));

        navPanel.setBackground(Color.WHITE);

        homePanel = createOption("/resources/images/icons/Home.png", "Home");
        browsePanel = createOption("/resources/images/icons/Rooms.png", "Browse");
        reqPanel = createOption("/resources/images/icons/Notification.png", "Requests");
        archPanel = createOption("/resources/images/icons/Archive.png", "Archive");
        pfPanel = createOption("/resources/images/icons/Profile.png", "Profile");

        addPanel(homePanel, 0);
        addPanel(browsePanel, 1);
        addPanel(archPanel, 2); // Admin doesn't have requests, so we add archive instead
        addPanel(pfPanel, 3);
    }

    public void rebuildForUser(User user) {
        // Clear everything
        navPanel.removeAll();
        selectedPanel = null;

        // Recreate panels fresh
        homePanel = createOption("/resources/images/icons/Home.png", "Home");
        browsePanel = createOption("/resources/images/icons/Rooms.png", "Browse");
        reqPanel = createOption("/resources/images/icons/Notification.png", "Requests");
        archPanel = createOption("/resources/images/icons/Archive.png", "Archive");
        pfPanel = createOption("/resources/images/icons/Profile.png", "Profile");

        // Force revalidate of the navPanel to clear old constraints
        navPanel.setLayout(new GridBagLayout());

        if (user.getUserType().equals("Admin")) {
            addPanelWithWeight(homePanel, 0, 1.0);
            addPanelWithWeight(browsePanel, 1, 1.0);
            addPanelWithWeight(archPanel, 2, 1.0);
            addPanelWithWeight(pfPanel, 3, 1.0);
        } else {
            reqPanel = createOption("/resources/images/icons/Notification.png", "Requests");
            addPanelWithWeight(homePanel, 0, 1.0);
            addPanelWithWeight(browsePanel, 1, 1.0);
            addPanelWithWeight(reqPanel, 2, 1.0);
            addPanelWithWeight(pfPanel, 3, 1.0);
        }

        navPanel.revalidate();
        navPanel.repaint();
    }

    private void addPanel(RoundedPanel panel, int x) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.ipady = 20;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 2, 0, 2);
        navPanel.add(panel, gbc);
    }

    private void addPanelWithWeight(RoundedPanel panel, int x, double weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.ipady = 20;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 2, 0, 2);
        navPanel.add(panel, gbc);
    }

    private RoundedPanel createOption(String imgPath, String text) {
        RoundedPanel panel = new RoundedPanel(20, 0);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(91, 112, 121));
        panel.setPreferredSize(new Dimension(80, 25));
        panel.setMaximumSize(new Dimension(80, 25));

        Image imgIcon = new ImageIcon(getClass().getResource(imgPath)).getImage().getScaledInstance(20, 20,
                Image.SCALE_SMOOTH);
        Image scaledImg = imgIcon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImg));
        panel.add(label);

        JLabel textLbl = new JLabel(text);
        textLbl.setForeground(Color.WHITE);
        textLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        textLbl.setVisible(false);
        textLbl.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        panel.add(textLbl);

        // BACKEND TO DO: Make it so that when I click the button item, it will go to
        // the frames
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GridBagLayout layout = (GridBagLayout) navPanel.getLayout();
                if (selectedPanel != null) {
                    selectedPanel.getComponent(1).setVisible(false); // hides the text of the previously selected panel
                    selectedPanel.setBackground(new Color(91, 112, 121)); // goes back to the original color of the
                    // previously selected panel

                    GridBagConstraints old = layout.getConstraints(selectedPanel);
                    old.weightx = 1;
                    layout.setConstraints(selectedPanel, old); // Goes back to the old positions
                }

                textLbl.setVisible(true); // shows the text of the currently selected panel
                selectedPanel = panel;

                GridBagConstraints gbc = layout.getConstraints(panel);
                gbc.weightx = 3;
                layout.setConstraints(panel, gbc);

                // so it will go back to the previous looks
                navPanel.revalidate();
                navPanel.repaint();
            }
        });

        return panel;
    }

    public void resetToDefault() {
        if (selectedPanel == null)
            return;

        GridBagLayout layout = (GridBagLayout) navPanel.getLayout();

        // component(1) is the text label — same as mouseClicked uses
        selectedPanel.getComponent(1).setVisible(false);
        selectedPanel.setBackground(new Color(91, 112, 121));

        GridBagConstraints gbc = layout.getConstraints(selectedPanel);
        gbc.weightx = 1;
        layout.setConstraints(selectedPanel, gbc);

        selectedPanel = null;

        navPanel.revalidate();
        navPanel.repaint();
    }

    public JPanel getNavBar() {
        return navPanel;
    }

}
