package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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


import simulator.misc.Pair;
import simulator.model.Vehicle;

public class CO2Dialogo extends JDialog{

	private int _status;
	private JComboBox<String> _vehicles;
	private DefaultComboBoxModel<String> _vehiclesModel;
	private JComboBox<Integer> _CO2;
	private DefaultComboBoxModel<Integer> _CO2Model;
	private JSpinner spinner;
	
	private String vehicleId;
	private int contClass;
	private int time;
	

	public CO2Dialogo(Frame parent) {
		super(parent, true);
		initGUI();
		this._status = 1;
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change CO2 Class");
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

		_CO2Model = new DefaultComboBoxModel<>();
		for (int i = 0; i <= 10; i++) {
			_CO2Model.addElement(i);
		}
		_CO2 = new JComboBox<>(_CO2Model);
		JLabel co2label = new JLabel("CO2 Class: ");
		
		_vehiclesModel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclesModel);
		JLabel vehicleslabel = new JLabel("Vehicle: ");
		
		JLabel tickslabel = new JLabel("Ticks: ");
		spinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));

		viewsPanel.add(vehicleslabel);
		viewsPanel.add(_vehicles);
		viewsPanel.add(co2label);
		viewsPanel.add(_CO2);
		viewsPanel.add(tickslabel);
		viewsPanel.add(spinner);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				CO2Dialogo.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				vehicleId = (String) _vehiclesModel.getSelectedItem();
				contClass = (int) _CO2Model.getSelectedItem();
				time = (int) spinner.getValue();
				setVisible(false);
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		setLocation(450, 250);
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(List<Vehicle> vl) {
		this._vehiclesModel.removeAllElements();
		for(Vehicle v:vl) {
			this._vehiclesModel.addElement(v.getId());
		}
		setVisible(true);
		return _status;
	}
	
	public Pair<String, Integer> getSettedContClass() {
		return new Pair<String, Integer>(vehicleId, contClass);
	}
	
	public int getTime(){
		return this.time;
	}
	

}
