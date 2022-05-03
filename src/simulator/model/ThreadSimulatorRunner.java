package simulator.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ThreadSimulatorRunner extends Thread {
	private TrafficSimulator trafiSimulator;
	private List<Boolean> arrived; //lista de booleanos que contiene si un vehiculo ha terminado su recorrido
	
	public ThreadSimulatorRunner(TrafficSimulator t) {
		this.trafiSimulator = t;
		this.arrived = new LinkedList<>(); 
		setArrivedList();
	}
	
	public void run() {
		while (!allArrived()) {
			trafiSimulator.advance();
			updateArrivedList();
		}
	}
	
	private void setArrivedList() {
		for (Vehicle v : trafiSimulator.getVehicles()) {
			Boolean b;
			
			if (v.getStatus() != VehicleStatus.ARRIVED) b = false;
			else b = true;
			
			arrived.add(b);
		}
	}
	
	private void updateArrivedList() {
		for (int i = 0; i < arrived.size(); i++) {
			if (!arrived.get(i) && trafiSimulator.getVehicles().get(i).getStatus() == VehicleStatus.ARRIVED) arrived.set(i, true);
		}
	}
	
	public boolean allArrived() {
		for (Boolean b : arrived) {
			if (!b) return false;
		}
		return true;
	}

}
