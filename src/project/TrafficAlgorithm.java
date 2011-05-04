package project;

import java.util.ArrayList;

import utils.GenerationUtil;
import utils.OutputUtil;
import utils.Utils;

import dataStructures.Vehicle;
import dataStructures.Zone;

public class TrafficAlgorithm {
	
	public static void start(Integer numberOfCars, 
							 Double frequency, 
							 ArrayList<Zone> fromZones, 
							 ArrayList<Zone> toZones) {
		
		OutputUtil outUtil = new OutputUtil("TestOut");	
		GenerationUtil gen = new GenerationUtil();		
		String output = outUtil.KMLHeader();	
		for(int i = 0; i < numberOfCars; i++){			
			Vehicle v = gen.generateVehicle(fromZones.get(0), toZones.get(0));
			System.out.println(v.toString());			
			output += outUtil.KMLTrip(Utils.GWS842LatLon(v.getShortestPath()));
		}
		System.out.println();		
		output += outUtil.KMLFooter();		
		outUtil.writeFile(output, "kml");
		System.exit(1);
	}	
	
}
