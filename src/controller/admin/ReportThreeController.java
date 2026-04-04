package controller.admin;

import dao.schedule.RequestScheduleDAO;
import java.util.List;
import model.schedule.RequestSchedule;
import view.admin.Report3;

public class ReportThreeController {

    private Report3 view;
    private RequestScheduleDAO requestDAO;

    private int[][] weeklyApprovedArr; // 6 days x 24 hours

    public ReportThreeController() {
        this.requestDAO = new RequestScheduleDAO();
        this.weeklyApprovedArr = new int[6][24]; // 6 days, 24 hours
        loadData();
        initView();
    }

    public void loadData() {
        // Reset array
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 24; j++) {
                weeklyApprovedArr[i][j] = 0;
            }
        }

        List<RequestSchedule> requests = requestDAO.getAllActiveRequests();

        for (RequestSchedule req : requests) {
            // Only APPROVED requests
            if (Integer.parseInt(req.getStatus()) == RequestScheduleDAO.STATUS_APPROVED) {

                int hour = extractHour(req.getTimeIn());
                int dayIndex = extractDayIndex(req.getDateRequested()); // 0 = Monday

                if (hour >= 0 && hour < 24 && dayIndex >= 0 && dayIndex < 6) {
                    weeklyApprovedArr[dayIndex][hour]++;
                }
            }
        }
    }

    // Convert "HH:mm" to hour integer
    private int extractHour(String time) {
        try {
            return Integer.parseInt(time.split(":")[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    // Convert date to day index (0=Monday, 5=Saturday)
    private int extractDayIndex(String date) {
        // Assuming date format is yyyy-MM-dd
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            int day = d.getDayOfWeek().getValue(); // Monday=1 ... Sunday=7
            return day <= 6 ? day - 1 : -1; // Only Monday-Saturday
        } catch (Exception e) {
            return -1;
        }
    }

    public void initView() {
        this.view = new Report3();
        view.setWeeklyData(weeklyApprovedArr);

        // Optional: refresh when a date is selected
        view.setOnDateSelected(date -> {
            loadData(); // you can filter by date here if needed
            view.setWeeklyData(weeklyApprovedArr);
        });
    }

    public Report3 getView() {
        return view;
    }

    public int[][] getWeeklyData() {
        return weeklyApprovedArr;
    }
}