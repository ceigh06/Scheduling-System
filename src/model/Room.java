package model;

public class Room {
    private String roomCode = "";
    private String buildingCode = "";
    private int floor = 0;
    private int capacity = 0;
    private String status = "";

    public void load(String roomCode, String buildingCode, int floor, int capacity, String status){
        this.roomCode = roomCode;
        this.buildingCode = buildingCode;
        this.floor = floor;
        this.capacity = capacity;
        this.status = status;
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
