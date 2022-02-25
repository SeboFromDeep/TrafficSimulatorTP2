package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import exception.InvalidArgumentsException;
import exception.NegativeCoordException;
import exception.NullStrategyException;

public class Junction extends SimulatedObject {
	
	//Attributes
	private List<Road> incomingRoads;				//carreteras entrantes
	private Map<Junction, Road> outgoingRoads;		//mapa<cruce, carretera> 
	private List<List<Vehicle>> queueList;			//lista de coches de una carretera que esperan en el cruce
	private Map<Road, List<Vehicle>> roadListMap; 	//mapa para buscar mejor en la lista anterior
	private LightSwitchingStrategy lsStrat;
	private DequeingStrategy dqStrat;
	private int green;
	private int lastSwitchingTime;
	private int x;
	private int y;

	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor) throws InvalidArgumentsException {//comprobar valores
		super(id);
		try {
			validateArguments(lsStrategy, dqStrategy, xCoor, yCoor);
			this.incomingRoads = new LinkedList<Road>();
			this.outgoingRoads = new HashMap<Junction, Road>();
			this.queueList = new ArrayList<List<Vehicle>>();
			this.roadListMap = new HashMap<Road, List<Vehicle>>();
			this.lsStrat = lsStrategy;
			this.dqStrat = dqStrategy;
			this.green = -1;
			this.lastSwitchingTime = 0;
			this.x = xCoor;
			this.y = yCoor;
		} catch (NullStrategyException | NegativeCoordException e) {
			throw new InvalidArgumentsException(e.getMessage());
		}
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
		incomingRoads.add(r);
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
	
	public void validateArguments(LightSwitchingStrategy lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor) throws NullStrategyException, NegativeCoordException {
		if (lsStrategy == null || dqStrategy == null) throw new NullStrategyException("[ERROR]: can not have a null LightSwitchingStrategy or DequeingStrategy.");
		if (xCoor < 0 || yCoor < 0)	throw new NegativeCoordException("[ERROR]: xCoor and yCoor have to be a positive integer");
	}
}
