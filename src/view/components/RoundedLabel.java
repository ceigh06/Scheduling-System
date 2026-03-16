package view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RoundedLabel extends JLabel {
	//mostly for pfp pa lang ito, pwede pa mabago parameters lng baguhin of other labels na rin

    private Image image;
    private int borderThickness = 0;
    private int dimension = 0;;
    private Color borderColor = Color.BLACK;

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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        int diameter = Math.min(getWidth(), getHeight());

        // Clip image to circle
        g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(image, 0, 0, diameter, diameter, this);

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