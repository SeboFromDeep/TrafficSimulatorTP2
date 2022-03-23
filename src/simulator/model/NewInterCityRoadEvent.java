package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent{

	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) {
		Junction aux = map.getJunction(src);
		Junction aux2 = map.getJunction(dest);
		InterCityRoad r = new InterCityRoad(id, aux, aux2, maxSpeed, contLimit, length, weather);
		map.addRoad(r);
	}
	
	public String toString() {
		return String.format("New Inter City Road '%s'", id);
	}
}

