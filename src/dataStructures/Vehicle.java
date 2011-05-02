package dataStructures;

import interfaces.IVehicle;

public class Vehicle implements IVehicle {
	
	final private Integer personalMaxSpeed;
	final private Trip shortestPath;
	private Trip trip;
	private String gpsFormat;
	
	public Vehicle(Trip shortestPath){
		this.personalMaxSpeed = -1; //Unlimited
		this.shortestPath = shortestPath;
		this.gpsFormat = "UTM";
	}	
	
	public Vehicle(Trip shortestPath, String gpsFormat){
		this.personalMaxSpeed = -1; //Unlimited
		this.shortestPath = shortestPath;
		this.gpsFormat = gpsFormat;
	}	

	public Integer getPersonalMaxSpeed() {
		return personalMaxSpeed;
	}

	public Trip getShortestPath() {
		return shortestPath;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public Trip getTrip() {
		return trip;
	}

	@Override
	public void nextStep() {
		// TODO (not yet) This should be done after the first demo. To confirm that the vehicles don't overlap
		
	}

	public void setGpsFormat(String gpsFormat) {
		this.gpsFormat = gpsFormat;
	}

	public String getGpsFormat() {
		return gpsFormat;
	}

	@Override
	public GPSSignal from() {
		return this.trip.getInstance(0);
	}

	@Override
	public GPSSignal to() {
		return this.trip.getInstance(trip.size());
	}	
	
	public String toString() {
		return personalMaxSpeed+" "+shortestPath.toString()+" "+trip.toString()+" "+gpsFormat;
	}

}
