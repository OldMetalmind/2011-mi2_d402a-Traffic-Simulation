package project;

import java.util.HashMap;

import interfaces.IAllVehicles;

public class AllVehicles implements IAllVehicles {
	private HashMap<String, Vehicle> vehicles; 
	
	public AllVehicles(){
		this.vehicles = new HashMap<String, Vehicle>();
	}

	@Override
	public Vehicle getVehicle(String id) {		
		return this.vehicles.get(id);
	}

	@Override
	public void addVehicle(Vehicle vehicle) {
		this.vehicles.put(vehicle.getID(), vehicle);	
	}

	
}
