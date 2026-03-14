package view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import view.components.RoundedButton;

public class ConfirmPanel {

	public JPanel confirmPanel; 
	private RoundedButton button1, button2; 
	
	public void setActions(ActionListener action1, ActionListener action2) {
        setBtn1Action(action1);
        setBtn2Action(action2);
    }
	
	public JPanel getConfirmPanel() {
		return confirmPanel;
	}
	
	public RoundedButton getBtn1(){
		return button1; 
	}
	
	public RoundedButton getBtn2() {
		return button2; 
	}
	
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
	
	public void setSize(int w, int h) {
		button1.setPreferredSize(new Dimension(w,h));
		button2.setPreferredSize(new Dimension(w,h));
	}
	
	//parameter 'action' is the one to execute upon click
	public void setBtn1Action(ActionListener action) {
        for (ActionListener a : button1.getActionListeners()) {
            button1.removeActionListener(a);
        }
        button1.addActionListener(action);
    }
	
	public void setBtn2Action(ActionListener action) {
        for (ActionListener a : button2.getActionListeners()) {
            button2.removeActionListener(a);
        }
        button2.addActionListener(action);
    }
}
