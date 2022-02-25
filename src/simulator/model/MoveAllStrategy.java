package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeingStrategy{

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		return new ArrayList<Vehicle>(q);
	}

}
