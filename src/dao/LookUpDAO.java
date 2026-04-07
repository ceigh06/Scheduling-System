package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utilities.DBConnection;

public class LookUpDAO {
    private static Connection connection;

    public LookUpDAO() {
        connection = DBConnection.getConnection();
    }

    public static String getFullSectionName(int sectionKey) {
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

    public static String getFullFacultyName(String facultyId) {
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

    public String getFullCollege(String studentNumber) {
        String fullCollegeName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT c.CollegeCode, c.CollegeName " + 
            "FROM College c JOIN Program p ON c.CollegeCode = p.CollegeCode " +
            "JOIN Section sec ON p.ProgramCode = sec.ProgramCode " +
            "JOIN Student s ON sec.SectionKey = s.SectionKey " + 
            "WHERE s.StudentNumber = ?");
            stmt.setString(1, studentNumber);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullCollegeName = set.getString("CollegeName") + " (" + set.getString("CollegeCode") + ")";
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullCollegeName;
    }

    public String getFullCollegeFaculty(String collegeCode) {
        String fullCollegeName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM College WHERE CollegeCode = ?");
            stmt.setString(1, collegeCode);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullCollegeName = set.getString("CollegeName") + " (" + set.getString("CollegeCode") + ")";
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullCollegeName;
    }

    public String getFullProgramName(String studentNumber) {
        String fullProgramName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT p.ProgramCode, p.ProgramName FROM Program p " + 
            "JOIN Section sec ON sec.ProgramCode = p.ProgramCode " +
            "JOIN Student s ON s.SectionKey = sec.SectionKey " +
            "WHERE s.StudentNumber = ?");
            stmt.setString(1, studentNumber);

            ResultSet set = stmt.executeQuery();
            set.next();

            fullProgramName = set.getString("ProgramName") + " (" + set.getString("ProgramCode") + ")";
        } catch (Exception e) {
            System.out.println("Error checking if record exists: " + e.getMessage());
        }
        return fullProgramName;
    }
    
}
