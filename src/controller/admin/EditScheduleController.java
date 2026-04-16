package controller.admin;

import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO; // Add this import
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

    User user;
    static boolean isRequestSchedule;
    static boolean isStudentRequestor;
    static String requestorID;
    static String requestorName;

    public EditScheduleController(Schedule schedule, User user) throws SQLException {
        this.user = user;
        showArchiveForm(schedule, user);
    }

    public void showArchiveForm(Schedule schedule, User user) throws SQLException {
       
        LookUpDAO lookUpDAO = new LookUpDAO();
        String fullSectionName = LookUpDAO.getFullSectionName(Integer.parseInt(schedule.getSectionKey()));

        // Determine if this is a request schedule
        
        isRequestSchedule = false;
        isStudentRequestor = false;
        requestorID = null;
        requestorName = null;
        requestorName = null;
        
        
        // Check if status indicates approved request (3 = approved)
        if (schedule.getStatus().equals("3")) {
            RequestScheduleDAO requestScheduleDAO = new RequestScheduleDAO();
            RequestSchedule requestSchedule = requestScheduleDAO.getBySchedule(schedule);
            
            if (requestSchedule != null) {
                isRequestSchedule = true;
                requestorID = requestSchedule.getRequestor();
                
                // Determine if student or faculty requestor
                isStudentRequestor = requestScheduleDAO.isStudentRequestor(requestorID);
                
                // Get appropriate name
                if (isStudentRequestor) {
                    requestorName = lookUpDAO.getFullStudentName(requestorID);
                } else {
                    requestorName = LookUpDAO.getFullFacultyName(requestorID); // Add this method to LookUpDAO
                }
            }
        }

        RequestForm requestForm = new RequestForm(schedule, user, false,
                requestorID, requestorName, fullSectionName, isStudentRequestor); // Add parameter

        // Add to MainFrame
        MainFrame.addContentPanel(requestForm, "ArchiveForm");
        MainFrame.showPanel("ArchiveForm", "Archive Schedule");

        ConfirmPanel confirmPanel = requestForm.getReqConfirm();

        confirmPanel.setBtn1Action(e -> {
            MainFrame.showPanel("Schedule");
        });

        confirmPanel.setBtn2Action(e -> {
            
            if (isRequestSchedule) {
                // Use the new unified method
                if (RequestScheduleDAO.archiveRequestSchedule(
                        schedule.getRoomCode(),
                        schedule.getCourseCode(),
                        requestorID,
                        schedule.getSectionKey(),
                        schedule.getFacultyID(),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeIn()),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeOut()),
                        schedule.getScheduledDay(),
                        isStudentRequestor
                )) {
                    NotificationMessage notif = new NotificationMessage(null, 
                        "Request Schedule archived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, 
                        "Failed to archive Request Schedule.", user);
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
                    NotificationMessage notif = new NotificationMessage("", 
                        "Schedule archived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, 
                        "Failed to archive Schedule.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                }
            }

        });
    }
}