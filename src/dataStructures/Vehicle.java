package dataStructures;

import utils.DatabaseUtil;
import utils.Utils;
import interfaces.IVehicle;

public class Vehicle implements IVehicle {

	private int vehicle_id;
	private ShortestPath shortestPath; //This is the path that the vehicle must make.
	private Voyage voyage; //This is where all the positioning of the vehicle will be saved.
	private int index; //this index is related to the 'shortest path' trip.
	private GPSSignal checkpoint;
		
	
	public Vehicle(ShortestPath shortestPath){
		this.index = 1;
		this.shortestPath = shortestPath;
		this.voyage = new Voyage(shortestPath.getInstance(0));
		this.setCheckpoint(shortestPath.getInstance(this.index));
	}	

	public ShortestPath getShortestPath() {
		return shortestPath;
	}

	public Voyage getVoyage() {
		return voyage;
	}

	public GPSSignal origin() {
		return this.shortestPath.getInstance(0);
	}
	
	public GPSSignal destination() {
		return this.shortestPath.getInstance(shortestPath.size() - 1);
	}
	
	public void setVehicle_id(int vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public int getVehicle_id() {
		return this.vehicle_id;
	}
	
	public String getActualPositionUTM() {
		GPSSignal last = this.voyage.getLast();
		Utils.LatLon2UTM(last);
		return "'POINT("+ last.getLongitude() +" "+ last.getLatitude() +")'::geometry";
	}	

	public GPSSignal getActualPosition(){
		return this.voyage.getLast();
	}
	
	public String toString() {
		return "["+this.vehicle_id+"|"+shortestPath.toString()+"|"+voyage.toString()+"]";
	}

	public String getVoyageFormat() {
		return this.voyage.getFormat();
	}

	public String getShortestPathFormat() {
		return this.shortestPath.getFormat();
	}
	public void move(DatabaseUtil database, double timeLeft, double time) {		
		GPSSignal position = this.getActualPosition();
		
		//GPSSignal checkpoint = this.getShortestPath().getInstance( this.index );
		//GPSSignal checkpoint = this.getShortestPath().getInstance( this.getVoyage().size() );

		//int speedLimit = this.getShortestPath().getSpeedLimitAt( this.getVoyage().size() );
		int speedLimit = this.shortestPath.getSpeedLimitAt( this.index );
		assert(speedLimit > 20);

		double allowedDistance = timeLeft * speedLimit;
		assert(allowedDistance >= 0);
		
		double distance = Utils.UTMdistance(position, this.checkpoint); //it's in meters;
		assert(distance > 0);

		GPSSignal newPosition = null;
		if(allowedDistance < distance || allowedDistance == 0){
			newPosition = move(database, distance, allowedDistance, position, this.checkpoint, this.index);
		}
		else { //distance < allowedDistance			
			double tmpAllowedDistance = allowedDistance - distance;
			double tmpTimeLeft = timeLeft - this.timespent(distance, speedLimit);
			assert(tmpTimeLeft < timeLeft);
			newPosition = moveRecursive(database, tmpAllowedDistance, tmpTimeLeft, this.index + 1, this.checkpoint);
		}		
		
		assert(newPosition != null): "new position is null";
		assert(newPosition.getFormat() == "UTM"): "new position is not in UTM format";
				
		this.setActualPosition(newPosition, time);
		database.setVehicle(this.vehicle_id, newPosition);
	}
	
	private GPSSignal moveRecursive(DatabaseUtil database, double distance, double timeLeft, int index, GPSSignal position) {
		int speedLimit = this.shortestPath.getSpeedLimitAt( index );
		double allowedDistance = timeLeft * speedLimit;
		this.checkpoint = this.getShortestPath().getInstance(index);
		if(allowedDistance < distance || allowedDistance == 0){
			return move(database, distance, allowedDistance, position, this.checkpoint, index);
		}
		else {
			double tmpAllowedDistance = allowedDistance - distance;
			double tmpTimeLeft = timeLeft - this.timespent(distance, speedLimit);			
			assert(tmpTimeLeft < timeLeft);
			return moveRecursive(database, tmpAllowedDistance, tmpTimeLeft, index + 1, this.checkpoint);
		}		
	}
	
	private GPSSignal move(DatabaseUtil database, double distance, double allowedDistance, GPSSignal from, GPSSignal to, int index) {
		this.index = index;
		this.checkpoint = to;
		float percentage = (float) (allowedDistance/distance);
		GPSSignal newPosition = database.lineInterpolatePoint(from, to, percentage);
		return newPosition;
	}
	
	private double timespent(double distance, Integer speedLimit) {
		assert(speedLimit > 0);
		return (1/speedLimit.doubleValue())*distance;
	}	

	private void setActualPosition(GPSSignal location, double time){
		this.voyage.addInstance(location, time);
	}

	public void setCheckpoint(GPSSignal checkpoint) {
		this.checkpoint = checkpoint;
	}

	public GPSSignal getCheckpoint() {
		return checkpoint;
	}
}
