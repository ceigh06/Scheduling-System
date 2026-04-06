package controller.admin;

import dao.LookUpDAO;
import dao.StudentDAO;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import java.sql.SQLException;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
import utilities.DateTimeBuilder;
import view.common.ConfirmPanel;
import view.common.MainFrame;
import view.common.NotificationMessage;
import view.common.RequestForm;

public class EditScheduleController {

    private Schedule schedule;
    User user;

    public EditScheduleController(Schedule schedule, User user) throws SQLException {
        this.schedule = schedule;
        this.user = user;
        showArchiveForm(schedule, user);

    }

    public void showArchiveForm(Schedule schedule, User user) throws SQLException {
       
        LookUpDAO lookUpDAO = new LookUpDAO();
        String fullSectionName = lookUpDAO.getFullSectionName(Integer.parseInt(schedule.getSectionKey()));

        
        String requestorID = null;
        String studentName = null;
        if (schedule.getStatus().equals("3")) {
            RequestScheduleDAO requestScheduleDAO = new RequestScheduleDAO();
            StudentDAO studentDAO = new StudentDAO();
            RequestSchedule requestSchedule = requestScheduleDAO.getBySchedule(schedule);
            requestorID = requestSchedule.getStudentRequested();
            studentName = lookUpDAO.getFullStudentName(requestorID);
        }

        RequestForm requestForm = new RequestForm(schedule, user, false,
                requestorID, studentName, fullSectionName);

        // Add to MainFrame
        MainFrame.addContentPanel(requestForm, "ArchiveForm");
        MainFrame.showPanel("ArchiveForm", "Archive Schedule");

        ConfirmPanel confirmPanel = requestForm.getReqConfirm();

        confirmPanel.setBtn1Action(e -> {
            MainFrame.showPanel("Schedule");
        });

        confirmPanel.setBtn2Action(e -> {
            
            if (requestForm.isRequest) {
                RequestScheduleDAO requestScheduleDAO = new RequestScheduleDAO();

                if (RequestScheduleDAO.archiveStudentSchedule(
                        schedule.getRoomCode(),
                        schedule.getCourseCode(),
                        requestForm.studentNumber,
                        schedule.getSectionKey(),
                        schedule.getFacultyID(),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeIn()),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeOut()),
                        schedule.getScheduledDay()
                )) {
                    NotificationMessage notif = new NotificationMessage(null, "Request Schedule archived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, "Failed to archive Request Schedule.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                }

            } else {
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                if (scheduleDAO.archiveSchedule(
                        schedule.getRoomCode(),
                        schedule.getCourseCode(),
                        schedule.getSectionKey(),
                        schedule.getFacultyID(),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeIn()),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeOut()),
                        schedule.getScheduledDay()
                )) {
                    NotificationMessage notif = new NotificationMessage("", "Schedule archived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, "Failed to archive Schedule.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                }
            }

        });
    }

}
