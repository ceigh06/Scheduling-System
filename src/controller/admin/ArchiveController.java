package controller.admin;

import controller.shared.RoomsController;
import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import java.sql.SQLException;
import model.Room;
import model.schedule.Schedule;
import model.user.User;
import view.common.ConfirmPanel;
import view.common.MainFrame;
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
                        schedule.getTimeIn(),
                        schedule.getTimeOut(),
                        schedule.getScheduledDay()
                )) {
                    System.out.println(schedule.getID());
                    System.out.println("Request Schedule unarchived successfully.");
                } else {
                    System.out.println("Failed to unarchive Schedule.");
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
                    System.out.println("Schedule unarchived successfully.");
                } else {
                    System.out.println("Failed to unarchive Schedule.");
                }
            }

            try {
                new AdminController(user);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }
}