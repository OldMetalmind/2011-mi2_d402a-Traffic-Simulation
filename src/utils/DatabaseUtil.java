package utils;

import java.sql.*;

import dataStructures.AllVehicles;
import dataStructures.GPSSignal;
import dataStructures.ShortestPath;
import dataStructures.Vehicle;

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
			this.connection = DriverManager.getConnection(defaultUrl, defaultUser, defaultPassword);
		}
		catch( Exception e){
			try {
				this.connection = DriverManager.getConnection(defaultUrl, defaultUser, "admin");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}	
	}
	
	public DatabaseUtil(String url, String user, String password){
		try{
			this.connection = DriverManager.getConnection(url, user, password);			
		}
		catch( Exception e){
			e.printStackTrace();
		}	
	}
		
	public void closeConnection(){
		try {
			this.connection.close();
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
	public ShortestPath getShortestPath(GPSSignal from, GPSSignal to) throws SQLException{
		//TODO: Do we need to make sure they are different?
		/*if(from.equals(to))
			return new Trip(to.getFormat());*/
			
		int idFrom = getClosestPoint(from);
		int idTo = getClosestPoint(to);		
		assert(idFrom != idTo): "idFrom and idTo should be different.";
		String sql = "SELECT * FROM shortest_path(' " +
				" SELECT gid AS id, " +
				" start_id::int4 AS source, " +
				" end_id::int4 AS target, " +
				" ST_Length(the_geom)::float8 AS cost " +
				" FROM network', "+idFrom+", "+idTo+", false, false);";		
		
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		return resultSet2ShortestPath(result);
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
			//The query should always returns nothing;
		}		
	}	
	/**
	 * 
	 * @param result
	 * @return Trip in GSW84 format
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */ 
	private ShortestPath resultSet2ShortestPath(ResultSet result) throws NumberFormatException, SQLException{
		//vertex_id | edge_id | cost
		ShortestPath path = new ShortestPath("UTM");
		Statement statement = this.connection.createStatement();
		
		//TODO: Try to remove this loop and create a query that does this in only one query.
		while(result.next()){
			int id = Integer.parseInt(result.getString("edge_id"));
			if(id == -1)
				continue;
			String sql = 	"SELECT ST_asText(startpoint) AS start, " +
								" ST_asText(endpoint) AS end, " +
								" hastmax" +
							" FROM network WHERE id = "+id+";";
			
			ResultSet rst = statement.executeQuery(sql);
			rst.next();			
			Integer speedLimit = Integer.parseInt(rst.getString("hastmax"));
			path.addInstance(new GPSSignal(rst.getString("start"), path.getFormat()), speedLimit);
			path.addInstance(new GPSSignal(rst.getString("end"), path.getFormat()), speedLimit);
			rst.close();
		}
		statement.close();
		result.close();
		assert(path.size() > 0 && path.getInstance(0) != null): "Trip should return something with";
		return path;
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
						
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		result.next();		
		int id = Integer.parseInt(result.getString("id"));
		result.close();
		statement.close();	
		return id;
	}

	public void setVehicle(int vehicle_id, GPSSignal newPosition) {		
		String sql = "UPDATE vehicles SET location = ST_MakePoint("+ newPosition.getLatitude() +","+ newPosition.getLongitude()+")::geometry WHERE vehicle_id = "+vehicle_id+" ;";		
		try {
			Statement statement = this.connection.createStatement();
			statement.executeQuery(sql);
			statement.close();
		} catch (SQLException e) {
			//this shouldn't return nothing... it's ok..
		}	
	}

	public GPSSignal lineInterpolatePoint(GPSSignal from, GPSSignal to, float percentage) {
		GPSSignal point = null;
		String sql = "SELECT ST_asText(ST_Line_Interpolate_Point(line,0.164511)) as point FROM ST_SetSRID( 'LINESTRING(548373.46 6296466.44,548239.31 6295999.02)'::geometry , 4326 ) as line;";
		try {
			Statement statement = this.connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			result.next();
			point = new GPSSignal( result.getString("point"), "UTM");			
			result.close();
			statement.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		assert(point != null && point.getFormat() == "UTM");
		return point;
	}

	public void save(AllVehicles vehicles) {
		// TODO Save all vehicles to table: output.
		/*
		for(Vehicle v : vehicles.getVehicles()){
			System.out.println("id > "+ v.getVehicle_id()+" |"+v.getVoyage());
		}
		*/
	}
}
