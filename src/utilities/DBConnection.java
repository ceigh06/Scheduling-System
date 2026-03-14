package utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
	//LAPTOP-1K042OSK\\SQLEXPRESS:1433;databaseName=Cafe;encrypt=true;trustServerCertificate=true
	static String url = "jdbc:sqlserver://";
	static public String user;
	static String pass;

	// public DBConnection(){
	// 	url += "LAPTOP-81CGQV8U\\SQLEXPRESS;databaseName=SchedulingSystem;encrypt=true;trustServerCertificate=true";
	// 	this.user = "sa";
	// 	this.pass = "a";
	// }

	public DBConnection(String serverName, String databaseName, String user, String pass) throws SQLException{ // sets up the db 
		url += serverName + ";databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true";
		this.user = user;
		this.pass = pass;
	}
	
	public static Connection connect() throws SQLException { // only used for connecting the db. basically a plug to use the db
		return DriverManager.getConnection(url,user,pass);
	}
	
	public void disconnect(Connection db) throws SQLException {
		db.close();
	}
	
	
}