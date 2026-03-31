import java.sql.SQLException;

import controller.login.LoginController;
import utilities.DBConnection;
import view.common.MainFrame;

public class Main {
    public static void main(String[] args) throws SQLException {
        new DBConnection("LAPTOP-81CGQV8U\\SQLEXPRESS", "SchedulingSystem", "sa", "a");

        MainFrame.init();
        new LoginController();
        // FacultyController facultyController = new FacultyController(frame);
    }
}
