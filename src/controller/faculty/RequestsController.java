package controller.faculty;

import java.awt.Checkbox;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import model.schedule.RequestSchedule;
import model.user.User;
import utilities.DateTimeBuilder;
import view.common.CheckRequests;
import view.common.MainFrame;

public class RequestsController {

    User user;

    public RequestsController(User user) {
        this.user = user;
        showRequests();
    }

    void showRequests() {
        CheckRequests checkRequests = new CheckRequests();
        RequestScheduleDAO rsDAO = new RequestScheduleDAO();
        List<RequestSchedule> pendingRequests = rsDAO.getPendingRequestByFaculty(user.getUserID(),
                LocalDate.now().toString());
        LookUpDAO lookUp = new LookUpDAO();

        if (pendingRequests == null) {

        } else {
            checkRequests.setRequestCount(pendingRequests.size());
            for (RequestSchedule rs : pendingRequests) {
                List<String> data = new ArrayList<>();
                data.add(lookUp.getFullStudentName(rs.getStudentRequested()));
                data.add(lookUp.getFullSectionName(Integer.parseInt(rs.getSectionKey())));
                data.add(lookUp.getFullCourseName(rs.getCourseCode()));
                data.add(DateTimeBuilder.formatTo12Hour(
                        Integer.parseInt(rs.getDateRequested().split(" ")[1].split(":")[0]),
                        Integer.parseInt(rs.getDateRequested().split(" ")[1].split(":")[1])));
                data.add(lookUp.getFullRoomName(rs.getRoomCode()));
                data.add((DateTimeBuilder.formatTo12Hour(Integer.parseInt(rs.getTimeIn().split(":")[0]),
                        Integer.parseInt(rs.getTimeIn().split(":")[1]))) + " - " +
                        (DateTimeBuilder.formatTo12Hour(Integer.parseInt(rs.getTimeOut().split(":")[0]),
                                Integer.parseInt(rs.getTimeOut().split(":")[1]))));

                checkRequests.loadRequests(data);
            }
        }

        MainFrame.addContentPanel(checkRequests, "CheckStudentRequests");
        MainFrame.showPanel("CheckStudentRequests");
    }
}
