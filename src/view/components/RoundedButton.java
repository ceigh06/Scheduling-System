package view.components;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class RoundedButton extends JButton {
    
    private int radius, borderThickness;
    private Color borderColor; 

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
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        //only adds border if the borderThickness is greater than 0, should not be negative
        if (borderThickness > 0 && borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            int inset = borderThickness / 2;
            g2.drawRoundRect(inset, inset, 
                    getWidth() - borderThickness, 
                    getHeight() - borderThickness, 
                    radius, radius);
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
}