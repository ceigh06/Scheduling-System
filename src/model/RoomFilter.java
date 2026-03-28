package model;

import java.util.List;

public class RoomFilter {
    private List<Building> buildings;
    private String timeIn;
    private String timeOut;
    private Course course;
    private String floor;
    private String capacity;
    
    public void load(List<Building> buildings, String timeIn, String timeOut, Course course, String floor,
            String capacity) {
        this.buildings = buildings;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.course = course;
        this.floor = floor;
        this.capacity = capacity;
    }
    public List<Building> getBuildings() {
        return buildings;
    }
    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
    public String getTimeIn() {
        return timeIn;
    }
    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }
    public String getTimeOut() {
        return timeOut;
    }
    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }
    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }
    public String getCapacity() {
        return capacity;
    }
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }


}
