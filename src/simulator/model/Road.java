package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	
	//Atributes
	private Junction srcJunc;
	private Junction destJunc;
	private int maxSpeed;
	private int speedLimit;
	private int contLimit;
	private int totalCont;
	private int length;
	private Weather weather;
	private List<Vehicle> vehicles;//cambiar getter

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws IllegalArgumentException{
		super(id);
		try {
			validateArguments(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
			this.srcJunc = srcJunc;
			this.destJunc = destJunc;
			this.maxSpeed = maxSpeed;
			this.speedLimit = maxSpeed;
			this.contLimit = contLimit;
			this.length = length;
			this.weather = weather;
			this.totalCont = 0;
			this.vehicles = new ArrayList<Vehicle>();
			
			try {
				this.srcJunc.addOutGoingRoad(this);
				this.destJunc.addIncommingRoad(this);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			
	}


	//Abstract methods
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	//Methods
	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for (Vehicle v : vehicles) {
			try {
				v.setSpeed(calculateVehicleSpeed(v));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			v.advance(time);
		}
		vehicles.sort(Comparator.comparing(Vehicle::getLocation).reversed());
	}

	@Override
	public JSONObject report() {
		JSONObject road = new JSONObject();
		road.put("id", getId());
		road.put("speedlimit", getSpeedLimit());
		road.put("weather", getWeather().toString());
		road.put("co2", getTotalCO2());
		
		JSONArray vehicles_array = new JSONArray();
		for (Vehicle v : vehicles) {
			vehicles_array.put(v.getId());
		}
		road.put("vehicles", vehicles_array);
		return road;
	}
	
	void enter(Vehicle v) throws IllegalArgumentException{
		if (v.getLocation() == 0 && v.getSpeed() == 0) vehicles.add(v);
		else throw new IllegalArgumentException("[ERROR]: vehicle has to have speed 0 and location 0 to enter a Road.");
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void addContamination(int c) throws IllegalArgumentException{
		if (c < 0) throw new IllegalArgumentException("[ERROR]: contamination has to be positive");
		totalCont += c;
	}
	
	public void setWeather(Weather weather) throws IllegalArgumentException{
		if (weather == null) throw new IllegalArgumentException("[ERROR]: weather can not be null");
		this.weather = weather;
	}

	//Getters & Setters
	public Junction getSrc() {
		return srcJunc;
	}


	public void setSrc(Junction srcJunc) {
		this.srcJunc = srcJunc;
	}


	public Junction getDest() {
		return destJunc;
	}


	public void setDest(Junction destJunc) {
		this.destJunc = destJunc;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}


	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	
	public int getSpeedLimit() {
		return speedLimit;
	}


	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}


	public int getContLimit() {
		return contLimit;
	}


	public void setContLimit(int contLimit) {
		this.contLimit = contLimit;
	}


	public int getTotalCO2() {
		return totalCont;
	}


	public void setTotalCO2(int totalCont) {
		this.totalCont = totalCont;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public Weather getWeather() {
		return weather;
	}


	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}


	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public void validateArguments(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws IllegalArgumentException{
		if (id == null || id == "") throw new IllegalArgumentException("[ERROR]: id can not be null or an empty string.");
		if (maxSpeed < 1) throw new IllegalArgumentException("[ERROR]: maxSpeed has to be a positive integer.");
		if (contLimit < 1) throw new IllegalArgumentException("[ERROR]: contLimit can not be negative.");
		if (length < 1) throw new IllegalArgumentException("[ERROR]: length has to be positive.");
		if (srcJunc == null || destJunc == null) throw new IllegalArgumentException("[ERROR]: neither source Junction or destination Junction can be null.");
		if (weather == null) throw new IllegalArgumentException("[ERROR]: weather can not be null.");
	}
}
