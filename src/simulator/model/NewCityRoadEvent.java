package simulator.model;

import exception.InvalidArgumentsException;
import exception.JunctionNotPresentException;
import exception.RoadAlreadyPresentException;

public class NewCityRoadEvent extends Event {

	private String id;
	private String srcJunc;
	private String destJunc;
	private int length;
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;

	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id = id;
		this.srcJunc = srcJun;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		try {
			map.addRoad(new CityRoad(id, map.getJunction(srcJunc), map.getJunction(destJunc), maxSpeed, co2Limit, length, weather));
		} catch (RoadAlreadyPresentException | JunctionNotPresentException | InvalidArgumentsException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}

}
