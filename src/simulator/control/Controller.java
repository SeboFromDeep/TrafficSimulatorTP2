package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
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
		PrintStream p = new PrintStream(out);
		int i = 0;
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		while(i < n) {
			trafficsimulator.advance();
			ja.put(trafficsimulator.report());
			i++;
		}
		jo.put("states", ja);
		p.print(jo.toString(3));
	}
	public void reset() {
		trafficsimulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		trafficsimulator.addObserver(o);
	}
	public void removeObserver(TrafficSimObserver o) {
		trafficsimulator.removeObserver(o);
	}
	public void addEvent(Event e) {
		trafficsimulator.addEvent(e);
	}
	
	public void run(Integer _ticks) {
		// TODO Auto-generated method stub
		
	}
	
}