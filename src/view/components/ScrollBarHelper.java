package view.components;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ScrollBarHelper {

    // Apply custom slim scrollbars to any JScrollPane
    public static void applySlimScrollBar(JScrollPane scrollPane, int thickness, int arcSize, Color thumbColor, Color trackColor) {
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(thickness, arcSize, thumbColor, trackColor));
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI(thickness, arcSize, thumbColor, trackColor));
    }

    // Inner class for the custom UI
    private static class CustomScrollBarUI extends BasicScrollBarUI {
        private final int thickness;
        private final int arcSize;
        private final Color thumbColor;
        private final Color trackColor;

        public CustomScrollBarUI(int thickness, int arcSize, Color thumbColor, Color trackColor) {
            this.thickness = thickness;
            this.arcSize = arcSize;
            this.thumbColor = thumbColor;
            this.trackColor = trackColor;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, arcSize, arcSize);

            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }

        @Override
        protected Dimension getMinimumThumbSize() {
            return new Dimension(thickness, thickness);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setVisible(false);
            return button;
        }

        @Override
        protected void configureScrollBarColors() {
            scrollbar.setPreferredSize(new Dimension(thickness, thickness));
        }
    }
}
