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
		
		GPSSignal from = this.getActualPosition();
		GPSSignal checkpoint = this.getShortestPath().getInstance(positionIndex+1);	
		
		double distance = Utils.UTMdistance(from,checkpoint); //it's in meters;
		
		double maxDistance = timeLeft * this.shortestPath.getSpeedLimitAt(this.positionIndex);
		
		if(distance < maxDistance){
			move(distance, checkpoint);
		}
		else {
			
		}
		
		
		return null;
	}


	//TODO: Make sure vehicles don't overlap.
	/**
	 * Moves the vehicle x meters in direction d
	 * @param m - how much meters it will move
	 * @param d - the direction of the movement
	 */
	private void move(double m, GPSSignal d) {
		// TODO CHECK: moves the vehicle x meters in direction d;
		GPSSignal position = this.getActualPosition();
		
		double angletemp = Math.atan((position.getLatitude() - d.getLatitude() / 
									position.getLongitude() - d.getLongitude()));
		
		double angle = Math.atan2(position.getLatitude() - d.getLatitude(), 
									position.getLongitude() - d.getLongitude());
		double x = Math.cos(angle) * m;
		double y = Math.sin(angle) * m;
		
		setActualPosition(position.getLatitude() + y, position.getLongitude() + x );
		// cos(x) = adjacent / hipotenuse*
		// sin(x) = oposite / hipotenuse* 
	}

	private void setActualPosition(double latitude, double longitude) {
		this.trip.addInstance(new GPSSignal(latitude, longitude, "UTM"));
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
	public String getActualPositionUTM() {
		System.out.println("Vehicle| shortestPath:\n"+this.shortestPath);
		String ret = "";
		GPSSignal last = null;
		if(this.trip != null)
			last = this.trip.getInstance(this.trip.size());
		else
			last = this.shortestPath.getInstance(0);
			
		if(last.getFormat() == "UTM")
			ret = "'POINT("+ last.getLongitude() +" "+ last.getLatitude() +")'::geometry";
		else {
			GPSSignal n = Utils.LatLon2UTM(last);
			ret = "'POINT("+ n.getLongitude() +" "+ n.getLatitude() +")'::geometry";
		}
		return ret;
	}
	
	public GPSSignal getActualPosition(){
		return this.trip.getInstance( this.trip.size() );
	}
	
	public String toString() {
		return personalMaxSpeed+" "
				+shortestPath.toString()+" "
				//+trip.toString()+" "
				+gpsFormat;
	}
}
