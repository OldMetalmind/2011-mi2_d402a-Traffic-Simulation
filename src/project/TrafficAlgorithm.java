package project;

import java.sql.SQLException;

import utils.DatabaseUtil;
import utils.OutputUtil;
import dataStructures.*;

public class TrafficAlgorithm {

	private UserInput user;
	private AllVehicles vehicles;
	private DatabaseUtil database;

	public TrafficAlgorithm(UserInput userInput) throws SQLException {
		System.out.println("Traffic Algorithm phase 1 started");
		this.user = userInput;
		this.database = new DatabaseUtil();
		this.database.clearVehicles();

		long t0 = System.currentTimeMillis();
		user = userInput;
		setVehicles(new AllVehicles());
		database = new DatabaseUtil();
		String out = "";
		OutputUtil kml = new OutputUtil("Test");
		out = kml.KMLHeader();

		database.clearVehicles();
		this.vehicles = new AllVehicles();
		for (int i = 0; i < this.user.getTotalVehicles(); i++) {
			Zone from = this.user.getFromZones().selectRandomZone();
			Zone to = this.user.getToZones().selectRandomZone();
			Vehicle v = this.vehicles.generateVehicle(from, to);
			this.vehicles.addVehicle(v);
			this.database.addVehicle(v, this.vehicles.size());
			//TODO: Maybe v.getVehicle_id() would be better than this.vehicles.size();
			// just to avoid errors
			System.out.println((i + 1) + "/" + this.user.getTotalVehicles());
			System.out.println("Execution time: "
					+ (System.currentTimeMillis() - t0) + "miliceconds");
		}
		run();
	}

	private void setVehicles(AllVehicles allVehicles) {
		// TODO Auto-generated method stub
	}

	public void run() {
		System.out.println("Traffic Algorithm phase 2 started");
		DatabaseUtil database = new DatabaseUtil();
		double time = 0;
		while (time <= this.user.getDuration()) {
			// System.out.println(
			// this.vehicles.getVehicle(0).getActualPosition() );
			this.vehicles.move(database, this.user.getDuration() - time, time);
			time += user.getFrequency();
		}
		
		this.database.clearSignals();
		int number_of_cars = vehicles.size();
		for (int i = 0; i < number_of_cars; i++) {
			Vehicle v = this.vehicles.getVehicle(i);
			this.database.addSignals(i+1, v); //Vehicles ids starts from 1 not 0
		}

		System.out.println("Traffic Algorithm finished");

		System.out.print("Saving to KML...");
		OutputUtil out = new OutputUtil("voyagesUTM");
		out.save2KML(this.vehicles);
		System.out.println("...saved");

		System.out.print("Saving to database...");
		database.save(this.vehicles);
		System.out.println("...saved");

		System.exit(1);
	}
}
