package dataStructures;

import java.util.*;

import interfaces.IAllVehicles;

public class AllVehicles implements IAllVehicles {
	private ArrayList<Vehicle> vehicles; 
	
	public AllVehicles(){
		this.vehicles = new ArrayList<Vehicle>();
	}

	@Override
	public Vehicle getVehicle(Integer index) {		
		return this.vehicles.get(index);
	}

	@Override
	public void addVehicle(Vehicle vehicle) {
		this.vehicles.add(vehicle);	
	}

	
}
