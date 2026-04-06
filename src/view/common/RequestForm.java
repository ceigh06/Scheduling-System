package view.common;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
import utilities.DateTimeBuilder;
import view.components.RoundedTextField;
import view.components.ScrollBarHelper;

@SuppressWarnings("serial")
public class RequestForm extends JPanel {

    public static boolean isRequest = false;
    public static String studentNumber;

    ConfirmPanel reqConfirm;

    public void setSubmitOnClick(ActionListener action) {
        reqConfirm.setBtn2Action(action);
    }

    public void setGoBackOnClick(ActionListener action) {
        reqConfirm.setBtn1Action(action);
    }

    String requestorID,
            name,
            section,
            roomCode,
            timeIn,
            timeOut,
            course,
            professor;

    public RequestForm(RequestSchedule requestSchedule) {
        studentNumber = null;
        isRequest = false;
        this.roomCode = requestSchedule.getRoomCode();
        this.requestorID = requestSchedule.getStudentRequested();
        this.name = requestSchedule.getStudentRequested();
        this.section = requestSchedule.getSectionKey();
        this.timeIn = requestSchedule.getTimeIn();
        this.timeOut = requestSchedule.getTimeOut();
        this.course = requestSchedule.getCourseCode();
        this.professor = requestSchedule.getFacultyID();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel formsPanel = new JPanel();
        formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
        formsPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formsPanel.setBackground(Color.WHITE);

        JLabel sectionHeader = new JLabel("Confirm your request", JLabel.CENTER);
        sectionHeader.setForeground(Color.DARK_GRAY);
        sectionHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sectionHeader.setAlignmentX(CENTER_ALIGNMENT);
        formsPanel.add(sectionHeader);
        formsPanel.add(Box.createVerticalStrut(25));

        // Top section - Student info (4 rows, tighter spacing)
        JPanel formsTop = new JPanel(new GridLayout(4, 1, 0, 12));
        formsTop.setBackground(Color.WHITE);
        formsTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        formsTop.add(labeledField("Requestor", requestorID));
        formsTop.add(labeledField("Name", name));
        formsTop.add(labeledField("Section", section));
        formsTop.add(labeledField("Room Code", this.roomCode));
        formsPanel.add(formsTop);
        formsPanel.add(Box.createVerticalStrut(20));

        // Middle section - Time In/Time Out (labels row, values row)
        JPanel timeSection = new JPanel();
        timeSection.setLayout(new BoxLayout(timeSection, BoxLayout.Y_AXIS));
        timeSection.setBackground(Color.WHITE);
        timeSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Labels row
        JPanel timeLabels = new JPanel(new GridLayout(1, 2, 20, 0));

        timeLabels.setBackground(Color.WHITE);

        JLabel timeInLbl = new JLabel("Time In");
        timeInLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeInLbl.setForeground(Color.DARK_GRAY);

        JLabel timeOutLbl = new JLabel("Time Out");
        timeOutLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeOutLbl.setForeground(Color.DARK_GRAY);

        timeLabels.add(timeInLbl);
        timeLabels.add(timeOutLbl);

        // Values row
        JPanel timeValues = new JPanel(new GridLayout(1, 2, 20, 0));
        timeValues.setBackground(Color.WHITE);

        RoundedTextField timeInTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200),
                null);
        timeInTxt.setText(timeIn);
        styleField(timeInTxt);

        RoundedTextField timeOutTxt = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200),
                null);
        timeOutTxt.setText(timeOut);
        styleField(timeOutTxt);

        timeValues.add(timeInTxt);
        timeValues.add(timeOutTxt);

        timeSection.add(timeLabels);
        timeSection.add(Box.createVerticalStrut(6));
        timeSection.add(timeValues);

        formsPanel.add(timeSection);
        formsPanel.add(Box.createVerticalStrut(20));

        // Bottom section - Course and Professor
        JPanel formsBottom = new JPanel(new GridLayout(2, 1, 0, 12));
        formsBottom.setBackground(Color.WHITE);
        formsBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        formsBottom.add(labeledField("Course", course));
        formsBottom.add(labeledField("Professor", professor));
        formsPanel.add(formsBottom);
        formsPanel.add(Box.createVerticalStrut(30));

        // Confirm buttons
        reqConfirm = new ConfirmPanel(MainFrame.getFrame(),
                "GO BACK", "SUBMIT",
                new Color(91, 112, 121), 2,
                new Color(91, 112, 121), 2);

        formsPanel.add(reqConfirm.getConfirmPanel());
        contentPanel.add(formsPanel, BorderLayout.CENTER);

        JScrollPane scrollPanel = new JScrollPane(contentPanel);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        scrollPanel.setBorder(null);
        scrollPanel.getViewport().setBackground(Color.WHITE);

        add(scrollPanel, BorderLayout.CENTER);
    }

    public RequestForm(Schedule schedule, User user, Boolean viewArchives,
                   String requestorID, String studentName, String fullSectionName) throws SQLException {
        isRequest = false;
        studentNumber = null;
        if (user.getUserType().equals("Admin")) {
            isRequest = schedule.getStatus().equals("3");

        if (isRequest) {
            this.requestorID = requestorID;
            studentNumber = this.requestorID;
            this.name = studentName;
        }

        this.section = fullSectionName; 
        this.roomCode = schedule.getRoomCode();
        this.timeIn = DateTimeBuilder.formatTo12Hour(schedule.getTimeIn());
        this.timeOut = DateTimeBuilder.formatTo12Hour(schedule.getTimeOut());
        this.course = schedule.getCourseCode();
        this.professor = schedule.getFacultyID();

            setLayout(new BorderLayout());
            setBackground(Color.WHITE);

            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(Color.WHITE);

            JPanel formsPanel = new JPanel();
            formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
            formsPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
            formsPanel.setBackground(Color.WHITE);

            JLabel sectionHeader = new JLabel("Schedule Details", JLabel.CENTER);
            sectionHeader.setForeground(Color.DARK_GRAY);
            sectionHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
            sectionHeader.setAlignmentX(CENTER_ALIGNMENT);
            formsPanel.add(sectionHeader);
            formsPanel.add(Box.createVerticalStrut(25));

            // Top section - Student info (4 rows, tighter spacing)
            int rowCount = isRequest ? 4 : 2;
            JPanel formsTop = new JPanel(new GridLayout(rowCount, 1, 0, 12));
            formsTop.setBackground(Color.WHITE);
            formsTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

            if (isRequest) {
                formsTop.add(labeledField("Requestor", requestorID));
                formsTop.add(labeledField("Name", name));
            }
            formsTop.add(labeledField("Section", section));
            formsTop.add(labeledField("Room Code", this.roomCode));
            formsPanel.add(formsTop);
            formsPanel.add(Box.createVerticalStrut(20));

            // Middle section - Time In/Time Out (labels row, values row)
            JPanel timeSection = new JPanel();
            timeSection.setLayout(new BoxLayout(timeSection, BoxLayout.Y_AXIS));
            timeSection.setBackground(Color.WHITE);
            timeSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

            // Labels row
            JPanel timeLabels = new JPanel(new GridLayout(1, 2, 20, 0));

            timeLabels.setBackground(Color.WHITE);

            JLabel timeInLbl = new JLabel("Time In");
            timeInLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            timeInLbl.setForeground(Color.DARK_GRAY);

            JLabel timeOutLbl = new JLabel("Time Out");
            timeOutLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            timeOutLbl.setForeground(Color.DARK_GRAY);

            timeLabels.add(timeInLbl);
            timeLabels.add(timeOutLbl);

            // Values row
            JPanel timeValues = new JPanel(new GridLayout(1, 2, 20, 0));
            timeValues.setBackground(Color.WHITE);

            RoundedTextField timeInTxt = new RoundedTextField(10, 20, 1,
                    new Color(200, 200, 200),
                    null);
            timeInTxt.setText(timeIn);
            styleField(timeInTxt);

            RoundedTextField timeOutTxt = new RoundedTextField(10, 20, 1,
                    new Color(200, 200, 200),
                    null);
            timeOutTxt.setText(timeOut);
            styleField(timeOutTxt);

            timeValues.add(timeInTxt);
            timeValues.add(timeOutTxt);

            timeSection.add(timeLabels);
            timeSection.add(Box.createVerticalStrut(6));
            timeSection.add(timeValues);

            formsPanel.add(timeSection);
            formsPanel.add(Box.createVerticalStrut(20));

            // Bottom section - Course and Professor
            JPanel formsBottom = new JPanel(new GridLayout(2, 1, 0, 12));
            formsBottom.setBackground(Color.WHITE);
            formsBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

            formsBottom.add(labeledField("Course", course));
            formsBottom.add(labeledField("Professor", professor));
            formsPanel.add(formsBottom);
            formsPanel.add(Box.createVerticalStrut(30));

            String btn2Text = viewArchives ? "UNARCHIVE" : "ARCHIVE";

            // Confirm buttons
            reqConfirm = new ConfirmPanel(MainFrame.getFrame(),
                    "GO BACK", btn2Text,
                    new Color(91, 112, 121), 2,
                    new Color(91, 112, 121), 2);

            formsPanel.add(reqConfirm.getConfirmPanel());
            contentPanel.add(formsPanel, BorderLayout.CENTER);

            JScrollPane scrollPanel = new JScrollPane(contentPanel);
            scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
            scrollPanel.setBorder(null);
            scrollPanel.getViewport().setBackground(Color.WHITE);

            add(scrollPanel, BorderLayout.CENTER);
        }
    }

    public static JPanel labeledField(String labelText, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.DARK_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        label.setAlignmentX(LEFT_ALIGNMENT);

        RoundedTextField field = new RoundedTextField(10, 20, 1,
                new Color(200, 200, 200),
                null);
        field.setText(value);
        styleField(field);
        field.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(field);
        return panel;
    }

    public static void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setForeground(Color.DARK_GRAY);
        field.setEditable(false);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    public ConfirmPanel getReqConfirm() {
        return reqConfirm;
    }
    
}
