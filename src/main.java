import java.sql.SQLException;

import controller.login.LoginController;
import utilities.DBConnection;
import view.common.MainFrame;

public class main {
    public static void main(String[] args) throws SQLException {
        new DBConnection("Zia\\SQLEXPRESS", "SchedulingSystemUPDATED", "sa", "12345");

        MainFrame.init();
        new LoginController();
        // FacultyController facultyController = new FacultyController(frame);
    }
}
