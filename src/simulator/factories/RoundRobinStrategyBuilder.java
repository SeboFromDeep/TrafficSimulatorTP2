package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{
	
	private int timeSlot;

	RoundRobinStrategyBuilder() {
		super("round_robin_lss");
		this.timeSlot = 1;
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		if (data != null && data.has("timeslot")) this.timeSlot = data.getInt("timeslot");
		return new RoundRobinStrategy(timeSlot);
	}

}
