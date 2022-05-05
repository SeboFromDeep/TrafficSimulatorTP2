package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.ThreadSimulatorRunner;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	private Controller ctrl;
	private JButton playButton;
	private JButton stopButton;
	private JButton resetButton;
	private JButton CO2Button;
	private JButton weatherButton;
	private JButton loadButton;
	private JButton offButton;
	private boolean _stopped;
	private CO2Dialogo co2dialog;
	private WeatherDialogo wdialog;
	private JSpinner spinner;
	private JComboBox<String> combo;
	private DefaultComboBoxModel<String> comboModel;
	private File file;
	
	private volatile Thread _thread;

	public ControlPanel(Controller _ctrl) {
		// TODO Auto-generated constructor stub
		this.ctrl = _ctrl;
		initGUI();
		ctrl.addObserver(this);
		this.file = null;
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
		
		resetButton = new JButton(new ImageIcon("resources/icons/reset.png"));
		resetButton.setToolTipText("Reset the simulation");
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
				
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
		
		JLabel simulationMode = new JLabel("Simulation mode: ");
		comboModel = new DefaultComboBoxModel<>();
		comboModel.addElement("Tick Simulation");
		comboModel.addElement("Complete Simulation with Threads");
		combo = new JComboBox<>(comboModel);
	
		
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
		barra.add(resetButton);
		barra.add(stopButton);
		barra.add(ticksLabel);
		barra.add(spinner);
		barra.addSeparator();
		barra.add(simulationMode);
		barra.add(combo);
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
	
	public void enableButtonsAfterThread() {
		enableButtons(true);
	}
	
	private void load() throws JSONException, Exception {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./resources/examples"));
		
		FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter(".JSON", "json");
		fileChooser.setFileFilter(jsonFilter);
		
		fileChooser.showOpenDialog(fileChooser);
		file = fileChooser.getSelectedFile();
		if(file != null) {
			InputStream in = new FileInputStream(file);
			
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

		int status = co2dialog.open(this.ctrl.getVehicles());

		if (status == 1) {
			try {
				List<Pair<String, Integer>> cs = new ArrayList<Pair<String, Integer>>();
				cs.add(co2dialog.getSettedContClass());
				
				int time = this.ctrl.getTime() + co2dialog.getTime();
				
				SetContClassEvent e = new SetContClassEvent(time, cs);
				this.ctrl.addEvent(e);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Error al cambiar el contclass", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		co2dialog = null;
	}
	private void changeWeather() {
		
		wdialog = new WeatherDialogo((Frame)SwingUtilities.getWindowAncestor(this));

		int status = wdialog.open(this.ctrl.getRoads());

		if (status == 1) {
			try {
				List<Pair<String, Weather>> ws = new ArrayList<Pair<String,Weather>>();
				ws.add(wdialog.getSettedWeather());
				
				int time = this.ctrl.getTime() + wdialog.getTime();
				
				SetWeatherEvent e = new SetWeatherEvent(time, ws);
				this.ctrl.addEvent(e);
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
		switch ((String) this.comboModel.getSelectedItem()) {
		case "Tick Simulation":
			int ticks = (int) this.spinner.getValue();
			System.out.println(String.format("Running %d ticks", ticks));
			enableButtons(false);
			_stopped = false;
			run_sim(ticks);
			break;
		case "Complete Simulation with Threads":
			_thread = new ThreadSimulatorRunner(ctrl.getTrafficSimulator(), this);
			System.out.println("Completing the simulation...");
			enableButtons(false);
			stopButton.setEnabled(false);
			_thread.start();
			try {
				_thread.join();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}	
			System.out.println("Simulation completed.");
			break;
		default:
			break;
		}
	}
	
	private void stop() {
		_stopped = true;
		enableButtons(true);
		System.out.println("Simulation stopped");
	}
	
	private void reset() {
		if (file != null) {
			ctrl.reset();
			try {
				InputStream in = new FileInputStream(file);
				
				ctrl.reset();
				ctrl.loadEvents(in);
				in.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage()); 
			}
			System.out.println("Simulation reseted.");
		}
	}
}