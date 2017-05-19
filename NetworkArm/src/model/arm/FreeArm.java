package src.model.arm;

import java.util.Arrays;
import java.util.Observable;

import Jama.Matrix;
import src.model.LinkedEnvironment;
import src.model.network.Neuron;

public class FreeArm extends Observable {

	public double epaule, coude;
	public float arm1, arm2;

	double x, y;

	Arm arm;

	NeuroControl[] _nc = new NeuroControl[6];

	private String last_command;

        
        public double getPosX() {
            return x;
	}

	public double getPosY() {
            return y;
	}
        
        
	public int midX() {

		int rep = 0;
		rep = (int) (Math.cos(Math.toRadians(epaule)) * arm1);
		return rep;
	}

	public int midY() {

		int rep = 0;
		rep = (int) (Math.sin(Math.toRadians(epaule)) * arm1);
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
                updatePosition();

	}

	public FreeArm(int ep, int cou, int l1, int l2) {

		epaule = ep;
		coude = cou;
		arm1 = l1;
		arm2 = l2;
                updatePosition();
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

        
        public void apply(Neuron goal) {
            this.arm1 = goal.getWeight(2)*150;
            this.arm2 = goal.getWeight(3)*150;
            this.coude = goal.getWeight(4)*360;
            this.epaule = goal.getWeight(5)*360;
            
            updatePosition();
            setChanged();
            notifyObservers();         
            
            try {
                    Thread.sleep(200);
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }
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
                        
                        updatePosition();
                        
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

                if (cou > coude)
                    coude++;
		else if(cou != coude)
                    coude--;
            
                if (ep > epaule) 
                    epaule++;
                else if(ep != epaule)
                    epaule--;

		
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

    private void updatePosition() {
        
        x = (endX()+300.0)/600.0;
        y = (endY()+300.0)/600.0;
        
    }

}
