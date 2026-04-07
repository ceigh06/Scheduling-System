package view.faculty;

import dao.LookUpDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.schedule.Schedule;
import view.common.RequestForm;
import view.components.RoundedButton;
import view.components.RoundedPanel;
import view.components.RoundedTextField;




public class ViewFacultySched extends JPanel {

	RoundedButton goBackBtn;
	private JPanel contentPanel, formsPanel, 
	formsTop, formsBottom, timeValues, timeSection, 
	timeLabels;
	private RoundedPanel headerPanel; 
	private RoundedTextField field, timeInTxt, timeOutTxt;
	private JLabel headerLabel;

        public void setOnBackClicked(ActionListener action){
                goBackBtn.addActionListener(action);
        }

	public ViewFacultySched(String classType, Schedule schedule) {
		setLayout(new BorderLayout()); 
		setBackground(Color.WHITE); 
		
		contentPanel = new JPanel(new BorderLayout()); 
		contentPanel.setBackground(Color.WHITE); 
	
		formsPanel = new JPanel(); 
		formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
		formsPanel.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));
		formsPanel.setBackground(Color.WHITE); 
		
		headerPanel = addHeaderPanel(classType);
		formsPanel.add(headerPanel); 
		formsPanel.add(Box.createVerticalStrut(15));
		
		formsTop = new JPanel(new GridLayout(2,1,10,10)); 
		formsTop.setBackground(Color.WHITE);
        formsTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        
        field = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        RequestForm.styleField(field);
		
        formsTop.add(RequestForm.labeledField("Section", LookUpDAO.getFullSectionName(Integer.parseInt((schedule.getSectionKey())))));
        formsTop.add(RequestForm.labeledField("Room", schedule.getRoomCode()));
        formsPanel.add(formsTop);
        formsPanel.add(Box.createVerticalStrut(10));

        timeSection = new JPanel();
        timeSection.setLayout(new BoxLayout(timeSection, BoxLayout.Y_AXIS));
        timeSection.setBackground(Color.WHITE);
        timeSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        // Labels row
        timeLabels = new JPanel(new GridLayout(1, 2, 20, 0));
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
        timeValues = new JPanel(new GridLayout(1, 2, 20, 0));
        timeValues.setBackground(Color.WHITE);
        
        timeInTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        timeInTxt.setText(schedule.getTimeIn());
        RequestForm.styleField(timeInTxt);

        timeOutTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200), 
                null);
        timeOutTxt.setText(schedule.getTimeOut());
        RequestForm.styleField(timeOutTxt); 
        
        timeValues.add(timeInTxt);
        timeValues.add(timeOutTxt);
        
        timeSection.add(timeLabels);
        timeSection.add(Box.createVerticalStrut(6));
        timeSection.add(timeValues);
        
        formsPanel.add(timeSection);
        formsPanel.add(Box.createVerticalStrut(20));
		
        formsBottom = new JPanel(new GridLayout(2, 1, 0, 12)); 
        formsBottom.setBackground(Color.WHITE);
        formsBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        
        formsBottom.add(RequestForm.labeledField("Course", schedule.getCourseCode())); 
        formsBottom.add(RequestForm.labeledField("Professor", LookUpDAO.getFullFacultyName(schedule.getFacultyID())));
        formsPanel.add(formsBottom);
//        formsPanel.setBackground(Color.RED);
        formsPanel.add(Box.createVerticalStrut(30));
        
        JPanel goBackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,30));
        goBackPanel.setBackground(Color.WHITE);
        
        goBackBtn = new RoundedButton("GO BACK", 20, new Color(91, 112, 121), 2); 
        goBackBtn.setForeground(Color.WHITE);
        goBackBtn.setBackground(new Color(117,144,156)); 
        goBackBtn.setPreferredSize(new Dimension(200,40));
        
        
        
        goBackPanel.add(goBackBtn); 
        
        contentPanel.add(formsPanel, BorderLayout.CENTER);
        contentPanel.add(goBackPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
	}
	
	public RoundedPanel addHeaderPanel(String classType) {
		headerPanel = new RoundedPanel(20, 4, new Color(91,112,121)); 
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); 
		headerPanel.setBackground(new Color(117,144,156));
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		headerLabel = new JLabel(classType, JLabel.CENTER);
		headerLabel.setFont(new Font("Segoe UI",Font.BOLD, 23));
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		headerPanel.add(headerLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        return headerPanel;
	}
}
