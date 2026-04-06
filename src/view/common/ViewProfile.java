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
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import model.user.Faculty;
import model.user.Student;
import model.user.User;
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

		ImageIcon rawIcon = new ImageIcon(getClass().getResource("/resources/images/icons/Profile.png"));
		Image scaled = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		RoundedLabel pfp = new RoundedLabel(new ImageIcon(scaled), 2, new Color(91, 112, 121), 100);
		picPanel.add(pfp, gbcPfp);
		picPanel.revalidate();	
		picPanel.repaint();
	    //personal information panel
	    
	    info = new JPanel();
	    info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
	    info.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
	    info.setAlignmentX(LEFT_ALIGNMENT);

	    add(picPanel, BorderLayout.NORTH);
	    
	    
        container = new RoundedPanel(30, 2, new Color(117, 144, 156));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(243, 244, 247));

        info.add(container);
        add(info, BorderLayout.CENTER);
	    
	    btnPanel = new JPanel();
	    btns = new ConfirmPanel(this, 
	    		"GO BACK", "LOG OUT",
	    		new Color(91, 112 ,121), 2,
	    		new Color(91, 112 ,121), 2);
		btns.getConfirmPanel().setOpaque(false);
	    btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
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
		head.setForeground(new Color(91, 112 ,121));
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


	public void loadUser(Student user, List<String> data) {
		String[][] dataset = { { "Student Number", user.getUserID() },
				{ "Full Name", data.get(0)},
				{ "College", data.get(1)},
				{ "Program", data.get(2)},
				{ "Section", data.get(3)} };

		for (int i = 0; i < dataset.length; i++) {
			contentLbl(dataset[i][0], dataset[i][1]);
		}
	}

	public void loadUser(Faculty user, List<String> data) {
		String[][] dataset = { { "Employee Num", user.getUserID() },
				{ "Full Name", data.get(0) },
				{ "College", data.get(1) },
				{ "Position", user.getPosition() } };

		for (int i = 0; i < dataset.length; i++) {
			contentLbl(dataset[i][0], dataset[i][1]);
		}
	}

	    public void loadUser(User user) {
        
        JLabel adminLabel = new JLabel("ADMIN", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        adminLabel.setForeground(new Color(91, 112, 121));
        adminLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        GridBagConstraints gbcName = new GridBagConstraints();
        gbcName.gridx = 0;
        gbcName.gridy = 1;
        gbcName.anchor = GridBagConstraints.CENTER;
        gbcName.insets = new Insets(0, 0, 10, 0);
        picPanel.add(adminLabel, gbcName);
        picPanel.revalidate();
        picPanel.repaint();
 
    }

	public void setOnBackClicked(ActionListener action) {
		btns.setBtn1Action(action);
	}

	public void setOnLogoutClicked(ActionListener action) {
		btns.setBtn2Action(action);
	}
}
