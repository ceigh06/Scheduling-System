package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.Building;
import view.components.RoundedButton;

@SuppressWarnings("serial")
public class BrowseBuilding extends JPanel  {

    private JPanel wrapper;
    private JPanel bldgContent;

    private List<RoundedButton> buildingButtons = new ArrayList<>();

    Consumer<Building> onBuildingClicked;

    public void setOnBuildingClicked(Consumer<Building> action) {
        this.onBuildingClicked = action;// register
    }
    
    public void loadBuilding(List<Building> buildings) {
        bldgContent = new JPanel(new GridLayout(0, 2, 16, 16));
        bldgContent.setBackground(Color.WHITE);

        for (Building building : buildings) {
            RoundedButton btn = createBldgBtn(building.getName(), ""); //registering the button to the model
            btn.addActionListener(e ->{
                onBuildingClicked.accept(building);
            });
            bldgContent.add(btn);
        }
        wrapper.add(bldgContent);
    }

    public BrowseBuilding() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        MainFrame.setNavBarVisible(true);

        wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
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

    private RoundedButton createBldgBtn(String bldgName, String imgPath) {
        RoundedButton btn = new RoundedButton(bldgName, 25, new Color(139,0,0),2);
        btn.setForeground(new Color(139,0,0));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btn.setPreferredSize(new Dimension(120,120));
        btn.setMaximumSize(new Dimension(120,120));

        // ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
        // Image img = icon.getImage();
        // btn.setBackgroundImage(img);

        return btn;
    }

    // @Override
    // public void actionPerformed(ActionEvent e) {
    //     RoundedButton source = (RoundedButton) e.getSource();
    //     String buildingName = source.getText();

    //     MainFrame.setNavBarVisible(true);
    //     RoomBrowser roomPanel = new RoomBrowser(buildingName);
    //     MainFrame.addContentPanel(roomPanel, "roomBrowser");
    //     MainFrame.showPanel("roomBrowser", buildingName);
    // }
}