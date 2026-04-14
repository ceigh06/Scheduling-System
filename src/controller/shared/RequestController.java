package controller.shared;

import java.util.ArrayList;
import java.util.List;

import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
import service.RequestValidatorService;
import service.ScheduleValidator;
import utilities.DateTimeBuilder;
import view.common.MainFrame;
import view.common.NotificationMessage;
import view.common.RequestForm;

public class RequestController {
    User user;

    public RequestController(RequestSchedule requestSchedule, User user) {
        // guard clause so that the student can only request one time
        if (user.getUserType().equals("Student") && RequestValidatorService.hasExceededMaxRequests(requestSchedule.getSectionKey(), DateTimeBuilder.getCurrentDate())){
            MainFrame.setNotification("Your Section has Exceeded Request Limit");
            return;
        }
        this.user = user;
        LookUpDAO lookUp = new LookUpDAO();
        List<String> data = new ArrayList<>();

        data.add(requestSchedule.getRequestor());
        if(user.getUserType().equalsIgnoreCase("student")) {
            data.add(lookUp.getFullStudentName(requestSchedule.getRequestor()));
        }
        else {
            data.add(LookUpDAO.getFullFacultyName(requestSchedule.getRequestor()));
        }        
        data.add(LookUpDAO.getFullSectionName(Integer.parseInt(requestSchedule.getSectionKey())));
        data.add(lookUp.getFullRoomName(requestSchedule.getRoomCode()));
        data.add(requestSchedule.getTimeIn());
        data.add(requestSchedule.getTimeOut());
        data.add(lookUp.getFullCourseName(requestSchedule.getCourseCode()));
        data.add(LookUpDAO.getFullFacultyName(requestSchedule.getFacultyID()));

        RequestForm requestForm = new RequestForm(data, "Request Schedule", "Submit");
        MainFrame.addContentPanel(requestForm, "Form");
        MainFrame.showPanel("Form", "Request Form");
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

        if (user.getUserType().equals("Faculty")){//guard clause
            ScheduleDAO dao = new ScheduleDAO();
            List<Schedule> schedules;
            schedules = dao.getFacultySchedulesByDay(user, DateTimeBuilder.getDayName().substring(0, 3));
            System.out.println(DateTimeBuilder.getDayName().substring(0, 2));
            for (Schedule schedule : schedules){
                System.out.println(schedule.getTimeIn());
            }
            if (ScheduleValidator.isOverlapping(requestSchedule.getTimeIn(), requestSchedule.getTimeOut(), schedules)){
                MainFrame.setNotification("This Schedule overlaps with your current schedule!");
                return;
            }
        }

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
