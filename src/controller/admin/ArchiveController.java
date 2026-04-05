package controller.admin;

import controller.shared.RoomsController;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import java.sql.SQLException;
import model.Room;
import model.schedule.Schedule;
import model.user.User;
import utilities.DateTimeBuilder;
import view.common.ConfirmPanel;
import view.common.MainFrame;
import view.common.NotificationMessage;
import view.common.RequestForm;

public class ArchiveController {

    private Schedule schedule;
    private Room room;
    User user;

    public ArchiveController(User user) throws SQLException {
        new RoomsController(user, true);
    }

    public ArchiveController(User user, Schedule schedule, Room room, Boolean viewArchives) throws SQLException {
        this.schedule = schedule;
        this.room = room;
        this.user = user;
        if (viewArchives) {
            showArchiveForm(schedule, user, viewArchives);
        } else {
            new EditScheduleController(schedule, room, user);
        }
    }

    public void showArchiveForm(Schedule schedule, User user, Boolean viewArchives) throws SQLException {
        RequestForm requestForm = new RequestForm(schedule, user, viewArchives);

        // Add to MainFrame
        MainFrame.addContentPanel(requestForm, "UnarchiveForm");
        MainFrame.showPanel("UnarchiveForm", "Unarchive Schedule");

        ConfirmPanel confirmPanel = requestForm.getReqConfirm();

        confirmPanel.setBtn1Action(e -> {
            MainFrame.showPanel("Schedule");
        });

        confirmPanel.setBtn2Action(e -> {

            if (requestForm.isRequest) {
                RequestScheduleDAO requestScheduleDAO = new RequestScheduleDAO();

                if (RequestScheduleDAO.unarchiveStudentSchedule(
                        schedule.getRoomCode(),
                        schedule.getCourseCode(),
                        requestForm.studentNumber,
                        schedule.getSectionKey(),
                        schedule.getFacultyID(),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeIn()),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeOut()),
                        schedule.getScheduledDay()
                )) {
                    NotificationMessage notif = new NotificationMessage(null, "Request Schedule unarchived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, "Failed to unarchive Request Schedule.", user);
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
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeIn()),
                        DateTimeBuilder.formatTo12Hour(schedule.getTimeOut()),
                        schedule.getScheduledDay()
                )) {
                    NotificationMessage notif = new NotificationMessage("", "Schedule unarchived successfully.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif");
                } else {
                    NotificationMessage notif = new NotificationMessage(null, "Failed to unarchive Schedule.", user);
                    MainFrame.addContentPanel(notif, "Notif");
                    MainFrame.showPanel("Notif", "Notification");
                }
            }

        });
    }
}