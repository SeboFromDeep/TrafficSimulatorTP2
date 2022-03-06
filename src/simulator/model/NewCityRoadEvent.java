package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent{
	
	

	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) {
		Junction aux = map.getJunction(src);
		Junction aux2 = map.getJunction(dest);
		CityRoad r = new CityRoad(id, aux, aux2, maxSpeed, contLimit, length, weather);
		map.addRoad(r);
	}

}

