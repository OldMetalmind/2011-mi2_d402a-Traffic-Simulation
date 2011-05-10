package utils;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
import dataStructures.GPSSignal;
import dataStructures.Trip;

/*
 * Methods that help other classes;
 */
public class Utils {
	
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
	
	/**
	 * 
	 * @param kmh - kilometer per hour
	 * @return meter per second
	 */
	public static double kmh2ms(double kmh){
		return kmh / 3600;
	}
	
	/*
	public static void main(String[] argv) {
		double latitude = 55;
		double longitude = 9;
		
		GPSSignal s1 = new GPSSignal(latitude,longitude,"LatLon");
		GPSSignal s2 = LatLon2UTM(s1);
		GPSSignal s3 = UTM2LatLon(s2);
		System.out.println(s1+"\n"+s2+"\n"+s3);		
	}
	*/

}
