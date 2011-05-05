package dataStructures;

import utils.Utils;
import interfaces.IZone;

public class Zone implements IZone {
	
	//Make sure center is GWS84....
	final private GPSSignal center;
	final private Double radius;
	
	public Zone(GPSSignal center, Double radius){
		this.center = center;
		this.radius = radius;
	}
	
	public Zone(String zonex, String format) {
		String coord[] = zonex.split(" ");
		this.center = new GPSSignal(coord[0]+" "+coord[1], format);
		this.radius = Double.parseDouble(coord[2]);
	}

	/*
	public boolean hasInside(GPSSignal gps) {
		return Math.sqrt( Math.pow(center.getLatitude() - gps.getLatitude(), 2 )
				+ Math.pow(center.getLongitude() - gps.getLongitude(), 2) )
				< radius;
	}
	*/
	
	//TODO: A real random....
	public GPSSignal generateRandomGPS(){		
		return new GPSSignal(center.getLatitude(), center.getLongitude(), center.getFormat());
	}
	
	public String toString(){
		return "("+this.center.toString()+") "+this.radius+" -format: "+ this.center.getFormat();
	}	
}
