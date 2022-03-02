package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{
	
	private int MaxSpeed;
	private int ContClass;
	private List<String> itinerary;
	private String id;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int
			contClass, List<String> itinerary) {
			super(time);
			this.id = id;
			this.itinerary = itinerary;
			this.MaxSpeed = maxSpeed;
			this.ContClass = contClass;
			
			}

	@Override
	void execute(RoadMap map) throws Exception {
		// TODO Auto-generated method stub
		List<Junction> l = new ArrayList<Junction>();
		for(String i: itinerary) {
			Junction aux = map.getJunction(i);
			l.add(aux);
		}
		Vehicle v = new Vehicle(id, MaxSpeed, ContClass, l);
		map.addVehicle(v);
	}

}
