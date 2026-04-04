package controller.student;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JPanel;

import dao.LookUpDAO;
import dao.StudentDAO;
import dao.schedule.RequestScheduleDAO;
import model.schedule.RequestSchedule;
import model.user.Student;
import model.user.User;
import service.RequestValidatorService;
import service.SectionRequestValidator;
import view.common.ControlNotifs;
import view.common.MainFrame;
import view.common.NotificationMessage;

public class RequestsController {

    User user;

    public RequestsController(User user) {
        this.user = user;
        Student student = new StudentDAO().get(user.getUserID());
        String status = requestStatus(student.getSectionKey());
        int requestKey = SectionRequestValidator.getLastUsedRequestKey();
        showCheckRequestPage(status, requestKey);

    }

    void showCheckRequestPage(String status, int requestKey) {
        JPanel panel = new JPanel();
        LookUpDAO lookUp = new LookUpDAO();
        Student student = new StudentDAO().get(user.getUserID());
        RequestSchedule rs = new RequestScheduleDAO().getRequestSchedule(requestKey);

        if ((status.equalsIgnoreCase("declined") || status.equalsIgnoreCase("pending")
                || status.equalsIgnoreCase("approved") || status.equalsIgnoreCase("void"))
                && (user.getUserID().equalsIgnoreCase(rs.getStudentRequested()))) {
            ControlNotifs page = new ControlNotifs();
            student = new StudentDAO().get(rs.getStudentRequested());
            String section = lookUp.getFullSectionName(student.getSectionKey());
            String room = lookUp.getFullRoomName(rs.getRoomCode());
            String timeIn = handleTimeChange(rs.getTimeIn());
            String timeOut = handleTimeChange(rs.getTimeOut());
            String course = lookUp.getFullCourseName(rs.getCourseCode());
            String faculty = lookUp.getFullFacultyName(rs.getFacultyID());
            page.loadRequestForm(student, section, room, timeIn, timeOut, course, faculty, status);
            page.loadRequestStatusHeader(status);

            panel = page;
        }

        else if (RequestValidatorService.hasExceededMaxRequests(String.valueOf(student.getSectionKey()), rs.getDateRequested())) {
            NotificationMessage notifPage = new NotificationMessage("",
                    "Your section has a pending request.");
            panel = notifPage;
        }

        else {
            System.out.println(SectionRequestValidator.getLastUsedRequestKey());
            System.out.println(status);
            NotificationMessage page = new NotificationMessage("", "Your section has no pending request.");
            panel = page;
        }
        MainFrame.addContentPanel(panel, "CheckRequest");
        MainFrame.showPanel("CheckRequest");
    }

    private String handleTimeChange(String time) {
        LocalTime timeObj = LocalTime.parse(time);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = timeObj.format(formatter);

        return formattedTime;
    }

    private String requestStatus(int sectionKey) {
        RequestScheduleDAO rsDAO = new RequestScheduleDAO();
        List<RequestSchedule> requests = rsDAO.getRequestOfSection(sectionKey);
        String status = SectionRequestValidator.getRequestStatus(requests);
        return status;
    }

}
