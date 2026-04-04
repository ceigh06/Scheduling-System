package controller.admin;

import dao.schedule.RequestScheduleDAO;
import dao.schedule.ScheduleDAO;
import model.Room;
import model.schedule.Schedule;
import model.user.User;
import view.common.ConfirmPanel;
import view.common.MainFrame;
import view.common.RequestForm;

public class EditScheduleController {

    private Schedule schedule;
    private Room room;
    User user;

    public EditScheduleController(Schedule schedule, Room room, User user) {
        this.schedule = schedule;
        this.room = room;
        this.user = user;
        showArchiveForm(schedule, user);

    }

    public void showArchiveForm(Schedule schedule, User user) {
        RequestForm requestForm = new RequestForm(schedule, user);

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
                        schedule.getTimeIn(),
                        schedule.getTimeOut(),
                        schedule.getScheduledDay()
                )) {
                    System.out.println(schedule.getID());
                    System.out.println("Request Schedule archived successfully.");
                } else {
                    System.out.println("Failed to archive Schedule.");
                }

            } else {
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                if (scheduleDAO.archiveSchedule(
                        schedule.getRoomCode(),
                        schedule.getCourseCode(),
                        schedule.getSectionKey(),
                        schedule.getFacultyID(),
                        schedule.getTimeIn(),
                        schedule.getTimeOut(),
                        schedule.getScheduledDay()
                )) {
                    System.out.println("Schedule archived successfully.");
                } else {
                    System.out.println("Failed to archive Schedule.");
                }
            }

            new AdminController(user);
        });
    }

}
