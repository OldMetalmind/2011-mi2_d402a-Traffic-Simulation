package dataStructures;

import java.util.HashMap;

import interfaces.IMap;

public class Map implements IMap {

	private HashMap<GPSSignal, Integer> map;
	
	public Map(){
		this.map = new HashMap<GPSSignal, Integer>();
	}
	
	public Integer getId(GPSSignal s) {
		return this.map.get(s);
	}

	public boolean check(GPSSignal s) {
		return this.map.containsKey(s);
	}

	public GPSSignal closestFreePosition(GPSSignal s) {
		if(this.check(s))
			return null;
		else
			return null;
	}

	public Integer add(GPSSignal s, int id) {
		return this.map.put(s, id);
	}

	public Integer remove(GPSSignal s) {			
		return this.map.remove(s);
	}

	public void reset() {
		this.map.clear();		
	}

}
