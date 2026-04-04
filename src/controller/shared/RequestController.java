package controller.shared;

import dao.schedule.RequestScheduleDAO;
import model.schedule.RequestSchedule;
import model.user.User;
import utilities.DateTimeBuilder;
import view.common.MainFrame;
import view.common.NotificationMessage;
import view.common.RequestForm;

public class RequestController {
    User user;

    public RequestController(RequestSchedule requestSchedule, User user) {
        this.user = user;
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

        RequestScheduleDAO requestScheduleDAO = new RequestScheduleDAO();
        requestScheduleDAO.addRequest(requestSchedule);

        String message = "";

        if (user.getUserType().equals("Student")) {
            message = "Your request is successfully submitted !";
        } else {
            message = "Your request is successfully scheduled!";
        }
        NotificationMessage notifModal = new NotificationMessage("", message);
        MainFrame.addContentPanel(notifModal, "Notification");
        MainFrame.showPanel("Notification");
    }
}
