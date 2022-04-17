package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	private Controller ctrl;
	private JButton playButton;
	private JButton stopButton;
	private JButton CO2Button;
	private JButton weatherButton;
	private JButton loadButton;
	private JButton offButton;
	private boolean _stopped;
	private CO2Dialogo co2dialog;
	private WeatherDialogo wdialog;
	private JSpinner spinner;

	public ControlPanel(Controller _ctrl) {
		// TODO Auto-generated constructor stub
		this.ctrl = _ctrl;
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new BorderLayout());

		
		JToolBar barra = new JToolBar();
		barra.setLayout(new BoxLayout(barra, BoxLayout.LINE_AXIS));
		
		loadButton = new JButton(new ImageIcon("resources/icons/open.png"));
		loadButton.setToolTipText("Load a file event");
		loadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					load();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			}
		});
		
		CO2Button = new JButton(new ImageIcon("resources/icons/co2class.png"));
		CO2Button.setToolTipText("Change the co2");
		CO2Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				changeCO2();
			}
		});
		
		weatherButton = new JButton(new ImageIcon("resources/icons/weather.png"));
		weatherButton.setToolTipText("Change the weather");
		weatherButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				changeWeather();
			}
		});
		
		playButton = new JButton(new ImageIcon("resources/icons/run.png"));
		playButton.setToolTipText("Run the simulation");
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});
		
		stopButton = new JButton(new ImageIcon("resources/icons/stop.png"));
		stopButton.setToolTipText("Pause the simulation");
		stopButton.setEnabled(false);
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		
		JLabel ticksLabel = new JLabel("Ticks: ");
		spinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		
		
		offButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		offButton.setToolTipText("Close the simulation");
		offButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		barra.add(loadButton);
		barra.addSeparator();
		barra.add(CO2Button);
		barra.add(weatherButton);
		barra.addSeparator();
		barra.add(playButton);
		barra.add(stopButton);
		barra.add(ticksLabel);
		barra.add(spinner);
		barra.add(Box.createHorizontalGlue());
		barra.addSeparator();
		barra.add(offButton);
		this.add(barra);
	}

	// INTERFACE METHODS
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
	
	// 	ADITIONAL METHODS
	private void enableButtons(boolean b) {
		stopButton.setEnabled(!b);
		playButton.setEnabled(b);
		loadButton.setEnabled(b);
		CO2Button.setEnabled(b);
		weatherButton.setEnabled(b);
		offButton.setEnabled(b);
	}
	
	private void load() throws JSONException, Exception {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(fileChooser);
		File f = fileChooser.getSelectedFile();
		if(f != null) {
			InputStream in = new FileInputStream(f);
			
			ctrl.reset();
			ctrl.loadEvents(in);
			in.close();
		}
		
	}
	
	private void close() {
		int n = JOptionPane.showConfirmDialog(null,"Are you sure you want to go out?", "EXIT", JOptionPane.YES_NO_OPTION);
		if(n == 0) {
			System.exit(0);
		}
	}
	
	private void changeCO2() {
		co2dialog = new CO2Dialogo((Frame)SwingUtilities.getWindowAncestor(this));

		int status = co2dialog.open(this.ctrl.getVehicleList());

		if (status == 1) {
			try {
				this.ctrl.change(co2dialog.getCO2());
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Error al cambiar el contclass", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		co2dialog = null;
	}
	private void changeWeather() {
		
		wdialog = new WeatherDialogo((Frame)SwingUtilities.getWindowAncestor(this));

		int status = wdialog.open(this.ctrl.getRoadsList());

		if (status == 1) {
			try {
				this.ctrl.change(wdialog.getWeather());
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Error al cambiar el tiempo", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		wdialog = null;
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				ctrl.run(1);
			} catch (Exception e) {
			// TODO show error message
				_stopped = true;
				enableButtons(true);
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableButtons(true);
			_stopped = true;
		}
	}
	
	public void play() {
		int ticks = (int) this.spinner.getValue();
		System.out.println(String.format("Running %d ticks", ticks));
		enableButtons(false);
		_stopped = false;
		run_sim(ticks);
	}
	
	private void stop() {
		_stopped = true;
		enableButtons(true);
		System.out.println("Simulation stopped");
	}

}