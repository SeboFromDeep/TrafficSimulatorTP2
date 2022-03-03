package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event{
	
	private List<Pair<String,Integer>> cs;

	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) throws Exception {
		super(time);
		if(cs == null)throw new Exception();
		this.cs = cs;
		}

	@Override
	void execute(RoadMap map) throws Exception {
		// TODO Auto-generated method stub
		for(Pair<String, Integer> i : cs) {
			Vehicle aux = map.getVehicle(i.getFirst());
			aux.setContClass(i.getSecond());
		}
	}
}

