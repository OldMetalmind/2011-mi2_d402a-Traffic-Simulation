package interfaces;

import project.Vehicle;

public interface IAllVehicles {

	public Vehicle getVehicle(String id);
	public void addVehicle(Vehicle vehicle);
}
