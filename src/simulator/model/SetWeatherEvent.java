package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	private List<Pair<String,Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws IllegalArgumentException {
		super(time);
		if(ws == null) throw new IllegalArgumentException("[ERROR]:	weather can not be null.");
		this.ws = ws;
		}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> i : ws) {
			Road aux = map.getRoad(i.getFirst());
			aux.setWeather(i.getSecond());
		}
	}
}
