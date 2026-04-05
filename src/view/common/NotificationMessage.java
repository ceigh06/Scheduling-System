package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.landing.Landing;

public class NotificationMessage extends JPanel {
    
    private JLabel iconLabel, messageLabel;
    
    public NotificationMessage(String iconPath, String message) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Use BoxLayout.Y_AXIS for vertical stacking
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));
        
        // Icon (centered)
        iconLabel = new JLabel(grabIcon(iconPath), SwingConstants.CENTER);
        iconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        content.add(Box.createVerticalStrut(120));
        content.add(iconLabel);
        
        // Spacer between icon and message
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Message (centered)
        messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(new Color(91, 112, 121));
        messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        content.add(messageLabel);
        
        // Spacer between message and hint
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Hint (centered)
        JLabel hint = new JLabel("Click anywhere to go back", SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.ITALIC, 14));
        hint.setForeground(new Color(117, 144, 156));
        hint.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        content.add(hint);
        
        content.add(Box.createVerticalGlue());
        
        add(content, BorderLayout.CENTER);
        
        addMouseListener(new ClickListener());
        content.addMouseListener(new ClickListener());
    }
    
    private ImageIcon grabIcon(String path) {
        if (path != null && !path.isEmpty()) {
            try {
                java.net.URL imgUrl = getClass().getResource(path);
                if (imgUrl != null) {
                    Image scaled = new ImageIcon(imgUrl).getImage()
                        .getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                } else {
                    System.err.println("ERROR: Image not found: " + path);
                }
            } catch (Exception e) {
                System.err.println("ERROR: Failed to load: " + path);
                e.printStackTrace();
            }
        }
        return null; 
    }
    
    private class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            MainFrame.addContentPanel(new Landing(), "Landing");
            MainFrame.showPanel("Landing", "RoomFindr");
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
    
    public void setIcon(String path) {
        iconLabel.setIcon(grabIcon(path));
    }
    
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}