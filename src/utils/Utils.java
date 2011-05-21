package utils;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
import dataStructures.GPSSignal;
import dataStructures.Trip;
import dataStructures.Zone;

/*
 * Methods that help other classes;
 */
public class Utils {
	
	final static int UTMCode = 32; //32
	final static char UTMZone = 'N'; // N
	final static double kmh2ms = 0.27777778;
	
	public static Trip UTM2LatLon(Trip trip) {
		Trip lltrip = new Trip("LonLat"); 
		for(int i = 0; i < trip.size(); i++){
			lltrip.addInstance( UTM2LatLon( trip.getInstance(i) ) );
		}			
		return lltrip;
	}
	
	public static Trip UTM2LonLat(Trip trip) {
		Trip lltrip = new Trip("LonLat"); 
		for(int i = 0; i < trip.size(); i++){
			lltrip.addInstance( UTM2LonLat( trip.getInstance(i) ) );
		}			
		return lltrip;
	}

	public static GPSSignal UTM2LonLat(GPSSignal signal) {		
		if(signal.getFormat() == "LonLat")
			return signal;		
	    UTMRef utm = new UTMRef(signal.getLatitude(), signal.getLongitude(),UTMZone, UTMCode);
	    LatLng ll = utm.toLatLng();
	    ll.toWGS84();
	    return new GPSSignal(ll.getLng(),ll.getLat(),"LonLat");
	}

	public static GPSSignal UTM2LatLon(GPSSignal signal) {		
		if(signal.getFormat() == "LatLon")
			return signal;		
	    UTMRef utm = new UTMRef(signal.getLatitude(), signal.getLongitude(),UTMZone, UTMCode);
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
	
	/**
	 * Calculate the distance between these two UTM signals.
	 * @param from
	 * @param to
	 * @return Distance between the two point in meters
	 */
	public static double UTMdistance(GPSSignal from, GPSSignal to) {		
		assert(from.getFormat() == "UTM" && to.getFormat() == "UTM");		
		return Math.sqrt(Math.pow(from.getLatitude() - to.getLatitude(), 2) 
				+ Math.pow(from.getLongitude() - to.getLongitude(), 2));
	}


	/**
	 * 
	 * @param kmh - kilometer per hour
	 * @return meter per second
	 */
	public static double convertKmh2ms(Integer i) {
		return i.doubleValue()*kmh2ms;
	}

	public static String linearRingCoordinates(Zone z) {
		// TODO Return a string with the coordinates of the circunference separated by a comma
		return null;
	}
}
