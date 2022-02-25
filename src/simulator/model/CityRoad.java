package simulator.model;

import exception.InvalidArgumentsException;
import exception.NegativeSpeedException;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws InvalidArgumentsException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		// TODO Auto-generated constructor stub
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		switch (getWeather()) {
		case WINDY:
			x = 10;
			break;
		case STORM:
			x = 20;
			break;
		default:
			x = 2;
			break;
		}
		setTotalCont(Math.min(getTotalCont()-x, 0));
	}

	@Override
	void updateSpeedLimit() {
		// always maxSpeed
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return  (11-v.getContClass()*getMaxSpeed()/11);
	}

}
