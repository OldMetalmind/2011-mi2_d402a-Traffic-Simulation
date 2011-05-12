package dataStructures;

import java.util.Vector;

import interfaces.IVoyage;

public class Voyage extends Trip implements IVoyage {

	private Vector<Double> time;
	
	public Voyage(GPSSignal signal) {
		super(signal);
		this.time = new Vector<Double>();
		this.time.add(0.0);
	}
	public Voyage(String str) {
		super(str);
		this.time = new Vector<Double>();
	}
	
	public Vector<GPSSignal> getPath() {
		return super.getPath();
	}

	public void setInstance(Integer i, GPSSignal s) {
		super.setInstance(i, s);
	}

	public GPSSignal getInstance(Integer i) {		
		return super.getInstance(i);
	}

	public Integer size() {
		return super.size();
	}
	
	public void addInstance(GPSSignal s, double time) {
		super.addInstance(s);
		this.time.add(time);
		assert(this.size() == super.size());
	}
	
	public Vector<Double> getTimes() {		
		return this.time;
	}

	public Double getTimeAt(Integer i) {
		return this.time.get(i);
	}
	
	public String getFormat(){
		return super.getFormat();
	}
	
	public Vector<GPSSignal> getVoyage() {		
		return super.getPath();
	}
	
	public GPSSignal getLast(){
		return super.getLast();
	}
	
	public boolean isEmpty(){
		return super.isEmpty();
	}

	public String toString(){
		return "Voyage: "+super.toString();
	}
}
