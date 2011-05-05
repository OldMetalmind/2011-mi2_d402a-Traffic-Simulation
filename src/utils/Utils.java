package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;
import uk.me.jstott.jcoord.UTMRef;
import dataStructures.GPSSignal;
import dataStructures.Road;
import dataStructures.Trip;

/*
 * Methods that help other classes;
 */
public class Utils {
	
	
	
	/*
	public static void getPoints(Connection conn, String roadID){
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
	 */
	/*
	public static Trip road2points(Road road) {
		Trip UTM_Trip = new Trip("UTM");
		String[] res = road.getGeomText().split("[,]|[((]|[))]");
		for(int i = 2; i < res.length; i++){
			GPSSignal g = new GPSSignal(res[i],"UTM");
			UTM_Trip.addInstance(g);
		}
		return UTM_Trip;
	}
	 */
	
	public static Trip UTM2LatLon(Trip trip) {
		Trip lltrip = new Trip("LatLon"); 
		for(int i = 0; i < trip.size(); i++){
			lltrip.addInstance( UTM2LatLon( trip.getInstance(i) ) );
		}			
		return lltrip;
	}

	public static GPSSignal UTM2LatLon(GPSSignal signal) {		
		if(signal.getFormat() == "LatLon")
			return signal;		
	    UTMRef utm = new UTMRef(signal.getLatitude(), signal.getLongitude(),'N', 32);
	    LatLng ll = utm.toLatLng();
	    ll.toWGS84();
	    return new GPSSignal(ll.getLat(),ll.getLng(),"LatLon");
	}

	public static GPSSignal LatLon2UTM(GPSSignal signal) {		
		if(signal.getFormat() == "UTM")
			return signal;		
		LatLng ll = new LatLng(signal.getLatitude(),signal.getLongitude());
		UTMRef utm = ll.toUTMRef();		
		return new GPSSignal(utm.getEasting(),utm.getNorthing(),"UTM");	
	    
		
	}

}
