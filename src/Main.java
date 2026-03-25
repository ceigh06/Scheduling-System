import controller.faculty.FacultyController;
import view.common.MainFrame;
import view.common.Report1;
import view.common.Report3;
import view.landing.Landing;
import view.landing.Login;

public class Main {
    public static void main(String[] args) {
        // faculty side
        
        System.out.println("Im running");

        MainFrame.init();

        MainFrame.addContentPanel(new Report1(), "report1"); 
		MainFrame.showPanel("report1", "Admin Reports");

        //TEST NEW NAV BAR
    }
}
