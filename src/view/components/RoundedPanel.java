package view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

@SuppressWarnings("serial")
public class RoundedPanel extends RoundedComponent {
    
    private Color borderColor = Color.WHITE; 
    
    public RoundedPanel(int radius, int borderThickness) {
        super(radius, borderThickness);
        setLayout(new BorderLayout());
    }
    
    public RoundedPanel(int radius, int borderThickness, LayoutManager layout) {
        super(radius, borderThickness);
        setLayout(layout);
    }
    
    public RoundedPanel(int radius, int borderThickness, Color borderColor) {
        super(radius, borderThickness);
        this.borderColor = borderColor;
        setLayout(new BorderLayout());
    }
    
    public RoundedPanel(int radius, int borderThickness, Color borderColor, LayoutManager layout) {
        super(radius, borderThickness);
        this.borderColor = borderColor;
        setLayout(layout);
    }
    
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (getBorderThickness() > 0 && borderColor != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setStroke(new java.awt.BasicStroke(getBorderThickness())); //test line 
            g2.setColor(borderColor);
            g2.drawRoundRect(
                getBorderThickness() / 2, 
                getBorderThickness() / 2, 
                getWidth() - getBorderThickness(), 
                getHeight() - getBorderThickness(), 
                getCornerRadius(), 
                getCornerRadius()
            );
            
            g2.dispose();
        }
    }
}