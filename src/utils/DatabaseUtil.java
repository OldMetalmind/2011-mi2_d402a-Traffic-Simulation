package utils;

import java.sql.*;

import dataStructures.GPSSignal;
import dataStructures.Trip;
import dataStructures.Vehicle;

/*
 * Every action/method related to the database, should be here.
 */
public class DatabaseUtil {

	final private double precision = 0.1;

	java.sql.Connection connection;
	
	static private String defaultUrl = "jdbc:postgresql://localhost:5432/project";
	static private String defaultUser = "postgres";
	static private String defaultPassword = "admin";
	
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
		assert(idFrom != idTo): "idFrom and idTo should be different.";
		String sql = "SELECT edge_id, st_astext(startpoint) as start, " +
				"st_astext(endpoint) as end," +
				"hastmax, " +
				"start_id as source, " +
				"end_id as target " +
				"FROM shortest_path('SELECT gid AS id," +
				" start_id::int4 AS source," +
				"  end_id::int4 AS target," +
				"   ST_Length(the_geom)::float8 AS cost" +
				"	FROM network',"+idFrom+ ","+idTo+ ", false, false) as x" +
						" left join network ON (x.edge_id=network.gid)";
				
		System.out.println(sql);
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
		
		//TODO: Try to remove this loop and create a query that does this in only one query.
		
		/*THIS SHOULD BE USED IN GETSHORTESTPATH METHOD
		 * SELECT st_astext(startpoint) as start,
		st_astext(endpoint) as end,
		hastmax,
		start_id as source, 
		end_id as target
			FROM shortest_path('SELECT gid AS id,
					    start_id::int4 AS source,
					    end_id::int4 AS target,
					   ST_Length(the_geom)::float8 AS cost
						FROM network',"+ ","+ " false, false) as x
						 left join network ON (x.edge_id=network.gid);*/

		while(result.next()){
			int id = Integer.parseInt(result.getString("edge_id"));
			if(id == -1)
				continue;
			Integer speedLimit = Integer.parseInt(result.getString("hastmax"));
			trip.addInstance(new GPSSignal(result.getString("start"), trip.getFormat()), speedLimit);
			trip.addInstance(new GPSSignal(result.getString("end"), trip.getFormat()), speedLimit);
		}
		statement.close();
		result.close();
		assert(trip.size() > 0): "Trip should return something with";
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
		
		Double lat = signal.getLatitude();
		Double lng = signal.getLongitude();
		int bboxsize = 100;
		
		String sql= "SELECT ST_Distance(ST_MakePoint("+lat + "," + lng +
			")::geometry, ST_astext(the_geom)::geometry) as x,id " +
					"FROM network " +
					"WHERE ST_intersects(ST_MakeBox2D(ST_Point(" +(lat-bboxsize) + ","+(lng-bboxsize)
					+"),ST_Point(" +(lat+bboxsize)  + ","+(lng+bboxsize)+
					")),the_geom)" +
					" ORDER BY x asc limit 1;";
		
		System.out.println("DatabaseUtil\n"+ sql);
				
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		result.next();		
		int id = Integer.parseInt(result.getString("id"));	
		
		result.close();
		statement.close();	
		return id;
	}
}
