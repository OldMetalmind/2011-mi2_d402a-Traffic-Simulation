package interfaces;

import dataStructures.Zone;

public interface IZones {

	public Zone getZone(int index);
	public void addZone(Zone zone);
	public String getName();
	public String toString();
	
}
