package view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;

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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import view.components.RoundedPanel;
import view.components.RoundedToggleSwitch;

public class Report1 extends JPanel {

    private RoundedToggleSwitch toggleBtn;
    private JPanel contentPanel;
    private JPanel monthlyPanel;
    private JPanel weeklyPanel;
    private JToggleButton allBtn;
    private JToggleButton approvedBtn;
    private JToggleButton declinedBtn;
    private JToggleButton voidedBtn;
    
    private Consumer<Boolean> onToggleChanged;
    private Consumer<String> onFilterChanged;
    
    private int monthlyApproved;
    private int monthlyDeclined;
    private int monthlyVoid;
    private int weeklyApproved;
    private int weeklyDeclined;
    private int weeklyVoid;
    private int[] weeklyApprovedArr;
    private int[] weeklyDeclinedArr;
    private int[] weeklyVoidArr;

    public JToggleButton getAllBtn() {
        return allBtn;
    }

    public JToggleButton getApprovedBtn() {
        return approvedBtn;
    }

    public JToggleButton getDeclinedBtn() {
        return declinedBtn;
    }

    public JToggleButton getVoidedBtn() {
        return voidedBtn;
    }

    public void setOnToggleChanged(Consumer<Boolean> callback) {
        this.onToggleChanged = callback;
    }

    public void setOnFilterChanged(Consumer<String> callback) {
        this.onFilterChanged = callback;
    }

    public void setMonthlyData(int approved, int declined, int voided) {
        this.monthlyApproved = approved;
        this.monthlyDeclined = declined;
        this.monthlyVoid = voided;
    }

    public void setWeeklyData(int approved, int declined, int voided) {
        this.weeklyApproved = approved;
        this.weeklyDeclined = declined;
        this.weeklyVoid = voided;
    }

    public void setWeeklyArrays(int[] approved, int[] declined, int[] voided) {
        this.weeklyApprovedArr = approved;
        this.weeklyDeclinedArr = declined;
        this.weeklyVoidArr = voided;
    }

