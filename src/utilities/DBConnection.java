package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
	//user;databasename;encrypt=true;trustServerCertificate=true

	private static String url = "jdbc:sqlserver://";
    private static Connection connection;

	public DBConnection(String serverName, String databaseName, String user, String pass){ // sets up the db 
		url += serverName + ";databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true";
		try {
			connection = DriverManager.getConnection(url,user,pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // connection is established here, if it fails, an exception is thrown and the program will crash. this is intentional because the program cannot run without a db connection. if the connection is successful, the connection object is stored in the class variable for later use.
	}

	public static  Connection getConnection() { // only used for connecting the db. basically a plug to use the db
		return connection;
	}
	
	public static void disconnect()  {
		try{
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}