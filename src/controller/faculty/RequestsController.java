package controller.faculty;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
import utilities.DateTimeBuilder;
import view.common.CheckRequests;
import view.common.MainFrame;
import view.common.NotificationMessage;

public class RequestsController {

    User user;

    public RequestsController(User user) {
        this.user = user;
        showRequests();
    }

    void showRequests() {
        CheckRequests checkRequests = new CheckRequests();
        RequestScheduleDAO rsDAO = new RequestScheduleDAO();
        List<RequestSchedule> pendingRequests = rsDAO.getPendingRequestByFaculty(
                user.getUserID(), LocalDate.now().toString());
        LookUpDAO lookUp = new LookUpDAO();
        JPanel page;

        if (pendingRequests.isEmpty()) {
            page = new NotificationMessage("/resources/images/icons/allCaughtUpIcon.png",
                    "No student pending requests.");
        } else {
            checkRequests.loadRequestCount(pendingRequests.size());

            checkRequests.setOnDecline(requestKey -> {
                rsDAO.updateStatus(Integer.parseInt(requestKey), 2);
                showConfirmationMessage("Request Declined!");
            });

            checkRequests.setOnAccept(requestKey -> {
                RequestSchedule approvedRequest = pendingRequests.stream()
                        .filter(rs -> String.valueOf(rs.getID()).equals(requestKey))
                        .findFirst()
                        .orElse(null);
                boolean isOverlapping = false;

                ScheduleDAO schedDAO = new ScheduleDAO();
                List<Schedule> facultySchedule = schedDAO.getFacultySchedulesByDay(user,
                        DateTimeBuilder.getDayName());
                if (isOverlappingWithFacultySchedule(facultySchedule, approvedRequest)) {
                    isOverlapping = true;
                    MainFrame.setNotification("This schedule is overlapping with your current schedule.");
                }

                if (approvedRequest != null && !isOverlapping) {
                    rsDAO.updateStatus(Integer.parseInt(requestKey), 3);
                    handleScheduleOverlapCheck(approvedRequest);
                    showConfirmationMessage("Request Accepted! Added to your schedule.");
                }
            });

            for (RequestSchedule rs : pendingRequests) {
                List<String> data = new ArrayList<>();
                data.add(lookUp.getFullStudentName(rs.getRequestor()));
                data.add(lookUp.getFullSectionName(Integer.parseInt(rs.getSectionKey())));
                data.add(lookUp.getFullCourseName(rs.getCourseCode()));
                data.add(DateTimeBuilder.formatTo12Hour(
                        Integer.parseInt(rs.getDateRequested().split(" ")[1].split(":")[0]),
                        Integer.parseInt(rs.getDateRequested().split(" ")[1].split(":")[1])));
                data.add(lookUp.getFullRoomName(rs.getRoomCode()));
                data.add(DateTimeBuilder.formatTo12Hour(
                        Integer.parseInt(rs.getTimeIn().split(":")[0]),
                        Integer.parseInt(rs.getTimeIn().split(":")[1]))
                        + " - " +
                        DateTimeBuilder.formatTo12Hour(
                                Integer.parseInt(rs.getTimeOut().split(":")[0]),
                                Integer.parseInt(rs.getTimeOut().split(":")[1])));

                checkRequests.loadRequests(data, String.valueOf(rs.getID()));
            }

            page = checkRequests;
        }

        MainFrame.addContentPanel(page, "CheckStudentRequests");
        MainFrame.showPanel("CheckStudentRequests", "Check Student Requests");
    }

    public void showConfirmationMessage(String message) {
        NotificationMessage notificationMessage = new NotificationMessage("", message);
        MainFrame.addContentPanel(notificationMessage, "NotificationPage");
        MainFrame.showPanel("NotificationPage");
    }

    public void handleScheduleOverlapCheck(RequestSchedule approvedRequest) {
        RequestScheduleDAO rsDAO = new RequestScheduleDAO();
        List<RequestSchedule> pendingRequests = rsDAO.getPendingRequestOfRoomToday(
                approvedRequest.getRoomCode(), LocalDate.now().toString());

        for (RequestSchedule rs : pendingRequests) {
            if (rs.getID() == approvedRequest.getID())
                continue;

            String firstTimeIn = rs.getTimeIn().split("\\.")[0];
            String firstTimeOut = rs.getTimeOut().split("\\.")[0];
            String secondTimeIn = approvedRequest.getTimeIn().split("\\.")[0];
            String secondTimeOut = approvedRequest.getTimeOut().split("\\.")[0];

            boolean isOverlapping = isOverlappingWithRequest(firstTimeIn, firstTimeOut, secondTimeIn, secondTimeOut);

            System.out.println("Checking RS ID: " + rs.getID()
                    + " overlap result: " + isOverlapping);

            if (isOverlapping) {
                rsDAO.updateStatus(rs.getID(), 2);
            }
        }
    }

    private boolean isOverlappingWithFacultySchedule(List<Schedule> facultySchedule, RequestSchedule approvedRequest) {
        for (Schedule schedule : facultySchedule) {
            String firstTimeIn = schedule.getTimeIn().split("\\.")[0];
            String firstTimeOut = schedule.getTimeOut().split("\\.")[0];
            String secondTimeIn = approvedRequest.getTimeIn().split("\\.")[0];
            String secondTimeOut = approvedRequest.getTimeOut().split("\\.")[0];
            if (isOverlappingWithRequest(firstTimeIn, firstTimeOut, secondTimeIn, secondTimeOut)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlappingWithRequest(String firstTimeIn, String firstTimeOut, String secondTimeIn,
            String secondTimeOut) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime firstFormattedTimeIn = LocalTime.parse(firstTimeIn, formatter);
        LocalTime firstFormattedTimeOut = LocalTime.parse(firstTimeOut, formatter);
        LocalTime secondFormattedTimeIn = LocalTime.parse(secondTimeIn, formatter);
        LocalTime secondFormattedTimeOut = LocalTime.parse(secondTimeOut, formatter);

        return firstFormattedTimeIn.isBefore(secondFormattedTimeOut)
                && firstFormattedTimeOut.isAfter(secondFormattedTimeIn);
    }
}