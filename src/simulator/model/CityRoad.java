package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws IllegalArgumentException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		switch (getWeather()) {
		case WINDY:
			x = 10;
			break;
		case STORM:
			x = 10;
			break;
		default:
			x = 2;
			break;
		}
		setTotalCO2(Math.max(getTotalCO2()-x, 0));
	}

	@Override
	void updateSpeedLimit() {
		// always maxSpeed
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return  ((11-v.getContClass())*getSpeedLimit()/11);
	}

}
