package project;

import java.util.ArrayList;
import java.util.Random;

import utils.GenerationUtil;
import utils.OutputUtil;
import utils.Utils;

import dataStructures.AllVehicles;
import dataStructures.Vehicle;
import dataStructures.Zone;

public class TrafficAlgorithm {
			
	public static void start(Integer numberOfCars, 
							 Double frequency, 
							 ArrayList<Zone> fromZones, 
							 ArrayList<Zone> toZones) {
		
		
		GenerationUtil gen = new GenerationUtil();
		Random rand = new Random();
		for(int i = 0; i < numberOfCars; i++){			
			AllVehicles vehicles = new AllVehicles();
			Zone from = selectRandomZone(fromZones, rand);
			Zone to = selectRandomZone(toZones, rand);
			Vehicle v = gen.generateVehicle(from, to);		
			vehicles.addVehicle(v);
		}
		System.exit(1);
	}	
	
	private static Zone selectRandomZone(ArrayList<Zone> zones, Random rand){
		int i = rand.nextInt(zones.size());
		zones.get(i).decreaseNumVehicles();
		if(zones.get(i).getMaxVehicles() == 0)
			zones.remove(i);
		return zones.get(i);
	}
	/*
	 * 
	 * 
	 	OutputUtil outUtil = new OutputUtil("TestOut");
		String output = outUtil.KMLHeader();		
		for(int i = 0; i < numberOfCars; i++){			
			System.out.println(v.toString());			
			output += outUtil.KMLTrip(Utils.UTM2LatLon(v.getShortestPath()));
		}
		
		System.out.println();		
		output += outUtil.KMLFooter();		
		outUtil.writeFile(output, "kml");
	 */
}
