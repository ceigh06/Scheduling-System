import controller.login.LoginController;
import java.sql.SQLException;
import utilities.DBConnection;
import view.common.MainFrame;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        DBConnection connection = new DBConnection("JessieHP\\SQLEXPRESS",
                "latestRoomSchedulingSystemDB", "sa", "jessie");
        MainFrame.init();
        new LoginController();

    }
}
