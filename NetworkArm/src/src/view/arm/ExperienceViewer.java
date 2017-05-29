package src.view.arm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import src.model.arm.CompleteArm;
import src.model.arm.FreeArm;
import src.view.View;

@SuppressWarnings("serial")
public class ExperienceViewer extends JPanel {
	/** A CompleteArm */
	CompleteArm _compArm;
	
	public FreeArm free;

	/** Default Color */
	Color[] _defColors = { Color.blue, Color.red, Color.green, Color.cyan, Color.magenta, Color.pink, Color.black };

	/** Panel for displaying Arm */
	JArm2D _jArm;

	/**
	 * To show commands we try to do and command we really do.
	 */
	CommandViewer command_viewer;

	/**
	 * A graph to shows errors between positions of the neuron and of the data.
	 */
	GraphViewer errors_viewer;

	View parent;

	final int _nbMuscles = 6;
	final int _nbJoint = 2;

	/**
	 * Create the panel.
	 */
	public ExperienceViewer(CompleteArm arm, View p, int size) {

		super();
		_compArm = arm;
		parent = p;
		buildGUI(new Dimension(size, size), p);

	}
	
	public ExperienceViewer(FreeArm arm, View p, int size) {

		super();
		free = arm;
		parent = p;
		buildGUI(new Dimension(size, size), p);

	}

	private void buildGUI(Dimension dim, View p) {
		setLayout(new BorderLayout(0, 0));

		// LeftArm : JArm2D above with JArmLabel and JArmControl
		JPanel armPanel = new JPanel();
		armPanel.setLayout(new BorderLayout(0, 0));
		_jArm = new JArm2D(dim, _compArm.getArm(), p);
		armPanel.add(_jArm, BorderLayout.CENTER);
		JArmLabel jArmInfo = new JArmLabel(_compArm.getArm());
		armPanel.add(jArmInfo, BorderLayout.NORTH);
		_compArm.getArm().addObserver(_jArm);
		_compArm.getArm().addObserver(jArmInfo);

		command_viewer = new CommandViewer(new Dimension(800, 150));

		errors_viewer = new GraphViewer(parent, new Dimension(500, 500));
		p.getWorld().addObserver(errors_viewer);

		add(_jArm, BorderLayout.CENTER);
		add(command_viewer, BorderLayout.SOUTH);
		add(errors_viewer, BorderLayout.EAST);

		this.setVisible(true);
	}

	public CompleteArm getArm() {
		return _compArm;
	}

	public void reset() {
		_jArm.resetMemory();
	}

	public void update() {
		repaint();
		_jArm.update();
	}

	public CommandViewer getCommandViewer() {
		return command_viewer;
	}

	public JArm2D getArm2D() {
		return _jArm;
	}

	public GraphViewer getGraph() {
		return errors_viewer;
	}
}
