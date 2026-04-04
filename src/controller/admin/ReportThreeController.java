package controller.admin;

import dao.schedule.RequestScheduleDAO;
import java.sql.SQLException;
import view.admin.Report3;

public class ReportThreeController {
    
    private Report3 view;
    private RequestScheduleDAO requestDAO;
    
    // Data for peak scheduling hours
    private int[] hourlyApprovedArr; // 24 hours, count of approved requests per hour
    
    public ReportThreeController() {
        this.requestDAO = new RequestScheduleDAO();
    }
    
    public void initView() throws SQLException {
        this.view = new Report3();
        
    }
    
    public Report3 getView() {
        return this.view;
    }

    
}