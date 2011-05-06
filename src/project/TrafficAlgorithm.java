package project;

import dataStructures.*;

public class TrafficAlgorithm {
					
	public static void start(UserInput user_input) {
		
		AllVehicles vehicles = new AllVehicles();
		for(int i = 0; i < user_input.getTotalVehicles(); i++){			
			
			Zone from = user_input.getFromZones().selectRandomZone();
			Zone to = user_input.getToZones().selectRandomZone();
			Vehicle v = vehicles.generateVehicle(from, to);		
			vehicles.addVehicle(v);
		}
		
		run(vehicles);
		
		System.exit(1);
	}
	
	private static void run(AllVehicles vehicles){
		
		
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
