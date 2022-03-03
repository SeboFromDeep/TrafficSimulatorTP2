package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;
import simulator.model.Weather;

public class SetContClassEventBuilder extends Builder<Event>{

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		
		JSONArray vehiclesJson = data.getJSONArray("info");
		List<Pair<String, Integer>> vehiclesList = new ArrayList<>();
		for (int i = 0; i < vehiclesJson.length(); i++) {
			JSONObject v = vehiclesJson.getJSONObject(i);
			String id = v.getString("vehicle");
			int c = data.getInt("class");
			vehiclesList.add(new Pair<String, Integer>(id, c));
		}
		try {
			return new SetContClassEvent(time, vehiclesList);
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

}
