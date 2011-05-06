package dataStructures;

import java.util.Random;

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
	
	/**
	 * @return a random GPS signal in UTM format, that is located inside the zone
	 */
	public GPSSignal generateRandomGPS(){	
		Random rand = new Random();
		double distance = rand.nextDouble()*this.radius;
		double degree = rand.nextDouble()*360;
		double radian = Math.toRadians(degree);
		
		double x = Math.cos(radian)*distance;
		double y = Math.sin(radian)*distance;
		
		return new GPSSignal(this.center.getLatitude() + y, this.center.getLongitude() + x, "UTM");
		//return new GPSSignal(center.getLatitude(), center.getLongitude(), center.getFormat());
	}
	
	public String toString(){
		return "("+this.center.toString()+") "+this.radius+" -format: "+ this.center.getFormat();
	}	
}
