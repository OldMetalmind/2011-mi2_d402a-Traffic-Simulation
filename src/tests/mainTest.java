package tests;

import java.sql.*;

import traffic.*;
import uk.me.jstott.jcoord.*;
import utils.Output;

public class mainTest {
	private static Path UTMpath;
	private static Path GWS84path;
	
	
	/*
	 * At this moment what  the code does is to run a certain query for the shortest path between node 1 and 2,
	 * we calculate the various multilinestrings, find the GPS positioning of them and generate the 
	 * GPS signals of them.
	 * All this is output into a text.kml file to that it can be visible on Google Earth. 
	 */
	public static void main(String[] args) {
		
		java.sql.Connection conn;
		String q = "SELECT * FROM shortest_path(' SELECT gid AS id, start_id::int4 AS source, end_id::int4 AS target, ST_Length(the_geom)::float8 AS cost FROM network', 1, 2, false, false);";
		UTMpath = new Path();
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
		UTM2GWS84(UTMpath);
		
		Output o = new Output("test");
		o.savePathToKML(GWS84path);
		System.out.println(GWS84path.toString());
	}
	
	private static void getPoints(Connection conn, String vertex){
		String query = "Select ST_asText(the_geom) from network where id="+vertex;
		try {
			
			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery(query);
			while(r.next()){				
				String multilinestring = r.getString(1);
				multilinestring2points(multilinestring);
			}
			s.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
	}

	private static void multilinestring2points(String multilinestring) {
		String[] res = multilinestring.split("[,]|[((]|[))]");
		for(int i = 2; i < res.length; i++){
			GPSSignal g = new GPSSignal(res[i]);
			UTMpath.addInstance(g);
		}
	}
	
	private static void UTM2GWS84(Path path){
		GWS84path = new Path(); 
		for(int i = 0; i < path.size(); i++){
			UTMRef utm = new UTMRef( path.getInstance(i).getLatitude().intValue(),path.getInstance(i).getLongitude().intValue(),'N', 32);			
			LatLng ll = utm.toLatLng();
			GPSSignal s = new GPSSignal(ll.toString(),"GWS84");
			GWS84path.addInstance(s);			
		}
	}	
}
