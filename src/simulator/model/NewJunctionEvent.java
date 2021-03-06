package simulator.model;


public class NewJunctionEvent extends Event {
	
	private String id;
	private LightSwitchingStrategy lsStrat;
	private DequeuingStrategy dqStrat;
	private int x;
	private int y;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.lsStrat = lsStrategy;
		this.dqStrat = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
		
	}

	@Override
	void execute(RoadMap map) {
		map.addJunction(new Junction(id, lsStrat, dqStrat, x, y));
	}

	public String toString() {
		return String.format("New Junction '%s'", id);
	}
}
