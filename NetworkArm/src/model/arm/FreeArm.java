package model.arm;

import java.util.Arrays;
import java.util.Observable;

import Jama.Matrix;
import model.LinkedEnvironment;

public class FreeArm extends Observable {

	public double epaule, coude;
	public double arm1, arm2;

	int x, y;

	Arm arm;

	NeuroControl[] _nc = new NeuroControl[6];

	private String last_command;

	public int midX() {

		int rep = 0;
		rep = (int) (Math.cos(Math.toRadians(epaule)) * arm1);
		x = rep;
		return rep;
	}

	public int midY() {

		int rep = 0;
		rep = (int) (Math.sin(Math.toRadians(epaule)) * arm1);
		y = rep;
		return rep;
	}

	public int endX() {

		int rep = 0;
		rep = (int) (Math.cos(Math.toRadians(coude)) * arm2);
		rep = rep + midX();
		return rep;
	}

	public int endY() {

		int rep = 0;
		rep = (int) (Math.sin(Math.toRadians(coude)) * arm2);
		rep = rep + midY();
		return rep;
	}

	public FreeArm() {

		epaule = 0;
		coude = 0;
		arm1 = 100;
		arm2 = 100;

	}

	public FreeArm(int ep, int cou, int l1, int l2) {

		epaule = ep;
		coude = cou;
		arm1 = l1;
		arm2 = l2;

	}

	public void setup(double ang0, double ang1) {
		// setup arm with no angular speed
		// double[] ang = { ang0, ang1 };
		// _arm.setArmPos(ang);
		// _arm.setArmSpeed(new Matrix(1, 2)); // Matrix at zero by default
		//
		// // setup muscles length
		// // _muscles.computeTorque(new Matrix(1,6), _arm.getArmPos(),
		// // _arm.getArmSpeed());
		//
		// // No neural activity
		// for (int i = 0; i < _nc.length; i++) {
		// _nc[i].setAct(0.0);
		// _nc[i].setU(0.0);
		// }

		last_command = "RESET";

		// Observable
		setChanged();
		notifyObservers();

	}

	public boolean apply(FreeArm goal) {

		// Matrix previous_pos = new Matrix(0, 0);
		// Matrix current_pos = _arm.getArmPos();

		// last_command = matrixToString(comm);

		double elapsed_time = 0.0;

		boolean arret = false;

		while (!arret) {

			applyCommand((int) goal.epaule, (int) goal.coude, (int) goal.arm1, (int) goal.arm2);
			// elapsed_time += dt;

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// previous_pos = current_pos;
			// current_pos = _arm.getArmPos();

			if (goal.endX() == endX()) {
				if (goal.endY() == endY()) {
					arret = true;
				}
			}

			// System.out.println(this.midX() + " " + goal.midX() + " | " +
			// this.midY() + " " + goal.midY());
		}

		return true;

	}

	public void applyCommand2(int ep, int cou, int l1, int l2) {

		if (ep != epaule) {
			if (ep >= epaule) {
				epaule++;
			} else {
				epaule--;
			}
		}

		if (cou != coude) {
			if (cou >= coude) {
				coude++;
			} else {
				coude--;
			}
		}

		if (l1 != arm1) {
			if (l1 > arm1) {
				arm1++;
			} else {
				arm1--;
			}
		}

		if (l2 != arm2) {
			if (l2 > arm2) {
				arm2++;
			} else {
				arm2--;
			}
		}

		// for (int i = 0; i < _nc.length; i++) {
		// _nc[i].applyCommand(comm.get(0, i), dt);
		// _act.set(0, i, _nc[i].getAct());
		// }
		//
		// // Observable
		setChanged();
		notifyObservers();
		// return applyActivation(_act, dt);
	}

	public void applyCommand(int ep, int cou, int l1, int l2) {

		if (ep != epaule) {
			if (ep >= epaule) {
				epaule++;
			} else {
				epaule--;
			}
		}

		if (cou != coude) {
			if (cou >= coude) {
				coude++;
			} else {
				coude--;
			}
		}

		if (l1 != arm1) {
			if (l1 > arm1) {
				arm1++;
			} else {
				arm1--;
			}
		}

		if (l2 != arm2) {
			if (l2 > arm2) {
				arm2++;
			} else {
				arm2--;
			}
		}

		// for (int i = 0; i < _nc.length; i++) {
		// _nc[i].applyCommand(comm.get(0, i), dt);
		// _act.set(0, i, _nc[i].getAct());
		// }
		//
		// // Observable
		setChanged();
		notifyObservers();
		// return applyActivation(_act, dt);
	}

	public void change() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "FreeArm [epaule=" + epaule + ", coude=" + coude + ", arm1=" + arm1 + ", arm2=" + arm2 + ", x=" + x
				+ ", y=" + y + ", _arm=" + arm + ", _nc=" + Arrays.toString(_nc) + ", last_command=" + last_command
				+ "]";
	}

}
