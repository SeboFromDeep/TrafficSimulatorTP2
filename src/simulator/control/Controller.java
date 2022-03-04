package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator trafficsimulator;
	private Factory<Event> eventfactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws Exception{
	// TODO complete
		if(sim == null || eventsFactory == null) throw new Exception();
		this.trafficsimulator = sim;
		this.eventfactory = eventsFactory;
	}
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray ja = jo.getJSONArray("events");
		for(int i = 0;i < ja.length();i++) {
			trafficsimulator.addEvent(eventfactory.createInstance(ja.getJSONObject(i)));
		}
	}
	public void run(int n, OutputStream out) throws Exception {
		int i = 0;
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		while(i < n) {
			trafficsimulator.advance();
			ja.put(trafficsimulator.report());
			i++;
		}
		jo.put("states", ja);
	}
	public void reset() {
		trafficsimulator.reset();
	}
}