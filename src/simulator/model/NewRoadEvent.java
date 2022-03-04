package simulator.model;

public abstract class NewRoadEvent extends Event{
	
	protected int length;
	protected String dest;   //cruce de destino
	protected String src;    //cruce de incio
	protected Weather weather;
	protected int contLimit;
	protected int maxSpeed;
	protected String id;

	NewRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.src = srcJun;
		this.dest = destJunc;
		this.weather = weather;
		this.id = id;
		this.contLimit = co2Limit;
		this.length = length;
		this.maxSpeed = maxSpeed;
	}

	@Override
	abstract void execute(RoadMap map);

}
