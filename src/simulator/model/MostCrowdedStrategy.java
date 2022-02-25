package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{

	private int timeSlot;
	
	MostCrowdedStrategy(int timeSlot){
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.isEmpty()) return -1;
		else if (currGreen == -1) return mostCrowdedSinceBeginning(qs);
		else if (currTime - lastSwitchingTime < timeSlot) return currGreen;
		return mostCrowdedSinceCurrGreen(qs, currGreen);
	}
	
	//iterates through qs from the beginning to the end
	public int mostCrowdedSinceBeginning(List<List<Vehicle>> qs) {
		int mostCrowdedIdx = 0;
		int mostCrowdedSize = 0;
		
		for (int i = 0; i < qs.size(); i++) {
			if (qs.get(i).size() > mostCrowdedSize) {
				mostCrowdedIdx = i;
				mostCrowdedSize = qs.get(i).size();
			}
		}
		return mostCrowdedIdx;
	}
	//iterates through qs in a circular way from currGreen+1 to currGreen 
	public int mostCrowdedSinceCurrGreen(List<List<Vehicle>> qs, int currGreen) {
		int mostCrowdedIdx = 0;
		int mostCrowdedSize = 0;
		int idx = currGreen + 1;
		for (int i = 0; i < qs.size(); i++) {
			if (qs.get(idx).size() > mostCrowdedSize) {
				mostCrowdedIdx = idx;
				mostCrowdedSize = qs.get(idx).size();
			}
			idx = (idx + 1) % qs.size();
		}
		
		return 0;
	}
}
