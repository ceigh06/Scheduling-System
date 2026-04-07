package view.landing;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.Room;
import model.user.User;
import view.components.RoundedButton;
import view.components.RoundedPanel;
import view.components.RoundedTextField;
import view.components.ScrollBarHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.function.Consumer;
import java.awt.event.ActionListener;

public class Landing extends JPanel {

    private final Color OFF_WHITE = new Color(250, 249, 246);
    private RoundedTextField searchBar;
    private ActionListener onSearchAction;
    private ActionListener onRoomViewAction;
    private JPanel contentPanel;

    private Consumer<Room> onRoomClicked;

    User user;

    public void setOnRoomClicked(Consumer<Room> action) {
        this.onRoomClicked = action;
    }

    public void setOnRoomViewAction(ActionListener action) {
        this.onRoomViewAction = action;
    }

    public void setOnSearchAction(MouseAdapter action) {
        searchBar.addMouseListener(action);
    }

    public Landing(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(OFF_WHITE);

    }

    public void loadLandingContent() {
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JScrollPane createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(OFF_WHITE);
        contentPanel.add(createSearchSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JScrollPane mainScroll = new JScrollPane(contentPanel);
        ScrollBarHelper.applySlimScrollBar(mainScroll, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        mainScroll.setBorder(null);
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

        // BACKEND TO DO: If possible I want this to display
        // the name (only first name) of the user

        // if not possible: RESO ->
        // JLabel title = new JLabel("The best rooms are just a click away");
        JLabel title = new JLabel("Welcome back, " + user.getFirstName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setAlignmentX(LEFT_ALIGNMENT);
        wrapper.add(Box.createRigidArea(new Dimension(0, 2)));

        JLabel subtitle = new JLabel("The best rooms are just a click away");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(subtitle, BorderLayout.SOUTH);
        wrapper.add(Box.createRigidArea(new Dimension(0, 15)));

        searchBar = new RoundedTextField(20, 25, 2,
                new Color(220, 220, 220),
                new Color(117, 144, 156));

        searchBar.setMaximumSize(new Dimension(400, 45));
        searchBar.setPreferredSize(new Dimension(400, 45));
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

        // BACKEND TO DO:
        searchBar.addActionListener(e -> {
            if (onSearchAction != null) {
                onSearchAction.actionPerformed(e);
            }
        });

        wrapper.add(searchBar);
        searchPanel.add(wrapper, BorderLayout.CENTER);
        return searchPanel;
    }

    public void loadRooms(String title, List<Room> rooms) {
        contentPanel.add(createRoomSection(title, rooms));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JPanel createRoomSection(String title, List<Room> rooms) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setOpaque(false);
        sectionPanel.setBackground(OFF_WHITE);

        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
        sectionTitle.setBorder(new EmptyBorder(0, 10, 10, 0));

        sectionPanel.add(sectionTitle, BorderLayout.NORTH);

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.X_AXIS));
        cardsContainer.setOpaque(false);
        cardsContainer.setBackground(OFF_WHITE);
        cardsContainer.setBorder(new EmptyBorder(5, 15, 5, 30));
        // sample only to show the interface
        for (Room room : rooms) {
            /*
             * BACKEND TO DO: Make an arrayList maybe or SQL query that will get the room
             * names
             * for the reported rooms with [most viewed : most available today]
             */
            cardsContainer.add(createRoomCard(room.getBuildingCode() + " - " + room.getRoomCode(), room));
            cardsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
        }

        JScrollPane scrollPane = new JScrollPane(
                cardsContainer,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        ScrollBarHelper.applySlimScrollBar(scrollPane, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        scrollPane.setBorder(null);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(450, 200)); // test line
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(OFF_WHITE);

        sectionPanel.add(scrollPane, BorderLayout.CENTER);
        return sectionPanel;
    }

    private RoundedPanel createRoomCard(String roomName, Room room) {
        RoundedPanel roomCard = new RoundedPanel(25, 2, Color.LIGHT_GRAY, new BorderLayout());
        roomCard.setPreferredSize(new Dimension(170, 170));
        roomCard.setMaximumSize(new Dimension(170, 170));
        roomCard.setBackground(Color.WHITE);
        roomCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel nameLabel = new JLabel(roomName, JLabel.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(91, 112, 121));

        RoundedButton viewBtn = new RoundedButton("View Room", 20);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setBackground(new Color(91, 112, 121));
        viewBtn.setPreferredSize(new Dimension(120, 35));

        viewBtn.addActionListener(e -> {
            onRoomClicked.accept(room);
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