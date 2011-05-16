package utils;

import java.sql.*;
import java.util.Vector;

import dataStructures.AllVehicles;
import dataStructures.GPSSignal;
import dataStructures.ShortestPath;
import dataStructures.Trip;
import dataStructures.Vehicle;
import dataStructures.Voyage;

/*
 * Every action/method related to the database, should be here.
 */
public class DatabaseUtil {

	java.sql.Connection connection;

	static private String defaultUrl = "jdbc:postgresql://localhost:5432/project";
	static private String defaultUser = "postgres";
	static private String defaultPassword = "admin";

	public DatabaseUtil() {
		try {
			this.connection = DriverManager.getConnection(defaultUrl,
					defaultUser, defaultPassword);
		} catch (Exception e) {
			try {
				this.connection = DriverManager.getConnection(defaultUrl,
						defaultUser, "123");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public DatabaseUtil(String url, String user, String password) {
		try {
			this.connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Finds the shortest path between two points and return the Trip
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws SQLException
	 */
	public ShortestPath getShortestPath(GPSSignal from, GPSSignal to)
			throws SQLException {
		// TODO: Do we need to make sure they are different?
		/*
		 * if(from.equals(to)) return new Trip(to.getFormat());
		 */
		long t0 = System.currentTimeMillis();
		int idFrom = getClosestPoint(from);
		int idTo = getClosestPoint(to);
		int bbox = 1000; // set a bounding box containing start and end vertex
		// plus a 0.1 degree buffer for example.
		assert (idFrom != idTo) : "idFrom and idTo should be different.";
		String sql = "SELECT st_astext(startpoint) as start, "
				+ "st_astext(endpoint) as end,hastmax,"
				+ "  start_id as source, end_id as target "
				+ "FROM dijkstra_sp_delta('network'," + idFrom + "," + idTo
				+ "," + bbox + ") as x "
				+ "left join network ON (x.gid=network.gid)";

		// System.out.println(sql);
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);

		System.out.println("Execution time: "
				+ (System.currentTimeMillis() - t0) + "miliceconds");
		return resultSet2ShortestPath(result);
	}

	public void clearVehicles() {
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
		String sql = "INSERT INTO vehicles VALUES (" + vehicle_id
				+ ", ST_SetSRID(" + v.getActualPositionUTM() + ",4236))";
		try {
			Statement statement = this.connection.createStatement();
			statement.executeQuery(sql);
			v.setVehicle_id(vehicle_id);
			statement.close();
		} catch (SQLException e) {
			// The query should always returns nothing;
		}
	}

	/**
	 * 
	 * @param result
	 * @return Trip in GSW84 format
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	private ShortestPath resultSet2ShortestPath(ResultSet result)
			throws NumberFormatException, SQLException {
		// vertex_id | edge_id | cost
		ShortestPath path = new ShortestPath("UTM");
		Statement statement = this.connection.createStatement();
		result.next();
		Integer speedLimit = Integer.parseInt(result.getString("hastmax"));
		path.addInstance(new GPSSignal(result.getString("start"), path.getFormat()), speedLimit);
		//path.addInstance(new GPSSignal(result.getString("end"), path.getFormat()), speedLimit);
		while (result.next()) {
			speedLimit = Integer.parseInt(result.getString("hastmax"));
			path.addInstance(new GPSSignal(result.getString("start"), path.getFormat()), speedLimit);
			//path.addInstance(new GPSSignal(result.getString("end"), path.getFormat()), speedLimit);
		}
		statement.close();
		result.close();
		System.out.println("DatabaseUtil | "+path);
		assert (path.size() > 0 && path.getInstance(0) != null) : "Trip should return something with";
		return path;
	}

	/**
	 * 
	 * @param signal
	 * @return the id of the_geom closest to the signal given
	 * @throws SQLException
	 */
	private Integer getClosestPoint(GPSSignal signal) throws SQLException {
		if (signal.getFormat() == "LatLon") {
			signal = Utils.LatLon2UTM(signal);
		}

		Double lat = signal.getLatitude();
		Double lng = signal.getLongitude();
		int bboxsize = 100;

		String sql = "SELECT ST_Distance(ST_MakePoint(" + lat + "," + lng
				+ ")::geometry, ST_astext(the_geom)::geometry) as x,id "
				+ "FROM network "
				+ "WHERE ST_intersects(ST_MakeBox2D(ST_Point("
				+ (lat - bboxsize) + "," + (lng - bboxsize) + "),ST_Point("
				+ (lat + bboxsize) + "," + (lng + bboxsize) + ")),the_geom)"
				+ " ORDER BY x asc limit 1;";

		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		result.next();
		int id = Integer.parseInt(result.getString("id"));
		result.close();
		statement.close();
		return id;
	}

	public void clearSignals() {
		String sql = "DELETE FROM signals";

		Statement statement;
		try {
			statement = this.connection.createStatement();
			statement.executeQuery(sql);
			statement.close();
		} catch (SQLException e) {
			return;
		}
	}

	public void addSignals( Vehicle v) {
		assert (v.getVoyage().getPath().size() == v.getVoyage().getTimes()
				.size());
		int signals = v.getVoyage().getPath().size();
		System.out.println(v.getVoyage().getTimes());

		String sql = "INSERT INTO signals (vehicle_id, location, time) VALUES";
		for (int i = 0; i < signals; i++) {
			GPSSignal position = v.getVoyage().getPath().elementAt(i);
			double time = v.getVoyage().getTimeAt(i);
			sql += "(" + v.getVehicle_id() + "," + " ST_SetSRID(ST_MakePoint("
					+ position.getLatitude() + "," + position.getLongitude()
					+ ")::geometry,4326)," + time + "),";

		}
		sql = sql.substring(0, sql.length() - 1);// Removing last comma from the
		// string
		try {
			Statement statement = this.connection.createStatement();
			statement.executeQuery(sql);
			statement.close();
		} catch (SQLException e) {
			// this shouldn't return nothing... it's ok..
		}
	}

	public GPSSignal lineInterpolatePoint(GPSSignal to, GPSSignal from, float percentage) {
		GPSSignal point = null;
		String sql = "SELECT ST_asText(ST_Line_Interpolate_Point(line,0.164511)) as point FROM ST_SetSRID( 'LINESTRING("+to.getLatitude()+" "+to.getLongitude()+","+from.getLatitude()+" "+from.getLongitude()+")'::geometry , 4326 ) as line;";
		try {
			Statement statement = this.connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			result.next();
			point = new GPSSignal(result.getString("point"), "UTM");
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		assert (point != null && point.getFormat() == "UTM");
		return point;
	}

	public void save(AllVehicles vehicles) {
		// TODO Save all vehicles to table: signals.
		// Call methods clearSignals and addSignals(id, vehicle) from here,
		// because now it's called from TrafficAlgorithm
		clearSignals();
		for (Vehicle v : vehicles.getVehicles()) {
			
			addSignals(v); 
			System.out.println("id > " + v.getVehicle_id() + " |"
					+ v.getVoyage());
		}
	}

	public void setVehicle(int vehicle_id, GPSSignal newPosition) {
		// TODO Deprecated method?
		
	}
}
