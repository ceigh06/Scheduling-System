package view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class RoundedComponent extends JComponent {
    
    private int cornerRadius;
    private int borderThickness;
    private Color borderColor;
    
    public RoundedComponent(int cornerRadius, int borderThickness) {
        this.cornerRadius = cornerRadius;
        this.borderThickness = borderThickness;
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        int x = borderThickness / 2;
        int y = borderThickness / 2;
        int w = getWidth() - borderThickness;
        int h = getHeight() - borderThickness;
        
        // Fill background
        if (getBackground() != null) {
            g2.setColor(getBackground());
            g2.fillRoundRect(x, y, w, h, cornerRadius, cornerRadius);
        }
        
        // Draw border
        if (borderColor != null && borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.drawRoundRect(x, y, w, h, cornerRadius, cornerRadius);
        }
        
        g2.dispose();
    }
    
    public void setBorderColor(Color color) {
        borderColor = color;
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        cornerRadius = radius;
        repaint();
    }
    
    public void setBorderThickness(int thickness) {
        borderThickness = thickness;
        repaint();
    }
    
    public Color getBorderColor() {
    	return borderColor; 
    }
    
    public int getCornerRadius() {
    	return cornerRadius; 
    }
    
    public int getBorderThickness() {
    	return borderThickness; 
    }
}