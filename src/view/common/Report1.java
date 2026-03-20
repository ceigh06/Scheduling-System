package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import view.components.RoundedPanel;
import view.components.RoundedToggleSwitch;

public class Report1 extends JPanel implements ItemListener {

    private RoundedToggleSwitch toggleBtn;
    private JPanel contentPanel;
    //BACKEND TO DO: these panels should be populated with actual data from the database when created
    private JPanel monthlyPanel;
    private JPanel weeklyPanel;
    //BACKEND TO DO: these labels should be updated with actual counts from the database when the panels are created
    private JLabel approvedCount;
    private JLabel deniedCount;
    private JLabel voidCount; 
    private int monthlyApproved, monthlyDenied, monthlyVoid;
    private int weeklyApproved, weeklyDenied, weeklyVoid;
    private JToggleButton allBtn;
    private JToggleButton approvedBtn;
    private JToggleButton deniedBtn;
    private JToggleButton voidedBtn;

    public JToggleButton getAllBtn() {
        return allBtn;
    }

    public JToggleButton getApprovedBtn() {
        return approvedBtn;
    }

    public JToggleButton getDeniedBtn() {
        return deniedBtn;
    }

    public JToggleButton getVoidedBtn() {
        return voidedBtn;
    }

    //BACKEND TO DO: these methods should retrieve actual counts from the database 
    //I just hardcoded them for now to test the UIs
    public int getMonthlyApproved(){
        return monthlyApproved = 300;
    }

    public int getMonthlyDenied(){
        return monthlyDenied = 150;
    }

    public int getMonthlyVoid(){
        return monthlyVoid = 50;
    }

    public int getWeeklyApproved(){
        return weeklyApproved = 80;
    }

    public int getWeeklyDenied(){
        return weeklyDenied = 40;
    }

    public int getWeeklyVoid(){
        return weeklyVoid = 10;
    }

    public JLabel getApprovedCount(){
        return approvedCount;
    }

    public JLabel getDeniedCount(){
        return deniedCount;
    }

    public JLabel getVoidCount(){
        return voidCount;
    }

    public JPanel getMonthlyPanel(){
        return monthlyPanel;
    }

    public JPanel getWeeklyPanel(){
        return weeklyPanel;
    }
    
    public Report1(){
        setLayout(new BorderLayout()); 
        setBackground(Color.WHITE);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout()); 
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        toggleBtn = new RoundedToggleSwitch(40, 2); 
        toggleBtn.setPreferredSize(new Dimension(220,40));
        toggleBtn.addItemListener(this);

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.add(toggleBtn);

