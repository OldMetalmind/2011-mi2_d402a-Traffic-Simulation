package dataStructures;

import interfaces.IUserInput;

public class UserInput implements IUserInput {

	final private int totalVehicles;
	final private double frequency;
	final private double duration;
	final private Zones fromZones;
	final private Zones toZones;
	
	public UserInput(int TotalVehicles, double Frequency, double Duration, Zones FromZones, Zones ToZones){
		totalVehicles = TotalVehicles;
		frequency = Frequency;
		duration = Duration;
		fromZones = FromZones;
		toZones = ToZones;		
	}
	
	public int getTotalVehicles() {
		return totalVehicles;
	}

	public double getFrequency() {
		return frequency;
	}

	public Zones getFromZones() {
		return fromZones;
	}

	public Zones getToZones() {
		return toZones;
	}

	public double getDuration() {		
		return duration;
	}
}
