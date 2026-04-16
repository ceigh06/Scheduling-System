package controller.admin;

import java.sql.SQLException;
import view.admin.Report3;

public class ReportThreeController {
    
    private Report3 view;
    
    // Data for peak scheduling hours
    
    public ReportThreeController() {
    }
    
    public void initView() throws SQLException {
        this.view = new Report3();
        
    }
    
    public Report3 getView() {
        return this.view;
    }

    

    public void refreshData() {
        try {
            // Report3 queries fresh data on construction, so rebuild the view in-place
            Report3 newView = new Report3();
            this.view = newView;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}