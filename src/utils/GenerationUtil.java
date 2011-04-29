package utils;

import dataStructures.GPSSignal;
import dataStructures.Trip;
import dataStructures.Vehicle;
import dataStructures.Zone;
import interfaces.IGenerateRandomInfo;

public class GenerationUtil implements IGenerateRandomInfo {

	@Override 
	public Vehicle generateVehicle(Zone from, Zone to) {		
		GPSSignal f = from.generateRandomGPS();
		GPSSignal t = to.generateRandomGPS();		
		DatabaseUtil db = new DatabaseUtil();
		Trip trip = db.getShortestPath(f, t);		
		return new Vehicle(trip);
	}
}
