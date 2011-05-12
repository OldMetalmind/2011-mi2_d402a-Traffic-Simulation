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
		assert(!f.toString().isEmpty()): "'from' signal, shouldn't be empty";
		assert(!t.toString().isEmpty()): "'to' signal, shouldn't be empty";
		assert(!f.equals(t)) : "'from' and 'to' signals shouldn't be the same";
		DatabaseUtil db = new DatabaseUtil();
		Trip shortestpath = null;
		try {
			shortestpath = db.getShortestPath(f, t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert(!shortestpath.isEmpty()): "'shortestpath', shouldn't be empty";
		System.out.println("AllVehicles| shortestPath:"+ shortestpath);
		return new Vehicle(shortestpath);
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
