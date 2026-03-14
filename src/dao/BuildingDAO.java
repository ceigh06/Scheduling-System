package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Building;
import utilities.DBConnection;

public class BuildingDAO {
    private static Connection connection;
    public BuildingDAO() throws SQLException {
        this.connection = new DBConnection("LAPTOP-81CGQV8U\\SQLEXPRESS", "SchedulingSystem", "sa", "a").connect();
    }
    Building get(String buildingCode) throws SQLException{
        

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Building WHERE BuildingCode = ?");
        stmt.setString(1, buildingCode);
        ResultSet set = stmt.executeQuery();
        
        set.next(); 

        String name = set.getString("BuildingName");

        Building building = new Building();
        building.load(buildingCode, name);

        return building;
    }

    public List<Building> getAllBuilding() throws SQLException{
        List<Building> allBuildings = new ArrayList<>();


        Statement stmt = connection.createStatement();
        ResultSet set = stmt.executeQuery("SELECT * FROM Building");
        
        while(set.next()){
            Building building = new Building();
            building.load(set.getString("BuildingCode"), set.getString("BuildingName"));
            allBuildings.add(building); 
        }
        
        
        return allBuildings;
    }
}
