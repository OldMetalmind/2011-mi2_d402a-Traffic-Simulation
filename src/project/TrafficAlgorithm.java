package project;

import java.sql.SQLException;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.*;

import utils.DatabaseUtil;
import utils.OutputUtil;
import dataStructures.*;

public class TrafficAlgorithm {
		
	private UserInput user;
	private AllVehicles vehicles;
	private DatabaseUtil database;


	public TrafficAlgorithm(UserInput userInput) throws SQLException{
		
		long t0 = System.currentTimeMillis();
		user = userInput;
		setVehicles(new AllVehicles());
		database = new DatabaseUtil();
		String out = "";
		OutputUtil kml = new OutputUtil("Test");
		out = kml.KMLHeader();
		
		database.clearVehicles(); 
		AllVehicles vehicles = new AllVehicles();
		for(int i = 0; i < user.getTotalVehicles(); i++){			
			Zone from = user.getFromZones().selectRandomZone();
			Zone to = user.getToZones().selectRandomZone();
			
			Vehicle v = vehicles.generateVehicle(from, to);
			vehicles.addVehicle(v);
			database.addVehicle(v,vehicles.size());
			//System.out.println(v);
		}
        System.out.println("Execution time: " + (System.currentTimeMillis()-t0) + "miliceconds");

	}
	
	public void run(){
		DatabaseUtil database = new DatabaseUtil();
		double time = 0;
		while(time <= user.getDuration()){			
			vehicles.move(database, user.getDuration() - time);			
			time+=user.getFrequency();
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
			output += outUtil.KMLTrip(Utils.UTM2LatLon(v.getShortestPath()));
		}
				
		output += outUtil.KMLFooter();		
		outUtil.writeFile(output, "kml");
	 */
}
