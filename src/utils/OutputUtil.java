package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import dataStructures.GPSSignal;
import dataStructures.Pair;
import dataStructures.Trip;



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
	
	//public void save2database(){}
	
	public void save2file(String output){
		writeFile(output,"txt");
	}
	
	/*
	 * This only saves 1 trip to a KML file
	 */
	public void savePathToKML(Trip path){
		
		String output = "<?xml version='1.0' encoding='UTF-8'?>\n";
		//output += "<import namespace='http://www.w3.org/2005/Atom' schemaLocation='http://code.google.com/apis/kml/schema/atom-author-link.xsd'/>";
		output += "<kml xmlns='http://www.opengis.net/kml/2.2'>\n";		
		output += "<Document>\n" +
					"<Placemark>\n" +
						"<name>GPS path</name>\n" +
							"<MultiGeometry>\n" +
								/*"<LineString>\n" +
									"<extrude>1</extrude>\n" +
										"<tessellate>0</tessellate>\n" +
										"<altitudeMode>clampToGround</altitudeMode>\n" +
										"<coordinates></coordinates>\n" +
								"</LineString>\n" +*/
								"<LineString>\n" +
									"<coordinates>\n";
									for(int i = 0; i < path.size(); i++){
										GPSSignal g = path.getInstance(i);
										output += g.getLongitude()+","+g.getLatitude()+"\n";
									}		
		output += 					"</coordinates>\n" +
								"</LineString>\n" +
							"</MultiGeometry>\n" +
					"</Placemark>\n" +
				"</Document>\n" +
				"</kml>";
		
		writeFile(output,"kml");		
	}
	
	/*
	 * This method accepts a list of trips and writes them into a .kml file 
	 */
	public void save2KML(ArrayList<Trip> trips){		
		//TODO: Given a undetermined number of trips write them all into a .kml file
		//check Helper.java
	}
	
	
	private void writeFile(String output, String ext){
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
}
