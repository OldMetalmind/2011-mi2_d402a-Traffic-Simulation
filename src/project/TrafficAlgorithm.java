package project;

import java.util.ArrayList;

import utils.GenerationUtil;

import dataStructures.Vehicle;
import dataStructures.Zone;

public class TrafficAlgorithm {
	
	public static void start(Integer numberOfCars, 
							 Double frequency, 
							 ArrayList<Zone> fromZones, 
							 ArrayList<Zone> toZones) {	
		
		GenerationUtil gen = new GenerationUtil();
		for(int i = 0; i < numberOfCars; i++){
			Vehicle v = gen.generateVehicle(fromZones.get(0), toZones.get(0));
			System.out.println(v.toString());
		}	
		
	}	
	
}
