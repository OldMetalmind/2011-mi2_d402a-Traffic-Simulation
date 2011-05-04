package project;

import java.util.ArrayList;

import utils.GenerationUtil;
import utils.OutputUtil;

import dataStructures.Vehicle;
import dataStructures.Zone;

public class TrafficAlgorithm {
	
	public static void start(Integer numberOfCars, 
							 Double frequency, 
							 ArrayList<Zone> fromZones, 
							 ArrayList<Zone> toZones) {
		OutputUtil outUtil = new OutputUtil("TestOut");		
		
		String output = outUtil.KMLHeader();
		GenerationUtil gen = new GenerationUtil();
		for(int i = 0; i < numberOfCars; i++){
			Vehicle v = gen.generateVehicle(fromZones.get(0), toZones.get(0));
			output += outUtil.KMLTrip(v.getTrip());
			System.out.println(v.toString());
		}	
		output += outUtil.KMLFooter();
		System.out.println();
		outUtil.writeFile(output, ".kml");		
	}	
	
}
