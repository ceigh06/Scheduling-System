package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Building;
import utilities.DBConnection;

public class BuildingDAO {
    private static Connection connection;
    public BuildingDAO() throws SQLException {
        this.connection = DBConnection.getConnection(); // shared connection
    }
    public Building get(String buildingCode) throws SQLException{
        

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Building WHERE BuildingCode = ?");
        stmt.setString(1, buildingCode);
        ResultSet set = stmt.executeQuery();
        
        set.next(); 

        String name = set.getString("BuildingName");

        Building building = new Building();
        building.load(buildingCode, name);

        return building;
    }

    public List<Building> getAllBuilding() throws SQLException{
        List<Building> allBuildings = new ArrayList<>();


        Statement stmt = connection.createStatement();
        ResultSet set = stmt.executeQuery("SELECT * FROM Building");
        
        while(set.next()){
            Building building = new Building();
            building.load(set.getString("BuildingCode"), set.getString("BuildingName"));
            allBuildings.add(building); 
        }
        
        
        return allBuildings;
    }

     // Get most requested buildings with counts for monthly view
    public List<BuildingRequestCount> getMostRequestedBuildingsMonthly() throws SQLException {
        List<BuildingRequestCount> results = new ArrayList<>();
        
        String sql = "SELECT b.BuildingCode, b.BuildingName, COUNT(*) as RequestCount " +
                     "FROM dbo.RequestSchedule s " +
                     "JOIN dbo.Room r ON s.RoomCode = r.RoomCode " +
                     "JOIN dbo.Building b ON r.BuildingCode = b.BuildingCode " +
                     "WHERE MONTH(s.DateRequested) = MONTH(GETDATE()) " +
                     "AND YEAR(s.DateRequested) = YEAR(GETDATE()) " +
                     "AND s.Status = 3 " +
                     "AND s.IsArchived = 0 " +
                     "GROUP BY b.BuildingCode, b.BuildingName " +
                     "ORDER BY RequestCount DESC";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String code = rs.getString("BuildingCode");
            String name = rs.getString("BuildingName");
            int count = rs.getInt("RequestCount");
            results.add(new BuildingRequestCount(code, name, count));
        }
        return results;
    }
    
    // Get most requested buildings with counts for weekly view
    public List<BuildingRequestCount> getMostRequestedBuildingsWeekly() throws SQLException {
        List<BuildingRequestCount> results = new ArrayList<>();
        
        String sql = "SELECT b.BuildingCode, b.BuildingName, COUNT(*) as RequestCount " +
                     "FROM dbo.RequestSchedule s " +
                     "JOIN dbo.Room r ON s.RoomCode = r.RoomCode " +
                     "JOIN dbo.Building b ON r.BuildingCode = b.BuildingCode " +
                     "WHERE s.DateRequested >= DATEADD(DAY, -7, GETDATE()) " +
                     "AND s.Status = 3 " +
                     "AND s.IsArchived = 0 " +
                     "GROUP BY b.BuildingCode, b.BuildingName " +
                     "ORDER BY RequestCount DESC";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String code = rs.getString("BuildingCode");
            String name = rs.getString("BuildingName");
            int count = rs.getInt("RequestCount");
            results.add(new BuildingRequestCount(code, name, count));
        }
        return results;
    }
    
    // ==================== STATUS BREAKDOWN METHODS ====================
    
    // Get status breakdown for a building (monthly) - [approved, declined, void]
    public int[] getBuildingStatusCountsMonthly(String buildingCode) throws SQLException {
        int[] counts = new int[3];
        
        String sql = "SELECT s.Status, COUNT(*) as Count " +
                     "FROM dbo.RequestSchedule s " +
                     "JOIN dbo.Room r ON s.RoomCode = r.RoomCode " +
                     "WHERE r.BuildingCode = ? " +
                     "AND MONTH(s.DateRequested) = MONTH(GETDATE()) " +
                     "AND YEAR(s.DateRequested) = YEAR(GETDATE()) " +
                     "AND s.Status IN (0, 2, 3) " +
                     "AND s.IsArchived = 0 " +
                     "GROUP BY s.Status";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, buildingCode);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int status = rs.getInt("Status");
            int count = rs.getInt("Count");
            if (status == 3) counts[0] = count;      // Approved
            else if (status == 2) counts[1] = count;  // Declined
            else if (status == 0) counts[2] = count; // Void
        }
        return counts;
    }
    
    // Get status breakdown for a building (weekly)
    public int[] getBuildingStatusCountsWeekly(String buildingCode) throws SQLException {
        int[] counts = new int[3];
        
        String sql = "SELECT s.Status, COUNT(*) as Count " +
                     "FROM dbo.RequestSchedule s " +
                     "JOIN dbo.Room r ON s.RoomCode = r.RoomCode " +
                     "WHERE r.BuildingCode = ? " +
                     "AND s.DateRequested >= DATEADD(DAY, -7, GETDATE()) " +
                     "AND s.Status IN (0, 2, 3) " +
                     "AND s.IsArchived = 0 " +
                     "GROUP BY s.Status";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, buildingCode);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int status = rs.getInt("Status");
            int count = rs.getInt("Count");
            if (status == 3) counts[0] = count;
            else if (status == 2) counts[1] = count;
            else if (status == 0) counts[2] = count;
        }
        return counts;
    }
    
    // ==================== ROOM FREQUENCY METHODS ====================
    
    // Get room frequency distribution for a building (monthly)
    public List<RoomFrequency> getRoomFrequencyMonthly(String buildingCode) throws SQLException {
        List<RoomFrequency> results = new ArrayList<>();
        
        String sql = "SELECT s.RoomCode, COUNT(*) as RequestCount " +
                     "FROM dbo.RequestSchedule s " +
                     "JOIN dbo.Room r ON s.RoomCode = r.RoomCode " +
                     "WHERE r.BuildingCode = ? " +
                     "AND MONTH(s.DateRequested) = MONTH(GETDATE()) " +
                     "AND YEAR(s.DateRequested) = YEAR(GETDATE()) " +
                     "AND s.Status = 3 " +
                     "AND s.IsArchived = 0 " +
                     "GROUP BY s.RoomCode " +
                     "ORDER BY RequestCount DESC";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, buildingCode);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String roomCode = rs.getString("RoomCode");
            int count = rs.getInt("RequestCount");
            results.add(new RoomFrequency(roomCode, count));
        }
        return results;
    }
    
    // Get room frequency distribution for a building (weekly)
    public List<RoomFrequency> getRoomFrequencyWeekly(String buildingCode) throws SQLException {
        List<RoomFrequency> results = new ArrayList<>();
        
        String sql = "SELECT s.RoomCode, COUNT(*) as RequestCount " +
                     "FROM dbo.RequestSchedule s " +
                     "JOIN dbo.Room r ON s.RoomCode = r.RoomCode " +
                     "WHERE r.BuildingCode = ? " +
                     "AND s.DateRequested >= DATEADD(DAY, -7, GETDATE()) " +
                     "AND s.Status = 3 " +
                     "AND s.IsArchived = 0 " +
                     "GROUP BY s.RoomCode " +
                     "ORDER BY RequestCount DESC";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, buildingCode);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String roomCode = rs.getString("RoomCode");
            int count = rs.getInt("RequestCount");
            results.add(new RoomFrequency(roomCode, count));
        }
        return results;
    }
    
   
    
       // Get room utilization for all rooms in a building (current week)
    public List<RoomUtilization> getRoomUtilizationWeekly(String buildingCode) throws SQLException {
        List<RoomUtilization> results = new ArrayList<>();
        
        String sql = "WITH RoomHours AS (" +
                     "    SELECT " +
                     "        r.RoomCode, " +
                     "        ISNULL((SELECT SUM(DATEDIFF(HOUR, ms.TimeIn, ms.TimeOut)) " +
                     "                FROM dbo.MasterSchedule ms " +
                     "                WHERE ms.RoomCode = r.RoomCode " +
                     "                AND ms.ScheduledDay IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday') " +
                     "                AND ms.IsArchived = 0), 0) as MasterHours, " +
                     "        ISNULL((SELECT SUM(DATEDIFF(HOUR, rs.TimeIn, rs.TimeOut)) " +
                     "                FROM dbo.RequestSchedule rs " +
                     "                WHERE rs.RoomCode = r.RoomCode " +
                     "                AND rs.Status = 3 " +
                     "                AND rs.DateRequested >= DATEADD(DAY, -7, GETDATE()) " +
                     "                AND rs.IsArchived = 0), 0) as RequestHours " +
                     "    FROM dbo.Room r " +
                     "    WHERE r.BuildingCode = '" + buildingCode.replace("'", "''") + "' AND r.IsArchived = 0 " +
                     ") " +
                     "SELECT " +
                     "    RoomCode, " +
                     "    (MasterHours + RequestHours) as TotalUsedHours, " +
                     "    78 as AvailableHours, " +
                     "    CASE WHEN 78 > 0 " +
                     "         THEN ROUND(((MasterHours + RequestHours) * 100.0 / 78), 2) " +
                     "         ELSE 0 " +
                     "    END as UtilizationPercent " +
                     "FROM RoomHours " +
                     "ORDER BY UtilizationPercent DESC";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            String roomCode = rs.getString("RoomCode");
            double utilization = rs.getDouble("UtilizationPercent");
            results.add(new RoomUtilization(roomCode, utilization));
        }
        return results;
    }
    
          // Get room utilization for monthly view
    public List<RoomUtilization> getRoomUtilizationMonthly(String buildingCode) throws SQLException {
        List<RoomUtilization> results = new ArrayList<>();
        
        // Use string concatenation for building code since CTE subqueries make parameter binding complex
        String sql = "WITH RoomHours AS (" +
                     "    SELECT " +
                     "        r.RoomCode, " +
                     "        ISNULL((SELECT SUM(DATEDIFF(HOUR, ms.TimeIn, ms.TimeOut)) " +
                     "                FROM dbo.MasterSchedule ms " +
                     "                WHERE ms.RoomCode = r.RoomCode " +
                     "                AND ms.ScheduledDay IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday') " +
                     "                AND ms.IsArchived = 0), 0) as MasterHoursPerWeek, " +
                     "        ISNULL((SELECT SUM(DATEDIFF(HOUR, rs.TimeIn, rs.TimeOut)) " +
                     "                FROM dbo.RequestSchedule rs " +
                     "                WHERE rs.RoomCode = r.RoomCode " +
                     "                AND rs.Status = 3 " +
                     "                AND MONTH(rs.DateRequested) = MONTH(GETDATE()) " +
                     "                AND YEAR(rs.DateRequested) = YEAR(GETDATE()) " +
                     "                AND rs.IsArchived = 0), 0) as RequestHoursThisMonth " +
                     "    FROM dbo.Room r " +
                     "    WHERE r.BuildingCode = '" + buildingCode.replace("'", "''") + "' AND r.IsArchived = 0 " +
                     ") " +
                     "SELECT " +
                     "    RoomCode, " +
                     "    (MasterHoursPerWeek + (RequestHoursThisMonth / 4.0)) as TotalWeeklyHours, " +
                     "    78 as AvailableHoursPerWeek, " +
                     "    CASE WHEN 78 > 0 " +
                     "         THEN ROUND(((MasterHoursPerWeek + (RequestHoursThisMonth / 4.0)) * 100.0 / 78), 2) " +
                     "         ELSE 0 " +
                     "    END as UtilizationPercent " +
                     "FROM RoomHours " +
                     "ORDER BY UtilizationPercent DESC";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            String roomCode = rs.getString("RoomCode");
            double utilization = rs.getDouble("UtilizationPercent");
            results.add(new RoomUtilization(roomCode, utilization));
        }
        return results;
    }
    
    // ==================== HELPER CLASSES ====================
    
    public static class BuildingRequestCount {
        public String buildingCode;
        public String buildingName;
        public int requestCount;
        
        public BuildingRequestCount(String code, String name, int count) {
            this.buildingCode = code;
            this.buildingName = name;
            this.requestCount = count;
        }
    }
    
    public static class RoomFrequency {
        public String roomName;
        public int requestCount;
        
        public RoomFrequency(String name, int count) {
            this.roomName = name;
            this.requestCount = count;
        }
    }
    
    public static class RoomUtilization {
        public String roomName;
        public double utilizationPercent;
        
        public RoomUtilization(String name, double percent) {
            this.roomName = name;
            this.utilizationPercent = percent;
        }
    }
}
