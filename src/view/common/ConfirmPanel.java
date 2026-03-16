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
	ConfirmPanel(JFrame frame, String btn1, String btn2){
		confirmPanel = new JPanel(new GridLayout(1, 2, 40, 20)); 

        button1 = new RoundedButton(btn1, 25); 
        button1.setForeground(Color.WHITE);
        button1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button1.setBackground(new Color(139, 0, 0));
        button1.setFocusPainted(false);
        button1.setBorderPainted(false);
        
        button2 = new RoundedButton(btn2,25); 
        button2.setForeground(Color.WHITE);
        button2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button2.setBackground(new Color(139, 0, 0));
        button2.setFocusPainted(false);
        button2.setBorderPainted(false);
        
        confirmPanel.add(button1); 
        confirmPanel.add(button2); 
	}
	
	public ConfirmPanel(JPanel panel, String btn1, String btn2){
		confirmPanel = new JPanel(new GridLayout(1, 2, 40, 20)); 

        button1 = new RoundedButton(btn1, 25); 
        button1.setForeground(Color.WHITE);
        button1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button1.setBackground(new Color(139, 0, 0));
        button1.setFocusPainted(false);
        button1.setBorderPainted(false);
        
        button2 = new RoundedButton(btn2,25); 
        button2.setForeground(Color.WHITE);
        button2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button2.setBackground(new Color(139, 0, 0));
        button2.setFocusPainted(false);
        button2.setBorderPainted(false);
        
        confirmPanel.add(button1); 
        confirmPanel.add(button2); 
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
}