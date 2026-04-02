import java.sql.SQLException;

import controller.login.LoginController;
import utilities.DBConnection;
import view.common.MainFrame;
import view.common.RequestHistory;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        DBConnection connection = new DBConnection("Zia\\SQLEXPRESS",
                "SchedulingSystemUPDATED", "sa", "12345");

        MainFrame.init();
        MainFrame.addContentPanel(new RequestHistory(), "requestHistory");
        MainFrame.showPanel("requestHistory", "SEARCH ROOMS");

    }
}
