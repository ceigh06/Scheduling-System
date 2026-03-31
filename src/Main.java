import controller.login.LoginController;
import java.sql.SQLException;
import utilities.DBConnection;
import view.common.MainFrame;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnection connection = new DBConnection("ALEX\\SQLEXPRESS", "SchedulingSystem", "sa", "1234");

        MainFrame.init();
       
        new LoginController();
        // FacultyController facultyController = new FacultyController(frame);
        
    }
}
