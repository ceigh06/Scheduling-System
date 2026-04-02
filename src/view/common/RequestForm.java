package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
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

@SuppressWarnings("serial")
public class RequestForm extends JPanel {

    String studeNo = "2024101030", 
           name = "Jessie Claire Santos",
           section = "2AG2",
           roomCode,
           timeIn = "7:00 AM",
           timeOut = "10:00 AM",
           course = "Event-Driven Programming",
           professor = "Janice Castillo";

    public RequestForm(JFrame frame, String roomCode) {
        this.roomCode = roomCode;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        JPanel formsPanel = new JPanel();
        formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
        formsPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formsPanel.setBackground(Color.WHITE);

        JLabel sectionHeader = new JLabel("Confirm your request", JLabel.CENTER); 
        sectionHeader.setForeground(Color.DARK_GRAY);
        sectionHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sectionHeader.setAlignmentX(CENTER_ALIGNMENT);
        formsPanel.add(sectionHeader);
        formsPanel.add(Box.createVerticalStrut(25));

        // Top section - Student info (4 rows, tighter spacing)
        JPanel formsTop = new JPanel(new GridLayout(4, 1, 0, 12)); 
        formsTop.setBackground(Color.WHITE);
        formsTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        
        formsTop.add(labeledField("Student No.", studeNo));
        formsTop.add(labeledField("Name", name));
        formsTop.add(labeledField("Section", section));
        formsTop.add(labeledField("Room Code", this.roomCode));
        formsPanel.add(formsTop);
        formsPanel.add(Box.createVerticalStrut(20));

        // Middle section - Time In/Time Out (labels row, values row)
        JPanel timeSection = new JPanel();
        timeSection.setLayout(new BoxLayout(timeSection, BoxLayout.Y_AXIS));
        timeSection.setBackground(Color.WHITE);
        timeSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        // Labels row
        JPanel timeLabels = new JPanel(new GridLayout(1, 2, 20, 0));
        
        timeLabels.setBackground(Color.WHITE);
        
        JLabel timeInLbl = new JLabel("Time In"); 
        timeInLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeInLbl.setForeground(Color.DARK_GRAY);
        
        JLabel timeOutLbl = new JLabel("Time Out"); 
        timeOutLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeOutLbl.setForeground(Color.DARK_GRAY);
        
        timeLabels.add(timeInLbl);
        timeLabels.add(timeOutLbl);
        
        // Values row
        JPanel timeValues = new JPanel(new GridLayout(1, 2, 20, 0));
        timeValues.setBackground(Color.WHITE);
        
        RoundedTextField timeInTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        timeInTxt.setText(timeIn);
        styleField(timeInTxt);

        RoundedTextField timeOutTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        timeOutTxt.setText(timeOut);
        styleField(timeOutTxt); 
        
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
        
        formsBottom.add(labeledField("Course", course)); 
        formsBottom.add(labeledField("Professor", professor));
        formsPanel.add(formsBottom);
        formsPanel.add(Box.createVerticalStrut(30));

        // Confirm buttons
        ConfirmPanel reqConfirm = new ConfirmPanel(MainFrame.getFrame(),
		    "GO BACK", "SUBMIT",
		    new Color(91, 112 ,121), 2, 
		    new Color(91, 112 ,121),2);

        reqConfirm.setBtn1Action(e -> {
        	//BACKEND TO DO: Make it so that if the user wants to go back, 
        	//they are still browsing the same building that they selected
        });
        
        //make the message dynamic based on the request status 
        reqConfirm.setBtn2Action(e -> {
        	MainFrame.setNavBarVisible(false); 
        	MainFrame.addContentPanel(
        			new NotificationMessage("C:\\Users\\HP\\Downloads\\checkIcon.png","Your request is successfully submitted."), 
        			"Notification"); 
        	MainFrame.showPanel("Notification", "RoomFindr"); 
        });
        
        
        formsPanel.add(reqConfirm.getConfirmPanel()); 
        contentPanel.add(formsPanel, BorderLayout.CENTER);

        JScrollPane scrollPanel = new JScrollPane(contentPanel);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder());
        scrollPanel.getViewport().setBackground(Color.WHITE);

        add(scrollPanel, BorderLayout.CENTER);
    }

    public static JPanel labeledField(String labelText, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.DARK_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        label.setAlignmentX(LEFT_ALIGNMENT);

        RoundedTextField field = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        field.setText(value);
        styleField(field);
        field.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(field);
        return panel;
    }

    public static void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setForeground(Color.DARK_GRAY);
        field.setEditable(false);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }
}