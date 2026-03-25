package model;

import java.util.List;

import model.schedule.Schedule;

public class Room {
    private String roomCode = "";
    private String buildingCode = "";
    private int floor = 0;
    private int capacity = 0;
    private String status = "";
    private List<Schedule> schedules;

    public void load(String roomCode, String buildingCode, int floor, int capacity, String status,List<Schedule> schedules){
        this.roomCode = roomCode;
        this.buildingCode = buildingCode;
        this.floor = floor;
        this.capacity = capacity;
        this.status = status;
        this.schedules = schedules;
    }
    
    public List<Schedule> getSchedules(){
        return schedules;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public int getFloor() {
        return floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }


}
