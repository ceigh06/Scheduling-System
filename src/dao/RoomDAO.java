package dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Room;
import utilities.DBConnection;

public class RoomDAO {
    private static Connection connection;

    public RoomDAO() throws SQLException  {
        connection = DBConnection.getConnection(); // shared connection
    }
    Room get(String roomCode) throws SQLException{

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Room WHERE RoomCode = ?");
        stmt.setString(1, roomCode);
        ResultSet set = stmt.executeQuery();
        set.next(); 

        String buildingCode = set.getString("BuildingCode");
        int floor = set.getInt("Floor");
        int capacity = set.getInt("Capacity");
        String status = set.getString("Status");

        Room room = new Room();
        room.load(roomCode, buildingCode, floor, capacity, status);

        return room;
    }

    public List<Room> getAllRooms(String buildingCode) throws SQLException{
    
        List<Room> allRooms = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Room WHERE BuildingCode = ?");
        stmt.setString(1, buildingCode);
        ResultSet set = stmt.executeQuery();

        while(set.next()){
            Room room = new Room();
            room.load(set.getString("RoomCode"), set.getString("BuildingCode"), set.getInt("Floor"), set.getInt("Capacity"), set.getString("Status"));
            allRooms.add(room);
        }

        return allRooms;
    }

}
