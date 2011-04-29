package interfaces;

import java.util.ArrayList;

import dataStructures.GPSSignal;


public interface ITrip {
	
	//Get all the GPS signals of the path
	public ArrayList<GPSSignal> getPath();	
	public void addInstance(GPSSignal s);
	public void setInstance(Integer i, GPSSignal s);
	public GPSSignal getInstance(Integer i);
	public Integer size();
	public String toString();
}
