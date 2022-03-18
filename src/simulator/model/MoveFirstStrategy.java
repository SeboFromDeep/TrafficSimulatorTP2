package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy{

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> first = new ArrayList<Vehicle>();
		if (q.isEmpty()) return first;
		first.add(q.get(0));
		return first;
	}

}
