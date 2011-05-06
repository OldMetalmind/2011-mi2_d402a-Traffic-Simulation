package interfaces;

import dataStructures.Zones;

public interface IUserInput {
	
	public int getTotalVehicles();
	public double getFrequency();
	public Zones getFromZones();
	public Zones getToZones();
	public double getDuration();
}
