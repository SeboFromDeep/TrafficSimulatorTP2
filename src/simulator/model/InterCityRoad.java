package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws IllegalArgumentException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		switch (getWeather()) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
			break;
		case WINDY:
			x = 15;
			break;
		case STORM:
			x = 20;
			break;
		default:
			//throw new IllegalArgumentException("Unexpected value: " + getWeather());		
		}
		setTotalCO2(((100-x)*getTotalCO2())/100);
	}

	@Override
	void updateSpeedLimit() {
		if (getTotalCO2() > getContLimit()) setSpeedLimit(getMaxSpeed()/2);
		else setSpeedLimit(getMaxSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if (getWeather() == Weather.STORM) return ((getSpeedLimit()*8)/10);
		
		return getMaxSpeed();
	}
	
}
