package dataStructures;

import java.sql.SQLException;
import java.util.*;

import utils.DatabaseUtil;

import interfaces.IAllVehicles;

public class AllVehicles implements IAllVehicles {
	
	private Vector<Vehicle> vehicles; 
	
	public AllVehicles(){
		this.vehicles = new Vector<Vehicle>();
	}

	public Vehicle getVehicle(Integer index) {		
		return this.vehicles.get(index);
	}

	public void addVehicle(Vehicle vehicle) {
		this.vehicles.add(vehicle);	
	}
	
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

	public void move(DatabaseUtil database, double timeleft) {
		for(Vehicle v : this.vehicles){
			v.move(database, timeleft);
		}		
	}

	public int size() {
		return this.vehicles.size();
	}

	
}
