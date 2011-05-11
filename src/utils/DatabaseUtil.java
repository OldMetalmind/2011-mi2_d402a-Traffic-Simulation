package utils;

import java.sql.*;

import dataStructures.GPSSignal;
import dataStructures.Trip;
import dataStructures.Vehicle;

/*
 * Every action/method related to the database, should be here.
 */
public class DatabaseUtil {

	private static final double precision = 0.1;

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
	
	/**
	 * Finds the shortest path between two points and return the Trip
	 * @param from
	 * @param to
	 * @return
	 * @throws SQLException
	 */
	public Trip getShortestPath(GPSSignal from, GPSSignal to) throws SQLException{		
		if(from.equals(to))
			return new Trip(to.getFormat());
			
		int idFrom = getClosestPoint(from);
		int idTo = getClosestPoint(to);		
		
		System.out.println("DbUtil| from: "+idFrom+"  to: "+idTo );
		String sql = "SELECT * FROM shortest_path(' " +
				" SELECT gid AS id, " +
				" start_id::int4 AS source, " +
				" end_id::int4 AS target, " +
				" ST_Length(the_geom)::float8 AS cost " +
				" FROM network', "+idFrom+", "+idTo+", false, false);";
				
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		return resultSet2Trip(result);
	}
	
	public void clearVehicles(){
		String sql = "DELETE FROM vehicles";
		
		Statement statement;
		try {
			statement = this.connection.createStatement();
			statement.executeQuery(sql);
			statement.close();		
		} catch (SQLException e) {
			return;
		}
	}

	public void addVehicle(Vehicle v, int vehicle_id) {
		String sql = "INSERT INTO vehicles VALUES ("+vehicle_id+", ST_SetSRID("+ v.getActualPositionUTM()+",4236))";
		try {
			Statement statement = this.connection.createStatement();
			statement.executeQuery(sql);
			v.setVehicle_id(vehicle_id);
			statement.close();
		} catch (SQLException e) {
			//The query never returns nothing;
		}
		
	}	
	/**
	 * 
	 * @param result
	 * @return Trip in GSW84 format
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */ 
	private Trip resultSet2Trip(ResultSet result) throws NumberFormatException, SQLException{
		//vertex_id | edge_id | cost
		Trip trip = new Trip("UTM");
		Statement statement = this.connection.createStatement();
		while(result.next()){
			int id = Integer.parseInt(result.getString("edge_id"));
			if(id == -1)
				break;
			String sql = 	"SELECT ST_asText(startpoint) AS start, " +
								" ST_asText(endpoint) AS end, " +
								" hastmax" +
							" FROM network WHERE id = "+id+";";
			
			ResultSet rst = statement.executeQuery(sql);
			rst.next();			
			Integer speedLimit = Integer.parseInt(rst.getString("hastmax"));
			trip.addInstance(new GPSSignal(rst.getString("start"), trip.getFormat()), speedLimit);
			trip.addInstance(new GPSSignal(rst.getString("end"), trip.getFormat()), speedLimit);
			rst.close();
			
		}
		statement.close();
		result.close();

		return trip;
	}
	/**
	 * 
	 * @param signal
	 * @return the id of the_geom closest to the signal given
	 * @throws SQLException
	 */
	private Integer getClosestPoint(GPSSignal signal) throws SQLException{
		if(signal.getFormat() == "LatLon"){
			signal = Utils.LatLon2UTM(signal);
		}

		String sql= "SELECT ST_Distance(ST_MakePoint("+signal.getLatitude()+"," +signal.getLongitude()+
				")::geometry, ST_astext(the_geom)::geometry) as x,* from network order by x asc limit 1;";
				
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		result.next();
		int id = Integer.parseInt(result.getString("id"));		
		result.close();
		statement.close();	
		return id;
	}
}
