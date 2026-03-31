package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import view.components.RoundedLabel;
import view.components.RoundedPanel;

@SuppressWarnings("serial")
public class CheckRequests extends JPanel{
	JScrollPane mainScrollPane;
	JPanel reqNumPanel, requestsWrapper;
	
	JLabel reqNum;
	CheckRequests(){
		setLayout(new BorderLayout());
		
		reqNumPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		reqNum = new JLabel("3 Classroom Schedule Requests");
		reqNum.setFont(new Font("Arial", Font.PLAIN, 20));
		reqNumPanel.add(reqNum);
		
		requestsWrapper = new JPanel();
		requestsWrapper.setLayout(new BoxLayout(requestsWrapper, BoxLayout.Y_AXIS));
	    requestsWrapper.setOpaque(false);

		requestsWrapper.add(reqNumPanel);
		
		mainScrollPane = new JScrollPane(requestsWrapper);
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		mainScrollPane.setBorder(null);
		add(mainScrollPane, BorderLayout.CENTER);
		
		newRequest();
		newRequest();
		newRequest();
		newRequest();
		
		
	}
	
	//UTILITY
	public String[] setSchedRequest(String name, String section, String course, String reqTime, String roomCode, String time) {
		String[] reqInfo = {name, section, course, reqTime, roomCode, time};
		//gets student information na need makita ni faculty 
		//BACKEND TO DO: USE SETTERS AND SETTERS NA LANG FOR STUD INFO
		return  reqInfo;
	}
	
	//new request panel
	public void newRequest() { 

		String[] info = setSchedRequest("Jessie Santos", "BSIT 2A G2", "IT 208", "10:00 AM", "Prog Lab 2", "10:00 AM - 1:00 PM");
		
		//panel for student infos
		JPanel requestPanel = new JPanel(new GridBagLayout());
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

		RoundedLabel pfp = new RoundedLabel(new ImageIcon(scaledImg), 2, new Color(117, 144, 156), 100);	
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
		//delete or confirm buttons
		ConfirmPanel confirmBtns = new ConfirmPanel(
				requestPanel, 
				"DELETE", "CONFIRM",
				new Color(227,75,75), 2, 
				new Color(77,139, 78),2);
		confirmBtns.setBtn1Color(new Color(255, 100, 100));
		confirmBtns.setBtn2Color(new Color(63, 193, 127));

		RoundedPanel mainPanel = new RoundedPanel(50, 10, new Color(117, 144, 156), new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.setBackground(new Color(243, 244, 247));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		requestPanel.setOpaque(false);
		confirmBtns.getConfirmPanel().setOpaque(false);

		mainPanel.add(requestPanel, BorderLayout.CENTER);
		mainPanel.add(confirmBtns.getConfirmPanel(), BorderLayout.SOUTH);

		// Wrap in transparent container
		JPanel wrapper = new JPanel(new FlowLayout());
		wrapper.setOpaque(false);
		wrapper.add(mainPanel);
		
		requestsWrapper.add(wrapper);
		requestsWrapper.revalidate();
		requestsWrapper.repaint();
		
	}

}