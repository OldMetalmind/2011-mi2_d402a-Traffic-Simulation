package utils;

import java.sql.SQLException;

import dataStructures.GPSSignal;
import dataStructures.Trip;
import dataStructures.Vehicle;
import dataStructures.Zone;
import interfaces.IGenerateRandomInfo;

public class GenerationUtil implements IGenerateRandomInfo {

	public Vehicle generateVehicle(Zone from, Zone to) {
		GPSSignal f = from.generateRandomGPS();
		GPSSignal t = to.generateRandomGPS();
		DatabaseUtil db = new DatabaseUtil();
		Trip trip = null;
		try {
			trip = db.getShortestPath(f, t);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return new Vehicle(trip);
	}
}
