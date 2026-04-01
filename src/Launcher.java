import java.sql.SQLException;

import controller.login.LoginController;
import utilities.DBConnection;
import view.common.MainFrame;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        DBConnection connection = new DBConnection("DESKTOP-0PU1JBT\\SQLEXPRESS",
                "RoomSchedulingSystemCyVer", "sa", "1234");

        MainFrame.init();
        new LoginController();

    }
}
