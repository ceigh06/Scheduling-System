package dao.schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Course;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
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
}
