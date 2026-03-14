package view.landing;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import view.common.MainFrame;
import view.components.RoundedButton;
import view.components.RoundedPanel;
import view.components.RoundedPasswordField;
import view.components.RoundedTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Login extends JPanel implements ActionListener {
    
    private RoundedTextField userText; 
    private RoundedPasswordField passwordText;
    private RoundedButton loginBtn;
    private Image backgroundImage = new ImageIcon("C:\\Users\\HP\\Downloads\\background.png").getImage();
    
    public String getUsername() {
        return userText.getText();
    }

    public String getPassword() {
        return new String(passwordText.getPassword());
    }
    
    public RoundedButton getLoginButton() {
        return loginBtn;
    }
    
    public Login(){
        setLayout(new BorderLayout());
        setOpaque(false);
        
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 80));
        wrapper.setOpaque(false);
        
        // Rounded white card
        RoundedPanel card = new RoundedPanel(20, 0);
        card.setPreferredSize(new Dimension(320, 370));
        card.setBorderColor(null);
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel title = new JLabel("Welcome!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(60, 60, 60));
        title.setAlignmentX(LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel subtitle = new JLabel("Log in to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Username label
        JLabel userLabel = createLabel("Username");
        userLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(userLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Username field
        userText = new RoundedTextField(30, 15, 2, 
            new Color(220, 220, 220), 
            new Color(139, 0, 0));
        userText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userText.setBackground(new Color(245, 245, 245));
        userText.setMaximumSize(new Dimension(260, 40));
        userText.setAlignmentX(LEFT_ALIGNMENT);
        card.add(userText);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Password label
        JLabel passLabel = createLabel("Password");
        passLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(passLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Password field
        passwordText = new RoundedPasswordField(30, 15, 2, 
            new Color(220, 220, 220), 
            new Color(139, 0, 0));
        passwordText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordText.setBackground(new Color(245, 245, 245));
        passwordText.setMaximumSize(new Dimension(260, 40));
        passwordText.setAlignmentX(LEFT_ALIGNMENT);
        card.add(passwordText);
        card.add(Box.createVerticalGlue());
        
        // Login button
        loginBtn = new RoundedButton("Login", 20);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setPreferredSize(new Dimension(260, 40));
        loginBtn.setMaximumSize(new Dimension(260, 40));
        loginBtn.setBackground(new Color(139,0,0));
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.addActionListener(this);
        
        card.add(loginBtn);
        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	MainFrame.setNavBarVisible(true);
        MainFrame.addContentPanel(new Landing(), "Landing"); 
        MainFrame.showPanel("Landing", "RoomFindr");
    }
    
    //for background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}