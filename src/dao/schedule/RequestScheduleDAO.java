package dao.schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.schedule.RequestSchedule;
import utilities.DBConnection;

public class RequestScheduleDAO {
    Connection connection;

    public RequestScheduleDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Status constants
    public static final int STATUS_VOID = 0;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_DECLINED = 2;
    public static final int STATUS_APPROVED = 3;

    // Get all request schedules for a specific room
    public List<RequestSchedule> getRoom(String roomCode) {
        List<RequestSchedule> requests = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM RequestSchedule WHERE RoomCode = ? AND IsArchived = 0");
            stmt.setString(1, roomCode);
            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                RequestSchedule request = new RequestSchedule();
                request.load(
                        set.getInt("RequestKey"),
                        set.getString("RoomCode"),
                        set.getString("CourseCode"),
                        set.getString("StudentNumber"),
                        set.getString("SectionKey"),
                        set.getString("FacultyID"),
                        set.getString("TimeIn"),
                        set.getString("TimeOut"),
                        set.getString("ScheduledDay"),
                        set.getInt("Status"),
                        set.getDate("DateRequested").toString(),
                        set.getBoolean("IsArchived") ? "1" : "0");
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<RequestSchedule> getAppDecRequestOfStudent(String studentNumber) {
        List<RequestSchedule> sectionRequests = new ArrayList<>();
        try {
            PreparedStatement stmt = connection
                    .prepareStatement(
                            "SELECT * FROM RequestSchedule WHERE StudentNumber = ? AND (Status = 2 OR Status = 3)");
            stmt.setString(1, studentNumber);
            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                RequestSchedule requestSchedule = new RequestSchedule();
                requestSchedule.load(set.getInt("RequestKey"), set.getString("RoomCode"),
                        set.getString("SectionKey"), set.getString("CourseCode"), set.getString("FacultyID"),
                        set.getString("TimeIn"),
                        set.getString("TimeOut"), set.getString("ScheduledDay"), set.getString("Status"),
                        set.getInt("isArchived"),
                        set.getString("DateRequested"), set.getString("StudentNumber"));
                sectionRequests.add(requestSchedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sectionRequests;
    }

    public List<RequestSchedule> getRequestOfSection(int sectionKey) {
        List<RequestSchedule> sectionRequests = new ArrayList<>();
        try {
            PreparedStatement stmt = connection
                    .prepareStatement("SELECT * FROM RequestSchedule WHERE SectionKey = ?");
            stmt.setInt(1, sectionKey);
            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                RequestSchedule requestSchedule = new RequestSchedule();
                requestSchedule.load(set.getInt("RequestKey"), set.getString("RoomCode"),
                        set.getString("CourseCode"), String.valueOf(sectionKey), set.getString("FacultyID"),
                        set.getString("TimeIn"),
                        set.getString("TimeOut"), set.getString("ScheduledDay"), set.getString("Status"),
                        set.getInt("isArchived"),
                        set.getString("DateRequested"), set.getString("StudentNumber"));
                sectionRequests.add(requestSchedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sectionRequests;
    }

    // Get request by ID
    public RequestSchedule getById(int requestKey) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM RequestSchedule WHERE RequestKey = ?");
            stmt.setInt(1, requestKey);
            ResultSet set = stmt.executeQuery();

            if (set.next()) {
                RequestSchedule request = new RequestSchedule();
                request.load(
                        set.getInt("RequestKey"),
                        set.getString("RoomCode"),
                        set.getString("CourseCode"),
                        set.getString("StudentNumber"),
                        set.getString("SectionKey"),
                        set.getString("FacultyID"),
                        set.getString("TimeIn"),
                        set.getString("TimeOut"),
                        set.getString("ScheduledDay"),
                        set.getInt("Status"),
                        set.getDate("DateRequested").toString(),
                        set.getBoolean("IsArchived") ? "1" : "0");
                return request;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RequestSchedule getRequestSchedule(int requestKey) {
        RequestSchedule requestSchedule = new RequestSchedule();
        try {
            PreparedStatement stmt = connection
                    .prepareStatement("SELECT * FROM RequestSchedule WHERE RequestKey = ?");
            stmt.setInt(1, requestKey);
            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                requestSchedule.load(requestKey, set.getString("RoomCode"),
                        set.getString("SectionKey"), set.getString("CourseCode"), set.getString("FacultyID"),
                        set.getString("TimeIn"),
                        set.getString("TimeOut"), set.getString("ScheduledDay"), set.getString("Status"),
                        set.getInt("isArchived"),
                        set.getString("DateRequested"), set.getString("StudentNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestSchedule;
    }

    // Get single count by status for CURRENT MONTH
    public int getMonthlyCountByStatus(int status) {
        String sql = "SELECT COUNT(*) as count FROM RequestSchedule " +
                "WHERE Status = ? AND IsArchived = 0 " +
                "AND MONTH(DateRequested) = MONTH(GETDATE()) " +
                "AND YEAR(DateRequested) = YEAR(GETDATE())";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get single count by status for CURRENT WEEK
    public int getWeeklyCountByStatus(int status) {
        String sql = "SELECT COUNT(*) as count FROM RequestSchedule " +
                "WHERE Status = ? AND IsArchived = 0 " +
                "AND DateRequested >= DATEADD(day, -7, GETDATE()) " +
                "AND DateRequested <= GETDATE()";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get all counts for current month - CORRECTED STATUS ORDER
    // Returns: [pending, declined, approved, void] or [approved, declined, void,
    // pending] - adjust as needed
    public int[] getMonthlyCounts() {
        int[] counts = new int[4];
        counts[0] = getMonthlyCountByStatus(STATUS_APPROVED); // Approved
        counts[1] = getMonthlyCountByStatus(STATUS_DECLINED); // Declined
        counts[2] = getMonthlyCountByStatus(STATUS_VOID); // Void
        // Note: Pending (1) is typically not shown in reports as it's not finalized
        return counts;
    }

    // Get all counts for current week - CORRECTED STATUS ORDER
    public int[] getWeeklyCounts() {
        int[] counts = new int[3]; // Approved, Declined, Void (excluding Pending)
        counts[0] = getWeeklyCountByStatus(STATUS_APPROVED); // Approved
        counts[1] = getWeeklyCountByStatus(STATUS_DECLINED); // Declined
        counts[2] = getWeeklyCountByStatus(STATUS_VOID); // Void
        return counts;
    }

    // Get all status counts in one query - more efficient
    public int[] getAllStatusCountsMonthly() {
        String sql = "SELECT " +
                "SUM(CASE WHEN Status = 3 THEN 1 ELSE 0 END) as Approved, " +
                "SUM(CASE WHEN Status = 2 THEN 1 ELSE 0 END) as Declined, " +
                "SUM(CASE WHEN Status = 0 THEN 1 ELSE 0 END) as Void, " +
                "SUM(CASE WHEN Status = 1 THEN 1 ELSE 0 END) as Pending " +
                "FROM RequestSchedule " +
                "WHERE IsArchived = 0 " +
                "AND MONTH(DateRequested) = MONTH(GETDATE()) " +
                "AND YEAR(DateRequested) = YEAR(GETDATE())";

        int[] counts = new int[4]; // [approved, declined, void, pending]
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                counts[0] = rs.getInt("Approved");
                counts[1] = rs.getInt("Declined");
                counts[2] = rs.getInt("Void");
                counts[3] = rs.getInt("Pending");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counts;
    }

    // Get daily breakdown for current week by status
    public int[] getWeeklyDailyBreakdown(int status) {
        int[] dailyCounts = new int[7]; // Mon-Sun
        String sql = "SELECT DATENAME(weekday, DateRequested) as day, COUNT(*) as count " +
                "FROM RequestSchedule " +
                "WHERE Status = ? AND IsArchived = 0 " +
                "AND DateRequested >= DATEADD(day, -7, GETDATE()) " +
                "GROUP BY DATENAME(weekday, DateRequested) " +
                "ORDER BY CASE DATENAME(weekday, DateRequested) " +
                "WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 " +
                "WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 " +
                "WHEN 'Sunday' THEN 7 END";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String dayName = rs.getString("day");
                int count = rs.getInt("count");
                int dayIndex = getDayIndex(dayName);
                if (dayIndex >= 0 && dayIndex < 7) {
                    dailyCounts[dayIndex] = count;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailyCounts;
    }

    private int getDayIndex(String dayName) {
        switch (dayName) {
            case "Monday":
                return 0;
            case "Tuesday":
                return 1;
            case "Wednesday":
                return 2;
            case "Thursday":
                return 3;
            case "Friday":
                return 4;
            case "Saturday":
                return 5;
            case "Sunday":
                return 6;
            default:
                return -1;
        }
    }

    // Update request status
    public boolean updateStatus(int requestKey, int newStatus) {
        String sql = "UPDATE RequestSchedule SET Status = ? WHERE RequestKey = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newStatus);
            stmt.setInt(2, requestKey);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Archive request
    public boolean archive(int requestKey) {
        String sql = "UPDATE RequestSchedule SET IsArchived = 1 WHERE RequestKey = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, requestKey);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countActiveRequests(String sectionKey, String dateToday) {
        String sql = "SELECT COUNT(*) as Requests FROM RequestSchedule " +
                "WHERE SectionKey = ? " +
                "AND DateRequested = ? " +
                "AND Status = 1 " + // not cancelled
                "AND IsArchived = 0";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sectionKey);
            stmt.setString(2, dateToday);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt("Requests");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addRequest(RequestSchedule requestSchedule) {
        String sql = "INSERT INTO RequestSchedule (RoomCode, CourseCode, StudentNumber, SectionKey, FacultyID, TimeIn, TimeOut, ScheduledDay, Status, DateRequested, IsArchived) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, requestSchedule.getRoomCode());
            stmt.setString(2, requestSchedule.getCourseCode());
            stmt.setString(3, requestSchedule.getStudentRequested());
            stmt.setString(4, requestSchedule.getSectionKey());
            stmt.setString(5, requestSchedule.getFacultyID());
            stmt.setString(6, requestSchedule.getTimeIn());
            stmt.setString(7, requestSchedule.getTimeOut());
            stmt.setString(8, requestSchedule.getScheduledDay());
            stmt.setString(9, requestSchedule.getStatus());
            stmt.setString(10, requestSchedule.getDateRequested());
            stmt.setInt(11, requestSchedule.getIsArchived());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
