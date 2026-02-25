package view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LandingPage {
	JFrame frame; 
	
	LandingPage(){
		frame = new JFrame("LANDING"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500, 700);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.WHITE);

        //title and search bar 
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(139, 0, 0));

        JLabel title = new JLabel("RoomFindr", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(title, BorderLayout.NORTH);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Border defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        );

        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(400, 40));
        searchBar.setBorder(defaultBorder);
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchPanel.add(searchBar, BorderLayout.CENTER);

        //para meron sila makikita initially sa search bar before they click on it, 
        // and then it disappears when they click on it
        String hintText = "Start your search...";
        searchBar.setText(hintText);
        searchBar.setForeground(Color.GRAY);

        searchBar.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals(hintText)) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText(hintText);
                    searchBar.setForeground(Color.GRAY);
                }
            }
        });

        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        //content panel for the counter board and most requested rooms section
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel counterBoardPanel = new JPanel(new BorderLayout());
        counterBoardPanel.setPreferredSize(new Dimension(300, 100)); 
        counterBoardPanel.setBackground(new Color(139, 0, 0));
        counterBoardPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel counterTitle = new JLabel("Successful RoomFinds", JLabel.LEFT);
        counterTitle.setFont(new Font("Segoe UI", Font.BOLD, 23));
        counterTitle.setForeground(Color.WHITE);
        counterBoardPanel.add(counterTitle, BorderLayout.NORTH);

        JLabel countLabel = new JLabel("670", JLabel.LEFT);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 58));
        countLabel.setForeground(Color.WHITE);

        counterBoardPanel.add(countLabel, BorderLayout.CENTER);
        contentPanel.add(counterBoardPanel, BorderLayout.NORTH);

        //most requested room section 
        JPanel mostRequestedPanel = new JPanel(new BorderLayout());
        mostRequestedPanel.setBackground(Color.WHITE);
        mostRequestedPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel mostRequestedTitle = new JLabel("Most Requested Rooms");
        mostRequestedTitle.setFont(new Font("Segoe UI", Font.BOLD, 23));
        mostRequestedTitle.setForeground(Color.BLACK);
        mostRequestedTitle.setHorizontalAlignment(JLabel.LEFT);
        mostRequestedPanel.add(mostRequestedTitle, BorderLayout.NORTH);

        JPanel mostRequestedContent = new JPanel(new GridLayout(2, 2, 10, 15));
        mostRequestedContent.setBackground(Color.WHITE);

        //Sample room cards
        for (int i = 0; i < 4; i++) {
            JPanel roomCard = new JPanel(new BorderLayout());
            roomCard.setBackground(new Color(190,190,190));
            roomCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

            JLabel roomName = new JLabel("Room ", JLabel.CENTER);
            roomName.setFont(new Font("Segoe UI", Font.BOLD, 16));
            roomName.setForeground(Color.WHITE);
            roomCard.add(roomName, BorderLayout.CENTER);

            JButton roomButton = new JButton("View Room");
            roomButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            roomButton.setBackground(new Color(139, 0, 0));
            roomButton.setForeground(Color.WHITE);
            roomCard.add(roomButton, BorderLayout.SOUTH);
            mostRequestedContent.add(roomCard);
        }

        mostRequestedPanel.add(mostRequestedContent, BorderLayout.CENTER);
        contentPanel.add(mostRequestedPanel, BorderLayout.CENTER);
        
        //to make the panel scrollable
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(contentPanel);
        
        JScrollPane scrollPanel = new JScrollPane(mainPanel);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(10);
        
        frame.add(scrollPanel, BorderLayout.CENTER);    
        
        //navigation bar, sample only
        JPanel navPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        navPanel.setPreferredSize(new Dimension(frame.getWidth(), 35));
        navPanel.setBackground(new Color(139,0,0));
        
        JLabel browseBtn = new JLabel("B", SwingConstants.CENTER); 
        browseBtn.setForeground(Color.white);
        
        JLabel filterBtn = new JLabel("F", SwingConstants.CENTER);
        filterBtn.setForeground(Color.white);
        
        JLabel reqBtn = new JLabel("R", SwingConstants.CENTER);
        reqBtn.setForeground(Color.white);
        
        JLabel profileBtn = new JLabel("P", SwingConstants.CENTER);
        profileBtn.setForeground(Color.white);
        
        navPanel.add(browseBtn); 
        navPanel.add(filterBtn);
        navPanel.add(reqBtn); 
        navPanel.add(profileBtn);
        
        frame.add(navPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
        
	}
	
	public static void main(String[] args) {
		new LandingPage();
	}

}
