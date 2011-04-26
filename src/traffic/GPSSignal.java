package traffic;

public class GPSSignal {

	private Double latitude;
	private Double longitude;
	private String format;	
	
	public GPSSignal(Double latitude, Double longitude){
		this.format = "UTM";
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/*
	 * String format equal: "Coordinate Coordinate"
	 */
	public GPSSignal(String str){
		String coordinate[] = str.split(" ");
		this.format = "UTM";
		this.latitude = Double.parseDouble(clean(coordinate[0]));
		this.longitude = Double.parseDouble(clean(coordinate[1]));		
	}
	
	public GPSSignal(String str, String format){
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
	//Latitude first then longitude ex.: 123 144
	public String toString() {
		return this.latitude+" "+this.longitude;
	}

	private String clean(String str) {
		return str.replaceAll(",|[(]|[)]", "");
	}
	
	
}
