package model.schedule;

public class RequestSchedule extends Schedule {
    private String dateRequested;
    private String studentNumber;

    public void load(int ID, String roomCode, String sectionKey, String courseCode, String facultyID, String timeIn,
            String timeOut, String scheduledDay, String status, int isArchived, String dateRequested, String studentNumber) {
        // TODO Auto-generated method stub
        super.load(ID, roomCode, sectionKey, courseCode, facultyID, timeIn, timeOut, scheduledDay, status, isArchived);
        this.dateRequested = dateRequested;
        this.studentNumber = studentNumber;
    }

    public void setRoomCode(String roomCode) {
        super.setRoomCode(roomCode);
    }


    public String getDateRequested(){
        return dateRequested;
    }

    public String getStudentRequested(){
        return studentNumber;
    }
}
