package service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import dao.schedule.RequestScheduleDAO;


public class RequestValidatorService {

    private static final int MAX_REQUESTS = 1; // adjust to your rule

    public static boolean isSunday() {
        return LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean hasExceededMaxRequests(String sectionKey) {
        RequestScheduleDAO requestDAO = new RequestScheduleDAO();
        int count = requestDAO.countActiveRequests(sectionKey);
        return count >= MAX_REQUESTS;
    }
}