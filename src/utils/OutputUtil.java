package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;

import dataStructures.AllVehicles;
import dataStructures.GPSSignal;
import dataStructures.Trip;
import dataStructures.Vehicle;

/*
 * This class will only serve to output to a .KML file. 
 * The rest is open for future development.
 * 
 */
public class OutputUtil {

	private static String filename;
	
	public OutputUtil(String name){
		filename = name;
	}
		
	public void save2file(String output){
		writeFile(output,"txt");
	}
	
	/*
	 * This only saves 1 trip to a KML file
	 */
	public String savePathToKML(Trip path){
		
		String output = "<?xml version='1.0' encoding='UTF-8'?>\n";
		//output += "<import namespace='http://www.w3.org/2005/Atom' schemaLocation='http://code.google.com/apis/kml/schema/atom-author-link.xsd'/>";
		output += "<kml xmlns='http://www.opengis.net/kml/2.2'>\n";		
		output += "<Document>\n" +
					"<Placemark>\n" +
						"<name>GPS path</name>\n" +
							"<MultiGeometry>\n" + //
								"<LineString>\n" +
									"<coordinates>\n";
									for(int i = 0; i < path.size(); i++){
										GPSSignal g = path.getInstance(i);
										output += g.getLongitude()+","+g.getLatitude()+"\n";
									}		
		output += 					"</coordinates>\n" +
								"</LineString>\n" + //
							"</MultiGeometry>\n" +
					"</Placemark>\n" +
				"</Document>\n" +
				"</kml>";
		writeFile(output,"kml");	
		return output;
		
	}
	
	public String KMLTrip(Trip path){
		String output = "<LineString>\n" +
							"<coordinates>\n";
		for(int i = 0; i < path.size(); i++){
			GPSSignal g = path.getInstance(i);
			output += g.getLongitude()+","+g.getLatitude()+"\n";
		}
		output += "</coordinates>\n</LineString>\n";
		return output;
	}
	public String KMLHeader(){
		String output = "<?xml version='1.0' encoding='UTF-8'?>\n";
		//output += "<import namespace='http://www.w3.org/2005/Atom' schemaLocation='http://code.google.com/apis/kml/schema/atom-author-link.xsd'/>";
		output += "<kml xmlns='http://www.opengis.net/kml/2.2'>\n";		
		output += "<Document>\n";
		return output;
	}
	
	public String KMLFooter(){
		String output = "</Document>\n" +
						"</kml>";
		return output;
	}
		
	public void writeFile(String output, String ext){
		try{
	  		// Create file  
	  		FileWriter fstream = new FileWriter(filename+"."+ext);
	          BufferedWriter out = new BufferedWriter(fstream);
	          
	  		out.write( output );
	  		
	  		//Close the output stream
	  		out.close();
	  		}catch (Exception e){//Catch exception if any
	  			System.err.println("Error: " + e.getMessage());
	  		}
	}

	public void save2KML(AllVehicles vehicles) {
		String out = KMLHeader();
		for(Vehicle v : vehicles.getVehicles()){
			assert(v.getVoyage().getPath().size() == v.getVoyage().getTimes().size() );			
			for(int i = 0; i < v.getVoyage().size(); i++){
				GPSSignal s = v.getVoyage().getInstance(i);
				double time = v.getVoyage().getTimeAt(i);
				
				out += signal2KMLPlacemark(s, v.getVehicle_id(), time);
			}
		}
		out += KMLFooter();; 
		writeFile(out,"kml");
	}
	
	public static String signal2KMLPlacemark(GPSSignal s, int id, double time) {
		//GPSSignal ll = Utils.UTM2LonLat(s); //for Google Earth
		GPSSignal ll = s; //For Quantum Gis
		String out = "<Placemark>\n" +
				"\t<name>id="+id+"  t="+time+"</name>\n" +
				"\t<description>"+ll.getLatitude()+","+ll.getLongitude()+"</description>\n" +
				"\t<Point>\n" +
				"\t\t<coordinates>"+ll.getLatitude()+","+ll.getLongitude()+"</coordinates>\n"+
				"\t</Point>\n" +
				"</Placemark>\n";
		return out;
		
		/*
				"\t\t<altitudeMode>relativeToGround</altitudeMode>\n" +
				"\t\t<extrude>1</extrude>\n"+
		 */
	}
}
