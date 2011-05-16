package dataStructures;

import java.sql.SQLException;
import java.util.*;

import utils.DatabaseUtil;

import interfaces.IAllVehicles;

public class AllVehicles implements IAllVehicles {
	
	private static Vector<Vehicle> vehicles; 
	
	public AllVehicles(){
		vehicles = new Vector<Vehicle>();
	}

	public Vehicle getVehicle(Integer index) {		
		return vehicles.get(index);
	}

	public void addVehicle(Vehicle vehicle) {
		vehicles.add(vehicle);	
	}
	
	public Vehicle generateVehicle(Zone from, Zone to, int id) {
		GPSSignal f = from.generateRandomGPS();
		GPSSignal t = to.generateRandomGPS();
		
		assert(!f.toString().isEmpty()): "'from' signal, shouldn't be empty";
		assert(!t.toString().isEmpty()): "'to' signal, shouldn't be empty";
		assert(!f.equals(t)) : "'from' and 'to' signals shouldn't be the same";
		
		DatabaseUtil database = new DatabaseUtil();

		ShortestPath shortestpath = null;
		try {
			shortestpath = database.getShortestPath(f, t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assert(shortestpath != null): "'shortestpath', shouldn't be NULL";
		assert(shortestpath.size() > 0): "'shortestpath', shouldn't not be empty";
		assert(shortestpath.getInstance(0) != null): "value shouldn't be null";
		
		return new Vehicle(shortestpath, id);
	}

	public void move(DatabaseUtil database, double timeleft, double time) {
		assert(vehicles.size() > 0);
		for(Vehicle v : vehicles){
			v.move(database, timeleft, time);
		}		

	}

	public int size() {
		return vehicles.size();
	}

	public Vector<Vehicle> getVehicles() {		
		return AllVehicles.vehicles;
	}
	
	public String toString(){
		String str = "\n";
		for(Vehicle v: AllVehicles.vehicles)
			str += v.toString()+"\n";
		return str;
	}
}
