package view.admin;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import model.Room;
import model.schedule.Schedule;
import view.common.ConfirmPanel;
import view.common.MainFrame;
import view.components.RoundedComboBox;
import view.components.RoundedPanel;

@SuppressWarnings("serial")
public class AdminViewSchedule extends JPanel {

    // field components
    RoundedComboBox<String> courseCombo;

    // dynamic label
    JLabel timeOut;

    // time schedule components
    private JPanel spinnerPan;
    private JSpinner hrsSpinner;
    private JSpinner minsSpinner;
    private JComboBox<String> mrdmCombo;
    private JLabel timeinLbl;
    private JPanel timeInPanel;
    private JLabel hLbl;
    private JLabel mLbl;
    private JLabel mrdmLbl;

    private boolean onTimeChangedCallBack = false;

    private ConfirmPanel confirmArea;
    private JLabel roomLbl;
    private JPanel timeSched, form;
    private JScrollPane scrollPanel;
    private GridBagConstraints gbc;
    private JPanel container;

    private boolean[][] occupied = new boolean[28][2];
    
    String[] times = { "7:00 AM", "8:00 AM",
            "9:00 AM", "10:00 AM", "11:00 AM",
            "12:00 PM", "1:00 PM", "2:00 PM",
            "3:00 PM", "4:00 PM", "5:00 PM",
            "6:00 PM", "7:00 PM", "8:00 PM" };

    // Consumer callback for schedule click
    private Consumer<Schedule> onScheduleClicked;

    // Register method for schedule click callback
    public void setOnScheduleClicked(Consumer<Schedule> action) {
        this.onScheduleClicked = action;
    }

    // register of listeners
    public void setOnHourChanged(ChangeListener action) {
        hrsSpinner.addChangeListener(action);
    }

    public void setOnMinuteChanged(ChangeListener action) {
        minsSpinner.addChangeListener(action);
    }

    public void setOnMeridiemChanged(ActionListener action) {
        mrdmCombo.addActionListener(action);
    }

    public void setOnBackClicked(ActionListener action) {
        confirmArea.setBtn1Action(action);
    }

    public void setOnConfirmClicked(ActionListener action) {
        confirmArea.setBtn2Action(action);
    }
    
    public void setTimeOut(String time) {
        timeOut.setText(time);
        repaint();
        revalidate();
    }

    public void loadClassSchedule(Room room) {
        for (Schedule schedule : room.getSchedules()) {
            String formattedTimeIn = formatTime(schedule.getTimeIn());
            String formattedTimeOut = formatTime(schedule.getTimeOut());
            String timeRange = formattedTimeIn + " - " + formattedTimeOut;

            boolean isMasterSchedule = true;
            if (schedule.getStatus().trim().equals("3"))
                isMasterSchedule = false;

            addScheduleBlock(1, timeRange, isMasterSchedule, schedule);
        }
    }

    public AdminViewSchedule(Room room) {
        initializePage(room);

        // Contains the time visible to the users
        loadScheduleLayout();

        container.add(timeSched);
        container.add(Box.createRigidArea(new Dimension(0, 10)));

        // Scroll pane
        scrollPanel = new JScrollPane(container);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        scrollPanel.setBorder(null);

        // Don't call setClick here - schedule panels add their own listeners in addScheduleBlock
        add(scrollPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setPreferredSize(new Dimension(100, 50));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 40, 0, 40));
        southPanel.setBackground(Color.WHITE);

