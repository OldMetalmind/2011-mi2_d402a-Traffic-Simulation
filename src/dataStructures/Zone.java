package dataStructures;

import utils.Utils;
import interfaces.IZone;

public class Zone implements IZone {
	
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
	
	//TODO: Generate a random signal inside this zone. (The zone is in UTM format.)
	public GPSSignal generateRandomGPS(){		
		return new GPSSignal(center.getLatitude(), center.getLongitude(), center.getFormat());
	}
	
	public String toString(){
		return "("+this.center.toString()+") "+this.radius+" -format: "+ this.center.getFormat();
	}	
}
