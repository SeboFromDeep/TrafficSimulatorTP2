package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	
	//Atributes
	private List<Junction> itinerary;
	private int maxSpeed;
	private int speed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int totalCO2;
	private int contClass;
	private int distance;
	private int nextJuncIdx;
	

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws IllegalArgumentException{
		super(id);
		try {
			validateArguments(id, maxSpeed, contClass, itinerary);
			this.maxSpeed = maxSpeed;
			this.contClass = contClass;
			this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
			this.distance = 0;
			this.totalCO2 = 0;
			this.status = VehicleStatus.PENDING;
			this.nextJuncIdx = 1;
			
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		
	}

	//Methods
	@Override
	void advance(int time) {
		if (status == VehicleStatus.TRAVELING) {
			int old_location = location;	// var used to calculate the advanced spaced
			
			this.location = Math.min((location + speed), road.getLength());	// recalculate location
			
			int advanced_spaced = location - old_location;
			this.distance += advanced_spaced;
			
			int increasedCont = contClass * advanced_spaced;
			this.totalCO2 += increasedCont;
			try {
				road.addContamination(increasedCont);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (location >= road.getLength()) {			// if the vehicle reaches the end of the road:
				road.getDest().enter(this);			// vehicle enters junction
				this.setStatus(VehicleStatus.WAITING);	// changes status to waiting
			}	
		}
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		JSONObject vehicle = new JSONObject();
		vehicle.put("id", getId());
		vehicle.put("speed", getSpeed());
		vehicle.put("distance", getDistance());
		vehicle.put("co2", getTotalCO2());
		vehicle.put("class", getContClass());
		vehicle.put("status", getStatus().toString());
		if (getStatus().equals(VehicleStatus.TRAVELING) || getStatus().equals(VehicleStatus.WAITING)) {
			vehicle.put("road", getRoad().toString());
			vehicle.put("location", getLocation());
		}
		
		return vehicle;
	}

	void moveToNextRoad() {
		if (status != VehicleStatus.ARRIVED) {
			// Case 1: vehicle enters first road of itinerary
			if(getStatus() == VehicleStatus.PENDING) {
				try {
					this.road = itinerary.get(0).roadTo(itinerary.get(nextJuncIdx));
					nextJuncIdx++;
					road.enter(this);
					location = 0;
					setStatus(VehicleStatus.TRAVELING);
				}
				catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());;
				}
			}
			// Case 2: vehicle exits last road of itinerary
			else if (road.getDest() == itinerary.get(itinerary.size() - 1)) {
				road.exit(this);
				this.road = null;
				setStatus(VehicleStatus.ARRIVED);
			}
			// Case 3: vehicle enters next road of itinerary
			else {
				try {
					Road nextRoad = road.getDest().roadTo(itinerary.get(nextJuncIdx));
					nextJuncIdx++;
					road.exit(this);
					this.road = nextRoad;
					this.location = 0;
					road.enter(this);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());;
				}
				
				setStatus(VehicleStatus.TRAVELING);
			}
		}
	}
	
	
	//Getters & Setters
	public List<Junction> getItinerary() {
		return itinerary;
	}


	public void setItinerary(List<Junction> itinerary) {
		this.itinerary = itinerary;
	}


	public int getMaxSpeed() {
		return maxSpeed;
	}


	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}


	public int getSpeed() {
		if (status != VehicleStatus.TRAVELING) return 0;
		return speed;
	}


	public void setSpeed(int s) throws IllegalArgumentException{
		if (s < 0) throw new IllegalArgumentException("[ERROR]: speed can not be negative");
		this.speed = Math.min(getMaxSpeed(), s);
	}


	public VehicleStatus getStatus() {
		return status;
	}


	public void setStatus(VehicleStatus status) {
		this.status = status;
	}


	public Road getRoad() {
		return road;
	}


	public void setRoad(Road road) {
		this.road = road;
	}


	public int getLocation() {
		return location;
	}


	public void setLocation(int location) {
		this.location = location;
	}

	public int getTotalCO2() {
		return totalCO2;
	}


	public void setTotalCO2(int contamination) {
		this.totalCO2 = contamination;
	}


	public int getContClass() {
		return contClass;
	}


	public void setContClass(int contClass) throws IllegalArgumentException{
		if (contClass < 0 || contClass > 10) throw new IllegalArgumentException("[ERROR]: contClass has to be an integer between 0 and 10 (both included).");
		this.contClass = contClass;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}

	
	public void validateArguments(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws IllegalArgumentException{
		if (id == null || id == "") throw new IllegalArgumentException("[ERROR]: id can not be null or an empty string.");
		if (maxSpeed < 1 ) throw new IllegalArgumentException("[ERROR]: maxSpeed has to be a positive integer.");
		if (contClass < 0 || contClass > 10) throw new IllegalArgumentException("[ERROR]: contClass has to be an integer between 0 and 10 (both included).");
		if (itinerary.size() < 2) throw new IllegalArgumentException("[ERROR]: itinerary size has to be at least 2.");
	}
}

