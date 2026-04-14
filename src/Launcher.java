import controller.login.LoginController;

import java.sql.SQLException;
import utilities.DBConnection;
import view.common.MainFrame;

public class Launcher {
        public static void main(String[] args) throws SQLException {
                new DBConnection("26.218.110.33:1433", "SchedulingSystem", "admin_user",
                    "1234");
                MainFrame.init();
                new LoginController();
        }
}
