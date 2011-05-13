package dataStructures;

import interfaces.ITrip;

import java.util.Vector;


public class Trip implements ITrip {

	private Vector<GPSSignal> trip;
	private String format;	
	
	public Trip(GPSSignal signal){
		this.trip = new Vector<GPSSignal>();
		this.trip.add(signal);
		this.format = signal.getFormat();
	}
	
	public Trip(String format){
		this.trip = new Vector<GPSSignal>();
		this.format = format;
	}

	public Vector<GPSSignal> getPath() {		
		return this.trip;
	}
	public Integer size(){
		return trip.size();
	}
	
	public void addInstance(GPSSignal s) {
		this.trip.add(s);
	}

	public void setInstance(Integer i, GPSSignal s) {
		this.trip.set(i, s);
	}
	
	public GPSSignal getInstance(Integer i) {		
		return trip.get(i);
	}

	public String getFormat() {
		return format;
	}	

	public boolean isEmpty() {
		return this.trip.isEmpty();
	}
	
	public String toString(){
		String output = "";
		for(int i = 0; i < this.trip.size(); i++)
			output += this.trip.get(i).toString()+";";
		
		return output;
	}

	public GPSSignal getLast() {
		return this.trip.lastElement();
	}
}
