package view.network;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.LinkedEnvironment;
import view.View;

@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel {

	/**
	 * Buttons to control the network.
	 */
	private JButton btn_options, btn_arm_options, btn_launch;

	/**
	 * The environment where the network evolves.
	 */
	private LinkedEnvironment world;

	/**
	 * The frame that contains the panel.
	 */
	private View holder;

	/**
	 * A boolean to keep the previous state of the world (occurring -> true,
	 * else -> false)
	 */
	private boolean previous_state;

	/**
	 * Creates a panel with control buttons for the network.
	 * 
	 * @param view
	 *            The main frame of the GUI.
	 * @param w
	 *            The world where it takes place.
	 */
	public ButtonsPanel(View view, LinkedEnvironment w) {

		holder = view;
		world = w;

		// We create buttons
		btn_options = new JButton("Options");
		btn_launch = new JButton("Launch");
		btn_arm_options = new JButton("Arm options");

		// We add them listeners
		addListeners();

		// We add them to the object
		setLayout(new GridLayout(1, getClass().getDeclaredFields().length));

		add(btn_launch);
		add(btn_options);
		add(btn_arm_options);
	}

	/**
	 * Change the state of the enability of buttons.
	 * 
	 * @param current_state
	 */
	public void change(boolean current_state) {

		// If there is a process, we can't play another.
		btn_launch.setEnabled(!current_state);

		previous_state = !previous_state;
	}

	/**
	 * Add the listeners to the buttons of the panel.
	 */
	public void addListeners() {

		btn_options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				holder.launchOptions();
			}
		});

		btn_launch.addActionListener(new ActionListener() {
			
			// reseau de neurone 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//holder.getDraw().getArm2D().show_neurons = true;
				//holder.freedraw._jArm.show_neurons = true;
				start();
			}
		});

		btn_arm_options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				holder.launchArmOptions();
			}
		});
	}

	/**
	 * Prepare the world for the advent of the network and launch it.
	 */
	public void start() {

		/*
		 * We create a new network based on the previous network because if we
		 * want to launch the same network without clicking on it in the
		 * list_network, there is a Thread error
		 */
		world.reset();

		// holder.getDraw().getCommandViewer().clear();
		// holder.getDraw().getGraph().clear();
		
		holder.freedraw.getCommandViewer().clear();
		holder.freedraw.getGraph().clear();

		Thread th = new Thread(world);
		th.start();

	}
}
