package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.spi.DirStateFactory.Result;

import model.user.Faculty;
import utilities.DBConnection;

public class FacultyDAO {
    static Connection connection;

    public FacultyDAO() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Faculty get(String facultyID) {
        Faculty faculty = new Faculty();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Faculty WHERE EmployeeID = ?");
            stmt.setString(1, facultyID);

            ResultSet set = stmt.executeQuery();
            set.next();

            String employeeID = set.getString("EmployeeID");
            String firstName = set.getString("FirstName");
            String middleName = set.getString("MiddleName");
            String lastName = set.getString("LastName");
            String position = set.getString("Position");
            String collegeCode = set.getString("CollegeCode");
            String password = set.getString("Password");
            
            faculty.load(employeeID, firstName, middleName, lastName, position, collegeCode, password);
            return faculty;
        } catch (Exception e) {
            System.out.println("Error checking if faculty exists: " + e.getMessage());
        }
        return null;
    }

}
