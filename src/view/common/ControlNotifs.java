package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import model.schedule.RequestSchedule;
import model.user.Student;
import view.components.RoundedTextField;

public class ControlNotifs extends JPanel {

        private JPanel contentPanel, formsPanel, formsTop, timeSection, timeLabels, timeValues, formsBottom;
        private JLabel timeInLbl, timeOutLbl;
        private ConfirmPanel confirmArea;
        private RoundedTextField field, timeInTxt, timeOutTxt;
        private JScrollPane scrollPanel;

        public ControlNotifs() {
                setLayout(new BorderLayout());
                setBackground(Color.WHITE);

                contentPanel = new JPanel(new BorderLayout());
                contentPanel.setBackground(Color.WHITE);

                formsPanel = new JPanel();
                formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
                formsPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
                formsPanel.setBackground(Color.WHITE);

                // Top section - Student info (4 rows, tighter spacing)
                formsTop = new JPanel(new GridLayout(4, 1, 0, 12));
                formsTop.setBackground(Color.WHITE);
                formsTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

                field = new RoundedTextField(10, 20, 1,
                                new Color(200, 200, 200),
                                null);
                RequestForm.styleField(field);

                formsPanel.add(Box.createVerticalStrut(20));

                timeSection = new JPanel();
                timeSection.setLayout(new BoxLayout(timeSection, BoxLayout.Y_AXIS));
                timeSection.setBackground(Color.WHITE);
                timeSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

                // Labels row
                timeLabels = new JPanel(new GridLayout(1, 2, 20, 0));
                timeLabels.setBackground(Color.WHITE);

                timeInLbl = new JLabel("Time In");
                timeInLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                timeInLbl.setForeground(Color.DARK_GRAY);

                timeOutLbl = new JLabel("Time Out");
                timeOutLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                timeOutLbl.setForeground(Color.DARK_GRAY);

                timeLabels.add(timeInLbl);
                timeLabels.add(timeOutLbl);

                // Values row
                timeValues = new JPanel(new GridLayout(1, 2, 20, 0));
                timeValues.setBackground(Color.WHITE);

                timeInTxt = new RoundedTextField(10, 20, 1,
                                new Color(200, 200, 200),
                                null);

                RequestForm.styleField(timeInTxt);

                timeOutTxt = new RoundedTextField(10, 20, 1,
                                new Color(200, 200, 200),
                                null);

                RequestForm.styleField(timeOutTxt);

                formsPanel.add(Box.createVerticalStrut(20));

                // Bottom section - Course and Professor
                formsBottom = new JPanel(new GridLayout(2, 1, 0, 12));
                formsBottom.setBackground(Color.WHITE);
                formsBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

                formsPanel.add(Box.createVerticalStrut(30));

                contentPanel.add(formsPanel, BorderLayout.CENTER);

                scrollPanel = new JScrollPane(contentPanel);
                scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPanel.setBorder(BorderFactory.createEmptyBorder());
                scrollPanel.getViewport().setBackground(Color.WHITE);

                add(scrollPanel, BorderLayout.CENTER);
        }

        private JPanel addHeaderPanel(String status) {
                JPanel headerPanel = new JPanel();
                headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
                headerPanel.setBackground(Color.WHITE);
                headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel headerLabel = new JLabel(status, JLabel.CENTER);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
                headerLabel.setForeground(Color.DARK_GRAY);
                headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                headerPanel.add(headerLabel);
                headerPanel.add(Box.createVerticalStrut(8));

                return headerPanel;
        }

        public void loadRequestStatusHeader(String status) {
                JPanel statusHeader = addHeaderPanel(status);
                contentPanel.add(statusHeader, BorderLayout.NORTH);
        }

        public void loadRequestForm(Student student, String section, String room, String timeIn, String timeOut,
                        String course, String faculty, String status) {
                formsTop.add(RequestForm.labeledField("Student No.", student.getStudentID()));
                formsTop.add(RequestForm.labeledField("Name",
                                student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName()));
                formsTop.add(RequestForm.labeledField("Section", section));
                formsTop.add(RequestForm.labeledField("Room", room));
                formsPanel.add(formsTop);

                timeInTxt.setText(timeIn);
                timeOutTxt.setText(timeOut);
                timeValues.add(timeInTxt);
                timeValues.add(timeOutTxt);
                timeSection.add(timeLabels);
                timeSection.add(Box.createVerticalStrut(6));
                timeSection.add(timeValues);
                formsPanel.add(timeSection);

                formsBottom.add(RequestForm.labeledField("Course", course));
                formsBottom.add(RequestForm.labeledField("Faculty", faculty));
                formsPanel.add(formsBottom);

                if (status.equalsIgnoreCase("pending")) {
                        confirmArea = new ConfirmPanel(MainFrame.getFrame(),
                                        "GO BACK", "SUBMIT",
                                        new Color(227, 75, 75), 2,
                                        new Color(77, 139, 78), 2);
                        confirmArea.setBtn1Color(new Color(255, 100, 100));
                        confirmArea.setBtn2Color(new Color(63, 193, 127));
                        formsPanel.add(confirmArea.getConfirmPanel());
                }

        }

}
