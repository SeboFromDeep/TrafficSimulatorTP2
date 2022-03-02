package simulator.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	
	private RoadMap roadMap;
	private List<Event> events;
	private int time;

	public TrafficSimulator() {
		this.roadMap = new RoadMap();
		this.events = new SortedArrayList<Event>(Comparator.comparing(Event::getTime)); // no se si está bien del todo
		this.time = 0;
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public void advance() {
		//1. increment time
		time++;
		
		//2. execute and remove events with time == this.time
		for (Event e : events) {
			if (e.getTime() == time) {
				e.execute(roadMap);
				events.remove(e);
			}
			if (e.getTime() > time) break; // since the events list is sorted by time we can break after an event has greater time
		}
		
		//3. make junctions advance
		for (Junction j : roadMap.getJunctions()) {
			j.advance(time);
		}
		
		//4. make roads advance
		for (Road r : roadMap.getRoads()) {
			r.advance(time);
		}
	}

	public JSONObject report() {
		JSONObject ts = new JSONObject();
		ts.put("time", time);
		ts.put("state", roadMap.report().toString());
		
		return ts;
	}
}
