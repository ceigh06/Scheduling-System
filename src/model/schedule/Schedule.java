package model.schedule;

public class Schedule {
    private int ID;
    private String roomCode;
    private String sectionKey;
    private String courseCode;
    private String facultyID;
    private String timeIn;
    private String timeOut;
    private String status;
    private String scheduledDay;
    private int isArchived;

    public void load(int ID, String roomCode, String sectionKey, String courseCode, String facultyID, String timeIn,
            String timeOut, String scheduledDay, String status, int isArchived) {
        this.ID = ID;
        this.roomCode = roomCode;
        this.sectionKey = sectionKey;
        this.courseCode = courseCode;
        this.facultyID = facultyID;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = status;
        this.scheduledDay = scheduledDay;
        this.isArchived = isArchived;
    }

    public int getID() {
        return ID;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getSectionKey() {
        return sectionKey;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public String getStatus() {
        return status;
    }

    public String getScheduledDay() {
        return scheduledDay;
    }

    public int getIsArchived() {
        return isArchived;
    }

}
