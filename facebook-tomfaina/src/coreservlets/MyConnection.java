package coreservlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
	private Connection connection;
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	
	private static final String DBMS = "mysql";
	private static final String CONNECTOR = "jdbc"; 
	private static final String IP = "localhost"; 
	private static final String PORT = "3306" ; 
	private static final String DB = "FacebookDB";
	private static final String DB_LINK = CONNECTOR +":" + DBMS + "://" + IP + ":" + PORT + "/" + DB;

	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";

	static{
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 

	}
	
	public MyConnection() throws SQLException, Exception {
				
		connection = DriverManager.getConnection(DB_LINK, USERNAME, PASSWORD);
	}


	public Connection getConnection() {
		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void closeConnection() {
		try {
			connection.close();
			System.out.println("Connection closed!--");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
