package view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class RoundedLabel extends JLabel {
    private Image image;
    private int borderThickness = 0;
    private Color borderColor;
    private String text;
    private JLabel innerLabel;

    public RoundedLabel(ImageIcon icon, int borderThickness, Color borderColor, int dimension) {
        this.image = icon.getImage();
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        setPreferredSize(new Dimension(dimension, dimension));
        setOpaque(false); // CRITICAL: prevents background fill from covering image
    }

    public RoundedLabel(JLabel label, int borderThickness, Color borderColor, int dimension) {
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        setPreferredSize(new Dimension(dimension, dimension));
        setOpaque(false); 

        if(label != null && label.getText() != null) {
        	this.text = label.getText();
            setHorizontalAlignment(label.getHorizontalAlignment()); 
        }
    }

    public RoundedLabel(String text,  int borderThickness, Color borderColor, int dimension) {
        this.text = text;
        // this.setBackground(bgColor);
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        setPreferredSize(new Dimension(dimension, dimension));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int diameter = Math.min(getWidth(), getHeight());
        
        // Center the circle in the component
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create circular clip
        Ellipse2D circle = new Ellipse2D.Float(x, y, diameter, diameter);
        g2.clip(circle);

        //lab rat 
        if(getBackground() != null && getBackground().getAlpha() > 0) {
        	g2.setColor(getBackground());
        	g2.fill(circle);
        }

        // drawing of image on top of background color
        if(image != null) {
            g2.drawImage(image, x, y, diameter, diameter, this);
        }
        
        String displayText = text; 
        if(displayText == null && innerLabel != null){
            displayText = innerLabel.getText(); 
        }

        if(displayText != null && !displayText.isEmpty()) {
            g2.setColor(getForeground() != null? getForeground() : Color.BLACK); 

            if(innerLabel != null && innerLabel.getFont() != null){
                g2.setFont(innerLabel.getFont());
            } else if(getFont() != null){
                g2.setFont(getFont());
            }
            
            FontMetrics fm = g2.getFontMetrics(); 
            int textWidth = fm.stringWidth(displayText);
            int textHeight = fm.getAscent();
            int textX = x + (diameter - textWidth) / 2;
            int textY = y + (diameter + textHeight) / 2 - 3; // Adjust for better vertical centering
            g2.drawString(displayText, textX, textY);
        }

        g2.setClip(null); 

        // Draw border
        if (borderThickness > 0 && borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            int offset = borderThickness / 2;
            g2.drawOval(x + offset, y + offset, diameter - borderThickness, diameter - borderThickness);
        }

        g2.dispose();
    }
}