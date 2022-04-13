package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	private Image _car;
	private Color _BG_COLOR = Color.WHITE; 
	
	private RoadMap _map;
	
	public MapByRoadComponent(Controller c) {
		setPreferredSize(new Dimension(300, 200));
		initGUI();
		c.addObserver(this);
	}
	private void initGUI() {
		_car = loadImage("car.png");		
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics2D g) {
		drawRoads(g);
	}
	private void drawVehicles(Graphics g, Road r, int y) {
		for (Vehicle v: r.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				int x1 = 50, x2 = getWidth() - 100;
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));
				g.drawImage(_car, x, y - 8, 16, 16, this);
			}
			
		}
		
	}
	private void drawRoads(Graphics2D g) {
		for (int i = 0; i < _map.getRoads().size(); i++) {
			int x1 = 50, x2 = getWidth() - 100, y = (i + 1) * 50;
			Road r = _map.getRoads().get(i);
			
			g.setColor(Color.BLACK);
			
			g.drawLine(x1, y, x2, y);
			g.drawString(r.getId(), x1 - 30, y + 4);
			
			g.setColor(new Color(200, 100, 0));
			g.drawString(r.getSrc().getId(), x1 - 4, y - 6);
			
			g.drawString(r.getDest().getId(), x2 - 4, y - 6);
			
			g.setColor(Color.BLUE);
			g.fillOval(x1 - 5, y- 5, 10, 10);
			
			if (r.getDest().getGreen() == i) g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
			g.fillOval(x2 - 5, y - 5, 10, 10);
			
			drawVehicles(g, r, y);
		}
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}
	}
	
	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			_map = map;
			repaint();
		});
	}
}
