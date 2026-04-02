package view.common;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import model.Course;
import model.Room;
import model.schedule.Schedule;
import view.components.RoundedButton;
import view.components.RoundedComboBox;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class ViewSchedule extends JPanel {

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

    // lab or lec
    RoundedButton lecBtn;
    RoundedButton labBtn;
    boolean isLec = true;

    private boolean onTimeChangedCallBack = false;

    private ConfirmPanel confirmArea;
    private JLabel roomLbl;
    private JPanel timeSched, form;
    private JScrollPane scrollPanel;
    private GridBagConstraints gbc;
    private JPanel container;

    private boolean[][] occupied = new boolean[28][2];
    // BACKEND TO DO: checker if the time has overlapping schedule 'markOccupied()'
    String[] times = { "7:00 AM", "8:00 AM",
            "9:00 AM", "10:00 AM", "11:00 AM",
            "12:00 PM", "1:00 PM", "2:00 PM",
            "3:00 PM", "4:00 PM", "5:00 PM",
            "6:00 PM", "7:00 PM", "8:00 PM" };

    // register of listeners
    public void setOnLecBtn(ActionListener action) {
        lecBtn.addActionListener(action);
    }

    public void setOnLabBtn(ActionListener action) {
        labBtn.addActionListener(action);
    }

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

    public void setIsLec(boolean isLec) {
        this.isLec = isLec;
    }

    public boolean getIsLec() {
        return isLec;
    }

    public void setTimeOut(String time) {
        timeOut.setText(time);
        repaint();
        revalidate();
    }

    public void loadCourse(List<Course> courses) {
        courseCombo.getComboBox().removeAllItems();
        for (Course course : courses) {
            courseCombo.getComboBox().addItem(course.toString());
        }
        courseCombo.setBackground(Color.WHITE);
        courseCombo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        courseCombo.setMaximumSize(new Dimension(400, 50));
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

    public ViewSchedule(Room room) {
        initializePage(room);

        // Contains the time visible to the users
        // FRONTEND TO BACKEND: No need to make this database connected
        loadScheduleLayout();

        // creates space for the schedule panels to be added to timeSched
        // RFONTEND TO BACKEND: No need to make this database connected

        // BACKEND TO DO: No need to create this manually,
        // make it so that the checkers are working

        container.add(timeSched);
        container.add(Box.createRigidArea(new Dimension(0, 10)));

        lecBtn = new RoundedButton("LECTURE SCHEDULE", 20, new Color(91, 112 ,121), 2);
        labBtn = new RoundedButton("LABORATORY SCHEDULE", 20, new Color(91, 112 ,121), 2);

        // Scroll pane 
        scrollPanel = new JScrollPane(container);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // test line
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        scrollPanel.setBorder(null);

        // false: view only
        // true: view and click
        setClick(false); // enables clicking of panels (ONLY)
        // BACKEND TO DO: direct to room schedule to modify the room schedule
        add(scrollPanel, BorderLayout.CENTER);

        

        
    }

    public void loadConfirmationPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setPreferredSize(new Dimension(100, 50));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 40, 0, 40));
        southPanel.setBackground(Color.WHITE);
        confirmArea = new ConfirmPanel(MainFrame.getFrame(),
                "GO BACK", "CONFIRM",
                new Color(227, 75, 75), 2,
                new Color(77, 139, 78), 2);
        confirmArea.setBtn1Color(new Color(255, 100, 100));
        confirmArea.setBtn2Color(new Color(63, 193, 127));
        confirmArea.setBackground(Color.WHITE);
        southPanel.add(confirmArea.getConfirmPanel(), BorderLayout.CENTER);
        container.add(southPanel, BorderLayout.SOUTH);
    }

    public void loadFormPanel(){
        // container panel of the forms below
        form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        form.setBackground(Color.WHITE);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.setMaximumSize(new Dimension(430, Integer.MAX_VALUE));

        // Course selection
        JLabel selectCourseLbl = new JLabel("SELECT COURSE");
        selectCourseLbl.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel selectCoursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectCoursePanel.setBackground(Color.WHITE);
        selectCoursePanel.add(selectCourseLbl);

        // Lecture/Lab buttons
        JPanel unitBtnPanel = new JPanel();
        unitBtnPanel.setLayout(new BoxLayout(unitBtnPanel, BoxLayout.X_AXIS));
        unitBtnPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        unitBtnPanel.setBackground(Color.WHITE);
        unitBtnPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // test line

        // BACKEND TO DO: Make functionalities for lecBtn and labBtn
        
        lecBtn.setPreferredSize(new Dimension(180, 60));
        lecBtn.setMaximumSize(new Dimension(180, 60));
        lecBtn.setForeground(Color.WHITE);
        lecBtn.setBackground(new Color(117, 144, 156));
        
        labBtn.setPreferredSize(new Dimension(180, 60));
        labBtn.setMaximumSize(new Dimension(180, 60));
        labBtn.setForeground(Color.WHITE);
        labBtn.setBackground(new Color(117, 144, 156));

        unitBtnPanel.add(lecBtn);
        unitBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        unitBtnPanel.add(labBtn);

        // Time out section
        JLabel timeoutLbl = new JLabel("TIME OUT");
        timeoutLbl.setFont(new Font("Arial", Font.BOLD, 20));
        JPanel timeOutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeOutPanel.setBackground(Color.WHITE);
        timeOutPanel.add(timeoutLbl);

        // BACKEND TO DO: Make this automatic from the computed time out based on their
        createTimeInSection();

        // and their course lecture or laboratory hour
        timeOut = new JLabel("No Time Set.", SwingConstants.CENTER);
        timeOut.setForeground(Color.GRAY);
        timeOut.setFont(new Font("Arial", Font.BOLD, 15));

        RoundedPanel timePanel = new RoundedPanel(10, 1, new Color(91, 112, 121));
        timePanel.setLayout(new GridBagLayout());
        timePanel.setPreferredSize(new Dimension(365, 60));
        timePanel.setMaximumSize(new Dimension(365, 60));
        timePanel.setBackground(Color.WHITE);

        GridBagConstraints gbcTime = new GridBagConstraints();
        gbcTime.gridx = 0;
        gbcTime.gridy = 0;
        gbcTime.anchor = GridBagConstraints.CENTER;
        timePanel.add(timeOut, gbcTime);

        JPanel timeWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        timeWrapper.setBackground(Color.WHITE);
        timeWrapper.add(timePanel);

        // Add all to form
        courseCombo = new RoundedComboBox<>(new String[] {}, 20, 2);
        courseCombo.setBorderColor(new Color(91, 112 ,121));
        form.add(selectCoursePanel);
        form.add(courseCombo);
        form.add(unitBtnPanel);
        form.add(timeInPanel);
        form.add(spinnerPan);
        form.add(timeOutPanel);
        form.add(timeWrapper);

        container.add(form);
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
        // sqlTime comes in as "07:00:00" or "HH:mm:ss"
        String[] parts = sqlTime.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        String suffix = hour >= 12 ? "PM" : "AM";
        int displayHour = hour > 12 ? hour - 12 : hour; // convert 13 → 1, 14 → 2
        if (displayHour == 0)
            displayHour = 12; // convert 0 → 12 for midnight

        String displayMinute = String.format("%02d", minute); // keep leading zero

        return displayHour + ":" + displayMinute + " " + suffix;
    }

    // initilization of the whole page
    void loadScheduleLayout() {
        // get the times array in the room

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
            timeLbl.setForeground(new Color(91, 112 ,121));
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

        // contains all the components of the view schedule frame
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.setBorder(BorderFactory.createEmptyBorder(20, -10, 20, 0)); // test line
        container.setBackground(Color.WHITE);

        // contains the information of the panel
        // BACKEND TO DO: Make it so that the database is automatically giving the
        // information
        // of the class to this panel
        RoundedPanel labelPanel = new RoundedPanel(25, 0, new BorderLayout());
        roomLbl = new JLabel(room.getRoomCode(), SwingConstants.CENTER);
        roomLbl.setForeground(Color.WHITE);
        roomLbl.setFont(new Font("Arial", Font.BOLD, 16));
        roomLbl.setOpaque(false);

        labelPanel.setBackground(new Color(117, 144, 156));
        labelPanel.setPreferredSize(new Dimension(200, 50));
        labelPanel.setMaximumSize(new Dimension(200, 50));
        labelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPanel.add(roomLbl, BorderLayout.CENTER);

        container.add(labelPanel);
        container.add(Box.createRigidArea(new Dimension(0, 10))); // this is used for spacing

        // the time table of the schedule, not including the panels
        timeSched = new JPanel(new GridBagLayout());
        timeSched.setBackground(Color.WHITE);
        timeSched.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        timeSched.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        timeSched.setAlignmentX(Component.CENTER_ALIGNMENT);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

    }

    // UTILITY
    public void addScheduleBlock(int column, String timeRange, boolean schedType, Schedule schedule) {
        System.out.println("Adding block: " + timeRange);
        String[] times = timeRange.split(" - ");
        System.out.println("Start: '" + times[0] + "', End: '" + times[1] + "'");
        int startRow = getRowFromTime(timeRange.split(" - ")[0]);
        int rowSpan = getTimeSpan(timeRange);

        // Remove overlapping components
        // checks the components of the timeSched if its overlapping with the panel the
        // user decided, if the element is a JPanel, it removes the border so the border
        // looks
        // like its at the back

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
        schedPanel.setBackground(schedType ? new Color(255, 169, 62) : new Color(255, 245, 157));
        if (!schedType)
            schedPanel.setForeground(Color.WHITE);

        schedPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 5, 5)));

        schedPanel.add(new JLabel(schedule.getCourseCode()){{
            setFont(new Font("Arial", Font.BOLD, 20));}});
        schedPanel.add(new JLabel(schedule.getFacultyID()){{
            setFont(new Font("Arial", Font.BOLD, 16));}});
        schedPanel.add(new JLabel(timeRange){{
            setFont(new Font("Arial", Font.PLAIN, 16));}});

        timeSched.add(schedPanel, gbc);
        timeSched.setComponentZOrder(schedPanel, 0);

        markOccupied(column, startRow, rowSpan);
        timeSched.revalidate();
        timeSched.repaint();
    }

    // UTILITY
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

    // UTILITY
    public int getTimeSpan(String timeRange) {
        String[] times = timeRange.split(" - ");
        int start = getRowFromTime(times[0]);
        int end = getRowFromTime(times[1]);
        return end - start;
    }

    // UTILITY
    public void markOccupied(int col, int startRow, int rowSpan) {
        for (int i = startRow; i < startRow + rowSpan; i++) {
            occupied[i][col] = true;
        }
    }

    // UTILITY
    public void setClick(boolean enable) {
        // checks the components in the GridBagLayout, treat it as an array
        for (Component c : timeSched.getComponents()) {
            // check if the component is a JPanel
            if (c instanceof JPanel) {
                JPanel panel = (JPanel) c;

                // for disabling the CLICKABLE feature of panel
                // if its not a JPanel, the component should not be CLICKABLE
                for (java.awt.event.MouseListener m : panel.getMouseListeners()) {
                    panel.removeMouseListener(m);
                }

                if (enable) {
                    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    panel.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            // BACKEND TO DO: Make the ADMIN view and modify the schedule
                            panel.setBackground(Color.WHITE); // TEST LINE IF WORKING
                        }
                    });

                } else {
                    // if the component is NOT a JPanel, the cursor should be default arrow
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }
}