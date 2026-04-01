package dao.schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Course;
import model.schedule.Schedule;
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
                schedule.load(0, roomCode, set.getString("CourseCode"), set.getString("SectionKey"), roomCode,
                        set.getString("TimeIn"), set.getString("TimeOut"), roomCode, set.getString("Status"), 0);
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
                schedule.load(set.getInt(0), set.getString(1), set.getString(2), set.getString(3),
                        set.getString(4), set.getString(5), set.getString(6), set.getString(7), set.getString(8),
                        set.getInt(9));
                schedules.add(schedule);
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

}
