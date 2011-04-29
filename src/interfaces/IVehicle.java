package interfaces;

import traffic.GPSSignal;

public interface IVehicle {
		
	public void nextStep();		
	public GPSSignal from();
	public GPSSignal to();
	
}
