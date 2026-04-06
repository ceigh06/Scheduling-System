package controller.faculty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
import service.ScheduleValidator;
import utilities.DateTimeBuilder;
import view.common.MainFrame;
import view.common.RequestForm;
import view.common.ViewSchedule;
import view.faculty.ViewFacultySched;

public class ViewScheduleController {

    User user;
    ViewSchedule view;

    ViewScheduleController(User user) {
        this.user = user;

        String today = DateTimeBuilder.getDayName().substring(0, 3);

        // empty list on construction — reloadSchedule fills it
        view = new ViewSchedule(new ArrayList<>());

        // load today's schedule immediately
        loadScheduleForDay(today);

        // controller handles day clicks
        view.setOnDayClicked(day -> {
            loadScheduleForDay(day);
        });

        // controller handles schedule block clicks
        view.setOnScheduleClicked(schedule -> {
            showSchedule(schedule);
        });

        MainFrame.addContentPanel(view, "ViewSchedule");
        MainFrame.showPanel("ViewSchedule");
    }

    void showSchedule(Schedule schedule) {
        System.out.println(schedule.getStatus());
        schedule.setTimeIn(DateTimeBuilder.formatTo12Hour(schedule.getTimeIn()));
        schedule.setTimeOut(DateTimeBuilder.formatTo12Hour(schedule.getTimeOut()));
        System.out.println(schedule.getTimeIn());
        if (schedule.getStatus().equals("3")) { // request
            showRequestSchedule(schedule);
        } else {
            ViewFacultySched view = new ViewFacultySched("Original Schedule", schedule);
            MainFrame.addContentPanel(view, "FocusOnSchedule");
            MainFrame.showPanel("FocusOnSchedule");
            view.setOnBackClicked(e -> {
                MainFrame.showPanel("ViewSchedule");
            });
        }
    }

    void showRequestSchedule(Schedule schedule) {
        RequestScheduleDAO dao = new RequestScheduleDAO();
        RequestSchedule requestSchedule = dao.getById(schedule.getID());

        requestSchedule.setTimeIn(DateTimeBuilder.formatTo12Hour(requestSchedule.getTimeIn()));
        requestSchedule.setTimeOut(DateTimeBuilder.formatTo12Hour(requestSchedule.getTimeOut()));

        LookUpDAO lookUp = new LookUpDAO();
        List<String> data = new ArrayList<>();
        System.out.println("time in" + requestSchedule.getTimeIn());
        data.add(requestSchedule.getStudentRequested());
        data.add(lookUp.getFullFacultyName(requestSchedule.getStudentRequested()));
        data.add(lookUp.getFullSectionName(Integer.parseInt(requestSchedule.getSectionKey())));
        data.add(lookUp.getFullRoomName(requestSchedule.getRoomCode()));
        data.add(String.valueOf(requestSchedule.getTimeIn()));
        data.add(String.valueOf(requestSchedule.getTimeOut()));
        data.add(lookUp.getFullCourseName(requestSchedule.getCourseCode()));
        data.add(lookUp.getFullFacultyName(requestSchedule.getFacultyID()));

        String header = getHeader(requestSchedule.getDateRequested(),schedule.getTimeIn(), schedule.getTimeOut());

        boolean isPast = ScheduleValidator.isPast(requestSchedule.getDateRequested(),schedule.getTimeOut());

        RequestForm form = new RequestForm(data, header, "Cancel");
        form.setGoBackOnClick(e -> {
            MainFrame.showPanel("ViewSchedule");
        });

        if (!isPast) {
            form.setSubmitOnClick(e -> {
                submitCancelation(data, header);
            });
        } else{
            form.setSubmitOnClick(e ->{
                MainFrame.setNotification("Cannot cancel past requests");
            });
        }

        MainFrame.addContentPanel(form, "Form");
        MainFrame.showPanel("Form");
    }

    private String getHeader(String date, String timeIn, String timeOut) {
    if (ScheduleValidator.isOngoing(date, timeIn, timeOut)) {
        return "On going Requested Class";
    } else if (ScheduleValidator.isIncoming(date, timeIn)) {
        return "Up Coming Requested Class";
    } else {
        return "Past Request Schedule";
    }
}

    void submitCancelation(List<String> data, String header) {
        RequestForm form = new RequestForm(data, header, "Cancel");
        MainFrame.addContentPanel(form, "Form");
        MainFrame.showPanel("Form");

    }

    private void loadScheduleForDay(String day) {
        try {
            ScheduleDAO dao = new ScheduleDAO();
            List<Schedule> schedules = dao.getFacultySchedulesByDay(user, day);
            System.out.println(schedules.isEmpty());
            view.reloadSchedule(schedules);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}