package view.landing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import view.components.RoundedButton;
import view.components.RoundedPanel;
import view.components.RoundedTextField;

@SuppressWarnings("serial")
public class AdminLanding extends JPanel {

    private final Color OFF_WHITE = new Color(250, 249, 246);
    private RoundedTextField searchBar;
    private ActionListener onSearchAction;
    private ActionListener onRoomViewAction;

    public void setOnRoomViewAction(ActionListener action) {
        this.onRoomViewAction = action;
    }

    public void setOnSearchAction(MouseAdapter action) {
        searchBar.addMouseListener(action);
    }

    public AdminLanding() {
        setLayout(new BorderLayout());
        setBackground(OFF_WHITE);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JScrollPane createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(OFF_WHITE);
        contentPanel.add(createSearchSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        contentPanel.add(createReportSection("   Total Room Requests        Most Requested Room",2, 187, 187));

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(createReportSection("Most Vacancies Today",1, 387, 187));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 2)));

        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setBorder(BorderFactory.createEmptyBorder());
        mainScroll.getViewport().setBackground(OFF_WHITE);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return mainScroll;
    }

    private RoundedPanel createSearchSection() {
        RoundedPanel searchPanel = new RoundedPanel(0, 1, new BorderLayout());
        searchPanel.setBackground(OFF_WHITE);
        searchPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setBackground(OFF_WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome back, ADMIN!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setAlignmentX(LEFT_ALIGNMENT);
        wrapper.add(Box.createRigidArea(new Dimension(0, 2)));

        JLabel subtitle = new JLabel("Search Rooms");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(subtitle, BorderLayout.SOUTH);
        wrapper.add(Box.createRigidArea(new Dimension(0, 15)));

        searchBar = new RoundedTextField(20, 25, 2,
                new Color(220, 220, 220),
                new Color(170, 20, 20)
        );

        searchBar.setMaximumSize(new Dimension(440, 45));
        searchBar.setPreferredSize(new Dimension(440, 45));
        searchBar.setBackground(Color.WHITE);
        searchBar.setAlignmentX(LEFT_ALIGNMENT);
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        String hintText = "Browse rooms by time...";
        searchBar.setText(hintText);
        searchBar.setForeground(Color.GRAY);

        searchBar.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals(hintText)) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText(hintText);
                    searchBar.setForeground(Color.GRAY);
                }
            }
        });

        //BACKEND TO DO: 
        searchBar.addActionListener(e -> {
            if (onSearchAction != null) {
                onSearchAction.actionPerformed(e);
            }
        });

        wrapper.add(searchBar);
        searchPanel.add(wrapper, BorderLayout.CENTER);
        return searchPanel;
    }

    private JPanel createReportSection(String title, int cardCount, int width, int height) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setOpaque(false);
        sectionPanel.setBackground(OFF_WHITE);

        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setBorder(new EmptyBorder(0, 10, 10, 0));

        sectionPanel.add(sectionTitle, BorderLayout.NORTH);

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.X_AXIS));
        cardsContainer.setOpaque(false);
        cardsContainer.setBackground(OFF_WHITE);
        cardsContainer.setBorder(new EmptyBorder(5, 15, 5, 30));


        for(int i=0;i<cardCount;i++){
        cardsContainer.add(createCard("Sample Report", width, height));
        cardsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
        }
        

        sectionPanel.add(cardsContainer, BorderLayout.CENTER);

        return sectionPanel;
    }

    private RoundedPanel createCard(String roomName, int width, int height) {
        RoundedPanel roomCard = new RoundedPanel(25, 2, Color.LIGHT_GRAY, new BorderLayout());
        roomCard.setPreferredSize(new Dimension(width, height));
        roomCard.setMaximumSize(new Dimension(width, height));
        roomCard.setBackground(Color.WHITE);
        roomCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel nameLabel = new JLabel(roomName, JLabel.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(139, 0, 0));

        RoundedButton viewBtn = new RoundedButton("View Report", 20);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setBackground(new Color(139, 0, 0));
        viewBtn.setPreferredSize(new Dimension(120, 35));

        viewBtn.addActionListener(e -> {
            if (onRoomViewAction != null) {
                onRoomViewAction.actionPerformed(e);
            }
        });

        roomCard.add(nameLabel, BorderLayout.CENTER);
        roomCard.add(viewBtn, BorderLayout.SOUTH);
        return roomCard;
    }

    public String getSearchText() {
        String text = searchBar.getText();
        return text.equals("Browse rooms by time...") ? "" : text;
    }

    public void clearSearch() {
        searchBar.setText("Browse rooms by time...");
        searchBar.setForeground(Color.GRAY);
    }
}
