package controller.faculty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.schedule.ScheduleDAO;
import model.schedule.Schedule;
import model.user.Faculty;
import utilities.DateTimeBuilder;
import view.common.MainFrame;
import view.common.ViewSchedule;

public class ViewScheduleController {

    Faculty faculty;

    ViewScheduleController(Faculty faculty) {
        this.faculty = faculty;
        ScheduleDAO dao = new ScheduleDAO();

        String today = DateTimeBuilder.getDayName().substring(0, 3);
        List<Schedule> schedules = new ArrayList<>();
        try {
            schedules = ScheduleDAO.getFacultySchedulesByDay(faculty, today);
        } catch (SQLException e) {
            e.printStackTrace();
        } // this is for the default schedule that will be displayed at the launch of the controller


        ViewSchedule view = new ViewSchedule(schedules);
        view.loadFacultySchedule(schedules);

        MainFrame.addContentPanel(view, "ViewSchedule");
        MainFrame.showPanel("ViewSchedule");
    }
}
