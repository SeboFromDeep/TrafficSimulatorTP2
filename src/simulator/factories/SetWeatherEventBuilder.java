package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		
		JSONArray roadsJson = data.getJSONArray("info");
		List<Pair<String, Weather>> roadsList = new ArrayList<>();
		for (int i = 0; i < roadsJson.length(); i++) {
			JSONObject r = roadsJson.getJSONObject(i);
			String id = r.getString("road");
			Weather w = Weather.valueOf(r.getString("weather").toUpperCase());
			roadsList.add(new Pair<String, Weather>(id, w));
		}
		
		try {
			return new SetWeatherEvent(time, roadsList);
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

}
