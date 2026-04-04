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

import view.common.RequestHistory;
import view.components.RoundedLabel;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;

public class Report3 extends JPanel{

    private JPanel calendar; 
    List<String> dates, trimmedDates, day;
    private static int dateTodayIdx = -1;
	private static String dateSelected;
    JLabel selectedDayLabel = null;
	RoundedPanel selectedDatePanel = null;

    public Report3() {
        setLayout(new BorderLayout()); 
        setBackground(Color.WHITE); 

        JPanel topPanel = createTopPanel(); 
        add(topPanel, BorderLayout.NORTH);

        // this is the main content panel that holds all the components of the Report3
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(topPanel); 

        RoundedPanel heatMapPanel = new RoundedPanel(20, 2, new Color(117, 144, 156));
        heatMapPanel.setPreferredSize(new Dimension(400,250));
        heatMapPanel.setMaximumSize(new Dimension(400,250));
        heatMapPanel.setLayout(new BorderLayout());
        heatMapPanel.setBackground(Color.WHITE);
        heatMapPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // creating the heatMap panel and adding it to the content panel
        heatMapPanel.add(createHeatMap(), BorderLayout.CENTER);

        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(heatMapPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(createHeatmapLegend());
        contentPanel.add(Box.createVerticalStrut(10));

        JLabel lineChartLbl = new JLabel("Time-of-Day Requests");
        lineChartLbl.setAlignmentX(CENTER_ALIGNMENT);
        lineChartLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // creating the calendar panel and adding it to the content panel
        contentPanel.add(loadCalendar());

        RoundedPanel lineChartPanel = new RoundedPanel(20, 2, new Color(117, 144, 156));
        lineChartPanel.setPreferredSize(new Dimension(400,250));
        lineChartPanel.setMaximumSize(new Dimension(400,250));
        lineChartPanel.setLayout(new BorderLayout());
        lineChartPanel.setBackground(Color.WHITE);
        lineChartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lineChartPanel.add(createLineGraph(), BorderLayout.CENTER);

        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lineChartLbl);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(calendar);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lineChartPanel);
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

    public JPanel createHeatMap() {
    //BACKEND TO DO: Replace with actual database data
    double[][] data = {
        {3.0, 5.0, 2.0, 1.0, 0.0, 1.0}, // Mon
        {4.0, 8.0, 3.0, 2.0, 1.0, 0.0}, // Tue
        {2.0, 6.0, 4.0, 3.0, 1.0, 0.0}, // Wed
        {1.0, 4.0, 3.0, 2.0, 1.0, 1.0}, // Thu
        {0.0, 3.0, 2.0, 2.0, 1.0, 0.0}, // Fri
        {0.0, 1.0, 1.0, 1.0, 0.0, 0.0}  // Sat
    };

    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    String[] slots = {"7am-9am", "9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm", "5pm-7pm"};

    JPanel heatMapPanel = new JPanel() {
        @Override
        // this is where the heatmap is drawn, using the data array for values and the days and slots arrays for labels
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int marginLeft = 50; // this is to create space for the day labels on the left
            int marginTop = 30; // this is to create space for the time slot labels at the top
            int width = getWidth() - marginLeft;
            int height = getHeight() - marginTop;
            int rows = data.length;
            int cols = data[0].length;
            int cellWidth = width / cols;
            int cellHeight = height / rows;
            double maxValue = 10.0; // for color intensity scaling, can be dynamically calculated from data
            // Draw cells
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // change logic for color intensity here to percentage of transparency 
                    // based on max value in the data set (currently hardcoded to 10)
                    int x = marginLeft + j * cellWidth;
                    int y = marginTop + i * cellHeight;
                    double value = data[i][j];
                    double intensity = Math.min(1.0, value/maxValue);
                    int alpha = (int)(intensity * 255);

                    //blue gradient based on intensity (the transparency)
                    g.setColor(new Color(91, 112, 121, alpha)); // this is the color for the cell, with alpha for transparency
                    g.fillRect(x, y, cellWidth, cellHeight); // fill the cell with the color
                    
                    g.setColor(Color.BLACK);
                    String val = String.format("%.0f", value); // this is tto show the whole number part only
                    FontMetrics fm = g.getFontMetrics();
                    int textX = x + (cellWidth - fm.stringWidth(val)) / 2;
                    int textY = y + (cellHeight + fm.getAscent()) / 2 - 2;
                    g.drawString(val, textX, textY);
                    
                    // Cell border
                    g.setColor(Color.GRAY);
                    g.drawRect(x, y, cellWidth, cellHeight);
                }
            }

            // Draw labels
            // Draw day labels on the left, centered within each row
            g.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g.setColor(Color.BLACK);
            for (int i = 0; i < rows; i++) {
                // adjust y position to be centered within the cell and to account for font height
                g.drawString(days[i], 10, marginTop + i * cellHeight + cellHeight / 2 + 5);
            }

            // Draw time slot labels at the top, centered within each column
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

