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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import model.schedule.RequestSchedule;
import view.components.RoundedLabel;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;

public class RequestHistory extends JPanel {
	JPanel calendar, mainWrapper, requestPanel, wrapper, statusWrapper;
	JLabel dateLabel, dayLabel, status;
	RoundedLabel roundedDate, pfp;
	RoundedPanel clickableDate, statusPanel;
	JScrollPane mainScrollPane;
	Color defaultColor = new Color(117, 144, 156);
	Color selectedColor = new Color(255, 227, 85);
	JLabel selectedDayLabel = null;
	RoundedPanel selectedDatePanel = null;
	List<String> dates, trimmedDates, day;

	private static int dateTodayIdx = -1;
	private static String dateSelected;
	private static List<RequestSchedule> selectedDateRequests = new ArrayList<>();
	private List<RequestSchedule> allRequests = new ArrayList<>();
	private List<List<String>> allCardData = new ArrayList<>();

	public RequestHistory() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
		dates = loadWeek();
		trimmedDates = handleTrimmingDates(dates, 2);
		day = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

		calendar = new JPanel(new GridLayout(1, day.size(), 10, 0));
		calendar.setPreferredSize(new Dimension(0, 90));
		calendar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
		calendar.setMinimumSize(new Dimension(0, 90));

		mainWrapper = new JPanel();
		mainWrapper.setLayout(new BoxLayout(mainWrapper, BoxLayout.Y_AXIS));
		mainWrapper.setOpaque(false);

		mainScrollPane = new JScrollPane(mainWrapper);
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		ScrollBarHelper.applySlimScrollBar(mainScrollPane, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
		mainScrollPane.setBorder(null);

		add(mainScrollPane, BorderLayout.CENTER);
	}

	public void noRequests() {
		JPanel noRequests = new JPanel(new BorderLayout());
		noRequests.setOpaque(false);
		JLabel noReqLabel = new JLabel("No Request Schedule for this day!", SwingConstants.CENTER);
		noReqLabel.setFont(new Font("Arial", Font.BOLD, 16));
		noReqLabel.setForeground(new Color(117, 144, 156));
		noRequests.add(noReqLabel, BorderLayout.CENTER);		  
		mainWrapper.add(noRequests);
	}

