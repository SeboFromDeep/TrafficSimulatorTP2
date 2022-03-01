package simulator.model;

import exception.InvalidArgumentsException;
import exception.JunctionAlreadyPresentException;

public class NewJunctionEvent extends Event {
	
	private String id;
	private LightSwitchingStrategy lsStrat;
	private DequeingStrategy dqStrat;
	private int x;
	private int y;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.lsStrat = lsStrategy;
		this.dqStrat = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
		
	}

	@Override
	void execute(RoadMap map) {
		try {
			map.addJunction(new Junction(id, lsStrat, dqStrat, x, y));
		} catch (JunctionAlreadyPresentException | InvalidArgumentsException e) {
			e.getMessage();
		}

	}

}