        confirmArea = new ConfirmPanel(MainFrame.getFrame(), "GO BACK", "CONFIRM",
                new Color(227, 75, 75), 2,
                new Color(77, 139, 78), 2);
        confirmArea.setBackground(Color.WHITE);
        southPanel.add(confirmArea.getConfirmPanel(), BorderLayout.CENTER);
        container.add(southPanel, BorderLayout.SOUTH);
    }

    private void createTimeInSection() {
        // Time in label
        timeinLbl = new JLabel("TIME IN");
        timeinLbl.setFont(new Font("Arial", Font.BOLD, 20));

        timeInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeInPanel.setBackground(Color.WHITE);
        timeInPanel.add(timeinLbl);

        // Spinner panel
        spinnerPan = new JPanel(new GridLayout(2, 3, 20, 5));
        spinnerPan.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        spinnerPan.setBackground(Color.WHITE);
        spinnerPan.setMaximumSize(new Dimension(400, 80));

        // Labels
        hLbl = new JLabel("Hour");
        mLbl = new JLabel("Minute");
        mrdmLbl = new JLabel("AM/PM");

        // Spinners
        SpinnerNumberModel hrsModel = new SpinnerNumberModel(7, 1, 12, 1);
        hrsSpinner = new JSpinner(hrsModel);

        SpinnerNumberModel minModel = new SpinnerNumberModel(0, 0, 59, 1);
        minsSpinner = new JSpinner(minModel);

        // AM/PM combo
        String[] meridiem = { "AM", "PM" };
        mrdmCombo = new JComboBox<>(meridiem);

        // Add to panel
        spinnerPan.add(hLbl);
        spinnerPan.add(mLbl);
        spinnerPan.add(mrdmLbl);
        spinnerPan.add(hrsSpinner);
        spinnerPan.add(minsSpinner);
        spinnerPan.add(mrdmCombo);
    }

    // GETTERS FOR VALUES
    public String getTimeInValue() {
        int hour = (Integer) hrsSpinner.getValue();
        int minute = (Integer) minsSpinner.getValue();
        String period = (String) mrdmCombo.getSelectedItem();

        return String.format("%02d:%02d %s", hour, minute, period);
    }

    public int getHour() {
        return (Integer) hrsSpinner.getValue();
    }

    public int getMinute() {
        return (Integer) minsSpinner.getValue();
    }

    public String getMeridiem() {
        return (String) mrdmCombo.getSelectedItem();
    }

    // SETTERS
    public void setTimeIn(int hour, int minute, String meridiem) {
        hrsSpinner.setValue(hour);
        minsSpinner.setValue(minute);
        mrdmCombo.setSelectedItem(meridiem);
    }

    private String formatTime(String sqlTime) {
        String[] parts = sqlTime.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        String suffix = hour >= 12 ? "PM" : "AM";
        int displayHour = hour > 12 ? hour - 12 : hour;
        if (displayHour == 0)
            displayHour = 12;

        String displayMinute = String.format("%02d", minute);

        return displayHour + ":" + displayMinute + " " + suffix;
    }

    void loadScheduleLayout() {
        for (int i = 0; i < times.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i * 2;
            gbc.gridheight = 2;
            gbc.weightx = 0.1;
            gbc.weighty = 2;

            JLabel timeLbl = new JLabel(times[i], SwingConstants.CENTER);
            timeLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            timeLbl.setOpaque(true);
            timeLbl.setBackground(Color.WHITE);
            timeLbl.setPreferredSize(new Dimension(60, 30)); 
            timeSched.add(timeLbl, gbc);
        }

        for (int i = 0; i < times.length * 2; i++) {
            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.gridheight = 1;
            gbc.weightx = 0.9;
            gbc.weighty = 1;

            JPanel emptyCell = new JPanel();
            emptyCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            emptyCell.setBackground(Color.WHITE);
            emptyCell.setPreferredSize(new Dimension(200, 30));

            timeSched.add(emptyCell, gbc);
        }
    }

    void initializePage(Room room) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.setBorder(BorderFactory.createEmptyBorder(20, -10, 20, 0));
        container.setBackground(Color.WHITE);

        RoundedPanel labelPanel = new RoundedPanel(25, 0, new BorderLayout());
        roomLbl = new JLabel(room.getRoomCode(), SwingConstants.CENTER);
        roomLbl.setForeground(Color.WHITE);
        roomLbl.setFont(new Font("Arial", Font.BOLD, 16));
        roomLbl.setOpaque(false);

        labelPanel.setBackground(new Color(139, 0, 0));
        labelPanel.setPreferredSize(new Dimension(200, 50));
        labelPanel.setMaximumSize(new Dimension(200, 50));
        labelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPanel.add(roomLbl, BorderLayout.CENTER);

        container.add(labelPanel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));

        timeSched = new JPanel(new GridBagLayout());
        timeSched.setBackground(Color.WHITE);
        timeSched.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        timeSched.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        timeSched.setAlignmentX(Component.CENTER_ALIGNMENT);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
    }

    // Only schedule panels (yellow/gray) are clickable - empty cells are not
    public void addScheduleBlock(int column, String timeRange, boolean schedType, Schedule schedule) {
        System.out.println("Adding block: " + timeRange);
        String[] times = timeRange.split(" - ");
        System.out.println("Start: '" + times[0] + "', End: '" + times[1] + "'");
        int startRow = getRowFromTime(timeRange.split(" - ")[0]);
        int rowSpan = getTimeSpan(timeRange);

        // Remove overlapping components
        for (Component comp : timeSched.getComponents()) {
            GridBagConstraints c = ((GridBagLayout) timeSched.getLayout()).getConstraints(comp);
            if (c.gridx == column && c.gridy >= startRow && c.gridy < startRow + rowSpan) {
                if (comp instanceof JPanel) {
                    ((JPanel) comp).setBorder(null);
                }
            }
        }

        gbc.gridx = column;
        gbc.gridy = startRow;
        gbc.gridheight = rowSpan;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel schedPanel = new JPanel();
        schedPanel.setLayout(new BoxLayout(schedPanel, BoxLayout.Y_AXIS));
        schedPanel.setBackground(schedType ? Color.YELLOW : new Color(190, 190, 190));
        if (!schedType)
            schedPanel.setForeground(Color.WHITE);

        schedPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Add schedule info
        schedPanel.add(new JLabel(schedule.getSectionKey()));
        schedPanel.add(new JLabel(schedule.getCourseCode()));
        schedPanel.add(new JLabel(schedule.getFacultyID()));
        schedPanel.add(new JLabel(timeRange));

        // Only schedule panels (yellow/gray) get click listener - empty cells don't have this
        schedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        schedPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (onScheduleClicked != null) {
                    onScheduleClicked.accept(schedule);
                }
            }
        });

        timeSched.add(schedPanel, gbc);
        timeSched.setComponentZOrder(schedPanel, 0);

        markOccupied(column, startRow, rowSpan);
        timeSched.revalidate();
        timeSched.repaint();
    }

    public int getRowFromTime(String time) {
        String[] parts = time.split(" ");
        String[] hourMin = parts[0].split(":");

        int hour = Integer.parseInt(hourMin[0]);
        int minute = Integer.parseInt(hourMin[1]);
        String period = parts[1];

        if (period.equals("PM") && hour != 12)
            hour += 12;
        if (period.equals("AM") && hour == 12)
            hour = 0;

        int startHour = 7;
        int totalMinutes = ((hour - startHour) * 60) + minute;
        return totalMinutes / 30;
    }

    public int getTimeSpan(String timeRange) {
        String[] times = timeRange.split(" - ");
        int start = getRowFromTime(times[0]);
        int end = getRowFromTime(times[1]);
        return end - start;
    }

    public void markOccupied(int col, int startRow, int rowSpan) {
        for (int i = startRow; i < startRow + rowSpan; i++) {
            occupied[i][col] = true;
        }
    }

    // REMOVED: No longer needed - only schedule panels are clickable
    // Empty cells (white) don't have click listeners
}