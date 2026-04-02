package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import view.components.RoundedLabel;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;

public class RequestHistory extends JPanel {
	JPanel calendar, mainWrapper, requestPanel, wrapper;
	JLabel dateLabel, dayLabel, status;
	RoundedLabel roundedDate, pfp;
	RoundedPanel clickableDate, statusPanel;
	JScrollPane mainScrollPane;
	Color defaultColor = new Color(117, 144, 156); // grayish-blue
    Color selectedColor = new Color(255, 227, 85);
     JLabel selectedDayLabel = null;
     RoundedPanel selectedDatePanel = null;


	public RequestHistory() {
	    setLayout(new BorderLayout());
	    //pa modify na lang ng padding if gagawing rounded yung scrollpane
	    setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

	    String[] date = { "22", "23", "24", "25", "26", "27" };
	    
	    //depend kung gano kadami mag aappear sa calendar
	    String[] day = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

	    // main date panel
	    calendar = new JPanel(new GridLayout(1, day.length, 10, 0));

	    for (int i = 0; i < date.length; i++) {
	        RoundedPanel clickableDate = new RoundedPanel(55, 2, new Color(91, 112 ,121)); // declare inside loop
	        clickableDate.setLayout(new BoxLayout(clickableDate, BoxLayout.Y_AXIS));
	        clickableDate.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	        clickableDate.setPreferredSize(new Dimension(0, 75));

	        JLabel dateLabel = new JLabel(date[i], SwingConstants.CENTER);
	        RoundedLabel roundedDate = new RoundedLabel(dateLabel, 0, Color.WHITE, 35);
	        roundedDate.setBackground(Color.WHITE);
	        roundedDate.setPreferredSize(new Dimension(33, 35));
	        roundedDate.setMaximumSize(new Dimension(33, 35));
	        roundedDate.setAlignmentX(Component.CENTER_ALIGNMENT);

	        JLabel dayLabel = new JLabel(day[i], SwingConstants.CENTER);
	        dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
	        dayLabel.setForeground(Color.WHITE);
	        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	        clickableDate.add(Box.createVerticalStrut(3));
	        clickableDate.add(roundedDate);
	        clickableDate.add(Box.createVerticalStrut(10));
	        clickableDate.add(dayLabel);
            clickableDate.setCursor(new Cursor(Cursor.HAND_CURSOR));

	        clickableDate.setBackground(defaultColor);
	        clickableDate.setOpaque(false);    
	        
	        //if di pa nagseselect si user eto yung initally selected
	        if (date[i].equals("24") && selectedDatePanel == null) {
	            clickableDate.setBackground(selectedColor);
	            dayLabel.setForeground(Color.BLACK);
	            selectedDatePanel = clickableDate;
	            selectedDayLabel = dayLabel;
	        }

	        final RoundedPanel thisDatePanel = clickableDate;
	        final JLabel thisDayLabel = dayLabel;

	        clickableDate.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                // Reset previously selected panel and label
	                if (selectedDatePanel != null && selectedDatePanel != thisDatePanel) {
	                    selectedDatePanel.setBackground(defaultColor);
	                    selectedDayLabel.setForeground(Color.WHITE);
	                    if (selectedDayLabel != null) {
	                        selectedDayLabel.setForeground(Color.WHITE);
	                    }
	                }

	                // Highlight the newly clicked panel and label
	                thisDatePanel.setBackground(selectedColor);
	                thisDayLabel.setForeground(Color.BLACK);

	                // Update references
	                selectedDatePanel = thisDatePanel;
	                selectedDayLabel = thisDayLabel;
	            }
	        });

	        calendar.add(clickableDate);
			
	    }
	    
	    mainWrapper = new JPanel();
	    mainWrapper.setLayout(new BoxLayout(mainWrapper, BoxLayout.Y_AXIS));
	    mainWrapper.setOpaque(false);
	    
	    mainWrapper.add(calendar, BorderLayout.NORTH);
		mainWrapper.add(Box.createVerticalStrut(5));
	    
	    mainScrollPane = new JScrollPane(mainWrapper);
	    mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
	    ScrollBarHelper.applySlimScrollBar(mainScrollPane,10, 30, Color.GRAY, Color.LIGHT_GRAY);
		mainScrollPane.setBorder(null);
		
		add(mainScrollPane, BorderLayout.CENTER);

		//simple checker lang
		//BACKEND TO DO: pa lagay na lang to into method
		//NO REQUESTS PANEL
		JPanel noRequests = new JPanel(new BorderLayout());
		JLabel noReqLabel = new JLabel("No Request Schedule for this day!", SwingConstants.CENTER);
		noReqLabel.setFont(new Font("Arial", Font.BOLD, 16));
		noReqLabel.setForeground(new Color(117, 144, 156));
		noRequests.add(noReqLabel, BorderLayout.CENTER);
		
		
		int requestsCount = 0; // example count, replace with actual logic to check requests for the selected date
		if(requestsCount > 0){
			newRequest(true);
			newRequest(false);
			newRequest(true);
		}else {
			mainWrapper.add(noRequests);
		}
				
		
	    
	    
	}
	
	private void newRequest(boolean reqStatus) { 

		String[] info = {"Jessie Santos", "BSIT 2A G2", "IT 208", "10:00 AM", "Prog Lab 2", "10:00 AM - 1:00 PM"};
		
		//panel for student infos
		requestPanel = new JPanel(new GridBagLayout());
		requestPanel.setFont(new Font("Arial", Font.BOLD, 20));
		requestPanel.setBackground(new Color(221, 221, 219));
		// Profile picture
		GridBagConstraints gbcPfp = new GridBagConstraints();
		gbcPfp.gridx = 0;
		gbcPfp.gridy = 0;
		gbcPfp.gridheight = 4;
		gbcPfp.weightx = 0.1;
		gbcPfp.anchor = GridBagConstraints.CENTER;
		//icon for pfp
		ImageIcon rawIcon = new ImageIcon("pfp.png");
		Image scaledImg = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		pfp = new RoundedLabel(new ImageIcon(scaledImg), 2, new Color(91, 112 ,121), 100);
		requestPanel.add(pfp, gbcPfp);

		// Info labels (name, section, course)
		for (int i = 0; i < 4; i++) {
		    GridBagConstraints gbcInfo = new GridBagConstraints();
		    gbcInfo.gridx = 1;
		    gbcInfo.gridy = i;
		    gbcInfo.weightx = 0.9;
		    gbcInfo.anchor = GridBagConstraints.WEST;
			gbcInfo.insets = new Insets(5, 5, 5, 5);
			// simple if else lang for creating ng rows para mag show yung "requested at"
		    if(i == 3) {
		    	requestPanel.add(new JLabel("Requested at: " + info[3]){{
		    	    setFont(new Font("Courier New", Font.BOLD, 14));
		    	}}, gbcInfo);
		    }else {
			    requestPanel.add(new JLabel(info[i]){{
			        setFont(new Font("Courier New", Font.BOLD, 14));
			    }}, gbcInfo);
		    }
		    
		}
		// Classroom schedule label
		GridBagConstraints gbcSchedLabel = new GridBagConstraints();
		gbcSchedLabel.gridx = 0;
		gbcSchedLabel.gridy = 4;
		gbcSchedLabel.anchor = GridBagConstraints.WEST;
		gbcSchedLabel.insets = new Insets(15, 5, 0, 5);
		requestPanel.add(new JLabel("CLASSROOM SCHEDULE"){{
		    setFont(new Font("Courier New", Font.BOLD, 12));
		}}, gbcSchedLabel);

		// Classroom schedule data
		GridBagConstraints gbcSchedData = new GridBagConstraints();
		gbcSchedData.gridx = 0;
		gbcSchedData.gridy = 5;
		gbcSchedData.anchor = GridBagConstraints.WEST;
		gbcSchedData.insets = new Insets(5, 5, 15, 5);
		requestPanel.add(new JLabel(info[4]){{
		    setFont(new Font("Courier New", Font.BOLD, 12));
		}}, gbcSchedData);

		// Time label
		GridBagConstraints gbcTimeLabel = new GridBagConstraints();
		gbcTimeLabel.gridx = 1;
		gbcTimeLabel.gridy = 4;
		gbcTimeLabel.anchor = GridBagConstraints.WEST;
		gbcTimeLabel.insets = new Insets(15, 15, 0, 5);
		requestPanel.add(new JLabel("TIME"){{
		    setFont(new Font("Courier New", Font.BOLD, 12));
		}}, gbcTimeLabel);

		// Time data
		GridBagConstraints gbcTimeData = new GridBagConstraints();
		gbcTimeData.gridx = 1;
		gbcTimeData.gridy = 5;
		gbcTimeData.anchor = GridBagConstraints.WEST;
		gbcTimeData.insets = new Insets(5, 15, 15, 5);
		requestPanel.add(new JLabel(info[5]){{
		    setFont(new Font("Courier New", Font.BOLD, 12));
		}}, gbcTimeData);

		RoundedPanel mainPanel = new RoundedPanel(60, 3, new Color(91, 112 ,121), new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.setBackground(new Color(243, 244, 247));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

		requestPanel.setOpaque(false);
		
		statusPanel = new RoundedPanel(40, 2, new Color(117, 144, 156));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		statusPanel.setPreferredSize(new Dimension(150, 40));
        statusPanel.setMaximumSize(new Dimension(150, 40));
		statusPanel.add(Box.createVerticalStrut(10));
		
		status = new JLabel();
		status.setAlignmentX(CENTER_ALIGNMENT);
		status.setFont(new Font("Courier New", Font.BOLD, 20));
		
		if(reqStatus == true) {
			status.setText("APPROVED");
			statusPanel.setBackground(new Color(63, 193, 127));
			statusPanel.setBorderColor(new Color(77,139, 78));
			statusPanel.add(status);
		}else {
			status.setText("DENIED");
			statusPanel.setBackground(new Color(255, 100, 100));
			statusPanel.setBorderColor(new Color(227, 75, 75));
			statusPanel.add(status);
		}

		mainPanel.add(statusPanel, BorderLayout.NORTH);
		mainPanel.add(requestPanel, BorderLayout.CENTER);

		// Wrap in transparent container
		wrapper = new JPanel(new FlowLayout());
		wrapper.setOpaque(false);
		wrapper.add(mainPanel);
		
		mainWrapper.add(wrapper);
		mainWrapper.revalidate();
		mainWrapper.repaint();
		
	}
	

}