package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
import dataStructures.GPSSignal;
import dataStructures.Road;
import dataStructures.Trip;

/*
 * Methods that help other classes;
 */
public class Helper {
	
	
	
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

	private static Trip road2points(Road road) {
		Trip UTM_Trip = new Trip("UTM");
		String[] res = road.getGeomText().split("[,]|[((]|[))]");
		for(int i = 2; i < res.length; i++){
			GPSSignal g = new GPSSignal(res[i]);
			UTM_Trip.addInstance(g);
		}
		return UTM_Trip;
	}
	
	private static Trip UTM2GWS84(Trip UTMTrip){
		Trip GWS84trip = new Trip("GWS84"); 
		for(int i = 0; i < UTMTrip.size(); i++){
			UTMRef utm = new UTMRef( UTMTrip.getInstance(i).getLatitude().intValue(),UTMTrip.getInstance(i).getLongitude().intValue(),'N', 32);			
			LatLng ll = utm.toLatLng();
			GPSSignal s = new GPSSignal(ll.toString(),"GWS84");
			GWS84trip.addInstance(s);			
		}
		return GWS84trip;
	}	

}
