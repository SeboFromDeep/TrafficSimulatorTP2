package simulator.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] colNames;
	List<Junction> junctions;

	public JunctionsTableModel(Controller c) {
		this.colNames = new String[] {"Id",
									  "Green",
									  "Queues"};
		this.junctions = c.getJunctions();
		c.addObserver(this);
	}
	@Override
	public int getRowCount() {
		return junctions != null ? junctions.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return junctions.get(rowIndex).getId();
		case 1:
			int greenIdx = junctions.get(rowIndex).getGreen();
			return formatGreenIndex(rowIndex, greenIdx);
		case 2:
			return formatQueueList(rowIndex, columnIndex);
		default:
			return null;
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.junctions = map.getJunctions();
		this.fireTableDataChanged();
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.junctions = new ArrayList<Junction>();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
	private Object formatGreenIndex(int rowIndex, int greenIdx) {
		return greenIdx == -1 ? "NONE" : junctions.get(rowIndex).getGreen();
	}
	
	private String formatQueueList(int rowIndex, int columnIndex) {
		//TODO
		String s = "";
		for (int i = 0; i < junctions.get(rowIndex).getIncomingRoads().size(); i++){
			s += junctions.get(rowIndex).getIncomingRoads().get(i).getId() + ":[";
			for (Vehicle v : junctions.get(rowIndex).getQueueList().get(i)) {
				s += v.getId();
				if (junctions.get(rowIndex).getQueueList().get(i).indexOf(v) < junctions.get(rowIndex).getQueueList().get(i).size()-1) s += ", ";
			}
			s += "] ";
		}
		return s;
	}

}