    public void showMonthlyView() {
        contentPanel.removeAll();
        monthlyPanel = createMonthlyPanel();
        contentPanel.add(monthlyPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showWeeklyView() {
        contentPanel.removeAll();
        weeklyPanel = createWeeklyPanel();
        contentPanel.add(weeklyPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void updateWeeklyChart(int[] approved, int[] declined, int[] voided) {
        if (weeklyPanel != null) {
            contentPanel.removeAll();
            weeklyPanel = createWeeklyPanelWithData(approved, declined, voided);
            contentPanel.add(weeklyPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    public void updateWeeklyChart(int[] data, String label) {
        if (weeklyPanel != null) {
            contentPanel.removeAll();
            weeklyPanel = createWeeklyPanelSingleSeries(data, label);
            contentPanel.add(weeklyPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    public Report1() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        buildHeader();
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void renderInitialView() {
        showMonthlyView();
    }

    private void buildHeader() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        toggleBtn = new RoundedToggleSwitch(40, 2);
        toggleBtn.setPreferredSize(new Dimension(220, 40));
        
        toggleBtn.addItemListener(e -> {
            boolean isWeekly = (e.getStateChange() == java.awt.event.ItemEvent.SELECTED);
            if (onToggleChanged != null) {
                onToggleChanged.accept(isWeekly);
            }
        });

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.add(toggleBtn);
        topPanel.add(btnWrapper, BorderLayout.NORTH);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(-5, 0, 5, 0));

        JLabel titleLabel = new JLabel("Total Room Schedule Requests", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
    }

    private JPanel createMonthlyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.add(createStatsCard("Monthly Room Schedule Requests", monthlyApproved, monthlyDeclined, monthlyVoid), BorderLayout.NORTH);
        panel.add(createPieChart(monthlyApproved, monthlyDeclined, monthlyVoid), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createWeeklyPanel() {
        return createWeeklyPanelWithData(weeklyApprovedArr, weeklyDeclinedArr, weeklyVoidArr);
    }

    private JPanel createWeeklyPanelWithData(int[] approvedArr, int[] declinedArr, int[] voidedArr) {
        int approved = weeklyApproved;
        int declined = weeklyDeclined;
        int voided = weeklyVoid;

        if (approvedArr == null) approvedArr = new int[6];
        if (declinedArr == null) declinedArr = new int[6];
        if (voidedArr == null) voidedArr = new int[6];

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(Color.WHITE);

        JLabel weekSpan = new JLabel("Current Week");
        weekSpan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        weekSpan.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(weekSpan);
        contentWrapper.add(Box.createVerticalStrut(3));

        JPanel statsCard = createStatsCard("Weekly Room Schedule Requests", approved, declined, voided);
        statsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(statsCard);
        contentWrapper.add(Box.createVerticalStrut(15));

        JPanel columnChartCard = createColumnChart(approvedArr, declinedArr, voidedArr);
        columnChartCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(columnChartCard);

        JPanel buttonPanel = createFilterButtons(approvedArr, declinedArr, voidedArr);
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

    private JPanel createWeeklyPanelSingleSeries(int[] data, String label) {
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(Color.WHITE);

        JLabel weekSpan = new JLabel("Current Week");
        weekSpan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        weekSpan.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(weekSpan);
        contentWrapper.add(Box.createVerticalStrut(3));

        JPanel statsCard = createStatsCard("Weekly Room Schedule Requests", weeklyApproved, weeklyDeclined, weeklyVoid);
        statsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(statsCard);
        contentWrapper.add(Box.createVerticalStrut(15));

        JPanel columnChartCard = createColumnChart(data, label);
        columnChartCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(columnChartCard);

        JPanel buttonPanel = createFilterButtons(weeklyApprovedArr, weeklyDeclinedArr, weeklyVoidArr);
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

    private JPanel createFilterButtons(int[] approvedArr, int[] declinedArr, int[] voidedArr) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonPanel.setBackground(Color.WHITE);

        allBtn = new JToggleButton("All");
        approvedBtn = new JToggleButton("Approved");
        declinedBtn = new JToggleButton("Declined");
        voidedBtn = new JToggleButton("Voided");

        JToggleButton[] buttons = {allBtn, approvedBtn, declinedBtn, voidedBtn};

        for (JToggleButton btn : buttons) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
        }

        allBtn.setSelected(true);
        allBtn.setBackground(new Color(139, 0, 0));
        allBtn.setForeground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        for (JToggleButton btn : buttons) {
            group.add(btn);
            buttonPanel.add(btn);

            btn.addActionListener(e -> {
                for (JToggleButton b : buttons) {
                    if (b.isSelected()) {
                        b.setBackground(new Color(139, 0, 0));
                        b.setForeground(Color.WHITE);
                    } else {
                        b.setBackground(Color.WHITE);
                        b.setForeground(Color.BLACK);
                    }
                }

                if (onFilterChanged != null) {
                    if (btn == allBtn) {
                        onFilterChanged.accept("All");
                    } else if (btn == approvedBtn) {
                        onFilterChanged.accept("Approved");
                    } else if (btn == declinedBtn) {
                        onFilterChanged.accept("Declined");
                    } else if (btn == voidedBtn) {
                        onFilterChanged.accept("Voided");
                    }
                }
            });
        }

        return buttonPanel;
    }

    public JPanel createPieChart(int approved, int declined, int voided) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);

        RoundedPanel chartPanel = new RoundedPanel(20, 2, new Color(190, 190, 190));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        chartPanel.setPreferredSize(new Dimension(400, 230));

        JLabel statsTitle = new JLabel("Pie Chart of Room Requests");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Approved", approved);
        dataset.setValue("Declined", declined);
        dataset.setValue("Void", voided);

        JFreeChart pieChart = ChartFactory.createPieChart("", dataset, true, true, false);

        ChartPanel chart = new ChartPanel(pieChart);
        chart.setPreferredSize(new Dimension(350, 150));
        chart.setOpaque(false);

        chartPanel.add(statsTitle);
        chartPanel.add(Box.createVerticalStrut(5));
        chartPanel.add(chart);

        wrapper.add(chartPanel);
        panel.add(wrapper, BorderLayout.CENTER);

        return panel;
    }

    // FIXED: Y-axis now starts at 0
    public JPanel createColumnChart(int[] approved, int[] declined, int[] voided) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        RoundedPanel chartPanel = new RoundedPanel(20, 0);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        chartPanel.setPreferredSize(new Dimension(400, 230));

        JLabel title = new JLabel("Column Chart of Room Requests");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartPanel.add(title);
        chartPanel.add(Box.createVerticalStrut(5));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] daysOfWeek = {"M", "T", "W", "Th", "F", "Sat"};

        for (int i = 0; i < 6; i++) {
            dataset.addValue(approved[i], "Approved", daysOfWeek[i]);
            dataset.addValue(declined[i], "Declined", daysOfWeek[i]);
            dataset.addValue(voided[i], "Void", daysOfWeek[i]);
        }

        JFreeChart columnChart = ChartFactory.createBarChart("", "Week Day", "", dataset);

        // FIX: Configure Y-axis to always start at 0
        CategoryPlot plot = columnChart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLowerBound(0);
        rangeAxis.setAutoRangeMinimumSize(1);
        rangeAxis.setUpperMargin(0.15);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel chart = new ChartPanel(columnChart);
        chart.setPreferredSize(new Dimension(400, 150));
        chart.setOpaque(false);
        chartPanel.add(chart);

        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    // FIXED: Y-axis now starts at 0
    public JPanel createColumnChart(int[] status, String dataSetValue) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        RoundedPanel chartPanel = new RoundedPanel(20, 0);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        chartPanel.setPreferredSize(new Dimension(400, 230));

        JLabel title = new JLabel("Column Chart of Room Requests");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartPanel.add(title);
        chartPanel.add(Box.createVerticalStrut(5));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] daysOfWeek = {"M", "T", "W", "Th", "F", "Sat"};

        for (int i = 0; i < 6; i++) {
            dataset.addValue(status[i], dataSetValue, daysOfWeek[i]);
        }

        JFreeChart columnChart = ChartFactory.createBarChart("", "Week Day", "", dataset);

        // FIX: Configure Y-axis to always start at 0
        CategoryPlot plot = columnChart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLowerBound(0);
        rangeAxis.setAutoRangeMinimumSize(1);
        rangeAxis.setUpperMargin(0.15);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel chart = new ChartPanel(columnChart);
        chart.setPreferredSize(new Dimension(400, 150));
        chart.setOpaque(false);
        chartPanel.add(chart);

        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    public JPanel createStatsCard(String title, int approvedCount, int declinedCount, int voidCount) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);

        RoundedPanel statsPanel = new RoundedPanel(20, 2, new Color(190, 190, 190));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        statsPanel.setPreferredSize(new Dimension(400, 220));

        JLabel statsTitle = new JLabel(title);
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        int total = approvedCount + declinedCount + voidCount;
        JLabel totalStatsLbl = new JLabel(String.valueOf(total));
        totalStatsLbl.setFont(new Font("Segoe UI", Font.BOLD, 50));
        totalStatsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel breakdownPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        breakdownPanel.setBackground(Color.WHITE);
        breakdownPanel.setMaximumSize(new Dimension(300, 120));

        JLabel approvedLbl = new JLabel("Approved", SwingConstants.LEFT);
        approvedLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel approvedLabel = new JLabel(String.valueOf(approvedCount), SwingConstants.RIGHT);
        approvedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel declinedLbl = new JLabel("Declined", SwingConstants.LEFT);
        declinedLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel declinedLabel = new JLabel(String.valueOf(declinedCount), SwingConstants.RIGHT);
        declinedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel voidLbl = new JLabel("Void", SwingConstants.LEFT);
        voidLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel voidLabel = new JLabel(String.valueOf(voidCount), SwingConstants.RIGHT);
        voidLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        breakdownPanel.add(approvedLbl);
        breakdownPanel.add(approvedLabel);
        breakdownPanel.add(declinedLbl);
        breakdownPanel.add(declinedLabel);
        breakdownPanel.add(voidLbl);
        breakdownPanel.add(voidLabel);

        JPanel breakdownWrapper = new JPanel(new GridBagLayout());
        breakdownWrapper.setBackground(Color.WHITE);
        breakdownWrapper.add(breakdownPanel);

        statsPanel.add(statsTitle);
        statsPanel.add(Box.createVerticalStrut(2));
        statsPanel.add(totalStatsLbl);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(breakdownWrapper);

        wrapper.add(statsPanel);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }
}