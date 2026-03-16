package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Room;
import view.components.RoundedPanel;

@SuppressWarnings("serial")
public class RoomBrowser extends JPanel{
	JPanel roomPanel;
	JPanel roomCard;
	private RoundedPanel selectedPanel; 
	private String selectedRoomCode;
	int roomCtr = 0; //kunwari lang
	
	public String selectedRoomCode() {
		return selectedRoomCode;
	}
	
	public void loadRooms(List<Room> rooms) {
		roomCard.removeAll();
		for (Room room : rooms) {
			roomCtr++;
			roomCard.add(createRoomCards(room.getRoomCode(), room.getStatus(), room.getBuildingCode()));
		}
		
		roomCard.revalidate();
		roomCard.repaint();
	}

	public RoomBrowser(String bldgName,List<Room> rooms) {
		setLayout(new BorderLayout()); 
		setBackground(new Color(250, 249, 246)); 
		
		JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JLabel selectedBldg = new JLabel("Welcome to " + bldgName, JLabel.CENTER);
        selectedBldg.setForeground(Color.BLACK);
        selectedBldg.setFont(new Font("Georgia", Font.BOLD, 23));
        welcomePanel.add(selectedBldg);
        
        wrapper.add(welcomePanel);

        roomPanel = new JPanel(new BorderLayout());
        roomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel roomTitle = new JLabel("Select a room to schedule.");
        roomTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        roomPanel.add(roomTitle, BorderLayout.NORTH);

        roomCard = new JPanel(new GridLayout(roomCtr, 1, 10, 10));
        roomCard.setBorder(new EmptyBorder(10, 0, 10, 0));

		loadRooms(rooms);

		roomPanel.add(roomCard, BorderLayout.CENTER);

        ConfirmPanel confirmArea = new ConfirmPanel(MainFrame.getFrame(),"Go Back", "Confirm");
        roomPanel.add(confirmArea.getConfirmPanel(), BorderLayout.SOUTH);
        wrapper.add(roomPanel);
        
        confirmArea.setBtn1Action(e -> {
        	MainFrame.showPanel("browseBuilding", "Browse Buildings"); 
        	clearSelection();
        });
        
        confirmArea.setBtn2Action(e ->{
        	if(selectedRoomCode == null) {
        		JOptionPane.showMessageDialog(null, "Please selected a room first", "Empty selection", JOptionPane.WARNING_MESSAGE);
        		return; 
        	}
        	
        	ViewSchedule sched = new ViewSchedule(selectedRoomCode); 
        	MainFrame.setNavBarVisible(true); 
        	MainFrame.addContentPanel(sched, "viewSched"); 
        	MainFrame.showPanel("viewSched", "View Schedule");
        	clearSelection(); 
        });
        
        JScrollPane scrollPanel = new JScrollPane(wrapper);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPanel, BorderLayout.CENTER);
        
	}
	
	 RoundedPanel createRoomCards(String roomCode, String status, String buildingName) {
	        String description;

	        RoundedPanel roomCard = new RoundedPanel(20,0);
	        roomCard.setPreferredSize(new Dimension(400, 100));
	        roomCard.setMaximumSize(new Dimension(400,100));
	        
	        RoundedPanel codePanel = new RoundedPanel(20,1, new Color(130,0,0));
	        codePanel.setBackground(new Color(139, 0, 0));
	        codePanel.setPreferredSize(new Dimension(90, 60));
	        codePanel.setMaximumSize(new Dimension(90,60));
	        roomCard.setLayout(new BorderLayout(7, 0));

	        JLabel codeLabel = new JLabel(roomCode, SwingConstants.CENTER);
	        codeLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
	        codeLabel.setForeground(Color.WHITE);
	        codeLabel.setAlignmentX(CENTER_ALIGNMENT);
	        codePanel.add(codeLabel);

	        RoundedPanel infoPanel = new RoundedPanel(20,0); //NOTE: border shouldn't be visible
	        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
	        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
	        infoPanel.setBackground(Color.WHITE);

	        JLabel statusLabel = new JLabel(status.toUpperCase());
	        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

	        JLabel nameLabel = new JLabel(buildingName);
	        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
			
	        if (status.equals("0")) {
				statusLabel.setText("Available");
	            description = "Confirm to check room schedule.";
	        // } else if (status.equalsIgnoreCase("Occupied")) { // this is not in the database only available and under maintenance
	        //     description = "This room is fully occupied today.";
	        } else if (status.equals("1")) {
				statusLabel.setText("Under Maintenance");
	            description = "This room cannot be used until further notice.";
	        } else {
	            description = "Error occurred. Please try again.";
	        }

	        JLabel descriptionLabel = new JLabel(description);
	        descriptionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
	        descriptionLabel.setForeground(Color.DARK_GRAY);

	        infoPanel.add(statusLabel);
	        infoPanel.add(nameLabel);
	        infoPanel.add(descriptionLabel);

	        roomCard.add(codePanel, BorderLayout.WEST);
	        roomCard.add(infoPanel, BorderLayout.CENTER);
	        roomCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

	        roomCard.addMouseListener(new MouseAdapter(){
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	//de-selecting so only one panel is selected 
	            	//selecting again means removing the selection 
	                if(selectedPanel == infoPanel) {
	                    infoPanel.setBorderThickness(0);
	                    selectedPanel = null;
	                    selectedRoomCode = null;
	                } else {
	                	//de-selecting the previous
	                    if(selectedPanel != null) {
	                        selectedPanel.setBorderThickness(0);
	                    }
	                    
	                    //selecting the room cards users chose 
	                    infoPanel.setBorderColor(new Color(139,0,0));
	                    infoPanel.setBorderThickness(2);
	                    selectedPanel = infoPanel;
	                    selectedRoomCode = roomCode;
	                }
	                //"repaint" is remodeling the rounded panel because 
	                //rounded panel is different from normal panels 
	                infoPanel.repaint();
	                if(selectedPanel != null) {
	                    selectedPanel.repaint();
	                }
	            }
	        });
	        return roomCard;
	    }
	 
	public void clearSelection() {
		 if(selectedPanel != null) {
			 selectedPanel = null;
			 selectedRoomCode = null;
		 }
	 }
}
