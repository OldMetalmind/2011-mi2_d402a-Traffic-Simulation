package dataStructures;

import interfaces.ITrip;

import java.util.Vector;


public class Trip implements ITrip {

	private Vector<GPSSignal> trip;
	private Vector<Integer> speedLimit;
	private String format;
	
	public Trip(String format){
		this.trip = new Vector<GPSSignal>();
		this.speedLimit = new Vector<Integer>();
		this.setFormat(format);
	}
	
	public Trip(Vector<GPSSignal> trip){
		this.trip = trip;
		this.speedLimit = null;
		if(!trip.isEmpty()) 
			this.setFormat(trip.get(0).getFormat()); 
		else {
			System.out.println("ERROR no format.....");
			this.setFormat(null) ; 
		}
	}
	
	public Vector<GPSSignal> getPath() {		
		return this.trip;
	}
	public Integer size(){
		return trip.size();
	}

	public void addInstance(GPSSignal s) {
		if(!this.trip.contains(s))
			this.trip.add(s);
	}

	public void setInstance(Integer i, GPSSignal s) {
		this.trip.set(i, s);
	}
	
	public GPSSignal getInstance(Integer i) {		
		return trip.get(i);
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public Vector<Integer> getSpeedLimits() {
		return this.speedLimit;
	}

	public Integer getSpeedLimitAt(Integer i) {
		return this.speedLimit.get(i);
	}
	
	public String toString(){
		String output = "";
		for(int i = 0; i < this.trip.size(); i++)
			output += this.trip.get(i).toString()+";";
		
		return output;
	}

	public void addInstance(GPSSignal s, Integer speedLimit) {
		this.trip.add(s);
		this.speedLimit.add(speedLimit);
		if(this.speedLimit.size() != this.trip.size())
			System.out.println("There's a problem with size");		
	}

	public boolean isEmpty() {
		return this.trip.isEmpty();
	}
}
