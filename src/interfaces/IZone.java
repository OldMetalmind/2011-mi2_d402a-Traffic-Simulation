package interfaces;

import dataStructures.GPSSignal;

public interface IZone {
	
	public boolean hasInside(GPSSignal gps);
	public GPSSignal generateRandomGPS();

}
