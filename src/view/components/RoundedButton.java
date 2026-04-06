package view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    
    private int radius, borderThickness;
    private Color borderColor; 
    private Image bgImage;

    public void setBackgroundImage(Image img) {
        this.bgImage = img;
        repaint();
    }

    public void onClick(ActionListener action) {
        addActionListener(action);
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
    public void setBorderThickness(int borderThickness) {
    	this.borderThickness = borderThickness;
    	repaint(); 
    }
    
    public int getBorderThickness() {
    	return borderThickness;
    }
    
    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        this.borderThickness = 0; 

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }
    
    public RoundedButton(String text, int radius, Color borderColor, int borderThickness) {
    	super(text);
    	this.radius = radius;
    	this.borderThickness = borderThickness; 
        this.borderColor = borderColor;

        setOpaque(false); 
        setContentAreaFilled(false); 
        setBorderPainted(false); 
        setBackground(Color.WHITE); //default if the color is not supplied properly
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }
    
    @Override
    // This method is responsible for drawing the button with rounded corners, background image, 
    // and border if specified
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Set the clipping area to a rounded rectangle to ensure all drawing is confined within it
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), radius, radius));
        
        // Draw background image if available, otherwise fill with background color
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }

        g2.dispose();

        super.paintComponent(g);
        
        //only adds border if the borderThickness is greater than 0, should not be negative
        if (borderThickness > 0 && borderColor != null) {

            // Create a separate Graphics2D for the border to avoid affecting the main button rendering
            Graphics2D gBorder = (Graphics2D) g.create();
            gBorder.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Set the color and stroke for the border
            gBorder.setColor(borderColor);
            gBorder.setStroke(new BasicStroke(borderThickness));
            int inset = borderThickness / 2;
            gBorder.drawRoundRect(inset,inset,
                    getWidth() - borderThickness,
                    getHeight() - borderThickness,
                    radius,radius);
            gBorder.dispose();
        }
    }
}