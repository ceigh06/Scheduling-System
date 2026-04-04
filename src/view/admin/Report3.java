package view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import view.components.RoundedLabel;
import view.components.RoundedPanel;

public class Report3 extends JPanel{

    public Report3() {
        setLayout(new BorderLayout()); 
        setBackground(Color.WHITE); 

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,10)); 
        
        JLabel titleLabel = new JLabel("Peak Scheduling Hours"); 
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        
        //BACKEND TO DO: Get actual date from database
        JLabel weekSpan = new JLabel("March 16 - March 21"); 
        weekSpan.setAlignmentX(CENTER_ALIGNMENT);
        weekSpan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
        JLabel visualTitle = new JLabel("Heatmap of Peak Scheduling Hours");
        visualTitle.setAlignmentX(CENTER_ALIGNMENT);
        visualTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(weekSpan);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(visualTitle);
        
        add(topPanel, BorderLayout.NORTH); 

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // add here the heatmap components, this is for testing purposes only 
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(createHeatMap());
        contentPanel.add(Box.createVerticalStrut(10));

        JLabel lineChartLbl = new JLabel("Time-of-Day Requests");
        lineChartLbl.setAlignmentX(CENTER_ALIGNMENT);
        lineChartLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        String[] date = { "22", "23", "24", "25", "26", "27" };
	    //BACKEND TO DO/NOTE: always limit the date to 6 lang kasi here naka 
	    //depend kung gano kadami mag aappear sa calendar
	    String[] day = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

        JPanel calendar = new JPanel(new GridLayout(1, date.length, 10, 0));
        calendar.setBackground(Color.RED); //test line, remove later, para lang kita mo saan ilalagay

        // ELDREI TO DO: Implement logic to populate the calendar with actual dates and corresponding data from the database, 
        // and make the calendar interactive (clickable dates that show detailed info)
        // this is not visible YET because the logic to populate the calendar with actual data from the database is not yet implemented
        // and the spaces are different based on the size of your charts 

        for (int i = 0; i < date.length; i++) {
	        RoundedPanel clickableDate = new RoundedPanel(55, 5, Color.BLACK);
	        clickableDate.setLayout(new BoxLayout(clickableDate, BoxLayout.Y_AXIS));
	        clickableDate.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	        clickableDate.setPreferredSize(new Dimension(350, 75));

	        JLabel dateLabel = new JLabel(date[i], SwingConstants.CENTER);
	        RoundedLabel roundedDate = new RoundedLabel(dateLabel, 0, Color.WHITE, 35);
	        roundedDate.setBackground(Color.WHITE);
	        roundedDate.setPreferredSize(new Dimension(33, 35));
	        roundedDate.setMaximumSize(new Dimension(33, 35));
	        roundedDate.setAlignmentX(Component.CENTER_ALIGNMENT);

	        JLabel dayLabel = new JLabel(day[i], SwingConstants.CENTER);
	        dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
	        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	        clickableDate.add(Box.createVerticalStrut(3));
	        clickableDate.add(roundedDate);
	        clickableDate.add(Box.createVerticalStrut(10));
	        clickableDate.add(dayLabel);

	        clickableDate.setBackground(new Color(221, 221, 219));
	        clickableDate.setOpaque(false);    

        }

        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lineChartLbl);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(calendar);  //why is this not fucking working 
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(createLineGraph());
        contentPanel.add(Box.createVerticalStrut(5));
        

        add(contentPanel, BorderLayout.CENTER);

    }

    public JPanel createHeatMap(){
        //BACKEND TO DO: Implement heatmap generation logic here, using data from the database
        JPanel heatMapPanel = new JPanel(); 
        heatMapPanel.setBackground(Color.WHITE);
        heatMapPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        heatMapPanel.setPreferredSize(new Dimension(350, 200));

        // ELDREI TO DO: Replace this placeholder with the actual heatmap visualization once implemented 
        // try RoundedPanel as the panel for the visualization to make it look nicer
        // refer to the implementation of Report1 for how to create a heatmap visualization using JLabels and color coding

        JLabel placeholder = new JLabel("Heatmap goes here");
        placeholder.setFont(new Font("Georgia", Font.ITALIC, 14));
        heatMapPanel.add(placeholder);

        return heatMapPanel; 
    }

    public JPanel createLineGraph(){
        //BACKEND TO DO: Implement line graph generation logic here, using data from the database
        JPanel lineGraphPanel = new JPanel(); 
        lineGraphPanel.setBackground(Color.WHITE);
        lineGraphPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lineGraphPanel.setPreferredSize(new Dimension(350, 200)); //test line 

        // ELDREI TO DO: Replace this placeholder with the actual line graph visualization once implemented 
        // try RoundedPanel as the panel for the visualization to make it look nicer
        // refer to the implementation of Report1 for how to create a line graph visualization using JLabels and color coding

        JLabel placeholder = new JLabel("Line Graph goes here");
        placeholder.setFont(new Font("Georgia", Font.ITALIC, 14));
        lineGraphPanel.add(placeholder);

        return lineGraphPanel; 
    }


}
