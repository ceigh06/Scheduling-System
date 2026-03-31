package view.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import view.components.RoundedPanel;
import view.components.RoundedProgressBar;
import view.components.RoundedToggleSwitch;
import view.components.ScrollBarHelper;

public class Report2 extends JPanel {

	String[] mostReqMonthly, mostReqWeekly;
	JPanel pagePanel, showBuildings, topWrapper, content, reportPanel, topReportWrapper, overview, frequencyTable;
	JScrollPane mainScrollPane, reportPane;
	RoundedToggleSwitch toggle, reportToggle;
	JLabel header, selected, buildingName;
	RoundedPanel building, overviewTable, reportTable;
	RoundedProgressBar progress;
	GridBagConstraints bname, progBar, totalReq, gbcArrow;
	CardLayout cardLayout;

	public Report2() {
		setLayout(new BorderLayout());
		// landing panel = contains yung buong R2.1

		cardLayout = new CardLayout();
		pagePanel = new JPanel(cardLayout);

		showBuildings = new JPanel();
		showBuildings.setLayout(new BorderLayout());

		toggle = new RoundedToggleSwitch(30, 2);
		toggle.setPreferredSize(new Dimension(350, 30));
		toggle.setMaximumSize(new Dimension(200, 30));

		header = new JLabel("MOST REQUESTED BUILDINGS");
		header.setFont(new Font("Arial", Font.PLAIN, 15));

		selected = new JLabel("FEBRUARY");
		selected.setFont(new Font("Arial", Font.BOLD, 15));

		toggle.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// This means "Weekly" was chosen
				selected.setText("March 8 - March 14");

			} else {
				selected.setText("FEBRUARY");
			}
		});

		topWrapper = new JPanel();
		topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));

		toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
		header.setAlignmentX(Component.CENTER_ALIGNMENT);
		selected.setAlignmentX(Component.CENTER_ALIGNMENT);

		topWrapper.add(Box.createVerticalStrut(10));
		topWrapper.add(toggle);
		topWrapper.add(Box.createVerticalStrut(5));
		topWrapper.add(header);
		topWrapper.add(Box.createVerticalStrut(3));
		topWrapper.add(selected);
		topWrapper.add(Box.createVerticalStrut(10));

		showBuildings.add(topWrapper, BorderLayout.NORTH);
		String[] buildings = { "PH - Pimentel Hall", "MH - Mendoza Hall", "CH - Chuchu", "MH - Mendoza Hall",
				"PH - Pimentel Hall", "MH - Mendoza Hall", "CH - Chuchu", "MH - Mendoza Hall" };
		int[] requests = { 170, 95, 60, 40, 170, 95, 60, 40 };
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setAlignmentY(Component.TOP_ALIGNMENT);
		content.setPreferredSize(new Dimension(400, buildings.length * 90));
		content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// to print out the building pannels
