package dao.schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Section;
import model.schedule.Schedule;
import model.user.Faculty;
import model.user.User;
import utilities.DBConnection;

public class ScheduleDAO {
    Connection connection;

    public ScheduleDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Schedule> getRoom(String roomCode) {
        List<Schedule> schedules = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("EXEC sp_GetRoomScheduleForToday @RoomCode = ?");
            stmt.setString(1, roomCode);
            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                Schedule schedule = new Schedule();
                schedule.load(1, roomCode, set.getString("SectionKey"), set.getString("CourseCode"),
                        set.getString("FacultyID"),
                        set.getString("TimeIn"), set.getString("TimeOut"), set.getString("ScheduledDay"),
                        set.getString("Status"), set.getInt("IsArchived"));
                schedules.add(schedule);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return schedules;
    }

    public List<Schedule> addRoomSchedules(String roomCode, List<Schedule> schedules) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MasterSchedule WHERE RoomCode = ?");
            stmt.setString(1, roomCode);
            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                Schedule schedule = new Schedule();
                schedule.load(set.getInt(1), set.getString(2), set.getString(3), set.getString(4),
                        set.getString(5), set.getString(6), set.getString(7), set.getString(8), set.getString(9),
                        set.getInt(10));
                schedules.add(schedule);
                System.out.println();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return schedules;
    }

    public String getFacultyIDByStudentCourse(String studentNumber, String courseCode) {
        String sql = "SELECT DISTINCT ms.FacultyID " +
                "FROM MasterSchedule ms " +
                "JOIN EnrolledCourses ec ON ms.CourseCode = ec.CourseCode " +
                "AND ms.SectionKey = ec.SectionKey " + // match both keys
                "WHERE ec.StudentNumber = ? " +
                "AND ec.CourseCode = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentNumber);
            stmt.setString(2, courseCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("FacultyID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean archiveSchedule(
            String roomCode,
            String courseCode,
            String sectionKey,
            String facultyID,
            String timeIn,
            String timeOut,
            String scheduledDay) {

        String sql = "UPDATE MasterSchedule " +
                "SET IsArchived = 1 " +
                "WHERE RoomCode = ? " +
                "AND CourseCode = ? " +
                "AND SectionKey = ? " +
                "AND FacultyID = ? " +
                "AND TimeIn = ? " +
                "AND TimeOut = ? " +
                "AND ScheduledDay = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomCode);
            stmt.setString(2, courseCode);
            stmt.setString(3, sectionKey);
            stmt.setString(4, facultyID);
            stmt.setString(5, timeIn);
            stmt.setString(6, timeOut);
            stmt.setString(7, scheduledDay);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean unarchiveSchedule(
            String roomCode,
            String courseCode,
            String sectionKey,
            String facultyID,
            String timeIn,
            String timeOut,
            String scheduledDay) {

        String sql = "UPDATE MasterSchedule " +
                "SET IsArchived = 0 " +
                "WHERE RoomCode = ? " +
                "AND CourseCode = ? " +
                "AND SectionKey = ? " +
                "AND FacultyID = ? " +
                "AND TimeIn = ? " +
                "AND TimeOut = ? " +
                "AND ScheduledDay = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomCode);
            stmt.setString(2, courseCode);
            stmt.setString(3, sectionKey);
            stmt.setString(4, facultyID);
            stmt.setString(5, timeIn);
            stmt.setString(6, timeOut);
            stmt.setString(7, scheduledDay);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Schedule> filterActiveSchedules(List<Schedule> schedules) {
        List<Schedule> activeSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {

            if (schedule.getIsArchived() == 0) {
                activeSchedules.add(schedule);
            }
        }

        return activeSchedules;
    }

    public List<Schedule> filterInactiveSchedules(List<Schedule> schedules) {
        List<Schedule> inactiveSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {

            if (schedule.getIsArchived() == 1) {
                inactiveSchedules.add(schedule);
            }
        }

        return inactiveSchedules;
    }

    public List<Section> getSectionByFacultyCourse(String courseCode, String facultyID) {
        String query = "SELECT DISTINCT s.SectionKey, s.SectionID, s.ProgramCode FROM MasterSchedule m JOIN Section s ON s.SectionKey = m.SectionKey WHERE CourseCode = ? AND FacultyID = ?";

        List<Section> sections = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseCode);
            stmt.setString(2, facultyID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Section section = new Section(rs.getInt("SectionKey"), rs.getString("SectionID"),
                        rs.getString("ProgramCode"));
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Schedule> getSchedulesByDay(String day) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();

        String fullDay;
        switch (day) {
            case "Mon":
                fullDay = "Monday";
                break;
            case "Tue":
                fullDay = "Tuesday";
                break;
            case "Wed":
                fullDay = "Wednesday";
                break;
            case "Thu":
                fullDay = "Thursday";
                break;
            case "Fri":
                fullDay = "Friday";
                break;
            case "Sat":
                fullDay = "Saturday";
                break;
            default:
                fullDay = day;
        }

        String sql = "SELECT * FROM MasterSchedule WHERE ScheduledDay = ? AND IsArchived = 1";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullDay);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Schedule sched = new Schedule();
                sched.load(
                        rs.getInt("ScheduleID"),
                        rs.getString("RoomCode"),
                        rs.getString("SectionKey"),
                        rs.getString("CourseCode"),
                        rs.getString("FacultyID"),
                        rs.getString("TimeIn"),
                        rs.getString("TimeOut"),
                        fullDay,
                        rs.getString("Status"),
                        rs.getInt("IsArchived"));
                schedules.add(sched);
            }
        }

        return schedules;
    }

    public List<Schedule> getFacultySchedulesByDay(User user, String day) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();

        String fullDay;
        switch (day) {
            case "Mon":
                fullDay = "Monday";
                break;
            case "Tue":
                fullDay = "Tuesday";
                break;
            case "Wed":
                fullDay = "Wednesday";
                break;
            case "Thu":
                fullDay = "Thursday";
                break;
            case "Fri":
                fullDay = "Friday";
                break;
            case "Sat":
                fullDay = "Saturday";
                break;
            default:
                fullDay = day;
        }

        // UNION of MasterSchedule and approved RequestSchedule for the faculty
        String sql = "SELECT ScheduleID AS ID, RoomCode, SectionKey, CourseCode, FacultyID, " +
        "       TimeIn, TimeOut, ScheduledDay, Status, IsArchived " +
        "FROM MasterSchedule " +
        "WHERE FacultyID = ? AND ScheduledDay = ? AND IsArchived = 0 " +
        "UNION ALL " +
        "SELECT RequestKey AS ID, RoomCode, SectionKey, CourseCode, FacultyID, " +
        "       TimeIn, TimeOut, ScheduledDay, Status, IsArchived " +
        "FROM RequestSchedule " +
        "WHERE FacultyID = ? AND ScheduledDay = ? AND Status = 3 AND IsArchived = 0 " +
        "AND DateRequested >= DATEADD(day, 1-DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) " +
        "AND DateRequested <  DATEADD(day, 8-DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) " +
        "ORDER BY TimeIn";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, user.getUserID());
        stmt.setString(2, fullDay);
        stmt.setString(3, user.getUserID());
        stmt.setString(4, fullDay);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Schedule sched = new Schedule();
            System.out.println(rs.getInt("ID"));
            sched.load(
                    rs.getInt("ID"),
                    rs.getString("RoomCode"),
                    rs.getString("SectionKey"),
                    rs.getString("CourseCode"),
                    rs.getString("FacultyID"),
                    rs.getString("TimeIn"),
                    rs.getString("TimeOut"),
                    fullDay,
                    rs.getString("Status"),
                    rs.getInt("IsArchived"));
            schedules.add(sched);
        }

        return schedules;
    }
}
