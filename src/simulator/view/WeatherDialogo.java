package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class WeatherDialogo extends JDialog{

	private int _status;
	private JComboBox<String> _roads;
	private DefaultComboBoxModel<String> _roadsModel;
	private JComboBox<String> _weather;
	private DefaultComboBoxModel<String> _weatherModel;
	private JSpinner spinner;
	

	public WeatherDialogo(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change Road Weather");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Select your favorite");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_weatherModel = new DefaultComboBoxModel<>();
		
		_weatherModel.addElement("SUNNY");
		_weatherModel.addElement("CLOUDY");
		_weatherModel.addElement("RAINY");
		_weatherModel.addElement("WINDY");
		_weatherModel.addElement("STORM");
		
		_weather = new JComboBox<>(_weatherModel);
		JLabel weatherlabel = new JLabel("Weather: ");
		
		_roadsModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_roadsModel);
		JLabel roadslabel = new JLabel("Road: ");
		
		JLabel tickslabel = new JLabel("Ticks: ");
		spinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));

		viewsPanel.add(roadslabel);
		viewsPanel.add(_roads);
		viewsPanel.add(weatherlabel);
		viewsPanel.add(_weather);
		viewsPanel.add(tickslabel);
		viewsPanel.add(spinner);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				WeatherDialogo.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		setLocation(450, 250);
		pack();
		setResizable(false);
		setVisible(true);
	}
}
