package controller.student;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import model.schedule.RequestSchedule;
import model.user.User;
import view.common.MainFrame;
import view.common.RequestHistory;

public class RequestHistoryController {
    User user;

    public RequestHistoryController(User user) {
        this.user = user;
        showRequestHistory();

    }

    void showRequestHistory() {
        RequestHistory requestHistory = new RequestHistory();
        RequestScheduleDAO rsDAO = new RequestScheduleDAO();
        LookUpDAO lookUp = new LookUpDAO();

        List<RequestSchedule> allRequests = rsDAO.getAppDecRequestOfStudent(user.getUserID());

        List<List<String>> allCardData = new ArrayList<>();
        for (RequestSchedule rs : allRequests) {
            List<String> cardData = new ArrayList<>();
            cardData.add(rs.getStatus());
            cardData.add(lookUp.getFullStudentName(rs.getStudentRequested()));
            cardData.add(lookUp.getFullSectionName(Integer.parseInt(rs.getSectionKey())));
            cardData.add(rs.getCourseCode());
            cardData.add(handleTimeFormatting(rs.getDateRequested().split(" ")[1]));
            cardData.add(lookUp.getFullRoomName(rs.getRoomCode()));
            cardData.add(handleTimeFormatting(rs.getTimeIn()) + " - " + handleTimeFormatting(rs.getTimeOut()));
            allCardData.add(cardData);
        }

        requestHistory.loadRequests(allRequests, allCardData);

        MainFrame.addContentPanel(requestHistory, "RequestHistory");
        MainFrame.showPanel("RequestHistory", "Request History");
    }

    public String handleTimeFormatting(String dateTime) {
        DateTimeFormatter flexibleFormatter = new DateTimeFormatterBuilder()
                .appendPattern("HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                .optionalEnd()
                .toFormatter();

        LocalTime time = LocalTime.parse(dateTime, flexibleFormatter);
        return time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }
}
