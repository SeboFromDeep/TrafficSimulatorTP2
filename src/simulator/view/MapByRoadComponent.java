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
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	private Image _car;
	private Color _BG_COLOR = Color.WHITE; 
	
	private RoadMap _map;
	
	public MapByRoadComponent(Controller c) {
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
	
	private void drawVehicles(Graphics g, List<Vehicle> vehicles, int x1, int x2, int y) {
		for (Vehicle v: vehicles) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) v.getRoad().getLength()));
				g.drawImage(_car, x, y, 16, 16, this);
				g.setColor(Color.GREEN);
				g.drawString(v.getId(), x + 2, y);
			}
		}
		
	}
	
	private void drawWeatherImages(Graphics g, Weather w, int x, int y) {
		String weatherImg = null;
		switch (w) {
		case CLOUDY:
			weatherImg = "cloud.png";
			break;
		case RAINY:
			weatherImg = "rain.png";
			break;
		case STORM:
			weatherImg = "storm.png";
			break;
		case SUNNY:
			weatherImg = "sun.png";
			break;
		case WINDY:
			weatherImg = "wind.png";
			break;
		default:
			break;
		}
		g.drawImage(loadImage(weatherImg), x, y, 32, 32, this);
	}
	
	private void drawContaminationImages(Graphics g, int cont, int contLimit, int x, int y) {
		int contLevel = (int) Math.floor(Math.min((double) cont/(1.0 + (double) contLimit),1.0) / 0.19);
		
		String contLevelImg = String.format("cont_%d.png", contLevel);
		g.drawImage(loadImage(contLevelImg), x, y, 32, 32, this);
	}
	
	private void drawMap(Graphics2D g) {
		for (int i = 0; i < _map.getRoads().size(); i++) {
			int x1 = 50, x2 = getWidth() - 100, y = (i + 1) * 50;
			Road r = _map.getRoads().get(i);
			
			// Draw the road
			g.setColor(Color.BLACK);
			g.drawLine(x1, y, x2, y);
			g.drawString(r.getId(), x1 - 30, y + 4);
			
			// Draw the Junctions of the road
			g.setColor(new Color(200, 100, 0));
			g.drawString(r.getSrc().getId(), x1 - 4, y - 6);
			g.drawString(r.getDest().getId(), x2 - 4, y - 6);
			g.setColor(Color.BLUE);
			g.fillOval(x1 - 5, y- 5, 10, 10);
			if (r.getDest().getGreen() == i) g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
			g.fillOval(x2 - 5, y - 5, 10, 10);
			
			// Draw the vehicles of the road
			drawVehicles(g, r.getVehicles(), x1, x2, y - 8);
			// Draw the weather of the road
			drawWeatherImages(g, r.getWeather(), x2 + 10, y - 17);
			// Draw the contamination of the road
			drawContaminationImages(g, r.getTotalCO2(), r.getContLimit(), x2 + 47, y - 17);
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
		setPreferredSize(new Dimension(1, 1));
		setSize(new Dimension(487, 374));	// estos datos los saqué debuggeando y mirando los datos anteriores al reset
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
		int maxH = 200;
		int maxW = 200;
		if (_map.getRoads().size() > 7) {
			maxH = Math.max(maxH, maxH + (_map.getRoads().size()) - 7)*3;
		}
		maxH += 20;
		if (maxH > getHeight()) {
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
