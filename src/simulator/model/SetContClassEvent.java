package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event{
	
	private List<Pair<String,Integer>> cs;

	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) throws IllegalArgumentException {
		super(time);
		if(cs == null)throw new IllegalArgumentException("[ERROR]: contClass can not be null." );
		this.cs = cs;
		}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Integer> i : cs) {
			Vehicle aux = map.getVehicle(i.getFirst());
			aux.setContClass(i.getSecond());
		}
	}
	
	public String toString() {
		String s = "Change CO2 class: [";
		for (Pair<String, Integer> pair : cs) {
			s += String.format("(%s, %d)", pair.getFirst(), pair.getSecond());
			if (cs.indexOf(pair) < cs.size()-1) s += ", ";
		}
		s += "]";
		return s;
	}
}

