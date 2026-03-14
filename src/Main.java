import controller.faculty.FacultyController;
import view.common.MainFrame;
import view.landing.Landing;
import view.landing.Login;

public class Main {
    public static void main(String[] args) {
        // faculty side
        MainFrame frame = new MainFrame();
        Landing landing = new Landing();
        frame.init();
        frame.addContentPanel(landing, "Landing");
        FacultyController facultyController = new FacultyController(frame);
    }
}
