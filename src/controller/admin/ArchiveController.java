package controller.admin;

import controller.shared.RoomsController;
import java.sql.SQLException;
import model.user.User;

public class ArchiveController {

    public ArchiveController(User user) throws SQLException {
        new RoomsController(user);
        
    }
}