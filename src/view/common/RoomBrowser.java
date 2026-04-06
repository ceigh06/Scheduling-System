package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.Room;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;

public class RoomBrowser extends JPanel{
	private JPanel roomPanel;
	private JPanel roomCard;

	private ConfirmPanel confirmArea;

	private RoundedPanel selectedPanel; 
	
	private Room selectedRoom;


	private Consumer<Room> onRoomClicked;
	
	public void setOnRoomClicked(Consumer<Room> action){
		this.onRoomClicked = action;
	}

	public void setOnBackButton(ActionListener action){
		confirmArea.setBtn1Action(action);
	}

	public void setOnConfirmButton(ActionListener action){
		confirmArea.setBtn2Action(action);
	}

	public Room getSelectedRoom(){
		return selectedRoom;
	}
	
	public void loadRooms(List<Room> rooms) {
		roomCard.removeAll();
		for (Room room : rooms) {
			roomCard.add(createRoomCards(room, room.getStatus(), room.getBuildingCode()));
			roomCard.add(Box.createVerticalStrut(5)); // Add spacing between cards
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

		String header = "Welcome to " + bldgName;
		
		if(bldgName == null){
			header = "Showing Available Rooms";
		}

        JLabel selectedBldg = new JLabel(header , JLabel.CENTER);
        selectedBldg.setForeground(Color.BLACK);
        selectedBldg.setFont(new Font("Georgia", Font.BOLD, 23));
        welcomePanel.add(selectedBldg);
        
        wrapper.add(welcomePanel);

        roomPanel = new JPanel(new BorderLayout());
        roomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel roomTitle = new JLabel("Select a room to schedule.");
        roomTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        roomPanel.add(roomTitle, BorderLayout.NORTH);

        // roomCard = new JPanel(new GridLayout(rooms.size(), 1, 10, 10));
		roomCard = new JPanel(); 
		roomCard.setLayout(new BoxLayout(roomCard, BoxLayout.Y_AXIS));
        roomCard.setBorder(new EmptyBorder(10, 0, 10, 0));

		loadRooms(rooms);

		roomPanel.add(roomCard, BorderLayout.CENTER);

		confirmArea = new ConfirmPanel(MainFrame.getFrame(),
			"GO BACK", "CONFIRM",
			new Color(227,75,75), 2, 
			new Color(77,139, 78),2);
		confirmArea.setBtn1Color(new Color(255, 100, 100));
		confirmArea.setBtn2Color(new Color(63, 193, 127));
		confirmArea.getConfirmPanel().setOpaque(false);
        roomPanel.add(confirmArea.getConfirmPanel(), BorderLayout.SOUTH);
        wrapper.add(roomPanel);
        
        JScrollPane scrollPanel = new JScrollPane(wrapper);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        scrollPanel.setBorder(null);
        add(scrollPanel, BorderLayout.CENTER);
        
	}

	public RoomBrowser(String bldgName,List<Room> rooms, boolean viewArchives) {
		if (!viewArchives) return;
		setLayout(new BorderLayout()); 
		setBackground(new Color(250, 249, 246)); 
		
		JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

		String header = "Welcome to " + bldgName;
		
		if(bldgName == null){
			header = "Showing Available Rooms";
		}

        JLabel selectedBldg = new JLabel(header , JLabel.CENTER);
        selectedBldg.setForeground(Color.BLACK);
        selectedBldg.setFont(new Font("Georgia", Font.BOLD, 23));
        welcomePanel.add(selectedBldg);
        
        wrapper.add(welcomePanel);

        roomPanel = new JPanel(new BorderLayout());
        roomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel roomTitle = new JLabel("Select a room to view archived schedules.");
        roomTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        roomPanel.add(roomTitle, BorderLayout.NORTH);

        // roomCard = new JPanel(new GridLayout(rooms.size(), 1, 10, 10));
		roomCard = new JPanel(); 
		roomCard.setLayout(new BoxLayout(roomCard, BoxLayout.Y_AXIS));
        roomCard.setBorder(new EmptyBorder(10, 0, 10, 0));

		loadRooms(rooms);

		roomPanel.add(roomCard, BorderLayout.CENTER);

		confirmArea = new ConfirmPanel(MainFrame.getFrame(),
			"GO BACK", "CONFIRM",
			new Color(227,75,75), 2, 
			new Color(77,139, 78),2);
		confirmArea.setBtn1Color(new Color(255, 100, 100));
		confirmArea.setBtn2Color(new Color(63, 193, 127));
        roomPanel.add(confirmArea.getConfirmPanel(), BorderLayout.SOUTH);
        wrapper.add(roomPanel);
        
        JScrollPane scrollPanel = new JScrollPane(wrapper);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        scrollPanel.setBorder(null);
        add(scrollPanel, BorderLayout.CENTER);
        
	}

	RoundedPanel createRoomCards(Room room, String status, String buildingName) {

	        String description;

	        RoundedPanel roomCard = new RoundedPanel(20,0);
	        roomCard.setPreferredSize(new Dimension(400, 100));
	        roomCard.setMaximumSize(new Dimension(400,100));
	        roomCard.setLayout(new BorderLayout(7, 0));

	        RoundedPanel codePanel = new RoundedPanel(20,1, new Color(91,112,121), new GridBagLayout());
	        codePanel.setBackground(new Color(91,112,121));
	        codePanel.setPreferredSize(new Dimension(90, 60));
	        codePanel.setMaximumSize(new Dimension(90,60));

			// **Insert line breaks every 5 characters**
		String roomCode = room.getRoomCode();
		String wrappedCode = roomCode.replaceAll("(.{6})", "$1\n").trim();	

		JTextArea codeLabel = new JTextArea(wrappedCode);
		codeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
		codeLabel.setForeground(Color.WHITE);
		codeLabel.setBackground(codePanel.getBackground()); 
		codeLabel.setEditable(false);      
		codeLabel.setFocusable(false);     
		codeLabel.setBorder(null);         
		codeLabel.setOpaque(true);           
		codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		codeLabel.setSize(80, Short.MAX_VALUE);
		codeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;      
		gbc.weighty = 1.0;        
		gbc.anchor = GridBagConstraints.CENTER; 
		gbc.fill = GridBagConstraints.NONE;    

		int lineCount = wrappedCode.split("\n").length;
		int lineHeight = codeLabel.getFontMetrics(codeLabel.getFont()).getHeight();
		int preferredHeight = lineCount * lineHeight;

		codeLabel.setMaximumSize(new Dimension(80, preferredHeight));
		codeLabel.setPreferredSize(new Dimension(80, preferredHeight));
			codePanel.add(codeLabel, gbc);


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

					if (onRoomClicked != null){ // passing back of model if clicked
						selectedRoom = room;
					}

	            	//de-selecting so only one panel is selected 
	            	//selecting again means removing the selection 
	                if(selectedPanel == infoPanel) {
	                    infoPanel.setBorderThickness(0);
	                    selectedPanel = null;
	                    selectedRoom = null;
	                } else {
	                	//de-selecting the previous
	                    if(selectedPanel != null) {
	                        selectedPanel.setBorderThickness(0);
	                    }
	                    
	                    //selecting the room cards users chose 
	                    infoPanel.setBorderColor(new Color(91,112,121));
	                    infoPanel.setBorderThickness(2);
	                    selectedPanel = infoPanel;
	                    selectedRoom = room;
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
			 selectedRoom = null;
		 }
	 }
}
