package interfaces;

import dataStructures.Vehicle;
import dataStructures.Zone;

public interface IAllVehicles {

	public void addVehicle(Vehicle vehicle);
	Vehicle getVehicle(Integer index);
	public Vehicle generateVehicle(Zone from, Zone to);
}
