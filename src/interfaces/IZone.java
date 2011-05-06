package interfaces;

import dataStructures.GPSSignal;

public interface IZone {
	
	public GPSSignal generateRandomGPS();
	public String toString();
	public int getMaxVehicles();
	public void decreaseNumVehicles();
}
