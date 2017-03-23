package view;

import java.util.*;
import javax.swing.*;

import model.*;
import model.options.*;
import view.arm.*;
import view.network.*;
import control.*;

@SuppressWarnings("serial")
public class FreeView extends JFrame implements Observer {

	private JRadioList list_networks;
	private ButtonsPanel buttons;
	private ExperienceViewer draw;
	public ExperienceViewerFree freedraw;
	private OptionsFrame options;
	private ArmOptionsFrame arm_options;
	private LinkedEnvironment world;

	public FreeView(LinkedEnvironment e) {

		// the frame for the options
		//options = new OptionsFrame(this);
		arm_options = new ArmOptionsFrame();
		world = e;

		BasicOptions.stopped.addObserver(options);
		world.addObserver(this);

		// We create all necessary objects
		//draw = new ExperienceViewer(world.getArm(), this, world.getSize());

		freedraw.free.addObserver(freedraw.getCommandViewer());
		world.getNet().addObserver(freedraw.getArm2D());
		world.addObserver(freedraw.getGraph());

		ArrayList<String> names_network = new ArrayList<String>();
		names_network.add("Self Organizing Map");
		names_network.add("Dynamic Self Organizing Map");
		names_network.add("Growing Neural Gas");

		list_networks = new JRadioList(names_network);

		//buttons = new ButtonsPanel(this, world);

		// We add listeners
		list_networks.addActionListenerOnAll(new NetworkController(world));

		// We add them to the frame
		JMenuBar menu_bar = new JMenuBar();
		JMenu menu_net = new JMenu("Networks");
		menu_net.add(list_networks);

		menu_bar.add(menu_net);
		menu_bar.add(buttons);

		setJMenuBar(menu_bar);
		add(freedraw);

		// We initialize data and network
		list_networks.setSelected(0);

		// We show the frame
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Intelligent Arm");
		setVisible(true);
	}

	public void launchOptions() {
		options.setVisible(true);
		options.switcher(world.getNet().toString());
	}

	public void launchArmOptions() {
		arm_options.setVisible(true);
	}

	public ExperienceViewerFree getDraw() {
		return freedraw;
	}

	public LinkedEnvironment getWorld() {
		return world;
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		freedraw.repaint();

		// We verify if there is a process is occurring
		if (BasicOptions.stopped.bool()) {
			buttons.change(false);
			options.setDisabled(false);
			getJMenuBar().getMenu(0).setEnabled(true);

			// We don't switch options when a process occurs, because it can't
			// change and it switches too much times
			if (options.isVisible())
				options.switcher(world.getNet().toString());
		} else {
			buttons.change(true);
			options.setDisabled(true);

			getJMenuBar().getMenu(0).setEnabled(false);
		}
	}
}
