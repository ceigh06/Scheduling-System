package view.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import view.components.RoundedButton;

public class ConfirmPanel {

	public JPanel confirmPanel; 
	private RoundedButton button1, button2; 
	 //BYE HELLO 
	 // Constructor with JFrame (unchanged, but now accepts border args)
    public ConfirmPanel(JFrame frame, String btn1, String btn2, Color border1, int thickness1, Color border2, int thickness2){
        initPanel(btn1, btn2, border1, thickness1, border2, thickness2);
    }

    // Constructor with JPanel (unchanged, but now accepts border args)
    public ConfirmPanel(JPanel panel, String btn1, String btn2, Color border1, int thickness1, Color border2, int thickness2){
        initPanel(btn1, btn2, border1, thickness1, border2, thickness2);
    }


    private void initPanel(String btn1, String btn2, Color border1, int thickness1, Color border2, int thickness2){
        confirmPanel = new JPanel(new GridLayout(1, 2, 40, 20)); 

        button1 = new RoundedButton(btn1, 25, border1, thickness1); 
        button1.setForeground(Color.WHITE);
        button1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button1.setBackground(new Color(27, 94, 60)); // default green
        button1.setFocusPainted(false);

        button2 = new RoundedButton(btn2, 25, border2, thickness2); 
        button2.setForeground(Color.WHITE);
        button2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button2.setBackground(new Color(27, 94, 60)); // default green
        button2.setFocusPainted(false);

        confirmPanel.add(button1); 
        confirmPanel.add(button2); 
    }
     public void setBtn1Color(Color color) {
        button1.setBackground(color);
    }

    public void setBtn2Color(Color color) {
        button2.setBackground(color);
    }
	
	//parameter 'action' is the one to execute upon click
	public void setBtn1Action(ActionListener action) {
        button1.addActionListener(action);
    }
	
	public void setBtn2Action(ActionListener action) {
        button2.addActionListener(action);
    }	
	
	public JPanel getConfirmPanel() {
		return confirmPanel;
	}

    public void setBackground(Color color) {
        confirmPanel.setBackground(color);
    }
}