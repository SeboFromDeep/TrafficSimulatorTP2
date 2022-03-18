package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{

	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int x = data.getJSONArray("coor").getInt(0);
		int y = data.getJSONArray("coor").getInt(1);
		
		JSONObject lsStratJson = data.getJSONObject("ls_strategy");
		LightSwitchingStrategy lsStrat = lssFactory.createInstance(lsStratJson);
		
		JSONObject dqStratJson = data.getJSONObject("dq_strategy");
		DequeuingStrategy dqStrat = dqsFactory.createInstance(dqStratJson);
		
		return new NewJunctionEvent(time, id, lsStrat, dqStrat, x, y);
	}

}
