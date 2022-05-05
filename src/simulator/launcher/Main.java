package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Integer _ticks = 10;
	private static String _mode = null;
	private static String _modeSelected = null;
	private static Factory<Event> _eventsFactory = null;
	private static Factory<LightSwitchingStrategy> _lsFactory = null;
	private static Factory<DequeuingStrategy> _dqFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			try {
				parseModeOption(line);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			parseTickOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(
				Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator's main loop(default value is 10)").build());
		cmdLineOptions.addOption(
				Option.builder("m").longOpt("mode").hasArg().desc("Mode of the simulation").build());

		return cmdLineOptions;
	}
	
	private static void parseModeOption(CommandLine line) throws Exception {
		// TODO Auto-generated method stub
		if(line.hasOption("m")) {
			_mode = line.getOptionValue("m");
			if(_mode.equalsIgnoreCase("GUI")) {
				_modeSelected = "GUI";
			}
			else if(_mode.equalsIgnoreCase("Console")) {
				_modeSelected = "Console";
			}
			else {
				throw new Exception("Incorrect mode");
			}
		}
		else {
			_modeSelected = "GUI";
		}
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseTickOption(CommandLine line) throws ParseException {
		// TODO Auto-generated method stub
		if(line.hasOption("t")) {
			_ticks = Integer.parseInt(line.getOptionValue("t"));
		}
	}
	
	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		/*if (_inFile == null) {
			throw new ParseException("An events file is missing");
		}*/
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void initFactories() {

		// TODO complete this method to initialize _eventsFactory
		
		//builders de las estrategia de semaforos
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		_lsFactory = new BuilderBasedFactory<>(lsbs);
		
		//builders de las estrategia de colas
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		_dqFactory = new BuilderBasedFactory<>(dqbs);
		
		//builders de los eventos
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add( new NewJunctionEventBuilder(_lsFactory,_dqFactory) );
		ebs.add( new NewCityRoadEventBuilder() );
		ebs.add( new NewInterCityRoadEventBuilder() );
		ebs.add( new NewVehicleEventBuilder() );
		ebs.add( new SetWeatherEventBuilder() );
		ebs.add( new SetContClassEventBuilder() );
		_eventsFactory = new BuilderBasedFactory<>(ebs);

	}
	
	private static void startGUIMode() throws Exception {
		// TODO complete this method to start the simulation
		
		TrafficSimulator sim = new TrafficSimulator();
		Controller controller = new Controller(sim, _eventsFactory);
		if(_inFile != null) {
			InputStream in = new FileInputStream(new File(_inFile));
			controller.loadEvents(in);
			in.close();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
				}
			});
	}

	private static void startBatchMode() throws Exception {
		// TODO complete this method to start the simulation
		
		OutputStream out;
		InputStream in = new FileInputStream(new File(_inFile));
		TrafficSimulator sim = new TrafficSimulator();
		Controller controller = new Controller(sim, _eventsFactory);
		
		if (_outFile == null){
			out = System.out;
		}
		else{
			out = new FileOutputStream(new File(_outFile));
		}
		
		controller.loadEvents(in);
		controller.run(_ticks, out);
		in.close();
		out.flush();
		out.close();
	}

	private static void start(String[] args) throws Exception {
		initFactories();
		parseArgs(args);
		if (_modeSelected == "GUI") startGUIMode();
		else startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
