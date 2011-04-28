package interfaces;

import traffic.GPSSignal;

public interface IVehicle {
	
	//final Integer personalMaxSpeed;
	//Trip trip;
	//Trip shortestPath;
	
	public void nextStep();		
	public GPSSignal from();
	public GPSSignal to();
	
}
