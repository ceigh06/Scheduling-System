package view.components;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class RoundedTextField extends JTextField {
    
    private int cornerRadius;
    private Color borderColor;
    private Color focusColor;
    private int borderThickness;
    private boolean isFocused = false;
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
    
    public void setFocusColor(Color color) {
        this.focusColor = color;
        repaint();
    }
    
    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }
    
    public int getCornerRadius() {
    	return cornerRadius; 
    }
    
    public Color getBorderColor() {
    	return borderColor; 
    }
    
    public Color getFocusColor() {
    	return focusColor; 
    }
    
    public int getBorderThickness() {
    	return borderThickness; 
    }
    
    public RoundedTextField(int columns, int radius, int borderThickness,
                           Color borderColor, Color focusColor) {
        super(columns);
        this.cornerRadius = radius;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        this.focusColor = focusColor;
        
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        
        //this is for the red borders when the text fields are clicked
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, 
                getHeight() - 1, cornerRadius, cornerRadius));
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);


        //to switch the color of border color when unfocused and focused
        g2.setColor(isFocused ? focusColor : borderColor);

        g2.setStroke(new BasicStroke(borderThickness));
        g2.draw(new RoundRectangle2D.Float(
                borderThickness / 2f,
                borderThickness / 2f,
                getWidth() - borderThickness,
                getHeight() - borderThickness,
                cornerRadius,
                cornerRadius
        ));

        g2.dispose();
    }
}