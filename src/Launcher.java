import controller.login.LoginController;
import java.sql.SQLException;
import utilities.DBConnection;
import view.common.MainFrame;

public class Launcher {
    public static void main(String[] args) throws SQLException {
<<<<<<< Updated upstream
        DBConnection connection = new DBConnection("LAPTOP-81CGQV8U\\SQLEXPRESS",
                "SchedulingSystem", "sa", "a");
=======
        DBConnection connection = new DBConnection("Zia\\SQLEXPRESS",
                "latestRoomSchedulingSystemDB", "sa", "12345");
>>>>>>> Stashed changes
        MainFrame.init();
        new LoginController();

    }
}
