package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import extra.jtable.EventEx;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{

	
	private List<Event> _events;
	private String[] _colNames = {"Time", "Desc" };
	private Controller ctl;

	public EventsTableModel(Controller ctl) {
		_events=null;
		this.ctl = ctl;
		ctl.addObserver(this);
	}

	public void update() {
		fireTableDataChanged();;		
	}
	
	public void setEventsList(List<Event> events) {
		_events = events;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	//si no pongo esto no coge el nombre de las columnas
	//
	//this is for the column header
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	public int getColumnCount() {
		return _colNames.length;
	}

	public int getRowCount() {
		return _events == null ? 0 : _events.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _events.get(rowIndex).getTime();
			break;
		case 1:
			s = _events.get(rowIndex).toString();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		setEventsList(events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		setEventsList(events);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		setEventsList(events);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		setEventsList(events);
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
