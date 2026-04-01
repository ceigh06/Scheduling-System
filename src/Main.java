import java.sql.SQLException;

import controller.faculty.FacultyController;
import controller.login.LoginController;
import utilities.DBConnection;
import view.common.BrowseBuilding;
import view.common.MainFrame;
import view.landing.Landing;
import view.landing.Login;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnection connection = new DBConnection("LAPTOP-81CGQV8U\\SQLEXPRESS",
                "SchedulingSystem", "sa", "a");

        MainFrame.init();
        new LoginController();

    }
}
