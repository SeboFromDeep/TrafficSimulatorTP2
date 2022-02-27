package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import exception.InvalidArgumentsException;
import exception.InvalidContClassException;
import exception.InvalidIdException;
import exception.InvalidMaxSpeedException;
import exception.ItineraryTooShortException;
import exception.NegativeContaminationException;
import exception.NegativeSpeedException;
import exception.RoadException;
import exception.VehicleException;

public class Vehicle extends SimulatedObject {
	
	//Atributes
	private List<Junction> itinerary;
	private int maxSpeed;
	private int speed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contamination;
	private int contClass;
	private int distance;
	

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws InvalidArgumentsException{//hacer comprobaciones
		super(id);
		try {
			validateArguments(id, maxSpeed, contClass, itinerary);
			this.maxSpeed = maxSpeed;
			this.contClass = contClass;
			this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
			this.distance = 0;
			this.contamination = 0;
			this.status = VehicleStatus.PENDING;
			
		} catch (InvalidIdException | InvalidMaxSpeedException | InvalidContClassException | ItineraryTooShortException e) {
			throw new InvalidArgumentsException(e.getMessage());
		}
		
	}

	//Methods
	@Override
	void advance(int time) {
		// TODO Auto-generated method stub
		int old_location = location;	// var used to calculate the advanced spaced
		
		this.location = Math.min((location + speed), road.getLength());	// recalculate location
		
		int advanced_spaced = location - old_location;
		this.distance += advanced_spaced;
		
		int increasedCont = contClass * advanced_spaced;
		this.contamination += increasedCont;
		try {
			road.addContamination(increasedCont);
		} catch (NegativeContaminationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (location >= road.getLength()) {			// if the vehicle reaches the end of the road:
			road.getDestJunc().enter(this);			// vehicle enters junction
			this.setStatus(VehicleStatus.WAITING);	// changes status to waiting
		}
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		JSONObject vehicle = new JSONObject();
		vehicle.put("id", getId());
		vehicle.put("speed", getSpeed());
		vehicle.put("distance", getDistance());
		vehicle.put("co2", getContamination());
		vehicle.put("class", getContClass());
		vehicle.put("status", getStatus().toString());
		if (getStatus().equals(VehicleStatus.TRAVELING) || getStatus().equals(VehicleStatus.WAITING)) {
			vehicle.put("road", getRoad().toString());
			vehicle.put("location", getLocation());
		}
		
		return vehicle;
	}

	void moveToNextRoad() {
		// Case 1: vehicle enters first road of itinerary
		if(getStatus() == VehicleStatus.PENDING) {
			try {
				this.road = itinerary.get(0).roadTo(itinerary.get(1));
				road.enter(this);
				location = 0;
				setStatus(VehicleStatus.TRAVELING);
			}
			catch (RoadException e) {
				e.printStackTrace();
			}
		}
		// Case 2: vehicle exits last road of itinerary
		else if (road.getDestJunc() == itinerary.get(itinerary.size() - 1)) {
			road.exit(this);
			this.road = null;
			setStatus(VehicleStatus.ARRIVED);
		}
		// Case 3: vehicle enters next road of itinerary
		else {
			try {
				Road nextRoad = road.getDestJunc().roadTo(road.getDestJunc());
				road.exit(this);
				this.road = nextRoad;
				this.location = 0;
				road.enter(this);
			} catch (RoadException e) {
				e.printStackTrace();
			}
			
			setStatus(VehicleStatus.TRAVELING);
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


	public void setSpeed(int s) throws NegativeSpeedException{
		if (s < 0) throw new NegativeSpeedException("[ERROR]: speed can not be negative");
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

	public int getContamination() {
		return contamination;
	}


	public void setContamination(int contamination) {
		this.contamination = contamination;
	}


	public int getContClass() {
		return contClass;
	}


	public void setContClass(int contClass) throws InvalidContClassException{
		if (contClass < 0 || contClass > 10) throw new InvalidContClassException("[ERROR]: contClass has to be an integer between 0 and 10 (both included).");
		this.contClass = contClass;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}

	
	public void validateArguments(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws InvalidIdException, InvalidMaxSpeedException, InvalidContClassException, ItineraryTooShortException{
		if (id == null || id == "") throw new InvalidIdException("[ERROR]: id can not be null or an empty string.");
		if (maxSpeed < 1 ) throw new InvalidMaxSpeedException("[ERROR]: maxSpeed has to be a positive integer.");
		if (contClass < 0 || contClass > 10) throw new InvalidContClassException("[ERROR]: contClass has to be an integer between 0 and 10 (both included).");
		if (itinerary.size() < 2) throw new ItineraryTooShortException("[ERROR]: itinerary size has to be at least 2.");
	}
}

