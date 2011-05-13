package interfaces;

import dataStructures.GPSSignal;

public interface IZone {
	
	public GPSSignal generateRandomGPS();
	public String getName();
	public double getMaxVehicles();
	public void decreaseNumVehicles();
	public String toString();
}
