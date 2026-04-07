package controller.admin;

import dao.BuildingDAO;
import java.sql.SQLException;
import java.util.List;
import view.admin.Report2;

public class ReportTwoController {

    private Report2 view;
    private BuildingDAO buildingDAO;

    // Current data state
    private boolean isWeeklyView = false;
    private String selectedBuildingCode;
    private String selectedBuildingName;

    // Cached data
    private String[] buildingNames;
    private int[] buildingRequestCounts;
    private int totalRequests;
    private int approvedCount;
    private int declinedCount;
    private int voidCount;
    private String[][] roomData;

    public ReportTwoController() {
        try {
            this.buildingDAO = new BuildingDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    public void loadData() {
        if (buildingDAO == null) {
            return;
        }

        try {
            loadBuildingListData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBuildingListData() throws SQLException {
        List<BuildingDAO.BuildingRequestCount> buildings;

        if (isWeeklyView) {
            buildings = buildingDAO.getMostRequestedBuildingsWeekly();
        } else {
            buildings = buildingDAO.getMostRequestedBuildingsMonthly();
        }

        // Convert to arrays for the view
        buildingNames = new String[buildings.size()];
        buildingRequestCounts = new int[buildings.size()];

        System.out.println("I AM TESTING: " + buildings.size());

        for (int i = 0; i < buildings.size(); i++) {
            BuildingDAO.BuildingRequestCount b = buildings.get(i);
            buildingNames[i] = b.buildingCode + " - " + b.buildingName;
            System.out.println("THIS IS A TEST: " + b.requestCount);
            buildingRequestCounts[i] = b.requestCount;
        }

        // Calculate total
        totalRequests = 0;
        for (int count : buildingRequestCounts) {
            totalRequests += count;
        }
    }

    private void loadBuildingDetails(String buildingCode) throws SQLException {
        if (buildingDAO == null) {
            return;
        }

        // Get status breakdown
        int[] statusCounts;
        if (isWeeklyView) {
            statusCounts = buildingDAO.getBuildingStatusCountsWeekly(buildingCode);
        } else {
            statusCounts = buildingDAO.getBuildingStatusCountsMonthly(buildingCode);
        }

        approvedCount = statusCounts[0];
        declinedCount = statusCounts[1];
        voidCount = statusCounts[2];
    }

    private void loadRoomFrequencyData(String buildingCode) throws SQLException {
        if (buildingDAO == null) {
            return;
        }

        List<BuildingDAO.RoomFrequency> rooms;
        if (isWeeklyView) {
            rooms = buildingDAO.getRoomFrequencyWeekly(buildingCode);
        } else {
            rooms = buildingDAO.getRoomFrequencyMonthly(buildingCode);
        }

        // Convert to String[][] for view [RoomCode, RequestCount]
        roomData = new String[rooms.size()][2];
        for (int i = 0; i < rooms.size(); i++) {
            roomData[i][0] = rooms.get(i).roomName;
            roomData[i][1] = String.valueOf(rooms.get(i).requestCount);
        }
    }

    private void loadRoomUtilizationData(String buildingCode) throws SQLException {
        if (buildingDAO == null) {
            return;
        }

        List<BuildingDAO.RoomUtilization> rooms;
        if (isWeeklyView) {
            rooms = buildingDAO.getRoomUtilizationWeekly(buildingCode);
        } else {
            rooms = buildingDAO.getRoomUtilizationMonthly(buildingCode);
        }

        // Convert to String[][] for view [RoomCode, Utilization%]
        roomData = new String[rooms.size()][2];
        for (int i = 0; i < rooms.size(); i++) {
            roomData[i][0] = rooms.get(i).roomName;
            roomData[i][1] = String.format("%.2f%%", rooms.get(i).utilizationPercent);
        }
    }

    public void initView() {
        this.view = new Report2();

        // Set initial data
        if (buildingNames != null && buildingRequestCounts != null) {
            view.setBuildingData(buildingNames, buildingRequestCounts);
        }

        String periodText = isWeeklyView ? "This Week" : "This Month";
        view.setSelectedPeriod(periodText);

        // Toggle callback for Monthly/Weekly switch (top toggle)
        view.setOnToggleChanged(isWeekly -> {
            this.isWeeklyView = isWeekly;
            try {
                loadBuildingListData();
                view.setBuildingData(buildingNames, buildingRequestCounts);
                view.setSelectedPeriod(isWeekly ? "This Week" : "This Month");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Building selection callback - show panel first, then load data
        view.setOnBuildingSelected(buildingName -> {
            this.selectedBuildingName = buildingName;
            this.selectedBuildingCode = buildingName.split(" - ")[0];

            // Show panel immediately with default data
            view.showReportPanel();

            // Then load real data asynchronously
            try {
                loadBuildingDetails(selectedBuildingCode);
                String period = isWeeklyView ? "March 8 - March 14" : "FEBRUARY";

                // Load initial view (Request Frequency)
                loadRoomFrequencyData(selectedBuildingCode);
                totalRequests = approvedCount + declinedCount + voidCount;
                view.setReportData(period, selectedBuildingName, totalRequests,
                        approvedCount, declinedCount, voidCount, roomData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Report toggle callback (REQUESTED / UTILIZATION) - bottom toggle
        view.setOnReportToggleChanged(isUtilization -> {
            if (isUtilization) {
                // Switch to UTILIZATION view
                view.setReportTableTitle("Room Utilization");
                view.setReportColumnHeader("UTILIZATION %");
                try {
                    loadRoomUtilizationData(selectedBuildingCode);
                    view.setRoomFrequencyData(roomData);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // Switch to REQUESTED view
                view.setReportTableTitle("Room Request Distribution");
                view.setReportColumnHeader("REQUESTS");
                try {
                    loadRoomFrequencyData(selectedBuildingCode);
                    view.setRoomFrequencyData(roomData);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Back button callback
        view.setOnBackClicked(() -> {
            view.showBuildingList();
        });
    }

    public Report2 getView() {
        return view;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public String getSelectedBuilding() {
        return selectedBuildingName;
    }

    public void refreshData() {
        loadData();
        if (view != null && buildingNames != null) {
            view.setBuildingData(buildingNames, buildingRequestCounts);
        }
    }

    public String getTopBuilding() {
        if (buildingNames == null || buildingRequestCounts == null || buildingNames.length == 0) {
            return null;
        }

        int maxIndex = 0;

        for (int i = 1; i < buildingRequestCounts.length; i++) {
            if (buildingRequestCounts[i] > buildingRequestCounts[maxIndex]) {
                maxIndex = i;
            }
        }

        return buildingNames[maxIndex]; 
    }

    public String getTopBuildingName() {
        String top = getTopBuilding();
        return (top != null) ? top.split(" - ")[1] : "No Requests";
    }
}