	public void newRequest(List<String> requestData) {
		requestPanel = new JPanel(new GridBagLayout());
		requestPanel.setFont(new Font("Arial", Font.BOLD, 20));
		requestPanel.setBackground(new Color(221, 221, 219));

		GridBagConstraints gbcPfp = new GridBagConstraints();
		gbcPfp.gridx = 0;
		gbcPfp.gridy = 0;
		gbcPfp.gridheight = 4;   
		gbcPfp.weightx = 0.1;
		gbcPfp.weighty = 0;             
		gbcPfp.insets = new Insets(5, 5, 0, 0);
		gbcPfp.anchor = GridBagConstraints.NORTH;

		ImageIcon rawIcon = new ImageIcon(getClass().getResource("/resources/images/icons/Profile.png"));
		Image scaled = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		RoundedLabel pfp = new RoundedLabel(new ImageIcon(scaled), 2, new Color(91, 112, 121), 80);
		requestPanel.add(pfp, gbcPfp);
		requestPanel.revalidate();	
		requestPanel.repaint();

		for (int i = 1; i < 5; i++) {
			GridBagConstraints gbcInfo = new GridBagConstraints();
			gbcInfo.gridx = 1;
    		gbcInfo.gridy = i - 1;      
    		gbcInfo.weightx = 0.9;
    		gbcInfo.weighty = 0;         
    		gbcInfo.anchor = GridBagConstraints.WEST;
    		gbcInfo.insets = new Insets(i == 1 ? 5 : 2, 20, 2, 5);  
			if (i == 4) {
				requestPanel.add(new JLabel("Requested at: " + requestData.get(4)) {
					{
						setFont(new Font("Arial", Font.BOLD, 14));
					}
				}, gbcInfo);
			} else {
				if (i == 1) {
					requestPanel.add(new JLabel(requestData.get(i)) {
						{
							setFont(new Font("Arial", Font.BOLD, 20));
						}
					}, gbcInfo);
				} else {
					requestPanel.add(new JLabel(requestData.get(i)) {
						{
							setFont(new Font("Arial", Font.BOLD, 15));
						}
					}, gbcInfo);
				}
			}
		}

		GridBagConstraints gbcRoomCode = new GridBagConstraints();
		gbcRoomCode.gridx = 0;
		gbcRoomCode.gridy = 5;
		gbcRoomCode.anchor = GridBagConstraints.WEST;
		gbcRoomCode.insets = new Insets(15, 15, 0, 5);
		requestPanel.add(new JLabel("ROOM CODE") {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcRoomCode);

		GridBagConstraints gbcRoomData = new GridBagConstraints();
		gbcRoomData.gridx = 0;
		gbcRoomData.gridy = 6;
		gbcRoomData.anchor = GridBagConstraints.WEST;
		gbcRoomData.insets = new Insets(5,15, 15, 5);
		requestPanel.add(new JLabel(requestData.get(5)) {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcRoomData);

		GridBagConstraints gbcTimeLabel = new GridBagConstraints();
		gbcTimeLabel.gridx = 1;
		gbcTimeLabel.gridy = 5;
		gbcTimeLabel.anchor = GridBagConstraints.WEST;
		gbcTimeLabel.insets = new Insets(15, 20, 0, 5);
		requestPanel.add(new JLabel("TIME") {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcTimeLabel);

		GridBagConstraints gbcTimeData = new GridBagConstraints();
		gbcTimeData.gridx = 1;
		gbcTimeData.gridy = 6;
		gbcTimeData.anchor = GridBagConstraints.WEST;
		gbcTimeData.insets = new Insets(5, 20, 15, 5);
		requestPanel.add(new JLabel(requestData.get(6)) {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcTimeData);

		RoundedPanel mainPanel = new RoundedPanel(60, 3, new Color(91, 112, 121), new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.setBackground(new Color(243, 244, 247));
		mainPanel.setPreferredSize(new Dimension(400, 250));
		mainPanel.setMaximumSize(new Dimension(400, 250));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

		requestPanel.setOpaque(false);

		statusPanel = new RoundedPanel(40, 2, new Color(117, 144, 156));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		statusPanel.setPreferredSize(new Dimension(300, 40));
		statusPanel.setMaximumSize(new Dimension(300, 40));
		statusPanel.add(Box.createVerticalStrut(10));

		status = new JLabel();
		status.setAlignmentX(CENTER_ALIGNMENT);
		status.setFont(new Font("Arial", Font.BOLD, 20));

		if (Integer.parseInt(requestData.get(0)) == 3) {
			status.setText("APPROVED");
			statusPanel.setBackground(new Color(63, 193, 127));
			statusPanel.setBorderColor(new Color(77, 139, 78));
		} else {
			status.setText("DENIED");
			statusPanel.setBackground(new Color(255, 100, 100));
			statusPanel.setBorderColor(new Color(227, 75, 75));
		}
		statusPanel.add(status);

		statusWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		statusWrapper.setOpaque(false);
		statusWrapper.add(statusPanel);

		mainPanel.add(statusWrapper, BorderLayout.NORTH);
		mainPanel.add(requestPanel, BorderLayout.CENTER);

		wrapper = new JPanel(new FlowLayout());
		wrapper.setOpaque(false);
		wrapper.add(mainPanel);

		mainWrapper.add(wrapper);
		mainWrapper.revalidate();
		mainWrapper.repaint();
	}

	public void loadRequests(List<RequestSchedule> requests, List<List<String>> cardData) {
		this.allRequests = requests;
		this.allCardData = cardData;

		calendar.removeAll();
		mainWrapper.removeAll();
		mainWrapper.add(calendar);
		mainWrapper.add(Box.createVerticalStrut(5));

		for (int i = 0; i < trimmedDates.size(); i++) {
			String dateString = dates.get(i);

			RoundedPanel clickableDate = new RoundedPanel(55, 2, new Color(91, 112, 121));
			clickableDate.setLayout(new BoxLayout(clickableDate, BoxLayout.Y_AXIS));
			clickableDate.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			clickableDate.setPreferredSize(new Dimension(0, 75));

			JLabel dateLabel = new JLabel(trimmedDates.get(i), SwingConstants.CENTER);
			RoundedLabel roundedDate = new RoundedLabel(dateLabel, 0, Color.WHITE, 35);
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
			clickableDate.setBackground(defaultColor);
			clickableDate.setOpaque(false);

			if (dateTodayIdx == i && selectedDatePanel == null) {
				clickableDate.setBackground(selectedColor);
				dayLabel.setForeground(Color.BLACK);
				selectedDatePanel = clickableDate;
				selectedDayLabel = dayLabel;
			}

			final RoundedPanel thisDatePanel = clickableDate;
			final JLabel thisDayLabel = dayLabel;
			final String thisSelectedDateString = dateString;

			clickableDate.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (selectedDatePanel != null && selectedDatePanel != thisDatePanel) {
						selectedDatePanel.setBackground(defaultColor);
						selectedDayLabel.setForeground(Color.WHITE);
					}

					thisDatePanel.setBackground(selectedColor);
					thisDayLabel.setForeground(Color.BLACK);
					selectedDatePanel = thisDatePanel;
					selectedDayLabel = thisDayLabel;
					dateSelected = thisSelectedDateString;

					renderRequestsForDate(dateSelected);
				}
			});

			calendar.add(clickableDate);
		}

		if (dateTodayIdx != -1) {
			dateSelected = dates.get(dateTodayIdx);
		} else {
			dateSelected = dates.get(0);
		}
		renderRequestsForDate(dateSelected);
	}

	private void renderRequestsForDate(String date) {
		mainWrapper.removeAll();
		mainWrapper.add(calendar);
		mainWrapper.add(Box.createVerticalStrut(5));

		selectedDateRequests = new ArrayList<>();
		List<List<String>> filteredCardData = new ArrayList<>();

		for (int i = 0; i < allRequests.size(); i++) {
			RequestSchedule rs = allRequests.get(i);
			LocalDate reqDate = LocalDate.parse(rs.getDateRequested().split(" ")[0]);
			if (reqDate.equals(LocalDate.parse(date))) {
				selectedDateRequests.add(rs);
				filteredCardData.add(allCardData.get(i));
			}
		}

		if (filteredCardData.isEmpty()) {
			noRequests();
			System.out.println("No requests for " + date);
		} else {
			for (List<String> cardData : filteredCardData) {
				newRequest(cardData);
				System.out.println("erm");
			}
		}

		mainWrapper.revalidate();
		mainWrapper.repaint();
	}

	private List<String> handleTrimmingDates(List<String> dates, int index) {
		List<String> trimmedDates = new ArrayList<>();
		for (String date : dates) {
			trimmedDates.add(date.split("-")[index]);
		}
		return trimmedDates;
	}

	private List<String> loadWeek() {
		LocalDate today = LocalDate.now();
		LocalDate date = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

		List<String> weekDates = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			weekDates.add(date.plusDays(i).toString());
			if (date.plusDays(i).equals(today)) {
				dateTodayIdx = i;
			}
		}
		return weekDates;
	}

	public String getSelectedDate() {
		return dateSelected;
	}

	public List<RequestSchedule> getAppDecRequests() {
		return selectedDateRequests;
	}
}