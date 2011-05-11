package interfaces;

import java.util.Vector;

import dataStructures.GPSSignal;


public interface ITrip {
	
	//Get all the GPS signals of the path
	public Vector<GPSSignal> getPath();	
	public void addInstance(GPSSignal s);	
	public void addInstance(GPSSignal s, Integer speedLimit);
	public void setInstance(Integer i, GPSSignal s);
	public GPSSignal getInstance(Integer i);
	public Integer size();
	public Vector<Integer> getSpeedLimits();
	public Integer getSpeedLimitAt(Integer i);
	public String toString();
}
