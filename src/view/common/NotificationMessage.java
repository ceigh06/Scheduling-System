package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.landing.Landing;

@SuppressWarnings("serial")
public class NotificationMessage extends JPanel {
    
    private JLabel iconLabel, messageLabel;
    
    /*
     * iconPath  Path to icon image (null for default checkmark)
     * message   Main message to display
     * backAction What to do when clicked
     * 
     * use this frame in an switch statement to change supply needed per status
     */
    public NotificationMessage(String iconPath, String message) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(120, 20, 180, 20));
        
        /*
         * MEMBER TO DO: supply the icon path here to get the image
         * BACKEND TO DO: Way to make the schedule control notification 
         * dynamic for both regular and irregular student 
         */
        
        iconLabel = new JLabel(grabIcon(iconPath), SwingConstants.CENTER);
        content.add(iconLabel, BorderLayout.NORTH);
        
        /* 
         * BACKEND TO DO: Supply the message depending on the status of the request 
         * this can also be used for other frames, like control notifications
         */
        messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(Color.DARK_GRAY);
        content.add(messageLabel, BorderLayout.CENTER);
        
        //So users have an idea that they can go back by clicking
        JLabel hint = new JLabel("Click anywhere to go back", SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.ITALIC, 14));
        hint.setForeground(Color.GRAY);
        content.add(hint, BorderLayout.SOUTH);
        
        add(content, BorderLayout.CENTER);
        
        // Click anywhere to go back
        addMouseListener(new ClickListener());
        content.addMouseListener(new ClickListener());
    }
    
    private ImageIcon grabIcon(String path) {
        if (path != null) {
            try {
                Image scaled = new ImageIcon(path).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            } catch (Exception e) {
            	System.err.println("ERROR: Image cannot be found."); 
            }
        }
        return null; 
    }
    
    private class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            MainFrame.addContentPanel(new Landing(), "Landing");
            MainFrame.showPanel("Landing","RoomFindr");
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    // Quick setters for reuse
    public void setIcon(String path) {
        iconLabel.setIcon(grabIcon(path));
    }
    
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}