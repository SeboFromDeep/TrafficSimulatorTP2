package simulator.factories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		
		JSONArray itJson = data.getJSONArray("itinerary");
		List<String> itList = new ArrayList<String>();
		for (int i = 0; i < itJson.length(); i++) {
			itList.add(itJson.getString(i));
		}
		
		return new NewVehicleEvent(time, id, maxSpeed, contClass, itList);
	}

}
