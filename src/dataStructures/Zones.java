package dataStructures;

import java.util.Vector;
import java.util.Random;

import interfaces.IZones;

public class Zones implements IZones {

	private Vector<Zone> zones;
	
	public Zones(){
		this.setZones(new Vector<Zone>());
	}
	
	public Zone getZone(int index) {
		return this.zones.get(index);
	}

	public void addZone(Zone zone) {
		this.zones.add(zone);
	}
	
	public void setZones(Vector<Zone> zones) {
		this.zones = zones;
	}

	public Vector<Zone> getZones() {
		return this.zones;
	}
	
	public Zone selectRandomZone(){
		Random rand = new Random();
		int i = rand.nextInt(getZones().size());
		getZones().get(i).decreaseNumVehicles();
		if(getZones().get(i).getMaxVehicles() == 0)
			getZones().remove(i);
		return getZones().get(i);
	}

	public String toString(){
		String ret = "";
		for(Zone z : getZones())
			ret += z.toString() + " ";
		assert(ret.length() > 0); 
		return ret;		
	}

}
