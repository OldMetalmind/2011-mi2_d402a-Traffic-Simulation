package interfaces;

import java.util.Vector;

import dataStructures.GPSSignal;

public interface IShortestPath extends ITrip {
	public void addInstance(GPSSignal s, Integer speedLimit);
	public Vector<Integer> getSpeedLimits();
	public Integer getSpeedLimitAt(Integer i);
}
