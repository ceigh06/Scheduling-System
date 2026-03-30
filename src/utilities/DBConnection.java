package utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
	//user;databasename;encrypt=true;trustServerCertificate=true

	static String url = "jdbc:sqlserver://";
	static public String user;
	static String pass;
	static Connection connection;

	public DBConnection(String serverName, String databaseName, String user, String pass) throws SQLException{ // sets up the db 
		url += serverName + ";databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true";
		this.user = user;
		this.pass = pass;
		connection = DriverManager.getConnection(url,user,pass); // connection is established here, if it fails, an exception is thrown and the program will crash. this is intentional because the program cannot run without a db connection. if the connection is successful, the connection object is stored in the class variable for later use.
	}

	public DBConnection(){}

	public static  Connection getConnection() throws SQLException { // only used for connecting the db. basically a plug to use the db
		return DriverManager.getConnection(url,user,pass);
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	
}