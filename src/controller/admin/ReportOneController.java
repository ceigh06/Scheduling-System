package controller.admin;

import dao.schedule.RequestScheduleDAO;
import view.admin.Report1;

public class ReportOneController {
    
    private Report1 view;
    private RequestScheduleDAO requestDAO;
    
    // Status: 3=Approved, 2=Declined, 0=Void (1=Pending not shown in reports)
    private int monthlyApproved, monthlyDeclined, monthlyVoid;
    private int weeklyApproved, weeklyDeclined, weeklyVoid;
    private int[] weeklyApprovedArr, weeklyDeclinedArr, weeklyVoidArr;
    
    public ReportOneController() {
        this.requestDAO = new RequestScheduleDAO();
        loadData();
    }
    
    private void loadData() {
        // Use the efficient single-query method
        int[] monthly = requestDAO.getAllStatusCountsMonthly();
        this.monthlyApproved = monthly[0];   // Status 3
        this.monthlyDeclined = monthly[1];   // Status 2
        this.monthlyVoid = monthly[2];       // Status 0

        for (int i : monthly) {
            System.out.println("Monthly count: " + i);
        }
        
        // For weekly, use individual queries or add a similar method
        this.weeklyApproved = requestDAO.getWeeklyCountByStatus(RequestScheduleDAO.STATUS_APPROVED);
        this.weeklyDeclined = requestDAO.getWeeklyCountByStatus(RequestScheduleDAO.STATUS_DECLINED);
        this.weeklyVoid = requestDAO.getWeeklyCountByStatus(RequestScheduleDAO.STATUS_VOID);
        
        this.weeklyApprovedArr = requestDAO.getWeeklyDailyBreakdown(RequestScheduleDAO.STATUS_APPROVED);
        this.weeklyDeclinedArr = requestDAO.getWeeklyDailyBreakdown(RequestScheduleDAO.STATUS_DECLINED);
        this.weeklyVoidArr = requestDAO.getWeeklyDailyBreakdown(RequestScheduleDAO.STATUS_VOID);
    }
    
    public void initView() {
        this.view = new Report1();
        
        // Set data: approved, declined, void (no pending)
        view.setMonthlyData(monthlyApproved, monthlyDeclined, monthlyVoid);
        view.setWeeklyData(weeklyApproved, weeklyDeclined, weeklyVoid);
        view.setWeeklyArrays(weeklyApprovedArr, weeklyDeclinedArr, weeklyVoidArr);
        
        // Toggle callback
        view.setOnToggleChanged(isWeekly -> {
            if (isWeekly) {
                view.showWeeklyView();
            } else {
                view.showMonthlyView();
            }
        });
        
        // Filter callback - CORRECTED labels
        view.setOnFilterChanged(filterType -> {
            switch(filterType) {
                case "All":
                    view.updateWeeklyChart(weeklyApprovedArr, weeklyDeclinedArr, weeklyVoidArr);
                    break;
                case "Approved":
                    view.updateWeeklyChart(weeklyApprovedArr, "Approved");
                    break;
                case "Declined":  // Changed from Denied
                    view.updateWeeklyChart(weeklyDeclinedArr, "Declined");
                    break;
                case "Voided":
                    view.updateWeeklyChart(weeklyVoidArr, "Voided");
                    break;
            }
        });
    }
    
    public Report1 getView() {
        return view;
    }

    public int getMonthlyTotal() {
        return monthlyApproved + monthlyDeclined + monthlyVoid;
    }
    
    public void refreshData() {
        loadData();
        view.setMonthlyData(monthlyApproved, monthlyDeclined, monthlyVoid);
        view.setWeeklyData(weeklyApproved, weeklyDeclined, weeklyVoid);
        view.setWeeklyArrays(weeklyApprovedArr, weeklyDeclinedArr, weeklyVoidArr);
    }
}