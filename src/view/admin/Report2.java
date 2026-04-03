package view.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
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
import java.util.function.Consumer;
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

    // Callbacks
    private Consumer<Boolean> onToggleChanged;
    private Consumer<String> onBuildingSelected;
    private Consumer<Boolean> onReportToggleChanged;
    private Runnable onBackClicked;

    // UI Components that need to be accessed by setters
    private JLabel selected; // Period label
    private JLabel repSelected; // Report panel period label
    private JLabel tableTitle; // Frequency table title
    private JLabel countHeader; // Frequency table column header
    private JPanel dataPanel; // Room frequency data panel
    private JPanel content; // Building list content panel
    private RoundedToggleSwitch toggle;
    private RoundedToggleSwitch reportToggle;

    // Original fields
    String[] mostReqMonthly, mostReqWeekly;
    JPanel pagePanel, showBuildings, topWrapper, reportPanel, topReportWrapper, overview, frequencyTable;
    JScrollPane mainScrollPane, reportPane;
    JLabel header, buildingName;
    RoundedPanel building, overviewTable, reportTable;
    RoundedProgressBar progress;
    GridBagConstraints bname, progBar, totalReq, gbcArrow;
    CardLayout cardLayout;

    public Report2() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        pagePanel = new JPanel(cardLayout);

        showBuildings = new JPanel();
        showBuildings.setLayout(new BorderLayout());

        toggle = new RoundedToggleSwitch(30, 2);
        toggle.setPreferredSize(new Dimension(350, 30));
        toggle.setMaximumSize(new Dimension(200, 30));

        // Store reference for later
        this.toggle = toggle;

        header = new JLabel("MOST REQUESTED BUILDINGS");
        header.setFont(new Font("Arial", Font.PLAIN, 15));

        selected = new JLabel("This Month");
        selected.setFont(new Font("Arial", Font.BOLD, 15));

        // Store reference
        this.selected = selected;

        toggle.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selected.setText("This Week");
            } else {
                selected.setText("This Month");
            }
            if (onToggleChanged != null) {
                onToggleChanged.accept(e.getStateChange() == ItemEvent.SELECTED);
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

        // Initialize with empty content - will be populated by setBuildingData
        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentY(Component.TOP_ALIGNMENT);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Store reference
        this.content = content;

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

    // ==================== SETTER METHODS ====================
    
    public void setOnToggleChanged(Consumer<Boolean> callback) {
        this.onToggleChanged = callback;
        // Attach listener to the toggle if it exists
        if (toggle != null) {
            toggle.addItemListener(e -> {
                if (onToggleChanged != null) {
                    onToggleChanged.accept(e.getStateChange() == ItemEvent.SELECTED);
                }
            });
        }
    }

    public void setOnBuildingSelected(Consumer<String> callback) {
        this.onBuildingSelected = callback;
    }

    public void setOnReportToggleChanged(Consumer<Boolean> callback) {
        this.onReportToggleChanged = callback;
        // Attach listener to the report toggle if it exists
        if (reportToggle != null) {
            reportToggle.addItemListener(e -> {
                if (onReportToggleChanged != null) {
                    onReportToggleChanged.accept(e.getStateChange() == ItemEvent.SELECTED);
                }
            });
        }
    }

    public void setOnBackClicked(Runnable callback) {
        this.onBackClicked = callback;
    }

    public void setBuildingData(String[] buildingNames, int[] requestCounts) {
        content.removeAll();

        if (buildingNames != null && requestCounts != null) {
            for (int i = 0; i < buildingNames.length; i++) {
                content.add(createBuildingCard(buildingNames[i], requestCounts[i]));
                content.add(Box.createVerticalStrut(10));
            }
            content.setPreferredSize(new Dimension(400, buildingNames.length * 90));
        }

        content.revalidate();
        content.repaint();
    }

    public void setSelectedPeriod(String period) {
        selected.setText(period);
        if (repSelected != null) {
            repSelected.setText(period);
        }
    }

    public void setReportData(String period, String buildingName, int totalRequests,
            int approved, int declined, int voided, String[][] roomData) {
        // Update period and building name
        if (repSelected != null) {
            repSelected.setText(period);
        }

        // Update overview table values
        if (overview != null) {
            updateOverviewTable(totalRequests, approved, declined, voided);
        }

        // Update room frequency data
        setRoomFrequencyData(roomData);
    }

    public void setReportTableTitle(String title) {
        if (tableTitle != null) {
            tableTitle.setText(title);
        }
    }

    public void setReportColumnHeader(String header) {
        if (countHeader != null) {
            countHeader.setText(header);
        }
    }

    public void setRoomFrequencyData(String[][] data) {
        if (dataPanel == null) return;
        
        dataPanel.removeAll();
        
        if (data != null && data.length > 0) {
            // Recalculate layout
            int rows = data.length;
            dataPanel.setLayout(new GridLayout(rows, 2, 10, 8));
            dataPanel.setMaximumSize(new Dimension(360, rows * 28));
            dataPanel.setPreferredSize(new Dimension(360, rows * 28));
            
            for (String[] row : data) {
                JLabel building = new JLabel(row[0], SwingConstants.CENTER);
                building.setFont(new Font("Arial", Font.PLAIN, 14));
                building.setForeground(new Color(91, 112, 121));
                
                JLabel count = new JLabel(row[1]);
                count.setFont(new Font("Arial", Font.BOLD, 14));
                count.setHorizontalAlignment(SwingConstants.CENTER);

                dataPanel.add(building);
                dataPanel.add(count);
            }
        }
        
        dataPanel.revalidate();
        dataPanel.repaint();
        
        Container parent = dataPanel.getParent();
        if (parent != null) {
            int height = 80 + (data.length * 30);
            parent.setMaximumSize(new Dimension(400, height));
            parent.setPreferredSize(new Dimension(400, height));
            parent.revalidate();
            parent.repaint();
        }
    }

    public void showReportPanel() {
        cardLayout.show(pagePanel, "Panel2");
    }

    public void showBuildingList() {
        cardLayout.show(pagePanel, "Panel1");
    }

    // ==================== HELPER METHODS ====================
    
    private void updateOverviewTable(int total, int approved, int declined, int voided) {
        // Remove all and rebuild with new values
        overview.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel numberLabel = new JLabel(String.valueOf(total));
        numberLabel.setFont(new Font("Arial", Font.BOLD, 48));
        numberLabel.setForeground(new Color(91, 112, 121));
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        overview.add(numberLabel, gbc);

        gbc.gridy++;
        JLabel totalLabel = new JLabel("TOTAL REQUESTS");
        totalLabel.setForeground(new Color(91, 112, 121));
        totalLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        overview.add(totalLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        overview.add(new JLabel("APPROVED") {
            {
                setForeground(new Color(91, 112, 121));
            }
        }, gbc);
        gbc.gridx = 1;
        overview.add(new JLabel(approved + " (" + calculatePercent(approved, total) + "%)"), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        overview.add(new JLabel("DECLINED") {
            {
                setForeground(new Color(91, 112, 121));
            }
        }, gbc);
        gbc.gridx = 1;
        overview.add(new JLabel(declined + " (" + calculatePercent(declined, total) + "%)"), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        overview.add(new JLabel("VOID") {
            {
                setForeground(new Color(91, 112, 121));
            }
        }, gbc);
        gbc.gridx = 1;
        overview.add(new JLabel(voided + " (" + calculatePercent(voided, total) + "%)"), gbc);

        overview.revalidate();
        overview.repaint();
    }

    private int calculatePercent(int value, int total) {
        if (total == 0) {
            return 0;
        }
        return (int) Math.round((value * 100.0) / total);
    }

    private RoundedPanel createBuildingCard(String name, int requests) {
        RoundedPanel card = new RoundedPanel(50, 3, new Color(117, 144, 156));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(400, 70));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setBackground(new Color(243, 244, 247));
        card.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel nameRow = new JPanel();
        nameRow.setLayout(new BoxLayout(nameRow, BoxLayout.X_AXIS));
        nameRow.setOpaque(false);

        JLabel buildingName = new JLabel(name);
        buildingName.setFont(new Font("Arial", Font.BOLD, 25));
        buildingName.setForeground(new Color(91, 112, 121));
        buildingName.setAlignmentX(Component.LEFT_ALIGNMENT);
        buildingName.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        JLabel arrow = new JLabel(">");
        arrow.setFont(new Font("Arial", Font.BOLD, 28));
        arrow.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        nameRow.add(buildingName);
        nameRow.add(Box.createHorizontalGlue());
        nameRow.add(arrow);

        JPanel progressRow = new JPanel();
        progressRow.setLayout(new FlowLayout());
        progressRow.setOpaque(false);

        RoundedProgressBar progress = new RoundedProgressBar(0, 200);
        progress.setValue(requests);
        progress.setPreferredSize(new Dimension(240, 20));
        progress.setMaximumSize(new Dimension(240, 20));

        JLabel totalRequests = new JLabel(requests + " Total Requests");
        totalRequests.setFont(new Font("Segeo UI", Font.PLAIN, 10));
        totalRequests.setForeground(new Color(91, 112, 121));
        totalRequests.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        progressRow.add(progress);
        progressRow.add(totalRequests);

        card.add(nameRow);
        card.add(Box.createVerticalStrut(5));
        card.add(progressRow);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                showReports();
                cardLayout.show(pagePanel, "Panel2");

               
                if (onBuildingSelected != null) {
                    onBuildingSelected.accept(name);
                }
            }
        });
        return card;
    }

    private void showReports() {
        
        reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));

        // top labels
        topReportWrapper = new JPanel();
        topReportWrapper.setLayout(new BoxLayout(topReportWrapper, BoxLayout.Y_AXIS));
        JLabel repHeader = new JLabel("MOST REQUESTED BUILDINGS");
        repHeader.setFont(new Font("Arial", Font.BOLD, 20));
        repHeader.setForeground(new Color(91, 112, 121));

        repSelected = new JLabel("This Month");
        repSelected.setFont(new Font("Arial", Font.BOLD, 18));
        this.repSelected = repSelected;

        JLabel reqOverview = new JLabel("Room Request Distribution");
        reqOverview.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftWrapper.setPreferredSize(new Dimension(400, 25));
        leftWrapper.setMaximumSize(new Dimension(400, 30));

        // Add back button
        JLabel backArrow = new JLabel("<");
        backArrow.setFont(new Font("Arial", Font.BOLD, 24));
        backArrow.setForeground(new Color(91, 112, 121));
        backArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backArrow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (onBackClicked != null) {
                    onBackClicked.run();
                }
            }
        });
        leftWrapper.add(backArrow);
        leftWrapper.add(Box.createHorizontalStrut(10));
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

        overviewTable = new RoundedPanel(30, 1, new Color(91, 112, 121));
        overviewTable.setBackground(new Color(243, 244, 247));
        overviewTable.add(overview);
        overviewTable.setPreferredSize(new Dimension(310, 200));
        overviewTable.setMaximumSize(new Dimension(310, 200));
        overviewTable.setAlignmentX(Component.CENTER_ALIGNMENT);

        // toggle - KEEPING ORIGINAL RoundedToggleSwitch FORMAT
        reportToggle = new RoundedToggleSwitch(30, 2);
        reportToggle.setPreferredSize(new Dimension(350, 30));
        reportToggle.setMaximumSize(new Dimension(350, 30));
        reportToggle.setToggleTexts("REQUESTED", "UTILIZATION");
        reportToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.reportToggle = reportToggle;

        reportToggle.addItemListener(e -> {
            if (onReportToggleChanged != null) {
                onReportToggleChanged.accept(e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        // big table - HARDCODED DEFAULT DATA
        String[][] defaultDataset = {
            {"SDL 1", "40"}, {"SDL 2", "20"}, {"SDL 3", "10"}
        };

        // Main wrapper with vertical layout
        RoundedPanel frequencyWrapper = new RoundedPanel(20, 2, new Color(91, 112, 121));
        frequencyWrapper.setLayout(new BoxLayout(frequencyWrapper, BoxLayout.Y_AXIS));
        frequencyWrapper.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        frequencyWrapper.setBackground(new Color(243, 244, 247));
        frequencyWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        tableTitle = new JLabel("Room Request Distribution");
        tableTitle.setFont(new Font("Arial", Font.PLAIN, 15));
        this.tableTitle = tableTitle;

        JPanel tableTitlewrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tableTitlewrapper.setPreferredSize(new Dimension(400, 25));
        tableTitlewrapper.setMaximumSize(new Dimension(400, 25));
        tableTitlewrapper.setBackground(new Color(243, 244, 247));
        tableTitlewrapper.add(tableTitle);

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(360, 25));
        headerPanel.setPreferredSize(new Dimension(360, 25));

        JLabel buildingHeader = new JLabel("BUILDING", SwingConstants.CENTER);
        buildingHeader.setFont(new Font("Arial", Font.BOLD, 13));
        buildingHeader.setForeground(new Color(91, 112, 121));

        countHeader = new JLabel("REQUESTS", SwingConstants.CENTER);
        countHeader.setFont(new Font("Arial", Font.BOLD, 13));
        countHeader.setForeground(new Color(91, 112, 121));
        countHeader.setHorizontalAlignment(SwingConstants.CENTER);
        this.countHeader = countHeader;

        headerPanel.add(buildingHeader);
        headerPanel.add(countHeader);

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.LIGHT_GRAY);
        separator.setMaximumSize(new Dimension(360, 1));
        separator.setPreferredSize(new Dimension(360, 1));

        // Data panel with hardcoded default data
        int rows = Math.max(defaultDataset.length, 1);
        dataPanel = new JPanel(new GridLayout(rows, 2, 10, 8));
        dataPanel.setOpaque(false);
        dataPanel.setMaximumSize(new Dimension(360, rows * 28));
        dataPanel.setPreferredSize(new Dimension(360, rows * 28));
        this.dataPanel = dataPanel;

        // Add hardcoded data initially
        for (String[] row : defaultDataset) {
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

        int height = 80 + (rows * 30);
        frequencyWrapper.setMaximumSize(new Dimension(400, height));
        frequencyWrapper.setPreferredSize(new Dimension(400, height));

        reportPanel.add(topReportWrapper);
        reportPanel.add(overviewTable);
        reportPanel.add(Box.createVerticalStrut(10));
        reportPanel.add(reportToggle);
        reportPanel.add(Box.createVerticalStrut(10));
        reportPanel.add(frequencyWrapper);

        reportPane = new JScrollPane(reportPanel);
        reportPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        reportPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ScrollBarHelper.applySlimScrollBar(reportPane, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        reportPane.getVerticalScrollBar().setUnitIncrement(16);

        pagePanel.add(reportPane, "Panel2");
        add(pagePanel);
    }
}