package dataStructures;

import java.util.Random;

import interfaces.IZone;

public class Zone implements IZone {
	
	//Make sure center is GWS84....
	final private GPSSignal center;
	final private Double radius;
	
	public Zone(GPSSignal center, Double radius){
		this.center = center;
		this.radius = radius;
	}
	
	public Zone(String zonex) {
		zonex = zonex.replaceAll("[^\\w,:;#?\\s]", "");
		String coord[] = zonex.split(";");
		this.center = new GPSSignal(coord[0].replace(',', ' '),"GWS84");
		this.radius = Double.parseDouble(coord[1]);
	}

	//TODO: it's not radius
	public boolean hasInside(GPSSignal gps) {
		return Math.sqrt( Math.pow(center.getLatitude() - gps.getLatitude(), 2 )
				+ Math.pow(center.getLongitude() - gps.getLongitude(), 2) )
				< radius;
	}
	
	//TODO: is GWS84 in km or meters? I mean, in which scale is GWS84?
	//http://en.wikipedia.org/wiki/World_Geodetic_System
	public GPSSignal generateRandomGPS(){
		/*
		Random rand = new Random();
		double degree = rand.nextDouble()*360; // TODO: Confirm this is between 0 and 360;
		double length = rand.nextDouble()*this.radius; // TODO: Confirm this is between 0 and radius
		assert(this.center.getFormat() == "GWS84");
		double x = length*Math.cos(degree)+this.center.getLongitude();
		double y = length*Math.sin(degree)+this.center.getLatitude();
		return new GPSSignal(y,x);
		*/
		
		return new GPSSignal(center.getLatitude(), center.getLongitude(), center.getFormat());
	}
	
	public String toString(){
		return "("+this.center.toString()+") "+this.radius;
	}
	
	private double retPX(){
		return -Math.acos( first() - second() ) +
				this.center.getLongitude() + 
				Math.PI;
	}
	
	//TODO: ...
	private double first(){
		return ( 
					(Math.cos(this.center.getLatitude()) * Math.cos(1)) 
				);
	}
	//TODO: ...
	private double second(){
		return 1;
	}
	
}
