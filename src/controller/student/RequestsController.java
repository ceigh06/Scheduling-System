package controller.student;

import java.util.List;

import dao.StudentDAO;
import dao.schedule.RequestScheduleDAO;
import model.schedule.RequestSchedule;
import model.user.Student;
import model.user.User;
import service.SectionRequestValidator;
import view.common.MainFrame;

public class RequestsController {

    User user;


    public RequestsController(User user) {
        this.user = user;
        Student student = new StudentDAO().get(user.getUserID());
        String status = requestStatus(student.getSectionKey());
    }

    void showCheckRequest() {

    }

    private String requestStatus(int sectionKey) {
        RequestScheduleDAO rsDAO = new RequestScheduleDAO();
        List<RequestSchedule> requests = rsDAO.getRequestOfSection(sectionKey);
        SectionRequestValidator requestValidator = new SectionRequestValidator();
        String status = requestValidator.getRequestStatus(requests);
        System.out.println(status);
        return status;
    }

}
