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
	//mostly for pfp pa lang ito, pwede pa mabago parameters lng baguhin of other labels na rin

    private Image image;
    private int borderThickness = 0;
    private int dimension = 0;
    private Color borderColor;
    private JLabel label;
    

    public RoundedLabel(ImageIcon icon, int dimension) {
    	this.dimension = dimension;
        this.image = icon.getImage();
        setPreferredSize(new Dimension(dimension, dimension));
    }

    public RoundedLabel(ImageIcon icon, int borderThickness, Color borderColor, int dimension) {
    	this.dimension = dimension;
        this.image = icon.getImage();
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        setPreferredSize(new Dimension(dimension, dimension));
    }
    public RoundedLabel(JLabel label, int borderThickness, Color borderColor, int dimension) {
    	this.dimension = dimension;
        this.label = label;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        setPreferredSize(new Dimension(dimension, dimension));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int diameter = Math.min(getWidth(), getHeight());
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // To set BACKGROUND COLOR HERE
        g2.setColor(getBackground());
        //how rounded the label if text
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 100, 100);

        // Clip to circle
        g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));

        if (image != null) {
            // Draw image clipped to circle
            g2.drawImage(image, 0, 0, diameter, diameter, this);
        } else if (label != null) {
            // Draw label text centered inside the circle
        	label.setSize(diameter, diameter);
        	label.setHorizontalAlignment(SwingConstants.CENTER);
        	label.setVerticalAlignment(SwingConstants.CENTER);
        	label.paint(g2);
        }

        // Reset clip before drawing border
        g2.setClip(null);
        drawBorder(g2, diameter);

        g2.dispose();
    }


    //Helper method for drawing border and color to the img
    private void drawBorder(Graphics2D g2, int diameter) {
        if (borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.drawOval(
                borderThickness / 2,
                borderThickness / 2,
                diameter - borderThickness,
                diameter - borderThickness
            );
        }
    }
}