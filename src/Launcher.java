import controller.login.LoginController;
import java.sql.SQLException;
import utilities.DBConnection;
import view.common.MainFrame;

public class Launcher {
    public static void main(String[] args) throws SQLException {
<<<<<<< HEAD
        DBConnection connection = new DBConnection("ALEX\\SQLEXPRESS",
                "SchedulingSystem", "sa", "1234");
=======
        DBConnection connection = new DBConnection("LAPTOP-81CGQV8U\\SQLEXPRESS",
                "SchedulingSystem", "sa", "a");

>>>>>>> ea1b68091c6ad32e4e6f1440d3dd4236c70fb122
        MainFrame.init();
        new LoginController();

    }
}
