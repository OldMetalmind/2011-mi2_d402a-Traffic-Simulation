package dataStructures;

import java.util.Random;

import utils.Utils;
import interfaces.IZone;

public class Zone implements IZone {
	
	final private GPSSignal center;
	final private double radius;
	private double numberVehicles = -1; // -1 = unlimited
	private String name;
	
	public Zone(GPSSignal center, Double radius){
		this.center = Utils.LatLon2UTM(center);
		this.radius = radius;
	}
	
	public Zone(GPSSignal center, double radius, double maxVehicles){
		this.center = Utils.LatLon2UTM(center);
		this.radius = radius;
		this.numberVehicles = maxVehicles;
	}
	
	public Zone(String zonex, String format) {
		String coord[] = zonex.split(" ");
		this.center = Utils.LatLon2UTM(new GPSSignal(coord[0]+" "+coord[1], format));
		this.radius = Double.parseDouble(coord[2]);
		this.numberVehicles = Double.parseDouble(coord[3]);
	}
	
	public Zone(GPSSignal center, Double radius, int maxVehicles){
		this.center = Utils.LatLon2UTM(center);
		this.radius = radius;
		this.numberVehicles = maxVehicles;
	}
	
	public Zone(String zonex, String format, int maxVehicles) {
		String coord[] = zonex.split(" ");
		this.center = Utils.LatLon2UTM(new GPSSignal(coord[0]+" "+coord[1], format));
		this.radius = Double.parseDouble(coord[2]);
		this.numberVehicles = maxVehicles;
	}
	
	/**
	 * @return a random GPS signal in UTM format, that is located inside the zone
	 */
	public GPSSignal generateRandomGPS(){
		assert(this.center.getFormat() == "UTM"): "center format is - " + this.center.getFormat() + ", it should be UTM";
		
		
		Random rand = new Random();
		double distance = rand.nextDouble()*this.radius;
		double degree = rand.nextDouble()*360;
		double radian = Math.toRadians(degree);
		
		double x = Math.cos(radian)*distance + this.center.getLongitude();
		double y = Math.sin(radian)*distance + this.center.getLatitude();

		return new GPSSignal(y, x, "UTM");
	}

	public double getMaxVehicles() {
		return numberVehicles;
	}

	public void decreaseNumVehicles() {
		if(this.numberVehicles != -1)
			this.numberVehicles--;		
	}

	public String getName() {
		return this.name;
	}
	
	public String toString(){
		return "["+this.center.toString() +" "+ this.center.getFormat() +" "+ this.radius +"]";
	}	
}
