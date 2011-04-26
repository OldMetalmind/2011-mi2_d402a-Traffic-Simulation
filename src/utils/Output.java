package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import traffic.GPSSignal;
import traffic.Path;

public class Output {

	ArrayList<Pair<String,String>> data;
	private static String filename;
	
	public Output(String name){
		filename = name;
	}
	
	public void save2database(){
		//TODO
	}
	
	public void save2file(String output){
		writeFile(output,"txt");
	}
	
	public void savePathToKML(Path path){		
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
	public void save2KML(){		
		//TODO
	}
	
	public String toString(){
		String out = "";
		for(int i = 0; i < data.size(); i++){
			if(i == 0){
				out += data.get(0).toString();
				continue;
			}
			out += " " + data.get(i).toString();
		}
		return out;
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
