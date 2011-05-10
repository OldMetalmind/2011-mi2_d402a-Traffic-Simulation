package dataStructures;

import utils.DatabaseUtil;
import utils.Utils;
import interfaces.IVehicle;

public class Vehicle implements IVehicle {
	
	final private double personalMaxSpeed; // m/s
	final private Trip shortestPath; //This is the path that the vehicle must make.
	private Trip trip; //This is where all the positioning of the vehicle will be saved.
	private int positionIndex; //this index is related to the 'shortest path' trip.
	private String gpsFormat;
	private int vehicle_id;
		
	
	public Vehicle(Trip shortestPath){
		this.personalMaxSpeed = -1; //Unlimited
		this.shortestPath = shortestPath;
		this.gpsFormat = shortestPath.getFormat();
		this.positionIndex = 0;
	}	
	
	public Vehicle(Trip shortestPath, String gpsFormat){
		this.personalMaxSpeed = -1; //Unlimited
		this.shortestPath = shortestPath;
		this.gpsFormat = gpsFormat;
		this.positionIndex = 0;
	}	

	public double getPersonalMaxSpeed() {
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

	public void setGpsFormat(String gpsFormat) {
		this.gpsFormat = gpsFormat;
	}

	public String getGpsFormat() {
		return gpsFormat;
	}

	public GPSSignal from() {
		return this.trip.getInstance(0);
	}
	
	public GPSSignal to() {
		return this.trip.getInstance(trip.size());
	}

	//TODO: Move the vehicle one more frequency and get his coordinates.
	public GPSSignal move(DatabaseUtil database, double timeLeft) {
		
		String sqlCheckpoint = "SELECT ST_Distance(";
		//after this query we will be able to find the distance till next checkpoint;
		double distanceTillCheckpoint = 100;
		double allowedActualMaxSpeed = shortestPath.getSpeedLimitAt(this.positionIndex);
		
		double vehiclePosition = 1;
		double nextCHECKPOINT = 2;
		
		String sql = "SELECT ST_Distance(+"+vehiclePosition+","+nextCHECKPOINT+")";
		
		double distanceMade = Utils.kmh2ms(allowedActualMaxSpeed) * timeLeft;
		
		return null;
	}

	public void setVehicle_id(int vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public int getVehicle_id() {
		return vehicle_id;
	}

	/**
	 * 
	 * @return a string with the actual position in UTM format
	 */
	public String getActualPosition() { 
		String ret = "";
		GPSSignal last = this.trip.getInstance(this.trip.size());
		if(last.getFormat() == "UTM")
			ret = "'POINT("+ last.getLongitude() +" "+ last.getLatitude() +")'::geometry";
		else {
			GPSSignal n = Utils.LatLon2UTM(last);
			ret = "'POINT("+ n.getLongitude() +" "+ n.getLatitude() +")'::geometry";
		}		
		return ret;
	}
	
	public String toString() {
		return personalMaxSpeed+" "
				+shortestPath.toString()+" "
				//+trip.toString()+" "
				+gpsFormat;
	}
}
