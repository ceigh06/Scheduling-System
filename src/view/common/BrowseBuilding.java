package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import model.Building;
import view.components.RoundedButton;
import view.components.ScrollBarHelper;

@SuppressWarnings("serial")
public class BrowseBuilding extends JPanel {

    private JPanel headerPanel;
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
            RoundedButton btn = createBldgBtn(building.getName(), "/resources/images/building/" + building.getName() + ".png"); //registering the button to the model
            btn.addActionListener(e -> {
                onBuildingClicked.accept(building);
            });
            bldgContent.add(btn);
        }
        wrapper.add(bldgContent);
        wrapper.revalidate();
        wrapper.repaint();

        JScrollPane scrollPane = (JScrollPane) getComponent(0);
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public BrowseBuilding() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(450, 520));
        setMaximumSize(new Dimension(450, 520));
        setMinimumSize(new Dimension(450, 520));

        wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        wrapper.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel bldgTitle = new JLabel("Select a building to explore");
        bldgTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bldgTitle.setForeground(new Color(91, 112, 121));
        headerPanel.add(bldgTitle, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Choose from available campus buildings");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitle.setForeground(new Color(117, 144, 156));
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        wrapper.add(headerPanel);

        JScrollPane scrollPanel = new JScrollPane(wrapper);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        scrollPanel.setBorder(null);
        scrollPanel.getViewport().setBackground(Color.WHITE);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        scrollPanel.setPreferredSize(new Dimension(410, 400));

        scrollPanel.setMaximumSize(new Dimension(410, 400));
        scrollPanel.setMinimumSize(new Dimension(410, 400));
        
        scrollPanel.getViewport().setPreferredSize(new Dimension(410, 400));
        scrollPanel.getViewport().setMaximumSize(new Dimension(410, 400));

        scrollPanel.setPreferredSize(new Dimension(410, 500));
        scrollPanel.setMaximumSize(new Dimension(410, Integer.MAX_VALUE));

        add(scrollPanel, BorderLayout.CENTER);
    }

    public BrowseBuilding(boolean viewArchives) {
        if (viewArchives) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            
            wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setBackground(Color.WHITE);
            wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

            headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(Color.WHITE);
            headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

            JLabel bldgTitle = new JLabel("Browse Archives");
            bldgTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
            bldgTitle.setForeground(new Color(91, 112, 121));
            headerPanel.add(bldgTitle, BorderLayout.NORTH);

            JLabel subtitle = new JLabel("Choose from available buildings with archived schedules");
            subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            subtitle.setForeground(new Color(117, 144, 156));
            headerPanel.add(subtitle, BorderLayout.SOUTH);

            wrapper.add(headerPanel);

            JScrollPane scrollPanel = new JScrollPane(wrapper);
            scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
            scrollPanel.setBorder(null);
            scrollPanel.getViewport().setBackground(Color.WHITE);
            scrollPanel.getVerticalScrollBar().setUnitIncrement(16);

            add(scrollPanel, BorderLayout.CENTER);
        }
    }

    private RoundedButton createBldgBtn(String bldgName, String imgPath) {
        RoundedButton btn = new RoundedButton(bldgName, 25, Color.DARK_GRAY, 2);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(120, 120));
        btn.setMaximumSize(new Dimension(120, 120));

        ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
        Image img = icon.getImage();
        btn.setBackgroundImage(img);
        return btn;
    }

}
