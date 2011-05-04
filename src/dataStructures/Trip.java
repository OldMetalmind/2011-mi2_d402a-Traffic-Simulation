package dataStructures;

import interfaces.ITrip;

import java.util.ArrayList;


public class Trip implements ITrip {

	private ArrayList<GPSSignal> path;
	private ArrayList<Integer> speedLimit;
	private String format;
	
	public Trip(String format){
		this.path = new ArrayList<GPSSignal>();
		this.setFormat(format);
	}
	
	public Trip(ArrayList<GPSSignal> path){
		this.path = path;
		if(!path.isEmpty()) 
			this.setFormat(path.get(0).getFormat()); 
		else {
			System.out.println("ERROR no format.....");
			this.setFormat(null) ; 
		}
	}
	
	public ArrayList<GPSSignal> getPath() {		
		return this.path;
	}
	public Integer size(){
		return path.size();
	}

	public void addInstance(GPSSignal s) {
		this.path.add(s);
	}

	public void setInstance(Integer i, GPSSignal s) {
		this.path.set(i, s);
	}
	
	public GPSSignal getInstance(Integer i) {		
		return path.get(i);
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public ArrayList<Integer> getSpeedLimits() {
		return this.speedLimit;
	}

	public Integer getSpeedLimitAt(Integer i) {
		return this.speedLimit.get(i);
	}
	
	public String toString(){
		String output = "";
		for(int i = 0; i < this.path.size(); i++)
			output += this.path.get(i).toString()+";";
		
		return output;
	}
}
