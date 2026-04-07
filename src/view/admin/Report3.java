
package view.admin;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.events.MouseEvent;

import dao.schedule.RequestScheduleDAO;
import view.common.RequestHistory;
import view.components.RoundedLabel;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;

public class Report3 extends JPanel {

    private JPanel calendar;
    private JPanel lineGraphContainer;
    private List<String> dates, trimmedDates, day;
    private static int dateTodayIdx = -1;
    private static String dateSelected;
    private JLabel selectedDayLabel = null;
    private RoundedPanel selectedDatePanel = null;
    private static String weekRange;

    public Report3() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d");
        LocalDate today = LocalDate.now();
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        LocalDate saturday = monday.plusDays(5);
        weekRange = formatter.format(monday) + " - " + formatter.format(saturday);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = createTopPanel();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(topPanel);

        RoundedPanel heatMapPanel = new RoundedPanel(20, 2, new Color(117, 144, 156));
        heatMapPanel.setPreferredSize(new Dimension(400, 250));
        heatMapPanel.setMaximumSize(new Dimension(400, 250));
        heatMapPanel.setLayout(new BorderLayout());
        heatMapPanel.setBackground(Color.WHITE);
        heatMapPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        heatMapPanel.add(createHeatMap(), BorderLayout.CENTER);

        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(heatMapPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(createHeatmapLegend());
        contentPanel.add(Box.createVerticalStrut(10));

        JLabel lineChartLbl = new JLabel("Time-of-Day Requests");
        lineChartLbl.setAlignmentX(CENTER_ALIGNMENT);
        lineChartLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contentPanel.add(lineChartLbl);
        contentPanel.add(Box.createVerticalStrut(5));

        // Calendar
        calendar = loadCalendar();
        contentPanel.add(calendar);
        contentPanel.add(Box.createVerticalStrut(5));

        // Line graph container
        lineGraphContainer = new JPanel(new BorderLayout());
        lineGraphContainer.setPreferredSize(new Dimension(400, 250));
        lineGraphContainer.setBackground(Color.WHITE);

        // Initial graph
        lineGraphContainer.add(createLineGraph("Mon"), BorderLayout.CENTER);

        contentPanel.add(lineGraphContainer);
        contentPanel.add(Box.createVerticalStrut(5));

        JScrollPane scrollPanel = new JScrollPane(contentPanel);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ScrollBarHelper.applySlimScrollBar(scrollPanel, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        scrollPanel.setBorder(null);
        scrollPanel.getViewport().setBackground(Color.WHITE);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPanel, BorderLayout.CENTER);
    }

    public JPanel createHeatMap() throws SQLException {
        RequestScheduleDAO dao = new RequestScheduleDAO();
        double[][] data = dao.getWeeklyTimeSlotData();

        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] slots = {"7am-9am", "9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm", "5pm-7pm"};

        JPanel heatMapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int marginLeft = 50;
                int marginTop = 30;
                int width = getWidth() - marginLeft;
                int height = getHeight() - marginTop;
                int rows = data.length;
                int cols = data[0].length;
                int cellWidth = width / cols;
                int cellHeight = height / rows;
                double maxValue = 10.0;

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        int x = marginLeft + j * cellWidth;
                        int y = marginTop + i * cellHeight;
                        double value = data[i][j];
                        double intensity = Math.min(1.0, value / maxValue);
                        int alpha = (int) (intensity * 255);

                        g.setColor(new Color(91, 112, 121, alpha));
                        g.fillRect(x, y, cellWidth, cellHeight);

                        g.setColor(Color.BLACK);
                        String val = String.format("%.0f", value);
                        FontMetrics fm = g.getFontMetrics();
                        int textX = x + (cellWidth - fm.stringWidth(val)) / 2;
                        int textY = y + (cellHeight + fm.getAscent()) / 2 - 2;
                        g.drawString(val, textX, textY);

