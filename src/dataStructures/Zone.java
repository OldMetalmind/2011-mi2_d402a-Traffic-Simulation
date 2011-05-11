package dataStructures;

import java.util.Random;

import utils.Utils;
import interfaces.IZone;

public class Zone implements IZone {
	
	final private GPSSignal center;
	final private Double radius;
	private double numberVehicles; // -1 = unlimited
	
	public Zone(GPSSignal center, Double radius){
		this.center = center;
		this.radius = radius;
		this.numberVehicles = -1;
	}
	
	public Zone(GPSSignal center, double radius, double vehicles){
		this.center = center;
		this.radius = radius;
		this.numberVehicles = vehicles;
	}
	
	public Zone(String zonex, String format) {
		String coord[] = zonex.split(" ");
		this.center = new GPSSignal(coord[0]+" "+coord[1], format);
		this.radius = Double.parseDouble(coord[2]);
		this.numberVehicles = Double.parseDouble(coord[3]);
	}
	
	public Zone(GPSSignal center, Double radius, int maxVehicles){
		this.center = center;
		this.radius = radius;
		this.numberVehicles = maxVehicles;
	}
	
	public Zone(String zonex, String format, int maxVehicles) {
		String coord[] = zonex.split(" ");
		this.center = new GPSSignal(coord[0]+" "+coord[1], format);
		this.radius = Double.parseDouble(coord[2]);
		this.numberVehicles = maxVehicles;
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

	public double getMaxVehicles() {
		return numberVehicles;
	}

	@Override
	public void decreaseNumVehicles() {
		if(this.numberVehicles != -1)
			this.numberVehicles--;		
	}	
}
