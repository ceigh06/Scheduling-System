package controller.admin;

import controller.shared.RoomsController;
import dao.LookUpDAO;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import java.sql.SQLException;
import model.schedule.RequestSchedule;
import model.schedule.Schedule;
import model.user.User;
import view.common.ConfirmPanel;
import view.common.MainFrame;
import view.common.NotificationMessage;
import view.common.RequestForm;

public class ArchiveController {

    private Schedule schedule;
    User user;
    static String requestorID = null;
    static String requestorName = null;
    static boolean isRequestSchedule = false;
      static  boolean isStudentRequestor = false;

    public ArchiveController(User user) throws SQLException {
        new RoomsController(user, true);
    }

    public ArchiveController(User user, Schedule schedule, Boolean viewArchives) throws SQLException {
        this.schedule = schedule;
        this.user = user;
        if (viewArchives) {
            showArchiveForm(schedule, user, viewArchives);
        } else {
            new EditScheduleController(schedule, user);
        }
    }

    public void showArchiveForm(Schedule schedule, User user, Boolean viewArchives) throws SQLException {
        
        LookUpDAO lookUpDAO = new LookUpDAO();
        String fullSectionName = lookUpDAO.getFullSectionName(Integer.parseInt(schedule.getSectionKey()));

        // Determine if this is a request schedule
        requestorID = null;
        requestorName = null;
        isRequestSchedule = false;
        isStudentRequestor = false;
        
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
                    requestorName = lookUpDAO.getFullFacultyName(requestorID);
                }
            }
        }

        RequestForm requestForm = new RequestForm(schedule, user, viewArchives,
                requestorID, requestorName, fullSectionName, isStudentRequestor);

        // Add to MainFrame
        MainFrame.addContentPanel(requestForm, "UnarchiveForm");
        MainFrame.showPanel("UnarchiveForm", "Unarchive Schedule");

        ConfirmPanel confirmPanel = requestForm.getReqConfirm();

        confirmPanel.setBtn1Action(e -> {
            MainFrame.showPanel("ViewSchedule","Unarchive Schedule");
        });

        confirmPanel.setBtn2Action(e -> {

            if (isRequestSchedule) {
                if (RequestScheduleDAO.unarchiveRequestSchedule(
                        schedule.getRoomCode(),
                        schedule.getCourseCode(),
                        requestorID,
                        schedule.getSectionKey(),
                        schedule.getFacultyID(),
                        schedule.getTimeIn(),  // Keep original format for unarchive
                        schedule.getTimeOut(),
                        schedule.getScheduledDay()
                )) {
                    NotificationMessage notif = new NotificationMessage(null, 
                        "Request Schedule unarchived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, 
                        "Failed to unarchive Request Schedule.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                }

            } else {
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                if (scheduleDAO.unarchiveSchedule(
                        schedule.getRoomCode(),
                        schedule.getCourseCode(),
                        schedule.getSectionKey(),
                        schedule.getFacultyID(),
                        schedule.getTimeIn(),
                        schedule.getTimeOut(),
                        schedule.getScheduledDay()
                )) {
                    NotificationMessage notif = new NotificationMessage("", 
                        "Schedule unarchived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, 
                        "Failed to unarchive Schedule.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                }
            }

        });
    }
}