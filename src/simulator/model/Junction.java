package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	//Attributes
	private List<Road> incomingRoads;				//carreteras entrantes
	private Map<Junction, Road> outgoingRoads;		//mapa<cruce, carretera> 
	private List<List<Vehicle>> queueList;			//lista de coches de una carretera que esperan en el cruce
	private Map<Road, List<Vehicle>> roadListMap; 	//mapa para buscar mejor en la lista anterior
	private LightSwitchingStrategy lsStrat;
	private DequeuingStrategy dqStrat;
	private int green;
	private int lastSwitchingTime;
	private int x;
	private int y;

	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws IllegalArgumentException {
		super(id);
		validateArguments(id, lsStrategy, dqStrategy, xCoor, yCoor);
		this.incomingRoads = new ArrayList<Road>();
		this.outgoingRoads = new HashMap<Junction, Road>();
		this.queueList = new ArrayList<List<Vehicle>>();
		this.roadListMap = new HashMap<Road, List<Vehicle>>();
		this.lsStrat = lsStrategy;
		this.dqStrat = dqStrategy;
		this.green = -1;
		this.lastSwitchingTime = 0;
		this.x = xCoor;
		this.y = yCoor;
	}

	//Methods
	@Override
	void advance(int time) {
		if (green != -1) {
			List<Vehicle> dequedVehicles = dqStrat.dequeue(queueList.get(green));
			for (Vehicle v : dequedVehicles) {
				v.moveToNextRoad();					//move v to next road
				queueList.get(green).remove(v);		//remove v from queueList
			}
		}
		int lastGreen = green;
		this.green = lsStrat.chooseNextGreen(incomingRoads, queueList, green, lastSwitchingTime, time);
		if (green != lastGreen) lastSwitchingTime = time;
	}

	@Override
	public JSONObject report() {
		JSONObject junction = new JSONObject();
		
		junction.put("id", getId());
		
		String greenId = "none";
		if (green != -1) greenId = incomingRoads.get(green).getId();	//search for the road in the incomingRoads list
		junction.put("green", greenId);
		
		JSONArray queues = new JSONArray();
		for (int i = 0; i < incomingRoads.size(); i++) {
			JSONObject roadQueue = new JSONObject();
			roadQueue.put("road", incomingRoads.get(i).getId());
			
			JSONArray vehicles = new JSONArray();
			for (Vehicle v : queueList.get(i)) {
				vehicles.put(v.getId());
			}
			roadQueue.put("vehicles", vehicles);
			
			queues.put(roadQueue);
		}
		junction.put("queues", queues);
		
		return junction;
	}
	
	void addIncommingRoad(Road r) throws IllegalArgumentException{
		if (r.getDest() != this) throw new IllegalArgumentException(String.format("[ERROR]: the road does not have %s as Destination Junction.", getId()));
		incomingRoads.add(r);							//add road to incomingRoads List
		List<Vehicle> l = new LinkedList<Vehicle>();	//create a linkedList of the vehicles from Road r
		queueList.add(l);								//add the linkedList to queueList
		roadListMap.put(r, l);							//create a new entry in the roadMapList
	}
	
	void addOutGoingRoad(Road r) throws IllegalArgumentException{
		if (r.getSrc() != this) throw new IllegalArgumentException(String.format("[ERROR]: the road does not have %s as Source Junction.", getId()));
		if (outgoingRoads.containsKey(r.getDest())) throw new IllegalArgumentException(String.format("[ERROR]: there is already a road that goes from %s to %s.", getId(), r.getDest().getId()));
		outgoingRoads.put(r.getDest(), r);
	}

	void enter(Vehicle v) {
		roadListMap.get(v.getRoad()).add(v);
	}
	
	Road roadTo(Junction j) {
		return outgoingRoads.get(j);
	}
	
	
	//Getters & Setters
	public List<Road> getIncomingRoads() {
		return incomingRoads;
	}

	public void setIncomingRoads(List<Road> incomingRoads) {
		this.incomingRoads = incomingRoads;
	}

	public Map<Junction, Road> getOutgoingRoads() {
		return outgoingRoads;
	}

	public void setOutgoingRoads(Map<Junction, Road> outgoingRoads) {
		this.outgoingRoads = outgoingRoads;
	}

	public List<List<Vehicle>> getQueueList() {
		return queueList;
	}

	public void setQueueList(List<List<Vehicle>> queueList) {
		this.queueList = queueList;
	}

	public LightSwitchingStrategy getLsStrat() {
		return lsStrat;
	}

	public void setLsStrat(LightSwitchingStrategy lsStrat) {
		this.lsStrat = lsStrat;
	}

	public DequeuingStrategy getDqStrat() {
		return dqStrat;
	}

	public void setDqStrat(DequeuingStrategy dqStrat) {
		this.dqStrat = dqStrat;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getLastSwitchingTime() {
		return lastSwitchingTime;
	}

	public void setLastSwitchingTime(int lastSwitchingTime) {
		this.lastSwitchingTime = lastSwitchingTime;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void validateArguments(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws IllegalArgumentException{
		if (id == null || id == "") throw new IllegalArgumentException("[ERROR]: id can not be null or an empty string.");
		if (lsStrategy == null || dqStrategy == null) throw new IllegalArgumentException("[ERROR]: can not have a null LightSwitchingStrategy or DequeingStrategy.");
		if (xCoor < 0 || yCoor < 0)	throw new IllegalArgumentException("[ERROR]: xCoor and yCoor have to be a positive integer");
	}
	
	public int getGreenLightIndex() {
		return this.green;
	}

	public List<Road> getInRoads() {
		// TODO Auto-generated method stub
		return this.incomingRoads;
	}
}
