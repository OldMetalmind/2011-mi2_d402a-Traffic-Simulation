package tests;

import java.sql.*;

import project.AllVehicles;
import project.Road;
import project.UserInput;
import project.Vehicle;

import traffic.*;
import uk.me.jstott.jcoord.*;
import utils.Output;

public class mainTest {
	private static Trip UTMtrip;
	private static Trip GWS84trip;
	
	public static void main(String args[]) {
		test();
	}
	/*
	 * At this moment what  the code does is to run a certain query for the shortest path between node 1 and 2,
	 * we calculate the various multilinestrings, find the GPS positioning of them and generate the 
	 * GPS signals of them.
	 * All this is output into a text.kml file to that it can be visible on Google Earth. 
	 */
	public static void test() {
				
		java.sql.Connection conn;
		
		String q = "SELECT * FROM shortest_path(' SELECT gid AS id, start_id::int4 AS source, end_id::int4 AS target, ST_Length(the_geom)::float8 AS cost FROM network', 1, 2, false, false);";
		UTMtrip = new Trip();
		try {
			String url = "jdbc:postgresql://localhost:5432/project";
			conn = DriverManager.getConnection(url, "postgres", "123");
			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery(q);
			while(r.next()){
				String vertex_id = r.getString(2);
				getPoints(conn,vertex_id);
			}
			s.close();
			conn.close();
		}
		catch( Exception e){
			e.printStackTrace();
		}	
		UTM2GWS84(UTMtrip);
		
		//save the shortest path to a vehicle and then add to the list of all vehicles;
		System.out.println("VehicleId ="+ GWS84trip.toString());
		Vehicle vehicleA = new Vehicle(GWS84trip.toString(), GWS84trip, "GWS84");
		AllVehicles everything = new AllVehicles();
		everything.addVehicle(vehicleA);
		
		Output o = new Output("test");
		o.savePathToKML(GWS84trip);
		System.out.println(GWS84trip.toString());
	}
	
	private static void getPoints(Connection conn, String roadID){
		String query = "Select ST_asText(the_geom), hastmax from network where id="+roadID;
		try {
			
			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery(query);
			while(r.next()){	
				Road road = new Road(roadID, Integer.parseInt(r.getString(2)), r.getString(1));
				road2points(road);
			}
			s.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
	}

	private static void road2points(Road road) {
		String[] res = road.getGeomText().split("[,]|[((]|[))]");
		for(int i = 2; i < res.length; i++){
			GPSSignal g = new GPSSignal(res[i]);
			UTMtrip.addInstance(g);
		}
	}
	
	private static void UTM2GWS84(Trip path){
		GWS84trip = new Trip(); 
		for(int i = 0; i < path.size(); i++){
			UTMRef utm = new UTMRef( path.getInstance(i).getLatitude().intValue(),path.getInstance(i).getLongitude().intValue(),'N', 32);			
			LatLng ll = utm.toLatLng();
			GPSSignal s = new GPSSignal(ll.toString(),"GWS84");
			GWS84trip.addInstance(s);			
		}
	}	
}
