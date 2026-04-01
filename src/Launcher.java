import java.sql.SQLException;

import controller.login.LoginController;
import utilities.DBConnection;
import view.common.MainFrame;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        new DBConnection("JessieHP\\SQLEXPRESS",
                "SchedulingSystem", "sa", "jessie");

        MainFrame.init();
        new LoginController();

    }
}
