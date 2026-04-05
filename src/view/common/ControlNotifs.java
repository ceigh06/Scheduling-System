package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.user.Student;
import view.components.RoundedPanel;
import view.components.RoundedTextField;
import view.components.ScrollBarHelper;

public class ControlNotifs extends JPanel {

        private JPanel contentPanel, formsPanel, formsTop, timeSection, timeLabels, timeValues, formsBottom;
        private JLabel timeInLbl, timeOutLbl, headerLabel;
        private ConfirmPanel confirmArea;
        private RoundedTextField field, timeInTxt, timeOutTxt;
        private JScrollPane scrollPanel;
        RoundedPanel headerPanel;

        public ControlNotifs() {
                setLayout(new BorderLayout());
                setBackground(Color.WHITE);

                contentPanel = new JPanel(new BorderLayout());
                contentPanel.setBackground(Color.WHITE);

                formsPanel = new JPanel();
                formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
                formsPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));
                formsPanel.setBackground(Color.WHITE);

                // Top section - Student info (4 rows, tighter spacing)
                formsTop = new JPanel(new GridLayout(4, 1, 0, 12));
                formsTop.setBackground(Color.WHITE);
                formsTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

                field = new RoundedTextField(10, 20, 1,
                                new Color(200, 200, 200),
                                null);
                RequestForm.styleField(field);

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
                formsBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                formsBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

                contentPanel.add(formsPanel, BorderLayout.CENTER);

                scrollPanel = new JScrollPane(contentPanel);
                scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
                scrollPanel.setBorder(BorderFactory.createEmptyBorder());
                scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
                scrollPanel.getViewport().setBackground(Color.WHITE);

                add(scrollPanel, BorderLayout.CENTER);
        }

        public JPanel addHeaderPanel(String classType) {
                headerLabel = new JLabel(classType, JLabel.CENTER);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
                headerLabel.setForeground(Color.WHITE);

                headerPanel = new RoundedPanel(40, 4, new Color(91, 112, 121));
                headerPanel.setLayout(new BorderLayout());
                headerPanel.setPreferredSize(new Dimension(200, 60));
                headerPanel.setMaximumSize(new Dimension(200, 60));
                headerPanel.setBackground(new Color(117, 144, 156));
                headerPanel.add(headerLabel);

                JPanel headerWrapper = new JPanel(); // test line
                headerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
                headerWrapper.setPreferredSize(new Dimension(300, 70));
                headerWrapper.setMaximumSize(new Dimension(300, 70));
                headerWrapper.setBackground(Color.WHITE);
                headerWrapper.add(headerPanel);
                return headerWrapper;
        }

        public void loadRequestStatusHeader(String status) {
                JPanel statusHeader = addHeaderPanel(status.toUpperCase());
                statusHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
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
                formsPanel.add(Box.createVerticalStrut(10));

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
                formsPanel.add(Box.createVerticalStrut(20));

                if (status.equalsIgnoreCase("pending")) {

                        confirmArea = new ConfirmPanel(MainFrame.getFrame(),
                                        "GO BACK", "CANCEL REQUEST",
                                        new Color(91, 112, 121), 2,
                                        new Color(227, 75, 75), 2);
                        confirmArea.setBtn2Color(new Color(255, 100, 100));

                        formsPanel.add(confirmArea.getConfirmPanel());
                }

                formsPanel.revalidate();
                formsPanel.repaint();

        }

        public void setOnCancelClicked(ActionListener action) {
                confirmArea.setBtn2Action(action);
        }

        public void setOnBackClicked(ActionListener action) {
                confirmArea.setBtn1Action(action);
        }

}
