import controller.login.LoginController;
import java.sql.SQLException;
import utilities.DBConnection;
import view.common.MainFrame;

public class main {
    public static void main(String[] args) throws SQLException {

        // DBConnection("ALEX\\SQLEXPRESS", "SchedulingSystem", "sa", "1234");
        new DBConnection("LAPTOP-81CGQV8U\\SQLEXPRESS", "SchedulingSystem", "sa", "a");

        MainFrame.init();

        new LoginController();
        // FacultyController facultyController = new FacultyController(frame);
    }
}
