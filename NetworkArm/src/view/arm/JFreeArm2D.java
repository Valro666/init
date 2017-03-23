package view.arm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Jama.Matrix;
import info.monitorenter.util.collections.RingBufferArrayFast;
import model.LinkedEnvironment;
import model.arm.FreeArm;
import model.network.AbstractNetwork;
import model.network.Neuron;
import view.View;

/**
 * Dessine le bras comme une suite de segments bleus. Les axes sont aussi
 * dessiné entre -1 et 1.
 * 
 * @author alain.dutech@loria.fr
 */
@SuppressWarnings("serial")
public class JFreeArm2D extends JPanel implements Observer {

	public volatile boolean show_neurons;

	/** Model : HumanArm */
	FreeArm arm;

	/** Parent view */
	View parent;

	/** Position of the goal */
	double _goalX = 0.0, _goalY = 0.0;
	/** Radius of Goal for displaying */
	double _goalRadius = 0.01;
	/** Display goals */
	boolean _fg_goal = true;

	public JFreeArm2D(Dimension dim, FreeArm free, View p) {
		super();

		this.parent = p;

		setPreferredSize(dim);

		arm = free;

		show_neurons = false;
	}

	public JFreeArm2D(FreeArm model, double minX, double maxX, double minY, double maxY) {
		super();
		arm = model;
	}

	public void update() {
		arm.change();
	}

	public void update(Observable model, Object o) {

		if (o != null && o instanceof Matrix) {
			Matrix goal = (Matrix) o;
			_goalX = goal.get(0, 0);
			_goalY = goal.get(0, 1);
		}

		this.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.fillRect(0, 0, 1000, 1000);
		g.setColor(Color.orange);
		// Essayer de tracer une croix -1,1; -1,1
		g.drawLine(281, 0, 281, 281 * 3);
		g.drawLine(0, 281, 281 * 3, 281);
		// Zone accessible
		// drawReachingArea(g);

		// if (show_neurons)
		drawNeurons(g);

		// Bras
		drawArm(g);

		// But
		// if (_fg_goal)
		drawGoal(g);
	}

	public void drawNeurons(Graphics g) {
		Neuron neur;

		
		 
		
		AbstractNetwork net = parent.getWorld().getNet();

		if (net != null && net.size() != 0) {
			for (int i = 0; i < net.getNeurons().size(); i++) {
				for (int k = 0; k < net.getNeurons().get(i).size(); k++) {

					g.setColor(Color.red);
					neur = net.getNeurons().get(i).get(k);

					// System.out.print("("+neur.getWeights().get(0)*echelle+";"+neur.getWeights().get(1)*echelle+")"+"|");

					g.fillOval((int) (neur.getWeights().get(0) * this.getSize().getWidth()),
							(int) (neur.getWeights().get(1) * this.getSize().getHeight()), 5, 5);

					g.setColor(Color.BLUE);

					for (int j = 0; j < neur.getNeighbors().size(); j++) {

						g.drawLine((int) (neur.getWeights().get(0) * this.getSize().getWidth()),
								(int) (neur.getWeights().get(1) * this.getSize().getHeight()),
								(int) (neur.getNeighbors().get(j).getWeights().get(0) * this.getSize().getWidth()),
								(int) (neur.getNeighbors().get(j).getWeights().get(1) * this.getSize().getHeight()));
					}
				}
			}
		}

		revalidate();
	}

	/**
	 * Draw the reaching area.
	 */
	private void drawReachingArea(Graphics g) {
		// g.setColor(Color.LIGHT_GRAY);
		//
		// final double l0 = _arm.getLength()[0];
		// final double l1 = _arm.getLength()[1];
		// final double l0_2 = l0 * l0;
		// final double l1_2 = l1 * l1;
		//
		// final ArmConstraints ac = _arm.getConstraints();
		//
		// double centre_x = 0;
		// double centre_y = 0;
		// double angle = 0;
		// double radius = 0;
		// double startAngle = 0;
		// double arcAngle = 0;
		//
		// double l2_2 = l0_2 + l1_2 - 2 * l0 * l1 * Math.cos(Math.PI -
		// ac._minq[1]);
		// radius = Math.sqrt(l2_2);
		// startAngle = ac._minq[0];
		// arcAngle = ac._maxq[0] - ac._minq[0];
		// this.drawArc(g, 0, 0, radius, startAngle, arcAngle);
		//
		// angle = _arm.getConstraints()._minq[0];
		// centre_x = l0 * Math.cos(angle);
		// centre_y = l0 * Math.sin(angle);
		// radius = l1;
		// startAngle = ac._minq[0];
		// arcAngle = ac._maxq[1] - ac._minq[1];
		// this.drawArc(g, centre_x, centre_y, radius, startAngle, arcAngle);
		//
		// angle = _arm.getConstraints()._maxq[0];
		// centre_x = l0 * Math.cos(angle);
		// centre_y = l0 * Math.sin(angle);
		// radius = l1;
		// startAngle = ac._maxq[0];
		// arcAngle = ac._maxq[1] - ac._minq[1];
		// this.drawArc(g, centre_x, centre_y, radius, startAngle, arcAngle);
		//
		// l2_2 = l0_2 + l1_2 - 2 * l0 * l1 * Math.cos(Math.PI - ac._maxq[1]);
		// radius = Math.sqrt(l2_2);
		// startAngle = Math.acos((l1_2 - l2_2 - l0_2) / (-2 * radius * l1)) +
		// ac._minq[0];
		// arcAngle = ac._maxq[0] - ac._minq[0];
		// this.drawArc(g, 0, 0, radius, startAngle, arcAngle);
	}

