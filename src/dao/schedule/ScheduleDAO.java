package dao.schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Section;
import model.schedule.Schedule;
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
                schedule.load(1, roomCode, set.getString("SectionKey"), set.getString("CourseCode"), set.getString("FacultyID"),
                        set.getString("TimeIn"), set.getString("TimeOut"), set.getString("ScheduledDay"), set.getString("Status"), set.getInt("IsArchived"));
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

public List<Schedule> filterActiveSchedules(List<Schedule> schedules) {
    List<Schedule> activeSchedules = new ArrayList<>();

    for (Schedule schedule : schedules) {
        
        if (schedule.getIsArchived() == 0) {
            activeSchedules.add(schedule);
        }
    }

    return activeSchedules;
}

    public List<Section> getSectionByFacultyCourse(String courseCode, String facultyID) {
        String query = "SELECT DISTINCT s.SectionKey, s.SectionID, s.ProgramCode FROM MasterSchedule m JOIN Section s ON s.SectionKey = m.SectionKey WHERE CourseCode = ? AND FacultyID = ?";

        List<Section> sections = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseCode);
            stmt.setString(2, facultyID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Section section = new Section(rs.getInt("SectionKey"), rs.getString("SectionID"), rs.getString("ProgramCode"));
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
