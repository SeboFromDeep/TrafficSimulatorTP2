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
		this.events = new SortedArrayList<Event>(Comparator.comparing(Event::getTime));
		this.time = 0;
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public void advance() {
		//1. increment time
 		time++;
		
		//2. execute and remove events with time == this.time
		List<Event> aux = new LinkedList<>();
		for (Event e : events) {
			if (e.getTime() == this.time) aux.add(e);
			else if (e.getTime() > time) break;
		}
			
		for (Event e : aux) {
			e.execute(roadMap);
			events.remove(e);
		}
		
		//3. make junctions advance
		for (Junction j : roadMap.getJunctions()) {
			j.advance(time);
		}
		
		//en la siguiente entrega intentaremos mejorar la eficiencia de este paso
		for (Vehicle v : roadMap.getVehicles()) {
			if (v.getStatus() == VehicleStatus.PENDING) v.moveToNextRoad();
		}
		
		//4. make roads advance
		for (Road r : roadMap.getRoads()) {
			r.advance(time);
		}
	}

	public JSONObject report() {
		JSONObject ts = new JSONObject();
		ts.put("time", time);
		ts.put("state", roadMap.report());
		
		return ts;
	}
	public void reset() {
		this.events.clear();
		this.roadMap.reset();
		this.time = 0;
	}
}
