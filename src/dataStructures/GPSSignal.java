package dataStructures;

public class GPSSignal {

	private Double latitude;
	private Double longitude;
	private String format;	
	
	public GPSSignal(String str, String format){
		if(str.startsWith("POINT"))
			str = clean(str);
		String coordinate[] = str.split(" ");
		this.format = format;
		this.latitude = Double.parseDouble(clean(coordinate[0]));
		this.longitude = Double.parseDouble(clean(coordinate[1]));		
	}
	
	public GPSSignal(Double latitude, Double longitude, String format){
		this.format = format;
		this.latitude = latitude;
		this.longitude = longitude;		
	}
	
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public String getFormat() {
		return format;
	}

	public boolean equals(GPSSignal other){
		if(other.getLatitude().doubleValue() == this.getLatitude().doubleValue() && other.getLongitude().doubleValue() == this.getLongitude().doubleValue())
			return true;
		else
			return false;
	}
	
	//Latitude first then longitude ex.: 123 144
	public String toString() {
		return this.latitude+" "+this.longitude;
	}

	private String clean(String str) {		
		str = str.replaceAll("[POINT]", "");
		return str.replaceAll("[,]|[(]|[)]", "");
	}
	
	
}
