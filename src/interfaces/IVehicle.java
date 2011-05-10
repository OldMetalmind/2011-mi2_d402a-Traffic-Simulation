package interfaces;

import utils.DatabaseUtil;
import dataStructures.*;

public interface IVehicle {
		
	public GPSSignal move(DatabaseUtil database, double time);
	public GPSSignal from();
	public GPSSignal to();
	public GPSSignal getActualPosition();
	public String getActualPositionUTM();
	public String toString();
}
