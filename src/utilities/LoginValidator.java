package utilities;

import dao.FacultyDAO;
import dao.StudentDAO;
import model.user.User;

public class LoginValidator {

    private static User authenticatedUser;
    private static String errorMessage;

    private static FacultyDAO facultyDAO = new FacultyDAO();
    private static StudentDAO studentDAO = new StudentDAO();

    public static boolean validate(String username, String password) {

        if ((username == null || username.trim().isEmpty()) && (password == null || password.trim().isEmpty())) {
            errorMessage = "Username and password cannot be empty.";
            return false;
        }
        if (username == null || username.trim().isEmpty()) {
            errorMessage = "Username cannot be empty.";
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            errorMessage = "Password cannot be empty.";
            return false;
        }

        // find user first
        if (!isExisting(username)) {
            errorMessage = "User does not exist.";
            return false;
        }

        // then validate password against stored user
        if (!isPasswordCorrect(password)) {
            errorMessage = "Incorrect password.";
            return false;
        }

        return true;
    }

    private static boolean isExisting(String username) {
        
        User user = studentDAO.get(username);
        if (user != null) {
            user.setUserType("Student");
            authenticatedUser = user; 
            return true;
        }

    
        user = facultyDAO.get(username);
        if (user != null) {
            user.setUserType("Faculty");
            authenticatedUser = user; 
            return true;
        }

        return false;
    }

    private static boolean isPasswordCorrect(String inputPassword) {
        String decrypted = decrypt(authenticatedUser.getPassword());
        return decrypted.equals(inputPassword);
    }

    public static String decrypt(String encryptedPassword) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : encryptedPassword.toCharArray()) {
            decrypted.append((char) (c - 5));
        }
        return decrypted.toString();
    }

    public static User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }
}