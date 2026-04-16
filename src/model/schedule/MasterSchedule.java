package model.schedule;

public class MasterSchedule extends Schedule{
    @Override
    public void load(int ID, String roomCode, String sectionKey, String courseCode, String facultyID, String timeIn,
            String timeOut, String scheduledDay, String status, int isArchived) {

                super.load(ID, roomCode, sectionKey, courseCode, facultyID, timeIn, timeOut, scheduledDay, status, isArchived);
    }
}
