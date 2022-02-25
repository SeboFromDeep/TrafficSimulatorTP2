package simulator.model;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	//Attributes
	private List<Road> incomingRoads;
	private Map<Junction, Road> outgoingRoads;
	private List<List<Vehicle>> queueList;
	//private Map<Road, List<Vehicle>> roadListMap;
	private LightSwitchingStrategy lsStrat;
	private DequeingStrategy dqStrat;
	private int green;
	private int red;
	private int x;
	private int y;

	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor) {//comprobar valores
		super(id);
		// TODO Auto-generated constructor stub
	}

	//Methods
	@Override
	void advance(int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}
	
	void addIncommingRoad(Road r) {
		
	}
	
	void addOutGoingRoad(Road r) {
		
	}

	void enter(Vehicle v) {
		
	}
	
	Road roadTo(Junction j) {
		return null;
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

	public DequeingStrategy getDqStrat() {
		return dqStrat;
	}

	public void setDqStrat(DequeingStrategy dqStrat) {
		this.dqStrat = dqStrat;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
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
	
}
