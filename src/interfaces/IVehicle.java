package interfaces;

import dataStructures.GPSSignal;

public interface IVehicle {
		
	public void nextStep();		
	public GPSSignal from();
	public GPSSignal to();
	public String toString();
}
