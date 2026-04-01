package view.common;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import view.components.RoundedTextField;

public class ControlNotifs extends JPanel{
        // BACKEND TO DO: Make sure to remove this and retrieve 
        // actual data from the database
        
	String studeNo = "2024101030", 
	        name = "Jessie Claire Santos",
	        section = "2AG2",
	        roomCode = "PH 101",
	        timeIn = "7:00 AM",
	        timeOut = "10:00 AM",
	        course = "Event-Driven Programming",
	        professor = "Janice Castillo";
	String reqStatus = "PENDING"; 
	
	public ControlNotifs(JFrame frame, String message) {
		setLayout(new BorderLayout()); 
		setBackground(Color.WHITE);
		
		JPanel contentPanel = new JPanel(new BorderLayout()); 
		contentPanel.setBackground(Color.WHITE); 
		
		JPanel formsPanel = new JPanel(); 
		formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
		formsPanel.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));
		formsPanel.setBackground(Color.WHITE); 
		
		JPanel headerPanel = addHeaderPanel(reqStatus);
		formsPanel.add(headerPanel); 
		formsPanel.add(Box.createVerticalStrut(15)); 
		
		// Top section - Student info (4 rows, tighter spacing)
        JPanel formsTop = new JPanel(new GridLayout(4, 1, 0, 12)); 
        formsTop.setBackground(Color.WHITE);
        formsTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        
        RoundedTextField field = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        RequestForm.styleField(field);
        
        formsTop.add(RequestForm.labeledField("Sstudent No.", studeNo));
        formsTop.add(RequestForm.labeledField("Name", name));
        formsTop.add(RequestForm.labeledField("Section", section));
        formsTop.add(RequestForm.labeledField("Room code", this.roomCode));
        formsPanel.add(formsTop);
        formsPanel.add(Box.createVerticalStrut(20));

        JPanel timeSection = new JPanel();
        timeSection.setLayout(new BoxLayout(timeSection, BoxLayout.Y_AXIS));
        timeSection.setBackground(Color.WHITE);
        timeSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        // Labels row
        JPanel timeLabels = new JPanel(new GridLayout(1, 2, 20, 0));
        timeLabels.setBackground(Color.WHITE);
        
        JLabel timeInLbl = new JLabel("Time In"); 
        timeInLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeInLbl.setForeground(new Color(91,112,121));
        
        JLabel timeOutLbl = new JLabel("Time Out"); 
        timeOutLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeOutLbl.setForeground(new Color(91,112,121));
        
        timeLabels.add(timeInLbl);
        timeLabels.add(timeOutLbl);
        
        // Values row
        JPanel timeValues = new JPanel(new GridLayout(1, 2, 20, 0));
        timeValues.setBackground(Color.WHITE);
        
        RoundedTextField timeInTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        timeInTxt.setText(timeIn);
        RequestForm.styleField(timeInTxt);

        RoundedTextField timeOutTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        timeOutTxt.setText(timeOut);
        RequestForm.styleField(timeOutTxt); 
        
        timeValues.add(timeInTxt);
        timeValues.add(timeOutTxt);
        
        timeSection.add(timeLabels);
        timeSection.add(Box.createVerticalStrut(6));
        timeSection.add(timeValues);
        
        formsPanel.add(timeSection);
        formsPanel.add(Box.createVerticalStrut(20));

        // Bottom section - Course and Professor
        JPanel formsBottom = new JPanel(new GridLayout(2, 1, 0, 12)); 
        formsBottom.setBackground(Color.WHITE);
        formsBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        
        formsBottom.add(RequestForm.labeledField("Course", course)); 
        formsBottom.add(RequestForm.labeledField("Professor", professor));
        formsPanel.add(formsBottom);
        formsPanel.add(Box.createVerticalStrut(30));

        // Confirm buttons
        ConfirmPanel confirmArea = new ConfirmPanel(MainFrame.getFrame(),
		"GO BACK", "SUBMIT",
		new Color(91, 112 ,121), 2, 
		new Color(91, 112 ,121),2);
	
        formsPanel.add(confirmArea.getConfirmPanel()); 

        contentPanel.add(formsPanel, BorderLayout.CENTER);

        JScrollPane scrollPanel = new JScrollPane(contentPanel);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder());
        scrollPanel.getViewport().setBackground(Color.WHITE);

        add(scrollPanel, BorderLayout.CENTER);
	}
	
	public JPanel addHeaderPanel(String status) {
		JPanel headerPanel = new JPanel(); 
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); 
		headerPanel.setBackground(Color.WHITE);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel headerLabel = new JLabel(status, JLabel.CENTER);
		headerLabel.setFont(new Font("Segoe UI",Font.BOLD, 23));
		headerLabel.setForeground(new Color(91,112,121));
		headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		headerPanel.add(headerLabel);
                headerPanel.add(Box.createVerticalStrut(8));
        
        return headerPanel;
	}

}
