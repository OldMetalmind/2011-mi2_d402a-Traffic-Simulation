package interfaces;

import java.util.Vector;

import dataStructures.GPSSignal;

public interface IVoyage extends ITrip {
	public void addInstance(GPSSignal s, double time);
	public Vector<Double> getTimes();
	public Double getTimeAt(Integer i);
}
