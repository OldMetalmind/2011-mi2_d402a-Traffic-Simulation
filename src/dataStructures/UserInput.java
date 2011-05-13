package dataStructures;

import interfaces.IUserInput;

public class UserInput implements IUserInput {

	final private int totalVehicles;
	final private double frequency;
	final private double duration;
	final private Zones fromZones;
	final private Zones toZones;
	final double defaultDuration = 1; //seconds
	
	public UserInput(int TotalVehicles, double Frequency, double Duration, Zones FromZones, Zones ToZones){
		this.totalVehicles = TotalVehicles;
		this.frequency = Frequency;
		this.duration = Duration;
		this.fromZones = FromZones;
		this.toZones = ToZones;		
	}
	
	public UserInput(int TotalVehicles, double Frequency, Zones FromZones, Zones ToZones){
		this.totalVehicles = TotalVehicles;
		this.frequency = Frequency;
		this.duration = this.defaultDuration;
		this.fromZones = FromZones;
		this.toZones = ToZones;		
	}

	public int getTotalVehicles() {
		return this.totalVehicles;
	}

	public double getFrequency() {
		return this.frequency;
	}

	public Zones getFromZones() {
		return this.fromZones;
	}

	public Zones getToZones() {
		return this.toZones;
	}

	public double getDuration() {		
		return this.duration;
	}
}
