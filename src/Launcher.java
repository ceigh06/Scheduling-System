import java.sql.SQLException;

import controller.login.LoginController;
import utilities.DBConnection;
import view.common.MainFrame;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        DBConnection connection = new DBConnection("LAPTOP-81CGQV8U\\SQLEXPRESS",
                "SchedulingSystem", "sa", "a");

        MainFrame.init();
        new LoginController();

    }
}
