package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	private List<Pair<String,Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws Exception {
		super(time);
		if(ws == null) throw new Exception();
		this.ws = ws;
		}

	@Override
	void execute(RoadMap map) throws Exception {
		// TODO Auto-generated method stub
		for(Pair<String, Weather> i : ws) {
			Road aux = map.getRoad(i.getFirst());
			aux.setWeather(i.getSecond());
		}
	}
}
