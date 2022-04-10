package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] colNames;
	List<Event> events;

	public EventsTableModel(Controller _ctrl) {
		this.colNames = new String[] {"Time", "Desc."};
		this.events = new ArrayList<Event>();
		_ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return events != null ? events.size() : 0;
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
		if (columnIndex == 0) {
			return ((Event) events.get(rowIndex)).getTime();
		} else {
			return ((Event) events.get(rowIndex)).toString();
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.events = events;
		this.fireTableDataChanged();
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		events.add(e);
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		events = new ArrayList<Event>();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public void removeEvent(Event e) {
		events.remove(e);
	}

}
