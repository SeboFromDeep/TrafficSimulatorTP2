package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	private Controller _ctrl;
	public MainWindow(Controller ctrl) {
	super("Traffic Simulator");
	_ctrl = ctrl;
	initGUI();
	}
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS)); 
		viewsPanel.add(mapsPanel);
		
		Border _defaultBorder = BorderFactory.createLineBorder(Color.black, 1);

		// tables
		JPanel eventsView =createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		eventsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Events", TitledBorder.LEFT,
				TitledBorder.TOP));
		tablesPanel.add(eventsView);
		
		JPanel Vehicles =createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		Vehicles.setPreferredSize(new Dimension(500, 200));
		Vehicles.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Vehicles", TitledBorder.LEFT,
				TitledBorder.TOP));
		tablesPanel.add(Vehicles);

		JPanel Roads =createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		Roads.setPreferredSize(new Dimension(500, 200));
		Roads.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Roads", TitledBorder.LEFT,
				TitledBorder.TOP));
		tablesPanel.add(Roads);
		
		JPanel Junctions =createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		Junctions.setPreferredSize(new Dimension(500, 200));
		Junctions.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Junctions", TitledBorder.LEFT,
				TitledBorder.TOP));
		tablesPanel.add(Junctions);

		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map", TitledBorder.LEFT,
				TitledBorder.TOP));
		mapsPanel.add(mapView);
		// TODO add a map for MapByRoadComponent
		// ...
		JPanel map = createViewPanel(new MapByRoadComponent(_ctrl), "Map road");
		map.setPreferredSize(new Dimension(500, 400));
		map.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map by road", TitledBorder.LEFT,
				TitledBorder.TOP));
		mapsPanel.add(map);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {int n = JOptionPane.showConfirmDialog(null,"Are you sure you want to go out?", "EXIT", JOptionPane.YES_NO_OPTION);
			if(n == 0) {
				System.exit(0);
			};}
			
			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}
		});
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	

	}
	private JPanel createViewPanel(JComponent c, String title) {
	JPanel p = new JPanel( new BorderLayout() );
	// TODO add a framed border to p with title
	p.add(new JScrollPane(c));
	return p;
	}

}