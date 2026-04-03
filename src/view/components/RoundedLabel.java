package view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RoundedLabel extends JLabel {
    private Image image;
    private int borderThickness = 0;
    private int dimension = 0;
    private Color borderColor;
    private String text;
    private JLabel label;
    private Color bgColor = new Color(255, 255, 255, 0); // Transparent background

    public RoundedLabel(ImageIcon icon, int borderThickness, Color borderColor, int dimension) {
        this.dimension = dimension;
        this.image = icon.getImage();
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        setPreferredSize(new Dimension(dimension, dimension));
        setOpaque(false); // CRITICAL: prevents background fill from covering image
    }

    public RoundedLabel(JLabel label, int borderThickness, Color borderColor, int dimension) {
    	this.dimension = dimension;
        this.label = label;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        setPreferredSize(new Dimension(dimension, dimension));
    }

    public RoundedLabel(String text,  int borderThickness, Color borderColor, int dimension) {
        this.text = text;
        this.setBackground(bgColor);
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        this.dimension = dimension;
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

        // Draw image centered and scaled to fill the circle
        if (image != null) {
            g2.drawImage(image, x, y, diameter, diameter, this);
        }

        // Remove clip for border
        g2.setClip(null);
        
        // Draw border
        if (borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            int offset = borderThickness / 2;
            g2.drawOval(x + offset, y + offset, 
                       diameter - borderThickness, diameter - borderThickness);
        }

        g2.dispose();
    }
}