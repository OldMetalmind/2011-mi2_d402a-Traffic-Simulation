package traffic;

import interfaces.IPath;

import java.util.ArrayList;

public class Path implements IPath {

	private ArrayList<GPSSignal> path;
	
	public Path(){
		this.path = new ArrayList<GPSSignal>();
	}
	
	public Path(ArrayList<GPSSignal> path){
		this.path = path;
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

	public String toString(){
		String output = "";
		for(int i = 0; i < this.path.size(); i++)
			output += this.path.get(i).toString()+";";
		
		return output;
	}
	
	public GPSSignal getInstance(Integer i) {		
		return path.get(i);
	}
}