	/**
	 * Draw an arc the the center of the circle q With starAngle and arcAngle in
	 * radian
	 */
	private void drawArc(Graphics g, double centre_x, double centre_y, double radius, double startAngle,
			double arcAngle) {
		// int x = xWin(centre_x - radius);
		// int y = yWin(centre_y + radius);
		// int width = xWin(centre_x + radius) - x;
		// int height = yWin(centre_y - radius) - y;
		// g.drawArc(x, y, width, height, (int) Math.toDegrees(startAngle),
		// (int) Math.toDegrees(arcAngle));
	}

	// /** Draw a circle centered on the goal */
	private void drawGoal(Graphics g) {
		// if (_arm.isPointReachable(_goalX, _goalY))

		if (LinkedEnvironment.goal != null) {

			FreeArm tmp = LinkedEnvironment.goal;
			g.setColor(Color.GREEN);
			g.fillOval(tmp.endX() + 281 - 5, tmp.endY() - 5 + 281, 10, 10);
		}

		// else
		// g.setColor(Color.RED);

		// drawArc(g, 150, 150, 5, 0, 360);
	}

	/** Draw Arm as a sequence of lines */

	public static int pol = 0;

	private void drawArm(Graphics g) {
		// Sleep(10);
		// try {
		// // Thread.sleep(100);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// pol++;
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(255, 0, 255));
		g2.drawLine(281, 281, arm.midX() + 281, arm.midY() + 281);
		g.fillOval(arm.midX() + 281 - 3, arm.midY() + 281 - 3, 6, 6);
		g2.setColor(Color.CYAN);
		g2.drawLine(arm.midX() + 281, arm.midY() + 281, arm.endX() + 281, arm.endY() + 281);
		g.fillOval(arm.endX() + 281 - 5, arm.endY() + 281 - 5, 10, 10);

		// g2.setColor(Color.ORANGE);
		// g.drawOval(281 - ((int) (arm.arm2 + arm.arm1) / 2), 281 - ((int)
		// (arm.arm2 + arm.arm1) / 2),
		// (int) (arm.arm2 + arm.arm1), (int) (arm.arm2 + arm.arm1));
		// System.out.println(pol);

		// System.out.println(_arm.epaule);
		// g2.drawLine(xWin(0), yWin(0), xWin(_arm.midX()), yWin(_arm.midY()));

		// System.out.println(_arm.midX() + " " + _arm.midY() + " " +
		// _arm.epaule);

		// _arm.epaule++;
		// _arm.coude--;

		// double[] posX = _arm.getArmX();
		// double[] posY = _arm.getArmY();
		//
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g2.setStroke(new BasicStroke(3));
		// g2.setColor(new Color(0, 204, 0));
		//
		// for (int i = 1; i < posX.length; i++) {
		// g.drawLine(xWin(posX[i - 1]), yWin(posY[i - 1]), xWin(posX[i]),
		// yWin(posY[i]));
		// }
		//
		// // Add end point to memory
		// addEndPoint(posX[posX.length - 1], posY[posY.length - 1]);
		//
		// // draw trajectory
		// drawMemory(g2);
	}

	private void drawMemory(Graphics2D g2) {
		// if (_endX.size() > 1) {
		// g2.setStroke(new BasicStroke(1));
		// g2.setColor(Color.magenta);
		// double srcX = _endX.getOldest();
		// double srcY = _endY.getOldest();
		// Iterator<Double> iY = _endY.iterator();
		// for (Iterator<Double> iX = _endX.iterator(); iX.hasNext();) {
		// Double x = (double) iX.next();
		// Double y = (double) iY.next();
		// g2.drawLine(xWin(srcX), yWin(srcY), xWin(x), yWin(y));
		// srcX = x;
		// srcY = y;
		// }
		// }
	}

	/**
	 * Compute the x_window_point from the x_model_point
	 * 
	 * @return window x
	 */
	// private int xWin(double x) {
	// double size = Math.min(_size.width, _size.height);
	// return (int) ((x - _minX) / (_maxX - _minX) * size);
	// }
	//
	// /**
	// * Compute the y_window_point from the y_model_point
	// *
	// * @return window y
	// */
	// private int yWin(double y) {
	// double size = Math.min(_size.width, _size.height);
	// return (int) (size - (y - _minY) / (_maxY - _minY) * size);
	// }

	/**
	 * Get the size of the memory trajectory drawn
	 * 
	 * @return _memSize
	 */
	// public int getMemorySize() {
	// return _endX.getBufferSize();
	// }
	//
	// /**
	// * Set the size of the memory trajectory drawn
	// */
	//
	// /**
	// * Empty the memory trajectory
	// */
	// public void resetMemory() {
	// _endX.clear();
	// _endY.clear();
	// }
	//
	// private void addEndPoint(double x, double y) {
	// _endX.add(x);
	// _endY.add(y);
	// }
	//
	// /**
	// * Set the goal position
	// *
	// * @param x
	// * @param y
	// */
	// public void setGoal(double x, double y) {
	// this._goalX = x;
	// this._goalY = y;
	// }

}
