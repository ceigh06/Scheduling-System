package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Desktop.Action;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.user.Faculty;
import model.user.Student;
import view.common.ConfirmPanel;
import view.components.RoundedLabel;
import view.components.RoundedPanel;

public class ViewProfile extends JPanel {
	JPanel picPanel, info, btnPanel;
	String header, content;
	String userType;//if student or faculty
	ConfirmPanel btns = new ConfirmPanel(this, "GO BACK", "LOG OUT");


	public ViewProfile(){
	    setLayout(new BorderLayout());
	    //profile pic panel
	    picPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbcPfp = new GridBagConstraints();
	    gbcPfp.gridx = 0;
	    gbcPfp.gridy = 0;
	    gbcPfp.anchor = GridBagConstraints.CENTER;
	    gbcPfp.insets = new Insets(20, 0, 10, 0);

	    ImageIcon rawIcon = new ImageIcon("pfp.png");
	    Image scaledImg = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
	    RoundedLabel pfp = new RoundedLabel(new ImageIcon(scaledImg), 2, Color.GRAY, 100);

	    picPanel.add(pfp, gbcPfp);
	    //personal information panel
	    
	    info = new JPanel();
	    info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
	    info.setAlignmentX(LEFT_ALIGNMENT);
	    info.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

	    add(picPanel, BorderLayout.NORTH);
	    add(info, BorderLayout.CENTER);
	    
	    userType = "student";
	    //BACKEND NOTE: U CAN ALSO USE BOOLEAN, u can still make this shorter
	    if(userType == "student") {
	    	String[][] dataset = {{"Student Number","2024100944"},
	    			{"Full Name","Krizzia Keith O. Garcia"},
	    			{"College","College of Information and Communications Technology (CICT)"},
	    			{"Program","Bachelor of Science in Information Technology (BSIT)"},
	    			{"Section","BSIT 2A G2"}};
	    	
	    }else if(userType == "faculty") {
	    	String[][] dataset = {{"Employee Num","2024100944"},
	    			{"Full Name","Master beth"},
	    			{"College","College of Information and Communications Technology (CICT)"},
	    			{"Position","Faculty"}};

	    	for(int i = 0; i < dataset.length; i++) {
	    		contentLbl(dataset[i][0],dataset[i][1]);
	    	}
	    }
		
	    btnPanel = new JPanel();
	    
	    btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	    btnPanel.add(btns.getConfirmPanel(), BorderLayout.CENTER); 
	    
	    add(btnPanel, BorderLayout.SOUTH);
	}

	public void setOnBackClicked(ActionListener action) {
		btns.setBtn1Action(action);
	}

	public void setOnLogoutClicked(ActionListener action) {
		btns.setBtn2Action(action);
	}

	public void contentLbl(String header, String content){
	    RoundedPanel entryPanel = new RoundedPanel(25, 3,new Color(114, 116, 113));
	    entryPanel.setBackground(new Color(221, 221, 219));
	    entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
	    entryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); // uniform spacing

	    JLabel head = new JLabel(header);
	    head.setFont(new Font("Arial", Font.BOLD, 13));
	    head.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    //text area para keri makuha yung info if mahaba and automatic na bumababa yung contents if mag ooverflow sha sa size
	    JTextArea text = new JTextArea(content);
	    text.setFont(new Font("Arial", Font.PLAIN, 15));
	    text.setLineWrap(true);
	    text.setWrapStyleWord(true);
	    text.setEditable(false);
	    text.setOpaque(false);
	    text.setMaximumSize(new Dimension(450, 50));
	    text.setAlignmentX(Component.LEFT_ALIGNMENT);

	    entryPanel.add(head);
	    entryPanel.add(text);

	    info.add(entryPanel);
	    info.add(Box.createRigidArea(new Dimension(0, 10)));
	    info.revalidate();
	    info.repaint();
	}

	public void loadUser(Student user) {
		String[][] dataset = {{"Student Number",user.getUserID()},
	    			{"Full Name", user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName()},
	    			{"College", "N/A"},
	    			{"Program", "N/A"},
	    			{"Section", String.valueOf(user.getSectionKey())}};

		for(int i = 0; i < dataset.length; i++) {
	    		contentLbl(dataset[i][0],dataset[i][1]);
	    	}
	}

	public void loadUser(Faculty user) {
		
	}
}
