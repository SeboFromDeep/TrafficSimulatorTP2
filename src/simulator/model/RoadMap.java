package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exception.InvalidItineraryException;
import exception.JunctionNotPresentException;
import exception.RoadAlreadyPresentException;
import exception.VehicleAlreadyPresentException;

public class RoadMap {
	
	//Atributes
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	
	private Map<String, Junction> junctionMap;
	private Map<String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;
	
	
	public RoadMap() {
		this.junctions = new ArrayList<Junction>();
		this.roads = new ArrayList<Road>();
		this.vehicles = new ArrayList<Vehicle>();
		this.junctionMap = new HashMap<String, Junction>();
		this.roadMap = new HashMap<String, Road>();
		this.junctionMap = new HashMap<String, Junction>();
	}
	
	//Methods
	void addJunction(Junction j) {
		if (!junctionMap.containsKey(j.getId())) {							//if the junction isn't in the RoadMap 
			junctions.add(j);												//add it to the end of the list
			junctionMap.put(j.getId(), junctions.get(junctions.size()-1));	//add a entry to the junctionsMap and assign it to the new junction
		}
	}
	
	void addRoad(Road r) throws RoadAlreadyPresentException, JunctionNotPresentException {
		if (roadMap.containsKey(r.getId())) throw new RoadAlreadyPresentException(String.format("[ERROR]: the Road %s already exist in the RoadMap.", r.getId()));  
		if (!junctionMap.containsKey(r.getSrcJunc().getId()) || !junctionMap.containsKey(r.getDestJunc().getId())) throw new JunctionNotPresentException("[ERROR]: both Junctions of the Road have to be Present in the RoadMap.");
		roads.add(r);
		roadMap.put(r.getId(), roads.get(roads.size()-1));
		
	}
	
	void addVehicle(Vehicle v) throws InvalidItineraryException, VehicleAlreadyPresentException {
		if (roadMap.containsKey(v.getId())) throw new VehicleAlreadyPresentException(String.format("[ERROR]: the Vehicle %s already exist in the RoadMap.", v.getId()));  
		if (invalidItinerary(v)) throw new InvalidItineraryException(String.format("[ERROR]: the itinerary of %s can not be completed.", v.getId()));
		vehicles.add(v);
		vehicleMap.put(v.getId(), vehicles.get(vehicles.size()-1));
	}
	

	public Junction getJunction(String id) {
		return junctionMap.get(id);
	}
	
	public Road getRoad(String id) {
		return roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehicleMap.get(id);
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		JSONArray junctionsArray = new JSONArray();
		for (Junction j : junctions) {
			JSONObject joJunction = j.report();
			junctionsArray.put(joJunction);
		}
		jo.put("junctions", junctionsArray);
		
		JSONArray RoadsArray = new JSONArray();
		for (Road r : roads) {
			JSONObject joRoad = r.report();
			RoadsArray.put(joRoad);
		}
		jo.put("roads", RoadsArray);
		
		JSONArray vehiclesArray = new JSONArray();
		for (Junction v : junctions) {
			JSONObject joJunction = v.report();
			vehiclesArray.put(joJunction);
		}
		jo.put("vehicles", vehiclesArray);
		
		
		return jo;
	}
	
	void reset() {
		this.junctions.clear();;
		this.roads.clear();
		this.vehicles.clear();
		this.junctionMap.clear();
		this.roadMap.clear();
		this.junctionMap.clear();
	}
	
	private boolean invalidItinerary(Vehicle v) {
		for (int i = 0; i < v.getItinerary().size()-1; i++) {
			Junction jSrc = v.getItinerary().get(i);
			Junction jDest = v.getItinerary().get(i+1);
			if (!junctionMap.containsKey(jSrc.getId())) return false; 			//the junction isn't present in the RoadMap
			if (roadMap.containsKey(jSrc.roadTo(jDest).getId())) return false;	//the road between jSrc and JDest isn't in the RoadMap
		}
			//the loop doesn't check the last junction (if there is at least 3 junctions)
			if (junctionMap.containsKey(v.getItinerary().get(v.getItinerary().size()-1).getId())) return false;
		return true;
	}
	
	//Getter & Setters
	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(junctions);
	}

	public List<Road> getRoads() {
		return Collections.unmodifiableList(roads);
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
	
}
