package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utilities.DBConnection;

public class LookUpDAO {
    private static Connection connection;

    public LookUpDAO() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getFullSectionName(int sectionKey) {
        String fullSectionName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Section WHERE SectionKey = ?");
            stmt.setInt(1, sectionKey);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullSectionName = set.getString("ProgramCode") + " " + set.getString("SectionID");
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullSectionName;
    }

    public String getFullRoomName(String roomCode) {
        String fullRoomName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Room WHERE RoomCode = ?");
            stmt.setString(1, roomCode);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullRoomName = set.getString("BuildingCode") + " - " + set.getString("RoomCode");
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullRoomName;
    }

    public String getFullCourseName(String courseCode) {
        String fullCourseName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Course WHERE CourseCode = ?");
            stmt.setString(1, courseCode);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullCourseName = set.getString("CourseCode") + " - " + set.getString("CourseDescription") + " | "
                    + set.getDouble("Units") + " Units";
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullCourseName;
    }

    public String getFullFacultyName(String facultyId) {
        String fullFacultyName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Faculty WHERE EmployeeID = ?");
            stmt.setString(1, facultyId);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullFacultyName = set.getString("FirstName") + " " + set.getString("MiddleName") + " "
                    + set.getString("LastName");
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullFacultyName;
    }

    public String getFullStudentName(String studentNumber) {
        String fullStudentName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Student WHERE StudentNumber = ?");
            stmt.setString(1, studentNumber);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullStudentName = set.getString("FirstName") + " " + set.getString("MiddleName") + " "
                    + set.getString("LastName");
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullStudentName;
    }
}
