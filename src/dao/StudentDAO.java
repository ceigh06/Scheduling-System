package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.user.Student;
import utilities.DBConnection;

public class StudentDAO {
    private static Connection connection;

     public StudentDAO() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student get(String studentID) {
        Student student = new Student();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Student WHERE StudentNumber = ?");
            stmt.setString(1, studentID);

            ResultSet set = stmt.executeQuery();
            set.next();
 
            String firstName = set.getString("FirstName");
            String middleName = set.getString("MiddleName");
            String lastName = set.getString("LastName");
            int section = Integer.parseInt(set.getString("SectionKey"));
            String password = set.getString("Password");
            
            student.load(studentID, firstName, middleName, lastName, section, password);
            return student;
        } catch (Exception e) {
            System.out.println("Error checking if student exists: " + e.getMessage());
        }
        return null;
    }
}
