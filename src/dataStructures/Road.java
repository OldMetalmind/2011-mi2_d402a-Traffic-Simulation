package dataStructures;

import interfaces.IRoad;

public class Road implements IRoad {

	final private String id;
	final private Integer speedLimit;
	final private String geomText;
	
	public Road(String id, Integer maxSpeed, String geomText){
		this.id = id;
		this.speedLimit = maxSpeed;
		this.geomText = geomText;
	}

	public String getId() {
		return id;
	}

	public Integer getSpeedLimit() {
		return speedLimit;
	}

	public String getGeomText() {
		return geomText;
	}

}
