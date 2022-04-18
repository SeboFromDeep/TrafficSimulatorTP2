package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
	
	private Controller ctrl;
	private JLabel numTime;
	private JLabel lastEvent;;

	public StatusBar(Controller ctrl) {
		super();
		this.ctrl = ctrl;
		this.ctrl.addObserver(this);
		initGUI();
		}

	private void initGUI() {
		// TODO Auto-generated method stub
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel time = new JLabel("Time: ");
		this.add(time);
		
		numTime = new JLabel("0");
		this.add(numTime);
		this.add(new JSeparator());
		
		lastEvent = new JLabel("");
		this.add(lastEvent, BorderLayout.WEST);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		numTime.setText(Integer.toString(time));
		lastEvent.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		lastEvent.setText("Event added (" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		lastEvent.setForeground(Color.RED);
		lastEvent.setText(err.toString());
		lastEvent.setForeground(Color.BLACK);
	}

}