                        g.setColor(Color.GRAY);
                        g.drawRect(x, y, cellWidth, cellHeight);
                    }
                }

                g.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g.setColor(Color.BLACK);

                for (int i = 0; i < rows; i++) {
                    g.drawString(days[i], 10, marginTop + i * cellHeight + cellHeight / 2 + 5);
                }

                for (int j = 0; j < cols; j++) {
                    int x = marginLeft + j * cellWidth + cellWidth / 2;
                    int textWidth = g.getFontMetrics().stringWidth(slots[j]);
                    g.drawString(slots[j], x - textWidth / 2, 20);
                }
            }
        };

        heatMapPanel.setBackground(Color.WHITE);
        heatMapPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        heatMapPanel.setPreferredSize(new Dimension(350, 200));

        return heatMapPanel;
    }

    public JPanel createLineGraph(String scheduledDay) throws SQLException {
        RequestScheduleDAO dao = new RequestScheduleDAO();
        double[] counts = dao.getTimeSlotDataForDay(scheduledDay);

        JPanel lineGraphPanel = new JPanel();
        lineGraphPanel.setBackground(Color.WHITE);
        lineGraphPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lineGraphPanel.setPreferredSize(new Dimension(350, 200));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String seriesName = "requests";

        String[] timeSlots = {"7am-9am", "9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm", "5pm-7pm"};

        for (int i = 0; i < counts.length; i++) {
            dataset.addValue(counts[i], seriesName, timeSlots[i]);
        }

        JFreeChart lineChart = ChartFactory.createLineChart("Peak Schedule Hours",
                "Time of Day", "Frequency", dataset,
                PlotOrientation.VERTICAL, false, false, false
        );

        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(Color.BLACK);
        plot.setOutlineStroke(new BasicStroke(1.5f));
        plot.setRangeGridlinesVisible(false);

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0, 100);
        yAxis.setTickUnit(new NumberTickUnit(20));
        yAxis.setAxisLineStroke(new BasicStroke(1.5f));
        yAxis.setTickMarksVisible(true);
        yAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setAxisLineStroke(new BasicStroke(1.5f));
        xAxis.setTickMarksVisible(true);
        xAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(91, 112, 121));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, true);

        lineChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 17));
        lineChart.getTitle().setPaint(new Color(91, 112, 121));

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(400, 250));
        chartPanel.setBackground(Color.WHITE);

        lineGraphPanel.setLayout(new BorderLayout());
        lineGraphPanel.add(chartPanel, BorderLayout.CENTER);

        return lineGraphPanel;
    }

    private JPanel loadCalendar() {
        dates = RequestHistory.loadWeek();
        trimmedDates = RequestHistory.handleTrimmingDates(dates, 2);
        day = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

        JPanel calendarPanel = new JPanel(new GridLayout(1, day.size(), 10, 0));
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.setPreferredSize(new Dimension(0, 90));
        calendarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 83));
        calendarPanel.setMinimumSize(new Dimension(0, 83));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        for (int i = 0; i < trimmedDates.size(); i++) {
            String dateString = dates.get(i);

            RoundedPanel clickableDate = new RoundedPanel(55, 2, new Color(91, 112, 121));
            clickableDate.setLayout(new BoxLayout(clickableDate, BoxLayout.Y_AXIS));
            clickableDate.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            clickableDate.setPreferredSize(new Dimension(0, 75));

            JLabel dateLabel = new JLabel(trimmedDates.get(i), SwingConstants.CENTER);
            RoundedLabel roundedDate = new RoundedLabel(dateLabel, 1, Color.WHITE, 35);
            roundedDate.setBackground(Color.WHITE);
            roundedDate.setPreferredSize(new Dimension(33, 35));
            roundedDate.setMaximumSize(new Dimension(33, 35));
            roundedDate.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel dayLabel = new JLabel(day.get(i), SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayLabel.setForeground(Color.WHITE);
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            clickableDate.add(Box.createVerticalStrut(3));
            clickableDate.add(roundedDate);
            clickableDate.add(Box.createVerticalStrut(10));
            clickableDate.add(dayLabel);
            clickableDate.setCursor(new Cursor(Cursor.HAND_CURSOR));
            clickableDate.setBackground(new Color(117, 144, 156));
            clickableDate.setOpaque(false);

            // highlight today's date
            if (dateTodayIdx == i && selectedDatePanel == null) {
                clickableDate.setBackground(new Color(255, 227, 85));
                dayLabel.setForeground(Color.BLACK);
                selectedDatePanel = clickableDate;
                selectedDayLabel = dayLabel;

                // Load initial line graph for today's day
                try {
                    lineGraphContainer.removeAll();
                    lineGraphContainer.add(createLineGraph(day.get(i)), BorderLayout.CENTER);
                    lineGraphContainer.revalidate();
                    lineGraphContainer.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            final RoundedPanel thisDatePanel = clickableDate;
            final JLabel thisDayLabel = dayLabel;
            final String thisSelectedDateString = dateString;
            final String dayOfWeek = day.get(i);

            clickableDate.addMouseListener(new MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (selectedDatePanel != null && selectedDatePanel != thisDatePanel) {
                        selectedDatePanel.setBackground(new Color(117, 144, 156));
                        selectedDayLabel.setForeground(Color.WHITE);
                    }

                    thisDatePanel.setBackground(new Color(255, 227, 85));
                    thisDayLabel.setForeground(Color.BLACK);
                    selectedDatePanel = thisDatePanel;
                    selectedDayLabel = thisDayLabel;
                    dateSelected = thisSelectedDateString;

                    // Load line graph for selected day
                    try {
                        lineGraphContainer.removeAll();
                        lineGraphContainer.add(createLineGraph(dayOfWeek), BorderLayout.CENTER);
                        lineGraphContainer.revalidate();
                        lineGraphContainer.repaint();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            calendarPanel.add(clickableDate);
        }

        return calendarPanel;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Peak Scheduling Hours");
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));

        JLabel weekSpan = new JLabel(weekRange);
        weekSpan.setAlignmentX(CENTER_ALIGNMENT);
        weekSpan.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel visualTitle = new JLabel("Heatmap of Peak Scheduling Hours");
        visualTitle.setAlignmentX(CENTER_ALIGNMENT);
        visualTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(1));
        topPanel.add(weekSpan);
        topPanel.add(Box.createVerticalStrut(1));
        topPanel.add(visualTitle);

        return topPanel;
    }

    private JPanel createHeatmapLegend() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        legendPanel.setBackground(Color.WHITE);

        JLabel legendTitle = new JLabel("Legends: ");
        legendTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        legendPanel.add(legendTitle);

        int[][] range = {{0, 5}, {6, 10}, {11, 15}};
        String[] labels = {"0-5 requests", "6-10 requests", "11-15 requests"};

        for (int i = 0; i < range.length; i++) {
            int avgValue = (range[i][0] + range[i][1]) / 2;
            double intensity = Math.min(1.0, avgValue / 15.0);
            int alpha = (int) (intensity * 255);

            JPanel colorBox = new JPanel();
            colorBox.setPreferredSize(new Dimension(10, 10));
            colorBox.setMaximumSize(new Dimension(10, 10));
            colorBox.setBackground(new Color(91, 112, 121, alpha));
            colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel rangeLabel = new JLabel(labels[i]);
            rangeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            legendPanel.add(colorBox);
            legendPanel.add(rangeLabel);

            if (i < range.length - 1) {
                legendPanel.add(Box.createHorizontalStrut(10));
            }
        }
        return legendPanel;
    }

    public String getPeakTimeSlotOfWeek() {
        try {
            RequestScheduleDAO dao = new RequestScheduleDAO();

            String[] dayAbbrevs = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            String[] timeSlots = {
                "7:00 AM - 9:00 AM",
                "9:00 AM - 11:00 AM",
                "11:00 AM - 1:00 PM",
                "1:00 PM - 3:00 PM",
                "3:00 PM - 5:00 PM",
                "5:00 PM - 7:00 PM"
            };

            int peakDayIndex = 0;
            int peakTimeIndex = 0;
            double peakCount = -1;

            // loop through all days
            for (int d = 0; d < dayAbbrevs.length; d++) {
                double[] counts = dao.getTimeSlotDataForDay(dayAbbrevs[d]);

                // find peak slot for this day
                for (int t = 0; t < counts.length; t++) {
                    if (counts[t] > peakCount) {
                        peakCount = counts[t];
                        peakDayIndex = d;
                        peakTimeIndex = t;
                    }
                }
            }

            if (peakCount <= 0) {
                return "No Requests This Week";
            }

            // convert day abbreviation to full name
            String fullDay = switch (dayAbbrevs[peakDayIndex]) {
                case "Mon" ->
                    "Monday";
                case "Tue" ->
                    "Tuesday";
                case "Wed" ->
                    "Wednesday";
                case "Thu" ->
                    "Thursday";
                case "Fri" ->
                    "Friday";
                case "Sat" ->
                    "Saturday";
                default ->
                    dayAbbrevs[peakDayIndex];
            };

            return fullDay + " " + timeSlots[peakTimeIndex];

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error retrieving peak time";
        }
    }
}
