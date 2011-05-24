package project;

import java.sql.SQLException;

import utils.DatabaseUtil;
import utils.OutputUtil;
import utils.Utils;
import dataStructures.*;

public class TrafficAlgorithm {

	private UserInput user;
	private AllVehicles vehicles;
	private DatabaseUtil database;
	long t0 = System.currentTimeMillis();

	public TrafficAlgorithm(UserInput userInput) throws SQLException {
		System.out.println("Traffic Algorithm phase 1 started");
		this.user = userInput;
		this.database = new DatabaseUtil();
		this.database.clearVehicles();
		/*
		System.out.println("_________");
		double d = Utils.distance(this.user.getFromZones().getZone(0).getCenter(), this.user.getToZones().getZone(0).getCenter());
		System.out.println("_________  " + d +" _________");
		*/
		
		
		init();
		OutputUtil zones_output = new OutputUtil("zones");
		zones_output.writeZones(this.user.getFromZones(), this.user.getToZones());
		user = userInput;		
		database = new DatabaseUtil();
		String out = "";
		OutputUtil shortest_paths_output = new OutputUtil("shortest_paths");
		out = shortest_paths_output.KMLHeader();

		database.clearVehicles();
		this.vehicles = new AllVehicles();
		for (int i = 0; i < this.user.getTotalVehicles(); i++) {
			Zone from = this.user.getFromZones().selectRandomZone();
			Zone to = this.user.getToZones().selectRandomZone();
			Vehicle v = this.vehicles.generateVehicle(from, to, i);
			this.vehicles.addVehicle(v);
			this.database.addVehicle(v, v.getVehicle_id());			
			out += shortest_paths_output.KMLTrip(v.getShortestPath(), "UTM");
			System.out.println((i+1) +"/"+ this.user.getTotalVehicles());	
		//	 System.out.println("Execution time: " + (System.currentTimeMillis()-t0) + "miliseconds");
		}
		out += shortest_paths_output.KMLFooter();
		shortest_paths_output.writeFile(out, "kml");
		//run();
		}
	
	

	private void init() {
		// TODO Auto-generated method stub
		
	}

	public void run(){
		System.out.println("Traffic Algorithm phase 2 started");
		DatabaseUtil database = new DatabaseUtil();
		double time = user.getFrequency();
		while(time <= this.user.getDuration()){	
			//this.vehicles.move(database, this.user.getDuration() - time, time);
			this.vehicles.move(database, user.getFrequency(), time);
			time += user.getFrequency();
		}
		
		System.out.println("Total time: "+(System.currentTimeMillis()-t0)+" miliseconds.");
		/*
		System.out.println("Traffic Algorithm finished");

		System.out.print("Saving to KML...");
		OutputUtil out = new OutputUtil("voyages");
		out.save2KML(this.vehicles, "UTM");
		System.out.println("...saved");

		System.out.print("Saving to database...");
		database.save(this.vehicles);
		System.out.println("...saved");
		*/
		

		System.exit(1);
	}
}
