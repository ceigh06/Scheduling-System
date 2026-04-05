package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Course;
import utilities.DBConnection;

public class CourseDAO {
    private static Connection connection;

    public CourseDAO()  {
        try {
            this.connection = DBConnection.getConnection(); // shared connection
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }

    public List<Course> getStudentCourse(String studentNumber){
        List<Course> courses = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT c.CourseCode, c.ProgramCode, c.CourseDescription, c.Units,c.IsMajor,IsArchived, ec.SectionKey FROM Course c "
            + "JOIN EnrolledCourses ec ON c.CourseCode = ec.CourseCode  WHERE ec.StudentNumber = ?");
            stmt.setString(1, studentNumber);
            ResultSet set = stmt.executeQuery();
            while (set.next()){
                Course course = new Course();
                course.setCode(set.getString("CourseCode"));
                course.setProgramCode(set.getString("ProgramCode"));
                course.setDescription(set.getString("CourseDescription"));
                course.setUnits(set.getInt("Units"));
                course.setMajor((set.getString("IsMajor").equals("1") ? true : false));
                course.setIsArchived((set.getString("IsArchived").equals("1") ? true : false ));
                course.setSection(set.getInt("SectionKey"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return courses;
    }


    public List<Course> getFacultyCourses(String facultyID) {
        List<Course> courses = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("EXEC sp_GetFacultyCourses @FacultyID = ?");
            stmt.setString(1, facultyID);
            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                Course course = new Course();
                course.setCode(set.getString("CourseCode"));
                course.setProgramCode(set.getString("ProgramCode"));
                course.setDescription(set.getString("CourseDescription"));
                course.setUnits(set.getInt("Units"));
                course.setMajor((set.getString("IsMajor").equals("1") ? true : false));
                course.setIsArchived((set.getString("IsArchived").equals("1") ? true : false ));
                courses.add(course);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }


    
}
