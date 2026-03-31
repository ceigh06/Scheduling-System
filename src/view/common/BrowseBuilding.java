package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import model.Building;
import view.components.RoundedPanel;

@SuppressWarnings("serial")
public class BrowseBuilding extends JPanel {

    private JPanel headerPanel;
    private JPanel wrapper;
    private JPanel bldgContent;

    Consumer<Building> onBuildingClicked;

    public void setOnBuildingClicked(Consumer<Building> action) {
        this.onBuildingClicked = action;// register
    }

    public void loadBuilding(List<Building> buildings) {
        bldgContent = new JPanel(new GridLayout(0, 2, 16, 16));
        bldgContent.setBackground(Color.WHITE);

        wrapper.removeAll();
        wrapper.add(headerPanel);

        int rows = (int) Math.ceil(buildings.size() / 2.0);
        bldgContent = new JPanel(new GridLayout(0, 2, 16, 16));
        bldgContent.setBackground(Color.WHITE);
        bldgContent.setAlignmentX(CENTER_ALIGNMENT); // test line
        bldgContent.setPreferredSize(new Dimension(0, rows * 140)); // 140 = height of each card

        for (Building building : buildings) {
            String imagePath = "/resources/images/building/" + building.getName().trim().replace(" ", "_") + ".png";
            // path builder for getting building resources.
            System.out.println("Loading image for building: " + building.getName() + " from path: " + imagePath);
            RoundedPanel buildingPanel = createBldgCard(building.getName(), imagePath); // registering the button to the
                                                                                        // model
            buildingPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (onBuildingClicked != null) {
                        onBuildingClicked.accept(building); // trigger
                    }
                }
            });
            bldgContent.add(buildingPanel);
        }
        wrapper.add(bldgContent);
        wrapper.revalidate();
        wrapper.repaint();
    }

    public BrowseBuilding() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        MainFrame.setNavBarVisible(true);

        wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel bldgTitle = new JLabel("Select a building to explore");
        bldgTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bldgTitle.setForeground(Color.BLACK);
        headerPanel.add(bldgTitle, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Choose from available campus buildings");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitle.setForeground(Color.GRAY);
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        wrapper.add(headerPanel);

        JScrollPane scrollPanel = new JScrollPane(wrapper);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder());
        scrollPanel.getViewport().setBackground(Color.WHITE);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPanel, BorderLayout.CENTER);
    }

    private RoundedPanel createBldgCard(String bldgName, String imgPath) {
        RoundedPanel card = new RoundedPanel(20, 2, new Color(139, 0, 0));
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(140, 140));
        card.setMaximumSize(new Dimension(140, 140));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
            Image img = icon.getImage();
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            centerWrapper.add(imgLabel);
        } catch (Exception e) {
            System.out.println("Image not found for: " + bldgName);
        }
        // Added text area so the building name can be displayed completely even when
        // long
        JTextArea nameArea = new JTextArea(bldgName);
        nameArea.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameArea.setForeground(new Color(139, 0, 0));
        nameArea.setEditable(false);
        nameArea.setOpaque(false);
        nameArea.setFocusable(false);     
        nameArea.setHighlighter(null);
        nameArea.setLineWrap(true);
        nameArea.setWrapStyleWord(true);
        nameArea.setColumns(10); // if 10 characters fit in one line, adjust as needed
        nameArea.setRows(3); // maximum of 3 lines, adjust as needed
        nameArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        nameArea.setAlignmentY(JTextArea.CENTER_ALIGNMENT);

        centerWrapper.add(nameArea, BorderLayout.CENTER);
        card.add(centerWrapper, BorderLayout.CENTER);
        return card;
    }
}