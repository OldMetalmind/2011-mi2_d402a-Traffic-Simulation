package interfaces;

import java.util.Vector;

import utils.DatabaseUtil;
import dataStructures.Vehicle;
import dataStructures.Zone;

public interface IAllVehicles {

	public void addVehicle(Vehicle vehicle);
	public Vehicle getVehicle(Integer index);
	public Vector<Vehicle> getVehicles();
	public Vehicle generateVehicle(Zone from, Zone to, int id);
	public void move(DatabaseUtil database, double timeLeft, double time);
	public int size();
}
