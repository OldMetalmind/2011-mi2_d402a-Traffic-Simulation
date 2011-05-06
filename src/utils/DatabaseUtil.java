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
	public Trip getShortestPath(GPSSignal from, GPSSignal to) throws SQLException{		
		if(from.equals(to))
			return new Trip(to.getFormat());
			
		
		int idFrom = getClosestPoint(from);
		int idTo = getClosestPoint(to);
		
		System.out.println(idFrom+" "+idTo);
		
		String sql = "SELECT * FROM shortest_path(' " +
				" SELECT gid AS id, " +
				" start_id::int4 AS source, " +
				" end_id::int4 AS target, " +
				" ST_Length(the_geom)::float8 AS cost " +
				" FROM network', "+idFrom+", "+idTo+", false, false);";
		
		System.out.println(sql);
		
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		return resultSet2Trip(result);
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
		if(signal.getFormat() == "LatLon")			
			signal = Utils.LatLon2UTM(signal);	
		
		//System.out.println(signal.getLongitude().intValue()+" "+signal.getLatitude().intValue());
		String sql = 	"SELECT * " +
						"FROM (" +
								" SELECT " +
								"	ST_MakePoint("+signal.getLongitude()+","+signal.getLatitude()+") AS pt " +
								" ) AS foo," +
								" network as f, "+
								" network as g " +
								" WHERE ST_Distance(ST_ClosestPoint(f.the_geom, pt), pt) " +
								"	< ST_Distance(ST_ClosestPoint(g.the_geom, pt), pt) " +
						"LIMIT 1;";
				
		//System.out.println(sql);
		
		int id = -1;
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		result.next();
		id = Integer.parseInt(result.getString("start_id"));		
		result.close();
		statement.close();	
		return id;
	}
}
