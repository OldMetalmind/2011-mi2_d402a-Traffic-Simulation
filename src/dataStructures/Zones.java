package dataStructures;

import java.util.Vector;
import java.util.Random;

import interfaces.IZones;

public class Zones implements IZones {

	final private String name;
	private static Vector<Zone> zones;
	
	public Zones(){
		this.name = "unkown";
		this.setZones(new Vector<Zone>());
	}
	
	public Zones(String name){
		this.setZones(new Vector<Zone>());
		this.name = name;		
	}
	
	public Zone getZone(int index) {
		return zones.get(index);
	}

	public void addZone(Zone zone) {
		zones.add(zone);
	}

	public String getName() {
		return name;
	}
	
	public void setZones(Vector<Zone> zones) {
		Zones.zones = zones;
	}

	public static Vector<Zone> getZones() {
		return zones;
	}
	
	public Zone selectRandomZone(){
		Random rand = new Random();
		int i = rand.nextInt(getZones().size());
		getZones().get(i).decreaseNumVehicles();
		if(getZones().get(i).getMaxVehicles() == 0)
			getZones().remove(i);
		return getZones().get(i);
	}

}
