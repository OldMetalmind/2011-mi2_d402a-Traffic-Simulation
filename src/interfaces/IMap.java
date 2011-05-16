package interfaces;

import dataStructures.GPSSignal;

public interface IMap {
	
	public Integer add(GPSSignal s, int id);
	public Integer remove(GPSSignal s);
	public Integer getId(GPSSignal s);
	public boolean check(GPSSignal s);
	public GPSSignal closestFreePosition(GPSSignal s);
	public void reset();

}
