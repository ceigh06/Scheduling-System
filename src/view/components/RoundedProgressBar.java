package view.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JProgressBar;

public class RoundedProgressBar extends JProgressBar{
	
	private int cornerRadius = 10;

    public RoundedProgressBar(int min, int max) {
        super(min, max);
        setOpaque(false);
        setBorderPainted(false);
        setStringPainted(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = 10;

        // Background
        g2.setColor(new Color(220, 220, 220)); // light gray background
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

        // Progress fill
        int progressWidth = (int) ((getPercentComplete()) * width);
        g2.setColor(new Color(91, 112 ,121)); // progressbar fill
        g2.fillRoundRect(0, 0, progressWidth, height, cornerRadius, cornerRadius);

        g2.dispose();
    }


}