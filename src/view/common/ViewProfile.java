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
	JPanel picPanel,info, btnPanel, infoWrapper;
	String header, content;
	String userType;//if student or faculty
	RoundedLabel pfp;
	RoundedPanel container;
	ImageIcon rawIcon;
	Image scaledImg;
	ConfirmPanel btns;
	
	public ViewProfile(){
	    setLayout(new BorderLayout());
	    //profile pic panel
	    picPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbcPfp = new GridBagConstraints();
	    gbcPfp.gridx = 0;
	    gbcPfp.gridy = 0;
	    gbcPfp.anchor = GridBagConstraints.CENTER;
	    gbcPfp.insets = new Insets(20, 0, 10, 0);

	    rawIcon = new ImageIcon("pfp.png");
	    scaledImg = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
	    pfp = new RoundedLabel(new ImageIcon(scaledImg), 2,new Color(139, 0, 0), 100);
	    

	    picPanel.add(pfp, gbcPfp);
	    //personal information panel
	    
	    info = new JPanel();
	    info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
	    info.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
	    info.setAlignmentX(LEFT_ALIGNMENT);

	    add(picPanel, BorderLayout.NORTH);
	    
	    
        container = new RoundedPanel(30, 5, new Color(139, 0, 0));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(221, 221, 219));

        info.add(container);
        add(info, BorderLayout.CENTER);
	    
	    btnPanel = new JPanel();
	    btns = new ConfirmPanel(this, "GO BACK", "LOG OUT");
	    btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	    btnPanel.add(btns.getConfirmPanel(), BorderLayout.CENTER); 
	    
	    add(btnPanel, BorderLayout.SOUTH);

	}

	public void contentLbl(String header, String content){
		infoWrapper = new JPanel();
		infoWrapper.setLayout(new BoxLayout(infoWrapper, BoxLayout.Y_AXIS));
		infoWrapper.setBackground(new Color(221, 221, 219));
		infoWrapper.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
		infoWrapper.setOpaque(false);

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

	    infoWrapper.add(head);
	    infoWrapper.add(text);

	    container.add(infoWrapper);
	    container.add(Box.createRigidArea(new Dimension(0, 10)));
	    
	    container.revalidate();
	    container.repaint();
	}


	public void loadUser(Student user) {
		String[][] dataset = { { "Student Number", user.getUserID() },
				{ "Full Name", user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName() },
				{ "College", "N/A" },
				{ "Program", "N/A" },
				{ "Section", String.valueOf(user.getSectionKey()) } };

		for (int i = 0; i < dataset.length; i++) {
			contentLbl(dataset[i][0], dataset[i][1]);
		}
	}

	public void loadUser(Faculty user) {
		String[][] dataset = { { "Employee Num", user.getUserID() },
				{ "Full Name", user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName() },
				{ "College", user.getCollegeCode() },
				{ "Position", user.getPosition() } };

		for (int i = 0; i < dataset.length; i++) {
			contentLbl(dataset[i][0], dataset[i][1]);
		}
	}
}
