package view.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ItemSelectable;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class RoundedToggleSwitch extends RoundedComponent implements ItemSelectable{
	
	
	private boolean selected = false;
    private String leftText = "Monthly";
    private String rightText = "Weekly";

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean state) {
        selected = state;
        repaint();
    }

    public void addItemListener(ItemListener listener) {
        listeners.add(listener);
    }
    
    public void setToggleTexts(String left, String right) {
        this.leftText = left;
        this.rightText = right;
        repaint();
    }

    private List<ItemListener> listeners = new ArrayList<>();

    public RoundedToggleSwitch(int radius, int border) {
        super(radius, border);

        setBackground(Color.WHITE);
        setBorderColor(new Color(117, 144, 156));
        setPreferredSize(new Dimension(250,50));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selected = !selected;

                int state = selected ? ItemEvent.SELECTED : ItemEvent.DESELECTED;

                ItemEvent event = new ItemEvent(RoundedToggleSwitch.this,
                    ItemEvent.ITEM_STATE_CHANGED,this,state);

                for (ItemListener l : listeners) {
                    l.itemStateChanged(event);
                }

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int half = w / 2;
        g2.setColor(new Color(117, 144, 156));

        if(selected) {
            g2.fillRoundRect(half, 0, half, h, getCornerRadius(), getCornerRadius());
        } else {
            g2.fillRoundRect(0, 0, half, h, getCornerRadius(), getCornerRadius());
        }

        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();

        int y = (h + fm.getAscent()) / 2 - 3;

        int leftX = (half - fm.stringWidth(leftText)) / 2;
        int rightX = half + (half - fm.stringWidth(rightText)) / 2;

        if(selected) {
            g2.setColor(new Color(117, 144, 156));
            g2.drawString(leftText, leftX, y);

            g2.setColor(Color.WHITE);
            g2.drawString(rightText, rightX, y);
        } else {
            g2.setColor(Color.WHITE);
            g2.drawString(leftText, leftX, y);

            g2.setColor(new Color(117, 144, 156));
            g2.drawString(rightText, rightX, y);
        }

        g2.dispose();
    }

    @Override
    public Object[] getSelectedObjects() {
        if (selected) {
            return new Object[] {
                rightText 
            };
        } else {
            return new Object[] {
                leftText 
            };
        }
    }

    // Not needed for this implementation
    @Override
    public void removeItemListener(ItemListener l) {}
}