//		BACKEND TO DO: Make a loop where the hightest is the first one to appear always
		// parang raning ganernch
		for (int i = 0; i < buildings.length; i++) {
			content.add(createBuildingCard(buildings[i], requests[i]));
			content.add(Box.createVerticalStrut(10));
		}

		showBuildings.add(content, BorderLayout.CENTER);

		mainScrollPane = new JScrollPane(showBuildings);
		mainScrollPane.setHorizontalScrollBarPolicy(mainScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainScrollPane.setVerticalScrollBarPolicy(mainScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ScrollBarHelper.applySlimScrollBar(mainScrollPane, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

		pagePanel.add(mainScrollPane, "Panel1");
		showReports();
		add(pagePanel);
	}

	private RoundedPanel createBuildingCard(String name, int requests) {
		RoundedPanel card = new RoundedPanel(50, 3, new Color(117, 144, 156));
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(400, 70));
		card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
		card.setAlignmentX(Component.CENTER_ALIGNMENT);
		card.setBackground(new Color(243, 244, 247));
		card.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		// Row for building name + arrow
		JPanel nameRow = new JPanel();
		nameRow.setLayout(new BoxLayout(nameRow, BoxLayout.X_AXIS));
		nameRow.setOpaque(false); // keep rounded background visible

		JLabel buildingName = new JLabel(name);
		buildingName.setFont(new Font("Arial", Font.BOLD, 25));
		buildingName.setForeground(new Color(91, 112 ,121));
		buildingName.setAlignmentX(Component.LEFT_ALIGNMENT);
		buildingName.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

		JLabel arrow = new JLabel(">");
		arrow.setFont(new Font("Arial", Font.BOLD, 28)); // bigger arrow
		arrow.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		nameRow.add(buildingName);
		nameRow.add(Box.createHorizontalGlue()); // pushes arrow to far right
		nameRow.add(arrow);

		// Progress bar row
		JPanel progressRow = new JPanel();
		progressRow.setLayout(new FlowLayout());
		progressRow.setOpaque(false);

		RoundedProgressBar progress = new RoundedProgressBar(0, 200);
		progress.setValue(requests);
		progress.setPreferredSize(new Dimension(240, 20));
		progress.setMaximumSize(new Dimension(240, 20));

		JLabel totalRequests = new JLabel(requests + " Total Requests");
		totalRequests.setFont(new Font("Segeo UI", Font.PLAIN, 10));
		totalRequests.setForeground(new Color(91, 112 ,121));
		totalRequests.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		progressRow.add(progress);
		progressRow.add(totalRequests);

		// Add rows to card
		card.add(nameRow);
		card.add(Box.createVerticalStrut(5));
		card.add(progressRow);

		card.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				showReports();
				cardLayout.show(pagePanel, "Panel2");
			}

		});
		return card;
	}

	private void showReports() {
		// wrapper for all elements
		reportPanel = new JPanel();
		reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
		reportPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));

		// top labels
		topReportWrapper = new JPanel();
		topReportWrapper.setLayout(new BoxLayout(topReportWrapper, BoxLayout.Y_AXIS));
		JLabel repHeader = new JLabel("MOST REQUESTED BUILDINGS");
		repHeader.setFont(new Font("Arial", Font.BOLD, 20));
		repHeader.setForeground(new Color(91, 112 ,121));
		JLabel repSelected = new JLabel("FEBRUARY");
		repSelected.setFont(new Font("Arial", Font.BOLD, 18));
		JLabel reqOverview = new JLabel("Room Request Distribution");
		reqOverview.setFont(new Font("Arial", Font.PLAIN, 15));

		JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftWrapper.setPreferredSize(new Dimension(400, 25));
		leftWrapper.setMaximumSize(new Dimension(400, 30));
		leftWrapper.add(reqOverview);

		repHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
		repSelected.setAlignmentX(Component.CENTER_ALIGNMENT);

		topReportWrapper.add(Box.createVerticalStrut(5));
		topReportWrapper.add(repHeader);
		topReportWrapper.add(Box.createVerticalStrut(3));
		topReportWrapper.add(repSelected);
		topReportWrapper.add(Box.createVerticalStrut(20));
		topReportWrapper.add(leftWrapper);
		topReportWrapper.add(Box.createVerticalStrut(5));

		// overview table
		overview = new JPanel(new GridBagLayout());
		overview.setPreferredSize(new Dimension(300, 200));
		overview.setAlignmentX(Component.CENTER_ALIGNMENT);
		overview.setMaximumSize(new Dimension(310, 200));
		overview.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;

		JLabel numberLabel = new JLabel("50");
		numberLabel.setFont(new Font("Arial", Font.BOLD, 48));
		numberLabel.setForeground(new Color(91, 112 ,121));
		numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		overview.add(numberLabel, gbc);

		gbc.gridy++;
		JLabel totalLabel = new JLabel("TOTAL REQUESTS");
		totalLabel.setForeground(new Color(91, 112 ,121));
		totalLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		overview.add(totalLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy++;
		overview.add(new JLabel("APPROVED") {
			{setForeground(new Color(91, 112 ,121));}}, gbc);
		gbc.gridx = 1;
		overview.add(new JLabel("25 (50%)"), gbc);//91, 112 ,121

		gbc.gridx = 0;
		gbc.gridy++;
		overview.add(new JLabel("DENIED"){
			{setForeground(new Color(91, 112 ,121));}}, gbc);
		gbc.gridx = 1;
		overview.add(new JLabel("20 (40%)"), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		overview.add(new JLabel("VOID"){
			{setForeground(new Color(91, 112 ,121));}}, gbc);
		gbc.gridx = 1;
		overview.add(new JLabel("5 (10%)"), gbc);

		overviewTable = new RoundedPanel(30, 1, new Color(91, 112, 121));
		overviewTable.setBackground(new Color(243, 244, 247));
		overviewTable.add(overview);
		overviewTable.setPreferredSize(new Dimension(310, 200));
		overviewTable.setMaximumSize(new Dimension(310, 200));
		overviewTable.setAlignmentX(Component.CENTER_ALIGNMENT);

		// toggle
		reportToggle = new RoundedToggleSwitch(30, 2);
		reportToggle.setPreferredSize(new Dimension(350, 30));
		reportToggle.setMaximumSize(new Dimension(350, 30));
		reportToggle.setToggleTexts("REQUESTED", "UTILIZATION");
		reportToggle.setAlignmentX(Component.CENTER_ALIGNMENT);

		// big table
		String[][] dataset = { 
				{ "SDL 1", "40" }, { "SDL 1", "20" }, { "SDL 3", "10" } ,
				{ "SDL 1", "40" }, { "SDL 1", "20" }, { "SDL 3", "10" },
				{ "SDL 1", "40" }, { "SDL 1", "20" }, { "SDL 3", "10" }
				};

		// Main wrapper with vertical layout
		RoundedPanel frequencyWrapper = new RoundedPanel(20, 2, new Color(91, 112, 121));
		frequencyWrapper.setLayout(new BoxLayout(frequencyWrapper, BoxLayout.Y_AXIS));
		frequencyWrapper.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		frequencyWrapper.setBackground(new Color(243, 244, 247));
		frequencyWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel tableTitle = new JLabel("Room Request Distribution");
		tableTitle.setFont(new Font("Arial", Font.PLAIN, 15));
		

		JPanel tableTitlewrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		tableTitlewrapper.setPreferredSize(new Dimension(400, 25));
		tableTitlewrapper.setMaximumSize(new Dimension(400, 25));
		tableTitlewrapper.setBackground(new Color(243, 244, 247));
		tableTitlewrapper.add(tableTitle);
		// Header panel
		JPanel headerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		headerPanel.setOpaque(false);
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

		JLabel buildingHeader = new JLabel("BUILDING", SwingConstants.CENTER);
		buildingHeader.setFont(new Font("Arial", Font.BOLD, 13));
		buildingHeader.setForeground(new Color(91, 112, 121));

		JLabel countHeader = new JLabel("REQUESTS", SwingConstants.CENTER);
		countHeader.setFont(new Font("Arial", Font.BOLD, 13));
		countHeader.setForeground(new Color(91, 112, 121));
		countHeader.setHorizontalAlignment(SwingConstants.CENTER);

		headerPanel.add(buildingHeader);
		headerPanel.add(countHeader);

		// Separator
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

		// Data panel
		JPanel dataPanel = new JPanel(new GridLayout(dataset.length, 2, 10, 8));
		dataPanel.setOpaque(false);

		for (String[] row : dataset) {
			JLabel building = new JLabel(row[0], SwingConstants.CENTER);
			building.setFont(new Font("Arial", Font.PLAIN, 14));
			building.setForeground(new Color(91, 112, 121));
			JLabel count = new JLabel(row[1]);
			count.setFont(new Font("Arial", Font.BOLD, 14));
			count.setHorizontalAlignment(SwingConstants.CENTER);

			dataPanel.add(building);
			dataPanel.add(count);
		}

		// Assemble
		frequencyWrapper.add(tableTitlewrapper);
		frequencyWrapper.add(Box.createVerticalStrut(8));
		frequencyWrapper.add(headerPanel);
		frequencyWrapper.add(Box.createVerticalStrut(8));
		frequencyWrapper.add(separator);
		frequencyWrapper.add(Box.createVerticalStrut(8));
		frequencyWrapper.add(dataPanel);

		// Dynamic height based on rows
		int height = 80 + (dataset.length * 28);
		frequencyWrapper.setMaximumSize(new Dimension(400, height));
		frequencyWrapper.setPreferredSize(new Dimension(400, height));

		reportPanel.add(topReportWrapper);
		reportPanel.add(overviewTable);
		reportPanel.add(Box.createVerticalStrut(10));
		reportPanel.add(reportToggle);
		reportPanel.add(Box.createVerticalStrut(10));
		reportPanel.add(frequencyWrapper);
//main report scrollpane
		reportPane = new JScrollPane(reportPanel);
		reportPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		reportPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ScrollBarHelper.applySlimScrollBar(reportPane, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
		reportPane.getVerticalScrollBar().setUnitIncrement(16);
		
		reportToggle.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// This means "UTILIZATION" was chosen
				tableTitle.setText("Average Room Utilization (%)");
				countHeader.setText("USAGE (%)");
				//BACKEND TO DO: change data values

			} else {
				tableTitle.setText("Room Frequency Distribution");
			}
		});

		pagePanel.add(reportPane, "Panel2");
		add(pagePanel);
	}
}
