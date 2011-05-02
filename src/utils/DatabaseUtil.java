package utils;

import java.sql.*;

import dataStructures.GPSSignal;
import dataStructures.Trip;

/*
 * Every action/method related to the database, should be here.
 */
public class DatabaseUtil {

	java.sql.Connection connection;
	
	static private String defaultUrl = "jdbc:postgresql://localhost:5432/project";
	static private String defaultUser = "postgres";
	static private String defaultPassword = "123";
	
	public DatabaseUtil(){
		try{
			connection = DriverManager.getConnection(defaultUrl, defaultUser, defaultPassword);
		}
		catch( Exception e){
			e.printStackTrace();
		}	
	}
	
	public DatabaseUtil(String url, String user, String password){
		try{
			connection = DriverManager.getConnection(url, user, password);
			
		}
		catch( Exception e){
			e.printStackTrace();
		}	
	}
	
	public ResultSet query(String query){
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return result;
	}	
	
	
	
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Finds the shortest path between two points and return the Trip
	 */
	public Trip getShortestPath(GPSSignal from, GPSSignal to){		
		//TODO Find the shortest trip between from and to points		
		return null;
	}
}
