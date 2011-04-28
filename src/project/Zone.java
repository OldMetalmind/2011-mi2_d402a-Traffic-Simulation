package project;

import traffic.GPSSignal;
import interfaces.IZone;

public class Zone implements IZone {
	
	final private GPSSignal center;
	final private Double radius;
	
	public Zone(GPSSignal center, Double radius){
		this.center = center;
		this.radius = radius;
	}

	public boolean hasInside(GPSSignal gps) {
		return Math.sqrt( Math.pow(center.getLatitude() - gps.getLatitude(), 2 )
				+ Math.pow(center.getLongitude() - gps.getLongitude(), 2) )
				< radius;
	}
}
