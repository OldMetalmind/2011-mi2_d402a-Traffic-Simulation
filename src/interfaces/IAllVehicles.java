package interfaces;

import utils.DatabaseUtil;
import dataStructures.Vehicle;
import dataStructures.Zone;

public interface IAllVehicles {

	public void addVehicle(Vehicle vehicle);
	Vehicle getVehicle(Integer index);
	public Vehicle generateVehicle(Zone from, Zone to);
	public void move(DatabaseUtil database, double timeLeft);
	public int size();
}
