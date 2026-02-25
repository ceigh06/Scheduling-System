package view;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login implements ActionListener {
	JFrame frame; 
    JLabel title, userLabel, passwordLabel; 
    JTextField userText;
    JPasswordField passwordText;
    JButton loginButton;
    static int valid = 0; 
    
    Login(){
    	frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setLayout(new BorderLayout()); 
        frame.setLocationRelativeTo(null); //center the frame on the screen
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(128,0,0));
        header.setPreferredSize(new java.awt.Dimension(0,50)); //width is managed by layout

        //JLabel is like "Text" option for Swing
        title = new JLabel("RoomFindr", JLabel.CENTER); 
        title.setForeground(Color.WHITE); //text color 
        title.setFont(new Font("Georgia", Font.BOLD, 26)); 
        header.add(title, BorderLayout.CENTER); // Add title to header panel == container in the center 
        frame.add(header,BorderLayout.NORTH); //positions the header panel at the top of the frame 

        // Create a panel for login form
        JPanel panel = new JPanel(); 
        panel.setLayout(new GridLayout(6,1,10,10));
        panel.setPreferredSize(new Dimension(340,400));
        panel.setBorder(BorderFactory.createEmptyBorder(140, 0, 10, 0));

        userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        userLabel.setForeground(new Color(42, 42, 42));
        panel.add(userLabel);

        userText = new JTextField(30);
        userText.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userText.setBackground(new Color(217,217,217));
        panel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        passwordLabel.setForeground(new Color(42, 42, 42));
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBackground(new Color(217,217,217));
        panel.add(passwordText);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(panel);
        frame.add(wrapper, BorderLayout.CENTER);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        loginButton.setBackground(new Color(139,0,0)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        loginButton.addActionListener(this);

        panel.add(loginButton, BorderLayout.SOUTH);

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.add(loginButton);
        panel.add(buttonWrapper); 
        frame.setVisible(true);
        frame.setBackground(Color.WHITE);
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		Login.valid = 0; 
        if(e.getSource() == loginButton) {
            // In a real application, you would validate the username and password against a database or other data source.
            // tao lang ako kaya nalilito lang ako
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

            if(!username.isEmpty() && !password.isEmpty()) {
                Login.valid = 1; 
                System.out.println("Login successful!");
                frame.dispose();
            }
            
        }
		
	}
	public static void main(String[] args) {
		new Login();
	}

	
}