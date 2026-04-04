package controller.faculty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import model.schedule.RequestSchedule;
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
            page = new NotificationMessage("", "No student pending requests.");
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

                if (approvedRequest != null) {
                    rsDAO.updateStatus(Integer.parseInt(requestKey), 3);
                    handleScheduleOverlapCheck(approvedRequest);
                    showConfirmationMessage("Request Accepted! Added to your schedule.");
                }
            });

            for (RequestSchedule rs : pendingRequests) {
                List<String> data = new ArrayList<>();
                data.add(lookUp.getFullStudentName(rs.getStudentRequested()));
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
        MainFrame.showPanel("CheckStudentRequests");
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

            boolean isOverlapping = isOverlappingWithRequest(rs, approvedRequest);

            System.out.println("Checking RS ID: " + rs.getID()
                    + " overlap result: " + isOverlapping);

            if (isOverlapping) {
                rsDAO.updateStatus(rs.getID(), 2);
            }
        }
    }

    private boolean isOverlappingWithRequest(RequestSchedule firstRS, RequestSchedule secondRS) {
        String firstTimeIn = firstRS.getTimeIn().split("\\.")[0];
        String firstTimeOut = firstRS.getTimeOut().split("\\.")[0];
        String secondTimeIn = secondRS.getTimeIn().split("\\.")[0];
        String secondTimeOut = secondRS.getTimeOut().split("\\.")[0];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime firstFormattedTimeIn = LocalTime.parse(firstTimeIn, formatter);
        LocalTime firstFormattedTimeOut = LocalTime.parse(firstTimeOut, formatter);
        LocalTime secondFormattedTimeIn = LocalTime.parse(secondTimeIn, formatter);
        LocalTime secondFormattedTimeOut = LocalTime.parse(secondTimeOut, formatter);

        return firstFormattedTimeIn.isBefore(secondFormattedTimeOut)
                && firstFormattedTimeOut.isAfter(secondFormattedTimeIn);
    }
}