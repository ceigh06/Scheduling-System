package controller.shared;

import dao.schedule.RequestScheduleDAO;
import model.schedule.RequestSchedule;
import utilities.DateTimeBuilder;
import view.common.MainFrame;
import view.common.RequestForm;

public class RequestController {
    public RequestController(RequestSchedule requestSchedule) {
        RequestForm requestForm = new RequestForm(requestSchedule);
        MainFrame.addContentPanel(requestForm, "Form");
        MainFrame.showPanel("Form");
        requestForm.setGoBackOnClick(e -> {
            onGoBack();
        });

        requestForm.setSubmitOnClick(e -> {
            onSubmit(requestSchedule);
        });
    }

    void onGoBack() {
        MainFrame.showPanel("Schedule");
    }

    void onSubmit(RequestSchedule requestSchedule) {

        requestSchedule.setTimeIn(DateTimeBuilder.convertTo24Hour(requestSchedule.getTimeIn()));
        requestSchedule.setTimeOut(DateTimeBuilder.convertTo24Hour(requestSchedule.getTimeOut()));

        System.out.println("===== Request Schedule =====");
        System.out.println("ID: " + requestSchedule.getID());
        System.out.println("Course Code: " + requestSchedule.getCourseCode());
        System.out.println("Section Key: " + requestSchedule.getSectionKey());
        System.out.println("Faculty ID: " + requestSchedule.getFacultyID());
        System.out.println("Room Code: " + requestSchedule.getRoomCode());
        System.out.println("Scheduled Day: " + requestSchedule.getScheduledDay());
        System.out.println("Date Requested: " + requestSchedule.getDateRequested());
        System.out.println("Time In: " + requestSchedule.getTimeIn());
        System.out.println("Time Out: " + requestSchedule.getTimeOut());
        System.out.println("Status: " + requestSchedule.getStatus());
        System.out.println("Student Requested: " + requestSchedule.getStudentRequested());
        System.out.println("Is Archived: " + requestSchedule.getIsArchived());
        System.out.println("============================");

        RequestScheduleDAO requestScheduleDAO = new RequestScheduleDAO();
        requestScheduleDAO.addRequest(requestSchedule);
    }
}