        topPanel.add(btnWrapper, BorderLayout.NORTH);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(-5, 0, 5, 0));

        JLabel titleLabel = new JLabel("Total Room Schedule Requests", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segeo UI", Font.BOLD, 24));

        titlePanel.add(titleLabel);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        // Content panels 
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        monthlyPanel = openMonthlyPanel();
        weeklyPanel = openWeeklyPanel();
        switchView(false); // default to monthly view

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    //test line: 
    public JPanel openMonthlyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        int approved = getMonthlyApproved();
        int denied = getMonthlyDenied();
        int voided = getMonthlyVoid();

        panel.add(createStatsCard("Monthly Room Schedule Requests", approved, denied, voided), BorderLayout.NORTH);
        // lower panel for the pie chart
        //BACKEND TO DO: the counts passed into createPieChart should be the 
        // actual counts from the database
        panel.add(createPieChart(getMonthlyApproved(), 
        getMonthlyDenied(), 
        getMonthlyVoid()),
        BorderLayout.SOUTH);

        return panel;
    }

    public JPanel openWeeklyPanel() {
        int approved = getWeeklyApproved();
        int denied   = getWeeklyDenied();
        int voided   = getWeeklyVoid();

        int[] approvedArr = {10, 15, 20, 18, 12, 5};
        int[] deniedArr = {5, 7, 10, 8, 6, 3};
        int[] voidedArr = {2, 3, 4, 3, 2, 1};

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(Color.WHITE);

        JPanel statsCard = createStatsCard("Weekly Room Schedule Requests", approved, denied, voided);
        statsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(statsCard);
        contentWrapper.add(Box.createVerticalStrut(15));

        JPanel columnChartCard = createColumnChart(approvedArr, deniedArr, voidedArr);
        columnChartCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(columnChartCard);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        //labas mo to jessie
        allBtn = new JToggleButton("All");
        approvedBtn = new JToggleButton("Approved");
        deniedBtn = new JToggleButton("Denied");
        voidedBtn = new JToggleButton("Voided");

        JToggleButton[] buttons = { allBtn, approvedBtn, deniedBtn, voidedBtn };

        for (JToggleButton btn : buttons) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
        }

        allBtn.setSelected(true);
        allBtn.setBackground(new Color(139,0,0));
        allBtn.setForeground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        for (JToggleButton btn : buttons) {
            group.add(btn);
            buttonPanel.add(btn);

            btn.addActionListener(e -> {
                for (JToggleButton b : buttons) {
                    if (b.isSelected()) {
                        b.setBackground(new Color(139,0,0));
                        b.setForeground(Color.WHITE);
                    } else {
                        b.setBackground(Color.WHITE);
                        b.setForeground(Color.BLACK);
                    }
                }

                //test lines: 
                if (btn == allBtn) {
                    columnChartCard.removeAll(); 
                    columnChartCard.add(createColumnChart(approvedArr, deniedArr, voidedArr));
                    columnChartCard.revalidate();
                    columnChartCard.repaint();
                } else if (btn == approvedBtn) {
                    columnChartCard.removeAll(); 
                    columnChartCard.add(createColumnChart(approvedArr, "Approved"));
                    columnChartCard.revalidate();
                    columnChartCard.repaint();
                } else if (btn == deniedBtn) {
                    columnChartCard.removeAll(); 
                    columnChartCard.add(createColumnChart(deniedArr, "Denied"));
                    columnChartCard.revalidate();
                    columnChartCard.repaint();
                } else if (btn == voidedBtn) {
                    columnChartCard.removeAll(); 
                    columnChartCard.add(createColumnChart(voidedArr, "Voided"));
                    columnChartCard.revalidate();
                    columnChartCard.repaint();
                }
            });
        }

        contentWrapper.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(contentWrapper,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public JPanel createPieChart(int approved, int denied, int voided){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);

        RoundedPanel chartPanel = new RoundedPanel(20, 2, new Color(190,190,190));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(10,20,20,20));
        chartPanel.setPreferredSize(new Dimension(400,230));

        JLabel statsTitle = new JLabel("Pie Chart of Room Requests");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        //BACKEND TO DO: the dataset should be populated with actual counts from the database
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Approved", approved);
        dataset.setValue("Denied", denied);
        dataset.setValue("Void", voided);

        JFreeChart pieChart = ChartFactory.createPieChart("", dataset, true, true, false);

        ChartPanel chart = new ChartPanel(pieChart);
        chart.setPreferredSize(new Dimension(350,150));
        chart.setOpaque(false);

        chartPanel.add(statsTitle);
        chartPanel.add(Box.createVerticalStrut(5)); //spacing for BoxLayout, don't remove
        chartPanel.add(chart);

        wrapper.add(chartPanel); 
        panel.add(wrapper, BorderLayout.CENTER);

        return panel; 
    }

    public JPanel createColumnChart(int[] approved, int[] denied, int[] voided){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        RoundedPanel chartPanel = new RoundedPanel(20, 0);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(10,20,20,20));
        chartPanel.setPreferredSize(new Dimension(400,230));

        JLabel title = new JLabel("Column Chart of Room Requests");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartPanel.add(title);
        chartPanel.add(Box.createVerticalStrut(5));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] daysOfWeek = {"M", "T", "W", "Th", "F", "Sat"};

        for(int i=0; i<6; i++){
            dataset.addValue(approved[i], "Approved", daysOfWeek[i]);
            dataset.addValue(denied[i], "Denied", daysOfWeek[i]);
            dataset.addValue(voided[i], "Void", daysOfWeek[i]);
        }

        JFreeChart columnChart = ChartFactory.createBarChart("", "Week Day", "", dataset);

        ChartPanel chart = new ChartPanel(columnChart);
        chart.setPreferredSize(new Dimension(400,150));
        chart.setOpaque(false);
        chartPanel.add(chart);

        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    // for specific dataset value (approved, denied, or voided) 
    // this method is used to create the column chart in the weekly view
    public JPanel createColumnChart(int[] status, String dataSetValue){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        RoundedPanel chartPanel = new RoundedPanel(20, 0);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(10,20,20,20));
        chartPanel.setPreferredSize(new Dimension(400,230));

        JLabel title = new JLabel("Column Chart of Room Requests");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartPanel.add(title);
        chartPanel.add(Box.createVerticalStrut(5));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] daysOfWeek = {"M", "T", "W", "Th", "F", "Sat"};

        for(int i=0; i<6; i++){
            dataset.addValue(status[i], dataSetValue, daysOfWeek[i]);
        }

        JFreeChart columnChart = ChartFactory.createBarChart("", "Week Day", "", dataset);

        ChartPanel chart = new ChartPanel(columnChart);
        chart.setPreferredSize(new Dimension(400,150));
        chart.setOpaque(false);
        chartPanel.add(chart);

        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    public JPanel createStatsCard(String title, int approvedCount, int deniedCount, int voidCount){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);

        RoundedPanel statsPanel = new RoundedPanel(20, 2, new Color(190, 190, 190));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(new EmptyBorder(10,20,10,20));
        statsPanel.setPreferredSize(new Dimension(400,220));

        // Title and total
        JLabel statsTitle = new JLabel(title);
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        int total = approvedCount + deniedCount + voidCount;
        JLabel totalStatsLbl = new JLabel(String.valueOf(total));
        totalStatsLbl.setFont(new Font("Segoe UI", Font.BOLD, 50));
        totalStatsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Breakdown panel
        JPanel breakdownPanel = new JPanel(new GridLayout(3,2,10,10));
        breakdownPanel.setBackground(Color.WHITE);
        breakdownPanel.setMaximumSize(new Dimension(300,120));

        JLabel approvedLbl = new JLabel("Approved", SwingConstants.LEFT);
        approvedLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel approvedLabel = new JLabel(String.valueOf(approvedCount), SwingConstants.RIGHT);
        approvedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel deniedLbl = new JLabel("Denied", SwingConstants.LEFT);
        deniedLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel deniedLabel = new JLabel(String.valueOf(deniedCount), SwingConstants.RIGHT);
        deniedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel voidLbl = new JLabel("Void", SwingConstants.LEFT);
        voidLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel voidLabel = new JLabel(String.valueOf(voidCount), SwingConstants.RIGHT);
        voidLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        breakdownPanel.add(approvedLbl);
        breakdownPanel.add(approvedLabel);
        breakdownPanel.add(deniedLbl); 
        breakdownPanel.add(deniedLabel);
        breakdownPanel.add(voidLbl); 
        breakdownPanel.add(voidLabel);

        JPanel breakdownWrapper = new JPanel(new GridBagLayout());
        breakdownWrapper.setBackground(Color.WHITE);
        breakdownWrapper.add(breakdownPanel);

        statsPanel.add(statsTitle);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(totalStatsLbl);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(breakdownWrapper);

        wrapper.add(statsPanel);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    //test line: PASSED 
    public void switchView(boolean isWeekly){
        contentPanel.removeAll(); // clear existing content so it is 'smooth' when switching
        if(isWeekly){
            contentPanel.add(openWeeklyPanel(), BorderLayout.CENTER);
        } else {
            contentPanel.add(openMonthlyPanel(), BorderLayout.CENTER);
        }
        // revalidate and repaint to update the UI after switching panels
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Determine if the toggle is in the "weekly" (selected) or 
        // "monthly" (deselected) state
        boolean isPindot = (e.getStateChange() == ItemEvent.SELECTED);
        switchView(isPindot); 
    }
}
