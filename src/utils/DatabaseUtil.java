package utils;

import java.sql.*;

import traffic.GPSSignal;
import traffic.Trip;

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
	 * Finds the shortest path between two points and return the trip
	 */
	public Trip getShortestPath(GPSSignal from, GPSSignal to){
		
		//TODO Find the closest position in a road that is closest to the GPSSignal from (?);		
		String q = "SELECT * FROM shortest_path(' SELECT gid AS id, start_id::int4 AS source, end_id::int4 AS target, ST_Length(the_geom)::float8 AS cost FROM network', 1, 2, false, false);";
		Trip UTMtrip = new Trip();
		try {
			//String url = "jdbc:postgresql://localhost:5432/project";
			//conn = DriverManager.getConnection(url, "postgres", "123");
			//Statement s = conn.createStatement();
			Statement s = this.connection.createStatement();
	
			ResultSet r = s.executeQuery(q);
			while(r.next()){
				String road_id = r.getString(2);
				//getPoints(connection,road_id);
			}
			s.close();
			connection.close();
		}
		catch( Exception e){
			e.printStackTrace();
		}	
		//UTM2GWS84(UTMtrip);
		return null;
	}
}
