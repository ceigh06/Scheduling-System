package controller.login;

import java.awt.Desktop.Action;

import view.common.MainFrame;
import view.landing.Login;

public class LoginController {
    //switch method to determine which login page to show
        // user -> create controller pass the frame.
    public LoginController() {
        
        MainFrame.setNavBarVisible(false);
        Login view = new Login();
        MainFrame.addContentPanel(view, "login");
        MainFrame.showPanel("login", "Log In");

        attachLoginListener(view);
    }

    void attachLoginListener(Login view) {
        view.setOnLoginButton(e -> {
            validateUser(view.getUsername(), view.getPassword());
        });
    }

    void validateUser(String userName, String password) {

        if (userName.isEmpty() || password.isEmpty()) {
            MainFrame.setNotification("Please fill in all fields.");
            return;
        }

        

    }
}
