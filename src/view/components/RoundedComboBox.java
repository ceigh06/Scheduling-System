package view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

@SuppressWarnings("serial")
public class RoundedComboBox<E> extends RoundedComponent {

    private JComboBox<E> comboBox;

    public RoundedComboBox(E[] items, int radius, int borderThickness) {
        super(radius, borderThickness);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorderColor(Color.GRAY);

        comboBox = new JComboBox<>(items);
        comboBox.setOpaque(false);
        comboBox.setBorder(new EmptyBorder(5, 10, 5, 10));
        comboBox.setFocusable(false);

        comboBox.setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setBorder(null);
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setOpaque(false);
                return button;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g,
                    Rectangle bounds, boolean hasFocus) {
                // Prevent default background painting
            }
        });

        add(comboBox, BorderLayout.CENTER);

        setPreferredSize(new Dimension(160, 35));
    }

    public JComboBox<E> getComboBox() {
        return comboBox;
    }

    public E getSelectedItem() {
        return (E) comboBox.getSelectedItem();
    }

    public void setSelectedItem(E item) {
        comboBox.setSelectedItem(item);
    }
}