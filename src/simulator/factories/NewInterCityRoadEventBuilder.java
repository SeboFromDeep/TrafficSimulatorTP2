package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event> {

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		String src = data.getString("src");
		String dest = data.getString("dest");
		int length = data.getInt("length");
		int contLimit = data.getInt("co2limit");
		int maxSpeed = data.getInt("maxspeed");
		
		String s = data.getString("weather").toUpperCase();
		Weather w = Weather.valueOf(s);
		return new NewInterCityRoadEvent(time, id, src, dest, length, contLimit, maxSpeed, w);
	}

}
