package interfaces;

import utils.DatabaseUtil;
import dataStructures.*;

public interface IVehicle {
		
	public void initMovement(DatabaseUtil database, double timeLeft, double times);
	public GPSSignal origin();
	public GPSSignal destination();
	public GPSSignal getActualPosition();
	public String getVoyageFormat();
	public String getShortestPathFormat();
	public String getActualPositionUTM();
	public String toString();
}
