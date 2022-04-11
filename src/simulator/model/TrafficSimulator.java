package simulator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	
	private RoadMap roadMap;
	private List<Event> events;
	private int time;
	private List<TrafficSimObserver> observers;

	public TrafficSimulator() {
		this.roadMap = new RoadMap();
		this.events = new SortedArrayList<Event>(Comparator.comparing(Event::getTime));
		this.time = 0;
		this.observers = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		notifyOnEventAdded(e);
	}
	
	public void advance() {
		//1. increment time
 		time++;
 		
 		notifyOnAdvanceStart();
		
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
		
		notifyOnAdvanceEnd();
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
		notifyOnReset();
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observers.add(o);
		notifyOnRegister();
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);
	}

	public void notifyOnAdvanceStart() {
		for (TrafficSimObserver o : observers) {
			o.onAdvanceStart(roadMap, events, time);
		}
	}
	
	public void notifyOnAdvanceEnd() {
		for (TrafficSimObserver o : observers) {
			o.onAdvanceEnd(roadMap, events, time);
		}
	}
	
	public void notifyOnEventAdded(Event e) {
		for (TrafficSimObserver o : observers) {
			o.onEventAdded(roadMap, events, e, time);
		}
	}
	
	public void notifyOnReset() {
		for (TrafficSimObserver o : observers) {
			o.onReset(roadMap, events, time);
		}
	}
	
	public void notifyOnRegister() {
		for (TrafficSimObserver o : observers) {
			o.onRegister(roadMap, events, time);
		}
	}
	
	public void notifyError(String message) {
		for (TrafficSimObserver o : observers) {
			o.onError(message);
		}
	}
	
	public List<Event> getEvents() {
		return Collections.unmodifiableList(events);
	}

	public List<Vehicle> getVehicles() {
		return roadMap.getVehicles();
	}
}
