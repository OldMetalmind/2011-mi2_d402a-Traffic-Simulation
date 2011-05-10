package project;

import java.sql.SQLException;

import utils.DatabaseUtil;
import dataStructures.*;

public class TrafficAlgorithm {
		
	private UserInput user;
	private AllVehicles vehicles;
	private DatabaseUtil database;
	
	public TrafficAlgorithm(UserInput userInput) throws SQLException{
		user = userInput;
		setVehicles(new AllVehicles());
		database = new DatabaseUtil();
		
		database.clearVehicles();
		AllVehicles vehicles = new AllVehicles();
		for(int i = 0; i < user.getTotalVehicles(); i++){			
			
			Zone from = user.getFromZones().selectRandomZone();
			Zone to = user.getToZones().selectRandomZone();
			Vehicle v = vehicles.generateVehicle(from, to);			
			vehicles.addVehicle(v);
			database.addVehicle(v,vehicles.size());
		}
	}
	
	public void run(){
		DatabaseUtil database = new DatabaseUtil();
		double timeleft = 0;
		while(timeleft <= user.getDuration()){			
			vehicles.move(database, user.getDuration() - timeleft);			
			timeleft+=user.getFrequency();
		}
		System.exit(1);
	}
	
	public void setVehicles(AllVehicles vehicles) {
		this.vehicles = vehicles;
	}

	public AllVehicles getVehicles() {
		return vehicles;
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
