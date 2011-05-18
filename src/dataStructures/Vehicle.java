package dataStructures;

import utils.DatabaseUtil;
import utils.Utils;
import interfaces.IVehicle;

public class Vehicle implements IVehicle {

	private int vehicle_id;
	private ShortestPath shortestPath; // This is the path that the vehicle must
	// make.
	private Voyage voyage; // This is where all the positioning of the vehicle
	// will be saved.
	private int index; // this index is related to the 'shortest path' trip.
	private GPSSignal checkpoint;
	public boolean stopped;

	public Vehicle(ShortestPath shortestPath, int id) {
		this.index = 1;
		this.shortestPath = shortestPath;
		this.voyage = new Voyage(shortestPath.getInstance(0));
		this.setCheckpoint(shortestPath.getInstance(this.index));
		this.vehicle_id = id;
		this.stopped = false;
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
		return "'POINT(" + last.getLongitude() + " " + last.getLatitude()
				+ ")'::geometry";
	}

	public GPSSignal getActualPosition() {
		return this.voyage.getLast();
	}

	public String toString() {
		return "[" + this.vehicle_id + "|" + shortestPath.toString() + "|"
				+ voyage.toString() + "]";
	}

	public String getVoyageFormat() {
		return this.voyage.getFormat();
	}

	public String getShortestPathFormat() {
		return this.shortestPath.getFormat();
	}

	private GPSSignal move(DatabaseUtil database, double distance,
			double allowedDistance, GPSSignal position, int index) {
		this.setCheckpoint(this.getShortestPath().getInstance(index));
		this.index = index;
		float percentage = allowedDistance > distance ? (float) (distance / allowedDistance)
				: (float) (allowedDistance / distance);
		GPSSignal newPosition = database.lineInterpolatePoint(this.checkpoint,
				position, percentage);
		return newPosition;
	}

	private double timespent(double distance, double speedLimit) {
		assert (speedLimit > 0);
		return (1 / speedLimit) * distance;
	}

	private void setActualPosition(GPSSignal location, double time) {
		this.voyage.addInstance(location, time);
	}

	public void setCheckpoint(GPSSignal checkpoint) {
		this.checkpoint = checkpoint;
	}

	public GPSSignal getCheckpoint() {
		return this.checkpoint;
	}

	public void reachedDestination() {
		this.stopped = true;
	}

	public void initMovement(DatabaseUtil database, double timeLeft, double time) {
		int alloweddistance = 5;
		GPSSignal newPosition = null;
		GPSSignal position = this.getActualPosition(); // Actual position
		this.setCheckpoint(this.shortestPath.getInstance(this.index)); // new
		// checkpoint
		if (position.equals(this.checkpoint)) {
			this.reachedDestination();
			return;
		}
		if (timeLeft == 0) {
			newPosition = this.getCheckpoint();
			assert (newPosition != null);
			if (database.checkDistanceToOtherVehicles(newPosition) > alloweddistance) {
				this.setActualPosition(newPosition, time);
				database.updateVehicle(this, this.vehicle_id);
			} else
				// TODO something
				this.reachedDestination();
			return;
		}
		double speedLimit = this.shortestPath.getSpeedLimitAt(this.index); // speed
		// limit
		assert (speedLimit > 0);
		double allowedDistance = timeLeft * speedLimit; // allowed distance
		// regarding the time
		// left and the speed
		// limit on the actual
		// road;
		assert (allowedDistance >= 0);

		double distance = Utils.UTMdistance(position, this.checkpoint); // distance
		// to
		// checkpoint
		// in
		// meters.
		assert (distance >= 0);

		if (allowedDistance < distance) {
			newPosition = move(database, distance, allowedDistance, position,
					this.index);
			if (database.checkDistanceToOtherVehicles(newPosition) > alloweddistance) {
				this.setActualPosition(newPosition, time);
				database.updateVehicle(this, this.vehicle_id);
			} else
				// TODO something
			return;

		} else { // allowedDistance > distance
			double tmpAllowedDistance = allowedDistance - distance; // tmpAllowedDistance
			double tmpTimeLeft = timeLeft
					- this.timespent(distance, speedLimit); // TODO confirm
			// method
			// "time spent" is
			// working
			// correctly.
		

			position = this.getCheckpoint();
			int tmpIndex = this.index + 1;
			if(tmpIndex == this.shortestPath.size()){
				newPosition = position;
				this.reachedDestination();
				return;
			}
			this.setCheckpoint(this.shortestPath.getInstance(tmpIndex));
			assert (!position.equals(this.checkpoint));

			newPosition = moveRecursive(database, tmpAllowedDistance,
					tmpTimeLeft, position, tmpIndex, time);
		}

		assert (newPosition != null);
		// System.out.println(this.getActualPositionUTM());
		if (database.checkDistanceToOtherVehicles(newPosition) > alloweddistance) {
			this.setActualPosition(newPosition, time);
			database.updateVehicle(this, this.vehicle_id);
		} else
			// TODO something
		database.updateVehicle(this, this.vehicle_id);

	}

	private GPSSignal moveRecursive(DatabaseUtil database,
			double allowedDistance, double timeLeft, GPSSignal position,
			int index, double time) {
		if (timeLeft == 0 || allowedDistance == 0) {
			// TODO: 3check Overlap
			this.reachedDestination();
			return position;
		}
		assert (!position.equals(this.checkpoint));
		double distance = Utils.UTMdistance(position, this.checkpoint); // distance
		// to
		// checkpoint
		// in
		// meters.

		double speedLimit = this.shortestPath.getSpeedLimitAt(index); // speed
		// limit
		if (allowedDistance < distance) {
			return this.move(database, distance, allowedDistance, position,
					index);
		} else { // allowedDistance > distance
			double tmpAllowedDistance = allowedDistance - distance; // tmpAllowedDistance
			double tmpTimeLeft = timeLeft
					- this.timespent(distance, speedLimit);
			assert (tmpTimeLeft < timeLeft) : "distance = " + distance;
			// System.out.println(tmpTimeLeft);
			position = this.getCheckpoint();
			int tmpIndex = index + 1;
			database.updateVehicle(this, this.vehicle_id);

			if (tmpIndex == this.shortestPath.size()) {
				this.reachedDestination();
				return position;
			}

			this.setCheckpoint(this.shortestPath.getInstance(tmpIndex));
			return moveRecursive(database, tmpAllowedDistance, tmpTimeLeft,
					position, tmpIndex, time);
		}
	}

}
