package controller.admin;

import model.Room;
import model.schedule.Schedule;
import model.user.User;

public class EditRoomScheduleController {

    private Schedule schedule;
    private Room room;
    User user;

    public EditRoomScheduleController(Schedule schedule, Room room, User user) {
        this.schedule = schedule;
        this.room = room;
        this.user = user;
        System.out.println("I am in lol controller");

    }
    
}