    public JPanel createLineGraph(){
        //BACKEND TO DO: Implement line graph generation logic here, using data from the database
        JPanel lineGraphPanel = new JPanel(); 
        lineGraphPanel.setBackground(Color.WHITE);
        lineGraphPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lineGraphPanel.setPreferredSize(new Dimension(350, 200)); //test line s

        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
        String seriesName = "requests"; 
        //BACKEND TO DO: Replace with actual database data (loops), this is for testing only 
        dataset.addValue(3, seriesName, "7am-9am");
        dataset.addValue(5, seriesName, "9am-11am");
        dataset.addValue(2, seriesName, "11am-1pm");
        dataset.addValue(1, seriesName, "1pm-3pm");
        dataset.addValue(0, seriesName, "3pm-5pm");
        dataset.addValue(1, seriesName, "5pm-7pm");

        JFreeChart lineChart = ChartFactory.createLineChart("Peak Schedule Hours",
            "Time of Day", "Frequency", dataset, 
            PlotOrientation.VERTICAL, false, false, false
        ); 
        
        // this part is for customization of the line chart's appearance
        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE); 
        plot.setOutlinePaint(Color.BLACK); 
        plot.setOutlineStroke(new BasicStroke(1.5f)); 
        plot.setRangeGridlinesVisible(false); 

        // this part is for customizing the y-axis to show percentage values and 
        // to set the range from 0 to 100 with ticks every 20 units 
        org.jfree.chart.axis.NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(0, 100);
        yAxis.setTickUnit(new NumberTickUnit(20));
        yAxis.setAxisLineStroke(new BasicStroke(1.5f)); 
        yAxis.setTickMarksVisible(true);
        yAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        // customize x-axis to show time slots and to rotate labels for better readability
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setAxisLineStroke(new BasicStroke(1.5f));
        xAxis.setTickMarksVisible(true);
        xAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // tilting the texts so it shows the whole thing

        // customize the line renderer to change the line color, thickness, and to show data points
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(91, 112, 121));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f)); 
        renderer.setSeriesShapesVisible(0, true);

        // customize the chart title
        lineChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 17));
        lineChart.getTitle().setPaint(new Color(91, 112, 121));

        // create a ChartPanel to display the chart 
        ChartPanel chartPanel = new ChartPanel(lineChart);        
        chartPanel.setPreferredSize(new Dimension(400, 250));
        chartPanel.setBackground(Color.WHITE);

        lineGraphPanel.setLayout(new BorderLayout());
        lineGraphPanel.add(chartPanel, BorderLayout.CENTER);

        return lineGraphPanel; 
    }

    private JPanel loadCalendar(){
        dates = RequestHistory.loadWeek();
		trimmedDates = RequestHistory.handleTrimmingDates(dates, 2);
        day = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

        calendar = new JPanel(new GridLayout(1, day.size(), 10, 0));
        calendar.setBackground(Color.WHITE);
		calendar.setPreferredSize(new Dimension(0, 90));
		calendar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 83));
		calendar.setMinimumSize(new Dimension(0, 83));
        calendar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

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

			if (dateTodayIdx == i && selectedDatePanel == null) {
				clickableDate.setBackground(new Color(255, 227, 85));
				dayLabel.setForeground(Color.BLACK);
				selectedDatePanel = clickableDate;
				selectedDayLabel = dayLabel;
			}

			final RoundedPanel thisDatePanel = clickableDate;
			final JLabel thisDayLabel = dayLabel;
			final String thisSelectedDateString = dateString;

			clickableDate.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (selectedDatePanel != null && selectedDatePanel != thisDatePanel) {
						selectedDatePanel.setBackground(new Color(117, 144, 156));
						selectedDayLabel.setForeground(Color.WHITE);
					}

					thisDatePanel.setBackground(new Color(255, 227, 85));
					thisDayLabel.setForeground(Color.BLACK);
					selectedDatePanel = thisDatePanel;
					selectedDayLabel = thisDayLabel;
					dateSelected = thisSelectedDateString;
                    
                    //BACKEND TO DO: Implements logics for line graph
				}
			});

			calendar.add(clickableDate);
		}
        return calendar; 
    }

    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,10)); 
        
        JLabel titleLabel = new JLabel("Peak Scheduling Hours"); 
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        
        //BACKEND TO DO: Get actual date from database
        JLabel weekSpan = new JLabel("March 16 - March 21"); 
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

    private JPanel createHeatmapLegend(){
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,5));
        legendPanel.setBackground(Color.WHITE);

        JLabel legendTitle = new JLabel("Legends: "); 
        legendTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        legendPanel.add(legendTitle);

        int[][] range = {{0,5}, {6,10}, {11,15}};
        String[] labels = {"0-5 requests", "6-10 requests", "11-15 requests"};

        for(int i=0; i<range.length; i++){
            int avgValue = (range[i][0] + range[i][1]) / 2;
            double intensity = Math.min(1.0, avgValue/15.0); //15 kasi yung max ngayon peor you can adjust it
            int alpha = (int)(intensity * 255);

            JPanel colorBox = new JPanel();
            colorBox.setPreferredSize(new Dimension(10,10));
            colorBox.setMaximumSize(new Dimension(10,10)); 
            colorBox.setBackground(new Color(91, 112, 121, alpha));
            colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel rangeLabel = new JLabel(labels[i]);
            rangeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            legendPanel.add(colorBox);
            legendPanel.add(rangeLabel);

            if(i < range.length - 1){
                legendPanel.add(Box.createHorizontalStrut(10));
            }
        }
        return legendPanel;
    }
}
